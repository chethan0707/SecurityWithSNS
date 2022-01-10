package com.chethan.mongosecurity.services

import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sns.model.SubscribeRequest
import com.chethan.mongosecurity.entities.Role
import com.chethan.mongosecurity.entities.User
import com.chethan.mongosecurity.repositories.RoleRepo
import com.chethan.mongosecurity.repositories.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Configuration
class UserServicesImpl : UserServices, UserDetailsService {
    @Autowired
    lateinit var awsSNSClient: AmazonSNSClient

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var roleRepo: RoleRepo

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun saveUser(user: User): User {
        user.password = passwordEncoder.encode(user.password)
        return userRepo.insert(user)
    }

    override fun saveRole(role: Role): Role {
        return roleRepo.insert(role)
    }

    override fun addRoleToUser(userName: String, roleName: String) {
//        val user = userRepo.findByUserName(userName)
        val user = userRepo.findByUserName(userName)
        val role = roleRepo.findByRoleName(roleName)
        user.roles.add(role)
        userRepo.save(user)
    }

    override fun getUser(userName: String): User {
//        return userRepo.findByUserName(userName)
        return userRepo.findBy_id(userName)
    }

    override fun getAllUsers(): List<User> {
        return userRepo.findAll()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByUserName(username)
        val authorities: MutableList<SimpleGrantedAuthority> = mutableListOf()
        user.roles.forEach {
            authorities.add(SimpleGrantedAuthority(it.roleName))
        }
        return org.springframework.security.core.userdetails.User(user.userName, user.password, authorities)
    }


    override fun snsSubscribe(phone: String): String {
        val subscribeRequest = SubscribeRequest(
            "arn:aws:sns:ap-south-1:815128034221:spring-otp",
            "sms",
            phone
        )
        awsSNSClient.subscribe(subscribeRequest)
        return "Subscribed successfully"
    }

    override
    fun generateOTP(): String {
        return (1000..9999).random().toString()
    }

    override fun sendOTP(phone: String, otp: String) {
        val publishRequest = PublishRequest()
        publishRequest.message = otp
        publishRequest.phoneNumber = phone
        val user = userRepo.findBy_id(phone)
        user.password = passwordEncoder.encode(otp)
        userRepo.save(user)
        awsSNSClient.publish(publishRequest)
    }
}