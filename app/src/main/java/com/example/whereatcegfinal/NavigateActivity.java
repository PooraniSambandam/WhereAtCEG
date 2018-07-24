package com.example.whereatcegfinal;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.view.Display;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.messaging.RemoteMessage;

import android.support.design.widget.FloatingActionButton;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;


import static com.example.whereatcegfinal.R.id.map;

public class NavigateActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    TextView tvDistanceDuration;
    ArrayList<LatLng> markerPoints;


    //To store longitude and latitude from map
    private double longitude, destLongitude;
    private double latitude, destLatitude;

    String mode;
    //Buttons
    //private ImageButton buttonSave;
    private ImageButton buttonCurrent;
    private ImageButton buttonView;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //Search Bar Variables
    SQLiteDatabase db;
    Boolean EditTextEmptyHold;
    EditText getLocation;
    ImageButton ShowData;
    String locationName, query;

    //FLOATING BUTTON
    /**
     * Vertices of pentagon
     */
    Point[] pentagonVertices;
    FloatingActionButton fab;

    /**
     * Buttons to be animated
     */
    Button[] buttons;
    int height, width;
    int radius;
    int ANIMATION_DURATION = 300;
    /**
     * Coordination of button
     */
    int startPositionX = 0;
    int startPositionY = 0;
    /**
     * To check which animation is to be played
     * O for enter animation
     * 1 for exit animation
     */
    int whichAnimation = 0;
    //Polygon
    int NUM_OF_SIDES = 5;
    int POSITION_CORRECTION = 11;
    int[] enterDelay = {80, 120, 160, 40, 0};
    int[] exitDelay = {80, 40, 0, 120, 160};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        /*/FLOATING BUTTON
        height = (int) getResources().getDimension(R.dimen.button_height);
        width = (int) getResources().getDimension(R.dimen.button_width);
        radius = (int) getResources().getDimension(R.dimen.radius);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        calculatePentagonVertices(radius, POSITION_CORRECTION);
*/

        //Search Bar
        ShowData = (ImageButton) findViewById(R.id.buttonSearch);


        //Navigation Part
        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
        markerPoints = new ArrayList<LatLng>();

        ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NavigateActivity.this, SearchSQLiteActivity.class);
                startActivity(intent);
            }
        });

        //Floating button

        height = (int) getResources().getDimension(R.dimen.button_height);
        width = (int) getResources().getDimension(R.dimen.button_width);
        radius = (int) getResources().getDimension(R.dimen.radius);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        calculatePentagonVertices(radius, POSITION_CORRECTION);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Initializing views and adding onclick listeners
        //buttonSave = (ImageButton) findViewById(R.id.buttonSave);
        buttonCurrent = (ImageButton) findViewById(R.id.buttonCurrent);
        buttonView = (ImageButton) findViewById(R.id.buttonView);
        //buttonSave.setOnClickListener(this);
        buttonCurrent.setOnClickListener(this);
        buttonView.setOnClickListener(this);


    }

    //Navigation code begins
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance =  point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration =  point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);

                // Bitmap for polyline animation
                Bitmap largeIcon = BitmapFactory.decodeResource(getBaseContext().getResources(),
                        R.drawable.heart);
                Bitmap convertedImage = getResizedBitmap(largeIcon, 50);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(convertedImage))
                        .position(points.get(0))
                        .flat(true));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 10));
                // mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(), 10));
                animateMarker(mMap, marker, points, false);
            }

            tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
    //Animate marker
    private static void animateMarker(final GoogleMap myMap, final Marker marker, final ArrayList<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size()) {
                    marker.setPosition(directionPoint.get(i));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(i), 15));
                }
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
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
    //@Override
    //public void onMapReady(GoogleMap googleMap) {
    //  mMap = googleMap;

    // Add a marker in Sydney and move the camera
    //LatLng sydney = new LatLng(-34, 151);
    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    //}
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        if (mMap != null) {
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
            mMap.setMyLocationEnabled(true);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //Destination
            Bundle bundle = getIntent().getExtras();

            if(bundle != null)
            {
                destLatitude = bundle.getDouble("latitudeValue");
                destLongitude =bundle.getDouble("longitudeValue");
                String destName = bundle.getString("destName");
                LatLng dest = new LatLng(destLatitude,destLongitude);
                LatLng origin = new LatLng(latitude,longitude);
                //LatLng dest1=new LatLng(13.011621, 80.237618);
                //Toast.makeText(NavigateActivity.this, "Inside bundle if ", Toast.LENGTH_LONG).show();
                //Toast.makeText(NavigateActivity.this, Double.toString(dest.latitude)+","+Double.toString(dest.longitude), Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions().position(dest).title(destName));
                CameraPosition cameraPosition=new CameraPosition.Builder().target(dest).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                markerPoints.add(0,origin);
                markerPoints.add(1,dest);
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        getCurrentLocation();


        /*
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        */
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }
/*
    @Override
    public void onClick(View v) {
    if(v == buttonCurrent){
            getCurrentLocation();
            moveMap();
        }

    }
*/
    //@Override
    public void onMapClick(LatLng latLng) {

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void calculatePentagonVertices(int radius, int rotation) {

        pentagonVertices = new Point[NUM_OF_SIDES];

        /**
         * Calculating the center of pentagon
         */
        Display display = getWindowManager().getDefaultDisplay();
        int centerX = display.getWidth() / 2;
        int centerY = display.getHeight() / 2;

        /**
         * Calculating the coordinates of vertices of pentagon
         */
        for (int i = 0; i < NUM_OF_SIDES; i++) {
            pentagonVertices[i] = new Point((int) (radius * Math.cos(rotation + i * 2 * Math.PI / NUM_OF_SIDES)) + centerX,
                    (int) (radius * Math.sin(rotation + i * 2 * Math.PI / NUM_OF_SIDES)) + centerY - 100);
        }

        buttons = new Button[pentagonVertices.length];

        for (int i = 0; i < buttons.length; i++) {
            //Adding button at (0,0) coordinates and setting their visibility to zero
            buttons[i] = new Button(NavigateActivity.this);
            buttons[i].setLayoutParams(new RelativeLayout.LayoutParams(5, 5));
            buttons[i].setX(0);
            buttons[i].setY(0);
            buttons[i].setTag(i);
            buttons[i].setOnClickListener(this);
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setBackgroundResource(R.drawable.circular_background);
            buttons[i].setTextColor(Color.WHITE);

            //buttons[i].setText(String.valueOf(i + 1));
            buttons[i].setTextSize(10);
            /**
             * Adding those buttons in acitvities layout
             */
            ((FrameLayout) findViewById(R.id.map)).addView(buttons[i]);
        }
        buttons[0].setText("Contacts");
        buttons[1].setText("Building");
        buttons[2].setText("Parking");
        buttons[3].setText("Eatery");
        buttons[4].setText("Amenity");
    }

    @Override
    public void onClick(View view) {

        if(view == buttonCurrent){
            getCurrentLocation();
            moveMap();
        }

        boolean isFabClicked = false;
        Bundle optionName = new Bundle();
        Intent NavigateSearchcat;
        switch (view.getId()) {
            case R.id.buttonCurrent:

                getCurrentLocation();
                moveMap();
                break;
            case R.id.fab:
                //Toast.makeText(getBaseContext(), "Pink button works", Toast.LENGTH_SHORT).show();
                isFabClicked = true;
                if (whichAnimation == 0) {
                    /**
                     * Getting the center point of floating action button
                     *  to set start point of buttons
                     */
                    startPositionX = (int) view.getX() + 50;
                    startPositionY = (int) view.getY() + 50;

                    for (Button button : buttons) {
                        button.setX(startPositionX);
                        button.setY(startPositionY);
                        button.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < buttons.length; i++) {
                        playEnterAnimation(buttons[i], i);
                    }
                    whichAnimation = 1;
                } else {
                    for (int i = 0; i < buttons.length; i++) {
                        playExitAnimation(buttons[i], i);
                    }
                    whichAnimation = 0;
                }

            default:
                if (!isFabClicked) {
                    switch ((int) view.getTag()) {
                        case 0:
                            Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
                            Intent ContactsOption=new Intent(NavigateActivity.this,contacts.class);
                            startActivity(ContactsOption);
                            break;
                        case 1:
                            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
                            optionName.putString("option","Building");
                            NavigateSearchcat = new Intent(NavigateActivity.this,SearchSQLiteActivity.class);
                            NavigateSearchcat.putExtras(optionName);
                            startActivity(NavigateSearchcat);
                            break;
                        case 2:
                            Toast.makeText(this, "Button 3 clicked", Toast.LENGTH_SHORT).show();
                            optionName.putString("option","Parking");
                            NavigateSearchcat = new Intent(NavigateActivity.this,SearchSQLiteActivity.class);
                            NavigateSearchcat.putExtras(optionName);
                            startActivity(NavigateSearchcat);
                            break;
                        case 3:
                            Toast.makeText(this, "Button 4 clicked", Toast.LENGTH_SHORT).show();
                            optionName.putString("option","Eateries");
                            NavigateSearchcat = new Intent(NavigateActivity.this,SearchSQLiteActivity.class);
                            NavigateSearchcat.putExtras(optionName);
                            startActivity(NavigateSearchcat);
                            break;
                        case 4:
                            Toast.makeText(this, "Button 5 clicked", Toast.LENGTH_SHORT).show();
                            optionName.putString("option","Amenities");
                            NavigateSearchcat = new Intent(NavigateActivity.this,SearchSQLiteActivity.class);
                            NavigateSearchcat.putExtras(optionName);
                            startActivity(NavigateSearchcat);
                            break;
                    }
                }
        }
    }

    private void playEnterAnimation(final Button button, int position) {

        /**
         * Animator that animates buttons x and y position simultaneously with size
         */
        AnimatorSet buttonAnimator = new AnimatorSet();

        /**
         * ValueAnimator to update x position of a button
         */
        ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(startPositionX + button.getLayoutParams().width / 2,
                pentagonVertices[position].x);
        buttonAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setX((float) animation.getAnimatedValue() - button.getLayoutParams().width / 2);
                button.requestLayout();
            }
        });
        buttonAnimatorX.setDuration(ANIMATION_DURATION);

        /**
         * ValueAnimator to update y position of a button
         */
        ValueAnimator buttonAnimatorY = ValueAnimator.ofFloat(startPositionY + 5,
                pentagonVertices[position].y);
        buttonAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setY((float) animation.getAnimatedValue());
                button.requestLayout();
            }
        });
        buttonAnimatorY.setDuration(ANIMATION_DURATION);

        /**
         * This will increase the size of button
         */
        ValueAnimator buttonSizeAnimator = ValueAnimator.ofInt(5, width);
        buttonSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.getLayoutParams().width = (int) animation.getAnimatedValue();
                button.getLayoutParams().height = (int) animation.getAnimatedValue();
                button.requestLayout();
            }
        });
        buttonSizeAnimator.setDuration(ANIMATION_DURATION);

        /**
         * Add both x and y position update animation in
         *  animator set
         */
        buttonAnimator.play(buttonAnimatorX).with(buttonAnimatorY).with(buttonSizeAnimator);
        buttonAnimator.setStartDelay(enterDelay[position]);
        buttonAnimator.start();
    }

    private void playExitAnimation(final Button button, int position) {

        /**
         * Animator that animates buttons x and y position simultaneously with size
         */
        AnimatorSet buttonAnimator = new AnimatorSet();

        /**
         * ValueAnimator to update x position of a button
         */
        ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(pentagonVertices[position].x - button.getLayoutParams().width / 2,
                startPositionX);
        buttonAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setX((float) animation.getAnimatedValue());
                button.requestLayout();
            }
        });
        buttonAnimatorX.setDuration(ANIMATION_DURATION);

        /**
         * ValueAnimator to update y position of a button
         */
        ValueAnimator buttonAnimatorY = ValueAnimator.ofFloat(pentagonVertices[position].y,
                startPositionY + 5);
        buttonAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setY((float) animation.getAnimatedValue());
                button.requestLayout();
            }
        });
        buttonAnimatorY.setDuration(ANIMATION_DURATION);

        /**
         * This will decrease the size of button
         */
        ValueAnimator buttonSizeAnimator = ValueAnimator.ofInt(width, 5);
        buttonSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.getLayoutParams().width = (int) animation.getAnimatedValue();
                button.getLayoutParams().height = (int) animation.getAnimatedValue();
                button.requestLayout();
            }
        });
        buttonSizeAnimator.setDuration(ANIMATION_DURATION);

        /**
         * Add both x and y position update animation in
         *  animator set
         */
        buttonAnimator.play(buttonAnimatorX).with(buttonAnimatorY).with(buttonSizeAnimator);
        buttonAnimator.setStartDelay(exitDelay[position]);
        buttonAnimator.start();
    }
}
