package com.example.user.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    double lat, lng;
    TextView textView;
    Button next;
    String userId;
    String name;
    String mobnum;
    EditText nt,et;
    public String msg;
     // private Datab
   // aseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
        nt=(EditText) findViewById(R.id.name);
        et=(EditText) findViewById(R.id.mobnum);
    }


    public void showMap(View v) {

        name = nt.getText() + "";
        mobnum = et.getText() + "";
        if (!name.equals("") && !mobnum.equals(""))
        {
            Intent i = new Intent(this, GoogleMapsActivity.class);
            i.putExtra("name", name);
            i.putExtra("mobile",mobnum);
            startActivity(i);

        } else {
            Toast.makeText(this, "Plz Enter Details", Toast.LENGTH_SHORT).show();
        }
    }
}
