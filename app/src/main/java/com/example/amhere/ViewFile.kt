package com.example.amhere

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*


class ViewFile : AppCompatActivity() {

    var listView: ListView? = null
    var databaseReference: DatabaseReference? = null
    var uploadFiles: MutableList<UploadFile?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_file)

        listView = findViewById<View>(R.id.listView) as ListView
        uploadFiles = ArrayList()
        viewAllFiles()
        listView!!.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val uploadFile = (uploadFiles as ArrayList<UploadFile?>).get(position)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(uploadFile!!.getUrl())
                startActivity(intent)
            }
    }

    private fun viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads")
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val uploadFile = postSnapshot.getValue(UploadFile::class.java)
                    uploadFiles!!.add(uploadFile)
                }
                val uploads = arrayOfNulls<String>(
                    uploadFiles!!.size
                )
                for (i in uploads.indices) {
                    uploads[i] = uploadFiles!![i]!!.getName()
                }
                val adapter = ArrayAdapter(
                    applicationContext, android.R.layout.simple_list_item_1, uploads
                )
                listView!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}


