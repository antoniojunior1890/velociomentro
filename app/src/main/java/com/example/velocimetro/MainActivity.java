package com.example.velocimetro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tv_speed;
    Typeface type_tf_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_speed = findViewById(R.id.tv_speed);

        type_tf_speed = Typeface.createFromAsset(this.getAssets(), "fonts/digital-7.ttf");
        tv_speed.setTypeface(type_tf_speed);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            doStuff();
        }

        this.updateSpeed(null);

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            CLocation cLocation = new CLocation(location);
            this.updateSpeed(cLocation);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    private void doStuff() {
        LocationManager locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);
        }
        Toast.makeText(this, "Conectando ao GPS!", Toast.LENGTH_SHORT).show();
    }

    private void updateSpeed(CLocation cLocation){
        float nCurrentSpeed = 0;

        if(cLocation != null){
            nCurrentSpeed = cLocation.getSpeed();
        }

        Formatter formatter = new Formatter(new StringBuilder());
        formatter.format(Locale.US,"%2.2f", nCurrentSpeed);
        String strCurrentSpeed = formatter.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ","0");
        strCurrentSpeed = strCurrentSpeed.replace(".",",");

        tv_speed.setText(strCurrentSpeed);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doStuff();
            }else {
                finish();
            }
        }
    }
}
