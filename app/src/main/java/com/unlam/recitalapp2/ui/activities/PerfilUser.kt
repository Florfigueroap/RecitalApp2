package com.unlam.recitalapp2.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.User
import com.unlam.recitalapp2.data.repositories.UserRepository

class PerfilUser : AppCompatActivity() {

    private lateinit var tvPerfilNickname: TextView
    private lateinit var tvPerfilName: TextView
    private lateinit var tvPerfilSurname: TextView
    private lateinit var tvMoney: TextView
    private lateinit var tvCreatedDate: TextView

    private lateinit var tilNickname: TextInputLayout
    private lateinit var tilName: TextInputLayout
    private lateinit var tilSurname: TextInputLayout

    private lateinit var etNickname: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var etSurname: TextInputEditText

    private lateinit var btnEditarPerfil: Button
    private lateinit var btnGuardarPerfil: Button

    private var currentUser: User? = null
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_user)

        userId = intent.getLongExtra("USER_ID", -1L)
        currentUser = UserRepository.getById(userId)

        if (currentUser == null) {
            Toast.makeText(this, "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Referencias UI
        tvPerfilNickname = findViewById(R.id.tvPerfilNickname)
        tvPerfilName = findViewById(R.id.tvPerfilName)
        tvPerfilSurname = findViewById(R.id.tvPerfilSurname)
        tvMoney = findViewById(R.id.tvMoney)
        tvCreatedDate = findViewById(R.id.tvCreatedDate)

        tilNickname = findViewById(R.id.tilNickname)
        tilName = findViewById(R.id.tilName)
        tilSurname = findViewById(R.id.tilSurname)

        etNickname = findViewById(R.id.etNickname)
        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)

        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil)

        // Cargar datos en modo lectura
        cargarDatosEnVista()

        // Prellenar campos de edición
        val user = currentUser!!
        etNickname.setText(user.nickname)
        etName.setText(user.name)
        etSurname.setText(user.surname)

        btnEditarPerfil.setOnClickListener {
            mostrarModoEdicion()
        }

        btnGuardarPerfil.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosEnVista() {
        val user = currentUser ?: return
        tvPerfilNickname.text = "Usuario: ${user.nickname}"
        tvPerfilName.text = "Nombre: ${user.name}"
        tvPerfilSurname.text = "Apellido: ${user.surname}"
        tvMoney.text = "Saldo: ${user.money}"
        tvCreatedDate.text = "Cuenta creada: ${user.createdDate}"
    }

    private fun mostrarModoEdicion() {
        tilNickname.visibility = View.VISIBLE
        tilName.visibility = View.VISIBLE
        tilSurname.visibility = View.VISIBLE
        btnGuardarPerfil.visibility = View.VISIBLE

        btnEditarPerfil.visibility = View.GONE
    }

    private fun guardarCambios() {
        val user = currentUser ?: return

        tilNickname.error = null

        val newNick = etNickname.text?.toString()?.trim() ?: ""
        val newName = etName.text?.toString()?.trim() ?: ""
        val newSurname = etSurname.text?.toString()?.trim() ?: ""

        if (newNick.isEmpty()) {
            tilNickname.error = "El usuario no puede estar vacío"
            return
        }

        // Verificar que el nickname no esté usado por otro usuario
        val disponible = UserRepository.isNicknameAvailable(newNick, excludeUserId = user.id)
        if (!disponible) {
            tilNickname.error = "Ya existe un usuario con ese nombre"
            return
        }

        val updatedUser = user.copy(
            nickname = newNick,
            name = newName,
            surname = newSurname
        )

        val ok = UserRepository.updateUser(this, updatedUser)
        if (!ok) {
            Toast.makeText(this, "No se pudieron guardar los cambios", Toast.LENGTH_SHORT).show()
            return
        }

        currentUser = updatedUser
        cargarDatosEnVista()

        Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()

        //vuelve a modo lectura
        tilNickname.visibility = View.GONE
        tilName.visibility = View.GONE
        tilSurname.visibility = View.GONE
        btnGuardarPerfil.visibility = View.GONE

        btnEditarPerfil.visibility = View.VISIBLE
    }
}
