package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    FirebaseDatabase fd;
    FirebaseAuth mauth;
    FirebaseUser muser;
    DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mauth = FirebaseAuth.getInstance();
        muser= mauth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

    }
    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(MainActivity2.this,MainActivity.class));
       /* if(muser!= null){
            Log.i("muser",muser.getDisplayName());
            Toast.makeText(MainActivity2.this, "Please wait", Toast.LENGTH_SHORT).show();
            VerifyUserExistence();
        }
        else{
            startActivity(new Intent(MainActivity2.this,MainActivity.class));
        }*/
    }

    private void VerifyUserExistence()  {

        String currentUserID = mauth.getCurrentUser().getUid();
        RootRef.child("users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.child("Name").exists()))
                {

                    Toast.makeText(MainActivity2.this, "Welcome", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity2.this,MainScreen.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}