package smssutosend.yassinedeveloper.com.smsautosend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SMSsender extends AppCompatActivity {


     //   = "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message=".$message."&mobile=".$mobile."";

    EditText SMStext,SMSto;
    String smsto,smstext;
    RadioButton typesend;
    String TypeSending;
    boolean CheckingR = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smssender);
        setTitle("SMS manual Sending");

        final Button SendSMS = (Button) findViewById(R.id.sendsms);
        SMStext = (EditText) findViewById(R.id.smstext);
        SMSto = (EditText) findViewById(R.id.smsto);
        typesend = (RadioButton) findViewById(R.id.typesend);

        typesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckingR){
                    CheckingR = false;
                    typesend.setChecked(true);
                }else{
                    CheckingR = true;
                    typesend.setChecked(false);
                }
            }
        });

        SendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smstext = SMStext.getText().toString();
                smsto   = SMSto.getText().toString();
                if(typesend.isChecked()){
                    TypeSending = "API";
                }else{
                    TypeSending = "SIM";
                }
                if(TextUtils.isEmpty(smstext)){
                    Toast.makeText(SMSsender.this, "please enter sms text" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(smsto)){
                    Toast.makeText(SMSsender.this, "please enter number phone" ,Toast.LENGTH_SHORT).show();
                    return;
                }
            //    String url  = "http://sms.dream-technology.in/api/send_transactional_sms.php";



                if(TypeSending.equals("SIM")){
                    try {

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(smsto, null, smstext, null, null);
                        //SendSMS(messageData.get("text"),i.getNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{

                    String curl =  "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+smstext+"&mobile="+smsto+"&lang=HN&&adv=1" ;

                    WebView webView = (WebView) findViewById(R.id.webView1);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl(curl);

                }

                /*
                try
                {
                    java.net.URL url = new URL(Url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write("");
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.getInputStream().close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


*/

/*

*/
//                String url  = "http://sms.dream-technology.in/api/send_transactional_sms.php";

            //    String url =  "http://sms.dream-technology.in/api/send_transactional_sms.php?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+smstext+"&mobile="+smsto ;


/*

                //Initializing the RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(SMSsender.this);
                //Again creating the string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Creating the json object from the response
                                    JSONObject jsonResponse = new JSONObject(response);

                                    SMSto.setText("json repense");
                                    //If it is success
                                    if(jsonResponse.getString("ERORR").equalsIgnoreCase("Success")){
                                        //Asking user to confirm otp
                                        SMStext.setText("erorr");

                                    }else{
                                        //If not successful user may already have registered

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                    //    String ff = "?username=u1876&msg_token=7v0RXf&sender_id=SSKPPN&message="+ smstext+"&mobile="+smsto;

                        params.put("username","u1876");
                        params.put("msg_token","7v0RXf");
                        params.put("sender_id","SSKPPN" );
                        params.put("message",smstext );
                        params.put("mobile", smsto);
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);

                SMSto.setText("aaaaa");
                try {

                    java.net.URL url = new URL(urll);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(String.format( URLEncoder.encode(ff, "UTF-8")));
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.getInputStream().close();
                    SMSto.setText("ddddd");
                }
                catch (Exception e){e.printStackTrace();}

                // Instantiate <></>he RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(SMSsender.this);
               // String     url ="http://www.google.com";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                SMStext.setText("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SMStext.setText("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
*/

            }
        });
    }
}
