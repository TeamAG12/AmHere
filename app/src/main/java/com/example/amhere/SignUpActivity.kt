package com.example.amhere

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class SignUpActivity : AppCompatActivity() {
    private var Name: EditText? = null
    private var Password: EditText? = null
    private var Login: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Name = findViewById<View>(R.id.etName1) as EditText
        Password = findViewById<View>(R.id.etPassword1) as EditText
        Login = findViewById<View>(R.id.btnLogin1) as Button
        Login!!.setOnClickListener { validate(Name!!.text.toString(), Password!!.text.toString()) }
    }

    private fun validate(userName: String, userPassword: String) {
        if (userName == "Admin" && userPassword == "1234") {
            val intent = Intent(
                this@SignUpActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
        }
    }
}