package com.example.amhere

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AttendedActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var firebaseAuth: FirebaseAuth? = null
    var etFileName: EditText? = null
    var btnUpload: Button? = null
    var btnView: Button? = null
    var logout: Button? = null
    var databaseReference: DatabaseReference? = null
    var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attended)

        logout = findViewById<View>(R.id.btnLogout) as Button
        etFileName = findViewById<View>(R.id.etFileName) as EditText
        btnUpload = findViewById<View>(R.id.btnUpload) as Button
        btnView = findViewById<View>(R.id.btnView) as Button
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads")
        storageReference = FirebaseStorage.getInstance().reference
        logout!!.setOnClickListener {
            firebaseAuth!!.signOut()
            finish()

            startActivity(Intent(this@AttendedActivity, MainActivity::class.java))
        }
        btnUpload!!.setOnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Please Select File"), 1)
        }
        btnView!!.setOnClickListener {
            val intent = Intent(this@AttendedActivity, ViewFile::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.second, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_tools) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            uploadFile(data.data)
        }
    }

    private fun uploadFile(data: Uri?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading")
        progressDialog.show()

        val reference = storageReference!!.child("uploads/" + System.currentTimeMillis() + ".pdf")
        reference.putFile(data!!)
            .addOnSuccessListener { taskSnapshot ->
                val uri = taskSnapshot.storage.downloadUrl
                while (!uri.isComplete);
                val url = uri.result
                val uploadFile = UploadFile(etFileName!!.text.toString(), url.toString())
                databaseReference!!.child(databaseReference!!.push().key!!).setValue(uploadFile)
                Toast.makeText(this@AttendedActivity, "File Uploaded", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Upload : " + progress.toInt() + "%")
            }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
}
