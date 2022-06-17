package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;
import static com.example.betaversion1.FBref.refSales;
import static com.example.betaversion1.FBref.refUsers;

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

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    FirebaseUser user;
    String uid,str1,bookId;
    ListView lv_AllReviews;
    ArrayList<String> reviewList=new ArrayList<>();
    ArrayList<Reviews> reviewValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    ArrayList<String> usernameList=new ArrayList<>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<String> ratings=new ArrayList<>();
    ArrayList<String> reviewContents=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_AllReviews=(ListView)findViewById(R.id.lv_AllReviews);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }


        readReviewInfo();
        readUserInfo();
        readBookInfo();

    }

    @Override
    protected void onStart(){
        super.onStart();
            if(!reviewList.isEmpty()){
                usernameList.clear();
                reviewContents.clear();
                ratings.clear();
                book_name.clear();
                for(int i=0;i<reviewList.size();i++){
                    uid=reviewValues.get(i).getUid();
                    int uIndex=userList.indexOf(uid);
                    usernameList.add(userValues.get(uIndex).getName());
                    reviewContents.add(reviewValues.get(i).getReviewContent());
                    ratings.add(String.valueOf(reviewValues.get(i).getRating()));
                    bookId=reviewList.get(i);
                    int bIndex=bookList.indexOf(bookId);
                    book_name.add(bookValues.get(bIndex).getName());
                }
                CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),usernameList,book_name,ratings,reviewContents);
                lv_AllReviews.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
    }

    private void readReviewInfo(){
        Query query = refReviews.orderByChild("uid");
        ValueEventListener VEL= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                reviewList.clear();
                reviewValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Reviews reviewTmp = data.getValue(Reviews.class);
                    reviewValues.add(reviewTmp);
                    reviewList.add(str1);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(VEL);


    }

    private void readBookInfo(){
        ValueEventListener bookListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refBooks.addValueEventListener(bookListener);
    }

    private void readUserInfo() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                userList.clear();
                userValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1 = (String) data.getKey();
                    Users userTmp = data.getValue(Users.class);
                    userValues.add(userTmp);
                    userList.add(str1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUsers.addValueEventListener(userListener);

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