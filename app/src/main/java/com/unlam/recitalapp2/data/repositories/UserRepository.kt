package com.unlam.recitalapp2.data.repositories

import com.unlam.recitalapp2.data.models.User

object UserRepository {

    // Lista de usuarios mockeados
    private val mockUsers = listOf(
        User(1504L, "MARTIN_ALBANESI", "abc4321", "Martin", "Albanesi", 3500000.50, "2024/05/13"),
        User(2802L, "Fran25", "contraseña123", "Franco German", "Mazafra", 200000.50, "2021/01/20"),
        User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 120000.0, "2018/04/15")
    )

    private val users = mockUsers.toMutableList()

    fun verifyUser(nickname: String, password: String): Boolean {
        val user = login(nickname, password)
        return user != null
    }

    fun login(nickName: String, password: String): User? {
        if (nickName.isEmpty() || password.isEmpty()) {
            return null
        }
        return users.find { it.nickname == nickName && it.password == password }
    }
}