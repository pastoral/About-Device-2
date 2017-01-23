package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import aboutdevice.com.munir.symphony.mysymphony.R;

public class NewsActivity extends AppCompatActivity {
    public TextView title,body;
    public String sTitle = "";
    public String sBody = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image_banner = (ImageView)findViewById(R.id.image_banner);
        setSupportActionBar(toolbar);
        title = (TextView)findViewById(R.id.txttitle) ;
        body = (TextView)findViewById(R.id.txtbody) ;

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
        if(bundle.getString("IMAGEURL") != null){
            Picasso.with(getApplicationContext()).load(bundle.getString("IMAGEURL")).centerCrop().into(image_banner);
        }
    }
}
