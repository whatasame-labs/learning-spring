# Authentication

## Basic hierarchy

`SecurityContextHolder` is where Spring Security stores the details of who is authenticated.

`SecurityContext` contains the `Authentication` of the currently authenticated user. It is obtained from
the `SecurityContextHolder`.

`Authentication` represent the currently authenticated user. It contains principal, credentials and authorities.

* principal: Identifies the user. When authenticating with a username/password this is often an instance
  of `UserDetails`.

* credentials: Often a password. In many cases, this is cleared after the user is authenticated, to ensure that it is
  not leaked.

* authorities: The `GrantedAuthority` instances are high-level permissions the user is granted. Two examples are roles
  and scopes.

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/securitycontextholder.png)

## Range of `SecurityContext`

By default, `SecurityContextHolder` uses a `ThreadLocal` to store details, which means that the `SecurityContext` is
always available to methods in the same thread, even if the `SecurityContext` is not explicitly passed around as an
argument to those methods.

It means if you have a new thread, it doesn't have the `SecurityContext` of the parent thread. So if you want to pass
the `SecurityContext` to the new thread, you can configure SecurityContextHolder with a strategy on startup to specify
how you would like the context to be stored.

> At thread local level, Spring Security’s FilterChainProxy ensures that the SecurityContext is always cleared.

## `AuthenticationManager`

AuthenticationManager is the API that defines how Spring Security’s Filters perform authentication.

### `ProviderManager` and `AuthenticationProvider`

`ProviderManager` is the default implementation of `AuthenticationManager`. It has a list of `AuthenticationProvider`.

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanager-parent.png)

It delegates to a list of `AuthenticationProvider`. As like filter chain, each `AuthenticationProvider` determine
authentication is success, failure or can't make
decision and pass to downstream provider.

If none of provider can authentication, it will throw `ProviderNotFoundException` which is
special  `AuthenticationException` that indicates that the `ProviderManager` was not configured to support the type
of `Authentication` that was passed into it.

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanager.png)

By default, `ProviderManager` tries to clear any sensitive credentials information from the Authentication object that is
returned by a successful authentication request. This prevents information, such as passwords, being retained longer
than necessary in the HttpSession.

## Authentication flow

`AbstractAuthenticationProcessingFilter` is used as a base Filter for authenticating a user’s credentials.

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/abstractauthenticationprocessingfilter.png)

1. When the user submits their credentials, the `AbstractAuthenticationProcessingFilter` creates an `Authentication`
   from
   the `HttpServletRequest` to be authenticated. The type of `Authentication` created depends on the subclass of
   `AbstractAuthenticationProcessingFilter`.

2. Next, the `Authentication` is passed into the `AuthenticationManager` to be authenticated.

   > It means some of `Authentication` might has wrong credentials.

3. If authentication fails, then Failure.

* The `SecurityContextHolder` is cleared out.

* `RememberMeServices.loginFail` is invoked. If remember me is not configured, this is a no-op. See the rememberme
  package.

  > `RememberMeServices` is used to handle remember me authentication like to store which imformation is used to
  authenticate.

* `AuthenticationFailureHandler` is invoked. See the AuthenticationFailureHandler interface.

4. If authentication is successful, then Success.

* `SessionAuthenticationStrategy` is notified of a new login. See the `SessionAuthenticationStrategy` interface.

* The `Authentication` is set on the `SecurityContextHolder`. Later, if you need to save the `SecurityContext` so that it
  can be automatically set on future requests, `SecurityContextRepository#saveContext` must be explicitly invoked. See the
  `SecurityContextHolderFilter` class.
    
    > Because `SecurityContextHolder` is thread local mode as default, so it must be explicitly saved to session.

* `RememberMeServices.loginSuccess` is invoked. If remember me is not configured, this is a no-op. See the rememberme
  package.

* `ApplicationEventPublisher` publishes an `InteractiveAuthenticationSuccessEvent`.

* `AuthenticationSuccessHandler` is invoked. See the `AuthenticationSuccessHandler` interface.

## References

* [Servlet Authentication Architecture :: Spring Security](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authentication)
