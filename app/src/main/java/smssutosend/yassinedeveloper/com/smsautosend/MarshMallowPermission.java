package smssutosend.yassinedeveloper.com.smsautosend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by ruhaan on 7/28/2016.
 */
public class MarshMallowPermission {


    public static final int CONTACT_READ_PERMISSION  = 4;
    private int STORAGE_PERMISSION_CODE = 23;
    private int READCALLLOG_PERMISSION_CODE = 20;
    int SEND_SMS_PERMISSION = 123;

    private SharedPreferences permissionStatus;
    Context context;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }


//check permission storage
public boolean checkPermissionForStorage(){
    int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    int result_wrtie = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (result == PackageManager.PERMISSION_GRANTED && result_wrtie == PackageManager.PERMISSION_GRANTED){
        return true;
    } else {
        return false;
    }
}
public void requestPermissionForStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
            // Toast.makeText(activity, "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }
//check permission storage
public boolean checkPermissionReadCallLog(){
    int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG);
    if (result == PackageManager.PERMISSION_GRANTED){
        return true;
    } else {
        return false;
    }
}
public void requestPermissionReadCallLog(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)  ){

        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_CALL_LOG},READCALLLOG_PERMISSION_CODE);
        }
    }


    //Requesting permission
    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){
            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }else{}
        }
    }


    public boolean checkPermissionForContact(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForContact(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)){
         //   Toast.makeText(activity, "Contact permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_CONTACTS},CONTACT_READ_PERMISSION);
        }
    }
  public boolean checkPermissionSendSMS(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    public void requestPermissionSendSMS(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)){
        //    Toast.makeText(activity, "Contact permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION);
        }
    }

// permissions



}