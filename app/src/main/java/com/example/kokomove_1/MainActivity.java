package com.example.kokomove_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences getPrefs;
    private SharedPreferences.Editor editor;
    private Button btn_main_signUp,btn_main_signIn;
    private TextView txt_main_condition,txt_main_forgetPassword;
    private CheckBox cb_main_checkStatus;
    private TextInputLayout textInput_main_account_layout,textInput_main_password_layout;
    private EditText edit_main_account,edit_main_password;
    private ImageView img_main_watchPassword;
    private String account,password;
    private boolean flag_password;
    private String[] accountBuf = new String[100];
    private String[] passwordBuf = new String[100];
    private String[] nameBuf = new String[100];
    private String[] statusBuf = new String[100];
    private int userCunt;
    private int correctCunt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        flag_password = false;
        account = "";
        password = "";
        userCunt = 0;
        correctCunt = 0;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserInfo");

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        getPrefs.getString("userName", "");
        getPrefs.getString("userAccount", "");
        getPrefs.getString("userStatus", "");

        edit_main_account = (EditText)findViewById(R.id.edit_main_account);
        edit_main_password = (EditText)findViewById(R.id.edit_main_password);
        img_main_watchPassword = (ImageView)findViewById(R.id.img_main_watchPassword);
        txt_main_forgetPassword = (TextView) findViewById(R.id.txt_main_forgetPassword);
        textInput_main_account_layout = (TextInputLayout) findViewById(R.id.textInput_main_account_layout);
        textInput_main_password_layout = (TextInputLayout) findViewById(R.id.textInput_main_password_layout);
        btn_main_signUp = (Button)findViewById(R.id.btn_main_signUp);
        btn_main_signIn = (Button)findViewById(R.id.btn_main_signIn);
        btn_main_signUp.setOnClickListener(onClick);
        btn_main_signIn.setOnClickListener(onClick);
        img_main_watchPassword.setOnClickListener(onClick);
        txt_main_forgetPassword.setOnClickListener(onClick);

        /*edit_main_account.setText(bc");
        edit_main_password.setText("123");*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        readExistingData();
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_main_signUp){
                Log.d(TAG, "onClick: btn_main_signUp");
                //myRef.child("Test01").setValue("Hello, World1111118888222!");
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }else if(v.getId() == R.id.btn_main_signIn){
                Log.d(TAG, "onClick: btn_main_signIn");
                account = edit_main_account.getText().toString();
                password = edit_main_password.getText().toString();
                if(checkEditViewEmpty()){
                    if(checkAccount() && checkPassword()){
                        Intent intent;
                        if(statusBuf[correctCunt].equals("一般會員")){
                            intent = new Intent(getApplicationContext(), HomeActivity.class);
                        }else {
                            intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
                        }
                        editor = getPrefs.edit();
                        editor.putString("userName", nameBuf[correctCunt]);
                        editor.putString("userAccount", accountBuf[correctCunt]);
                        editor.putString("userStatus", statusBuf[correctCunt]);
                        editor.apply();
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"帳號密碼錯誤",Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(v.getId() == R.id.img_main_watchPassword){
                Log.d(TAG, "onClick: img_main_watchPassword");
                if(flag_password){
                    edit_main_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag_password = false;
                }else {
                    edit_main_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag_password = true;
                }
            }else if(v.getId() == R.id.txt_main_forgetPassword){
                Log.d(TAG, "onClick: txt_main_forgetPassword");
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        }
    };

    private boolean checkAccount(){
        for (int i=0; i<userCunt ; i++){
            if(account.equals(accountBuf[i])){
                correctCunt = i;
                return true;
            }
        }
        return false;
    }

    private boolean checkPassword(){
        for (int i=0; i<userCunt ; i++){
            if(password.equals(passwordBuf[i])){
                return true;
            }
        }
        return false;
    }

    private boolean checkEditViewEmpty(){
        if(TextUtils.isEmpty(account)){
            textInput_main_account_layout.setError("請輸入帳號");
            textInput_main_password_layout.setError("");
            return false;
        }else if(TextUtils.isEmpty(password)){
            textInput_main_account_layout.setError("");
            textInput_main_password_layout.setError("請輸入密碼");
            return false;
        }else {
            textInput_main_account_layout.setError("");
            textInput_main_password_layout.setError("");
            return true;
        }
    }

    private void readExistingData(){
        Log.d(TAG, "readExistingData: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCunt = 0;
                //Log.d(TAG, "onDataChange: user001 ="+dataSnapshot.child("user001").child("account").getValue().toString());
                //Log.d(TAG, "onDataChange: user002 ="+dataSnapshot.child("user002").child("account").getValue().toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+snapshot.toString());
                    Log.d(TAG, "onDataChange: userCunt = " + userCunt);
                    accountBuf[userCunt] = dataSnapshot.child("user"+userCunt).child("account").getValue().toString();
                    passwordBuf[userCunt] = dataSnapshot.child("user"+userCunt).child("password").getValue().toString();
                    nameBuf[userCunt] = dataSnapshot.child("user"+userCunt).child("name").getValue().toString();
                    statusBuf[userCunt] = dataSnapshot.child("user"+userCunt).child("userStatus").getValue().toString();
                    userCunt++;
                }
                Log.d(TAG, "onDataChange: ---------userCunt = " + userCunt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}