package hackathon.project1.commuter;//package hackathon.project1.commuter;
//
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class Cab_Frag extends Fragment {
//
//
//    public Cab_Frag() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cab_, container, false);
//    }
//
//}
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hackathon.project1.commuter.GetNearbyPlacesData;
import hackathon.project1.commuter.R;

public class Cab_Frag extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    double latitude, longitude;
    double end_latitude, end_longitude;
    int PROXIMITY_RADIUS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);


    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            // Log.d("onLocationChanged", "Removing Location Updates");
        }
    }


    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        switch (v.getId()) {
            case R.id.button:
                EditText tf_location = findViewById(R.id.TF_Location);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions mo = new MarkerOptions();
                if (!location.equals("")) {
//                    Geocoder geocoder = new Geocoder(this);
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 5);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    for (int i = 0; i < addressList.size(); i++) {
//                        Address myAddress = addressList.get(i);
//                        LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
//                        mo.position(latLng);
//                        mo.title("Your Search result");
                    if(lastLocation!=null) {
                        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + ",-81.38992&destination="+ location+ "&key=AIzaSyCMfj9lDjQ1imr6X1y4mSmAzqDPct-l0bQ";
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    JSONArray array = object.getJSONArray("response");
                                    JSONObject obj = array.getJSONObject(0);
                                    JSONArray steps = obj.getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                                    ArrayList<LatLng>locations = new ArrayList<>();
                                    for(int  i=0;i<steps.length();i++){
                                        JSONObject obj1 = steps.getJSONObject(i);
                                        JSONObject start = obj1.getJSONObject("start_location");
                                        LatLng latLng = new LatLng(start.getDouble("lat"),start.getDouble("lng"));
                                        locations.add(latLng);
                                    }
                                    Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                                            .clickable(true)
                                            .addAll(locations)
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Responze",error.getMessage());
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(request);

                    }
                }
//                }

                break;
            case R.id.Go:
                //mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(end_latitude, end_longitude));
                markerOptions.title("Destination");
                markerOptions.draggable(true);
                float result[] = new float[10];
                Location.distanceBetween(latitude, longitude, end_latitude, end_longitude, result);
                markerOptions.snippet("Distance" + result[0]);
                mMap.addMarker(markerOptions);

                String busstop = "busstop";
                String url = getUrl(latitude, longitude, busstop);
                url = getUrl(latitude, longitude, busstop);

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(Cab_Frag.this, "Showing Nearby Bustops", Toast.LENGTH_LONG).show();
                break;

        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlaces) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/places/nearbysearch/");
        googlePlaceUrl.append("location" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlaces);
        googlePlaceUrl.append("&sensor=true" + "AIzaSyCMfj9lDjQ1imr6X1y4mSmAzqDPct-l0bQ");
        googlePlaceUrl.append("&key=");
        return googlePlaceUrl.toString();

    }

    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
        googleDirectionsUrl.append("&destination=" + end_latitude + "," + end_longitude);
        googleDirectionsUrl.append("&key=" + "AIzaSyBBCj33r8Ks8kEgIvC9hUwZ4h4Q2UvmgyY");

        return googleDirectionsUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
    }


}