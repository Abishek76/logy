package com.drnds.titlelogy.model.vendor;

/**
 * Created by Ajith on 11/3/2017.
 */

public class VendorUpload {
    private String documentType;
    private String description;
    private String uploadedDate;
    private String doumentpath;

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

    public String getDoumentpath() {
        return doumentpath;
    }

    public void setDoumentpath(String doumentpath) {
        this.doumentpath = doumentpath;
    }
}
