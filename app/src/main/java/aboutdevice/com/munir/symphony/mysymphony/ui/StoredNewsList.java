package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;

public class StoredNewsList extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler = new DatabaseHandler(getApplicationContext());

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

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<NotificationStore> notificationStoreList = databaseHandler.getAllNotifications();
        for(NotificationStore ns : notificationStoreList)

        {
            String log = "Title : " + ns.getNotification_title() + " , Content:  " + ns.getNotification_content() +
                    " , ctivityToBeOpened: " + ns.getActivityToBeOpened();
            Log.d("Stored Data : ", log);
        }
    }



}


