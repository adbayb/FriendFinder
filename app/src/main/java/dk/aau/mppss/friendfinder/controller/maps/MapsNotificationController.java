package dk.aau.mppss.friendfinder.controller.maps;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by adibayoub on 05/08/2015.
 */
public class MapsNotificationController {
    private Context context;
    private NotificationManager notificationManager;

    public MapsNotificationController(Context context) {
        if(context != null) {
            this.context = context;
            this.notificationManager = (NotificationManager) this.context.getSystemService(this.context.NOTIFICATION_SERVICE);
        }
    }

    public boolean newNotification(int idNotification, int idRessourceFile, String title, String description) {
        if(this.notificationManager != null) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this.context)
                            .setSmallIcon(idRessourceFile)
                            .setContentTitle(title)
                            .setContentText(description);

            this.notificationManager.notify(idNotification, notificationBuilder.build());
        }

        return false;
    }
}
