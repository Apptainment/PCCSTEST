package com.sudhir.test.pccstest;

/**
 * Created by pc on 3/29/2018.
 */

public class AppointmentList {

    String customerdetails, customeraddress, warrantydetail;
    String customerPostcode, customerMobile;

    public AppointmentList(String customerdetails, String customeraddress, String warrantydetail, String customerPostcode, String customerMobile) {
        this.customerdetails = customerdetails;
        this.customeraddress = customeraddress;
        this.warrantydetail = warrantydetail;
        this.customerPostcode = customerPostcode;
        this.customerMobile = customerMobile;
    }

    public String getCustomerdetails() {
        return customerdetails;
    }

    public void setCustomerdetails(String customerdetails) {
        this.customerdetails = customerdetails;
    }

    public String getCustomeraddress() {
        return customeraddress;
    }

    public void setCustomeraddress(String customeraddress) {
        this.customeraddress = customeraddress;
    }

    public String getWarrantydetail() {
        return warrantydetail;
    }

    public void setWarrantydetail(String warrantydetail) {
        this.warrantydetail = warrantydetail;
    }
}
