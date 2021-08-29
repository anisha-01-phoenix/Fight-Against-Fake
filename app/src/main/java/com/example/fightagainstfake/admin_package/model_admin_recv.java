package com.example.fightagainstfake.admin_package;

public class model_admin_recv {
    String status,complaintId,title,date,username,uid,proof, proofurl;

    public model_admin_recv(String status, String complaintId, String title, String date, String username, String uid, String proof, String proofurl) {
        this.status = status;
        this.complaintId = complaintId;
        this.title = title;
        this.date = date;
        this.username = username;
        this.uid = uid;
        this.proof = proof;
        this.proofurl = proofurl;
    }

    public model_admin_recv() {
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getProofurl() {
        return proofurl;
    }

    public void setProofurl(String proofurl) {
        this.proofurl = proofurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
