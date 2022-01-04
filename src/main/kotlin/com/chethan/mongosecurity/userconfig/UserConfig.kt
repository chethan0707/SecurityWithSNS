package com.chethan.mongosecurity.userconfig

import com.chethan.mongosecurity.repositories.RoleRepo
import com.chethan.mongosecurity.repositories.UserRepo
import com.chethan.mongosecurity.services.UserServicesImpl
import com.chethan.mongosecurity.entities.Role
import com.chethan.mongosecurity.entities.User
import org.springframework.boot.CommandLineRunner
import org.springframework.cglib.proxy.NoOp
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun init(userRepo: UserRepo, roleRepo: RoleRepo, userServices: UserServicesImpl): CommandLineRunner {
        return CommandLineRunner {
            userRepo.deleteAll()
//            roleRepo.deleteAll()
//            roleRepo.save(Role(null, "ROLE_USER"))
//            roleRepo.save(Role(null, "ROLE_ADMIN"))
//            roleRepo.save(Role(null, "ROLE_MANAGER"))
            val role1 = roleRepo.findAll()

            userServices.saveUser(
                User(null, "chethan", "chethan", "1234", mutableListOf()),
            )
            userServices.saveUser(
                User(null, "nithin", "retro", "1234", mutableListOf()),
            )
            userServices.saveUser(
                User(
                    null, "gowtham", "heat", "1234", mutableListOf()
                )
            )
            userServices.saveUser(
                User(
                    null, "suhas", "darksoul", "1234", mutableListOf()
                )
            )


            userServices.addRoleToUser("chethan", "ROLE_ADMIN")
            userServices.addRoleToUser("chethan", "ROLE_USER")
            userServices.addRoleToUser("chethan", "ROLE_MANAGER")
            userServices.addRoleToUser("retro", "ROLE_MANAGER")
//            print(userServices.getUser("chethan"))
        }

    }

}