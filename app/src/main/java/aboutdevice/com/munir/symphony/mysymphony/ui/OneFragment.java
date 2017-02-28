package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;


import org.json.JSONException;

import aboutdevice.com.munir.symphony.mysymphony.R;

import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;


import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class OneFragment extends Fragment {
   // private RecyclerView mRecyclerView;
   // private TileAdapter mTileAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;
   // private TileSpacesItemDecoration tileSpacesItemDecoration;
   public LinearLayout contactline1, contactline2;
    public AdView mAdView;
    public  View view;
    private LinearLayout featureArea, contactArea;
    private String modelName;
    private FetchJson fetchJson;
    public  boolean modelFound;

    public OneFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_one,container,false);
      /*  mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mTileAdapter = new TileAdapter(getActivity());
        tileSpacesItemDecoration = new TileSpacesItemDecoration(16);*/
        contactline1 = (LinearLayout)view.findViewById(R.id.hotline1);
        contactline2 = (LinearLayout)view.findViewById(R.id.hotline2);

        featureArea = (LinearLayout)view.findViewById(R.id.feature_area) ;
        contactArea = (LinearLayout)view.findViewById(R.id.contact_area) ;

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

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        modelFound = fetchJson.searchModelName(modelName);
        //setHasOptionsMenu(true);
       /* contactline1.setOnClickListener(new View.OnClickListener() {
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
        });*/


        //isGooglePlayServicesAvailable(getActivity());


    }

    @Override
    public void onResume() {
        super.onResume();
        if(modelFound) {
            featureArea.setVisibility(View.VISIBLE);
        }



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




}
