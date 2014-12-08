package nl.mprog.projects.NPuzzle10447768;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.RadioButton;

@SuppressLint("NewApi")
public class MainActivity extends Activity 
{
    public static final String State = "state";
    public static final String Difficulty = "difficulty";
    public static final String ImageNumber = "imagenumber";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences settings;

    //ids van puzzle opslaan
    public Integer[] imageIds =
        {
            R.drawable.brooklyn_bridge,
            R.drawable.diggrini_island,
            R.drawable.manhattan_skyline,
            R.drawable.qatar_skyline,
            R.drawable.rush_hour,
            R.drawable.singapore_city
        };
    
    public int difficulty = 1, imagenumber = 0;
    public int reqHeight = 0, reqWidth = 0;


    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //make the gallery
        GridView theGallery = (GridView)findViewById(R.id.theGallery);
        theGallery.setAdapter(new ImageAdapter(this));
        
//        theGallery.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                TextView tv = (TextView) findViewById(R.id.textAfbeeldingNr);
//                tv.setText("Gekozen Afbeelding: " + (i+1));
//                imagenumber = i;
//            }
//        }
        
//        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.4);
//        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.4);
        //vul gallery met imgs
//        for(int i=0;i<imageIds.length;i++)
//        {
//            theGallery.addView(addToGallery(imageIds[i], i));
//        }

        //maak seekbar
//        initSeek((SeekBar) findViewById(R.id.sliderMoeilijkheid));


    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(imageIds[position]);
            return imageView;
        }
    }
    
    
    @Override
    protected void onResume()
    {
        super.onResume();

        settings = getSharedPreferences(MyPREFERENCES, 0);

        // Necessary to clear first if we save preferences onPause. 
        int state = settings.getInt(State,0);
        int stateDifficulty = settings.getInt(Difficulty, 0);
        int stateImgnr = settings.getInt(ImageNumber, 0);

        if(state == 1)
        { 
            Intent intent = new Intent(this, TheGame.class);
            //stuur id van img mee en moeilijkheid
            intent.putExtra("imagebm", stateImgnr);
            intent.putExtra("difficulty", stateDifficulty);
            startActivity(intent);
            finish();
        }


    }

    
    
    //gallery
    public View addToGallery(Integer imageId, final Integer i)
    {
        //maak bitmap
        Bitmap bm = null, bm2 = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //vul bitmap
        try
        {
            bm = BitmapFactory.decodeResource(getResources(), imageId);
        }
        catch(OutOfMemoryError e)
        {
            options.inSampleSize = 2;
            try
            {
                bm = BitmapFactory.decodeResource(getResources(), imageId, options); 
            }
            catch(OutOfMemoryError e2)
            {
                options.inSampleSize *= 2;
                try{
                    bm = BitmapFactory.decodeResource(getResources(), imageId, options);   
                }
                catch(OutOfMemoryError e3)
                {
                    options.inSampleSize *= 2;
                    bm = BitmapFactory.decodeResource(getResources(), imageId, options);   
                }

            }
            //Use BitmapFactory.Options with an inSampleSize >= 1, and do inSampleSize *= 2 before each retry. 
        }
        //maak bitmap kleiner anders lag
        int subsample = resizeBitmap(bm);
        bm.recycle();
        options.inSampleSize = subsample;
        
        //maak bitmap met subsample
        bm2 = BitmapFactory.decodeResource(getResources(), imageId, options); 

        //layout
        int padding = 50;
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(reqHeight+padding, reqWidth+padding));
        layout.setGravity(Gravity.CENTER);

        //set img view voor bitmap
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(reqHeight, reqWidth));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm2);

        //set onclick op afbeelding in gallery
        imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView tv = (TextView) findViewById(R.id.textAfbeeldingNr);
                tv.setText("Gekozen Afbeelding: " + (i+1));
                imagenumber = i;
            }
         });

        layout.addView(imageView);
        return layout;
    }


    
    public int resizeBitmap(Bitmap bm)
    {
        //haal height/width van img
        int bmheight = bm.getHeight(), bmwidth = bm.getWidth();
        int subsample = 1;
        //bereken subsample
        if(bmheight > reqHeight || bmwidth > reqWidth)
        {
            if(bmwidth > bmheight)
            {
                subsample = (int) (bmheight/reqHeight);   
            } else 
            {
                subsample = (int) (bmwidth/reqWidth);   
            }   
        }
        return subsample;
    }

    
    
//    //initialiseer de seekbar voor difficulty
//    public void initSeek(SeekBar seekbar)
//    {
//        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
//        {
//
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
//            {
//                difficulty = progress;
//            }
//
//            public void onStartTrackingTouch(SeekBar seekBar)
//            {
//
//            }
//
//            public void onStopTrackingTouch(SeekBar seekBar)
//            {
//                TextView tv2 = (TextView) findViewById(R.id.textMoeilijkheid);
//                if(difficulty == 0)
//                {
//                    tv2.setText("Moeilijkheid: Makkelijk");
//                }else if(difficulty == 1)
//                {
//                    tv2.setText("Moeilijkheid: Normaal");
//                }else
//                {
//                    tv2.setText("Moeilijkheid: Moeilijk");
//                }
//            }
//        });
//    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.makkelijk:
                if (checked)
                    // set easy
                	difficulty = 0;
                break;
            case R.id.normaal:
                if (checked)
                	// set normal
                	difficulty = 1;
                break;
            case R.id.moeilijk:
                if (checked)
                    // set hard
                	difficulty = 2;
                break;
        }
    }
    
    
    //naar Game
    public void toGame(View view)
    {
        Intent intent = new Intent(this, TheGame.class);
        //stuur id van img mee en moeilijkheid
        intent.putExtra("imagebm", imagenumber);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("restart", 0);
        startActivity(intent);
        finish();
    }
}