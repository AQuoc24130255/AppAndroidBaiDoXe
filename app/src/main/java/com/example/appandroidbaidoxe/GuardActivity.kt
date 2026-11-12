package com.example.appandroidbaidoxe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appandroidbaidoxe.databinding.ActivityGuardBinding
import com.google.firebase.auth.FirebaseAuth

class GuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = "Xin chào, ${intent.getStringExtra("fullName")}"
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}