package com.drnds.titlelogy.model.vendor;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorPendingTab {
    private String penorderno;
    private String pentitle;

    public VendorPendingTab(String penorderno, String pentitle) {
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
