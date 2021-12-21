package com.example.amhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import java.util.jar.Attributes

class SignUpActivity : AppCompatActivity() {

    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Name = (EditText)findViewById(R.id.etName1);
        Password = (EditText)findViewById(R.id.etPassword1);
        Login = (Button)findViewById(R.id.btnLogin1);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword){
        if((userName.equals("Admin")) && (userPassword.equals("1234"))){
            Intent intent = new Intent(SignUpActivity.this,
               SignUpActivity.class);
            startActivity(intent);
        }
    }


}