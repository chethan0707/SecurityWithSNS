package com.chethan.mongosecurity.securityconfig

import com.chethan.mongosecurity.customfilters.CustomAuthenticationFilter
import com.chethan.mongosecurity.customfilters.CustomAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(http: HttpSecurity?) {
        val customAuthenticationFilter = CustomAuthenticationFilter(authenticationManagerBean())
        customAuthenticationFilter.setFilterProcessesUrl("/api/login")
        http?.csrf()?.disable()
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http?.authorizeRequests()?.antMatchers("/api/login/**")?.permitAll()
        http?.authorizeRequests()?.antMatchers(HttpMethod.GET, "/api/getUsers/**")
            ?.hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
        http?.authorizeRequests()?.antMatchers(HttpMethod.POST, "/api/saveUser/**")?.hasAnyAuthority("ROLE_ADMIN")
        http?.authorizeRequests()?.antMatchers(HttpMethod.POST, "/api/saveRole/**")?.hasAnyAuthority("ROLE_ADMIN")
        http?.authorizeRequests()?.antMatchers(HttpMethod.POST, "/api/addRole/**")?.hasAuthority("ROLE_ADMIN")
        http?.authorizeRequests()?.anyRequest()?.authenticated()
        http?.addFilter(customAuthenticationFilter)
        http?.addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}