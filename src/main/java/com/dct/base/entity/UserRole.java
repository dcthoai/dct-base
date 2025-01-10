package com.dct.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_role")
@DynamicUpdate // Hibernate only updates the changed columns to the database instead of updating the entire table
@SuppressWarnings("unused")
public class UserRole extends AbstractAuditingEntity {

    @Column(name = "role_ID", nullable = false)
    private int roleID;

    @Column(name = "user_ID", nullable = false)
    private int userID;

    public UserRole() {}

    public UserRole(int roleID, int userID) {
        this.roleID = roleID;
        this.userID = userID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
