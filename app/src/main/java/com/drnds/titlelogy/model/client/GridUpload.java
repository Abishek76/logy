package com.drnds.titlelogy.model.client;

/**
 * Created by ajithkumar on 8/7/2017.
 */

public class GridUpload {
    private String documentType;
    private String description;
    private String uploadedDate;
    private String doumentpath;

    public String getDoumentpath() {
        return doumentpath;
    }

    public void setDoumentpath(String doumentpath) {
        this.doumentpath = doumentpath;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}