package aboutdevice.com.munir.symphony.mysymphony;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.adapter.SectionAdapter;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.ui.FourFrgment;
import aboutdevice.com.munir.symphony.mysymphony.ui.OneFragment;
import aboutdevice.com.munir.symphony.mysymphony.ui.StoredNewsList;
import aboutdevice.com.munir.symphony.mysymphony.ui.ThreeFragment;
import aboutdevice.com.munir.symphony.mysymphony.ui.TwoFragment;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;
import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.permisionList;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;
import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;

public class MainActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private  AppBarLayout appBarLayout;
    private ThreeFragment threeFragment;
    private String modelName;
    private FetchJson fetchJson;
    public  boolean modelFound;
    private Button newsButton;
    public AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    private LinearLayout featureArea, contactArea;
    private SectionAdapter sectionAdapter;
    public static final  int REQUEST_INVITE = 1;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MainActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        featureArea = (LinearLayout)findViewById(R.id.feature_area) ;
        contactArea = (LinearLayout)findViewById(R.id.contact_area) ;
        remoteConfig = new RemoteConfig();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        //supportInvalidateOptionsMenu();




       // newsButton = (Button)findViewById(R.id.buttonNews) ;


        modelName = getSystemProperty("ro.product.device");
        fetchJson = new FetchJson(getContext());
        String read = fetchJson.readJSONFromAsset();
        try{
            fetchJson.jsonToMap(read);
            if(!fetchJson.searchModelName(modelName)) {
                modelName = getSystemProperty("ro.build.product");
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }



       // threeFragment = new ThreeFragment();
        // getSupportActionBar().setTitle("Parallax Tabs");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Set up the ViewPager with the sections adapter.
        modelFound = fetchJson.searchModelName(modelName);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitleEnabled(false);




       /* ImageView header = (ImageView) findViewById(R.id.main_imageview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.purple2);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener(){
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.vibrant));
                int vibrantDarkColor = palette.getDarkVibrantColor(getResources().getColor(R.color.vibrant_dark));
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
               // Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
               // Palette.Swatch vibrantDarkSwatch = palette.getDarkMutedSwatch();

                  //  collapsingToolbarLayout.setContentScrimColor(vibrantSwatch.getRgb());
                  //  collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkSwatch.getRgb());

            }
        });*/
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


       // MainActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        isGooglePlayServicesAvailable(this);
        MobileAds.initialize(getContext(), "ca-app-pub-4365083222822400~7196026575");
        mAdView = (AdView)findViewById(R.id.adView);
        // mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId("ca-app-pub-4365083222822400/8672759776");


        if(isGooglePlayServicesAvailable(this)){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this,this).build();


            // Check for App Invite invitations and launch deep-link activity if possible.
            // Requires that an Activity is registered in AndroidManifest.xml to handle
            // deep-link URLs.
            boolean autoLaunchDeepLink = true;
            AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                    .setResultCallback(
                            new ResultCallback<AppInviteInvitationResult>() {
                                @Override
                                public void onResult(AppInviteInvitationResult result) {
                                    Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                    if (result.getStatus().isSuccess()) {
                                        // Extract information from the intent
                                        Intent intent = result.getInvitationIntent();
                                        String deepLink = AppInviteReferral.getDeepLink(intent);
                                        String invitationId = AppInviteReferral.getInvitationId(intent);

                                        // Because autoLaunchDeepLink = true we don't have to do anything
                                        // here, but we could set that to false and manually choose
                                        // an Activity to launch to handle the deep link here.
                                        // ...
                                    }
                                }
                            });



        }
        else{
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable(this);
        //testDB();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();


    }



    /**
     * A placeholder fragment containing a simple view.
     */

    private void setupViewPager(ViewPager viewPager) {
        sectionAdapter = new SectionAdapter(getSupportFragmentManager());
        sectionAdapter.addFrag(new OneFragment(),"Home");
        if(modelFound) {
            sectionAdapter.addFrag(new TwoFragment(), "Features");
            //featureArea.setVisibility(View.VISIBLE);
        }
        sectionAdapter.addFrag(new ThreeFragment(), "Customer Care");
        sectionAdapter.addFrag(new FourFrgment(), "Contact");
        viewPager.setAdapter(sectionAdapter);
    }

  /*  @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }*/

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                Dialog df = googleApiAvailability.getErrorDialog(activity, status, 2404);
                df.setCancelable(false);
                df.show();
                //googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
    public String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

   /* public void testDB(){
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        Log.d("Insert: ", "Inserting ..");
        databaseHandler.addNotification(new NotificationStore("Test Title", "Test Content",
                "NewsActivity", "any", "TTTT", "bbbbbbbbbbbbb",
                "", " http://images.indianexpress.com/2016/07/eid-mubarak-3.jpg ", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())));
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<NotificationStore> notificationStoreList = databaseHandler.getAllNotifications();
        for(NotificationStore ns : notificationStoreList){
            String log = "Title : "+ ns.getNotification_title() + " , Content:  " + ns.getNotification_content()+
                          " , ctivityToBeOpened: "+ns.getActivityToBeOpened();
            Log.d("Stored Data : " , log);
        }

    }*/
    public void loadNews(View view){
        Intent intent = new Intent(getContext(), StoredNewsList.class);
        startActivity(intent);
    }

    public void loadInvitation(View view){
        //return;
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                        .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();

        startActivityForResult(intent, REQUEST_INVITE);

    }

    private void fetchRemoteConfig() {
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mFirebaseRemoteConfig.activateFetched();

                } else {

                }
                loadAdvertige();

            }
        });
    }

    private void loadAdvertige() {
        boolean modelExists = false;
        boolean isAdmobOn = mFirebaseRemoteConfig.getBoolean("is_admob_on");
        String restrictedDevices = mFirebaseRemoteConfig.getString("disable_admob_for");
        List<String> restricted_device_list = Arrays.asList(restrictedDevices.split("\\s*,\\s*"));
        if(isAdmobOn){
            modelExists = restricted_device_list.contains(remoteConfig.getModelName());
            if(modelExists){
                return;
            }
            else{
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }

        else{
            return;
        }

    }


   /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_news) {
            Intent i = new Intent(getApplicationContext(), StoredNewsList.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    } */

    public void loadFeatureFragment(View v){
        /*Fragment fg = new TwoFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       // SectionAdapter sectionAdapter = new SectionAdapter(getSupportFragmentManager());
        fragmentTransaction.replace(mViewPager.getCurrentItem(),fg);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
      //  Intent i = new Intent(getContext(),fg.getClass());
        //startActivity(i);
       // int pos = sectionAdapter.getItemPosition(TwoFragment.class);
       // mViewPager.setCurrentItem(1);

        mViewPager.setCurrentItem(1);

    }

    public void loadContactFragment(View v){
       // Fragment fg = new TwoFragment();
       // FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // SectionAdapter sectionAdapter = new SectionAdapter(getSupportFragmentManager());
        // fragmentTransaction.replace(mViewPager.getCurrentItem(),f);
        // Intent i = new Intent(getContext(),fg.getClass());
        //startActivity(i);
       // int pos = sectionAdapter.getItemPosition(TwoFragment.class);
       // mViewPager.setCurrentItem(2);
        if(mViewPager.getAdapter().getCount() ==4 ) {
            mViewPager.setCurrentItem(2);
        }
        else{
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if(requestCode == REQUEST_INVITE){
            if(resultCode == RESULT_OK){
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode,data);
                for(String id : ids){
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            }
            else{
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]

                //showMessage(getString(R.string.send_failed));
                Toast.makeText(this,getString(R.string.send_failed),Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //showMessage(getString(R.string.google_play_services_error));
        Toast.makeText(this,getString(R.string.google_play_services_error),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // mGoogleApiClient.stopAutoManage(this);
       // mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //mGoogleApiClient.disconnect();
    }



}
