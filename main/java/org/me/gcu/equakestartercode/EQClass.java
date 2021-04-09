package org.me.gcu.equakestartercode;
//Filip Chojnacki S1712859
import java.time.LocalDate;
import java.util.Date;

public class EQClass {

    private String title;
    private String description;
    private String link;
    private LocalDate pubDate;
    private String category;
    private double geolat;
    private double geolong;

    private String time;
    private String location;
    private int depth;
    private double magnitude;

    public EQClass()
    {
        title = "";
        description = "";
        link = "";
        pubDate = null;
        category = "";
        geolat = 0;
        geolong = 0;

        time = "";
        location = "";

        depth = 0;
        magnitude = 0;
    }

    public EQClass(String atitle, String adescription, String alink, LocalDate apubDate, String acategory, double ageolat, double ageolong, String atime, String alocation,  int adepth, double amagnitude)
    {
        title = atitle;
        description = adescription;
        link = alink;
        pubDate = apubDate;
        category = acategory;
        geolat = ageolat;
        geolong = ageolong;

        time = atime;
        location = alocation;

        depth = adepth;
        magnitude = amagnitude;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String adescription) { description = adescription; }

    public String getLink()
    {
        return link;
    }

    public void setLink(String alink) { link = alink; }

    public LocalDate getPubDate() { return pubDate; }

    public void setPubDate(LocalDate apubDate)
    {
        pubDate = apubDate;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String acategory)
    {
        category = acategory;
    }

    public double getGeolat()
    {
        return geolat;
    }

    public void setGeolat(double ageolat)
    {
        geolat = ageolat;
    }

    public double getGeolong()
    {
        return geolong;
    }

    public void setGeolong(double ageolong)
    {
        geolong = ageolong;
    }

    public String getTime()
    {
        return time;
    }
    public void setTime(String atime)
    {
        time = atime;
    }

    public String getLocation()
    {
        return location;
    }
    public void setLocation(String alocation)
    {
        location = alocation;
    }



    public int getDepth()
    {
        return depth;
    }
    public void setDepth(int adepth)
    {
        depth = adepth;
    }

    public double getMagnitude()
    {
        return magnitude;
    }
    public void setMagnitude(double amagnitude)
    {
        magnitude = amagnitude;
    }


    public String getAll()
    {

        String temp;

        temp = "Location: " + location + "\nMagnitude: "+ magnitude + "\nLink: " + link+ "\nPublication Date: "+ pubDate+ "\nCategory: "+ category;

        return temp;
    }

    public String toString()
    {

        String temp;

        temp = "Location: " + location + "Magnitude: " + magnitude;

        return temp;
    }


}
