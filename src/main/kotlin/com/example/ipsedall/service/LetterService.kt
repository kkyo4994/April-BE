package com.example.ipsedall.service

import com.example.ipsedall.entity.Letter
import com.example.ipsedall.entity.LetterId
import com.example.ipsedall.entity.User
import com.example.ipsedall.payload.request.LetterRequest
import com.example.ipsedall.payload.response.LetterCalendarContentResponse
import com.example.ipsedall.payload.response.LetterCalendarResponse
import com.example.ipsedall.payload.response.LetterContentResponse
import com.example.ipsedall.payload.response.LetterResponse
import com.example.ipsedall.repository.LetterRepository
import com.example.ipsedall.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth
import javax.transaction.Transactional

@Service
class LetterService(
    private val letterRepository: LetterRepository,
    private val userRepository: UserRepository
) {
    fun saveLetter(letterRequest: LetterRequest) {
        val user = getUser()

        val letter = Letter(
            userId = user.id,
            createdAt = LocalDate.now(),
            content = letterRequest.content,
            feeling = letterRequest.feeling,
            song = letterRequest.song,
            title = letterRequest.title
        )

        letterRepository.save(letter)
    }

    @Transactional
    fun patchLetter(letterRequest: LetterRequest) {
        val user = getUser()

        val letterId = LetterId(
            userId = user.id,
            createdAt = LocalDate.now()
        )

        val letter = letterRepository.findByIdOrNull(letterId) ?: throw TODO()
        letter.update(letterRequest)
    }

    fun getLetterToday(): LetterResponse {
        val user = getUser()

        val today = LocalDate.now()
        val lastMonth = today.minusMonths(1)

        val lastMonthLetterId = LetterId(
            userId = user.id,
            createdAt = lastMonth
        )

        val todayLetterId = LetterId(
            userId = user.id,
            createdAt = today
        )

        val lastMonthLetterOrNull = letterRepository.findByIdOrNull(lastMonthLetterId)
        val todayLetterOrNull = letterRepository.findByIdOrNull(todayLetterId)

        val lastMonthLetterContentResponseOrNull = lastMonthLetterOrNull?.let {
            buildLetterContentResponse(it)
        }

        val todayLetterContentResponseOrNull = todayLetterOrNull?.let {
            buildLetterContentResponse(it)
        }

        return LetterResponse(
            lastMonthLetter = lastMonthLetterContentResponseOrNull,
            todayLetter = todayLetterContentResponseOrNull
        )
    }

    fun getLetter(date: LocalDate): LetterContentResponse? {
        val user = getUser()

        val lastMonth = date.minusMonths(1)

        val lastMonthLetterId = LetterId(
            userId = user.id,
            createdAt = lastMonth
        )

        val lastMonthLetterOrNull = letterRepository.findByIdOrNull(lastMonthLetterId)

        val lastMonthLetterContentResponseOrNull = lastMonthLetterOrNull?.let {
            buildLetterContentResponse(it)
        }

        return lastMonthLetterContentResponseOrNull

    }

    private fun buildLetterContentResponse(letter: Letter): LetterContentResponse {
        return LetterContentResponse(
            content = letter.content,
            title = letter.title,
            feeling = letter.feeling,
            song = letter.song,
            createdAt = letter.letterId.createdAt
        )
    }

    private fun getUser(): User {
        val userEmail = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByEmail(userEmail) ?: throw TODO()
    }

    fun getCalendar(yearMonth: YearMonth): LetterCalendarResponse {
        val monthFirstDate = yearMonth.atDay(1)
        val monthLastDate = yearMonth.atEndOfMonth()

        val letters = letterRepository.findAllByLetterIdCreatedAtBetween(monthFirstDate, monthLastDate)

        val letterMap = letters.toLetterMap()

        val calendarLetter = yearMonth.getMonthOfCalendarLetter(letterMap)
        return LetterCalendarResponse(calendarLetter)
    }

    private fun List<Letter>.toLetterMap() =
        this.associateBy { it.letterId.createdAt }

    private fun YearMonth.getMonthOfCalendarLetter(letterMap: Map<LocalDate, Letter>): List<LetterCalendarContentResponse> {
        val monthFirstDate = this.atDay(1)
        val monthLastDate = this.atEndOfMonth()

        var currentDate = monthFirstDate

        val letterCalendarContentResponses: MutableList<LetterCalendarContentResponse> = mutableListOf()

        while (!monthLastDate.isAfter(currentDate)) {
            val letterOrNull = letterMap[currentDate]
            val letterCalendarContentResponse = LetterCalendarContentResponse(
                date = currentDate,
                feeling = letterOrNull?.feeling
            )

            letterCalendarContentResponses.add(letterCalendarContentResponse)
            currentDate = currentDate.plusDays(1)
        }

        return letterCalendarContentResponses
    }
}
