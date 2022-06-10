package com.example.ipsedall.entity

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class LetterId(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val createdAt: LocalDate
) : Serializable
