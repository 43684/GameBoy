package com.example.gameboy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gameboy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDB: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val name = binding.nameEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            validateData(name,email,pass,confirmPass)

           /* if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }*/
        }
    }

    private fun validateData(name:String,email: String, password: String,confirmPass:String) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show()
        } else if (confirmPass.isEmpty()) {
            Toast.makeText(this, "Confirm password!", Toast.LENGTH_SHORT).show()
        } else if (confirmPass != password) {
            Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show()
        } else {
            createUserAccount(name,email,password)
        }

    }

    private fun createUserAccount(name:String,email: String,password: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB = FirebaseDatabase.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo(name)
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {error->
                Toast.makeText(this, "Failed to create account due to ${error.message}!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo(name: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB = FirebaseDatabase.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid ?: ""
        val userData = UserData(name, 0)
        firebaseDB.reference.child("users").child(userId).setValue(userData)
    }

}