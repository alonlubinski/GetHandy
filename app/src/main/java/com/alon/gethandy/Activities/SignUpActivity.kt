package com.alon.gethandy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alon.gethandy.R
import com.alon.gethandy.Utils.Validation
import com.alon.gethandy.databinding.ActivityLoginBinding
import com.alon.gethandy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerBTNRegister.setOnClickListener {
            val firstName = binding.registerEDTFirst.editText?.text.toString().trim()
            val lastName = binding.registerEDTLast.editText?.text.toString().trim()
            val email = binding.registerEDTEmail.editText?.text.toString().trim()
            val password = binding.registerEDTPassword.editText?.text.toString().trim()
            val password2 = binding.registerEDTRepass.editText?.text.toString().trim()
            if(validateForm(firstName, lastName, email, password, password2)){
                // TODO: Write firebase sign up code

                finish()
            }
        }
    }

    private fun validateForm(firstName: String, lastName: String, email: String,
                             password: String, password2: String) : Boolean {
        var isValid = true
        var passwordValid = true
        if(!Validation.validateIsNotEmpty(firstName)){
            binding.registerEDTFirst.error = "Please enter valid first name"
            isValid = false
        } else {
            binding.registerEDTFirst.error = ""
        }
        if(!Validation.validateIsNotEmpty(lastName)){
            binding.registerEDTLast.error = "Please enter valid last name"
            isValid = false
        } else {
            binding.registerEDTLast.error = ""
        }
        if(!Validation.validateEmail(email)){
            binding.registerEDTEmail.error = "Please enter valid email"
            isValid = false
        } else {
            binding.registerEDTEmail.error = ""
        }
        if(!Validation.validatePasswordLength(password)){
            binding.registerEDTPassword.error = "Password must be at least 6 characters"
            isValid = false
            passwordValid = false
        } else {
            binding.registerEDTPassword.error = ""
        }
        if(!Validation.validatePasswordLength(password2)) {
            binding.registerEDTRepass.error = "Password must be at least 6 characters"
            isValid = false
            passwordValid = false
        } else {
            binding.registerEDTRepass.error = ""
        }
        if(!Validation.validatePasswordsMatch(password, password2) && passwordValid){
            binding.registerEDTPassword.error = "Passwords must be the same"
            binding.registerEDTRepass.error = "Passwords must be the same"
            isValid = false
        } else if(passwordValid){
            binding.registerEDTPassword.error = ""
            binding.registerEDTRepass.error = ""
        }
        return isValid
    }
}