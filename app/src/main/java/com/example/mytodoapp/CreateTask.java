package com.example.mytodoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateTask extends AppCompatActivity {
    EditText mname,mdate,mdetails;
    TextView mcolor;
    int id;
    ImageButton img_person, im_date;
    private int datepicker;
    private int monthPicked;
    private int yearPicked;


    TextView h;
    private int currentColor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        mname=findViewById(R.id.tname);
        mdate=findViewById(R.id.date);
        img_person=findViewById(R.id.imName);
        mdetails=findViewById(R.id.details);
        h=(TextView)findViewById(R.id.hex);

        im_date=findViewById(R.id.imaDate);
        Calendar calendar=Calendar.getInstance();
        datepicker=calendar.get(Calendar.DATE);
         yearPicked = calendar.get(Calendar.YEAR);
        id=getIntent().getExtras().getInt("userId");

    }

    public void submit(View view)
    {
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        try{
            Number currentIdNum=realm.where(Task.class).max("taskId");
            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
            Task task=realm.createObject(Task.class,nextId);
            task.setTaskName(mname.getText().toString());
            SimpleDateFormat f=new SimpleDateFormat("dd/MM/yyyy");
            Date d=f.parse(mdate.getText().toString());
            task.setDueDate(d);
            task.setTaskDetails(mdetails.getText().toString());
            task.setColor(h.getText().toString());
            task.setUserId(id);
            task.setChecked("false");
            realm.commitTransaction();
        }catch(Exception e){realm.cancelTransaction();}
        finally {
            realm.close();
        }
        onBackPressed();
    }

    public void pickColor(View view) {
        openDialog(false);
    }


    private void openDialog(boolean supportAlpha){
        AmbilWarnaDialog dialog=new AmbilWarnaDialog(this, currentColor, supportAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor=color;

                String s=(Integer.toHexString(color));
                String s1=s.substring(2);
                h.setText("#"+s1);
                h.setTextColor(color);
            }
        });

        dialog.show();
    }

    private void setDate() {
        mdate.setText(datepicker+"/"+(monthPicked+1)+"/"+yearPicked);
    }

    public void pickDate(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datepicker = dayOfMonth;
                monthPicked = month;
                yearPicked=year;
                setDate();
            }
        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,onDateSetListener,yearPicked,monthPicked,datepicker);
        datePickerDialog.show();
    }

    public void viewTasks(View view) {

        onBackPressed();
    }

    public void discard(View view)
    {
        onBackPressed();
    }

}
