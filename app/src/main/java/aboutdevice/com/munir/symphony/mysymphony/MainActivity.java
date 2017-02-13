package aboutdevice.com.munir.symphony.mysymphony;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.adapter.SectionAdapter;
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

public class MainActivity extends BaseActivity {
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
    private  boolean modelFound;
    private Button newsButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();

        newsButton = (Button)findViewById(R.id.buttonNews) ;

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
        isGooglePlayServicesAvailable(this);
        MainActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable(this);
        //testDB();

    }



    /**
     * A placeholder fragment containing a simple view.
     */

    private void setupViewPager(ViewPager viewPager) {
        SectionAdapter sectionAdapter = new SectionAdapter(getSupportFragmentManager());
        sectionAdapter.addFrag(new OneFragment(),"Home");
        if(modelFound) {
            sectionAdapter.addFrag(new TwoFragment(), "Feature");
        }
        sectionAdapter.addFrag(new ThreeFragment(), "Customer Care");
        sectionAdapter.addFrag(new FourFrgment(), "Contuct us");
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

}
