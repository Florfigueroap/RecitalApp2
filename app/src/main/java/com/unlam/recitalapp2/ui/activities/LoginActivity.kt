package com.unlam.recitalapp2.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.repositories.UserRepository
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)

        loginButton.setOnClickListener { handleLogin() }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Función que maneja el inicio de sesión, verificando las credenciales y mostrando mensajes de error si es necesario.
    private fun handleLogin() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        usernameEditText.error = null
        passwordEditText.error = null

        val user = UserRepository.login(this, username, password)

        when {
            user == null -> {
                // puede ser usuario inexistente o contraseña incorrecta
                val remaining = UserRepository.getRemainingAttempts(username)

                if (remaining in 1..2) {
                    Toast.makeText(
                        this,
                        "Credenciales incorrectas. Te quedan $remaining intento(s).",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // usuario no existe o todavía no falló ninguna vez (error genérico)
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            user.blocked -> {
                Toast.makeText(
                    this,
                    "Usuario bloqueado por intentos fallidos. Contacte con soporte.",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                startActivity(intent)
                finish()
            }
        }
    }
}