package com.drnds.titlelogy.model.client;

/**
 * Created by Ajithkumar on 6/21/2017.
 */

public class CreateUser {
    private String fname;
    private String lname;
    private String email;
    private String altemail;
    private String clientUid;
    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltemail() {
        return altemail;
    }

    public void setAltemail(String altemail) {
        this.altemail = altemail;
    }
}
