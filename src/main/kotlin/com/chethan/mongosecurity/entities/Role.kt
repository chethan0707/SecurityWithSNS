package com.chethan.mongosecurity.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Role(
    @Id
    var id: String?,
    var roleName: String,
) {
    override fun toString(): String {
        return "Role(id=$id, roleName='$roleName')"
    }
}
