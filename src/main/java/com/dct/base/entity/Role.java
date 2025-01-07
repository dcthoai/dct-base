package com.dct.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "role")
@DynamicUpdate // Hibernate only updates the changed columns to the database instead of updating the entire table
public class Role extends AbstractAuditingEntity {

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "code", length = 45, nullable = false, unique = true)
    private String code;

    public Role() {}

    public Role(String name, String code) {
        this.name = name;
        this.code = code;
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
}
