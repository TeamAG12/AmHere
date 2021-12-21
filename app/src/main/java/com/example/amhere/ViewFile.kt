package com.example.amhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewFile : AppCompatActivity() { ListView listView;
    DatabaseReference databaseReference;
    List<UploadFile> uploadFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        listView = (ListView)findViewById(R.id.listView);
        uploadFiles = new ArrayList<>();

        viewAllFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadFile uploadFile = uploadFiles.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadFile.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                UploadFile uploadFile = postSnapshot.getValue(UploadFile.class);
                uploadFiles.add(uploadFile);
            }

                String[] uploads = new String[uploadFiles.size()];
                for(int i=0; i<uploads.length;i++){
                uploads[i] = uploadFiles.get(i).getName();
            }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,uploads);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}