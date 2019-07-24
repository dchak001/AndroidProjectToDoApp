package com.example.mytodoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private Context mcontext;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;
    private TextView idLabel;
    private TextView idHolder;
    private ImageView img;
    private Button saveBtn,login;
    private int id;
    MyUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mcontext = this;
        name = findViewById(R.id.name_et);
        email = findViewById(R.id.mail_et);
        phone = findViewById(R.id.phone_et);
        password = findViewById(R.id.password_et);
        idLabel = findViewById(R.id.id_tv);
        idHolder = findViewById(R.id.id_et);
        img = findViewById(R.id.avatar_iv);
        saveBtn = findViewById(R.id.button_save);
        login = findViewById(R.id.button_login);
        Bundle b=getIntent().getExtras();
        try {
           id = Integer.parseInt(b.getString("id"));

        }catch (Exception e){  Log.i("userid"," "+e);}
        String n=b.getString("name");

       try{
        Realm realm = Realm.getDefaultInstance();
        user = realm.where(MyUser.class).equalTo("id", id).findFirst();
        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        password.setText(user.getPassword());
    }catch(Exception e){}

    }

    public void onSavePressed(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try
        {
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setPassword(password.getText().toString());
            realm.commitTransaction();




        }
        catch (Exception ex){
            realm.cancelTransaction();
            Toast.makeText(mcontext, "Failure" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            realm.close();
        }
       onBackPressed();

    }
  
}

