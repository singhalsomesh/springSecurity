package com.example.jwtTokenSpring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "notice_details")
public class Notice {

    @Id
    @Column(name = "notice_id")
    private long noticeId;

    @Column(name = "notice_summary")
    private String noticeSummary;

    @Column(name = "notice_details")
    private String noticeDetails;

    @Column(name = "notic_beg_dt")
    private Date noticBegDt;

    @Column(name = "notic_end_dt")
    private Date noticEndDt;

    @JsonIgnore
    @Column(name = "create_dt")
    private Date createDt;

    @JsonIgnore
    @Column(name = "update_dt")
    private Date updateDt;

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeSummary() {
        return noticeSummary;
    }

    public void setNoticeSummary(String noticeSummary) {
        this.noticeSummary = noticeSummary;
    }

    public String getNoticeDetails() {
        return noticeDetails;
    }

    public void setNoticeDetails(String noticeDetails) {
        this.noticeDetails = noticeDetails;
    }

    public Date getNoticBegDt() {
        return noticBegDt;
    }

    public void setNoticBegDt(Date noticBegDt) {
        this.noticBegDt = noticBegDt;
    }

    public Date getNoticEndDt() {
        return noticEndDt;
    }

    public void setNoticEndDt(Date noticEndDt) {
        this.noticEndDt = noticEndDt;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "noticeId=" + noticeId +
                ", noticeSummary='" + noticeSummary + '\'' +
                ", noticeDetails='" + noticeDetails + '\'' +
                ", noticBegDt=" + noticBegDt +
                ", noticEndDt=" + noticEndDt +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                '}';
    }
}