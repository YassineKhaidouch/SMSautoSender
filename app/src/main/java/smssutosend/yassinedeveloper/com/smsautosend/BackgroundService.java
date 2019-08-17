package smssutosend.yassinedeveloper.com.smsautosend;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
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


/**
 * Created by root on 10/31/17.
 */

public class BackgroundService extends Service {


    // finish code location
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private Activity activity;
    //data location initialize
    Context mContext;
    private SQLiteHandler db;
    HashMap<String, String> messageData ;
    List<CallsDetails> callsDetails = new ArrayList<>();
    Cursor cursor;
    int Count  = 0;

    boolean exist = false;
    String PhNumber  = null;
    String DateString = null;




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

        db = new SQLiteHandler(getApplicationContext());
        messageData = db.getTextMessage();
        cursor = db.fetch();



    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
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

            if (messageData.get("id") != null && messageData.get("startdate") != null) {

                try {
                    d1 = format.parse(dateStart);
                    d2 = format.parse(dateStop);
                    d3 = format.parse(current_datetime);
                    //in milliseconds
                    //   long diff_start_stop = (d2.getTime() - d1.getTime()) / (60 * 1000);
                    long diff_start = (d3.getTime() - d1.getTime()) / (60 * 1000);
                    long diff_stop = (d2.getTime() - d3.getTime()) / (60 * 1000);

                    // textView.setText("Cursor "+cursor.getCount());

                    db.addData("", "diff start " + diff_start, "diff stop " + diff_stop, "get log");
                    if ((diff_start >= 0 && diff_stop >= 0)) {
                        db.addData("", "Start run " + diff_start, "diff stop " + diff_stop, "get log");

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

                        //   while (managedCursor.moveToNext()) {
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

                        PhNumber = phNumber;
                        DateString = dateString;

                        db.addData("get number :"+PhNumber,dir,DateString, "dddd");
                        try {
                            d4 = format.parse(dateString);
                            long diff = (d3.getTime() - d4.getTime()) / (60 * 1000);
                            db.addData("different time :"+diff,"diff : "+diff,dir, "dddd");

                            if (diff <= 1 && dir.equals("OUTGOING")) {
                                db.addData("get missed call :"+phNumber,"ee","dd"+dateString,"olddd");
                                if (cursor != null ) {
                                    // move cursor to first row
                                    if (cursor.moveToFirst()) {
                                        do {
                                            String numberPhone = cursor.getString(cursor.getColumnIndex("number"));
                                            String calltype = cursor.getString(cursor.getColumnIndex("calltype"));
                                            String calldate = cursor.getString(cursor.getColumnIndex("calldate"));
                                            String status = cursor.getString(cursor.getColumnIndex("status"));
                                            String id = cursor.getString(cursor.getColumnIndex("id"));
                                            db.addData("Checking in db if number diplicated","","", "");
                                            if (PhNumber.equals(numberPhone)) {
                                                exist = true;
                                                db.addData("Checked number is found",PhNumber,"", "");
                                                if (status.equals("old")) {
                                                    db.addData("Checked number found","status old","", "");
                                                    try {
                                                        d5 = format.parse(calldate);
                                                        long diffe = (d3.getTime() - d5.getTime()) / (60 * 1000);
                                                        if (diffe >= 2) {
                                                            if (messageData.get("timeout").equals("SIM")) {
                                                                SmsManager smsManager = SmsManager.getDefault();
                                                                smsManager.sendTextMessage(PhNumber, null, messageData.get("text"), null, null);
                                                            } else {
                                                                SendSMS(messageData.get("text"), PhNumber);
                                                            }
                                                            db.addData("Checked nuumber uploaded","status new","", "");
                                                            db.update(id, PhNumber, "MISSED", DateString, "new");
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                            // move to next row
                                        } while (cursor.moveToNext());
                                    }
                                }

                                if(!exist){
                                    //if(!status.equals("old")) {
                                    try {
                                        // d5 = format.parse(DateString);
                                        // long diffe = (d3.getTime() - d5.getTime()) / (60 * 1000);
                                        /// if (diffe <= 1) {
                                        if (messageData.get("timeout").equals("SIM")) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(PhNumber, null, messageData.get("text"), null, null);
                                        } else {
                                            SendSMS(messageData.get("text"), PhNumber);
                                        }
                                        db.addData(PhNumber, "MISSED", DateString, "old");
                                        db.addData("number added", "---", DateString, "old");
                                        //     }
                                    } catch (Exception e) {
                                    }

                                }


                            }
                        } catch (Exception e) {
                            db.addData("error : " + e, "error adding call logs", "", "new");
                        }


                    }

                } catch (Exception e) {
                }

            }




       //     stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }

        return START_STICKY;
    }

    public void SendSMS (String text, String to){
        //  String requestUrl  = "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+text+"&mobile="+to;
        try {
            String requestUrl  =  String.format("http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message=%s&mobile=%s", URLEncoder.encode(text, "UTF-8"), URLEncoder.encode(to, "UTF-8"));
            String USER_AGENT = "Mozilla/5.0";
            java.net.URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent",USER_AGENT);//"Mozilla/5.0 (X11; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();





            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            db.addData("REQ : "+responseCode,"url reponse : "+response,"" , "req");
            in.close();
            conn.getInputStream().close();
            //print result
            //    SMStext.setText("url : "+ url+"\n repnse code " + responseCode+"\nreq : "+ response.toString());
            //   Toast.makeText(SMSsender.this, "req : "+response.toString() ,Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
            db.addData("REQ : fail","url error : "+e,"" , "req");
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
