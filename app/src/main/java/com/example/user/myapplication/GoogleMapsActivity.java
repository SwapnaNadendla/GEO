package com.example.user.myapplication;

/**
 * Created by user on 14-Dec-17.
 */


import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener  {

    LocationRequest request;

    GoogleApiClient apiClient;
    private GoogleMap mMap;
    double lat,lng;
    String msg;
    String name;
    String mobnum;
    String userId;

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        buildApiClient();
        apiClient.connect();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString("name");
        mobnum=bundle.getString("mobile");
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);

        if (location!=null){
            lat=location.getLatitude();
            lng=location.getLongitude();

            User user = new User(userId,mobnum,lat,lng);
            mDatabase.child(mobnum).setValue(user);
            Toast.makeText(this,mobnum+" * "+name +"*"+lat+" * "+lng,Toast.LENGTH_SHORT);
        }
        else {
            Toast.makeText(this, "Location is Null", Toast.LENGTH_SHORT).show();
        }
// Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               // String value = dataSnapshot.getValue(String.class);
                mMap.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                   // mMap = googleMap;
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                    // Add a marker in Sydney and move the camera
                    LatLng myLocation = new LatLng(user.getLatitude(),user.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLocation).title(user.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_smartphone_black_24dp)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,13));

                }
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      //  mMap.getUiSettings().setMyLocationButtonEnabled(true);
       // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // Add a marker in Sydney and move the camera
        //LatLng myLocation = new LatLng(16.35572, 80.530);
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("My Current Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

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
            User user = new User(userId,mobnum,lat,lng);
            mDatabase.child(mobnum).setValue(user);
            msg = "lat: " + lat + ",lng: " + lng;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        }

    }
   /* private void writeNewUser(String userId, String name, String msg) {
        User user = new User(name, msg);

        mDatabase.child("users").child(userId).setValue(user);
    }*/


}