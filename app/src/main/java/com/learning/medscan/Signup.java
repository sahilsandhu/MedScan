package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private TextView go_to_login;
    private TextView signup;
    private EditText txt_Name;
    private EditText txt_Pass1;
    private EditText txt_Pass2;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(Signup.this);

        mRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();


         go_to_login=findViewById(R.id.go_to_login);
         signup=findViewById(R.id.B_signup);
         txt_Name= findViewById(R.id.user_name_signup);
         txt_Pass1= findViewById(R.id.et_inPassword);
         txt_Pass2= findViewById(R.id.ET_repassword);
         pd=new ProgressDialog(this);


        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,login.class));
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=txt_Name.getText().toString();
                String pass1=txt_Pass1.getText().toString();
                String pass2=txt_Pass2.getText().toString();
                if(TextUtils.isEmpty(name) ||TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)){
                    Toast.makeText(Signup.this,"Please enter complete details",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!pass1.equals(pass2)){
                        Toast.makeText(Signup.this,"Password mismatch",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerUser(name,pass1);
                    }
                }

            }
        });


    }

    private void registerUser(String name, String pass1) {

        pd.setMessage("Please wait");
        pd.show();
        mAuth.createUserWithEmailAndPassword(name,pass1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map= new HashMap<>();
                map.put("Email",name);
                map.put("Password",pass1);
                String uid=mAuth.getCurrentUser().getUid();
                map.put("Id",uid);
                mRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(Signup.this,Register.class).putExtra("uid",uid));
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(Signup.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}