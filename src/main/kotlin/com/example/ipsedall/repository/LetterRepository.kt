package com.example.ipsedall.repository

import com.example.ipsedall.entity.Letter
import com.example.ipsedall.entity.LetterId
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface LetterRepository: CrudRepository<Letter, LetterId> {
    fun findAllByLetterIdCreatedAtBetween(startDate: LocalDate, endDate: LocalDate): List<Letter>
}
