package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Profile extends AppCompatActivity {

    ListView lv_userReviews;
    ImageView profilePic;
    TextView tvUsername,tvBio;
    FirebaseUser user;
    String str1,uid,username,pfpPath,bookId,bookImage;
    ArrayList<String> reviewList=new ArrayList<>();
    ArrayList<Reviews> reviewValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<String> ratings=new ArrayList<>();
    ArrayList<String> reviewContents=new ArrayList<>();
    ArrayList<String> imagePath=new ArrayList<>();
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic=(ImageView) findViewById(R.id.profilePic);
        tvUsername=(TextView) findViewById(R.id.tvUsername);
        tvBio=(TextView)findViewById(R.id.tvBio);
        lv_userReviews=(ListView)findViewById(R.id.lv_userReviews);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

        storage= FirebaseStorage.getInstance();


        readUserInfo();

    }

    private void displayUserReviews(){
        if(!reviewList.isEmpty()){
            reviewContents.clear();
            ratings.clear();
            book_name.clear();
            imagePath.clear();
            for(int i=0;i<reviewList.size();i++){
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
            CustomAdapterProf customAdapter=new CustomAdapterProf(getApplicationContext(),username,book_name,ratings,reviewContents,imagePath);
            lv_userReviews.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        }

    }

    private void readReviewInfo(){
        Query query = refReviews.orderByChild("uid").equalTo(uid);
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
                displayUserReviews();
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

    private void readUserInfo(){
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                userList.clear();
                userValues.clear();
                for(DataSnapshot data : dS.getChildren()){
                    str1= (String) data.getKey();
                    Users userTmp = data.getValue(Users.class);
                    userValues.add(userTmp);
                    userList.add(str1);
                }
                int index=userList.indexOf(uid);
                username=userValues.get(index).getName();
                tvUsername.setText(username);
                String bio=userValues.get(index).getBio();
                if(!bio.equals("Null"))
                    tvBio.setText(userValues.get(index).getBio());
                else tvBio.setText("");
                if(!userValues.get(index).getImage().equals("Null")) {
                    pfpPath = userValues.get(index).getImage();
                    downloadUserPfp(pfpPath);
                }
                readBookInfo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUsers.addValueEventListener(userListener);

    }

    private void downloadUserPfp(String s){
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef= storageRef.child(s);
        final long ONE_MEGABYTE = 3150* 3150;

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profilePic.setImageBitmap(bMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this,"not Working",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if(st.equals("Home Screen")){
            Intent si=new Intent(Profile.this,MainActivity.class);
            startActivity(si);
        }

        if(st.equals("Sign Out")){
            refAuth.signOut();
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent si=new Intent (Profile.this,SignUp.class);
            startActivity(si);
            finish();
        }
        return true;
    }


    public void EditProfile(View view) {
        Intent si=new Intent(Profile.this,EditProfile.class);
        startActivity(si);
    }

    public void postReview(View view) {
        Intent si=new Intent(Profile.this,PostReview.class);
        startActivity(si);
    }
}