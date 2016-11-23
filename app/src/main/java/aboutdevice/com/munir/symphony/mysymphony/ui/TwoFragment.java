package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.FeatureAdapter;
import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class TwoFragment extends Fragment  {
    public String modelName;
    TextView txtLat, txtLan, txtModelName ;
    RecyclerView recyclerView;
    private View view;
    private RecyclerView.Adapter mAdapter;
    public FetchJson fetchJson;
    private List<String> featureList = new ArrayList<String>();
    private LinkedHashMap<Integer,String> featureMap = new LinkedHashMap<Integer,String>();
    public TwoFragment (){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_two,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtModelName = (TextView)view.findViewById(R.id.txtmodelname);
        recyclerView = (RecyclerView)view.findViewById(R.id.feature_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        modelName = "Z8";
        fetchJson = new FetchJson(getContext());
        String read = fetchJson.readJSONFromAsset();
        try{
            fetchJson.jsonToMap(read);
            featureList = fetchJson.getMapData(modelName);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fetchJson.searchModelName(modelName)){
            txtModelName.setText(modelName);
            featureMap = fetchJson.createFeatureMap(featureList);
        }
        else{
            txtModelName.setText("This model is not enlisted");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(featureMap.size() > 0){
            mAdapter = new FeatureAdapter(getContext(),featureMap);
            recyclerView.setAdapter(mAdapter);
        }

        else{
            txtModelName.setText("No feature found");
        }
    }

}
