package com.chethan.mongosecurity.repositories

import com.chethan.mongosecurity.entities.Role
import org.springframework.data.mongodb.repository.MongoRepository

interface RoleRepo:MongoRepository<Role,String> {
    fun findByRoleName(rolename:String): Role
}