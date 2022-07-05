package com.fw.cobranca.upload;

import lombok.Data;

@Data
public class UploadInput {
    private String filename;
    private String base64;
    private String mimeType;
}
