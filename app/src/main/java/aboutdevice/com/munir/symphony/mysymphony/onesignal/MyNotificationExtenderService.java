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

/**
 * Created by munirul.hoque on 1/12/2017.
 */

public class MyNotificationExtenderService  extends NotificationExtenderService{
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        JSONObject data = notification.payload.additionalData;
        String modelSWVersion;
        boolean returnVal;
        modelSWVersion = data.optString("modelSWVersion", null);

        OverrideSettings overrideSettings = new OverrideSettings();

        if (modelSWVersion != null && modelSWVersion.equals(getSystemProperty("ro.custom.build.version"))) {
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
