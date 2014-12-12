package nl.mprog.projects.NPuzzle10447768;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	// Set the id's of the images
	public Integer[] idsOfImages = { R.drawable.brooklyn_bridge,
			R.drawable.diggrini_island, R.drawable.manhattan_skyline,
			R.drawable.qatar_skyline, R.drawable.rush_hour,
			R.drawable.singapore_city, R.drawable.lake, R.drawable.space,
			R.drawable.waterfall, R.drawable.real_place };

	public ArrayList<Bitmap> images;

	// Create the variables
	public static final String Difficulty = "difficulty",
			NumberOfImage = "numberofimage", Condition = "condition",
			Preferences = "preferences";
	SharedPreferences settings;

	public int imagenumber = 0, difficulty = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		images = new ArrayList<Bitmap>();
		for (Integer id : idsOfImages) {
			images.add(BitmapFactory.decodeResource(getResources(), id));
		}

		// Get the images
		GridView theGallery = (GridView) findViewById(R.id.theGallery);
		theGallery.setAdapter(new ImageAdapter(this));
		theGallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView afb = (TextView) findViewById(R.id.afbeeldingID);
				afb.setText("Afbeelding: " + (position + 1));
				imagenumber = position;
			}
		});
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return images.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			// set img view voor bitmap
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageBitmap(images.get(position));
			return imageView;
		}
	}

	// Set the difficulty by clicking on Radio Button
	public void onRadioButtonClicked(View view) {

		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
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

	@Override
	protected void onResume() {
		super.onResume();

		settings = getSharedPreferences(Preferences, 0);

		// Necessary to clear first if we save preferences onPause.
		int condition = settings.getInt(Condition, 0);
		int imageCond = settings.getInt(NumberOfImage, 0);
		int difficultyCond = settings.getInt(Difficulty, 0);

		if (condition == 1) {
			Intent intent = new Intent(this, TheGame.class);
			// Send imageID and difficulty with the new Intent
			intent.putExtra("imageID", imageCond);
			intent.putExtra("difficulty", difficultyCond);
			startActivity(intent);
			finish();
		}
	}

	// Begin the Game
	public void beginGame(View view) {
		Intent intent = new Intent(this, TheGame.class);
		// Send imageID and difficulty with the new Intent
		intent.putExtra("imageID", imagenumber);
		intent.putExtra("difficulty", difficulty);
		intent.putExtra("restart", 0);
		startActivity(intent);
		finish();
	}
}