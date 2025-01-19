package com.dct.base.repositories;

import com.dct.base.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Query(
        value = "select a.authority_code " +
                "from account_authority a " +
                "where a.user_ID = ?1",
        nativeQuery = true
    )
    Set<String> findAllByUserID(int userID);
}
