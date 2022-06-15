package com.example.betaversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PostReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView iv_BookPic;
    EditText et_BookName,et_Author,et_Pages,et_ReviewContent;
    Spinner sp_Rating,sp_AgeGroup,sp_Genre;
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Science-Fiction","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};
    String [] rating={"rating","1","2","3","4","5"};
    String bookName,author,ReviewContent;
    Books book;


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

        ArrayAdapter<String> ratingAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,rating);
        sp_Rating.setAdapter(ratingAdp);

    }

    public void UploadBookPic(View view) {
    }

    public void UploadReview(View view) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}