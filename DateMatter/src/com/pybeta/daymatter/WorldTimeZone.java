// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter;

import java.io.Serializable;

public class WorldTimeZone
    implements Serializable
{

    public WorldTimeZone()
    {
    }

    public String getCityName()
    {
        return cityName;
    }

    public String getCityNameTW()
    {
        return cityName_tw;
    }

    public String getCityNameZH()
    {
        return cityName_zh;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public String getCountryNameTW()
    {
        return countryName_tw;
    }

    public String getCountryNameZH()
    {
        return countryName_zh;
    }

    public String getGMT()
    {
        return gmt;
    }

    public int getId()
    {
        return id;
    }

    public int getRawOffset()
    {
        return rawOffset;
    }

    public long getTime2Modify()
    {
        return time2Modify;
    }

    public void setCityName(String s)
    {
        cityName = s;
    }

    public void setCityNameTW(String s)
    {
        cityName_tw = s;
    }

    public void setCityNameZH(String s)
    {
        cityName_zh = s;
    }

    public void setCountryName(String s)
    {
        countryName = s;
    }

    public void setCountryNameTW(String s)
    {
        countryName_tw = s;
    }

    public void setCountryNameZH(String s)
    {
        countryName_zh = s;
    }

    public void setGMT(String s)
    {
        gmt = s;
    }

    public void setId(int i)
    {
        id = i;
    }

    public void setRawOffset(int i)
    {
        rawOffset = i;
    }

    public void setTime2Modify(long l)
    {
        time2Modify = l;
    }

    public String toString()
    {
        return (new StringBuilder("WorldTime= countryName: ")).append(countryName).append(" ,countryNameZH: ").append(countryName_zh).append(" , countryName_tw").append(countryName_tw).append(" ,cityName: ").append(cityName).append(" ,cityNameZH: ").append(cityName_zh).append(", cityName_tw: ").append(cityName_tw).append(" ,gmt: ").append(gmt).append(" ,rawOffset: ").append(rawOffset).append(" ,id: ").append(id).append(" ,time2Modify: ").append(time2Modify).toString();
    }

    private String cityName;
    private String cityName_tw;
    private String cityName_zh;
    private String countryName;
    private String countryName_tw;
    private String countryName_zh;
    private String gmt;
    private int id;
    private int rawOffset;
    private long time2Modify;
}
