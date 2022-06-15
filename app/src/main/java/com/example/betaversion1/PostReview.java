package com.example.betaversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PostReview extends AppCompatActivity {

    ImageView iv_BookPic;
    EditText et_BookName,et_Author,et_Pages,et_ReviewContent;
    Spinner sp_Rating,sp_AgeGroup,sp_Genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);
    }

    public void UploadBookPic(View view) {
    }

    public void UploadReview(View view) {
    }
}