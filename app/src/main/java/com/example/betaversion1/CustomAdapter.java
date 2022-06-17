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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> username=new ArrayList<>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<String> ratings=new ArrayList<>();
    ArrayList<String> reviewContents= new ArrayList<>();
    ArrayList<String> hasImage= new ArrayList<>();
    Bitmap bMap;
    FirebaseStorage storage;
    StorageReference storageReference;
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, ArrayList<String> username,ArrayList<String> book_name,ArrayList<String> ratings
            ,ArrayList<String> reviewContents,ArrayList<String> hasImage){
        this.context=applicationContext;
        this.username=username;
        this.book_name=book_name;
        this.ratings=ratings;
        this.reviewContents=reviewContents;
        this.hasImage=hasImage;
        inflater= (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount(){
        return username.size();
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= inflater.inflate(R.layout.custom_lv_layout,null);
        TextView tv_username=(TextView) view.findViewById(R.id.tv_username);
        TextView tv_bookName=(TextView) view.findViewById(R.id.tv_bookName);
        TextView tv_Rating=(TextView) view.findViewById(R.id.tv_Rating);
        TextView tv_ReviewContent=(TextView) view.findViewById(R.id.tv_ReviewContent);
        ImageView iv_reviewPic=(ImageView) view.findViewById(R.id.iv_reviewPic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(username.size()>1){
            tv_username.setText("Username:"+" "+username.get(i));
        }
        else tv_username.setText("Username:"+" "+username.get(0));
        tv_bookName.setText("Book Name:"+" "+book_name.get(i));
        tv_Rating.setText("rating:"+" "+ratings.get(i)+"/"+"5");
        tv_ReviewContent.setText(reviewContents.get(i));
        if(!hasImage.get(i).equals("Null")) {
            StorageReference imageRef= storageReference.child(hasImage.get(i));
            final long ONE_MEGABYTE = 3150* 3150;

            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    iv_reviewPic.setImageBitmap(bMap);
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
