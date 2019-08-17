package smssutosend.yassinedeveloper.com.smsautosend;

/**
 * Created by root on 12/22/17.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SensorServiceBackground extends Service {

    //data location initialize
    Context mContext;
    private SQLiteHandler db;
    HashMap<String, String> messageData ;
    List<CallsDetails> callsDetails = new ArrayList<>();
    Cursor cursor;
    int Count  = 0;

    String PhNumber  = null;
    String DateString = null;

    public SensorServiceBackground(Context applicationContext) {super();}
    public SensorServiceBackground() {}


    @Override
    public void onCreate() {
        this.mContext = this;
         startTimer();
        db = new SQLiteHandler(getApplicationContext());
        messageData = db.getTextMessage();
        cursor = db.fetch();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        Dothis();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(this, SensorServiceBackground.class);
        sendBroadcast(broadcastIntent);
     //   stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, to wake up every 60 second
        timer.schedule(timerTask, 60000, 60000);

    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Dothis();
        }};}

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void  Dothis() {
        // do somting there
        Date dNow = new Date();
        String dateStart = messageData.get("startdate") + " " + messageData.get("starttime");
        String dateStop = messageData.get("enddate") + " " + messageData.get("endtime");
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String current_datetime = format.format(dNow);

        Date d1 = null;
        Date d2 = null;
        Date d3 = null;
        Date d4 = null;
        Date d5 = null;
        // test api send
/*
                try {
                    if(tr) {
                        tr = false;
                        SendSMS("Hello this final test it is working good . Thank you very match for your time", "9595775120");
                        db.addData("9595775120", "sent tset", "sent test", "9595775120");
                    }
                }catch (Exception e){
                    db.addData("error","","error test : "+e , "error");
                }
*/

       // if( messageData.get("startdate") == null){
       //      db.addData("logs", "db null set settings first ", "", "");}
        if( messageData.get("startdate") != null){

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);
                d3 = format.parse(current_datetime);

                long diff_start = (d3.getTime() - d1.getTime()) / (60 * 1000);
                long diff_stop = (d2.getTime() - d3.getTime()) / (60 * 1000);

                if ((diff_start >= 0 && diff_stop >= 0)) {

                 //   db.addData("logs", "start working ... ", "", "");

                    // set your detail here
                    // see if sms sent delay one day if number call again and missing call resend sms
                    StringBuffer sb = new StringBuffer();
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Cursor managedCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                    int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                    int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                    int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                    int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
                    //sb.append("Call Log :");
                    managedCursor.moveToFirst();
              //      managedCursor.moveToLast();

                    do{
                        String phNumber = managedCursor.getString(number);
                        String callType = managedCursor.getString(type);
                        String callDate = managedCursor.getString(date);
                        Date callDayTime = new Date(Long.valueOf(callDate));
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateString = formatter.format(new Date(Long.parseLong(callDate)));
                        String callDuration = managedCursor.getString(duration);
                        String dir = null;
                        int dircode = Integer.parseInt(callType);

                        switch (dircode) {
                            case CallLog.Calls.OUTGOING_TYPE:
                                dir = "OUTGOING";
                                break;
                            case CallLog.Calls.INCOMING_TYPE:
                                dir = "INCOMING";
                                break;
                            case CallLog.Calls.MISSED_TYPE:
                                dir = "MISSED";
                                break;
                        }

                        callsDetails.add(new CallsDetails(
                                phNumber,
                                dir,
                                null,
                                dateString));
/*
                        try {
                            d4 = format.parse(dateString);
                            long diff = (d3.getTime() - d4.getTime()) / (60 * 1000);
                            if (diff <= 10) {
                                db.addData("logs", "diff in get number " + phNumber, "ee "+diff,dateString);
                                callsDetails.add(new CallsDetails(
                                        phNumber,
                                        dir,
                                        null,
                                        dateString));
                            }
                        }catch (Exception e){
                            db.addData("logs", "error when getting number "+e , "", "");
                        }
*/

                    }while (managedCursor.moveToNext());

                    boolean exist = false;
                    for (CallsDetails i : callsDetails) {
                        PhNumber = i.getNumber();
                        DateString = i.getDate();
                        String dir = i.getType();
                        try {
                            d4 = format.parse(DateString);
                            long diff = (d3.getTime() - d4.getTime()) / (60 * 1000); // to second
                         //   db.addData("logs", "diff TIME " + diff, "", "");

                            if (diff <= 1 && dir.equals("MISSED")) {

                              //  db.addData("logs", "diff muched " + diff, "get number " + PhNumber, "");
                                if (cursor != null) {
                                    // move cursor to first row
                                    if (cursor.moveToFirst()) {
                                        do {
                                            String numberPhone = cursor.getString(cursor.getColumnIndex("number"));
                                            String calltype = cursor.getString(cursor.getColumnIndex("calltype"));
                                            String calldate = cursor.getString(cursor.getColumnIndex("calldate"));
                                            String status = cursor.getString(cursor.getColumnIndex("status"));
                                            String id = cursor.getString(cursor.getColumnIndex("id"));

                                     //       db.addData("logs", "Check in db ids " + id, "", "");
                                            if (numberPhone.equals(PhNumber)) {
                                           //     db.addData("logs", "Check number muched  " + numberPhone, "", "");
                                                exist = true;
                                                // if(exist){
                                                try {
                                                    d5 = format.parse(calldate);
                                                    long diffe = (d3.getTime() - d5.getTime()) / (60 * 1000);
                                                    if (diffe >= 1440) {
                                                        if (messageData.get("timeout").equals("SIM")) {
                                                            SmsManager smsManager = SmsManager.getDefault();
                                                            smsManager.sendTextMessage(PhNumber, null, messageData.get("text"), null, null);
                                                        } else {
                                                            SendSMS(messageData.get("text"), PhNumber);
                                                        }
                                                        db.update(id, PhNumber, dir, DateString, "new");
                                                    }
                                                } catch (Exception e) {
                                                    db.addData("logs", "error "+e, "", "");
                                                }
                                                //   stopSelf();
                                            }
                                            // move to next row
                                        } while (cursor.moveToNext());
                                    }
                                }
                                if (!exist) {
                                    //db.addData("logs", "number not exist  " + PhNumber, "", "");
                                    try {
                                        if (messageData.get("timeout").equals("SIM")) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(PhNumber, null, messageData.get("text"), null, null);
                                        } else {
                                            SendSMS(messageData.get("text"), PhNumber);
                                        }
                                        db.addData(PhNumber, dir, DateString, "old");
                                        exist = true;
                                    } catch (Exception e) {
                                    }
                                    //  stopSelf();

                                }

                            }

                        } catch (Exception e) {
                            db.addData("error : " + e, "error adding call logs", "", "new");
                        }
                    }
                }
            } catch (Exception e) {
                db.addData("error : " + e, "larg try", "", "new");
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void SendSMS (String text, String to){
      //  String requestUrl  = "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+text+"&mobile="+to;
        int responseCode = 0;
        StringBuffer response = null;
        try {
            String requestUrl  =  String.format("http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message=%s&mobile=%s&lang=HN&&adv=1", URLEncoder.encode(text, "UTF-8"), URLEncoder.encode(to, "UTF-8"));
            String USER_AGENT = "Mozilla/5.0";
            java.net.URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent",USER_AGENT);//"Mozilla/5.0 (X11; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
          //  db.addData("REQ : "+responseCode,"url reponse : "+response,"" , "req");
            in.close();
            conn.getInputStream().close();
            //print result
            //    SMStext.setText("url : "+ url+"\n repnse code " + responseCode+"\nreq : "+ response.toString());
            //   Toast.makeText(SMSsender.this, "req : "+response.toString() ,Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
           db.addData("REQ :"+responseCode,"TYPE ERROR : "+e,"SMS not sent to  "+to , "Error NO CONNECTING");
            e.printStackTrace();
        }

    }

public void  SenderSMSApi(String smstext ,String smsto){

   // final WebView wv = new WebView(this);
    String curl =  "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+smstext+"&mobile="+smsto ;
    WebView webView = new WebView(this);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.loadUrl(curl);

    }
}
