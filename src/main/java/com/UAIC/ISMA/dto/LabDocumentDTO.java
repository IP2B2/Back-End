package com.UAIC.ISMA.dto;

import lombok.Data;

@Data
public class LabDocumentDTO {
    private Long id;
    private String filename;
    private String fileType;
    private String version;
    private String filePath;
    private Long labId;
}
