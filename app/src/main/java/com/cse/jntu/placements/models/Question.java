package com.cse.jntu.placements.models;

import java.io.Serializable;

/**
 * Created by AKHIL on 09-10-2017.
 */

public class Question  implements Serializable{
    private String docid;
    private String question;
    private String username;
    private long views;

    public Question()
    {

    }
    public Question(String docid,String question,String username,long views)
    {
        this.docid=docid;
        this.question=question;
        this.username=username;
        this.views=views;
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

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
