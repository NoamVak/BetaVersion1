package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Shop extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<String> saleList=new ArrayList<>();
    ArrayList<Sales> saleValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<Integer> pages=new ArrayList<>();
    ArrayList<Integer> conditionList= new ArrayList<>();
    ArrayList<String> location=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();
    ArrayList<String> hasImage= new ArrayList<>();

    ListView shop_ListView;
    String str1,bookId,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shop_ListView=(ListView) findViewById(R.id.shop_ListView);


        readUserInfo();

        shop_ListView.setOnItemClickListener(this);
        shop_ListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



    }

    private void displaySales(){
        if(!saleList.isEmpty()){
            book_name.clear();
            pages.clear();
            conditionList.clear();
            location.clear();
            dateList.clear();
            hasImage.clear();
            for(int i=0;i<saleValues.size();i++){
                bookId=saleValues.get(i).getBookId();
                int bIndex=bookList.indexOf(bookId);
                book_name.add(bookValues.get(bIndex).getName());
                pages.add(bookValues.get(bIndex).getPages());
                conditionList.add(saleValues.get(i).getCondition());
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
            CustomAdapterShop customAdp= new CustomAdapterShop(getApplicationContext(),book_name,pages,conditionList,location,dateList,hasImage);
            shop_ListView.setAdapter(customAdp);
            customAdp.notifyDataSetChanged();
        }
    }


    private void readSaleInfo(){
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
        refSales.addValueEventListener(saleListener);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if(st.equals("Home Screen")){
            Intent si=new Intent(Shop.this,MainActivity.class);
            startActivity(si);
        }

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }
}