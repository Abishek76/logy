package com.drnds.titlelogy.model.vendor;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorOrderQueue {
    private String subclient;
    private String oderno;
    private String status;
    private String producttype;
    private String state;
    private String barrowername;
    private String countytype;
    private String ordertask;
    private String Order_Id;
    private String OrderPriority;
    private String subId;
    private String clintId;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClintId() {
        return clintId;
    }

    public void setClintId(String clintId) {
        this.clintId = clintId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    private String stateID;
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPropertyaddress() {
        return propertyaddress;
    }

    public void setPropertyaddress(String propertyaddress) {
        this.propertyaddress = propertyaddress;
    }

    private String county;
    private String propertyaddress;
    public String getSubclient() {
        return subclient;
    }

    public void setSubclient(String subclient) {
        this.subclient = subclient;
    }

    public String getOderno() {
        return oderno;
    }

    public void setOderno(String oderno) {
        this.oderno = oderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBarrowername() {
        return barrowername;
    }

    public void setBarrowername(String barrowername) {
        this.barrowername = barrowername;
    }

    public String getCountytype() {
        return countytype;
    }

    public void setCountytype(String countytype) {
        this.countytype = countytype;
    }

    public String getOrdertask() {
        return ordertask;
    }

    public void setOrdertask(String ordertask) {
        this.ordertask = ordertask;
    }

    public String getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(String order_Id) {
        Order_Id = order_Id;
    }

    public String getOrderPriority() {
        return OrderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        OrderPriority = orderPriority;
    }

}
