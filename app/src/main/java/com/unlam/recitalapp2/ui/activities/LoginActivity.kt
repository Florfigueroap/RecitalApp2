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
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)

        // Setea el listener al botón de login
        loginButton.setOnClickListener {
            handleLogin()
        }
    }

    // Función que maneja el inicio de sesión, verificando las credenciales y mostrando mensajes de error si es necesario.
    private fun handleLogin() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Limpia los errores
        usernameEditText.error = null
        passwordEditText.error = null

        // 3. Verifica que el usuario exista en la base de datos
        if (UserRepository.verifyUser(username, password)) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Cierra la actividad actual para que el usuario no pueda volver atrás
            finish()
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }
}