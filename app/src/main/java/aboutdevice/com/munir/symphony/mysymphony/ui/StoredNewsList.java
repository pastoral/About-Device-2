package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.StoredNewsListAdapter;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;
import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

public class StoredNewsList extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    RecyclerView notificationRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<NotificationStore> notificationStoreList , sortedNotificationStoreList;
    LinearLayoutManager lm;
    //FastItemAdapter notificationStoreFastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler = new DatabaseHandler(getApplicationContext());

        notificationRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler);
         lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecyclerView.setLayoutManager(lm);
        notificationRecyclerView.setHasFixedSize(true);

        //  notificationStoreFastItemAdapter = new FastItemAdapter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // NotificationStore nsi = new NotificationStore();
        // databaseHandler.deleteAll(nsi);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // notificationRecyclerView.setAdapter(notificationStoreFastItemAdapter);
        notificationStoreList = databaseHandler.getAllNotifications();

    }

    @Override
    protected void onResume() {
        super.onResume();

         Collections.reverse(notificationStoreList);
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        // List<NotificationStore> notificationStoreList = databaseHandler.getAllNotifications();
        /*for (NotificationStore ns : notificationStoreList)

        {
            String log = "Title : " + ns.getNotification_title() + " , Content:  " + ns.getNotification_content() +
                    " , ctivityToBeOpened: " + ns.getActivityToBeOpened();
            Log.d("Stored Data : ", log);
        }*/
        mAdapter = new StoredNewsListAdapter(getApplicationContext(), notificationStoreList);
        notificationRecyclerView.setLayoutManager(lm);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // notificationStoreFastItemAdapter.add(notificationStoreList);
        notificationRecyclerView.setAdapter(mAdapter);

        int curSize = mAdapter.getItemCount();
        mAdapter.notifyDataSetChanged();

        notificationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long i = mAdapter.getItemId(position);
               // Toast.makeText(StoredNewsList.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                int id = Integer.valueOf(String.valueOf(i));
                List<NotificationStore> ns = databaseHandler.getRecord(DatabaseHandler.key_id, id);

            }
        }));

    }





}


