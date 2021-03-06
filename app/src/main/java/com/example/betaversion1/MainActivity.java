package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;
import static com.example.betaversion1.FBref.refSales;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    AlertDialog.Builder adb;

    String uid,str1,bookId,bookImage;
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
    ArrayList<String> imagePath=new ArrayList<>();

    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_AllReviews=(ListView)findViewById(R.id.lv_AllReviews);

        lv_AllReviews.setOnItemClickListener(this);
        lv_AllReviews.setOnItemLongClickListener(this);
        lv_AllReviews.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        readUserInfo();


    }

    private void displayReviews(){
        if(!reviewList.isEmpty()){
            usernameList.clear();
            reviewContents.clear();
            ratings.clear();
            book_name.clear();
            imagePath.clear();
            for(int i=0;i<reviewList.size();i++){
                uid=reviewValues.get(i).getUid();
                int uIndex=userList.indexOf(uid);
                usernameList.add(userValues.get(uIndex).getName());
                reviewContents.add(reviewValues.get(i).getReviewContent());
                ratings.add(String.valueOf(reviewValues.get(i).getRating()));
                bookId=reviewList.get(i);
                int bIndex=bookList.indexOf(bookId);
                book_name.add(bookValues.get(bIndex).getName());
                if(bookValues.get(bIndex).getImage().equals("Null")) {
                    imagePath.add("Null");
                }
                else {
                    bookImage = bookValues.get(bIndex).getImage();
                    imagePath.add(bookImage);
                }
            }
            CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),usernameList,book_name,ratings,reviewContents,imagePath);
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
                displayReviews();
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
                readReviewInfo();
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
                readBookInfo();
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

        menu.add(0,0,150,"Sign Out");

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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        bookId=reviewList.get(i);
        Intent si=new Intent(MainActivity.this,ViewBook.class);
        si.putExtra("bookId",bookId);
        startActivity(si);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String newUid=reviewValues.get(i).getUid();
        adb= new AlertDialog.Builder(this);
        adb.setTitle("View other user");
        adb.setMessage("Do you want to visit this user's profile?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(!newUid.equals(fbUser.getUid())){
                    Intent si=new Intent(MainActivity.this,OtherProfile.class);
                    si.putExtra("UserId",newUid);
                    startActivity(si);
                }
                else{
                    Toast.makeText(MainActivity.this,"It's your account!",Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                }
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog ad=adb.create();
        ad.show();
        return true;
    }
}