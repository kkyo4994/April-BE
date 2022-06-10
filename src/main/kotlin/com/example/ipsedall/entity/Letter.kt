package com.example.ipsedall.entity

import com.example.ipsedall.payload.request.LetterRequest
import java.time.LocalDate
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
class Letter(
    userId: Long,
    createdAt: LocalDate,
    title: String,
    song: String,
    feeling: String,
    content: String
) {
    @EmbeddedId
    val letterId: LetterId = LetterId(userId, createdAt)

    var title: String = title
        protected set
    var song: String = song
        protected set
    var feeling: String = feeling
        protected set
    var content: String = content
        protected set

    fun update(letterRequest: LetterRequest) {
        this.title = letterRequest.title
        this.song = letterRequest.song
        this.content = letterRequest.content
        this.feeling = letterRequest.feeling
    }
}
