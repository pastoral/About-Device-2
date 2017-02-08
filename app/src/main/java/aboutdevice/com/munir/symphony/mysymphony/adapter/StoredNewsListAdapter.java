package aboutdevice.com.munir.symphony.mysymphony.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 2/7/2017.
 */

public class StoredNewsListAdapter  {
    TextView notification_title, notification_content;

    class StoredNewsListHolder extends RecyclerView.ViewHolder{
        
        public StoredNewsListHolder(View view){
            super(view);
            notification_title = (TextView)view.findViewById(R.id.notification_title);
            notification_content = (TextView)view.findViewById(R.id.notification_content);
        }
    }

}
