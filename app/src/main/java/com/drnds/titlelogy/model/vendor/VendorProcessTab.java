package com.drnds.titlelogy.model.vendor;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorProcessTab {

    private String orderno;
    private String title ;

    public VendorProcessTab(String orderno, String title) {
        this.orderno = orderno;
        this.title = title;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
