package com.alon.gethandy.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alon.gethandy.Utils.Validation
import com.alon.gethandy.Utils.hideKeyboard
import com.alon.gethandy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        val isRemembered = sharedPref.getBoolean("rememberMe", false)

        if (isRemembered) {
            val currentUserEmail = auth.currentUser?.email
            if (currentUserEmail != null) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("userEmail", currentUserEmail)
                startActivity(intent)
            }
        }

        binding.loginBTNRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBTNLogin.setOnClickListener {
            val email = binding.loginEDTEmail.editText?.text.toString().trim()
            val password = binding.loginEDTPassword.editText?.text.toString().trim()
            val checkbox: Boolean = binding.loginCKBRemember.isChecked
            if (validateForm(email, password)) {

                if (checkbox)
                    sharedPref.edit().putBoolean("rememberMe", true).apply()
                else
                    sharedPref.edit().putBoolean("rememberMe", false).apply()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userEmail = auth.currentUser?.email
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("userEmail", userEmail)
                            startActivity(intent)

                        } else {
                            this.hideKeyboard()
                            Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        var isValid = true
        if (!Validation.validateEmail(email)) {
            binding.loginEDTEmail.error = "Please enter valid email"
            isValid = false
        } else {
            binding.loginEDTEmail.error = ""
        }
        if (!Validation.validateIsNotEmpty(password)) {
            binding.loginEDTPassword.error = "Please enter valid password"
            isValid = false
        } else if (!Validation.validatePasswordLength(password)) {
            binding.loginEDTPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.loginEDTPassword.error = ""
        }
        return isValid
    }

}

