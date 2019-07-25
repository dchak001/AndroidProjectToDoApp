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

public class signUp_activity extends AppCompatActivity {

    private Context mcontext;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;
    private TextView idLabel;
    private TextView idHolder;
    private ImageView img;
    private Button saveBtn,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        mcontext=this;
        name=findViewById(R.id.name_et);
        email=findViewById(R.id.mail_et);
        phone=findViewById(R.id.phone_et);
        password=findViewById(R.id.password_et);
        idLabel=findViewById(R.id.id_tv);
        idHolder=findViewById(R.id.id_et);
        img=findViewById(R.id.avatar_iv);
        saveBtn=findViewById(R.id.button_save);
        login=findViewById(R.id.button_login);


    }

    public void onSavePressed(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        try
        {
            Number currentIdNum=realm.where(MyUser.class).max("id");
            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
            MyUser user = realm.createObject(MyUser.class, nextId);
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setPassword(password.getText().toString());
            idHolder.setText(Integer.toString(user.getId()));
            realm.commitTransaction();
            Log.i("signup","object stored");
            //Toast.makeText(mcontext, "Success", Toast.LENGTH_SHORT).show();



        }
        catch (Exception ex){
            realm.cancelTransaction();
            Toast.makeText(mcontext, "Failure" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            realm.close();
        }
    }
    public void moveTologin(View view){
        Intent intent=new Intent(this,Login_Activity.class);
        startActivity(intent);
        finish();
    }
}

