package me.jobayeralmahmud.library.enums;

import lombok.Getter;

@Getter
public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    WEBP("webp"),
    BMP("bmp"),
    SVG("svg"),
    TIFF("tiff"),
    TIF("tif"),
    ICO("ico"),
    PDF("pdf");

    private final String value;

    FileExtension(String value) {
        this.value = value;
    }
}
