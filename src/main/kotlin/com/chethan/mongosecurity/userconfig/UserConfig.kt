package com.chethan.mongosecurity.userconfig

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.chethan.mongosecurity.entities.User
import com.chethan.mongosecurity.repositories.RoleRepo
import com.chethan.mongosecurity.repositories.UserRepo
import com.chethan.mongosecurity.services.UserServicesImpl
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun amazonSNSClient(): AmazonSNSClient {
        val credentials = BasicAWSCredentials(
            "AKIA33SLKXOW44DIQ74X",
            "jrJluW++CJaxEcRwh3mWWquZyAesYYQNiQdrknWu"
        )
        return AmazonSNSClientBuilder.standard().withRegion("ap-south-1")
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build() as AmazonSNSClient
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
                User("+918904756775", "chethan", "1234", mutableListOf()),
            )
            userServices.saveUser(
                User(null, "retro", "1234", mutableListOf()),
            )
            userServices.saveUser(
                User(
                    null, "heat", "1234", mutableListOf()
                )
            )
            userServices.saveUser(
                User(
                    null, "darksoul", "1234", mutableListOf()
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