package me.jobayeralmahmud.library.enums;

import lombok.Getter;

@Getter
public enum MimeTypes {
    // Images
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    WEBP("image/webp"),
    BMP("image/bmp"),
    SVG("image/svg+xml"),
    TIFF("image/tiff"),
    ICO("image/x-icon"),

    // Documents
    PDF("application/pdf");

    private final String value;

    MimeTypes(String value) {
        this.value = value;
    }
}
