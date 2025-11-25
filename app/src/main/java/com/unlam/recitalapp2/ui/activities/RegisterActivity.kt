package com.unlam.recitalapp2.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.User
import com.unlam.recitalapp2.data.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var nameEditText: TextInputEditText
    private lateinit var surnameEditText: TextInputEditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nicknameEditText = findViewById(R.id.nickname_edit_text)
        passwordEditText = findViewById(R.id.register_password_edit_text)
        nameEditText = findViewById(R.id.name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() {
        val nick = nicknameEditText.text.toString().trim()
        val pass = passwordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()

        if (nick.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Usuario y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        val newUser = User(
            id = System.currentTimeMillis(),
            nickname = nick,
            password = pass,
            name = name,
            surname = surname,
            money = 0.0,
            createdDate = today,
            blocked = false
        )

        val ok = UserRepository.registerUser(this, newUser)

        if (ok) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
            finish() // vuelve al login
        } else {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }
}
