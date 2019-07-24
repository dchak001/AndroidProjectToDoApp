package com.example.mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class Login_Activity extends AppCompatActivity {
  private EditText muserid;
  private  EditText mpass;
  private Button mlogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        muserid=findViewById(R.id.userId);
        mpass=findViewById(R.id.password);
        mlogin=findViewById(R.id.Login_b);

    }

    @Override
    protected void onResume() {
        super.onResume();
        muserid.setText("");
        mpass.setText("");
        muserid.requestFocus();
    }


    public void onLoginPressed(View view)
    {   try{
        int id=Integer.parseInt(muserid.getText().toString());
        String pass=mpass.getText().toString();
        Realm realm = Realm.getDefaultInstance();
        MyUser user = realm.where(MyUser.class)
                .equalTo("id", id)
                .findFirst();

            if(!pass.equals((user.getPassword())))
            {Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return ;}
            Intent intent=new Intent(this,Dashboard.class);
            Bundle b=new Bundle();
            b.putString("username",user.getName());
            b.putString("email",user.getEmail());
            b.putString("phone",user.getPhone());
            b.putString("id",Integer.toString(user.getId()));
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }catch (NullPointerException e)
        {
            Toast.makeText(this, "Invalid UserId", Toast.LENGTH_SHORT).show();
        } catch(NumberFormatException e)
        {
            Toast.makeText(this, "Enter username ", Toast.LENGTH_SHORT).show();
        }
    }
}
