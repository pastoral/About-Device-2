package aboutdevice.com.munir.symphony.mysymphony.onesignal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONObject;

import java.math.BigInteger;

import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;

/**
 * Created by munirul.hoque on 1/12/2017.
 */

public class MyNotificationExtenderService  extends NotificationExtenderService{
    String bigPicture;
    String activityToBeOpened;
    String link;
    String modelSWVersion;
    String title,body;
    boolean returnVal;
    JSONObject data;
    DatabaseHandler databaseHandler;
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        data = notification.payload.additionalData;
        link = notification.payload.launchURL;
        bigPicture = notification.payload.bigPicture;
        modelSWVersion = data.optString("modelSWVersion", null);
        title = notification.payload.title;
        body = notification.payload.body;

        OverrideSettings overrideSettings = new OverrideSettings();

        if (modelSWVersion != null && (modelSWVersion.equals(getSystemProperty("ro.custom.build.version"))) || (modelSWVersion.equals(getSystemProperty("ro.build.display.id")))) {
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(MySymphonyApp.getContext().getResources(),
                            R.drawable.smiley);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        }

        else if(modelSWVersion != null && modelSWVersion.equals("any")){
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(MySymphonyApp.getContext().getResources(),
                            R.drawable.smiley);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            databaseHandler = new DatabaseHandler(getApplicationContext());
           // databaseHandler.addNotification(new NotificationStore(data.optString()));

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
        }
        return true;
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
