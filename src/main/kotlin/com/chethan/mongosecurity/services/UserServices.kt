package com.chethan.mongosecurity.services

import com.chethan.mongosecurity.entities.Role
import com.chethan.mongosecurity.entities.User

interface UserServices {
    fun saveUser(user: User): User
    fun saveRole(role: Role): Role
    fun addRoleToUser(userName:String,roleName:String)
    fun getUser(userName: String): User
    fun getAllUsers():List<User>
}