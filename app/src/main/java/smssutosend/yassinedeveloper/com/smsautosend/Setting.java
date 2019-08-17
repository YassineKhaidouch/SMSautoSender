package smssutosend.yassinedeveloper.com.smsautosend;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Setting extends AppCompatActivity implements View.OnClickListener {


        RadioButton typesendsms;
        Button Done, BExit;
        EditText numberphone, Textmessage ;


        Button btnDatePicker, btnTimePicker;
        EditText txtDate, txtTime;
        private int mYear, mMonth, mDay, mHour, mMinute;

        Button btnDatePicker2, btnTimePicker2;
        EditText txtDate2, txtTime2;
        private int mYear2, mMonth2, mDay2, mHour2, mMinute2;
        boolean Checking = false ;
        HashMap<String, String> getMessageText;
        private SQLiteHandler db;
        String TypeSending = "SIM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("Setting");

        Done = (Button) findViewById(R.id.done);
        BExit = (Button) findViewById(R.id.exit);
//start
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        // end
        btnDatePicker2=(Button)findViewById(R.id.btn_date2);
        btnTimePicker2=(Button)findViewById(R.id.btn_time2);
        txtDate2=(EditText)findViewById(R.id.in_date2);
        txtTime2=(EditText)findViewById(R.id.in_time2);

        btnDatePicker2.setOnClickListener(this);
        btnTimePicker2.setOnClickListener(this);


        db = new SQLiteHandler(getApplicationContext());
        getMessageText = db.getTextMessage();


        typesendsms = (RadioButton) findViewById(R.id.typesendsms);

        BExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTextMessage();
                Toast.makeText(Setting.this, "ALL SETTING RESET : Done ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        numberphone = (EditText) findViewById(R.id.numberphone);

        Textmessage = (EditText) findViewById(R.id.text_message);

        try {
            if (getMessageText.get("timeout").equals("SIM") || getMessageText.get("timeout").equals("")) {
                typesendsms.setChecked(false);
            } else {
                typesendsms.setChecked(true);
            }
        }catch (Exception e){}

        typesendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Checking){
                    typesendsms.setChecked(true);
                    Checking = false;
                }else{
                    typesendsms.setChecked(false);
                    Checking = true;
                }
            }
        });



        txtDate.setText(getMessageText.get("startdate"));
        txtTime.setText(getMessageText.get("starttime"));

        txtDate2.setText(getMessageText.get("enddate"));
        txtTime2.setText(getMessageText.get("endtime"));
        Textmessage.setText(getMessageText.get("text"));

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String Textmsg = Textmessage.getText().toString();

                    String Startdatetime = txtDate.getText().toString() + "  " + txtTime.getText().toString();
                    String Enddatetime = txtDate2.getText().toString() + "  " + txtTime2.getText().toString();

                    String StartDate = txtDate.getText().toString();
                    String StartTime = txtTime.getText().toString();
                    String StopDate = txtDate2.getText().toString();
                    String StopTime = txtTime2.getText().toString();


                    if (TextUtils.isEmpty(txtDate.getText().toString()) || TextUtils.isEmpty(txtTime.getText().toString())) {
                        Toast.makeText(Setting.this, "Please enter start data and time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(txtDate2.getText().toString()) || TextUtils.isEmpty(txtTime2.getText().toString())) {
                        Toast.makeText(Setting.this, "Please enter end data and time", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                    Date datenow = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date d1 = null;
                    Date d2 = null;
                    Date d3 = null;
                    String current_date  = format.format(datenow);

                        d1 = format.parse(Startdatetime);
                        d2 = format.parse(Enddatetime);
                        d3 = format.parse(current_date);

                        //in milliseconds
                        long diff = d2.getTime() - d1.getTime();

                        long diff_last = (d1.getTime() - d3.getTime())/ (60 * 1000);


                        long diffMinutes = diff / (60 * 1000);

                        if (diff_last < -1 ) {
                            Toast.makeText(Setting.this, "please ! you cannot use previouse time", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (diffMinutes <= 0 ) {
                            Toast.makeText(Setting.this, " Start date and time Must be greate than  Stop date and time not vers ", Toast.LENGTH_LONG).show();
                            return;
                        }

                    } catch (Exception e) {
                        Toast.makeText(Setting.this, "Error when parse date : "+e, Toast.LENGTH_SHORT).show();
                    }


                    if (typesendsms.isChecked()) {
                        TypeSending = "API";
                    } else {
                        TypeSending = "SIM";
                    }

                    if (TextUtils.isEmpty(Textmsg)) {
                        Toast.makeText(Setting.this, "Please enter default text message ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    db.deleteTextMessage();
                    db.addTextMessage("", Textmsg, StartDate, StartTime, StopDate, StopTime, TypeSending);
                    Toast.makeText(Setting.this, "DATA SET : WITHOUT ANY ERROR ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Setting.this, MainActivity.class));
                    finish();


                }catch (Exception e){
                    Toast.makeText(Setting.this, "error :  "+e, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
//start
        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);}
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();}
        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        // end
        if (v == btnDatePicker2) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear2 = c.get(Calendar.YEAR);
            mMonth2 = c.get(Calendar.MONTH);
            mDay2 = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDate2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);}}, mYear2, mMonth2, mDay2);
            datePickerDialog.show();}
        if (v == btnTimePicker2) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour2 = c.get(Calendar.HOUR_OF_DAY);
            mMinute2 = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtTime2.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour2, mMinute2, false);
            timePickerDialog.show();
        }
    }


}
