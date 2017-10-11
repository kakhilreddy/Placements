package com.cse.jntu.placements.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AKHIL on 07-10-2017.
 */

public class InterviewExperience implements Serializable {

private Comment []comments;
    private String companyName;
    private boolean isHired;
    private String title;
    private String username;
    private long views;
    private String timestamp;
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public InterviewExperience()
    {

    }
    public InterviewExperience(String docid,String title,String companyName,String username,boolean isHired,long views,String timestamp ,Comment[]comments)
    {
        this.docId=docid;
        this.title=title;
        this.companyName=companyName;
        this.username=username;
        this.isHired=isHired;
        this.views=views;
        this.timestamp=timestamp;
        if(comments!=null)
        { this.comments=new Comment[comments.length];
        int idx=0;
        for(Comment comment : comments)
        {
            this.comments[idx++]=comment;
        }}


    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isHired() {
        return isHired;
    }

    public void setHired(boolean hired) {
        isHired = hired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
