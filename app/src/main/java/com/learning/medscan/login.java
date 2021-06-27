 package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.PrimitiveIterator;

public class login extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private EditText et_Name;
    private EditText et_Password;
    private Button B_Login;
    private TextView go_to_sign;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        go_to_sign=findViewById(R.id.tv_goto_signup);
        go_to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,Signup.class));
                finish();
            }
        });

        et_Name=findViewById(R.id.et_username_login);
        et_Password=findViewById(R.id.et_userpassword_login);
        B_Login=findViewById(R.id.B_login);

        B_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_Name.getText().toString();
                String password=et_Password.getText().toString();

                if(name.isEmpty() || password.isEmpty() ){
                    Toast.makeText(login.this,"Enter both details.",Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(name,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(login.this, "welcome", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(login.this,MainScreen.class));
                                finish(); 
                            }
                            else
                            {
                                Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    
                }
            }
        });
    }
}