package com.codemaven.xadapp.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GPSTracker extends Service implements LocationListener {
    private final Context context;
    boolean isGpsEnable = false;
    boolean isNetworkEnable = false;
    boolean canGetLocation = false;
    double latitide;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;
    LocationManager locationManager;
    Location location;

    public GPSTracker(Context context) {
        this.context = context;
   //     getLocation();
    }

//    public Location getLocation() {
//        try {
//            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGpsEnable && !isNetworkEnable) {
//
//            } else {
//                this.canGetLocation = true;
//                if (isNetworkEnable) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return todo;
//                    }
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATE,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
//
//                    if (locationManager != null) {
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitide = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
