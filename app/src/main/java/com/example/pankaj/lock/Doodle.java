
package com.example.pankaj.lock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import weka.classifiers.functions.SMO;
import libsvm.svm;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureStroke;
import android.graphics.Path;
import android.util.Log;



import  weka.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffSaver;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class Doodle
{
    int check =0;
    String user_dir="/data/user/0/com.example.vinothramss.Sher_Lock/files/gesture_users/";
    final int REP_SIZE = 12;
    ArrayList<double[]> numericalRep = new ArrayList<double[]>();
    final double THRESHOLD = 0.001;
    final int TOLERANCE = 5;
    double[] means = new double[REP_SIZE];
    double[] variances = new double[REP_SIZE];

    public String arffData;
    int data_count=0;
    public int currentSymbol;
    int last_data=10;
    List<GestureEvents> mElist =   new ArrayList();
    ArrayList<Gesture> mGlist=new ArrayList();
    ArrayList<double[]> data = new ArrayList<double[]>();

    ArrayList<double[]> data_other = new ArrayList<double[]>();


    public Doodle(ArrayList<Gesture> gestureList, ArrayList<GestureEvents> list)
    {

        int p=gestureList.size();
        if(p>list.size())
            p=list.size();

        // Log.d("MachineLearning",gestureList.size()+":1size :"+list.size());
        mGlist=gestureList;
        data_count=p;
        if(list.size()>0)
            mElist=list.subList(0,p);
        //Log.d("MachineLearning",mElist.size()+":size :"+gestureList.size());

        for (int i=0;i<p;i++)
        {
            data.add(gestureToArray(mGlist.get(i),mElist.get(i),30,1));
            Log.d("checkData","Current User: "+gestureToArray(mGlist.get(i),mElist.get(i),30,1)[0]+"--"+gestureToArray(mGlist.get(i),mElist.get(i),30,1)[29]);
        }


    }

    public void addNewDataDoodle(ArrayList<Gesture> gestureList, ArrayList<GestureEvents> list)
    {

        int p=gestureList.size();
        if(p>list.size())
            p=list.size();

        // Log.d("MachineLearning",list.size()+":1size :"+gestureList.size());
        mGlist=gestureList;
        data_count=p;
        if(list.size()>0)
            mElist=list.subList(0,p);
        ///   Log.d("MachineLearning",mElist.size()+":size :"+gestureList.size());
//        for(GestureEvents g : mElist)
//        {
//            // Log.d("MachineLearning"," Moiton Event data:"+g.list.size());
//        }

        for (int i=0;i<p;i++)
        {
            data_other.add(gestureToArray(mGlist.get(i),mElist.get(i),30,0));
        }


    }

    public boolean isFilePresent(String path)
    {
        File file = new File(path);
        return file.exists();
    }

    public void shuffleData()
    {

        Collections.shuffle(data_other);

        Log.d("MachineLearning","Shuffling done");

        int size=data.size();
        List<double[]> list= data_other.subList(0,size);
        for ( int i=0;i<list.size();i++)
        {
            data.add(list.get(i));
        }
        Log.d("MachineLearning","Shuffling return");


    }

    public boolean authenticate(Gesture testGesture,GestureEvents  testEvent,ArrayList<String> users,String current,int symbol)
    {

        currentSymbol=symbol;
        String mUserModel=user_dir+current+"/"+current+currentSymbol+"model";

        File mUserFileModel = new File(mUserModel);

//mUserFileModel.delete();
//-------------------------------------------------------- IF MODEL EXIST----------------------------------------------------------
        if(false/*mUserFileModel.exists()*/)
        {

            Log.d("MachineLearning","MODEL IN FILE EXITS !!!!!");

            FastVector attributes = new FastVector();

            attributes.add(new Attribute("a"));//1
            attributes.add(new Attribute("b"));//2
            attributes.add(new Attribute("c"));//3
            attributes.add(new Attribute("d"));//4
            attributes.add(new Attribute("e"));//5
            attributes.add(new Attribute("f"));//6
            attributes.add(new Attribute("g"));//7
            attributes.add(new Attribute("h"));//8
            attributes.add(new Attribute("i"));//9
            attributes.add(new Attribute("j"));//10
            attributes.add(new Attribute("k"));//11
            attributes.add(new Attribute("l"));//12
            attributes.add(new Attribute("m"));//13
            attributes.add(new Attribute("n"));//14
            attributes.add(new Attribute("o"));//15
            attributes.add(new Attribute("p"));//16
            attributes.add(new Attribute("q"));//17
            attributes.add(new Attribute("r"));//18
            attributes.add(new Attribute("s"));//19
            attributes.add(new Attribute("t"));//20
            attributes.add(new Attribute("u"));//21
            attributes.add(new Attribute("v"));//22
            attributes.add(new Attribute("w"));//23
            attributes.add(new Attribute("w1"));//24
            attributes.add(new Attribute("w2"));//25
            attributes.add(new Attribute("w3"));//26
            attributes.add(new Attribute("w4"));//27
            attributes.add(new Attribute("w5"));//28
            attributes.add(new Attribute("w6"));//29


            FastVector classLabels = new FastVector(2);
            classLabels.add("1.0");
            classLabels.add("0.0");
            Attribute classAtt = new Attribute("class", classLabels);

            attributes.add(classAtt);

            Instances dataRaw = new Instances("TestInstances", attributes, 0);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

            double[] demoTest = gestureToArray(testGesture, testEvent, 29, -1);
            double[] values = demoTest;// sample values
            DenseInstance testInstance = new DenseInstance(1.0, values);
            testInstance.setDataset(dataRaw); // To associate with instances object

            double classify = 0.0;
            try {
                Classifier mlp = (Classifier) weka.core.SerializationHelper.read(mUserModel);
                Log.d("MachineLearning","-->TEST :"+testInstance);
                Log.d("MachineLearning",mUserModel+"-->MODEL :"+mlp);

                // now you can clasify
                String result = testInstance.classAttribute().value((int) mlp.classifyInstance(testInstance));

                Log.d("MachineLearning","MODEL IN FILE EXITS !!!!!");


                if (result.equals("0.0"))
                    classify = 1.0;

            } catch (Exception e)
            {
                e.printStackTrace();
            }

            double result = classify;

            if (result == 1)
                return true;
            Log.d("MachineLearning", "Result :" + result);

            return false;
        }


        //----------------------------------------------------------------------------------------------------------------------------------






        Log.d("TRAININGtime","---------------- FUNCTION CALL ----------------------:"+System.currentTimeMillis());

        Log.d("MachineLearningxxxx","Training Size :"+data.size());
        Log.d("MachineLearning","user name ->"+current);

        Set<String> hs = new HashSet<>();
        hs.addAll(users);
        hs.remove(current);
        users.clear();
        users.addAll(hs);


        Log.d("TRAININGtime","---------------- STEP1----------------------:"+System.currentTimeMillis());
        // list_file();
        // Log.d("Machinelearning","Users LIST"+users.size());
        int i;
        for(i=0;i<users.size();i++)
        {
            File  mUserFile=new File(user_dir+users.get(i)+"/"+users.get(i)+currentSymbol);
            //Log.d("checkData","Others User: "+users.get(i));
            Log.d("Machinelearning","user name:"+users.get(i));
            GestureLibrary userStore = GestureLibraries.fromFile(mUserFile);
              Log.d("Machinelearning","user Directory:"+mUserFile.getAbsolutePath());
            ArrayList<GestureEvents> mElist =   new ArrayList();
            String EventFile=mUserFile + "E";
            Log.d("Machinelearning","Event file:"+EventFile);
            Log.d("TRAININGtime","---------------- minSTEP1----------------------:"+System.currentTimeMillis());

            try {

                if (isFilePresent(EventFile) == true)
                {
                    FileInputStream fis = new FileInputStream(new File(EventFile));
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    mElist = (ArrayList<GestureEvents>) ois.readObject();
                      Log.d("Machinelearning", "event Auth:" + mElist.size());
                    ois.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            userStore.load();
            Log.d("TRAININGtime","---------------- minSTEP2----------------------:"+System.currentTimeMillis());

            ArrayList<Gesture> gesturesFromFile = new ArrayList<Gesture>();
            for (String entry : userStore.getGestureEntries())
            {
                gesturesFromFile.addAll(userStore.getGestures(entry));
            }
            Log.d("TRAININGtime","---------------- minSTEP3----------------------:"+System.currentTimeMillis());

            addNewDataDoodle(gesturesFromFile,mElist);
            Log.d("MachineLearning","------------> ADDING :("+gesturesFromFile.size()+","+mElist.size()+")");
            Log.d("MachineLearning","Training Size :"+data.size());
        }


        Log.d("MachineLearning","before shuffling "+data_other.size()+"------>"+data.size());

        shuffleData();

        Log.d("MachineLearning","after shuffling "+data_other.size()+"------>"+data.size());

        Log.d("TRAININGtime","---------------- STEP2----------------------:"+System.currentTimeMillis());


        check =1;

        double[] test =gestureToArray(testGesture,testEvent,29,1);
        // String dataRAW=predict(data,test,mUserModel);
        double result= predict(data,test,mUserModel);


        Log.d("MachineLearning","Result :"+result);


        if(result==1)
            return true;

        return false;

    }

    private void list_file()
    {
        File mUserDir=new File(user_dir);
        if(mUserDir != null)
        {
            for (File file : mUserDir.listFiles())
            {
                Log.d("machinelearning","\nFILE --> "+file.getName());
            }
        }
    }



    private double[] gestureToArray(Gesture gesture,  GestureEvents  event ,int size ,int clas)
    {
        double[] gestureValues = new double[size];
        ArrayList<GestureStroke> strokes = gesture.getStrokes();

        GestureStroke currentStroke = strokes.get(0);
        Path p=currentStroke.getPath();
        Path.FillType k=p.getFillType();
        gestureValues[0] = 0;//currentStroke.length;
        gestureValues[1] = 0;//currentStroke.points[0];
        gestureValues[2] = 0;//currentStroke.points[1];
        gestureValues[3] = 0;//currentStroke.points[currentStroke.points.length - 2];
        gestureValues[4] = 0;//currentStroke.points[currentStroke.points.length - 1];
        gestureValues[5] = 0;//currentStroke.boundingBox.width();
        gestureValues[6] =  Math.pow(currentStroke.points[0]-currentStroke.points[currentStroke.points.length - 2],2) -Math.pow(currentStroke.points[1]-currentStroke.points[currentStroke.points.length - 1],2);                   //  0;//currentStroke.boundingBox.height();
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

        //  Log.e("machineLearning", "ERRORp"+event.list.size());
        gestureValues[7] = duration;
        gestureValues[8] = 0;//event.getOrientation();// event.getStartx();
        gestureValues[9] = 0;//event.getStarty();
        gestureValues[10] = event.getStartVelocityx(last_data);
        gestureValues[11] = event.getStartVelocityy(last_data);
        gestureValues[12] = event.getEndVelocityx(last_data);
        gestureValues[13] = event.getEndVelocityy(last_data);

        gestureValues[14] = event.getStartPressure(last_data);
        gestureValues[15] = event.getStartSize(last_data);
        gestureValues[16] = event.getEndPressure(last_data);
        gestureValues[17] = event.getEndSize(last_data);
        gestureValues[18] = event.getStartVelocity(last_data);
        gestureValues[19] = event.getEndVelocity(last_data);
        gestureValues[20] = event.minVelocity();
        gestureValues[21] = event.maxVelocity();
        gestureValues[22] = event.list.size();
        gestureValues[23] = getStartAcc(event.list,last_data);
        gestureValues[24] = getEndAcc(event.list,last_data);
        gestureValues[25] = getStartAccx(event.list,last_data);
        gestureValues[26] = getEndAccx(event.list,last_data);
        gestureValues[27] = getStartAccy(event.list,last_data);
        gestureValues[28] = getEndAccy(event.list,last_data);






        Log.d("Feature","MOTIONevents: "+event.list.size());


        if((check==1)||(clas==-1))
        {
            System.out.println("------------------------------------feature list----------------------------------------------");
            System.out.println("feature 0 -strokes Size   :" + strokes.size());
            System.out.println("feature 1 -length   :" + gesture.getLength());
            System.out.println("feature 2 -duration :" + duration);
            System.out.println("feature 3 -points   :" + currentStroke.points.length);
            System.out.println("feature 4 -boxw   :" + currentStroke.boundingBox.width());
            System.out.println("feature 5 -boxh    :" + currentStroke.boundingBox.height());
            System.out.println("feature 6 -points(x)  :" + currentStroke.points[0]+"--->"+event.getStartx());
            System.out.println("feature 7 -points(y)  :" + currentStroke.points[1]+"--->"+event.getStarty());
            // Log.e("ACCELERATION :","------------------------------------------------------------------:"+event.list.size());
            getStartAccx(event.list,last_data);


        }
        else
        {
            gestureValues[29] = clas;
        }
        return gestureValues;
    }





    public double getStartAccx(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(l.get(i+1).vx-l.get(i).vx));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }
//
//        for (int i=0;i<a.size();i++)
//        {
//         //  Log.e("ACCELERATION :","valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }



    public double getEndAccx(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();


        //Log.e("ACCELERATION :","error :"+l.size());

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(l.get(i+1).vx-l.get(i).vx));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }


//        for (int i=0;i<a.size();i++)
//        {
//           // Log.e("ACCELERATION :","error : valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()<n)
            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =a.size()-n;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }


    public double getStartAccy(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(l.get(i+1).vy-l.get(i).vy));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }

//        for (int i=0;i<a.size();i++)
//        {
//          //  Log.e("ACCELERATION :","valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }



    public double getEndAccy(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(l.get(i+1).vy-l.get(i).vy));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }
//
//        for (int i=0;i<a.size();i++)
//        {
//            //Log.e("ACCELERATION :","valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()<n)
            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =a.size()-n;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }


    ////////////////////////////////////////////////////////////


    public static double getVelocityWithAngle(double vx, double vy) {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public  double xyVelocity(double vx,double vy)
    {
        return (float)getVelocityWithAngle(vx,vy);
    }


    public double getStartAcc(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(xyVelocity(l.get(i+1).vx,l.get(i+1).vy)- xyVelocity(l.get(i).vx,l.get(i).vy)));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }

//        for (int i=0;i<a.size();i++)
//        {
//          //  Log.e("ACCELERATION :","valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()>n)
            for(int i =0;i < n; i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }

    public double getEndAcc(ArrayList<motion> l,int n)
    {
        ArrayList<Double> time=new  ArrayList<Double>();
        ArrayList<Double> vd=new  ArrayList<Double>();
        ArrayList<Double> a=new  ArrayList<Double>();

        for(int i=0;i<l.size()-1;i++)
        {
            time.add(i,(double)(l.get(i+1).eventTime-l.get(i).eventTime));
            vd.add(i,(double)(xyVelocity(l.get(i+1).vx,l.get(i+1).vy)- xyVelocity(l.get(i).vx,l.get(i).vy)));
        }


        for (int i=0;i<vd.size();i++)
        {
            a.add((double)(vd.get(i)/time.get(i)));
        }

//        for (int i=0;i<a.size();i++)
//        {
//           // Log.e("ACCELERATION :","valuue:"+a.get(i));
//        }


        double sum=0;
        if(a.size()<n)
            for(int i =0;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        else
        {

            for(int i =a.size()-n;i < a.size(); i++)
            {
                sum=sum+a.get(i);
            }
        }

        return sum/n;
    }








    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];
        Random random = new Random();
        for (int i = 0; i < numberOfFolds; i++)
        {
            split[0][i] = data.trainCV(numberOfFolds, i, random);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }

    public static double calculateAccuracy(FastVector predictions) {
        double correct = 0;

        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }

        return 100 * correct / predictions.size();
    }

    public static Evaluation classify(Classifier model,
                                      Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation evaluation = new Evaluation(trainingSet);

        model.buildClassifier(trainingSet);
        evaluation.evaluateModel(model, testingSet);

        return evaluation;
    }


    private  double predict( ArrayList<double[]> myValues, double[] test,String mUserModel)
    {


        Log.d("TRAININGtime","---------------- STEP3----------------------:"+System.currentTimeMillis());

        double classify=0.0;

//        for(int k =0;k<myValues.size();k++)
//        {
//          //  Log.d("MachineLearning", k+"-Instance :" +myValues.get(k)[0]+"-->" +myValues.get(k)[18]);
//        }

        FastVector attributes = new FastVector();

        attributes.add(new Attribute("a"));//1
        attributes.add(new Attribute("b"));//2
        attributes.add(new Attribute("c"));//3
        attributes.add(new Attribute("d"));//4
        attributes.add(new Attribute("e"));//5
        attributes.add(new Attribute("f"));//6
        attributes.add(new Attribute("g"));//7
        attributes.add(new Attribute("h"));//8
        attributes.add(new Attribute("i"));//9
        attributes.add(new Attribute("j"));//10
        attributes.add(new Attribute("k"));//11
        attributes.add(new Attribute("l"));//12
        attributes.add(new Attribute("m"));//13
        attributes.add(new Attribute("n"));//14
        attributes.add(new Attribute("o"));//15
        attributes.add(new Attribute("p"));//16
        attributes.add(new Attribute("q"));//17
        attributes.add(new Attribute("r"));//18
        attributes.add(new Attribute("s"));//19
        attributes.add(new Attribute("t"));//20
        attributes.add(new Attribute("u"));//21
        attributes.add(new Attribute("v"));//22
        attributes.add(new Attribute("w"));//23
        attributes.add(new Attribute("w1"));//24
        attributes.add(new Attribute("w2"));//25
        attributes.add(new Attribute("w3"));//26
        attributes.add(new Attribute("w4"));//27
        attributes.add(new Attribute("w5"));//28
        attributes.add(new Attribute("w6"));//29






        FastVector classLabels = new FastVector(2);
        classLabels.add("1.0");
        classLabels.add("0.0");
        Attribute classAtt = new Attribute("class", classLabels);

        attributes.add(classAtt);

        Instances dataRaw = new Instances("TestInstances", attributes, 0);

        Log.d("MachineLearning","Atttribute SIZE: "+dataRaw.numAttributes());

        Log.d("MachineLearning","DATA SIZE: "+myValues.get(0).length);

        Log.d("MachineLearning","TEST DATA SIZE: "+test.length);

        for(int k =0;k<myValues.size();k++)
        {
            double[] a=myValues.get(k);
            // Log.d("FEATUREDATA",k+dataRaw.size()+"--->"+"RAW DATA: "+a[0]+"--"+a[1]+"--"+a[2]+"--"+a[3]+"--"+a[4]+"--"+a[5]+"--"+a[6]+"--"+a[7]+"--"+a[8]+"--"+a[9]+"--"+a[10]+"--"+a[11]+"--"+a[12]+"-->>>>>"+a[25]);
            Instance t=new DenseInstance(1.0, a);
            dataRaw.add(t);
        }

        Log.d("TRAININGtime","---------------- STEP4----------------------:"+System.currentTimeMillis());


        //Log.d("MachineLearning","TRAIN :\n"+dataRaw);
         System.out.println("checkDataxxx"+"TRAIN :\n"+dataRaw);

         for(int k =0;k<dataRaw.size();k++)
           {

         Log.d("MachineLearning",k+"--->"+"dataRaw : "+dataRaw.get(k));
              }
//
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
        Log.d("MachineLearning","NUMBER OF CLASSES :\n"+dataRaw.numClasses());
        // Assuming z (z on lastindex) as classindex
        Log.d("TRAININGtime","---------------- STEP5----------------------:"+System.currentTimeMillis());

        try {
            // Then train or build the algorithm/model on instances (dataRaw) created above.
            // J48 mlp =   new J48();

            // Do 10-split cross validation
            Instances[][] split = crossValidationSplit(dataRaw, 10);

            // Separate split into training and testing arrays
            Instances[] trainingSplits = split[0];
            Instances[] testingSplits = split[1];


            Classifier[] models = {
                    new J48(), // a decision tree
                    new PART(),
                    new DecisionTable(),//decision table majority classifier
                    new DecisionStump(), //one-level decision tree
                    new NaiveBayes(),
                    new AdaBoostM1(),
                    new RandomForest(),
                    new LMT()
            };


            /////////////////////////-----MULTI CLASSIFIERS IN LOOP----//////////////////////////////////
            //from :https://stackoverflow.com/questions/42884571/generate-roc-curve-in-eclipseweka

/*

            for (int j = 0; j < models.length; j++)
            {
                // Collect every group of predictions for current model in a FastVector
                FastVector predictions = new FastVector();

                // For each training-testing split pair, train and test the classifier

                for (int i = 0; i < trainingSplits.length; i++)
                {
                    Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);

                    predictions.appendElements(validation.predictions());
                    System.out.println(validation.toMatrixString());
                    // Uncomment to see the summary for each training-testing pair.
                      Log.d("ALL_DATA_CLASSIFIERS",models[j].toString());
                    // generate curve
                    ThresholdCurve tc = new ThresholdCurve();
                    int classIndex = 0;
                    Instances result = tc.getCurve(validation.predictions(), classIndex);
                    Log.d("ALL_DATA_CLASSIFIERS","tPR :"+validation.truePositiveRate(classIndex));
                    Log.d("ALL_DATA_CLASSIFIERS","fNR :"+validation.falseNegativeRate(classIndex));

                }

                // Calculate overall accuracy of current classifier on all splits
                double accuracy = calculateAccuracy(predictions);

                // Print current classifier's name and accuracy in a complicated,
                // but nice-looking way.
                Log.d("ALL_DATA_CLASSIFIERS","Accuracy of " + models[j].getClass().getSimpleName() + ": "
                        + String.format("%.2f%%", accuracy)
                        + "\n---------------------------------");

            }

            //////////////////////////////////////////////////////f//////


*/

            Log.d("TRAININGtime","START ----------------------:"+System.currentTimeMillis());
            Log.e("aaasasasasasasasa:","-->"+dataRaw);
//------------------------------------------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------------
            Classifier mlp= new RandomForest();

            Evaluation eval = new Evaluation(dataRaw);
            eval.crossValidateModel(mlp, dataRaw, 3, new Random(1));
            System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));
            Log.d("FEATUREDATA","Estimated Accuracy: "+Double.toString(eval.pctCorrect()));


            //Train a new classifier
            mlp = new RandomForest();

            // Sample algorithm, go through about neural networks to use this or replace with appropriate algorithm.
            mlp.buildClassifier(dataRaw);
            Log.d("FEATUREDATA","CLASSIFIER: "+mlp);
            // Create a test instance,I think you can create testinstance without
            // classindex value but cross check in weka as I forgot about it.




//--------------------------------------------------------------------------------------------------------------------
            double[] values = test;// sample values
            DenseInstance testInstance = new DenseInstance(1.0, values);
            testInstance.setDataset(dataRaw); // To associate with instances object
            Log.d("TRAININGtime","STOP ----------------------:"+System.currentTimeMillis());


            //data----> training----->model----->test----->resul

            Log.d("MachineLearning","Writing model!!!!!"+mUserModel);

            weka.core.SerializationHelper.write(mUserModel, mlp);

            Log.d("MachineLearning","-->TEST :"+testInstance);
            Log.d("MachineLearning",mUserModel+"-->MODEL :"+mlp);


            // now you can clasify
            String result= testInstance.classAttribute().value((int)mlp.classifyInstance(testInstance));
            if(result.equals("0.0"))
                classify=1.0;
//--------------------------------------------------------------------------------------------------------------------

            Log.d("FEATUREDATA","Output:"+result);
            Log.d("FEATUREDATA","Output:"+result);

            return classify;




        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return classify;
        // return dataRaw.toString();
    }
}