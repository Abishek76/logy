package com.drnds.titlelogy.model.client;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class PendingTab {
    private String penorderno;
    private String pentitle;

    public PendingTab(String penorderno, String pentitle) {
        this.penorderno = penorderno;
        this.pentitle = pentitle;
    }

    public String getPenorderno() {
        return penorderno;
    }

    public void setPenorderno(String penorderno) {
        this.penorderno = penorderno;
    }

    public String getPentitle() {
        return pentitle;
    }

    public void setPentitle(String pentitle) {
        this.pentitle = pentitle;
    }
}