package com.example.betaversion1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterShop extends BaseAdapter {
    Context context;
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<Integer> pages=new ArrayList<>();
    ArrayList<Integer> conditionList= new ArrayList<>();
    String[] condition={"Condition","Brand New","Like New","Used","Old"};
    ArrayList<String> location=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();
    ArrayList<String> hasImage= new ArrayList<>();
    Bitmap bMap;
    FirebaseStorage storage;
    StorageReference storageReference;
    LayoutInflater inflater;

    public CustomAdapterShop(Context applicationContext,ArrayList<String> book_name,ArrayList<Integer> pages,ArrayList<Integer> conditionList
            ,ArrayList<String> location, ArrayList<String> dateList,ArrayList<String> hasImage){
        this.context=applicationContext;
        this.book_name=book_name;
        this.pages=pages;
        this.conditionList=conditionList;
        this.location=location;
        this.dateList=dateList;
        this.hasImage=hasImage;
        inflater= (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return book_name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= inflater.inflate(R.layout.custom_lv_layout2,null);
        TextView bookName_text=(TextView) view.findViewById(R.id.bookName_text);
        TextView pages_text=(TextView) view.findViewById(R.id.pages_text);
        TextView saleLoc_text=(TextView) view.findViewById(R.id.saleLoc_text);
        TextView cond_text=(TextView) view.findViewById(R.id.cond_text);
        TextView date_text=(TextView) view.findViewById(R.id.date_text);
        ImageView bookPic_iv=(ImageView) view.findViewById(R.id.bookPic_iv);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        bookName_text.setText("Book Name- "+book_name.get(i));
        pages_text.setText("Pages- "+String.valueOf(pages.get(i)));
        cond_text.setText("Condition- "+condition[conditionList.get(i)]);
        saleLoc_text.setText("Where- "+location.get(i));
        date_text.setText("Published- "+dateList.get(i));
        if(!hasImage.get(i).equals("Null")) {
            StorageReference imageRef= storageReference.child(hasImage.get(i));
            final long ONE_MEGABYTE = 3150* 3150;

            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    bookPic_iv.setImageBitmap(bMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }




        return view;
    }
}
