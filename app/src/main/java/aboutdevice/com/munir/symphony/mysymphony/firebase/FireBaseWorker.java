package aboutdevice.com.munir.symphony.mysymphony.firebase;

import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class FireBaseWorker {
    public static FirebaseDatabase mDatabase;
    public static DatabaseReference mDatabaseReference;
    private ProgressBar progressBar;
    SearchView searchView;
    private List<CCAddress> list ;


}
