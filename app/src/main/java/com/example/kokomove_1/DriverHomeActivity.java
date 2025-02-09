package com.example.kokomove_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverHomeActivity extends AppCompatActivity {
    private String TAG = "DriverHomeActivity";
    private TextView txt_driverHome_userStatus,txt_driverHome_name;
    private String userName,userAccount,userStatus;
    private Button btn_driverHome_contact;
    private ListView lv_driverHome_list;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<UploadOrder> dataList;
    private OrderAdapter orderAdapter;
    private String name,orderNumber,category,receiveAddress,sendAddress,sendMethod, size,thing;
    private String receivePeople,receiveTime,sendPeople,sendTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        initView();
    }

    private void initView(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OrderInfo");

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        userName = getPrefs.getString("userName", "");
        userAccount = getPrefs.getString("userAccount", "");
        userStatus = getPrefs.getString("userStatus", "");

        txt_driverHome_userStatus = (TextView) findViewById(R.id.txt_driverHome_userStatus);
        txt_driverHome_name = (TextView) findViewById(R.id.txt_driverHome_name);
        btn_driverHome_contact = (Button)findViewById(R.id.btn_driverHome_contact);
        btn_driverHome_contact.setOnClickListener(onClick);
        txt_driverHome_userStatus.setText("會員: "+userStatus);
        txt_driverHome_name.setText("姓名: "+userName);

        lv_driverHome_list = (ListView) findViewById(R.id.lv_driverHome_list);
        dataList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, dataList);
        lv_driverHome_list.setAdapter(orderAdapter);
        readExistingData();
        lv_driverHome_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DriverOrderContentActivity.class);
                intent.putExtra("orderNumber",dataList.get(i).getOrderNumber());
                startActivity(intent);
            }
        });
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_driverHome_contact){
                Log.d(TAG, "onClick: btn_driverHome_contact");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverHomeActivity.this);
                View v = getLayoutInflater().inflate(R.layout.set_custom_dialog_layout,null);
                alertDialog.setView(v);
                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));

                AlertDialog dialog = alertDialog.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
                    dialog.dismiss();
                }));
                dialog.setCanceledOnTouchOutside(false);
            }
        }
    };

    private void readExistingData(){
        Log.d(TAG, "readExistingData: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String idKey;
                dataList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+postSnapshot.toString());
                    idKey = postSnapshot.getKey().toString();
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
                    Log.d(TAG, "onDataChange: orderNumber = "+orderNumber);
                    Log.d(TAG, "onDataChange: category = "+category);
                    Log.d(TAG, "onDataChange: receiveAddress = "+receiveAddress);
                    Log.d(TAG, "onDataChange: sendAddress = "+sendAddress);
                    Log.d(TAG, "onDataChange: sendMethod = "+sendMethod);
                    Log.d(TAG, "onDataChange: size = "+size);
                    Log.d(TAG, "onDataChange: thing = "+thing);
                    UploadOrder uploadOrder = new UploadOrder(idKey,name,thing,size,category,sendMethod,receiveAddress,sendAddress,
                            orderNumber,receivePeople,receiveTime,sendPeople,sendTime);
                    dataList.add(uploadOrder);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}