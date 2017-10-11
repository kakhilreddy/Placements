package com.cse.jntu.placements.models;

import android.net.Uri;

import java.net.URI;

/**
 * Created by AKHIL on 07-10-2017.
 */

public class User {
    private String UID;
    private  String uname;
    private String email;
private  boolean isVerified;

    public User()
    {

    }
    public User(String uid,String uname,String email,boolean isVerified)
    {
        this.isVerified=isVerified;
        this.UID=uid;
        this.uname=uname;
        this.email=email;

    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
