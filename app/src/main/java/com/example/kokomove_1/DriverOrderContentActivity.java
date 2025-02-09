package com.example.kokomove_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DriverOrderContentActivity extends AppCompatActivity {
    private String TAG = "DriverOrderContentActivity";
    private TextView txt_driverOrderContent_orderNumber,txt_driverOrderContent_name,txt_driverOrderContent_size,txt_driverOrderContent_category,
            txt_driverOrderContent_thing,txt_driverOrderContent_receiveAddress,txt_driverOrderContent_sendAddress,txt_driverOrderContent_sendMethod;
    private TextView txt_driverOrderContent_receivePeople,txt_driverOrderContent_receiveTime,txt_driverOrderContent_sendPeople,
            txt_driverOrderContent_sendTime;
    private Button btn_driverOrderContent_back,btn_driverOrderContent_receive,btn_driverOrderContent_send;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String idKey;
    private String name,orderNumber,category,receiveAddress,sendAddress,sendMethod, size,thing;
    private String receivePeople,receiveTime,sendPeople,sendTime;
    private String userName,userAccount,userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_content);
        initView();
    }

    private void initView(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OrderInfo");

        orderNumber = getIntent().getStringExtra("orderNumber");

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        userName = getPrefs.getString("userName", "");
        userAccount = getPrefs.getString("userAccount", "");
        userStatus = getPrefs.getString("userStatus", "");

        txt_driverOrderContent_orderNumber = (TextView) findViewById(R.id.txt_driverOrderContent_orderNumber);
        txt_driverOrderContent_name = (TextView) findViewById(R.id.txt_driverOrderContent_name);
        txt_driverOrderContent_size = (TextView) findViewById(R.id.txt_driverOrderContent_size);
        txt_driverOrderContent_category = (TextView) findViewById(R.id.txt_driverOrderContent_category);
        txt_driverOrderContent_thing = (TextView) findViewById(R.id.txt_driverOrderContent_thing);
        txt_driverOrderContent_receiveAddress = (TextView) findViewById(R.id.txt_driverOrderContent_receiveAddress);
        txt_driverOrderContent_sendAddress = (TextView) findViewById(R.id.txt_driverOrderContent_sendAddress);
        txt_driverOrderContent_sendMethod = (TextView) findViewById(R.id.txt_driverOrderContent_sendMethod);
        txt_driverOrderContent_receivePeople = (TextView) findViewById(R.id.txt_driverOrderContent_receivePeople);
        txt_driverOrderContent_receiveTime = (TextView) findViewById(R.id.txt_driverOrderContent_receiveTime);
        txt_driverOrderContent_sendPeople = (TextView) findViewById(R.id.txt_driverOrderContent_sendPeople);
        txt_driverOrderContent_sendTime = (TextView) findViewById(R.id.txt_driverOrderContent_sendTime);
        btn_driverOrderContent_back = (Button) findViewById(R.id.btn_driverOrderContent_back);
        btn_driverOrderContent_receive = (Button) findViewById(R.id.btn_driverOrderContent_receive);
        btn_driverOrderContent_send = (Button) findViewById(R.id.btn_driverOrderContent_send);
        btn_driverOrderContent_back.setOnClickListener(onClick);
        btn_driverOrderContent_receive.setOnClickListener(onClick);
        btn_driverOrderContent_send.setOnClickListener(onClick);
        
        readExistingData();

    }
    
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_driverOrderContent_back){
                Log.d(TAG, "onClick: btn_driverOrderContent_back");
                finish();
            }else if(view.getId() == R.id.btn_driverOrderContent_receive){
                Log.d(TAG, "onClick: ");
                RXTXItem("收貨");
            }else if(view.getId() == R.id.btn_driverOrderContent_send){
                Log.d(TAG, "onClick: ");
                RXTXItem("送達");
            }
        }
    };

    private void RXTXItem(String status){
        String time;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        Log.d(TAG, "Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        time = ""+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second;

        if(status.equals("收貨")){
            receiveTime = time;
            receivePeople = userName;
            Log.d(TAG, "receiveItem: receiveTime = "+receiveTime);
            Log.d(TAG, "receiveItem: receivePeople = "+receivePeople);
            Log.d(TAG, "receiveItem: sendTime = "+sendTime);
            Log.d(TAG, "receiveItem: sendPeople = "+sendPeople);
            Log.d(TAG, "receiveItem: idKey = "+idKey);
        }else {
            sendTime = time;
            sendPeople = userName;
            Log.d(TAG, "sendItem: receiveTime = "+receiveTime);
            Log.d(TAG, "sendItem: receivePeople = "+receivePeople);
            Log.d(TAG, "sendItem: sendTime = "+sendTime);
            Log.d(TAG, "sendItem: sendPeople = "+sendPeople);
            Log.d(TAG, "sendItem: idKey = "+idKey);
        }
        UploadOrder uploadOrder = new UploadOrder(idKey,name,thing,size,category,sendMethod,receiveAddress,sendAddress,
                orderNumber,receivePeople,receiveTime,sendPeople,sendTime);
        myRef.child(idKey).setValue(uploadOrder);

    }

    private void readExistingData(){
        Log.d(TAG, "readExistingData: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String loopOrderNum;
                String loopIdKey;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+postSnapshot.toString());
                    loopIdKey = postSnapshot.getKey().toString();
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
                        Log.d(TAG, "onDataChange: receivePeople = "+receivePeople);
                        Log.d(TAG, "onDataChange: receiveTime = "+receiveTime);
                        Log.d(TAG, "onDataChange: sendPeople = "+sendPeople);
                        Log.d(TAG, "onDataChange: sendTime = "+sendTime);
                        idKey = loopIdKey;
                        txt_driverOrderContent_name.setText(name);
                        txt_driverOrderContent_orderNumber.setText("訂單號碼: "+orderNumber);
                        txt_driverOrderContent_category.setText(category);
                        txt_driverOrderContent_size.setText(size);
                        txt_driverOrderContent_thing.setText(thing);
                        txt_driverOrderContent_receiveAddress.setText(receiveAddress);
                        txt_driverOrderContent_sendAddress.setText(sendAddress);
                        txt_driverOrderContent_sendMethod.setText(sendMethod);
                        txt_driverOrderContent_receivePeople.setText(receivePeople);
                        txt_driverOrderContent_receiveTime.setText(receiveTime);
                        txt_driverOrderContent_sendPeople.setText(sendPeople);
                        txt_driverOrderContent_sendTime.setText(sendTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}