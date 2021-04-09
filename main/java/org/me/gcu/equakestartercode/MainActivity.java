package org.me.gcu.equakestartercode;
//Filip Chojnacki S1712859

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnClickListener {
    private TextView rawDataDisplay;
    private ListView listdata;
    private Button submitButton;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    String result="";
    EditText et;
    EQClass earthq = null;
    ArrayList<EQClass> alist = null;
    private GoogleMap mMap;
    private int l;
    int scale;
    BufferedReader in = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");

       // rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);

        submitButton = (Button) findViewById(R.id.submitButton);

        listdata = (ListView) findViewById(R.id.mylist);

        Log.e("MyTag", "after startButton");
        // et = (EditText) findViewById(R.id.editText);
        getSearch();
        startProgress();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
         }
    public void getMarker(){

        LatLng landMark = new LatLng(0,0);
        if(l>0){
            for(int i = 0; i < l; i++) {
                EQClass eq = alist.get(i);
                landMark = new LatLng(eq.getGeolat(), eq.getGeolong());
                System.out.print(landMark);
                double magnitude = eq.getMagnitude();

                if(magnitude >= 3.0) {
                    mMap.addMarker(new MarkerOptions()
                            .position(landMark)
                            .title(eq.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                else if(magnitude >= 2.0) {
                    mMap.addMarker(new MarkerOptions()
                            .position(landMark)
                            .title(eq.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
                else if(magnitude >= 1.0) {
                    mMap.addMarker(new MarkerOptions()
                            .position(landMark)
                            .title(eq.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
                else {
                    mMap.addMarker(new MarkerOptions()
                            .position(landMark)
                            .title(eq.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
            }
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(landMark,scale));
        }else{System.out.println("List empty");}
    }
    public void getSearch(){
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

    }

    public void onClick(View aview) {
        Log.e("MyTag", "in onClick");
       // startProgress();
        getSearch();
        Log.e("MyTag", "after startProgress");

    }
    public void getListView(){
        final ArrayAdapter arrayAdapter = new ArrayAdapter
                (MainActivity.this, android.R.layout.simple_list_item_1, alist){
            @Override
            public  View getView(int position, View convertView, ViewGroup parent){
                listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EQClass data = (EQClass) getItem(position);
                        if(data.getMagnitude()>0){
                            String title = data.getAll();
                            System.out.println(title);
                          //  Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                            intent.putExtra("Title", title);

                            startActivity(intent);
                        }

                    }
                });

                View view = super.getView(position,convertView,parent);
                EQClass data = (EQClass) getItem(position);

                double magnitude = data.getMagnitude();
                if(magnitude >=3.0)
                {
                    view.setBackgroundColor(Color.RED);
                }
                else if (magnitude >=2.0)
                {
                    view.setBackgroundColor(Color.parseColor("#FF4500"));
                }
                else if (magnitude >=1.0){
                    view.setBackgroundColor(Color.YELLOW);
                } else{
                    view.setBackgroundColor(Color.GREEN);
                }
                return view;
            }

        }; listdata.setAdapter(arrayAdapter);

    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Task(urlSource).execute();
    } //

    private class Task extends AsyncTask<String, Integer, String>
    {
        int progress_status;
        private String url;
        public Task(String aurl)
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

            MainActivity.this.runOnUiThread(new Runnable()
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
            getListView();
            l=alist.size();
            getMarker();
        }
    }



}

