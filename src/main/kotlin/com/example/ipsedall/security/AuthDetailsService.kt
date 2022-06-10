package com.example.ipsedall.security

import com.example.ipsedall.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Repository

@Repository
class AuthDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw TODO()
        return User(
            user.email,
            "",
            mutableListOf()
        )
    }
}