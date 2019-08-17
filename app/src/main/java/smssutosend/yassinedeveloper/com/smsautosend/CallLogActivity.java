package smssutosend.yassinedeveloper.com.smsautosend;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CallLogActivity extends AppCompatActivity {


    RecyclerView recyclerview;


    Context mContext;

    private SQLiteHandler db;
    HashMap<String, String> messageData ;
    List<CallsDetails> callsDetails = new ArrayList<>();
    Cursor cursor;

    boolean exist = false;
    String PhNumber  = null;
    String DateString = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);
        setTitle("Call Log");
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);

        db = new SQLiteHandler(getApplicationContext());
        messageData = db.getTextMessage();
        cursor = db.fetch();


        // db = new SQLiteHandler(getApplicationContext());
       // Cursor cursor = db.fetch();
/*
        int i = 0;
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if(!email.equals(getCurrentuser.get("email"))){
                        userDetails.add(new UserDetails(
                                name,
                                email,
                                imgid[i]));
                        i++;
                    }
                    // move to next row
                } while (cursor.moveToNext());
            }
        }
  */



/*


        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
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
        }


        // test api send
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

        if(messageData.get("id") != null && messageData.get("startdate") != null ) {
            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);
                d3 = format.parse(current_datetime);

                //in milliseconds
                long diff_start_stop = (d2.getTime() - d1.getTime()) / (60 * 1000);
                long diff_start = (d3.getTime() - d1.getTime()) / (60 * 1000);
                long diff_stop = (d2.getTime() - d3.getTime()) / (60 * 1000);


                StringBuffer sb = new StringBuffer();
                Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
                sb.append("Call Log :");
                while (managedCursor.moveToNext()) {
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

                try {
                    d4 = format.parse(dateString);
                    long diff = (d3.getTime() - d1.getTime()) / (60 * 1000);
                    //long diff2 = (d2.getTime() - d4.getTime()) / (60 * 1000);
                    if (diff <= 1440) {
                        callsDetails.add(new CallsDetails(
                                phNumber,
                                dir,
                                null,
                                dateString));
                    }
                } catch (Exception e) {
                }

            }
            } catch (Exception e) {
            }
        }

            RecyclerviewAdapter recycler = new RecyclerviewAdapter(callsDetails);
            RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
            recyclerview.setLayoutManager(layoutmanager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(recycler);
    }

}
