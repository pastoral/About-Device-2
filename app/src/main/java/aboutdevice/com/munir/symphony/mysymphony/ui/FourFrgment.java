package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 11/15/2016.
 */

public class FourFrgment extends Fragment {
    public LinearLayout contactline1, contactline2, facebookarea;
    public FourFrgment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four,container,false);
        contactline1 = (LinearLayout)view.findViewById(R.id.contactline1);
        contactline2 = (LinearLayout)view.findViewById(R.id.contactline2);
        facebookarea = (LinearLayout)view.findViewById(R.id.facebookarea);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        contactline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:16272"));
                startActivity(intent);
            }
        });

        contactline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0966670066"));
                startActivity(intent);
            }
        });

        facebookarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFacebook();
            }
        });

    }

    public final void launchFacebook() {
        final String urlFb = "fb://page/"+"@presagibd";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If a Facebook app is installed, use it. Otherwise, launch
        // a browser
        final PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/"+"@presagibd";
            intent.setData(Uri.parse(urlBrowser));
        }

        startActivity(intent);
    }
}
