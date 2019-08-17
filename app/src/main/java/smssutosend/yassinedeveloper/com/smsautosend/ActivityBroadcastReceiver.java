package smssutosend.yassinedeveloper.com.smsautosend;

/**
 * Created by root on 12/22/17.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ActivityBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // context.startService(new Intent(context, SensorService.class));;

        Intent background = new Intent(context, SensorServiceBackground.class);
        background.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  background.addFlags(Intent.FLAG_FROM_BACKGROUND);
        context.startService(background);
    }

}