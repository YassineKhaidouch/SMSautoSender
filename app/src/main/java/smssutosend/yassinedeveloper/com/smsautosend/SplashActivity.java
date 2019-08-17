package smssutosend.yassinedeveloper.com.smsautosend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {

    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    public static final int CONTACT_READ_PERMISSION  = 4;
    private int STORAGE_PERMISSION_CODE = 23;
    private int READCALLLOG_PERMISSION_CODE = 20;
    int SEND_SMS_PERMISSION = 123;

    public static final int RequestPermissionCode = 1;


    Context context;
    Activity activity;
    private SQLiteHandler db;
    HashMap<String, String> messageData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(Build.VERSION.SDK_INT >= 23){

            if(checkPermission()){
           //     Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }



            if(!marshMallowPermission.checkPermissionForStorage()){
                marshMallowPermission.requestPermissionForStorage();
            }
            if(!marshMallowPermission.checkPermissionReadCallLog()){
                marshMallowPermission.requestPermissionReadCallLog();

            }
            if(!marshMallowPermission.checkPermissionForContact()){
                marshMallowPermission.requestPermissionForContact();

            }
            if(!marshMallowPermission.checkPermissionSendSMS()){
                marshMallowPermission.requestPermissionSendSMS();
            }

        }

        db = new SQLiteHandler(getApplicationContext());
        messageData = db.getTextMessage();


        if(messageData.get("id") != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashActivity.this, Setting.class));
            finish();
        }

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                        WRITE_EXTERNAL_STORAGE,
                        READ_CONTACTS,
                        READ_CALL_LOG,
                        SEND_SMS,
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean WriteDataPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadCallLogPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean SendSMSPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (WriteDataPermission && ReadContactsPermission && ReadCallLogPermission && SendSMSPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);
        int FourPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }


}
