package me.jobayeralmahmud.library.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.validation.ExistsId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExistsIdValidator implements ConstraintValidator<ExistsId, Serializable> {

    private final ApplicationContext applicationContext;
    private Class<?> entity;

    @Override
    public void initialize(ExistsId constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Serializable id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) return true;
        var repositoryName = entity.getSimpleName() + "Repository";
        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);

        JpaRepository<Object, Serializable> repository =
                (JpaRepository<Object, Serializable>) applicationContext.getBean(repositoryName);
        try {
            Optional<Object> entity = repository.findById(id);
            return entity.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
