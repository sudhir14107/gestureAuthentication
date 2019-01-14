package com.example.pankaj.lock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.util.Log;

public class temp
{
    int check =0;
    List<GestureEvents> mElist =   new ArrayList();
    final int REP_SIZE = 96;
    ArrayList<double[]> numericalRep = new ArrayList<double[]>();
    final double THRESHOLD = 0.001;
    final int TOLERANCE = 5;
    double[] means = new double[REP_SIZE];
    double[] variances = new double[REP_SIZE];



    int data_count=0;
    ArrayList<Gesture> mGlist=new ArrayList();

    public temp(ArrayList<Gesture> gestureList, ArrayList<GestureEvents> list)

    {

        mElist=list.subList(0,gestureList.size());
        mGlist=gestureList;
        data_count=mElist.size();
        Log.d("MachineLearning",mElist.size()+":size :"+gestureList.size());
        for(GestureEvents g : mElist)
        {
            Log.d("MachineLearning"," Moiton Event data:"+g.list.size());
        }
      /*
        for (Gesture gesture : gestureList)
        {

            System.out.println(gestureList.size()+"XXX"+numericalRep.size());
            numericalRep.add(gestureToArray(gesture));
        }

        for (int i = 0; i < REP_SIZE; i++)
        {
            double sum = 0.0;
            for (double[] gestureRep : numericalRep)
            {
                sum += gestureRep[i];
            }
            means[i] = sum / numericalRep.size();
        }
        for (int i = 0; i < REP_SIZE; i++)
        {
            double temp = 0;
            for (double[] gestureRep : numericalRep)
            {
                temp += (means[i] - gestureRep[i]) * (means[i] - gestureRep[i]);
            }
            variances[i] = temp / numericalRep.size();
        }*/
    }

    public boolean authenticate(Gesture testGesture)
    {
        check =1;
      /*  Log.d("filep","EVENT DATA AUTH :("+mElist.size()+"):"+mElist.get(0).list.get(0).getx()+"--"+mElist.get(0).list.get(0).gety());
        System.out.println("DATA"+testGesture.getStrokes().get(0).points[0]);

        double[] gestureRep = gestureToArray(testGesture);
        double[] confidences = new double[REP_SIZE];
        double confidence = 1.0;
        int count = 0;
        Log.d("MachineLearning","size :"+variances.length);
        for (int i = 0; i < REP_SIZE; i++)
        {
            if (variances[i] != 0)
            {
                confidences[i] = gauss(gestureRep[i], means[i], variances[i]);
                confidence *= confidences[i];
                count++;
            } else if (gestureRep[i] != 0)
            {
                return false;
            }
        }
        check=0;
        confidence *= count;
        return (confidence >= THRESHOLD);
        */
        return true;
    }

    private double[] gestureToArray(Gesture gesture)
    {

        Log.d("MachineLearning","size :");

        double[] gestureValues = new double[REP_SIZE];
        ArrayList<GestureStroke> strokes = gesture.getStrokes();
        for (int i = 0; i < REP_SIZE / 8; i++)
        {
            if (i < strokes.size())
            {


                GestureStroke currentStroke = strokes.get(i);
                gestureValues[8 * i + 0] = currentStroke.length;
                gestureValues[8 * i + 1] = currentStroke.points[0];
                gestureValues[8 * i + 2] = currentStroke.points[1];
                gestureValues[8 * i + 3] = currentStroke.points[currentStroke.points.length - 2];
                gestureValues[8 * i + 4] = currentStroke.points[currentStroke.points.length - 1];
                gestureValues[8 * i + 5] = currentStroke.boundingBox.width();
                gestureValues[8 * i + 6] = currentStroke.boundingBox.height();
                long duration = 0;
                try
                {
                    Field f = currentStroke.getClass().getDeclaredField("timestamps");
                    f.setAccessible(true);
                    long[] timestamps = (long[]) f.get(currentStroke);
                    duration = timestamps[timestamps.length - 1]
                            - timestamps[0];
                } catch (Exception e) {
                    Log.e("ERROR", "Reflection didn't work");
                }
                gestureValues[8 * i + 7] = duration;

                if(check==1)
                {
                    System.out.println("------------------------------------feature list----------------------------------------------");
                    System.out.println("feature 0 -strokes Size   :" + strokes.size());
                    System.out.println("feature 1 -length   :" + gesture.getLength());
                    System.out.println("feature 2 -duration :" + duration);
                    System.out.println("feature 3 -points   :" + currentStroke.points.length);
                    System.out.println("feature 4 -boxw   :" + currentStroke.boundingBox.width());
                    System.out.println("feature 5 -boxh    :" + currentStroke.boundingBox.height());
                    System.out.println("feature 6 -points(x,y)  :" + currentStroke.points.length);

                }
            }
            else
            {
                for (int j = 0; j < 8; j++)
                {
                    gestureValues[8 * i + j] = 0.0;
                }
            }
        }
        return gestureValues;
    }

    private double gauss(double x, double mean, double variance)
    {
        return Math.exp(-(x - mean) * (x - mean) / (TOLERANCE * 2 * variance));
    }
}


/*

package com.example.pankaj.lock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.util.Log;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Doodle
{
    int check =0;
    final int REP_SIZE = 96;
    ArrayList<double[]> numericalRep = new ArrayList<double[]>();
    final double THRESHOLD = 0.001;
    final int TOLERANCE = 5;
    double[] means = new double[REP_SIZE];
    double[] variances = new double[REP_SIZE];



    int data_count=0;
    List<GestureEvents> mElist =   new ArrayList();
    ArrayList<Gesture> mGlist=new ArrayList();
    ArrayList<double[]> data = new ArrayList<double[]>();


    public Doodle(ArrayList<Gesture> gestureList, ArrayList<GestureEvents> list)
    {

        Log.d("MachineLearning",list.size()+":1size :"+gestureList.size());
        mElist=list;//.subList(0,gestureList.size());
        mGlist=gestureList;
        data_count=mElist.size();
        Log.d("MachineLearning",mElist.size()+":size :"+gestureList.size());
        for(GestureEvents g : mElist)
        {
            Log.d("MachineLearning"," Moiton Event data:"+g.list.size());
        }

        for (int i=0;i<mElist.size();i++)
        {
            data.add(gestureToArray(mGlist.get(i),mElist.get(i)));
        }


    }

    public boolean authenticate(Gesture testGesture,GestureEvents  testEvent)
    {
        check =1;

        double[] test =gestureToArray(testGesture,testEvent);
        double result= predict(data,test);
        Log.d("MachineLearning","Result :"+result);

      return true;
    }

    private double[] gestureToArray(Gesture gesture,  GestureEvents  event )
    {

        Log.d("MachineLearning","size STROKE:"+gesture.getStrokes().size());

        double[] gestureValues = new double[REP_SIZE];
        ArrayList<GestureStroke> strokes = gesture.getStrokes();



                GestureStroke currentStroke = strokes.get(0);
                gestureValues[0] = currentStroke.length;
                gestureValues[1] = currentStroke.points[0];
                gestureValues[2] = currentStroke.points[1];
                gestureValues[3] = currentStroke.points[currentStroke.points.length - 2];
                gestureValues[4] = currentStroke.points[currentStroke.points.length - 1];
                gestureValues[5] = currentStroke.boundingBox.width();
                gestureValues[6] = currentStroke.boundingBox.height();
                long duration = 0;
                try
                {
                    Field f = currentStroke.getClass().getDeclaredField("timestamps");
                    f.setAccessible(true);
                    long[] timestamps = (long[]) f.get(currentStroke);
                    duration = timestamps[timestamps.length - 1]
                            - timestamps[0];
                } catch (Exception e) {
                    Log.e("ERROR", "Reflection didn't work");
                }
                gestureValues[7] = duration;
                gestureValues[8] = event.getStartx();
                gestureValues[9] = event.getStarty();
                gestureValues[10] = event.getVelocityx();
                gestureValues[11] = event.getVelocityy();




        if(check==1)
                {
                    System.out.println("------------------------------------feature list----------------------------------------------");
                    System.out.println("feature 0 -strokes Size   :" + strokes.size());
                    System.out.println("feature 1 -length   :" + gesture.getLength());
                    System.out.println("feature 2 -duration :" + duration);
                    System.out.println("feature 3 -points   :" + currentStroke.points.length);
                    System.out.println("feature 4 -boxw   :" + currentStroke.boundingBox.width());
                    System.out.println("feature 5 -boxh    :" + currentStroke.boundingBox.height());
                    System.out.println("feature 6 -points(x,y)  :" + currentStroke.points.length);

                }
        else
        {
            gestureValues[12] = 1;
        }
        return gestureValues;
    }

    private double gauss(double x, double mean, double variance)
    {
        return Math.exp(-(x - mean) * (x - mean) / (TOLERANCE * 2 * variance));
    }


    private double predict( ArrayList<double[]> myValues, double[] test)
    {
        double classify=0.0;
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("1"));
        attributes.add(new Attribute("2"));
        attributes.add(new Attribute("3"));
        attributes.add(new Attribute("4"));
        attributes.add(new Attribute("5"));
        attributes.add(new Attribute("6"));
        attributes.add(new Attribute("7"));
        attributes.add(new Attribute("8"));
        attributes.add(new Attribute("9"));
        attributes.add(new Attribute("10"));
        attributes.add(new Attribute("12"));
        attributes.add(new Attribute("13"));


        Instances dataRaw = new Instances("TestInstances", attributes, 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1); // Assuming z (z on lastindex) as classindex

        for (double[] a : myValues) {
            dataRaw.add(new DenseInstance(1.0, a));
        }

        try {
            // Then train or build the algorithm/model on instances (dataRaw) created above.

            MultilayerPerceptron mlp = new MultilayerPerceptron(); // Sample algorithm, go through about neural networks to use this or replace with appropriate algorithm.
            mlp.buildClassifier(dataRaw);

            // Create a test instance,I think you can create testinstance without
            // classindex value but cross check in weka as I forgot about it.

            double[] values = test;// sample values
            DenseInstance testInstance = new DenseInstance(1.0, values);
            testInstance.setDataset(dataRaw); // To associate with instances object

            // now you can clasify
             classify = mlp.classifyInstance(testInstance);
            return classify;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return  classify;
    }
  }

 */