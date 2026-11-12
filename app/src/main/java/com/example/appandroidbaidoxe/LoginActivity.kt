package com.example.appandroidbaidoxe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appandroidbaidoxe.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đã đăng nhập thì vào thẳng màn hình chính
        if (auth.currentUser != null) goToMain()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString()
            if (email.isEmpty() || pass.length < 6) {
                toast("Nhập email và mật khẩu (>= 6 ký tự)")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { goToMain() }
                .addOnFailureListener { toast("Đăng nhập thất bại: ${it.message}") }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString()
            if (email.isEmpty() || pass.length < 6) {
                toast("Nhập email và mật khẩu (>= 6 ký tự)")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { toast("Đăng ký thành công"); goToMain() }
                .addOnFailureListener { toast("Đăng ký thất bại: ${it.message}") }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}