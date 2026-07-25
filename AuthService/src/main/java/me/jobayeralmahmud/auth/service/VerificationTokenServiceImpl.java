package me.jobayeralmahmud.auth.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.entity.User;
import me.jobayeralmahmud.auth.entity.VerificationToken;
import me.jobayeralmahmud.auth.enums.VerificationType;
import me.jobayeralmahmud.auth.repository.VerificationTokenRepository;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    @Override
    public void addVerificationToken(User user, String verificationToken) {

        var token = VerificationToken.builder()
                .user(user)
                .tokenType(VerificationType.EMAIL_VERIFICATION.name())
                .token(verificationToken)
                .build();
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public UUID updateVerificationTokenStatus(String token) {

        VerificationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourcesNotFoundException("Token not found"));

        confirmationToken.verifyToken(LocalDateTime.now());

        return confirmationToken.getUser().getId();
    }
}