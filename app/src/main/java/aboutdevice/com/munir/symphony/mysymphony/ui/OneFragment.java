package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.TileAdapter;
import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;
import aboutdevice.com.munir.symphony.mysymphony.utils.TileSpacesItemDecoration;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class OneFragment extends Fragment {
   // private RecyclerView mRecyclerView;
   // private TileAdapter mTileAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;
   // private TileSpacesItemDecoration tileSpacesItemDecoration;
    public OneFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
      /*  mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mTileAdapter = new TileAdapter(getActivity());
        tileSpacesItemDecoration = new TileSpacesItemDecoration(16);*/
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
       /* mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(tileSpacesItemDecoration);
        mRecyclerView.setAdapter(mTileAdapter);*/
        CCAddress ccAddress = new CCAddress("Dhaka (Mirpur11)", "Dhaka","Plot-22 (2nd Floor), Block-D, Road-3, Section-11, Mirpur, Dhaka",
                true, 23.817577,90.401567,"ban", "2012-04-23T18:25:43.511Z","2012-04-23T18:25:43.511Z", "munirul.hoque@edison-bd.com" );

       Toast.makeText(getActivity(),String.valueOf(ccAddress.isCc()), Toast.LENGTH_SHORT).show();

    }
}
