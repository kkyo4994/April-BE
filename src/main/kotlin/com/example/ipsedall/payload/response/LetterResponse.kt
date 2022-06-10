package com.example.ipsedall.payload.response

import java.time.LocalDate

data class LetterResponse(
    val lastMonthLetter: LetterContentResponse?,
    val todayLetter: LetterContentResponse?
)

data class LetterContentResponse(
    val title: String,
    val song: String,
    val feeling: String,
    val content: String,
    val createdAt : LocalDate
)