/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pankaj.lock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStroke;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vinothramss.pankaj.R;

import static java.security.AccessController.getContext;


public class CreateActivity extends Activity implements SensorEventListener {
    private static final int TRAINING_SESSION_COUNT_MAX = 10;
    private static final int TRAINING_SESSION_COUNT_MIN = 5;
    private static final float LENGTH_THRESHOLD = 120.0f;
    int count = 0;
    int singleTouch = 0;
    protected Gesture mGesture;
    protected ArrayList<Gesture> mSavedGestureList;
    protected ArrayList<GestureEvents> mEventList;
    protected GestureEvents current;
    protected GestureEvents test;
    protected View mFinishSessionButton;
    protected View mDiscardButton;
    protected View mSaveButton;
    protected View mAuthenticateButton;
    protected View mLockButton;
    public int currentSymbol;
    ArrayList<String> users;
    //
    protected SensorManager mSensorManager;
    protected Sensor mPressure;
    private TextView mTextView;
    private VelocityTracker mVelocityTracker = null;
    //
    protected GestureOverlayView mGestureOverlay;
    protected Doodle mGestureValue;
    protected String mUserName;
    protected String mActivityType;
    protected File mUserDir;
    protected File mUserFile;
    protected TextView mTrainingSessionName;
    protected TextView mAuthenticationSessionName;
    protected String intStorageDirectory;

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_gesture);
        mFinishSessionButton = findViewById(R.id.finishSession);
        mDiscardButton = findViewById(R.id.discard);
        mSaveButton = findViewById(R.id.saveGesture);
        mAuthenticateButton = findViewById(R.id.authenticateGesture);
        mTrainingSessionName = (TextView) findViewById(R.id.trainingSessionName);
        mGestureOverlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
        mFinishSessionButton.setEnabled(false);
        mDiscardButton.setEnabled(false);
        mSaveButton.setEnabled(false);
        mGestureValue = null;
        mUserName = (String) this.getIntent().getExtras().get(getString(R.string.user_name));
        mActivityType = (String) this.getIntent().getExtras().get(getString(R.string.activity_type));
        users = (ArrayList<String>) getIntent().getSerializableExtra("usersList");
        intStorageDirectory = getFilesDir().toString();
        mUserDir = new File(intStorageDirectory + "/" + getString(R.string.root_dir) + "/" + mUserName + "/");

      /*  try {
            File dataDir = new File(intStorageDirectory + "/" + getString(R.string.root_dir));
            File copyPath=null;
            if (isStoragePermissionGranted())
            {
                 copyPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ "/" + getString(R.string.root_dir));

                if (!copyPath.exists())
                {
                    copyPath.mkdir();
                }
                Log.e("fileError","from COPY PATH:"+dataDir);
                Log.e("fileError","TO COPY PATH:"+copyPath);

                copyDirectory(dataDir, copyPath);
            }


        }
        catch (IOException e)
        {
            Log.e("fileError","error while copying the data");
            e.printStackTrace();
        }

        */


        //currentSymbol = r.nextInt(3) + 2;
        currentSymbol = checkMinimum_gesture();

        Log.e("fileError", "1mUserName:" + mUserName);
        mUserFile = new File(mUserDir.getAbsolutePath() + "/" + mUserName + currentSymbol);
        Log.e("fileError", "1mUserFile:" + mUserFile);


        Log.e("fileError", "1mUserFile:" + mUserDir.getAbsolutePath());


        if (mActivityType.equals(getString(R.string.train)))
        {

            mTrainingSessionName.setText(mUserName + "'s Training Session for (" + currentSymbol + ")");
            Boolean b = mUserDir.mkdirs();
            Toast.makeText(this, mUserDir.exists() + "MAKE DIR" + b, Toast.LENGTH_SHORT).show();

        } else if (mActivityType.equals(getString(R.string.retrain)))
        {
            mTrainingSessionName.setText("Retrain " + mUserName + "'s Doodle for (" + currentSymbol + ")");

            String user_dir = "/data/user/0/com.example.vinothramss.Sher_Lock/files/gesture_users/";
            String mUserModel = user_dir + mUserName + "/" + mUserName + currentSymbol + "model";
            File mUserFileModel = new File(mUserModel);
            mUserFileModel.delete();
            Log.d("MachineLearning", "deleting previous model" + mUserModel);

            // deletePreviousSession();
        } else if (mActivityType.equals(getString(R.string.authenticate)))
        {
            Random r = new Random();
            currentSymbol = r.nextInt(3) + 2;
            Log.e("fileError", "1mUserName:" + mUserName);
            mUserFile = new File(mUserDir.getAbsolutePath() + "/" + mUserName + currentSymbol);
            Log.e("fileError", "1mUserFile:" + mUserFile);

            setContentView(R.layout.authenticate_gesture);
            mAuthenticationSessionName = (TextView) findViewById(R.id.authenticationSessionName);
            mAuthenticationSessionName.setText("Authenticate with " + mUserName + "'s Doodle for (" + currentSymbol + ")");
            mGestureOverlay = (GestureOverlayView) findViewById(R.id.authenticate_overlay);
            mAuthenticateButton = findViewById(R.id.authenticateGesture);
            mAuthenticateButton.setEnabled(false);
        } else if (mActivityType.equals(getString(R.string.lock_apps))) {
            //setContentView(R.layout.app_list);
            // mLockButton = findViewById(R.id.authenticateGesture);
            // mLockButton.setEnabled(false);
        } else {
            Log.d(CreateActivity.class.getName().toString(), "ERROR: Invalid activity type");
            return;
        }


        mGestureOverlay.addOnGestureListener(new GesturesProcessor());
        mSavedGestureList = new ArrayList<Gesture>();
        mEventList = new ArrayList<GestureEvents>();
        current = new GestureEvents();
        current.list = new ArrayList<motion>();

        test = new GestureEvents();
        test.list = new ArrayList<motion>();
        Log.d("filep", "class :" + CreateActivity.class.getName().toString());


//// trying to find pressure value
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);// Get an instance of the sensor service, and use that to get an instance of a particular sensor.


    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float p = event.values[0];
        System.out.println("Pressure" + p);
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    public  int checkMinimum_gesture( )
    {
        File f2 = new File(mUserDir.getAbsolutePath() + "/" + mUserName + 2);
        File f3 = new File(mUserDir.getAbsolutePath() + "/" + mUserName + 3);
        File f4 = new File(mUserDir.getAbsolutePath() + "/" + mUserName + 4);
        int s2=0,s3=0,s4=0;
    try {


        if (isFilePresent(f2 + "E") == true)
        {
            FileInputStream fis = new FileInputStream(new File(f2 + "E"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<GestureEvents> l2 = (ArrayList<GestureEvents>) ois.readObject();
            s2=l2.size();
            Log.d("filep", "L2 SIZE " + l2.size());
            ois.close();
        }

        if (isFilePresent(f3 + "E") == true)
        {
            FileInputStream fis = new FileInputStream(new File(f3 + "E"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<GestureEvents> l3 = (ArrayList<GestureEvents>) ois.readObject();
            s3=l3.size();
            Log.d("filep", "L3 SIZE " + l3.size());
            ois.close();
        }
        if (isFilePresent(f4 + "E") == true)
        {
            FileInputStream fis = new FileInputStream(new File(f4 + "E"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<GestureEvents> l4 = (ArrayList<GestureEvents>) ois.readObject();
            s4=l4.size();
            Log.d("filep", "L4 SIZE " + l4.size());
            ois.close();
        }


    }catch (Exception e)
    {
        e.printStackTrace();
    }
        Random r = new Random();

    if((s3==s2)&&(s3==s4))
        return (r.nextInt(3) + 2);

        else
    {
        if((s2<s3)&&(s2<s4))
            return 2;
        else if((s3<s2)&&(s3<s4))
            return 3;
        else
             return 4;


    }

    }

    private void deletePreviousSession()
    {

        Log.e("fileError","2mUserFile:"+mUserFile);
        File f0 = new File(mUserFile + "E");
        boolean d0 = f0.delete();
        Log.d("filep","\nEVENT DELETED"+d0);
        if(mUserDir != null)
        {
            for (File file : mUserDir.listFiles())
            {
                Log.d("filep","\n2 EVENT DELETED"+file.getName());
                file.delete();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (mGesture != null)
        {
            outState.putParcelable("gesture", mGesture);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        mGesture = savedInstanceState.getParcelable("gesture");
        if (mGesture != null)
        {
            final GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
            overlay.post(new Runnable() {
                public void run()
                {
                    overlay.setGesture(mGesture);
                }
            });

            mFinishSessionButton.setEnabled(true);
        }
    }

    public void onAuthenticateButtonPress(View v)
    {

        Log.e("fileError","3mUserFile:"+mUserFile);

        GestureLibrary userStore = GestureLibraries.fromFile(mUserFile);
        Log.d("filep","user Directory:"+mUserFile.getAbsolutePath());


        ArrayList<GestureEvents> mElist =   new ArrayList();
        String EventFile=mUserFile + "E";
        try {


            if (isFilePresent(EventFile) == true)
            {
                FileInputStream fis = new FileInputStream(new File(EventFile));
                ObjectInputStream ois = new ObjectInputStream(fis);
                mElist = (ArrayList<GestureEvents>) ois.readObject();
                Log.d("filep", "eevent Auth:" + mElist.size());
                ois.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userStore.load();

        ArrayList<Gesture> gesturesFromFile = new ArrayList<Gesture>();
        for (String entry : userStore.getGestureEntries())
        {
            gesturesFromFile.addAll(userStore.getGestures(entry));
        }
        Doodle prediction = new Doodle(gesturesFromFile,mElist);


        ///-----------------------------------------------------------

    /*    String data=prediction.authenticate(mGesture,test,users,mUserName);

        try {
            File dataDir = new File(intStorageDirectory + "/" + getString(R.string.root_dir));
            File copyPath=null;
            if (isStoragePermissionGranted())
            {
                copyPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ "/" + "GestureRaw");

                if (!copyPath.exists())
                {
                    copyPath.mkdir();
                }
                Log.e("fileError","from COPY PATH:"+dataDir);
                Log.e("fileError","TO COPY PATH:"+copyPath);

                String fileName = "gesture_"+mUserName+".arff";
                String textToWrite = data;
                File myFile = new File(copyPath, fileName);
                FileWriter fileWriter = new FileWriter(myFile);
                fileWriter.append(data);
                fileWriter.flush();
                fileWriter.close();

            }


        }
        catch (IOException e)
        {
            Log.e("fileError","error while copying the data");
            e.printStackTrace();
        }

*/
//------------------------------------------------------
        if (prediction.authenticate(mGesture,test,users,mUserName,currentSymbol))
        {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            mGestureOverlay.clear(false);
            mAuthenticateButton.setEnabled(false);
        }
        else
        {
            Toast.makeText(this,"Incorrect. Please try again.", Toast.LENGTH_SHORT).show();
            mGestureOverlay.clear(false);
            mAuthenticateButton.setEnabled(false);
        }
    }
    public void onSaveButtonPress(View v)
    {
        singleTouch=1;
        if (mGesture != null)
        {
            mSaveButton.setEnabled(false);
            mDiscardButton.setEnabled(false);
            final GestureLibrary store = GestureBuilderActivity.getStore();
            store.load();

            Log.e("fileError","4mUserFile:"+mUserFile);
            ArrayList<GestureEvents> mElist =   new ArrayList();
            String EventFile=mUserFile + "E";
            try {


                if (isFilePresent(EventFile) == true)
                {
                    FileInputStream fis = new FileInputStream(new File(EventFile));
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    mElist = (ArrayList<GestureEvents>) ois.readObject();
                    Log.d("filep", "eevent Auth:" + mElist.size());
                    ois.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            ArrayList<GestureStroke> strokes=mGesture.getStrokes();
            float length =mGesture.getLength();
            Path p=mGesture.toPath();
            int count =mGesture.getStrokesCount();

            Log.d("fun","\nstart-----------------------------------------------------------------");

            Log.d("fun","\nlength :"+length);
            Log.d("fun","\ncount :"+count);
            int i;
            Log.d("fun","\nSTROKES DATA ");
//            for(i=0;i<strokes.size();i++)
//            {
//                GestureStroke s=strokes.get(i);
//                Log.d("fun","->"+s);
//
//            }

            Log.d("fun","\n----------------------------------------------------------------");
            setResult(RESULT_OK);

            final String path = new File(Environment.getExternalStorageDirectory(), "gestures").getAbsolutePath();
            Log.d("filep","---> earlier data "+mSavedGestureList.size());

            boolean haveMinGesturesBeenDrawn = mSavedGestureList.size() >= TRAINING_SESSION_COUNT_MIN;
            boolean haveMaxGesturesBeenDrawn = mSavedGestureList.size() >= TRAINING_SESSION_COUNT_MAX;
            if (!haveMaxGesturesBeenDrawn)
            {
                if (!haveMinGesturesBeenDrawn)
                {
                    mSavedGestureList.add(mGesture);
                    mGestureValue = new Doodle(mSavedGestureList,mElist);
                    store.save();
                    Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                }
                else if (haveMinGesturesBeenDrawn)
                {
                    mSavedGestureList.add(mGesture);
                    mGestureValue = new Doodle(mSavedGestureList,mElist);
                    store.save();
                    Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

                    /*
                    if (mGestureValue.authenticate(mGesture))
                   {
                        mGestureValue = new Doodle(mSavedGestureList,mElist);
                        Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, getString(R.string.invalid_gesture), Toast.LENGTH_LONG).show();
                    }
                    */
                }
            }
            if (mSavedGestureList.size() >= TRAINING_SESSION_COUNT_MIN)
            {
                mFinishSessionButton.setEnabled(true);
                mFinishSessionButton.setBackgroundColor(0xffffffff);
            }
            mGestureOverlay.clear(false);
        }
        else
        {
            setResult(RESULT_CANCELED);
        }
    }
    public void onDiscardButtonPress(View v)
    {
        setResult(RESULT_CANCELED);
        mGestureOverlay.clear(false);
    }
    public boolean isFilePresent(String path)
    {
        File file = new File(path);
        return file.exists();
    }
    public void onFinishSessionButtonPress(View v)
    {
        Log.e("fileError","5mUserFile:"+mUserFile);
        GestureLibrary userStore = GestureLibraries.fromFile(mUserFile);
        Log.d("filep", "Finish user Directory:" + mUserFile.getName());
        Log.d("filep", "event list0 :" + mEventList.size());

        ///// loading events list
        FileInputStream fis;
        ArrayList<GestureEvents> mElist =   new ArrayList();
        String EventFile=mUserFile + "E";
        try {


            if(isFilePresent(EventFile)==true)
            {
                fis = new FileInputStream (new File(EventFile));
                ObjectInputStream ois = new ObjectInputStream(fis);
                mElist = (ArrayList<GestureEvents>) ois.readObject();
                Log.d("filep", "event list1 :" + mElist.size());
                ois.close();
            }


            for (GestureEvents g : mEventList)
            {
                mElist.add(g);
            }


            Log.d("filep", "event ADDING");
            FileOutputStream fos = new FileOutputStream (new File(EventFile));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Log.d("filep", "event ADDING2");
            oos.writeObject(mElist);
            Log.d("filep", "event ADDING3");
            oos.close();
            Log.d("filep", "event STORING END:"+EventFile + "-->"+isFilePresent(EventFile));

        } catch (Exception e)
        {
            Log.d("filep","EVENT Excpetions");
            e.printStackTrace();
        }

        ////

        userStore.load();
        Log.d("filep", userStore.getGestureEntries().size() + "<-");
        for (Gesture gesture : mSavedGestureList)
        {
            userStore.addGesture(gesture.toString(), gesture);
        }


        Log.d("filep",userStore.getGestureEntries().size()+"<-");

        userStore.save();


        finish();
    }


    public void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private class GesturesProcessor implements GestureOverlayView.OnGestureListener
    {
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event)
        {
            singleTouch=0;
            mSaveButton.setEnabled(false);
            mDiscardButton.setEnabled(false);
            mGesture = null;
            current = new GestureEvents();
            current.list=new ArrayList<motion>();

            test = new GestureEvents();
            test.list=new ArrayList<motion>();
        }
        public void onGesture(GestureOverlayView overlay, MotionEvent event)
        {
            count++;

            motion mymotion =new motion();
            mymotion.x=event.getX();
            mymotion.y=event.getY();
            mymotion.pressure=event.getPressure();
            mymotion.size=event.getSize();
            mymotion.eventTime=event.getEventTime();
            mymotion.downTime=event.getDownTime();
            mymotion.orientation=event.getOrientation();



            Log.e("start","1starting:"+ event.getTouchMajor()+"\t "+event.getEventTime());
            Log.e("start","2starting:"+ event.getY()+"\t "+event.getEventTime());
            Log.e("start","3starting:"+ event.getPressure()+"\t "+event.getEventTime());
            Log.e("start","4starting:"+ event.getX()+"\t "+event.getEventTime());
            Log.e("start","5starting:"+ event.toString()+"\t "+event.getEventTime());
            Log.e("start","7starting:"+ event.getAction()+"\t "+event.getEventTime());
            Log.e("start","8starting:"+ event.getActionIndex()+"\t "+event.getEventTime());
            Log.e("start","9starting:"+ event.getButtonState()+"\t "+event.getEventTime());
            Log.e("start","10starting:"+ event.getDeviceId()+"\t "+event.getEventTime());
            Log.e("start","11starting:"+ event.getDownTime()+"\t "+event.getEventTime());
            Log.e("start","12starting:"+ event.getEventTime()+"\t "+event.getEventTime());
            Log.e("start","13starting:"+ event.getEdgeFlags()+"\t "+event.getEventTime());
            Log.e("start","16starting:"+ event.getOrientation()+"\t "+event.getEventTime());
            Log.e("start","17starting:"+ event.getRawX()+"\t "+event.getEventTime());
            Log.e("start","18starting:"+ event.getSize()+"\t "+event.getOrientation());


            System.out.println("count////////////////////////////////////////////////////: "+event.getX()+"--"+event.getY());
            int index = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(index);

            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    if (mVelocityTracker == null)
                    {

                        // Retrieve a new VelocityTracker object to watch the velocity
                        // of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {

                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }

                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mVelocityTracker == null)
                    {

                        // Retrieve a new VelocityTracker object to watch the velocity
                        // of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(event);
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    mVelocityTracker.computeCurrentVelocity(1000);

                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    mymotion.vx=VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                            pointerId);
                    mymotion.vy=VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                            pointerId);

                    Log.e("start","velocity:"+ "X velocity: "
                            + VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                            pointerId)
                            + "\nY velocity: "
                            + VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                            pointerId));
                    ;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    // Return a VelocityTracker object back to be re-used by others.
                    mVelocityTracker.recycle();
                    break;
            }

            current.add(mymotion);
            test.add(mymotion);
        }


        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event)
        {
            System.out.println("count////////////////////////////////////////////////////:"+count);
            count=0;
            if (current.list.size()>0)
                mEventList.add(current);
            current = new GestureEvents();
            System.out.println("stop-----------------------------------------------------------------------------------");
            System.out.println("STOP--"+event.getPressure()+"--"+event.getSize()+"---"+event.getTouchMajor());
            mGesture = overlay.getGesture();
            ArrayList<GestureStroke> points= mGesture.getStrokes();
            System.out.println("count2:"+points.size());

            int i;

            if (mGesture.getLength() < LENGTH_THRESHOLD)
            {
                overlay.clear(false);
                mGesture = null;
            }
            else
            {
                mSaveButton.setEnabled(true);
                mDiscardButton.setEnabled(true);
                if (mAuthenticateButton != null)
                {
                    mAuthenticateButton.setEnabled(true);
                }
            }
        }
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event)
        {

        }
    }
}