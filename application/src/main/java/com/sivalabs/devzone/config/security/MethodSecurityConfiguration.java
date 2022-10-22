/*
package com.sivalabs.devzone.config.security;


@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final RoleHierarchy roleHierarchy;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                (DefaultMethodSecurityExpressionHandler) super.createExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }
}
*/
