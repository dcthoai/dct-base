package com.dct.base.web.rest;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.ResultConstants;
import com.dct.base.dto.request.RegisterRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.entity.Account;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.service.AccountService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);
    private static final String ENTITY_NAME = "AuthResource";
    private final AccountService accountService;

    public AuthResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public BaseResponseDTO register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        log.debug("REST request to create an account. POST: /api/auth/register");
        Account account = accountService.createUserAccount(requestDTO);

        if (Objects.isNull(account) || Objects.isNull(account.getId()) || account.getId() < 1)
            throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.REGISTER_FAILED);

        return new BaseResponseDTO(
            HttpStatusConstants.CREATED,
            HttpStatusConstants.STATUS.SUCCESS,
            ResultConstants.REGISTER_SUCCESS,
            account
        );
    }
}
