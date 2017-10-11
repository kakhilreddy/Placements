package com.cse.jntu.placements.models;

/**
 * Created by AKHIL on 07-10-2017.
 */

public class Comment {
    private String comment;
    private String username;
    private String timestamp;
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Comment()
    {

    }
    public Comment(String docId,String comment,String username,String timestamp)
    {
        this.docId=docId;
        this.comment=comment;
        this.username=username;
        this.timestamp=timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
