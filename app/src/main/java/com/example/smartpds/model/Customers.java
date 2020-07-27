package com.example.smartpds.model;

public class Customers {
    boolean accountStatus;
    String address;
    String city;
    String email;
    String fname;
    boolean  kycDone;
    String lname;
    String mobile;
    long pincode;
    String shopname;
    String state;
    long walletAmmount;
    public boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getKycDone() {
        return kycDone;
    }

    public void setKycDone(boolean kycDone) {
        this.kycDone = kycDone;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getWalletAmmount() {
        return walletAmmount;
    }

    public void setWalletAmmount(long walletAmmount) {
        this.walletAmmount = walletAmmount;
    }

    public Customers() {
    }

    public Customers(boolean accountStatus, String address, String city, String email, String fname, boolean kycDone, String lname, String mobile, long pincode, String shopname, String state, long walletAmmount) {
        this.accountStatus = accountStatus;
        this.address = address;
        this.city = city;
        this.email = email;
        this.fname = fname;
        this.kycDone = kycDone;
        this.lname = lname;
        this.mobile = mobile;
        this.pincode = pincode;
        this.shopname = shopname;
        this.state = state;
        this.walletAmmount = walletAmmount;
    }






    public String getFname() {
        return fname;
    }

    public void setFname(String fname ) {
        this.fname = fname;
    }

}




