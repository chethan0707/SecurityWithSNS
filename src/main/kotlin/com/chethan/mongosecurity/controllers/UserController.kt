package com.chethan.mongosecurity.controllers

import com.chethan.mongosecurity.entities.Role
import com.chethan.mongosecurity.entities.User
import com.chethan.mongosecurity.services.UserServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api"])
class UserController {
    @Autowired
    lateinit var userServices: UserServices

    @GetMapping(path = ["/otp"])
    fun getotp(@RequestParam phone: String) {
        val user = userServices.getUser(phone)
        if (user._id == null) {
            userServices.snsSubscribe(phone)
        }
        val otp = userServices.generateOTP()
        userServices.sendOTP(phone, otp)
        println(phone)
        println(otp)
    }

    @PostMapping(path = ["/saveUser"])
    fun saveUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userServices.saveUser(user))
    }

    @GetMapping(path = ["/subscribe"])
    fun subscribe(phone: String): ResponseEntity<String> {
        return ResponseEntity.ok(userServices.snsSubscribe(phone))
    }

    @GetMapping(path = ["/getUsers"])
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userServices.getAllUsers())
    }

    @PostMapping(path = ["/saveRole"])
    fun saveRole(@RequestBody role: Role): ResponseEntity<Role> {
        return ResponseEntity.ok(userServices.saveRole(role))
    }

    @PostMapping(path = ["/addRole"])
    fun addRoleToUser(@RequestParam phone: String, rolename: String): ResponseEntity<String> {
        userServices.addRoleToUser(phone, rolename)
        return ResponseEntity.ok("Successfully added role to user")
    }
}