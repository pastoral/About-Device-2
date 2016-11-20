package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aboutdevice.com.munir.symphony.mysymphony.Constants;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.FireBaseWorker;
import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;
import aboutdevice.com.munir.symphony.mysymphony.utils.CCAddressViewHolder;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;

import static android.view.View.GONE;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class ThreeFragment extends Fragment {
    public String name;
    public View view;
    public int pos;
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    private List<CCAddress> ccAddressList;
    private LinearLayoutManager mLinearLayoutManager;
    private FireBaseWorker fireBaseWorker;
    private CCAddressViewHolder ccAddressViewHolder;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<CCAddress,CCAddressViewHolder> firebaseRecyclerAdapter;
    static boolean calledAlready = false;
    public ThreeFragment (){
        ccAddressList = new ArrayList<CCAddress>();
        this.pos = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_three,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView)view.findViewById(R.id.cc_recycler);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        fireBaseWorker = new FireBaseWorker();
        mDatabaseReference = fireBaseWorker.intDatabase(Constants.ADRESS);

        mDatabaseReference.keepSynced(true);
        //ccAddressList.clear();
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
                viewHolder.txtCCName.setText(model.getName());
                viewHolder.txtCCAddress.setText(model.getAddress());
                pos = position;
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
            Toast.makeText(getActivity(), "Got it",Toast.LENGTH_SHORT).show();
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }


    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }
}
