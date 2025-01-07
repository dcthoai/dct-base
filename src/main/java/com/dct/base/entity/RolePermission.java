package com.dct.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "role_permission")
@DynamicUpdate // Hibernate only updates the changed columns to the database instead of updating the entire table
public class RolePermission extends AbstractAuditingEntity {

    @Column(name = "role_ID", nullable = false)
    private Integer roleID;

    @Column(name = "role_code", length = 45)
    private String roleCode;

    @Column(name = "permission_ID", nullable = false)
    private Integer permissionID;

    @Column(name = "permission_code", length = 45)
    private String permissionCode;

    @Column(name = "permission_parent_code", length = 45)
    private String permissionParentCode;

    public RolePermission() {}

    public RolePermission(Integer roleID, Integer permissionID) {
        this.roleID = roleID;
        this.permissionID = permissionID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(Integer permissionID) {
        this.permissionID = permissionID;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionParentCode() {
        return permissionParentCode;
    }

    public void setPermissionParentCode(String permissionParentCode) {
        this.permissionParentCode = permissionParentCode;
    }
}
