# Architecture

## Basic

Spring Security’s Servlet support is based on Servlet Filters.

![](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/filterchain.png)

The servlet container creates a filter chain that contains both Filters and Servlets. This filter chain processes
the `HttpServletRequest`.

In the case of Spring MVC, the Servlet is an instance of `DispatcherServlet`. One servlet can handle a
single `HttpServletRequest`
and `HttpServletResponse`. However, more than one filter can be used to:

- Prevent downstream `Filter` and `Servlet` from being invoked.
- Modify the request or response used by downstream `Filter` and `Servlet`.

## Why `DelegatingFilterProxy`?

A `Filter` is part of the Servlet specification, not Spring. Therefore, the servlet container only allows filters to be
registered according to its own standard and does not directly support registering Spring-defined beans.

To address this, Spring Security provides `DelegatingFilterProxy`, which delegates filtering tasks to a
Spring-managed bean that implements the `javax.servlet.Filter` interface.

You can register `DelegatingFilterProxy` as a `Filter` in the servlet container. It then delegates all filtering
operations to a Spring-managed bean that implements the `Filter` interface.

![](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/delegatingfilterproxy.png)

Additionally, `DelegatingFilterProxy` delays looking up the `Filter` bean until it is needed (lazy initialization).
Spring
typically uses a `ContextLoaderListener` to load the Spring Beans, which is done only after the Filter instances need
to be registered.

## `FilterChainProxy` and `SecurityFilterChain`

`FilterChainProxy` is a bean wrapped by `DelegatingFilterProxy`. It is a special Filter provided by Spring Security
that allows delegating to multiple Filter instances through `SecurityFilterChain`.

Security filters of `SecurityFilterChain` are typically beans and are registered with the `FilterChainProxy` instead
of `DelegatingFilterProxy`.

An advantage of `FilterChainProxy` is:

1. It is the starting point of the Spring Security Filter Chain.

Therefore, if you want to debug the Spring Security Filter Chain, setting a breakpoint in `FilterChainProxy` is a good
idea.

![](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/securityfilterchain.png)

2. It provides more flexible invocation of `SecurityFilterChain` based on `HttpServletRequest`.

While `Filter` instances are invoked based on URL, `SecurityFilterChain` instances are invoked based on **anything** in
the `HttpServletRequest`.

![](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/multi-securityfilterchain.png)

`FilterChainProxy` determines which `SecurityFilterChain` should be used based on the `HttpServletRequest`.

`SecurityFilterChain` has its own security `Filter` instances, which can be zero or more.

## Tips

* Order of Security filters

  As with standard Servlet Filters, Spring Security Filters are ordered. To view the order of Spring Security Filters,
  check the INFO log level.

  However, this is bound to the `DefaultSecurityFilterChain`, so if you want to see the order of a custom filter chain,
  you need to configure your application to log security events.

* If you want to invoke a specific filter only once per request, extend the `OncePerRequestFilter` class rather than
  implementing the `Filter` interface.
  > Multiple invocations of a `Filter` can occur in cases of forward or include requests, etc.

* If multiple filters have the same order, it means that they have no deterministic order. It does not mean that they
  override
  each other.

* When you declare a `Filter` as a bean using the `@Bean` annotation or `@Component` annotation, it will be invoked
  twice,
  once by the container and once by Spring Security, even in different orders.

* If you want to change the log level,
  see [here](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-logging).

## Handling security exceptions

To translate security exceptions into HTTP responses, Spring Security provides the `ExceptionTranslationFilter`.

![](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/exceptiontranslationfilter.png)

`ExceptionTranslationFilter` is a security filter inserted into `FilterChainProxy`.

1. `ExceptionTranslationFilter` invokes `FilterChain.doFilter(request, response)` to continue processing the
   application.

2. If a user is not authenticated or an `AuthenticationException` is thrown, the authentication process starts.

    - `SecurityContextHolder` is cleared.
    - `HttpServletRequest` is stored in `RequestCache` for use after successful authentication.
    - `AuthenticationEntryPoint` is used to request user credentials, such as redirecting to the login page.

3. If an `AccessDeniedException` is thrown, access is denied. The `AccessDeniedHandler` is invoked to handle the access
   denial.

> If a user tries to access a resource that requires authentication but is not authenticated, `AuthenticationFilter`
> will
> throw an exception. Then, `ExceptionTranslationFilter` handles it and starts the authentication process.

## Summary

- Spring Security operates beneath the Servlet Filters.
- `DelegateFilterProxy` wraps Spring-managed `Filter` beans as standard Servlet Filters.
- `FilterChainProxy` is the starting point of the **Spring Security** filter chain.
- `FilterChainProxy` determines which `SecurityFilterChain` should be used.
- `SecurityFilterChain` has its own security `Filter` instances.
- `RequestCache` is used to store `HttpServletRequest` for use after successful authentication.

## Reference

- [Architecture :: Spring Security](https://docs.spring.io/spring-security/reference/servlet/architecture.html)
