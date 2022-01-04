package com.chethan.mongosecurity.customfilters

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Verification
import org.apache.tomcat.util.http.parser.Authorization
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.net.http.HttpHeaders
import java.util.Arrays.stream
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter() : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {


        if (request.servletPath.equals("/api/login") || request.servletPath.equals("/api/tokenrefresh")) {
            filterChain.doFilter(request, response)
        } else {
            val authorizationHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    val algorithm = Algorithm.HMAC256("secret".toByteArray())
                    val token = authorizationHeader.substring("Bearer ".length)
                    val verifier = JWT.require(algorithm).build()
                    val decodedJWT = verifier.verify(token)
                    val username = decodedJWT.subject
                    val roles: Array<String> = decodedJWT.getClaim("roles").asArray(String::class.java)
                    val authorities: MutableList<SimpleGrantedAuthority> = mutableListOf()
                    stream(roles).forEach {
                        authorities.add(SimpleGrantedAuthority(it))
                    }
                    val usernamePasswordAuthenticationToken =
                        UsernamePasswordAuthenticationToken(username, null, authorities)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                    filterChain.doFilter(request, response)
                } catch (exception: Exception) {
                    response.setHeader("error", exception.message.toString())
                    response.status = 403
                }
            } else {
                filterChain.doFilter(request, response)
            }
        }
    }

}
