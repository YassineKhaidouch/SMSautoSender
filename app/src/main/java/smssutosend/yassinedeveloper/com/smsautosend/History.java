package smssutosend.yassinedeveloper.com.smsautosend;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class History extends AppCompatActivity {
    SQLiteHandler db;
    RecyclerView recyclerview;
    List<DataDetails> dataDetails = new ArrayList<>();

    Cursor cursor;
    File sd;
    String csvFile = "";
    File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History of SMS  sent || Export to Excel");
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
         db = new SQLiteHandler(getApplicationContext());
        cursor = db.fetch();
        Date dNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
        String current_datetime = format.format(dNow);

        csvFile = current_datetime+".xls";
        sd = Environment.getExternalStorageDirectory();
        directory = new File(sd.getAbsolutePath()+"/SMSAUTOSENDER/");
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }




        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    String numberPhone = cursor.getString(cursor.getColumnIndex("number"));
                    String calltype = cursor.getString(cursor.getColumnIndex("calltype"));
                    String calldate = cursor.getString(cursor.getColumnIndex("calldate"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));
                    String id = cursor.getString(cursor.getColumnIndex("id"));

                    dataDetails.add(new DataDetails(
                            numberPhone,
                            calltype,
                            status,
                            calldate
                    ));

                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        RecyclerviewAdapter2 recycler = new RecyclerviewAdapter2(dataDetails);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(recycler)   ;


        Button Export = (Button) findViewById(R.id.export);
        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "PhoneNumber"));
            sheet.addCell(new Label(1, 0, "Call Type"));
            sheet.addCell(new Label(2, 0, "Call Date"));
            sheet.addCell(new Label(3, 0, "Message Status"));
            if (cursor != null) {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        String numberPhone = cursor.getString(cursor.getColumnIndex("number"));
                        String calltype = cursor.getString(cursor.getColumnIndex("calltype"));
                        String calldate = cursor.getString(cursor.getColumnIndex("calldate"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));
                        String id = cursor.getString(cursor.getColumnIndex("id"));

                        /*
                        if(numberPhone != null) {
                            dataDetails.add(new DataDetails(
                                    numberPhone,
                                    calltype,
                                    status,
                                    calldate
                            ));
                            */
                            int i = cursor.getPosition() + 1;
                            sheet.addCell(new Label(0, i,numberPhone ));
                            sheet.addCell(new Label(1, i, calldate));
                            sheet.addCell(new Label(2, i, calldate));
                            sheet.addCell(new Label(3, i, "Sent"));
                        // move to next row
                    } while (cursor.moveToNext());
                }
            }

            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(), "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch (WriteException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Exception write : "+e, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "exception io : "+e, Toast.LENGTH_SHORT).show();
        }

            }
        });



    }
}
