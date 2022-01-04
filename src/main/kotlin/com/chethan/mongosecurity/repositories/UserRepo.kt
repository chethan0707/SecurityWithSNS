package com.chethan.mongosecurity.repositories

import com.chethan.mongosecurity.entities.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepo : MongoRepository<User, String> {
    //    fun findByRoleName(roleName: String):MutableList<User>
    fun findByUserName(username: String): User
}