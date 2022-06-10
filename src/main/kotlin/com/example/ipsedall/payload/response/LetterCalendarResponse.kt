package com.example.ipsedall.payload.response

import java.time.LocalDate

data class LetterCalendarResponse(
    val dateList: List<LetterCalendarContentResponse>
)


data class LetterCalendarContentResponse(
    val date: LocalDate,
    val feeling: String?
)
