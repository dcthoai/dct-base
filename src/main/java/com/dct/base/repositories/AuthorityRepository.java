package com.dct.base.repositories;

import com.dct.base.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Query(
        value = "select rp.permission_code " +
                "from role_permission rp " +
                "join user_role ur " +
                "on rp.role_ID = ur.role_ID " +
                "where ur.user_ID = ?1",
        nativeQuery = true
    )
    Set<String> findAllByUserID(int userID);
}
