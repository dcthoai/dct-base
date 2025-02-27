package com.dct.base.repositories;

import com.dct.base.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findAccountByUsername(String username);
    Account findAccountByEmail(String email);
    Account findAccountByUsernameOrEmail(String username, String email);
}
