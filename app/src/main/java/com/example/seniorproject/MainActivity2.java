package com.example.seniorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
    public void onClickButt2(View view){
        EditText sendName=(EditText) findViewById(R.id.sendFName);
        EditText sendFam=(EditText) findViewById(R.id.sendLName);
        EditText sendNumber=(EditText) findViewById(R.id.sendPNumber);

        String sendName1=sendName.getText().toString();
        String sendFam1=sendFam.getText().toString();
        String sendNumber1=sendNumber.getText().toString();




        Intent intent =new Intent(this, MainActivity3.class);




        startActivity(intent);
    }
}