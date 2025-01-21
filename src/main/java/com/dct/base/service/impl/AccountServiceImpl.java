package com.dct.base.service.impl;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.request.RegisterRequestDTO;
import com.dct.base.entity.Account;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.repositories.AccountRepository;
import com.dct.base.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final String ENTITY_NAME = "AccountServiceImpl";
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account isExistsUser(RegisterRequestDTO requestDTO) {
        return isExistsUser(requestDTO.getUsername(), requestDTO.getEmail());
    }

    @Override
    public Account isExistsUser(String username, String email) {
        return accountRepository.findAccountByUsernameOrEmail(username, email);
    }

    @Override
    public Account findUserByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account findByID(int userID) {
        return accountRepository.findById(userID).orElse(null);
    }

    @Override
    public Account findUserByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account createUserAccount(RegisterRequestDTO requestDTO) {
        log.debug("Creating a new account for user: {}", requestDTO.getUsername());
        String username = requestDTO.getUsername();
        String email = requestDTO.getEmail();
        Account account = isExistsUser(requestDTO);

        if (Objects.isNull(account)) {
            String rawPassword = requestDTO.getPassword();
            String hashedPassword = passwordEncoder.encode(rawPassword);

            account = new Account(username, hashedPassword);
            account.setEmail(email);
            account.setRoles(SecurityConstants.ROLES.ROLE_USER);

            return accountRepository.save(account);
        }

        throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.ACCOUNT_EXISTED);
    }

    @Override
    public Account createAdminAccount() {
        return null;
    }

    @Override
    public Account update(Account account) {
        log.debug("Updating account: {}", account.getUsername());

        if (accountRepository.existsById(account.getId()))
            return accountRepository.save(account);

        throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.ACCOUNT_NOT_EXISTED);
    }
}
