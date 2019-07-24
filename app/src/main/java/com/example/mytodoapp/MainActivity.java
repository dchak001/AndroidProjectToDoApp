package com.example.mytodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    private Button log;
    private Button sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log=findViewById(R.id.button);
        sign=findViewById(R.id.button2);

    }

    public void signUpFunc(View view){
        Intent intent = new Intent(this,signUp_activity.class);
        startActivity(intent);
    }

   public void logIn(View view){
        Intent intent = new Intent(this,Login_Activity.class);
        startActivity(intent);
    }
}
