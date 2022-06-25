package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OtherProfile extends AppCompatActivity {

    ListView lv_userReviews1;
    TextView title_tv,subTitle_tv,tvUsername,tvBio;
    ImageView profilePic;
    Intent gi;
    Users user;
    String str1,uid,username,pfpPath,bookId,bookImage;
    ArrayList<String> reviewList=new ArrayList<>();
    ArrayList<Reviews> reviewValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<String> ratings=new ArrayList<>();
    ArrayList<String> reviewContents=new ArrayList<>();
    ArrayList<String> imagePath=new ArrayList<>();
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        lv_userReviews1=(ListView) findViewById(R.id.lv_userReviews1);
        title_tv=(TextView) findViewById(R.id.title_tv);
        subTitle_tv=(TextView) findViewById(R.id.subTitle_tv);
        tvUsername=(TextView) findViewById(R.id.tvUsername1);
        tvBio=(TextView) findViewById(R.id.tvBio1);
        profilePic=(ImageView) findViewById(R.id.profilePic1);

        gi=getIntent();
        uid=gi.getStringExtra("UserId");

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
            lv_userReviews1.setAdapter(customAdapter);
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
        Query query= refUsers.orderByChild("uid").equalTo(uid);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for(DataSnapshot data : dS.getChildren()){
                    user = data.getValue(Users.class);
                }
                username=user.getName();
                title_tv.setText(username+"'s Profile");
                subTitle_tv.setText((username+"'s Reviews"));
                tvUsername.setText(username);
                String bio=user.getBio();
                if(!bio.equals("Null"))
                    tvBio.setText(user.getBio());
                else tvBio.setText("");
                if(!user.getImage().equals("Null")) {
                    pfpPath = user.getImage();
                    downloadUserPfp(pfpPath);
                }
                readBookInfo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(userListener);

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
                Toast.makeText(OtherProfile.this,"not Working",Toast.LENGTH_SHORT).show();
            }
        });

    }

}