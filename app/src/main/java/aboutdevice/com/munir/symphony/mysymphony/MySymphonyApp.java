package aboutdevice.com.munir.symphony.mysymphony;

import android.app.Application;

import aboutdevice.com.munir.symphony.mysymphony.receiver.ConnectivityReceiver;

/**
 * Created by munirul.hoque on 12/21/2016.
 */

public class MySymphonyApp extends Application {
    private static MySymphonyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized MySymphonyApp getmInstance(){
        return mInstance;
    }
    public void setConnectiviyListner(ConnectivityReceiver.ConnectivityReceiverListner listner){
        ConnectivityReceiver.connectivityReceiverListner = listner;
    }
}
