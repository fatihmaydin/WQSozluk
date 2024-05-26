package com.wqferheng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WQFerhengConfig extends AppCompatActivity {
	ImageView buttonOK;

	private Spinner spinnerTheme;

	List<CheckBox> checkBoxes	=new ArrayList<>();
	private static final String PREFS_NAME = "com.wqferheng";
	private static final String PREF_PREFIX_KEY = "GenSett_";
	LinearLayout linearAlfabe;

	private boolean isSpinnerTouched = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toggletheme("");
		setContentView(R.layout.configuration);
		setResult(RESULT_CANCELED);
		androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
		if (actionBar != null) {
			// Set the display options
			actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
			// Set the icon
			actionBar.setIcon(R.drawable.settingss); // Your icon resource
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();

		findViewsByID();
		buttonOK.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				final Context context = WQFerhengConfig.this;
				Intent resultValue = new Intent();
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});
		List<String> Themes=new ArrayList<String>();
		Themes.add("App");
		Themes.add("Dark");
		Themes.add("Light");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				Themes);
		spinnerTheme.setAdapter(spinnerArrayAdapter);
		String pref= loadUrlPref(this.getBaseContext(),
				"Theme");


		spinnerTheme.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isSpinnerTouched = true;
				return false;
			}
		});
		spinnerTheme
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}


					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int arg2, long arg3) {
						// TODO Auto-generated method stub
						String str = arg0.getItemAtPosition(arg2).toString();
						WQFerhengActivity.strtheme=str;


						//if(spinnerTheme.getTag()!=null&&!spinnerTheme.getTag().toString().equalsIgnoreCase(""))

						saveUrlPref(WQFerhengConfig.this,"Theme", str+"");
						if (isSpinnerTouched)
						{
							WQFerhengActivity.SelectTheme();
							toggletheme("spinner");
						}
					}

				});
		for (int position = 0; position < spinnerArrayAdapter.getCount(); position++) {
			if(spinnerArrayAdapter.getItem(position).equalsIgnoreCase(pref)) {
				spinnerTheme.setSelection(position);
			}
		}

	}

	private void toggletheme(String sender)
	{
		setTheme(WQFerhengActivity.theme);

		if(!sender.equalsIgnoreCase(""))
			recreate();
	}

	private void findViewsByID() {
		spinnerTheme = (Spinner) findViewById(R.id.spinnerTheme);
		final Context context = WQFerhengConfig.this;

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		checkBoxes=	 	getViewsByTag(mainLayout);
		for (int i=0; i<checkBoxes.size(); i++)
		{
			CheckBox check=checkBoxes.get(i);
			String tag= "";
			if(check.getTag()!=null)
			{
				tag=check.getTag().toString();
				String pref= loadUrlPref(this.getBaseContext(),
					 tag);
				if(pref==null||pref.equalsIgnoreCase( "")||pref.toLowerCase().equalsIgnoreCase("true"))
					check.setChecked(true);
				else if(pref.toLowerCase().equalsIgnoreCase( "false"))
				{
					check.setChecked(false);
				}
			}
			check.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				CheckBox ch=(CheckBox) v;
					String tag= "";
					if(check.getTag()!=null)
					{
						tag=check.getTag().toString();
					}
					saveUrlPref(context,tag, ch.isChecked()+"");
					if (tag.equalsIgnoreCase("SpecialKeys"))
						WQFerhengActivity.ShowSpecialKeys=ch.isChecked();
					else if (tag.equalsIgnoreCase("HeaderTranslation"))
						WQFerhengActivity.ShowHeaderTranslation=ch.isChecked();
					else if (tag.equalsIgnoreCase("LangTranslation"))
						WQFerhengActivity.ShowLanguageTranslation=ch.isChecked();
				}
			});
		}
		linearAlfabe = (LinearLayout) findViewById(R.id.linearAlfabe);
	
		buttonOK = (ImageView) findViewById(R.id.buttonOK);
	}
	private static ArrayList<CheckBox> getViewsByTag(ViewGroup root){
		ArrayList<CheckBox> views = new ArrayList<CheckBox>();
		final int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = root.getChildAt(i);
			if (child instanceof ViewGroup) {
				views.addAll(getViewsByTag((ViewGroup) child));
			}

			if (child instanceof CheckBox) {
				views.add((CheckBox) child);
			}
		}
		return views;
	}


	// Write the prefix to the SharedPreferences object for this widget
	static void saveUrlPref(Context context, String key,
			String value) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putString(PREF_PREFIX_KEY + "wqferheng" + key, value);
		prefs.commit();
		Toast.makeText(context, key
		+":"+value, Toast.LENGTH_LONG);
	}

	public static String loadUrlPref(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String value = prefs.getString(PREF_PREFIX_KEY + "wqferheng" + key,
				null);
		//Toast.makeText(context, key
		//		+":"+value, Toast.LENGTH_LONG);
		if (value != null) {
			return value;
		} else {
			return "";
		}
	}
	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
