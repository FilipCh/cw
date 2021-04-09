package org.me.gcu.equakestartercode;
//Filip Chojnacki S1712859

import android.os.Build;

import android.util.Log;

import android.widget.ListView;


import androidx.annotation.RequiresApi;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.Locale;

public class DataParser {
    ArrayList<EQClass> alist;
    private ListView listdata;
    EQClass earthq;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<EQClass> dataParser(String result){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {



                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("channel")) {

                        alist = new ArrayList<EQClass>();


                    } else if (xpp.getName().equalsIgnoreCase("item")) {

                        earthq = new EQClass();


                    } else if (xpp.getName().equalsIgnoreCase("title")&& earthq !=null) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Title is " + temp);
                        earthq.setTitle(temp);

                    } else if (xpp.getName().equalsIgnoreCase("description") && earthq !=null) {
                        String temp = xpp.nextText();

                        Log.e("MyTag", "Description is " + temp);
                        String[] a = temp.split(";");
                        earthq.setLocation(a[1].substring(a[1].indexOf(":")+1));
                        earthq.setDepth(Integer.parseInt(a[3].replaceAll("\\D", "")));
                        earthq.setMagnitude(Double.parseDouble(a[4].substring(a[4].indexOf(":")+2)));
                        earthq.setDescription(temp);


                    } else if (xpp.getName().equalsIgnoreCase("link") && earthq!=null) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "URL is " + temp);
                        earthq.setLink(temp);

                    } else if (xpp.getName().equalsIgnoreCase("pubDate") && earthq !=null) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Publication date is " + temp);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                        LocalDate dateTime = LocalDate.parse(temp, formatter);
                        earthq.setPubDate(dateTime);
                        LocalDate a = earthq.getPubDate();
                        System.out.println(a);

                    } else if (xpp.getName().equals("category") && earthq !=null) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Category is " + temp);
                        earthq.setCategory(temp);

                    } else if (xpp.getName().equalsIgnoreCase("lat") && earthq !=null) {

                        String temp = xpp.nextText();
                        earthq.setGeolat(Double.parseDouble(temp));
                        Log.e("MyTag", "Lat is " + temp);


                    } else if (xpp.getName().equalsIgnoreCase("long") && earthq !=null) {
                        String temp = xpp.nextText();
                        earthq.setGeolong(Double.parseDouble(temp));
                        Log.e("MyTag", "Long is " + temp);
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item") ) {
                        //  Log.e("MyTag", "item is " + earthq.toString());

                        alist.add(earthq);


                    }
                }

                eventType = xpp.next();

            }


        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error " + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing" + ae1.toString());
        }

        Log.e("MyTag", "End document");
        return alist;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<EQClass> getAlist(){

        alist.sort(Comparator.comparing(EQClass::getMagnitude).reversed());
        return alist;
    }
}
