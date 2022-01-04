package com.chethan.mongosecurity.customfilters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.streams.toList

class CustomAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    ) :
    UsernamePasswordAuthenticationFilter(
    ) {
    init{
        this.authenticationManager = authenticationManager
    }

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
        val username: String? = request?.getParameter("username")
        val password = request?.getParameter("password")
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val algorithm = Algorithm.HMAC256("secret".toByteArray())
        val user: User = authResult?.principal as User
        val username = user.username
        val accessToken = JWT.create().withIssuer(request?.requestURL.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 1000))
            .withSubject(username)
            .withClaim("roles", user.authorities.stream().map {
                it.authority
            }.toList())
            .sign(algorithm)
        val refreshToken = JWT.create().withIssuer("localhost:8080/login")
            .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
            .withSubject(username)
            .sign(algorithm)
        response?.setHeader("access_token", accessToken)
        response?.setHeader("refresh_token", refreshToken)
    }
}