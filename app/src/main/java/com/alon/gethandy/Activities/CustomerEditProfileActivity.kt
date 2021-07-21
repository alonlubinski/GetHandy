package com.alon.gethandy.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import com.alon.gethandy.Fragments.Customer.ProfileFragment
import com.alon.gethandy.Models.User
import com.alon.gethandy.databinding.ActivityCustomerEditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class CustomerEditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerEditProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Firebase.firestore
        storage = Firebase.storage

        val user = intent.getSerializableExtra("user") as? User

        // Update UI
        binding.editProfilePGB.visibility = INVISIBLE
        binding.updateEDTFirst.editText?.setText(user?.firstName)
        binding.updateEDTLast.editText?.setText(user?.lastName)
        binding.updateEDTEmail.editText?.setText(user?.email)
        binding.updateEDTPhone.editText?.setText(user?.phone)
        //binding.updateEDTLocation.editText?.setText()
        if(user?.imageUri?.isNotEmpty() == true){
            Glide.with(this).load(user.imageUri).into(binding.updateIMGImage)
        }

        binding.updateBTNUpdate.setOnClickListener {
            binding.updateBTNUpdate.isEnabled = false
            binding.updateBTNPick.isEnabled = false
            binding.editProfilePGB.visibility = VISIBLE
            val storageRef = storage.reference
            val userImageRef = storageRef.child("images/users/" + user?.email + ".jpg")
            val bitmap = (binding.updateIMGImage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = userImageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                // Upload success
                userImageRef.downloadUrl.addOnSuccessListener {
                    // Download image url success
                    // TODO: upload updated information to the firebase
                    val firstName = binding.updateEDTFirst.editText?.text.toString().trim()
                    val lastName = binding.updateEDTLast.editText?.text.toString().trim()
                    val email = user?.email.toString()
                    val phone = binding.updateEDTPhone.editText?.text.toString().trim()
                    //val location =
                    // TODO: Upload image to firebase storage, then download the uri and put in the user
                    val imageUri = it.toString()
                    db.collection("users").document(email).update(
                        "firstName", firstName, "lastName", lastName,
                        "email", email, "phone", phone, "imageUri", imageUri
                    ).addOnSuccessListener {
                        val data = Intent()
                        user?.firstName = firstName
                        user?.lastName = lastName
                        user?.phone = phone
                        user?.imageUri = imageUri
                        data.putExtra("user", user)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }.addOnFailureListener {
                        Log.d("pttt", "data update failed")
                        binding.updateBTNUpdate.isEnabled = true
                        binding.updateBTNPick.isEnabled = true
                        binding.editProfilePGB.visibility = INVISIBLE
                    }
                }
            }.addOnFailureListener {
                // Upload failed
                Log.d("pttt", "image upload failed")
                binding.updateBTNUpdate.isEnabled = true
                binding.updateBTNPick.isEnabled = true
                binding.editProfilePGB.visibility = INVISIBLE
            }

        }

        binding.updateBTNPick.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    // Permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    // Show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else {
                    // Permission already granted
                    chooseImageGallery();
                }
            }
            else {
                // System OS is < Marshmallow
                chooseImageGallery();
            }
        }
    }

    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK){
            binding.updateIMGImage.setImageURI(data?.data)
        }
    }
}