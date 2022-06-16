package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PostReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView iv_BookPic;
    EditText et_BookName,et_Author,et_Pages,et_ReviewContent;
    Spinner sp_Rating,sp_AgeGroup,sp_Genre;
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Sci-Fi","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};
    Integer [] rating={0,1,2,3,4,5};
    ArrayList<String> bookImages;
    FirebaseUser user;
    String bookId,uid,strPages,bookName,author,reviewContent;
    int genreIndex=0,ageGroupIndex=0,pages,index=0;
    Books book;
    Reviews review;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        iv_BookPic=(ImageView) findViewById(R.id.iv_BookPic);
        et_Author=(EditText) findViewById(R.id.et_Author);
        et_BookName=(EditText) findViewById(R.id.et_BookName);
        et_Pages=(EditText) findViewById(R.id.et_pages);
        et_ReviewContent=(EditText) findViewById(R.id.et_ReviewContent);
        sp_Rating=(Spinner) findViewById(R.id.sp_Rating);
        sp_AgeGroup=(Spinner) findViewById(R.id.sp_AgeGroup);
        sp_Genre=(Spinner)findViewById(R.id.sp_Genre);

        sp_Genre.setOnItemSelectedListener(this);
        sp_AgeGroup.setOnItemSelectedListener(this);
        sp_Rating.setOnItemSelectedListener(this);

        ArrayAdapter<String> genreAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genre);
        sp_Genre.setAdapter(genreAdp);

        ArrayAdapter<String> ageGroupAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,ageGroup);
        sp_AgeGroup.setAdapter(ageGroupAdp);

        ArrayAdapter<Integer> ratingAdp= new ArrayAdapter<Integer>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,rating);
        sp_Rating.setAdapter(ratingAdp);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

    }

    public void UploadBookPic(View view) {


    }

    public void UploadReview(View view) {
        bookName=et_BookName.getText().toString();
        author=et_Author.getText().toString();
        strPages=et_Pages.getText().toString();
        reviewContent=et_ReviewContent.getText().toString();
        if(bookName.equals("")||reviewContent.equals("")||author.equals("")||strPages.equals("")||genreIndex==0){
            Toast.makeText(this,"Error: Fields cannot remain empty",Toast.LENGTH_SHORT).show();
        }
        else{
            bookId=refBooks.push().getKey();
            pages=Integer.parseInt(strPages);
            book=new Books(bookName,author,genreIndex,null,pages,bookId,index,"Null");
            review=new Reviews(uid,book,reviewContent,index);
            refBooks.child(String.valueOf(genreIndex)).child(author).setValue(book);
            refReviews.child(String.valueOf(index)).setValue(review);

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adp, View view, int i, long l) {
        if(adp.getId() == R.id.sp_AgeGroup){
            ageGroupIndex=i;
        }
        else if (adp.getId() == R.id.sp_Rating){
            index= rating[i];
        }
        else if(adp.getId() == R.id.sp_Genre){
            genreIndex=i;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}