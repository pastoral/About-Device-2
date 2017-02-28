package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Arrays;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;

public class NewsWebActivity extends AppCompatActivity {

    public WebView webView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    private  AdView mAdView;
    String Systray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        remoteConfig = new RemoteConfig();

        Bundle bundle = getIntent().getExtras();
        String targetURL = bundle.getString("targetUrl");
        Systray = bundle.getString("SYSTRAY");

        webView = (WebView)findViewById(R.id.webView);


// Enable responsive layout
        webView.getSettings().setUseWideViewPort(true);
// Zoom out if the content width is greater than the width of the veiwport
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4365083222822400~7196026575");
         mAdView = (AdView)findViewById(R.id.adViewNewsWeb);
        // mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId("ca-app-pub-4365083222822400/8672759776");
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();

        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(targetURL);

    }



    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
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

                    mFirebaseRemoteConfig.activateFetched();

                }
                else{

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        if(Systray == null){
            i = new Intent(getApplicationContext(),StoredNewsList.class);
        }
        else{
            i = new Intent(getApplicationContext(),MainActivity.class);
        }
        startActivity(i);
    }
}
