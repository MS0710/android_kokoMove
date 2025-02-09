package com.example.kokomove_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderContentActivity extends AppCompatActivity {
    private String TAG = "OrderContentActivity";
    private TextView txt_orderContent_orderNumber,txt_orderContent_name,txt_orderContent_size,txt_orderContent_category,
            txt_orderContent_thing,txt_orderContent_receiveAddress,txt_orderContent_sendAddress,txt_orderContent_sendMethod;
    private TextView txt_orderContent_receivePeople,txt_orderContent_receiveTime,txt_orderContent_sendPeople,
            txt_orderContent_sendTime;
    private Button btn_orderContent_back;
    private String name,orderNumber,category,receiveAddress,sendAddress,sendMethod, size,thing;
    private String receivePeople,receiveTime,sendPeople,sendTime;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_content);
        initView();
    }

    private void initView(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OrderInfo");

        orderNumber = getIntent().getStringExtra("orderNumber");


        txt_orderContent_orderNumber = (TextView) findViewById(R.id.txt_orderContent_orderNumber);
        txt_orderContent_name = (TextView) findViewById(R.id.txt_orderContent_name);
        txt_orderContent_size = (TextView) findViewById(R.id.txt_orderContent_size);
        txt_orderContent_category = (TextView) findViewById(R.id.txt_orderContent_category);
        txt_orderContent_thing = (TextView) findViewById(R.id.txt_orderContent_thing);
        txt_orderContent_receiveAddress = (TextView) findViewById(R.id.txt_orderContent_receiveAddress);
        txt_orderContent_sendAddress = (TextView) findViewById(R.id.txt_orderContent_sendAddress);
        txt_orderContent_sendMethod = (TextView) findViewById(R.id.txt_orderContent_sendMethod);
        txt_orderContent_receivePeople = (TextView) findViewById(R.id.txt_orderContent_receivePeople);
        txt_orderContent_receiveTime = (TextView) findViewById(R.id.txt_orderContent_receiveTime);
        txt_orderContent_sendPeople = (TextView) findViewById(R.id.txt_orderContent_sendPeople);
        txt_orderContent_sendTime = (TextView) findViewById(R.id.txt_orderContent_sendTime);
        btn_orderContent_back = (Button)findViewById(R.id.btn_orderContent_back);
        btn_orderContent_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        readExistingData();
    }

    private void readExistingData(){
        Log.d(TAG, "readExistingData: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String idKey;
                String loopOrderNum;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+postSnapshot.toString());
                    idKey = postSnapshot.getKey().toString();
                    loopOrderNum = postSnapshot.child("orderNumber").getValue().toString();
                    if (loopOrderNum.equals(orderNumber)){
                        name = postSnapshot.child("name").getValue().toString();
                        orderNumber = postSnapshot.child("orderNumber").getValue().toString();
                        category = postSnapshot.child("category").getValue().toString();
                        receiveAddress = postSnapshot.child("receiveAddress").getValue().toString();
                        sendAddress = postSnapshot.child("sendAddress").getValue().toString();
                        sendMethod = postSnapshot.child("sendMethod").getValue().toString();
                        size = postSnapshot.child("size").getValue().toString();
                        thing = postSnapshot.child("thing").getValue().toString();
                        receivePeople = postSnapshot.child("receivePeople").getValue().toString();
                        receiveTime = postSnapshot.child("receiveTime").getValue().toString();
                        sendPeople = postSnapshot.child("sendPeople").getValue().toString();
                        sendTime = postSnapshot.child("sendTime").getValue().toString();
                        Log.d(TAG, "onDataChange: name = "+name);
                        Log.d(TAG, "onDataChange: orderNumber = "+loopOrderNum);
                        Log.d(TAG, "onDataChange: category = "+category);
                        Log.d(TAG, "onDataChange: receiveAddress = "+receiveAddress);
                        Log.d(TAG, "onDataChange: sendAddress = "+sendAddress);
                        Log.d(TAG, "onDataChange: sendMethod = "+sendMethod);
                        Log.d(TAG, "onDataChange: size = "+size);
                        Log.d(TAG, "onDataChange: thing = "+thing);
                        txt_orderContent_name.setText(name);
                        txt_orderContent_orderNumber.setText("訂單號碼: "+orderNumber);
                        txt_orderContent_category.setText(category);
                        txt_orderContent_size.setText(size);
                        txt_orderContent_thing.setText(thing);
                        txt_orderContent_receiveAddress.setText(receiveAddress);
                        txt_orderContent_sendAddress.setText(sendAddress);
                        txt_orderContent_sendMethod.setText(sendMethod);
                        txt_orderContent_receivePeople.setText(receivePeople);
                        txt_orderContent_receiveTime.setText(receiveTime);
                        txt_orderContent_sendPeople.setText(sendPeople);
                        txt_orderContent_sendTime.setText(sendTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}