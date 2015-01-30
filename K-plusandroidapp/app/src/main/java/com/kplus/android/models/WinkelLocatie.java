package com.kplus.android.models;

import android.location.Location;

/**
 * Created by Vasco on 30-1-2015.
 */
public class WinkelLocatie
{
    private double maxLongitude, minLongitude;
    private double maxLatitude, minLatitude;
    private String name;

    public WinkelLocatie(String name, double maxLongitude, double minLongitude, double maxLatitude, double minLatitude)
    {
        this.name = name;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
        this.maxLatitude  = maxLatitude;
        this.minLatitude  = minLatitude;
    }

    public String getName(){ return name; }
    public double getMaxLongitude(){ return maxLongitude; }
    public double getMinLongitude(){ return minLongitude; }
    public double getMaxLatitude(){ return maxLatitude; }
    public double getMinLatitude(){ return minLatitude; }

    public boolean userInRange(Location userLocation)
    {
        if(userLocation.getLatitude() > minLatitude && userLocation.getLatitude() < maxLatitude)
        {
            if(userLocation.getLongitude() > minLongitude && userLocation.getLongitude() < maxLongitude)
            {
                return true;
            }
        }
        return false;
    }
}
