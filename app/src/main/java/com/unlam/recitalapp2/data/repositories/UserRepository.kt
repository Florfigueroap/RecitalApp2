package com.unlam.recitalapp2.data.repositories

import android.content.Context
import com.unlam.recitalapp2.data.models.User

object UserRepository {
    private const val MAX_ATTEMPTS = 3
    private const val PREFS_NAME = "users_prefs"
    private const val KEY_PREFIX_PASSWORD = "password_"
    private const val KEY_PREFIX_BLOCKED = "blocked_"

    // Lista de usuarios
    private val mockUsers = listOf(
        User(1504L, "MARTIN_ALBANESI", "abc4321", "Martin", "Albanesi", 3500000.50, "2024/05/13"),
        User(2802L, "Fran25", "contraseña123", "Franco German", "Mazafra", 200000.50, "2021/01/20"),
        User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 120000.0, "2018/04/15")
    )

    private val users = mockUsers.toMutableList()

    // contador de intentos fallidos por usuario
    private val failedAttemptsByNick = mutableMapOf<String, Int>()

    fun login(context: Context, nickName: String, password: String): User? {
        val nick = nickName.trim()
        val pass = password.trim()

        if (nick.isEmpty() || pass.isEmpty()) return null

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        var user = users.find { it.nickname == nick }

        //Si no está en memoria, se fija si es un usuario registrado en prefs
        if (user == null) {
            val storedPass = prefs.getString(KEY_PREFIX_PASSWORD + nick, null)
            if (storedPass != null) {
                val blockedFromPrefs = prefs.getBoolean(KEY_PREFIX_BLOCKED + nick, false)
                user = User(
                    id = System.currentTimeMillis(),
                    nickname = nick,
                    password = storedPass,
                    name = nick,
                    surname = "",
                    money = 0.0,
                    createdDate = "",
                    blocked = blockedFromPrefs
                )
                users.add(user)
            } else {
                //directamente el usuario no existe
                return null
            }
        }

        //estado bloqueado con prefs (por si viene de mock)
        val blockedFromPrefs = prefs.getBoolean(KEY_PREFIX_BLOCKED + nick, user.blocked)
        if (blockedFromPrefs && !user.blocked) {
            user = user.copy(blocked = true)
            replaceUser(user)
        }

        // si ya está bloqueado → devuelve bloqueado
        if (user.blocked) {
            return user
        }

        //contraseña incorrecta
        if (user.password != pass) {
            val current = failedAttemptsByNick[nick] ?: 0
            val newCount = current + 1
            failedAttemptsByNick[nick] = newCount

            if (newCount >= MAX_ATTEMPTS) {
                // se bloquea en memoria y en prefs
                setBlocked(context, user, true)
                return user.copy(blocked = true)
            }

            // menos de 3 intentos fallidos -> null (credenciales incorrectas)
            return null
        }

        // contraseña correcta → reseteamos intentos y devuelve user
        failedAttemptsByNick.remove(nick)
        return user
    }

    // cuántos intentos le quedan al usuario
    fun getRemainingAttempts(nickName: String): Int {
        val nick = nickName.trim()
        val current = failedAttemptsByNick[nick] ?: 0
        val remaining = MAX_ATTEMPTS - current
        return if (remaining < 0) 0 else remaining
    }

    //REGISTRO

    fun registerUser(context: Context, user: User): Boolean {
        val nick = user.nickname.trim()
        if (nick.isEmpty() || user.password.trim().isEmpty()) return false

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // ya existe en memoria si o no
        if (users.any { it.nickname == nick }) return false

        // ya existe en prefs si o no
        if (prefs.contains(KEY_PREFIX_PASSWORD + nick)) return false

        // se guarda en prefs para que quede para próximos inicios
        prefs.edit()
            .putString(KEY_PREFIX_PASSWORD + nick, user.password.trim())
            .putBoolean(KEY_PREFIX_BLOCKED + nick, user.blocked)
            .apply()

        //también en memoria
        users.add(user.copy(nickname = nick))

        return true
    }

    // HELPERS
    private fun setBlocked(context: Context, user: User, blocked: Boolean) {
        val updated = user.copy(blocked = blocked)
        replaceUser(updated)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_PREFIX_BLOCKED + user.nickname, blocked)
            .apply()
    }

    private fun replaceUser(updated: User) {
        val index = users.indexOfFirst { it.nickname == updated.nickname }
        if (index != -1) {
            users[index] = updated
        }
    }

    // Devuelve el usuario por id
    fun getById(userId: Long): User? {
        return users.find { it.id == userId }
    }

    fun isNicknameAvailable(nickName: String, excludeUserId: Long? = null): Boolean {
        val nick = nickName.trim()
        if (nick.isEmpty()) return false

        return users.none { user ->
            user.nickname == nick && (excludeUserId == null || user.id != excludeUserId)
        }
    }

    // Actualiza nombre, apellido y nickname del usuario
    fun updateUser(context: Context, updatedUser: User): Boolean {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index == -1) return false

        // Si el nickname cambió, verifica que no esté usado por otro
        if (!isNicknameAvailable(updatedUser.nickname, excludeUserId = updatedUser.id)) {
            return false
        }

        val oldUser = users[index]
        users[index] = updatedUser


        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val oldNick = oldUser.nickname
        if (prefs.contains(KEY_PREFIX_PASSWORD + oldNick)) {
            val pass = prefs.getString(KEY_PREFIX_PASSWORD + oldNick, oldUser.password)
            val blocked = prefs.getBoolean(KEY_PREFIX_BLOCKED + oldNick, oldUser.blocked)

            if (oldNick != updatedUser.nickname) {
                prefs.edit()
                    .remove(KEY_PREFIX_PASSWORD + oldNick)
                    .remove(KEY_PREFIX_BLOCKED + oldNick)
                    .apply()
            }

            // Guarda con el nuevo nickname
            prefs.edit()
                .putString(KEY_PREFIX_PASSWORD + updatedUser.nickname, pass)
                .putBoolean(KEY_PREFIX_BLOCKED + updatedUser.nickname, blocked)
                .apply()
        }

        return true
    }

}