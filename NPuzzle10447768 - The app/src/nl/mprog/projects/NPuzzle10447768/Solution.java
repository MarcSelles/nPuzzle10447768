package nl.mprog.projects.NPuzzle10447768;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Solution extends Activity
{
    private Thread mSplashThread;    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //layout
        setContentView(R.layout.activity_solution);
        final Solution sPlashScreen = this;   
        mSplashThread =  new Thread()
        {
            @Override
            public void run()
            {
                
                finish();
                //ga naar mainactivity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);                 
            }
        };
        mSplashThread.start();        
    }

    //voor als je de splash eerder wilt stoppen (ontouch)
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread)
            {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }    
} 
