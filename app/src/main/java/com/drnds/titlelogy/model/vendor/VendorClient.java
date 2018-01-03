package com.drnds.titlelogy.model.vendor;

/**
 * Created by Ajith on 9/25/2017.
 */

public class VendorClient {
    private String companyname;
    private String state;
    private String county;
    private String clientcode;
    private String clientUid;
    private String clientId;


    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getCompanyname() {return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getClientcode() {
        return clientcode;
    }

    public void setClientcode(String subprocesscode) {
        this.clientcode = subprocesscode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}


