package com.example.appandroidbaidoxe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appandroidbaidoxe.databinding.ActivityLoginBinding
import com.example.appandroidbaidoxe.model.DbPath
import com.example.appandroidbaidoxe.model.Role
import com.example.appandroidbaidoxe.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đã đăng nhập từ trước thì vào thẳng
        auth.currentUser?.let { routeByRole(it.uid) }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString()
            if (email.isEmpty() || pass.length < 6) {
                toast("Nhập email và mật khẩu (>= 6 ký tự)")
                return@setOnClickListener
            }
            binding.btnLogin.isEnabled = false
            Log.d("LOGIN", "Bấm nút, bắt đầu signIn")
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Log.d("LOGIN", "Auth OK, uid=${it.user!!.uid}")
                    routeByRole(it.user!!.uid)
                }
                .addOnFailureListener {
                    Log.e("LOGIN", "Auth FAIL", it)
                    binding.btnLogin.isEnabled = true
                    toast("Đăng nhập thất bại: ${it.message}")
                }
//            auth.signInWithEmailAndPassword(email, pass)
//                .addOnSuccessListener { routeByRole(it.user!!.uid) }
//                .addOnFailureListener {
//                    binding.btnLogin.isEnabled = true
//                    toast("Đăng nhập thất bại: ${it.message}")
//                }
        }
    }

    /** Tìm user trong DB theo uid, kiểm tra còn hoạt động, rồi chuyển màn hình theo vai trò. */
    private fun routeByRole(uid: String) {
        Log.d("LOGIN", "Bắt đầu đọc users theo uid=$uid")
        db.getReference(DbPath.USERS)
            .orderByChild("uid").equalTo(uid)
            .get()
            .addOnSuccessListener { snap ->
                Log.d("LOGIN", "Đọc DB OK, số kết quả=${snap.childrenCount}")
                // ...phần cũ giữ nguyên
                val user = snap.children.firstOrNull()?.getValue(User::class.java)
                when {
                    user == null -> fail("Tài khoản chưa được cấp quyền trong hệ thống")
                    !user.active -> fail("Tài khoản đã bị khóa")
                    else -> {
                        val target = if (user.role == Role.ADMIN)
                            AdminActivity::class.java else GuardActivity::class.java
                        startActivity(Intent(this, target).apply {
                            putExtra("userId", user.userId)
                            putExtra("fullName", user.fullName)
                        })
                        finish()
                    }
                }
            }
            .addOnFailureListener {
                Log.e("LOGIN", "Đọc DB FAIL", it)
                fail("Lỗi đọc dữ liệu: ${it.message}")
            }
//        db.getReference(DbPath.USERS)
//            .orderByChild("uid").equalTo(uid)
//            .get()
//            .addOnSuccessListener { snap ->
//                val user = snap.children.firstOrNull()?.getValue(User::class.java)
//                when {
//                    user == null -> fail("Tài khoản chưa được cấp quyền trong hệ thống")
//                    !user.active -> fail("Tài khoản đã bị khóa")
//                    else -> {
//                        val target = if (user.role == Role.ADMIN)
//                            AdminActivity::class.java else GuardActivity::class.java
//                        startActivity(Intent(this, target).apply {
//                            putExtra("userId", user.userId)
//                            putExtra("fullName", user.fullName)
//                        })
//                        finish()
//                    }
//                }
//            }
//            .addOnFailureListener { fail("Lỗi đọc dữ liệu: ${it.message}") }
    }

    private fun fail(msg: String) {
        auth.signOut()
        binding.btnLogin.isEnabled = true
        toast(msg)
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}