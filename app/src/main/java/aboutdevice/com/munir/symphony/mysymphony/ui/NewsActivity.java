package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;

import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;


public class NewsActivity extends AppCompatActivity {
    public TextView title,body, promo_view;
    public String sTitle = "";
    public String sBody = "";
    private AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image_banner = (ImageView)findViewById(R.id.image_banner);
        setSupportActionBar(toolbar);
        title = (TextView)findViewById(R.id.txttitle) ;
        body = (TextView)findViewById(R.id.txtbody) ;
        promo_view = (TextView)findViewById(R.id.txtconfig);

        Bundle bundle = getIntent().getExtras();
        sTitle = bundle.getString("title");
         sBody = bundle.getString("body");

        title.setText(sTitle);
        body.setText(sBody);
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4365083222822400~7196026575");
        mAdView = (AdView)findViewById(R.id.adViewNews);
        // mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId("ca-app-pub-4365083222822400/8672759776");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(bundle.getString("IMAGEURL") != null){
            Picasso.with(getApplicationContext()).load(bundle.getString("IMAGEURL")).into(image_banner);
        }



        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);

        fetchRemoteConfig();
    }

    private void fetchRemoteConfig() {
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewsActivity.this, "Fetch Succeeded",
                            Toast.LENGTH_SHORT).show();
                    mFirebaseRemoteConfig.activateFetched();
                    String m = null;
                }
                else{
                    Toast.makeText(NewsActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
                updateUIRemote();
            }
        });
    }

    private void updateUIRemote() {
        boolean isPromoOn = mFirebaseRemoteConfig.getBoolean("is_promotion_on");
        String pro_devices = "R100,ZVII,L23,L26,M95";
        List<String> pro_device_list = Arrays.asList(pro_devices.split("\\s*,\\s*"));
        if(isPromoOn){
            promo_view.setVisibility(View.VISIBLE);
        }
        else{
            promo_view.setVisibility(View.GONE);
        }
    }
}
