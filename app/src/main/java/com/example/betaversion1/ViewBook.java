package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
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

public class ViewBook extends AppCompatActivity {

    TextView bookName_tv,author_tv,pages_tv,genre_tv,ageGroup_tv;
    ImageView bookImage;
    Intent gi;
    String bookId;
    Books book;
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Sci-Fi","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        bookName_tv=(TextView) findViewById(R.id.bookName_tv);
        author_tv=(TextView) findViewById(R.id.author_tv);
        pages_tv=(TextView) findViewById(R.id.pages_tv);
        genre_tv=(TextView) findViewById(R.id.genre_tv);
        ageGroup_tv=(TextView) findViewById(R.id.ageGroup_tv);
        bookImage=(ImageView) findViewById(R.id.bookImage);

        storage=FirebaseStorage.getInstance();

        gi=getIntent();
        bookId=gi.getStringExtra("bookId");

        readBookInfo();
    }


    private void displayBook(){
        bookName_tv.setText("Book Name- "+book.getName());
        author_tv.setText("Written by- "+book.getAuthor());
        pages_tv.setText("Pages- "+String.valueOf(book.getPages()));
        genre_tv.setText("Genre- "+genre[book.getGenre()]);
        ageGroup_tv.setText("Recommended for- "+ageGroup[book.getAgeGroup()]);
        if(!book.getImage().equals("Null")){
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef= storageRef.child(book.getImage());
            final long ONE_MEGABYTE = 3150* 3150;

            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    bookImage.setImageBitmap(bMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewBook.this,"not Working",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void readBookInfo(){
        Query query=refBooks.orderByChild("id").equalTo(bookId);
        ValueEventListener bookListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for (DataSnapshot data : dS.getChildren()) {
                    book = data.getValue(Books.class);
                }
                displayBook();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(bookListener);
    }
}