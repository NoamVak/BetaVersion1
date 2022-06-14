package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OpeningScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(refAuth.getCurrentUser()!=null) {
            Intent si = new Intent(OpeningScreen.this, SignUp.class);
            startActivity(si);
            finish();
        }

    }


    public void SignUp(View view) {
        Intent si = new Intent(OpeningScreen.this, SignUp.class);
        startActivity(si);
        finish();
    }

    public void Credits(View view) {

    }
}