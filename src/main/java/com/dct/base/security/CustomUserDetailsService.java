package com.dct.base.security;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.entity.Account;
import com.dct.base.repositories.AccountRepository;
import com.dct.base.repositories.AuthorityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;

    public CustomUserDetailsService(AccountRepository accountRepository,
                                    AuthorityRepository authorityRepository) {
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
        log.debug("UserDetailsService 'CustomUserDetailsService' is configured for load user credentials info");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user by username: " + username);
        Account account = accountRepository.findAccountByUsername(username);

        if (Objects.isNull(account))
            throw new UsernameNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND);

        String[] userRoles = account.getRoles().trim().split(",");
        Set<String> userPermissions = authorityRepository.findAllByUserID(account.getId());

        Collection<SimpleGrantedAuthority> userAuthorities = Stream.concat(
            Arrays.stream(userRoles)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new),
            userPermissions.stream()
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toSet());

        return new CustomUserDetails(account, userAuthorities);
    }
}
