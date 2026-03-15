package me.jobayeralmahmud.library.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.jobayeralmahmud.library.enums.FileExtension;
import me.jobayeralmahmud.library.enums.MimeTypes;
import me.jobayeralmahmud.library.validation.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {

    private Long maxFileSize;
    private Set<String> mimeTypes;
    private List<String> extensions;

    @Override
    public void initialize(File constraintAnnotation) {
        mimeTypes = Arrays.stream(constraintAnnotation.mimeTypes())
                .map(MimeTypes::getValue)
                .collect(Collectors.toSet());
        extensions = Arrays.stream(constraintAnnotation.extensions())
                .map(FileExtension::getValue)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        maxFileSize = constraintAnnotation.maxSize() * 1024 * 1024;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            addConstraintViolation(context, "File must not be empty");
            return false;
        }

        if (!isSupportedContentType(file.getContentType())) {
            addConstraintViolation(context, "Invalid file type. Allowed types: " + String.join(", ", mimeTypes));
            return false;
        }

        if (!isValidExtension(file.getOriginalFilename())) {
            addConstraintViolation(context,
                    "Invalid file extension. Allowed extensions: " + String.join(", ", extensions));
            return false;
        }

        if (!isValidFileSize(file.getSize())) {
            addConstraintViolation(context,
                    "File size exceeds the maximum limit of " + (maxFileSize / (1024 * 1024)) + "MB");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean isSupportedContentType(String fileContentType) {
        return fileContentType != null && mimeTypes.contains(fileContentType);
    }

    private boolean isValidFileSize(Long fileSize) {
        return fileSize <= maxFileSize;
    }

    private boolean isValidExtension(String fileName) {
        if (fileName == null)
            return false;
        var extension = getFileExtension(fileName);
        return extensions.contains(extension.toLowerCase());
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        return lastDot == -1 ? "" : fileName.substring(lastDot + 1);
    }
}
