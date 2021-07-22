package com.alon.gethandy.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.Utils.Validation
import com.alon.gethandy.Utils.hideKeyboard
import com.alon.gethandy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        val db = Firebase.firestore

        binding.registerBTNRegister.setOnClickListener {
            val firstName = binding.registerEDTFirst.editText?.text.toString().trim()
            val lastName = binding.registerEDTLast.editText?.text.toString().trim()
            val email = binding.registerEDTEmail.editText?.text.toString().trim()
            val password = binding.registerEDTPassword.editText?.text.toString().trim()
            val password2 = binding.registerEDTRepass.editText?.text.toString().trim()
            val type = binding.registerRDG.checkedRadioButtonId
            val customer = binding.registerRDBCustomer.id

            val userType: String = when (type) {
                customer -> "customer"
                else -> "business"
            }

            if (validateForm(firstName, lastName, email, password, password2)) {

                val user =
                    User(firstName, lastName, email, "", userType, 0.0, 0.0, "")

                db.collection("users")
                    .document(email)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                if (userType == "business") {
                    val business = Business(
                        email,
                        "",
                        0.0,
                        0.0,
                        "",
                        0,
                        0,
                        0.0,
                        "00:00",
                        "00:00",
                        "",
                        "",
                        "$firstName $lastName"
                    )

                    db.collection("businesses")
                        .document(email)
                        .set(business)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            val user = auth.currentUser?.email

                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("userEmail", user)
                            startActivity(intent)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            this.hideKeyboard()
                            Toast.makeText(baseContext, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateForm(
        firstName: String, lastName: String, email: String,
        password: String, password2: String
    ): Boolean {

        var isValid = true
        var passwordValid = true

        if (!Validation.validateIsNotEmpty(firstName)) {
            binding.registerEDTFirst.error = "Please enter valid first name"
            isValid = false
        } else {
            binding.registerEDTFirst.error = ""
        }

        if (!Validation.validateIsNotEmpty(lastName)) {
            binding.registerEDTLast.error = "Please enter valid last name"
            isValid = false
        } else {
            binding.registerEDTLast.error = ""
        }

        if (!Validation.validateEmail(email)) {
            binding.registerEDTEmail.error = "Please enter valid email"
            isValid = false
        } else {
            binding.registerEDTEmail.error = ""
        }

        if (!Validation.validatePasswordLength(password)) {
            binding.registerEDTPassword.error = "Password must be at least 6 characters"
            isValid = false
            passwordValid = false
        } else {
            binding.registerEDTPassword.error = ""
        }

        if (!Validation.validatePasswordLength(password2)) {
            binding.registerEDTRepass.error = "Password must be at least 6 characters"
            isValid = false
            passwordValid = false
        } else {
            binding.registerEDTRepass.error = ""
        }

        if (!Validation.validatePasswordsMatch(password, password2) && passwordValid) {
            binding.registerEDTPassword.error = "Passwords must be the same"
            binding.registerEDTRepass.error = "Passwords must be the same"
            isValid = false
        } else if (passwordValid) {
            binding.registerEDTPassword.error = ""
            binding.registerEDTRepass.error = ""
        }
        return isValid
    }
}