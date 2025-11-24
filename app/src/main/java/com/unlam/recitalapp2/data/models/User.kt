package com.unlam.recitalapp2.data.models

data class User(
    val id: Long,
    val nickname: String,
    val password: String,
    val name: String,
    val surname: String,
    val money: Double,
    val createdDate: String
)
