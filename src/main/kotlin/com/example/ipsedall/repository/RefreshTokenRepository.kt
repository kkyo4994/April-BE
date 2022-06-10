package com.example.ipsedall.repository

import com.example.ipsedall.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {
    fun findByTokenValue(tokenValue: String): RefreshToken?
}
