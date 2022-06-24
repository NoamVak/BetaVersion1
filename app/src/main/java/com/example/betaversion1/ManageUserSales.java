package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageUserSales extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ArrayList<String> saleList=new ArrayList<>();
    ArrayList<Sales> saleValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<Integer> pages=new ArrayList<>();
    ArrayList<Integer> conditionList= new ArrayList<>();
    ArrayList<String> location=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();
    ArrayList<String> hasImage= new ArrayList<>();
    ArrayList<Integer> price= new ArrayList<>();

    String uid,str1,bookId,image;
    FirebaseUser user;
    ListView lv_mngSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_sales);

        lv_mngSales=(ListView) findViewById(R.id.lv_mngSales);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

        lv_mngSales.setOnItemClickListener(this);
        lv_mngSales.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        readBookInfo();
    }

    private void displaySales(){
        if(!saleList.isEmpty()){
            book_name.clear();
            pages.clear();
            conditionList.clear();
            location.clear();
            dateList.clear();
            hasImage.clear();
            price.clear();
            for(int i=0;i<saleValues.size();i++){
                bookId=saleValues.get(i).getBookId();
                int bIndex=bookList.indexOf(bookId);
                book_name.add(bookValues.get(bIndex).getName());
                pages.add(bookValues.get(bIndex).getPages());
                conditionList.add(saleValues.get(i).getCondition());
                price.add(saleValues.get(i).getPrice());
                if(!saleValues.get(i).getAddress().equals(""))
                    location.add(saleValues.get(i).getCity()+" - "+saleValues.get(i).getAddress());
                else location.add(saleValues.get(i).getCity());
                dateList.add(saleValues.get(i).getDate());
                if(bookValues.get(bIndex).getImage().equals("Null")) {
                    hasImage.add("Null");
                }
                else {
                    image=bookValues.get(bIndex).getImage();
                    hasImage.add(image);
                }
            }
            CustomAdapterShop customAdp= new CustomAdapterShop(getApplicationContext(),book_name,pages,conditionList,location,dateList
                    ,hasImage,price);
            lv_mngSales.setAdapter(customAdp);
            customAdp.notifyDataSetChanged();
        }
    }

    private void readSaleInfo(){
        Query query= refSales.orderByChild("userId").equalTo(uid);
        ValueEventListener saleListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                saleList.clear();
                saleValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Sales saleTmp = data.getValue(Sales.class);
                    saleValues.add(saleTmp);
                    saleList.add(str1);
                }
                displaySales();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(saleListener);
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
                readSaleInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refBooks.addValueEventListener(bookListener);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}