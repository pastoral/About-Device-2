package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.Constants;
import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.FireBaseWorker;
import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;
import aboutdevice.com.munir.symphony.mysymphony.receiver.ConnectivityReceiver;
import aboutdevice.com.munir.symphony.mysymphony.utils.CCAddressViewHolder;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;
import aboutdevice.com.munir.symphony.mysymphony.utils.FusedLocationFinder;
import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.REQUEST_CHECK_SETTINGS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.isFirebaseReady;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class ThreeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {
    public String name;
    public int pos;
    public ProgressBar progressBar, progressBarCC;
    public RecyclerView recyclerView;
    private List<CCAddress> ccAddressList;
    private LinearLayoutManager mLinearLayoutManager;
    private FireBaseWorker fireBaseWorker;
    private CCAddressViewHolder ccAddressViewHolder;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<CCAddress,CCAddressViewHolder> firebaseRecyclerAdapter;
    static boolean calledAlready = false;
    private TextView txtNearestCCAddress,txtNearestCCName;
    private static final String TAG = "FusedLocationFinder";
    public LocationRequest mLocationRequest;
    public LocationSettingsRequest mLocationSettingsRequest;
    public boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    public Location mCurrentlocation,ccLocation;
    private View view;
    private GoogleApiClient googleApiClient;
    private Context context;
    private int permissionCheck;
    private Bundle bundle;
    private BaseActivity baseActivity;
    public Map<String, Location> ccLocationMap;
    public Map<String,Float> distanceMap, sortedDistanceMap;
    public Button btnRefresh;
    public CardView nearest_cc_card;
    public Query query;
    // public GpsLocationReceiver gpsLocationReceiver;
    public String strNearestCCName, strNearestCCAddress;
    public SharedPreferences sharedpreferences;
    public  SharedPreferences.Editor editor;
    BroadcastReceiver gpsLocationReceiver;

    private boolean mapReady;
    public ThreeFragment (){
        ccAddressList = new ArrayList<CCAddress>();
        this.pos = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_three,container,false);

        //getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

            buildGoogleApiClient();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = savedInstanceState;
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBarCC = (ProgressBar)view.findViewById(R.id.progressBarCC);
        recyclerView = (RecyclerView)view.findViewById(R.id.cc_recycler);
        txtNearestCCName = (TextView)view.findViewById(R.id.txtNearestCCName);
        txtNearestCCAddress = (TextView)view.findViewById(R.id.txtNearestCCAddress);
        nearest_cc_card = (CardView)view.findViewById(R.id.nearest_cc_header);

        btnRefresh = (Button)view.findViewById(R.id.buttonRefresh);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
       // gpsLocationReceiver = new GpsLocationReceiver();
        ccLocation = new Location("Location of CC");
        ccLocationMap = new HashMap<>();
        distanceMap = new LinkedHashMap<>();
        sortedDistanceMap = new LinkedHashMap<>();
        sharedpreferences = getContext().getSharedPreferences("mysymphonyapp_data", Context.MODE_PRIVATE);


    }

    @Override
    public void onStart() {
        super.onStart();
        ccLocation = new Location("CC Location");
        if(googleApiClient != null){
            googleApiClient.connect();
           // getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        }
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        //getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        fireBaseWorker = new FireBaseWorker();
        mDatabaseReference = fireBaseWorker.intDatabase(Constants.ADRESS);

        mDatabaseReference.keepSynced(true);


    }

    @Override
    public void onResume() {
        super.onResume();
        //  askForPermission(permisionList[1],REQUEST_CHECK_SETTINGS);
        //ccAddressList.clear();

         final Intent intent = new Intent(getActivity(),MapsActivity.class);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CCAddress, CCAddressViewHolder>(
                CCAddress.class,
                R.layout.cc_list,
                CCAddressViewHolder.class,
                // mDatabaseReference.orderByChild("name")
                mDatabaseReference.orderByChild("name")
        ) {
            @Override
            protected void populateViewHolder(CCAddressViewHolder viewHolder, CCAddress model, int position) {
                progressBar.setVisibility(GONE);
                ccLocation.setLatitude(firebaseRecyclerAdapter.getItem(position).getLat());
                ccLocation.setLongitude(firebaseRecyclerAdapter.getItem(position).getLan());
                //Toast.makeText(getActivity(),String.valueOf(mCurrentlocation),Toast.LENGTH_SHORT).show();
                ccLocationMap.put(firebaseRecyclerAdapter.getItem(position).getName().toString(),ccLocation);
                ccLocation = new Location("CC Location");
                viewHolder.txtCCName.setText(model.getName());
                viewHolder.txtCCAddress.setText(model.getAddress());
                pos = position;

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String Key = getRef(position).getKey();
                        intent.putExtra("CCName", firebaseRecyclerAdapter.getItem(position).getName());
                        intent.putExtra("CCAddress", firebaseRecyclerAdapter.getItem(position).getAddress());
                        intent.putExtra("Latitude",String.valueOf(firebaseRecyclerAdapter.getItem(position).getLat()) );
                        intent.putExtra("Longitude" , String.valueOf(firebaseRecyclerAdapter.getItem(position).getLan()));
                        startActivity(intent);
                    }
                }));

            }
        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                itemCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (itemCount -1) && lastVisiblePosition == (positionStart -1))){
                    recyclerView.scrollToPosition(positionStart);

                }
            }
        });




        // recyclerView.setLayoutManager(mLinearLayoutManager);

        //recyclerView.setAdapter(firebaseRecyclerAdapter);
        if(firebaseRecyclerAdapter == null){
            Toast.makeText(getActivity(), "No adapter attached; skipping layout",Toast.LENGTH_SHORT).show();
        }
        else if (mLinearLayoutManager == null){
            Toast.makeText(getActivity(), "No layout manager attached; skipping layout",Toast.LENGTH_SHORT).show();
        }
        else{
           // Toast.makeText(getActivity(), "Got it",Toast.LENGTH_SHORT).show();
            isFirebaseReady = true;
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(firebaseRecyclerAdapter);

            if(sharedpreferences.getString("NEARESTCC_ADDRESS", null) != null  && sharedpreferences.getString("NEARESTCC_NAME", null) != null){

                nearest_cc_card.setVisibility(VISIBLE);
                txtNearestCCAddress.setText(sharedpreferences.getString("NEARESTCC_ADDRESS", null));
                txtNearestCCName.setText(sharedpreferences.getString("NEARESTCC_NAME", null));
            }


        }


        LocationAsyncRunner runner = new LocationAsyncRunner();
        runner.execute();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Refresh me", Toast.LENGTH_SHORT).show();
            }
        });
         gpsLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //here you can parse intent and get sms fields.
                boolean anyLocationProv = false;
                LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

                anyLocationProv = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // anyLocationProv |=  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // Log.i("", "Location service status" + anyLocationProv);
                //Toast.makeText(context, "Location service status : " + anyLocationProv, Toast.LENGTH_SHORT).show();
                if(anyLocationProv == false){
                    Toast.makeText(context, "No Location Service " , Toast.LENGTH_SHORT).show();
                    nearest_cc_card.setVisibility(GONE);
                    checkLocationSettings();
                }
                else{
                    LocationAsyncRunner runner = new LocationAsyncRunner();
                    runner.execute();
                }
            }
        };
        IntentFilter filter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        this.getContext().registerReceiver(gpsLocationReceiver, filter);


    }



    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(gpsLocationReceiver);

            stopLocationUpdates();


    }

    @Override
    public void onStop() {
        super.onStop();

            googleApiClient.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("ThirdFragment", "onConnectionFailed:" + connectionResult);
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void updateUI() {
        if (mCurrentlocation != null) {
            //txtLat.setText("Latitude: " + mCurrentlocation.getLatitude());
            //txtLan.setText("Longitude: " + mCurrentlocation.getLongitude());

            //  Toast.makeText(getActivity(),String.valueOf(distanceMap.size()),Toast.LENGTH_SHORT).show();
            if (distanceMap.size() > 0) {

                // Toast.makeText(getActivity(),String.valueOf(sortedDistanceMap.size()),Toast.LENGTH_SHORT).show();
                sortedDistanceMap = sortByValue(distanceMap);
                if (sortedDistanceMap.size() > 0) {
                   // Map.Entry<String, Float> entry = sortedDistanceMap.entrySet().iterator().next();
                    Map.Entry<String, Float> entry = sortedDistanceMap.entrySet().iterator().next();

                    strNearestCCName = entry.getKey();
                    query = mDatabaseReference.orderByChild("name").equalTo(entry.getKey());
                    Location lc = mCurrentlocation;
                    NearestCCFinder runner = new NearestCCFinder();
                    runner.execute();
                }

            } else {
                Toast.makeText(getActivity(), "Mara Kha ", Toast.LENGTH_SHORT).show();
                //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CHECK_SETTINGS :
                switch (resultCode){
                    case Activity.RESULT_OK :
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED :
                        Log.i("ThreeFragment", "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }



    public synchronized void buildGoogleApiClient(){
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }


    public  LocationRequest createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    public void buildLocationSettingRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,mLocationSettingsRequest);
        result.setResultCallback(this);

    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch(status.getStatusCode()){
            case LocationSettingsStatusCodes.SUCCESS :
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED :
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try{
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                }
                catch(IntentSender.SendIntentException e){
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE :
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    public void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            mRequestingLocationUpdates = true;
                        }
                    });
        }
        else{
            Log.v(TAG,"Start Location update gets hampered :((");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentlocation = location;
        mLastUpdateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        ///////////////////////////////////////////Update your UI now ////////////////////////////////
       // checkLocationSettings();
        setDistanceMap(ccLocationMap);
        updateUI();

        Log.v(TAG,"Last updated on " + mLastUpdateTime);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(mCurrentlocation == null){
                mCurrentlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                mLastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission},permsRequestCode);
            }
            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission},permsRequestCode);
            }
        }
        else{
            Log.v(TAG, " is already granted.");
        }
    }

    public void stopLocationUpdates(){
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mRequestingLocationUpdates = false;
                    }
                }
        );
    }





    public void fillDistanceMap(){
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                double lat = Double.parseDouble(map.get("lat").toString()) ;
                ccLocation.setLatitude(Double.parseDouble(map.get("lat").toString()));
                ccLocation.setLongitude(Double.parseDouble(map.get("lan").toString()));
                //Toast.makeText(getActivity(),String.valueOf(mCurrentlocation),Toast.LENGTH_SHORT).show();
                ccLocationMap.put(map.get("name").toString(),ccLocation);
                ccLocation = new Location("CC Location");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setDistanceMap(Map<String,Location> map){
        if(mCurrentlocation != null) {
            for (Map.Entry<String, Location> entry : map.entrySet()) {
                String key = entry.getKey();
                Location value = entry.getValue();
                distanceMap.put(key, mCurrentlocation.distanceTo(value));
            }
        }
        return;
    }

    public Map sortByValue(Map unSortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unSortedMap));
        sortedMap.putAll(unSortedMap);
        return sortedMap;
    }




    public class LocationAsyncRunner extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Patience", "Searching your location");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.isIndeterminate();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            createLocationRequest();
            buildLocationSettingRequest();
            checkLocationSettings();
           // fillDistanceMap();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();
            progressDialog.dismiss();
        }
    }

    class ValueComparator implements Comparator {
        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueA.compareTo(valueB);
        }
    }


    public class NearestCCFinder extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBarCC.isIndeterminate();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           // sortedDistanceMap = sortByValue(distanceMap);
          //  if (sortedDistanceMap.size() > 0) {


                ChildEventListener listner = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Map<String, Object> map = (Map<String,Object>)dataSnapshot.getValue();
                        //txtNearestCCAddress.setText(map.get("address").toString());
                        strNearestCCAddress = map.get("address").toString();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                query.addChildEventListener(listner);
           // }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nearest_cc_card.setVisibility(VISIBLE);
            editor = sharedpreferences.edit();
            editor.putString("NEARESTCC_ADDRESS", strNearestCCAddress);
            editor.putString( "NEARESTCC_NAME", strNearestCCName);
            editor.commit();
            txtNearestCCName.setText(strNearestCCName);
            txtNearestCCAddress.setText(strNearestCCAddress);
        }
    }


   private void showSnack(boolean isConnected){
       String message;
       int color;
       if(!isConnected){
           message = "Sorry! Not connected to internet";
           color = Color.RED;
       }
       else{
           message = "Good! Connected to Internet";
           color = Color.WHITE;
       }
       Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
       View sbView = snackbar.getView();
       TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
       textView.setTextColor(color);
       snackbar.show();
   }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }




}