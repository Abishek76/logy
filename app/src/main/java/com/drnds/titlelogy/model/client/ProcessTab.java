package com.drnds.titlelogy.model.client;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class ProcessTab {


    private String orderno;
    private String title ;

    public ProcessTab(String orderno, String title) {
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
