package com.alon.gethandy.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alon.gethandy.Models.Business
import com.alon.gethandy.databinding.ActivityBusinessEditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BusinessEditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessEditProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Firebase.firestore
        storage = Firebase.storage

        val business = intent.getSerializableExtra("business") as? Business

        binding.bEditProfilePGB.visibility = INVISIBLE
        binding.bUpdateEDTBusinessName.editText?.setText(business?.businessName)
        binding.bUpdateEDTStartTime.editText?.setText(business?.startHours)
        binding.bUpdateEDTEndTime.editText?.setText(business?.endHours)
        binding.bUpdateEDTPhone.editText?.setText(business?.phoneNumber)
        binding.bUpdateEDTOwner.editText?.setText(business?.ownerName)
        //TODO: check about location
//        binding.bUpdateEDTLocation.editText?.setText(business?.businessAddress)

        if (business?.businessImage?.isNotEmpty() == true) {
            Glide.with(this).load(business.businessImage).into(binding.bUpdateIMGImage)
        }

        binding.bUpdateBTNUpdate.setOnClickListener {

            binding.bUpdateBTNUpdate.isEnabled = false
            binding.bUpdateBTNPick.isEnabled = false
            binding.bEditProfilePGB.visibility = View.VISIBLE

            val storageRef = storage.reference
            val businessImageRef = storageRef.child("images/businesses/" + business?.ownerEmail + ".jpg")
            val bitmap = (binding.bUpdateIMGImage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = businessImageRef.putBytes(data)

            uploadTask.addOnSuccessListener {
                businessImageRef.downloadUrl.addOnSuccessListener {

                    val businessName = binding.bUpdateEDTBusinessName.editText?.text.toString().trim()
                    val startHour = binding.bUpdateEDTStartTime.editText?.text.toString().trim()
                    val endHour = binding.bUpdateEDTEndTime.editText?.text.toString().trim()
                    val phone = binding.bUpdateEDTPhone.editText?.text.toString().trim()
                    val ownerName = binding.bUpdateEDTOwner.editText?.text.toString().trim()
                    //TODO: check location
                    //val location = binding.bUpdateEDTLocation.editText?.text.toString().trim()
                    val description = binding.bUpdateEDTDescription.editText?.text.toString().trim()

                    val imageUri = it.toString()
                    val email = business?.ownerEmail.toString()

                    db.collection("businesses").document(email).update(
                        "businessName",
                        businessName,
                        "startHours",
                        startHour,
                        "endHours",
                        endHour,
                        "phoneNumber",
                        phone,
                        "description",
                        description,
                        "businessImage",
                        imageUri,
                        "ownerName",
                        ownerName

                    ).addOnSuccessListener {

                        val data = Intent()
                        business?.businessName = businessName
                        business?.startHours = startHour
                        business?.endHours = endHour
                        business?.phoneNumber = phone
                        business?.businessImage = imageUri
                        business?.description = description
                        business?.ownerName = ownerName

                        data.putExtra("business", business)
                        setResult(Activity.RESULT_OK, data)
                        finish()

                    }.addOnFailureListener {
                        Log.d("pttt", "data update failed")
                        binding.bUpdateBTNUpdate.isEnabled = true
                        binding.bUpdateBTNPick.isEnabled = true
                        binding.bEditProfilePGB.visibility = View.INVISIBLE
                    }
                }
            }
        }
        binding.bUpdateBTNPick.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    // Permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    // Show popup to request runtime permission
                    requestPermissions(
                        permissions, PERMISSION_CODE
                    );
                } else {
                    // Permission already granted
                    chooseImageGallery();
                }
            } else {
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BusinessEditProfileActivity.IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            binding.bUpdateIMGImage.setImageURI(data?.data)
        }
    }
}


