package nl.mprog.projects.NPuzzle10447768;

import java.util.Random;
import java.util.StringTokenizer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.view.MenuItem;

@SuppressLint("NewApi") public class TheGame extends Activity {
	// Set the id's of the images
	public Integer[] idsOfImages = { R.drawable.brooklyn_bridge,
			R.drawable.diggrini_island, R.drawable.manhattan_skyline,
			R.drawable.qatar_skyline, R.drawable.rush_hour,
			R.drawable.singapore_city, R.drawable.lake, R.drawable.space,
			R.drawable.waterfall, R.drawable.real_place };
	
	// Create the variables
    public static final String Difficulty = "difficulty",  
    		NumberOfImage = "numberofimage", Condition = "condition",
    		Sequence = "sequence", Preferences = "preferences" ;
    SharedPreferences settings;

    public int reqHeight = 0, reqWidth = 0, condition = 0, 
    		restart = 0, theTimer = 0, rowEmptyBox = 0, 
    		columnEmptyBox = 0, difficulty = 0, numberOfImage = 0,
    		boxesInRow = 0;
    

    public ImageView emptyBox;
    public int[][] conditionOfBox  = new int[2][50];

    // Create all possible tiles
    public int[][] idOfBoxes = {
            {R.id.r1e1, R.id.r1e2, R.id.r1e3, R.id.r1e4, R.id.r1e5},
            {R.id.r2e1, R.id.r2e2, R.id.r2e3, R.id.r2e4, R.id.r2e5},
            {R.id.r3e1, R.id.r3e2, R.id.r3e3, R.id.r3e4, R.id.r3e5},
            {R.id.r4e1, R.id.r4e2, R.id.r4e3, R.id.r4e4, R.id.r4e5},
            {R.id.r5e1, R.id.r5e2, R.id.r5e3, R.id.r5e4, R.id.r5e5}
        };
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        
        // Set the size of the game
        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.8);

        // Get the settings chosen by the user
        numberOfImage = getIntent().getExtras().getInt("imageID");
        difficulty = getIntent().getExtras().getInt("difficulty");
        restart = getIntent().getExtras().getInt("restart");
        settings = getSharedPreferences(Preferences, 0);
        condition = settings.getInt(Condition,0);

        boxesInRow = difficulty + 3;
        
        Bitmap bitmapImage = makeBitmap();
        
        // Scaling the bitmap
        Bitmap scaleBitmapImage = scalingBitmap(bitmapImage);

        makeMenu();

        // When there was an earlier condition
        if(condition == 1)
        {
            // Fill boxes with earlier condition
            String oldString = settings.getString(Sequence, "");
            StringTokenizer strWithoutComma = new StringTokenizer(oldString, ",");
            if(!oldString.isEmpty() && oldString != null) {
                for (int pos = 0; pos <= 49; pos++) {
                    conditionOfBox[1][pos] = Integer.parseInt(strWithoutComma.nextToken());
                    conditionOfBox[0][pos] = Integer.parseInt(strWithoutComma.nextToken());
                }
            }
        }
        
        // Making the boxes
        createBitmapBoxes(scaleBitmapImage, difficulty);
        theTimer = 0;
        settings = getSharedPreferences(Preferences, 0);
        condition = settings.getInt(Condition,0);
        if(condition == 0) {
            makeGame();
        }
        else {
            theTimer = 1;
        }
    }

    public void makeGame() {
    	// Show solution, then hussle
    	emptyBox = (ImageView) findViewById(idOfBoxes[0][0]);
        Runnable sol = new Runnable() {
            @Override
            public void run() {
                hussleBoxes();
                theTimer = 1;
            }
        };
        Handler theHussle = new Handler();
        theHussle.postDelayed(sol, 5000);
    }
    
    public void hussleBoxes() {
        Random random = new Random();
        // Make sure that every box has a random position
        for (int numOfRandoms = 0; numOfRandoms < 3000; numOfRandoms++) {
        	int randomFir = random.nextInt(boxesInRow);
        	int randomSec = random.nextInt(boxesInRow);
            ImageView box;
            box = (ImageView) findViewById(idOfBoxes[randomFir][randomSec]);
            moveBox(box);
        }
    }

    public Bitmap makeBitmap() {
    	Bitmap bitmapImage = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
            
        try {
            bitmapImage = BitmapFactory.decodeResource(getResources(), idsOfImages[numberOfImage]);
        }
        catch(OutOfMemoryError e) {
            options.inSampleSize = 1;
            try {
                bitmapImage = BitmapFactory.decodeResource(getResources(), idsOfImages[numberOfImage], options); 
            }
            catch(OutOfMemoryError e2) {
                options.inSampleSize *= 2;
                try {
                    bitmapImage = BitmapFactory.decodeResource(getResources(), idsOfImages[numberOfImage], options);   
                }
                catch(OutOfMemoryError e3) {
                        options.inSampleSize *= 2;
                        bitmapImage = BitmapFactory.decodeResource(getResources(), idsOfImages[numberOfImage], options);   
                }
            }
        }
        return bitmapImage;
    }


    public Bitmap scalingBitmap(Bitmap bitmap) {
        int  heightBitmap = bitmap.getHeight(), widthBitmap = bitmap.getWidth();
        float scaledWidth;
        float scaledHeight;
        float scaleBitmapWidth = (float) reqWidth / widthBitmap;
        float scaleBitmapHeight = (float) reqHeight / heightBitmap;

        // Check whether width or height is larger
        if(scaleBitmapWidth < scaleBitmapHeight) {
        	scaledWidth = scaleBitmapHeight * widthBitmap;
            scaledHeight = scaleBitmapHeight * heightBitmap;} 
        else {
        	scaledWidth = scaleBitmapWidth * widthBitmap;
            scaledHeight = scaleBitmapWidth * heightBitmap;}

        // Get begin and end y and x of rectangle
        float beginY = (reqHeight - scaledHeight) / 2;
        float beginX = (reqWidth - scaledWidth) / 2;
        float endY = beginY + scaledHeight;
        float endX = beginX + scaledWidth;

        // Make rectangle and Bitmap
        RectF rectangle = new RectF(beginX, beginY, endX, endY);
        Bitmap scaledBitmap = Bitmap.createBitmap(reqWidth, reqHeight, bitmap.getConfig());
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(bitmap, null, rectangle, null);

        return scaledBitmap;
    }

    
    public void createBitmapBoxes(Bitmap bitmap, int dif) {
        int boxHeight = (int)(bitmap.getHeight()/boxesInRow), boxWidth = (int)(bitmap.getWidth()/boxesInRow);
     // Create upper, middle and under rows
        if(dif == 0) {
        	// Three rows for easy
            final Bitmap [][] boxesBitmap = {
                {Bitmap.createBitmap(bitmap, 1,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,1,boxWidth,boxHeight)},
                
                {Bitmap.createBitmap(bitmap, 1,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight,boxWidth,boxHeight)},

                {Bitmap.createBitmap(bitmap, 1,boxHeight+boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight+boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight+boxHeight,boxWidth,boxHeight)}
                };
            createBoxes(boxesBitmap);
        }
        else if(dif == 1) {
        	// Four rows for normal
            Bitmap [][] boxesBitmap = {
                {Bitmap.createBitmap(bitmap, 1,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,1,boxWidth,boxHeight)},
                
                {Bitmap.createBitmap(bitmap, 1,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight,boxWidth,boxHeight)},
                
                {Bitmap.createBitmap(bitmap, 1,boxHeight+boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight+boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight+boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight*2,boxWidth,boxHeight)},
                
                {Bitmap.createBitmap(bitmap, 1,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight*3,boxWidth,boxHeight)}  
                };
            createBoxes(boxesBitmap);
        }
        else {
        	// Five rows for hard
            Bitmap [][] boxesBitmap = {
                {Bitmap.createBitmap(bitmap, 1,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,1,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*4,1,boxWidth,boxHeight)},

                {Bitmap.createBitmap(bitmap, 1,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*4,boxHeight,boxWidth,boxHeight)},

                {Bitmap.createBitmap(bitmap, 1,boxHeight*2,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight*2,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight*2,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight*2,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*4,boxHeight*2,boxWidth,boxHeight)},

                {Bitmap.createBitmap(bitmap, 1,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight*3,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*4,boxHeight*3,boxWidth,boxHeight)},

                {Bitmap.createBitmap(bitmap, 1,boxHeight*4,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth,boxHeight*4,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*2,boxHeight*4,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*3,boxHeight*4,boxWidth,boxHeight),
                Bitmap.createBitmap(bitmap, boxWidth*4,boxHeight*4,boxWidth,boxHeight)}
                };
            createBoxes(boxesBitmap);
        }
        
    }

    
    
    
    public void createBoxes(Bitmap[][] bitmap) {
        int countOfCond = 0, xCondition = 0, yCondition = 0,
        		emptyBoxesOfset = 6;
        final Bitmap[][] bitmapTiles = bitmap;
        // Every position has to be made
        for(int rowWidth = 0; rowWidth < boxesInRow; rowWidth++) {
            for(int rowHeight = 0; rowHeight < boxesInRow; rowHeight++) {
            	// Make the imageview of the box
                ImageView boxImage;
                boxImage = (ImageView) findViewById(idOfBoxes[rowWidth][rowHeight]);
                if(condition == 0) {
                    if(rowHeight == 0 && rowWidth == 0 ) {
                    	boxImage.setTag(bitmapTiles[rowWidth][rowHeight]);
                        boxImage.setImageBitmap(bitmapTiles[rowWidth][rowHeight]);
                        boxImage.setVisibility(View.INVISIBLE);
                    }
                    else {
                    	boxImage.setTag(bitmapTiles[rowWidth][rowHeight]);
                        boxImage.setImageBitmap(bitmapTiles[rowWidth][rowHeight]);
                    }
                }
                else {
                    yCondition = conditionOfBox[1][countOfCond];
                    xCondition = conditionOfBox[0][countOfCond];
                    countOfCond++;
                    if(xCondition < 0) {
                        boxImage.setImageBitmap(bitmapTiles[xCondition+emptyBoxesOfset][yCondition+emptyBoxesOfset]);
                        boxImage.setTag(bitmapTiles[xCondition+emptyBoxesOfset][yCondition+emptyBoxesOfset]);
                        emptyBox = (ImageView) findViewById(idOfBoxes[rowWidth][rowHeight]);
                        columnEmptyBox = rowHeight;
                        rowEmptyBox = rowWidth;
                        boxImage.setVisibility(View.INVISIBLE);
                    }
                    else {
                    	boxImage.setTag(bitmapTiles[xCondition][yCondition]);
                        boxImage.setImageBitmap(bitmapTiles[xCondition][yCondition]);
                    }
                }
                // Make action when clicked
                boxImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(theTimer == 1) {
                            moveBox(v);
                            checkForEnd(bitmapTiles);
                        }
                    }
                });
            }
        }
    }
    
    
    
    
    public void moveBox(View view) {
        int posX = -1, posY= -1;
        // Box cannot be the empty box
        if(view.getVisibility() == View.VISIBLE) {
            // Get coordinate by id's
            for(int x = 0; x < boxesInRow; x++) {
                for(int y = 0; y < boxesInRow; y++) {
                    if(view.getId()==idOfBoxes[x][y]) {
                        posX = x;
                        posY = y;
                    }
                }
            }
            // Check for empty neighbor box
            if(posX == rowEmptyBox && posY+1 == columnEmptyBox ||
            		posX+1 == rowEmptyBox && posY == columnEmptyBox ||
            		posY ==columnEmptyBox && posX-1 == rowEmptyBox  ||
                    posX == rowEmptyBox && posY-1 == columnEmptyBox ) {
            	// Swap the boxes
            	rowEmptyBox = posX;
                columnEmptyBox = posY;
                ImageView imageClicked = (ImageView) findViewById(idOfBoxes[posX][posY]);
                imageClicked.setVisibility(View.INVISIBLE);
                Bitmap bitmap = ((BitmapDrawable)imageClicked.getDrawable()).getBitmap();
                emptyBox.setImageBitmap(bitmap);
                emptyBox.setVisibility(View.VISIBLE);
                emptyBox = imageClicked;
            }
        }
    }


    
    
    public void checkForEnd(Bitmap [][] bitmapArray) {
        int totalBoxes = boxesInRow*boxesInRow, rightBoxes = 0,
        		countBox = 0, emptyBoxOfset = 6;
        for(int x1 = 0; x1 < boxesInRow; x1++) {
            for(int y1 = 0; y1 < boxesInRow; y1++) {
                ImageView checkView = (ImageView) findViewById(idOfBoxes[x1][y1]);
                Bitmap bitOriginal = bitmapArray[x1][y1];
                Bitmap bitView = ((BitmapDrawable)checkView.getDrawable()).getBitmap();
                // Check for the same box as box in solution
                if(bitView.sameAs(bitOriginal)) {
                    rightBoxes++;
                }
                for(int x2 = 0; x2 < boxesInRow; x2++) {
                    for(int y2 = 0; y2 < boxesInRow; y2++) {
                        bitOriginal = bitmapArray[x2][y2];
                        if(bitView.sameAs(bitOriginal)) {
                            conditionOfBox[0][countBox]= x2;
                            conditionOfBox[1][countBox] = y2;
                        }
                        // When empty box
                        if(checkView.getVisibility() == View.INVISIBLE) {
                            conditionOfBox[0][countBox]= x2-emptyBoxOfset;
                            conditionOfBox[1][countBox] = y2-emptyBoxOfset;
                        }
                    }
                }
                countBox++;
            }
        }
        // If all the boxes are right, then alert
        if(rightBoxes == (totalBoxes-1)) {
            AlertDialog.Builder ending = new AlertDialog.Builder(this);
            ending.setMessage(R.string.endgame_text);
            ending.setTitle(R.string.endgame_title);
            // Start a new game
            ending.setPositiveButton(R.string.endgame_button, 
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int num) {
                    restart = 1;
                    settings = getSharedPreferences(Preferences, 0);
                    Editor editor = settings.edit();
                    editor.clear();
                    editor.putInt(Condition, 0);
                    editor.commit();
                    Intent intent = new Intent(TheGame.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog theEnd = ending.create();
            theEnd.show();
        }
    }    

    public void makeMenu() {
        final Button menuButton = (Button) findViewById(R.id.buttonMenu);  
        menuButton.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                if(theTimer == 1) {
                    PopupMenu choice = new PopupMenu(TheGame.this, menuButton);
                    // Different menu for different difficulties
                    if (difficulty == 0) {
                    	choice.getMenuInflater().inflate(R.menu.menu2, choice.getMenu());
                    }
                    else if (difficulty == 1) {
                    	choice.getMenuInflater().inflate(R.menu.menu1, choice.getMenu());
                    }
                    else {
                    	choice.getMenuInflater().inflate(R.menu.menu3, choice.getMenu());
                    }
                    choice.show();  
                }
            }  
        });
    }



      
    public void easier(MenuItem mItem)  {
    	// Restart the game
        restart = 1;
        settings = getSharedPreferences(Preferences, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(Condition, 0);
        editor.commit();
        // Set difficulty one step easier
        difficulty -= 1;
        // Make new intent with new difficulty
        Intent intent = new Intent(this, TheGame.class);
        intent.putExtra("imageID", numberOfImage);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    
    

    public void harder(MenuItem item) {
    	// Restart the game
        restart = 1;
        settings = getSharedPreferences(Preferences, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(Condition, 0);
        editor.commit();
        // Set difficulty one step harder
        difficulty += 1;
        // Make new intent with new difficulty
        Intent intent = new Intent(this, TheGame.class);
        intent.putExtra("imageID", numberOfImage);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
    
    public void start(MenuItem mItem) {
        restart = 1;
        settings = getSharedPreferences(Preferences, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(Condition, 0);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    
    public void restart(View v) {
        restart = 1;
        settings = getSharedPreferences(Preferences, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(Condition, 0);
        editor.commit();
        // Send the same settings
        Intent intent = new Intent(this, TheGame.class);
        intent.putExtra("imageID", numberOfImage);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        settings = getSharedPreferences(Preferences, 0);
        // First clean the edit
        Editor edit = settings.edit();
        edit.clear();
        if(restart == 0) {
        	// Save the settings
        	String savedString = "";
            edit.putInt(Difficulty, difficulty);
            edit.putInt(NumberOfImage, numberOfImage);
            edit.putInt(Condition, 1);

            StringBuilder saveString = new StringBuilder();
            
            // Save the positions of the boxes
            for (int pos = 0; pos < 50; pos++) {
                saveString.append(conditionOfBox[0][pos]).append(",");
                saveString.append(conditionOfBox[1][pos]).append(",");
                savedString = saveString.toString();
            }
            edit.putString(Sequence, savedString);
            edit.commit();
        }
    }
}

