package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refBooks.push();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

    }

    public void profile(View view){
        Intent si=new Intent(MainActivity.this, Profile.class);
        startActivity(si);
    }

    public void createSale(View view) {
        Intent si=new Intent(MainActivity.this, CreateSale.class);
        startActivity(si);
    }

    public void shop(View view) {
        Intent si=new Intent(MainActivity.this, Shop.class);
        startActivity(si);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if(st.equals("Home Screen")){
            Toast.makeText(MainActivity.this,"You are already here!",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(st.equals("Sign Out")){
            refAuth.signOut();
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent si=new Intent (MainActivity.this,SignUp.class);
            startActivity(si);
            finish();
        }
        return true;
    }






}