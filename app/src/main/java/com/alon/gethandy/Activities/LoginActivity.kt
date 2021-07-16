package com.alon.gethandy.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alon.gethandy.R
import com.alon.gethandy.Utils.Validation
import com.alon.gethandy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginBTNRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBTNLogin.setOnClickListener {
            val email = binding.loginEDTEmail.editText?.text.toString().trim()
            val password = binding.loginEDTPassword.editText?.text.toString().trim()
            if(validateForm(email, password)){
                // TODO: Write firebase login code

            }
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateForm(email: String, password: String) : Boolean {
        var isValid = true
        if(!Validation.validateEmail(email)){
            binding.loginEDTEmail.error = "Please enter valid email"
            isValid = false
        } else {
            binding.loginEDTEmail.error = ""
        }
        if(!Validation.validateIsNotEmpty(password)){
            binding.loginEDTPassword.error = "Please enter valid password"
            isValid = false
        }
        else if(!Validation.validatePasswordLength(password)){
            binding.loginEDTPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.loginEDTPassword.error = ""
        }
        return isValid
    }
}