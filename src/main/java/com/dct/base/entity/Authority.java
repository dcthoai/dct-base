package com.dct.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Each record corresponds to a permission in the application, and one account can match multiple permissions
 * @author thoaidc
 */
@Entity
@Table(name = "authority")
@DynamicUpdate // Hibernate only updates the changed columns to the database instead of updating the entire table
@SuppressWarnings("unused")
public class Authority extends AbstractAuditingEntity {

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "code", length = 45, nullable = false, unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_ID", nullable = false)
    private int parentID;

    @Column(name = "parent_code", nullable = false)
    private String parentCode;

    public Authority() {}

    public Authority(String name, String code, int parentID, String parentCode, String description) {
        this.name = name;
        this.code = code;
        this.parentID = parentID;
        this.parentCode = parentCode;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
