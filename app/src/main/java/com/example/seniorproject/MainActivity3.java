package com.example.seniorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity3 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


    }
    public void onClickButt3(View view){
        Intent intent= new Intent(this, MainActivity4.class);
        startActivity(intent);
    }

    public void OnClickPrev(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}