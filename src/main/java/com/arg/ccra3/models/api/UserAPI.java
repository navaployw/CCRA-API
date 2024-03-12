package com.arg.ccra3.models.api;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "API_User")
@EntityListeners(AuditingEntityListener.class)
public class UserAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AID", insertable = false, updatable = false)
    private Long aid;
    @Column(name = "UID", nullable = false)
    private Long uid;
    @Column(name = "GROUPID")
    private Long groupid;
    @Column(name = "GROUPAIID")
    private Long groupaiid;
    @Column(name = "USERID")
    private String userid;
    @Column(name = "SECRETKEY")
    private String secretkey;
    @Column(name = "SECRET_START")
    private Date secret_start;
    @Column(name = "SECRET_END")
    private Date secret_end;
    @Column(name = "THRESHOLD_WEEK")
    private Integer threshold_week;
    @Column(name = "DELETED")
    private Boolean deleted;
    @Column(name = "DELETEDBY")
    private Long deletedby;
    @Column(name = "DELETEDDATE")
    private Date deleteddate;
    @Column(name = "CREATEDBY")
    private Long createdby;    
    @CreatedDate
    @Column(name = "CREATEDDATE")
    private Date createddate;
    @Column(name = "UPDATEBY")
    private Long updateby;
    @Column(name = "UPDATEDATE")
    private Date updatedate;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "THRESHOLD_WEEK_COUNT")
    private Integer threshold_week_count;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public Date getSecret_start() {
        return secret_start;
    }

    public void setSecret_start(Date secret_start) {
        this.secret_start = secret_start;
    }

    public Date getSecret_end() {
        return secret_end;
    }

    public void setSecret_end(Date secret_end) {
        this.secret_end = secret_end;
    }

    public Integer getThreshold_week() {
        return threshold_week;
    }

    public void setThreshold_week(Integer threshold_week) {
        this.threshold_week = threshold_week;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(Long deletedby) {
        this.deletedby = deletedby;
    }

    public Date getDeleteddate() {
        return deleteddate;
    }

    public void setDeleteddate(Date deleteddate) {
        this.deleteddate = deleteddate;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Long getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Long updateby) {
        this.updateby = updateby;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getThreshold_week_count() {
        return threshold_week_count;
    }

    public void setThreshold_week_count(Integer threshold_week_count) {
        this.threshold_week_count = threshold_week_count;
    }

    public Long getGroupaiid() {
        return groupaiid;
    }

    public void setGroupaiid(Long groupaiid) {
        this.groupaiid = groupaiid;
    }

    @Override
    public String toString() {
        return "UserAPI{" + "aid=" + aid + ", uid=" + uid + ", groupid=" + groupid + ", groupaiid=" + groupaiid + ", userid=" + userid + ", secretkey=" + secretkey + ", secret_start=" + secret_start + ", secret_end=" + secret_end + ", threshold_week=" + threshold_week + ", deleted=" + deleted + ", deletedby=" + deletedby + ", deleteddate=" + deleteddate + ", createdby=" + createdby + ", createddate=" + createddate + ", updateby=" + updateby + ", updatedate=" + updatedate + ", disabled=" + disabled + ", threshold_week_count=" + threshold_week_count + '}';
    }
    
}
