package com.example.ipsedall.controller

import com.example.ipsedall.payload.request.LetterRequest
import com.example.ipsedall.payload.response.LetterCalendarResponse
import com.example.ipsedall.payload.response.LetterContentResponse
import com.example.ipsedall.payload.response.LetterResponse
import com.example.ipsedall.service.LetterService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@RequestMapping("/letter")
@RestController
class LetterController(
    private val letterService: LetterService
) {
    @PostMapping
    fun saveLetter(@RequestBody letterRequest: LetterRequest) {
        letterService.saveLetter(letterRequest)
    }

    @GetMapping("/{date}")
    fun getLetter(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate): LetterContentResponse? {
        return letterService.getLetter(date)
    }

    @GetMapping
    fun getLetterToday(): LetterResponse {
        return letterService.getLetterToday()
    }

    @PatchMapping
    fun patchLetter(@RequestBody letterRequest: LetterRequest) {
        letterService.patchLetter(letterRequest)
    }

    @GetMapping("/calendar/{date}")
    fun getCalendar(@PathVariable @DateTimeFormat date: YearMonth): LetterCalendarResponse {
        return letterService.getCalendar((date))
    }
}
