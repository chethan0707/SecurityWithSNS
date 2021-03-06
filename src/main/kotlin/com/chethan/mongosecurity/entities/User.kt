package com.chethan.mongosecurity.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user_credentials")
class User(
    @Id
    var id: String?,
    var name: String,
    @Indexed
    var userName: String,
    var password: String,
    @DBRef
    var roles: MutableList<Role>
) {
    override fun toString(): String {
        return "User(id=$id, name='$name', userName='$userName', password='$password', roles=$roles)"
    }

}