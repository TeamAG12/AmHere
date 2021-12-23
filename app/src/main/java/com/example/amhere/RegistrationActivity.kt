package com.example.amhere

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


internal class RegistrationActivity : AppCompatActivity() {

    private var userName: EditText? = null
    private var userPassword: EditText? = null
    private var userEmail: EditText? = null
    private var regButton: Button? = null
    private var userLogin: TextView? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        setupUIViews()
        firebaseAuth = FirebaseAuth.getInstance()
        regButton!!.setOnClickListener {
            if (validate()) {
                // Upload to DB
                val user_email = userEmail!!.text.toString().trim { it <= ' ' }
                val user_password = userPassword!!.text.toString().trim { it <= ' ' }
                firebaseAuth!!.createUserWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Successfully Registered, Upload complete!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@RegistrationActivity,
                                    MainActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registration Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        userLogin!!.setOnClickListener {
            startActivity(
                Intent(
                    this@RegistrationActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    private fun setupUIViews() {
        userName = findViewById<View>(R.id.etUserName) as EditText
        userPassword = findViewById<View>(R.id.etUserPassword) as EditText
        userEmail = findViewById<View>(R.id.etUserEmail) as EditText
        regButton = findViewById<View>(R.id.btnRegister) as Button
        userLogin = findViewById<View>(R.id.tvUserLogin) as TextView
    }

    private fun validate(): Boolean {
        var result = false
        val name = userName!!.text.toString()
        val password = userPassword!!.text.toString()
        val email = userEmail!!.text.toString()
        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
        } else {
            result = true
        }
        return result
    }
}