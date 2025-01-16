package com.dct.base.aop;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.exception.BaseAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class CheckAuthorizeAspect {

    private static final Logger log = LoggerFactory.getLogger(CheckAuthorizeAspect.class);
    private static final String ENTITY_NAME = "CheckAuthorizeAspect";

    public CheckAuthorizeAspect() {
        log.debug("Configured CheckAuthorizeAspect for handle authenticate method");
    }

    /**
     * {@link Pointcut} specifies where (in which method, class, or annotation) AOP logic will be applied<p>
     * This function only serves to name and define the pointcut, it does not execute any logic<p>
     * Reusability: If you need to use the same pointcut in multiple places
     * (for example, in @{@link Around}, @{@link Before}, or @{@link After} annotations),
     * you can simply reference this function
     */
    @Pointcut("@annotation(com.dct.base.aop.CheckAuthorize)") // Full path to CustomAnnotation class
    public void checkAuthorizeByJwt() {}

    @Around("checkAuthorizeByJwt()")
    public Object aroundCheckAuthorizeByJwt(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        CheckAuthorize annotation = methodSignature.getMethod().getAnnotation(CheckAuthorize.class);

        List<String> requiredAuthorities = Arrays.asList(annotation.authorities().split(","));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> userAuthorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (userAuthorities.containsAll(requiredAuthorities))
            return pjp.proceed();

        try {
            ServletRequestAttributes servletRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(servletRequest).getRequest();
            String url = request.getRequestURL().toString();
            String username = Objects.nonNull(authentication.getName()) ? authentication.getName() : "Anonymous";
            log.error("User '{}' does not have any permission to access this function: {}", username, url);
        } catch (Exception ignore) {}

        throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.FORBIDDEN);
    }
}
