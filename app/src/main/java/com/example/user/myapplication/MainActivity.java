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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    GoogleApiClient apiClient;
    LocationRequest request;
    double lat, lng;
    TextView textView;
    Button next;
    String userId;
    String name;
    String mobnum;
    EditText nt,et;
    public String msg;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
   // private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = mDatabase.push().getKey();
        setContentView(R.layout.activity_main);
        nt=(EditText) findViewById(R.id.name);
        et=(EditText) findViewById(R.id.mobnum);

        textView=(TextView) findViewById(R.id.location);
        buildApiClient();
        apiClient.connect();


    }

    /*public void receiveData(View view) {
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                //Toast.makeText(getApplicationContext(),""+user.getName()+user.getMsg(),Toast.LENGTH_LONG);
                et.setText(user.getMsg());
                //Log.d(TAG, "User name: " + user.getName() + ", email " + user.getMsg());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }*/

    public static class User {

        public String name;
        public String mobnum;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String mobnum) {
            this.name = name;
            this.mobnum= mobnum;
        }
        public String getName()
        {
            return  this.name;
        }
        public String getMobnum()
        {
            return this.mobnum;
        }

    }
    protected synchronized void buildApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(3000);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            msg = "lat: " + lat + ",lng: " + lng;
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            name=nt.getText()+"";
            mobnum=et.getText()+"";
            if(name!="")
            {
            User user = new User(name,mobnum);
            mDatabase.child(userId).setValue(user);//storing to data base
            textView.setText(msg);
            }
        } else {
            Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();

        }

    }
   /* private void writeNewUser(String userId, String name, String msg) {
        User user = new User(name, msg);

        mDatabase.child("users").child(userId).setValue(user);
    }*/

    public void get(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);

        if (location!=null){
            lat=location.getLatitude();
            lng=location.getLongitude();
            msg="Lat : "+lat+" , Lng : "+lng;
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            User user = new User(name,mobnum);
            mDatabase.child(userId).setValue(user);
            Toast.makeText(this,msg+" "+userId,Toast.LENGTH_SHORT);
            textView.setText(msg);
        }
        else {
            Toast.makeText(this, "Location is Null", Toast.LENGTH_SHORT).show();
        }
    }
    public void showMap(View v){

        Intent i=new Intent(this,GoogleMapsActivity.class);
        i.putExtra("LAT",lat);
        i.putExtra("LNG",lng);
        startActivity(i);

    }
}
