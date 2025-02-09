package com.example.kokomove_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity {
    private String TAG = "CreateItemActivity";
    private EditText edit_createItem_name,edit_createItem_thing,edit_createItem_receiveAddress,edit_createItem_sendAddress;
    private Spinner spin_createItem_size,spin_createItem_category,spin_createItem_sendMethod;
    private Button btn_createItem_add;
    private String userName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String name,thing,size,category,sendMethod,receiveAddress,sendAddress,orderNumber;
    private int orderCunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        initView();
    }

    private void initView(){
        size = "小";
        category = "五金類";
        sendMethod = "機車";
        orderCunt = 1;

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        userName = getPrefs.getString("userName", "");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OrderInfo");

        edit_createItem_name = (EditText) findViewById(R.id.edit_createItem_name);
        edit_createItem_thing = (EditText) findViewById(R.id.edit_createItem_thing);
        edit_createItem_receiveAddress = (EditText) findViewById(R.id.edit_createItem_receiveAddress);
        edit_createItem_sendAddress = (EditText) findViewById(R.id.edit_createItem_sendAddress);

        edit_createItem_name.setText(userName);
        /*edit_createItem_thing.setText("鐵箱");
        edit_createItem_receiveAddress.setText("台北市");
        edit_createItem_sendAddress.setText("台中市");*/

        spin_createItem_size = (Spinner) findViewById(R.id.spin_createItem_size);
        List<String> tagItem1 = Arrays.asList("小","中","大");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_layout , tagItem1);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spin_createItem_size.setAdapter(adapter);
        spin_createItem_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tag1_position = position;
                size = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: tag1 = "+size);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_createItem_category = (Spinner) findViewById(R.id.spin_createItem_category);
        List<String> tagItem2 = Arrays.asList("五金類","玻璃易碎品","生鮮食品");
        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_layout , tagItem2);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spin_createItem_category.setAdapter(adapter2);
        spin_createItem_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tag1_position = position;
                category = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: tag1 = "+category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_createItem_sendMethod = (Spinner) findViewById(R.id.spin_createItem_sendMethod);
        List<String> tagItem3 = Arrays.asList("機車","廂型貨車","貨車","無人機");
        ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_layout , tagItem3);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spin_createItem_sendMethod.setAdapter(adapter3);
        spin_createItem_sendMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tag1_position = position;
                sendMethod = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: tag1 = "+sendMethod);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_createItem_add = (Button) findViewById(R.id.btn_createItem_add);
        btn_createItem_add.setOnClickListener(onClick);
        readExistingData();
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_createItem_add){
                Log.d(TAG, "onClick: btn_createItem_add");
                addNew();
            }
        }
    };

    private void addNew(){
        int r = 0;
        r = (int)(Math.random()*100)+1;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        Log.d(TAG, "Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        //orderNumber = ""+month+"/"+day+"/"+hour+":"+minute;
        orderNumber = ""+month+day+hour+minute+r;

        name = edit_createItem_name.getText().toString();
        thing = edit_createItem_thing.getText().toString();
        receiveAddress = edit_createItem_receiveAddress.getText().toString();
        sendAddress = edit_createItem_sendAddress.getText().toString();
        Log.d(TAG, "addNew: thing = "+thing);

        UploadOrder uploadOrder = new UploadOrder("",userName,thing,size,category,sendMethod,receiveAddress,sendAddress,orderNumber,
                "","","","");
        myRef.child("order"+orderCunt).setValue(uploadOrder);
        finish();
    }

    private void readExistingData(){
        Log.d(TAG, "readExistingData: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name,orderNumber,category,receiveAddress,sendAddress,sendMethod, size,thing;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+postSnapshot.toString());
                    name = postSnapshot.child("name").getValue().toString();
                    orderNumber = postSnapshot.child("orderNumber").getValue().toString();
                    category = postSnapshot.child("category").getValue().toString();
                    receiveAddress = postSnapshot.child("receiveAddress").getValue().toString();
                    sendAddress = postSnapshot.child("sendAddress").getValue().toString();
                    sendMethod = postSnapshot.child("sendMethod").getValue().toString();
                    size = postSnapshot.child("size").getValue().toString();
                    thing = postSnapshot.child("thing").getValue().toString();
                    Log.d(TAG, "onDataChange: name = "+name);
                    Log.d(TAG, "onDataChange: orderNumber = "+orderNumber);
                    Log.d(TAG, "onDataChange: category = "+category);
                    Log.d(TAG, "onDataChange: receiveAddress = "+receiveAddress);
                    Log.d(TAG, "onDataChange: sendAddress = "+sendAddress);
                    Log.d(TAG, "onDataChange: sendMethod = "+sendMethod);
                    Log.d(TAG, "onDataChange: size = "+size);
                    Log.d(TAG, "onDataChange: thing = "+thing);
                    orderCunt++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}