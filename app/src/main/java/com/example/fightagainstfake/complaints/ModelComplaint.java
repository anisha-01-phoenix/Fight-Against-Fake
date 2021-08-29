package com.example.fightagainstfake.complaints;

public class ModelComplaint {

    String complainId,complaintTitle,datetime,proof,status,uid,proofurl,username;

    public ModelComplaint(String complainId, String complaintTitle, String datetime, String proof, String status, String uid, String proofurl, String username) {
        this.complainId = complainId;
        this.complaintTitle = complaintTitle;
        this.datetime = datetime;
        this.proof = proof;
        this.status = status;
        this.uid = uid;
        this.proofurl = proofurl;
        this.username = username;
    }

    public ModelComplaint() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProofurl() {
        return proofurl;
    }

    public void setProofurl(String proofurl) {
        this.proofurl = proofurl;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
