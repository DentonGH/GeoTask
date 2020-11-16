package com.cagriyorguner.geotask.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagriyorguner.geotask.Adapters.CustomMenuAdapter;
import com.cagriyorguner.geotask.Database.RoomDB;
import com.cagriyorguner.geotask.Fragments.FormFragment;
import com.cagriyorguner.geotask.Models.ApiResponseModels.BaseModel;
import com.cagriyorguner.geotask.Models.ApiResponseModels.Job;
import com.cagriyorguner.geotask.Models.ApiResponseModels.Vehicle;
import com.cagriyorguner.geotask.Models.CustomMenuItem;
import com.cagriyorguner.geotask.Models.PostModel;
import com.cagriyorguner.geotask.Models.User;
import com.cagriyorguner.geotask.R;
import com.cagriyorguner.geotask.network.JsonPlaceHolderApi;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        CustomMenuAdapter.AdapterCallback, GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private FloatingActionButton addButton;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    FrameLayout frameLayout;
    private Fragment fragment;
    LatLng currentLocation;
    LatLng lastMarkerPosition;
    String lastLocationName;
    LinearLayout menuLinearLayout;
    Button createRouteButton;

    RecyclerView menuRecyclerView;
    LinearLayoutManager layoutManager;
    CustomMenuAdapter adapter;
    List<CustomMenuItem> customMenuItemsList = new ArrayList<>();
    List<CustomMenuItem> sortedCustomMenuItemsList = new ArrayList<>();
    List<LatLng> endPoints = new ArrayList<>();
    RoomDB database;

    User authenticatedUser;

    boolean tickButtonLock;

    //polyline object
    private List<Polyline> polyLines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializations
        database = RoomDB.getInstance(this);
        authenticatedUser = database.mainDao().getAuthenticatedUser(true);
        addButton = findViewById(R.id.addButton);
        frameLayout = findViewById(R.id.frame_layout);
        menuLinearLayout = findViewById(R.id.menuLinearLayout);
        createRouteButton = findViewById(R.id.createRouteButton);
        //pull data from db and initialize recyclerview for menu screen
        customMenuItemsList = database.mainDao().getAllCustomMenuItems(authenticatedUser.getID());
        recyclerViewBuilder();

        //onClickListeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addButton.getTag().equals("add")){
                    if(currentLocation != null){
                        addButton.setTag("tick");
                        addButton.setImageResource(R.drawable.ic_baseline_check_24);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(currentLocation.latitude + "," + currentLocation.longitude).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11.0f));
                        lastMarkerPosition = currentLocation;
                        tickButtonLock = true;
                        marker.setTitle(lastMarkerPosition.latitude + "," + lastMarkerPosition.longitude);
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try{
                            List<Address> listAddresses = geocoder.getFromLocation(lastMarkerPosition.latitude, lastMarkerPosition.longitude, 1);
                            if(listAddresses != null && listAddresses.size() > 0){
                                String address = "";
                                if(listAddresses.get(0).getThoroughfare() != null){
                                    address += listAddresses.get(0).getThoroughfare() + " ";
                                }
                                if(listAddresses.get(0).getLocality() != null){
                                    address += listAddresses.get(0).getLocality() + " ";
                                }
                                if(listAddresses.get(0).getAdminArea() != null){
                                    address += listAddresses.get(0).getAdminArea() + " ";
                                }
                                lastLocationName = address;
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        tickButtonLock = false;
                    }
                }
                else if (addButton.getTag().equals("tick") && !tickButtonLock){
                    addButton.setTag("add");
                    addButton.setImageResource(R.drawable.ic_baseline_add_24);

                    addButton.setVisibility(View.GONE);
                    fragment = new FormFragment(lastMarkerPosition, lastLocationName, "", "");
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).addToBackStack("FormFragment").commit();
                }
            }});

        createRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Toast.makeText(MainActivity.this,"Rota Oluşturuluyor...",Toast.LENGTH_LONG).show();
                //gathering data
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openrouteservice.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<BaseModel> call = jsonPlaceHolderApi.getOptimizatedLocations(MainActivity.this.getResources().getString(R.string.api_key), createRequestBody());
                call.enqueue(new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        if(!response.isSuccessful()){
                            System.out.println("Code " + response.code());
                            return;
                        }
                        BaseModel baseModelResponse = response.body();
                        sortedCustomMenuItemsList.clear();
                        //I sort the tasks and store them in a new list called sortedCustomMenuItemsList
                        for(int i = 0; i < baseModelResponse.getRoutes().get(0).getSteps().size(); i++){
                            if(i < baseModelResponse.getRoutes().get(0).getSteps().size()-1){
                                //I sort the tasks
                                int temp = baseModelResponse.getRoutes().get(0).getSteps().get(i).getJob();
                                temp--; // the nth job from the response corresponds to the n-1th element of my customMenuItemsList
                                if(temp != -1 && temp != customMenuItemsList.size()){
                                    CustomMenuItem customMenuItem = customMenuItemsList.get(temp);
                                    sortedCustomMenuItemsList.add(customMenuItem);
                                }
                            }
                            else{
                                //last element of getJob() method is the end destination which I did not define as a task, but it is a job regardless, so I add it
                                sortedCustomMenuItemsList.add(new CustomMenuItem(currentLocation.latitude, currentLocation.longitude));
                                //final destination / current position
                            }
                        }
                        //to draw the route, I'm only interested in task coordinates. So I create a new list consisting of coordinates user needs to go in a sorted way
                        endPoints.clear();
                        endPoints.add(new LatLng(currentLocation.latitude, currentLocation.longitude)); //current location
                        for(CustomMenuItem customMenuItem : sortedCustomMenuItemsList){
                            endPoints.add(new LatLng(customMenuItem.getLatitude(), customMenuItem.getLongitude()));
                        }
                        //drawing onto map
                        findRoutes(currentLocation, endPoints);
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

            }
        });

        //map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //when 'detayı göster' is pressed from adapter
    @Override
    public void onMethodCallbackButton1(CustomMenuItem customMenuItem) {
        addButton.setVisibility(View.GONE);
        fragment = new FormFragment(lastMarkerPosition, lastLocationName, customMenuItem.getTaskName(), customMenuItem.getDescription());
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).addToBackStack("FormFragment").commit();
    }

    //when 'konumu göster' is pressed from adapter
    @Override
    public void onMethodCallbackButton2(CustomMenuItem customMenuItem) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(customMenuItem.getLatitude(), customMenuItem.getLongitude())).title(customMenuItem.getLatitude() + "," + customMenuItem.getLongitude()).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(customMenuItem.getLatitude(), customMenuItem.getLongitude()), 11.0f));
    }

    //when delete ImageView is pressed from adapter
    @Override
    public void onMethodCallbackButton3() {
        createNewCustomMenuItemsList();
    }

    private void recyclerViewBuilder(){
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CustomMenuAdapter(customMenuItemsList, this);
        menuRecyclerView.setLayoutManager(layoutManager);
        menuRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_button) {
            if(menuLinearLayout.getTag().equals("1")){
                menuLinearLayout.setVisibility(View.VISIBLE);
                menuLinearLayout.setTag("2");
            } else if (menuLinearLayout.getTag().equals("2")){
                menuLinearLayout.setVisibility(View.GONE);
                menuLinearLayout.setTag("1");
            }
        }
        else if (id == R.id.sign_out_button){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("GeoTask'ten çıkış yap?")
                    .setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            database.mainDao().authenticateUser(false, authenticatedUser.getID());
                            finish();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("İptal", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final boolean[] isFirstTime = {true};

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if(isFirstTime[0]){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11.0f));
                    isFirstTime[0] = false;
                }
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
        };
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                tickButtonLock = true;
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot));
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11.0f));
                lastMarkerPosition = marker.getPosition();
                marker.setTitle(lastMarkerPosition.latitude + "," + lastMarkerPosition.longitude);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List<Address> listAddresses = geocoder.getFromLocation(lastMarkerPosition.latitude, lastMarkerPosition.longitude, 1);
                    if(listAddresses != null && listAddresses.size() > 0){
                        String address = "";
                        if(listAddresses.get(0).getThoroughfare() != null){
                            address += listAddresses.get(0).getThoroughfare() + " ";
                        }
                        if(listAddresses.get(0).getLocality() != null){
                            address += listAddresses.get(0).getLocality() + " ";
                        }
                        if(listAddresses.get(0).getAdminArea() != null){
                            address += listAddresses.get(0).getAdminArea() + " ";
                        }
                        lastLocationName = address;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                tickButtonLock = false;
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            mMap.setMyLocationEnabled(true);
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                mMap.setMyLocationEnabled(true);
            }
        }

    }

    //return back to initial state of MainActivity
    public void resetState(){
        addButton.setVisibility(View.VISIBLE);
        addButton.setImageResource(R.drawable.ic_baseline_add_24);

        mMap.clear();
    }

    //calling this if there are any changes in tasks
    public void createNewCustomMenuItemsList(){
        customMenuItemsList.clear();
        customMenuItemsList.addAll(database.mainDao().getAllCustomMenuItems(authenticatedUser.getID()));
        adapter.notifyDataSetChanged();
    }

    //creating request body with correct format
    PostModel createRequestBody(){
        //example call body
        //{"jobs":[{"id":1,"service":300,"amount":[1],"location":[1.98935,48.701],"skills":[1],"time_windows":[[32400,36000]]}],
        // "vehicles":[{"id":1,"profile":"driving-car","start":[2.35044,48.71764],"end":[2.35044,48.71764],"capacity":[4],"skills":[1,14],"time_window":[28800,43200]}]}

        Vehicle vehicle = new Vehicle(1, "driving-car", Arrays.asList(currentLocation.longitude, currentLocation.latitude), Arrays.asList(currentLocation.longitude,currentLocation.latitude), Arrays.asList(100), Arrays.asList(1), Arrays.asList(0, 1000000));
        return new PostModel(createJobs(), Arrays.asList(vehicle));
    }

    //to create request body, i need to create jobs, which are our tasks
    List<Job> createJobs(){
        List<List<Integer>> time_windows = new ArrayList<>();
        List<Integer> tempList = new ArrayList<>();
        tempList.add(0);
        tempList.add(1000000);
        time_windows.add(tempList);

        List<Job> jobsList = new ArrayList<>();
        for(int i = 0; i < customMenuItemsList.size(); i++){
            CustomMenuItem customMenuItem = customMenuItemsList.get(i);
            Job job = new Job(i+1, 300, Arrays.asList(1), Arrays.asList(customMenuItem.getLongitude(),customMenuItem.getLatitude()), Arrays.asList(1), time_windows);
            jobsList.add(job);
        }
        return jobsList;
    }

    //adds markers to all jobs at once
    private void addJobMarkers(){
        for(CustomMenuItem customMenuItem : customMenuItemsList){
            mMap.addMarker(new MarkerOptions().position(new LatLng(customMenuItem.getLatitude(), customMenuItem.getLongitude())).title(customMenuItem.getLatitude() + "," + customMenuItem.getLongitude()).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot)));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetState();
    }

    public void findRoutes(LatLng start, List<LatLng> endPoints) {
        if(start == null || endPoints.size() == 0) {
            Toast.makeText(MainActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(true)
                        .waypoints(endPoints)
                        .key(MainActivity.this.getResources().getString(R.string.map_key))
                        .build();
                routing.execute();
        }
    }

    //routing callbacks...
    @Override
    public void onRoutingFailure(com.directions.route.RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        //Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polyLines!=null) {
            polyLines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polyLines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polyLines.add(polyline);

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("Konumum");
        startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot));
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Hedef Konum");
        endMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot));
        mMap.addMarker(endMarker);

        //Add the job markers
        addJobMarkers();

        //zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 11.0f));
    }

    @Override
    public void onRoutingCancelled() {
        findRoutes(currentLocation, endPoints);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        findRoutes(currentLocation, endPoints);
    }

}