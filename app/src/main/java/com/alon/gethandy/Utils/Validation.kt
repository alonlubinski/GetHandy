package com.alon.gethandy.Utils

import com.alon.gethandy.databinding.ActivityLoginBinding
import com.alon.gethandy.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

object Validation {

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validateIsNotEmpty(value: String) : Boolean {
        if(value.isEmpty())
            return false
        return true
    }

    fun validateEmail(email: String) : Boolean {
        if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
            return false
        }
        return true
    }

    fun validatePasswordLength(password: String) : Boolean {
        if(password.length < 6)
            return false
        return true
    }

    fun validatePasswordsMatch(password: String, password2: String) : Boolean {
        if(password != password2)
            return false
        return true
    }
}