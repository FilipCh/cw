package org.me.gcu.equakestartercode;
//Filip Chojnacki S1712859
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;




public class Search extends AppCompatActivity implements View.OnClickListener {

    private ListView listdata;
    private Button submitButton;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    EditText et;
    EQClass earthq = null;
    ArrayList<EQClass> alist = null;
    LocalDate dateEnt;
    EditText date;
    LocalDate endDate;
    EditText enddate;
    DatePickerDialog datePickerDialog;
    ArrayList<EQClass> eq = new ArrayList<>();
    TextView north, south, east, west, magnitude, deep, shallow;
    String result="";
    BufferedReader in = null;
    private int l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Intent intent = getIntent();
        eq = (ArrayList<EQClass>) intent.getSerializableExtra("earthq");
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        date = (EditText) findViewById(R.id.date);
        date.setOnClickListener(this);
        enddate = (EditText) findViewById(R.id.enddate);
        enddate.setOnClickListener(this);
        Log.e("MyTag", "after startButton");

        north = (TextView) findViewById(R.id.north);
        south = (TextView) findViewById(R.id.south);
        east = (TextView) findViewById(R.id.east);
        west = (TextView) findViewById(R.id.west);
        deep = (TextView) findViewById(R.id.deep);
        shallow = (TextView) findViewById(R.id.shallow);
        magnitude = (TextView) findViewById(R.id.magnitude);

        startProgress();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View aview) {
        Log.e("MyTag", "in onClick");

        switch (aview.getId()){
            case R.id.submitButton:
                if (dateEnt!=null){
                getBest();}
                else{System.out.print("Wrong date selected");};

                System.out.println(dateEnt);
                System.out.println(endDate);
                break;
            case R.id.date:

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Search.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = format.format(c.getTime());
                                date.setText(strDate);
                                dateEnt=LocalDate.parse(strDate);
                                System.out.println(dateEnt);

                            }
                        }, mYear, mMonth, mDay );
                datePickerDialog.show();
                Log.e("MyTag", "after startProgress");
                break;
            case R.id.enddate:
                final Calendar endc = Calendar.getInstance();
                int endmYear = endc.get(Calendar.YEAR); // current year
                int endmMonth = endc.get(Calendar.MONTH); // current month
                int endmDay = endc.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Search.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                endc.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String endstrDate = format.format(endc.getTime());
                                enddate.setText(endstrDate);
                                endDate=LocalDate.parse(endstrDate);
                                System.out.println(endDate);


                            }
                        }, endmYear, endmMonth, endmDay );
                datePickerDialog.show();

                break;
            default:
                break;

        }}



    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<EQClass> getEarthq(){

        if(dateEnt == null) return null;
        if(endDate == null) endDate = dateEnt;
       // if(endDate.isBefore(dateEnt)) return null;
        ArrayList<EQClass> newlist = new ArrayList<>();
        int l = alist.size();
        for(int i = 0; i < l; i++){
            LocalDate temp = alist.get(i).getPubDate();
            if(temp.isBefore(dateEnt) || temp.isAfter(endDate))continue;
            newlist.add(alist.get(i));
        }

        return newlist;


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getBest(){
        ArrayList<EQClass> earthquakes1 = getEarthq();
        double wst = 1000;
        String maxwest = "";
        double est = -1000;
        String maxeast = "";
        double nth = -1000;
        String maxnorth = "";
        double sth = 1000;
        String maxsouth = "";
        double mag = 0;
        int deepest = 0;
        int shallowest = 1000;
        int l = earthquakes1.size();
        for(int i = 0; i < l; i++){
            EQClass eq = earthquakes1.get(i);
            if(eq.getMagnitude() > mag)mag = eq.getMagnitude();
            if(eq.getDepth() > deepest)deepest = eq.getDepth();
            if(eq.getDepth() < shallowest)shallowest = eq.getDepth();
            if(eq.getGeolong() < wst){
                wst = eq.getGeolong();
                maxwest = eq.getLocation();
            }
            if(eq.getGeolong() > est){
                est = eq.getGeolong();
                maxeast = eq.getLocation();
            }
            if(eq.getGeolat() > nth){
                nth = eq.getGeolat();
                maxnorth = eq.getLocation();
            }
            if(eq.getGeolat() < sth){
                sth = eq.getGeolat();
                maxsouth = eq.getLocation();
            }
        }
        magnitude.setText("Largest magnitude: " + mag);
        deep.setText("Deepest earthquake: " + deepest + "km");
        shallow.setText("Shallowest earthquake: " + shallowest + "km");
        north.setText("Furthest north earthquake: " + maxnorth);
        south.setText("Furthest south earthquake: " + maxsouth);
        west.setText("Furthest west earthquake: " + maxwest);
        east.setText("Furthest east earthquake: " + maxeast);

    }
    public void startProgress() {
        // Run network access on a separate thread;
        new TaskSearch(urlSource).execute();
    } //

    private class TaskSearch extends AsyncTask<String, Integer, String>
    {
        int progress_status;
        private String url;
        public TaskSearch(String aurl)
        {
            url = aurl;
        }

        @Override
        protected String doInBackground(String... params) {

            publishProgress(progress_status);

            URL aurl;
            URLConnection yc;

            String inputLine = "";

            try
            {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Toast.makeText(getApplicationContext(),  "IOException encountered", Toast.LENGTH_SHORT).show();
            }

            Search.this.runOnUiThread(new Runnable()
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void run() {
                    //parseData(result);
                    DataParser parser=new DataParser();
                    parser.dataParser(result);
                    alist=parser.getAlist();
                }
            });
            System.out.println(result);
            return result;

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            //display earthquakes
           // getListView();
            l=alist.size();

        }
    }
}