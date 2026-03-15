package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.entity.VerificationToken;
import me.jobayeralmahmud.enums.VerificationType;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.jwt.Jwt;
import me.jobayeralmahmud.jwt.JwtService;
import me.jobayeralmahmud.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final JwtService jwtService;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public void addVerificationToken(User user, String JwtToken) {

        var token = new VerificationToken();
        token.setUser(user);
        token.setType(VerificationType.EMAIL_VERIFICATION.name());
        token.setToken(JwtToken);
        tokenRepository.save(token);
    }

    @Override
    public UUID updateVerificationTokenStatus(String token) {
        Jwt parseToken = jwtService.parseToken(token);

        VerificationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourcesNotFoundException("Token not found"));

        if (confirmationToken.getVerifiedAt() != null) {
            throw new IllegalArgumentException("Token already verified!");
        }

        if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("This Token is expired please request for new token!");
        }

        confirmationToken.setVerifiedAt(LocalDateTime.now());

        return confirmationToken.getUser().getId();
    }
}
