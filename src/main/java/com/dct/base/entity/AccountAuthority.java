package com.dct.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "account_authority")
@DynamicUpdate // Hibernate only updates the changed columns to the database instead of updating the entire table
@SuppressWarnings("unused")
public class AccountAuthority extends AbstractAuditingEntity {

    @Column(name = "user_ID", nullable = false)
    private Integer userID;

    @Column(name = "authority_ID", nullable = false)
    private Integer authorityID;

    @Column(name = "authority_code", nullable = false)
    private String authorityCode;

    @Column(name = "authority_parent_code")
    private String authorityParentCode;

    public AccountAuthority() {}

    public AccountAuthority(Integer userID, Integer authorityID, String authorityCode) {
        this.userID = userID;
        this.authorityID = authorityID;
        this.authorityCode = authorityCode;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getAuthorityID() {
        return authorityID;
    }

    public void setAuthorityID(Integer authorityID) {
        this.authorityID = authorityID;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getAuthorityParentCode() {
        return authorityParentCode;
    }

    public void setAuthorityParentCode(String authorityParentCode) {
        this.authorityParentCode = authorityParentCode;
    }
}
