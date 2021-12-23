package com.example.amhere

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var Name: EditText? = null
    private var Password: EditText? = null
    private var Login: Button? = null
    private var Info: TextView? = null
    private var counter = 5
    private var userRegistration: TextView? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Name = findViewById<View>(R.id.etName) as EditText
        Password = findViewById<View>(R.id.etPassword) as EditText
        Login = findViewById<View>(R.id.btnLogin) as Button
        Info = findViewById<View>(R.id.tvInfo) as TextView
        userRegistration = findViewById<View>(R.id.tvRegister) as TextView
        Info!!.text = "No of attempt remaining : 5"
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        val user = firebaseAuth!!.currentUser
        if (user != null) {
            finish()
            startActivity(Intent(this@MainActivity, AttendedActivity::class.java))
        }
        Login!!.setOnClickListener { validate(Name!!.text.toString(), Password!!.text.toString()) }
        userRegistration!!.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SignUpActivity::class.java
                )
            )
        }
    }

    fun learnMore(view: View?) {
//        val intent = Intent(this@MainActivity, LearnMoreActivity::class.java)
//        startActivity(intent)
    }

    fun aboutUs(view: View?) {
//        val intent = Intent(this@MainActivity, AboutUsActivity::class.java)
//        startActivity(intent)
    }

    private fun validate(userName: String, userPassword: String) {
        progressDialog!!.setMessage("Please Wait")
        progressDialog!!.show()
        firebaseAuth!!.signInWithEmailAndPassword(userName, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, AttendedActivity::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    counter--
                    Info!!.text = "No of attempt remaining : $counter"
                    progressDialog!!.dismiss()
                    if (counter == 0) {
                        Login!!.isEnabled = false
                    }
                }
            }
    }
}
