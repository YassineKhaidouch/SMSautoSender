package smssutosend.yassinedeveloper.com.smsautosend;

/**
 * Created by root on 1/6/18.
 */

public class DataDetails {

    String NUMBER ;
    String TYPE;
    String STATUS;
    String DATE;

    public DataDetails(String number, String type, String status, String date) {
        NUMBER = number;
        TYPE = type;
        DATE = date;
        STATUS = status;
    }
    public String getDate(){
        return DATE;
    }
    public String getNumber(){
        return NUMBER;
    }
    public String getstatus(){return  STATUS;}
    public String getType(){return TYPE;}

}
