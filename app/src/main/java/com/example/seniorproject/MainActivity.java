package com.example.seniorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//google maps 2
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText location;
    EditText description;
    DatabaseReference myRef;
    final int IMAGE_REQ_CODE = 100, CAMERA_CLICK_CODE = 101;
    final String MODEL_NAME = "raghad_model_new-fp16.tflite";

    Bitmap bitmap;
    Button imageCapture;
    ImageView imageView;
    Yolov5TFLiteDetector yolov5TFLiteDetector;

    Paint boxPaint = new Paint();
    Paint textPain = new Paint();

    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    GoogleMap myMap;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageCapture = findViewById(R.id.captureButton);

        boxPaint.setStrokeWidth(5);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setColor(Color.RED);
        textPain.setTextSize(50);
        textPain.setColor(Color.RED);
        textPain.setStyle(Paint.Style.FILL);

        //initialize yolo model
        yolov5TFLiteDetector = new Yolov5TFLiteDetector();
        yolov5TFLiteDetector.setModelFile(MODEL_NAME);
        yolov5TFLiteDetector.initialModel(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQ_CODE);
            }
        });
        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_CLICK_CODE);
            }
        });
        //map

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        description = findViewById(R.id.description);
        Button next = findViewById(R.id.next);


        // EditText sendFName = findViewById(R.id.sendFName);
        // EditText sendLName = findViewById(R.id.sendLName);
        // EditText sendPNumber = findViewById(R.id.sendPNumber);

        myRef = FirebaseDatabase.getInstance().getReference().child("Information");
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this, "Location permission is denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertData(){
            String loc = location.getText().toString();
            String desc = description.getText().toString();
            information info = new information(loc,desc);
            myRef.push().setValue(info);
        }

    public void onClickNext1(View view){

        insertData();
        Intent intent= new Intent(this, MainActivity2.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQ_CODE && data != null){
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImageBitmap(bitmap);
                Toast.makeText(this, "Image Selected successfully", Toast.LENGTH_SHORT).show();

                predict();

        }
        else if(requestCode == CAMERA_CLICK_CODE && data != null){
            bitmap = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, "Image Captured", Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(bitmap);

                predict();

        }
    }

    void predict() {

        // load model and run prediction on it
        ArrayList<Recognition> predictions = yolov5TFLiteDetector.detect(bitmap);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas cropCanvas = new Canvas(mutableBitmap);
        String label = null;
        for (Recognition res : predictions) {
            RectF location = res.getLocation();
            label = res.getLabelName();
            float confidence = res.getConfidence();
            if (confidence < 0.1) {
                continue;
            }

            cropCanvas.drawRect((location.left / 320) * bitmap.getWidth(), (location.top / 320) * bitmap.getHeight(), (location.right / 320) * bitmap.getWidth(), (location.bottom / 320) * bitmap.getHeight(), boxPaint);
            cropCanvas.drawText(label + ":" + String.format("%.2f", confidence), (location.left / 320) * bitmap.getWidth(), (location.top / 320) * bitmap.getHeight(), textPain);

        }

        imageView.setImageBitmap(mutableBitmap);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng current = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        myMap.addMarker(new MarkerOptions().position(current).title("current location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(current));

    }
}