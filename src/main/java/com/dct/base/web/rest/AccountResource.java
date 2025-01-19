package com.dct.base.web.rest;

import com.dct.base.common.MapperUtils;
import com.dct.base.dto.response.AccountDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.entity.Account;
import com.dct.base.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AccountResource {

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{userID}")
    public BaseResponseDTO getAccountInfo(@PathVariable int userID) {
        log.debug("REST request to get account info. GET: /api/users/{}", userID);
        Account account = accountService.findByID(userID);
        AccountDTO accountDTO = MapperUtils.mapObject(account, AccountDTO.class);
        return new BaseResponseDTO(accountDTO);
    }
}
