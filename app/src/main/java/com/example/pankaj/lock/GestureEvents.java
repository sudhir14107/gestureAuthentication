package com.example.pankaj.lock;

import android.view.MotionEvent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pankaj on 1/11/17.
 */

public class GestureEvents implements Serializable
{

    public  ArrayList<motion> list;
     void GestureEvent(motion event)
     {
         list= new ArrayList<motion>() ;
     }

    public void add(motion e)
    {
        list.add(e);

    }

    public int getEeventCount()
    {
        return  list.size();
    }

    public ArrayList<motion> getEventList()
     {
         return list;
     }

    public float getStartx()
    {
        return list.get(0).getx();
    }
    public float getStarty()
    {
        return list.get(0).gety();
    }


    public static double getVelocityWithAngle(double vx, double vy) {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public float getStartVelocityx(int n)
    {
        float sum=0;
        if(list.size()>n)
        for(int i =0;i < n; i++)
        {
            sum=sum+list.get(i).vx;
        }
        else
        {

            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).vx;
            }
        }

        return sum/n;
    }

    public float getStartVelocity(int n)
    {
        float sum=0;
        if(list.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+    (float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);
            }
        else
        {

            for(int i =0;i < list.size(); i++)
            {
                sum=sum+ (float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);
            }
        }

        return sum/n;
    }



    public float getEndVelocity(int n)
    {
        float sum=0;
        if(list.size()<n)
            for(int i =0;i < list.size(); i++)
            {
                sum=sum+(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);
            }
        else
        {

            for(int i =list.size()-n;i < list.size(); i++)
            {
                sum=sum+(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);
            }
        }

        return sum/n;
    }


    public float getEndVelocityx(int n)
    {
        float sum=0;
        if(list.size()<n)
            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).vx;
            }
        else
        {

            for(int i =list.size()-n;i < list.size(); i++)
            {
                sum=sum+list.get(i).vx;
            }
        }

        return sum/n;
    }



    public float getStartVelocityy(int n)
    {
        float sum=0;
        if(list.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+list.get(i).vy;
            }
        else
        {

            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).vy;
            }
        }

        return sum/n;
    }
    public float getEndVelocityy(int n)
    {
        float sum=0;
        if(list.size()<n)
            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).vy;
            }
        else
        {

            for(int i =list.size()-n;i < list.size(); i++)
            {
                sum=sum+list.get(i).vy;
            }
        }

        return sum/n;
    }


    public float getOrientation()
    {
        float sum=0;
        for(int i =0;i < list.size(); i++)
        {
            sum=sum+list.get(i).vx;
        }
    return (float)(sum/list.size());
    }

    public float getStartPressure(int n)
    {
        float sum=0;
        if(list.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+list.get(i).pressure;
            }
        else
        {

            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).pressure;
            }
        }

        return sum/n;
    }
    public float getEndPressure(int n)
    {
        float sum=0;
        if(list.size()<n)
            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).pressure;
            }
        else
        {

            for(int i =list.size()-n;i < list.size(); i++)
            {
                sum=sum+list.get(i).pressure;
            }
        }

        return sum/n;
    }



    public float getStartSize(int n)
    {
        float sum=0;
        if(list.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+list.get(i).size;
            }
        else
        {

            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).size;
            }
        }

        return sum/n;
    }
    public float getEndSize(int n)
    {
        float sum=0;
        if(list.size()<n)
            for(int i =0;i < list.size(); i++)
            {
                sum=sum+list.get(i).size;
            }
        else
        {

            for(int i =list.size()-n;i < list.size(); i++)
            {
                sum=sum+list.get(i).size;
            }
        }

        return sum/n;
    }



    public float maxVelocity()
    {

        float max=0;
        for(int i =0;i < list.size(); i++)
        {
           if(max<(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy))
               max=(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);;
        }

        return max;
    }


    public float minVelocity()
    {

        float min=100000000;
        for(int i =0;i < list.size(); i++)
        {
            if(min>(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy))
                min=(float)getVelocityWithAngle(list.get(i).vx,list.get(i).vy);;
        }

        return min;
    }



}
