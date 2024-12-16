package com.wqsozluk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;

import com.facebook.ads.*;

import java.util.ArrayList;
import java.util.List;

public class WQDictionaryConfig extends AppCompatActivity {
	ImageView buttonOK;

	private com.facebook.ads.InterstitialAd interstitialAd;
	private static final String TAG = "ConfigActivity";
	private Spinner spinnerTheme;

	private Spinner spinnerLanguage;

	List<CheckBox> checkBoxes	=new ArrayList<>();
	private static final String PREFS_NAME = "com.wqsozluk";
	private static final String PREF_PREFIX_KEY = "GenSett_";
	LinearLayout linearAlfabe;

	private boolean isSpinnerTouched = false;

	private boolean isSpinnerLangTouched = false;
	private ImageView buttonAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBaseContext().getResources().updateConfiguration(WQDictionaryActivity.Config,
				getBaseContext().getResources().getDisplayMetrics());
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
				final Context context = WQDictionaryConfig.this;
				Intent resultValue = new Intent();
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});
		List<String> Themes=new ArrayList<String>();
		Themes.add("App");
		Themes.add(getResources().getString(R.string.strDark));
		Themes.add(getResources().getString(R.string.strLight));
		Themes.add(getResources().getString(R.string.strSysem));
		//Themes.add("Classic");
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
						String str = "App";
						if(arg2==0)
							WQDictionaryActivity.strtheme="App";
						else if(arg2==1)
							WQDictionaryActivity.strtheme="Dark";
						else if(arg2==2)
							WQDictionaryActivity.strtheme="Light";
						else if(arg2==3)
							WQDictionaryActivity.strtheme="System";
						str=WQDictionaryActivity.strtheme;

						//if(spinnerTheme.getTag()!=null&&!spinnerTheme.getTag().toString().equalsIgnoreCase(""))

						saveUrlPref(WQDictionaryConfig.this,"Theme", str);
						if (isSpinnerTouched)
						{
							WQDictionaryActivity.SelectTheme();
							toggletheme("spinner");
						}
					}

				});

			if(pref.equalsIgnoreCase("app")) {
				spinnerTheme.setSelection(0);
			}
			else if(pref.equalsIgnoreCase("dark")) {
				spinnerTheme.setSelection(1);
			}
		else if(pref.equalsIgnoreCase("light")) {
			spinnerTheme.setSelection(2);
		}
		else if(pref.equalsIgnoreCase("system")) {
				spinnerTheme.setSelection(3);
			}
		else spinnerTheme.setSelection(0);

		List<String> items2=new ArrayList<String>();

		items2.add( "ku-" +getResources().getString(R.string.Langku));
		items2.add( "en-" +getResources().getString(R.string.Langen));
		items2.add( "de-" + getResources().getString(R.string.Langde));
		items2.add( "tr-" + getResources().getString(R.string.Langtr));
		items2.add( "fa-" + getResources().getString(R.string.Langfa));

		ArrayAdapter<String> spinnerArrayAdapterLang = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				items2);
		spinnerLanguage.setAdapter(spinnerArrayAdapterLang);
		String preflang= loadUrlPref(this.getBaseContext(),
				"Theme");
		final SharedPreferences prefsLang = getBaseContext()
				.getSharedPreferences("Lang", 0);
		final String currentlang = prefsLang.getString("Lang", "ku") + "-";
		// makeText(currentlang);
		int selectedItemLangIndex = 0;
		for (int xx = 0; xx < items2.size(); xx++) {
			if (items2.get(xx).contains(currentlang)) {
				selectedItemLangIndex = xx;
				spinnerLanguage.setSelection(xx);
				break;
			}

		}
		spinnerLanguage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isSpinnerLangTouched = true;
				return false;
			}
		});
		spinnerLanguage
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
						//WQDictionaryActivity.languageToLoad=str;


						//if(spinnerTheme.getTag()!=null&&!spinnerTheme.getTag().toString().equalsIgnoreCase(""))

						if (isSpinnerLangTouched)
						{
							String itemselected = str;
							if (!itemselected.startsWith(currentlang)) {

								SharedPreferences.Editor editor = prefsLang
										.edit();
								if (itemselected.contains("ku-")) {
									editor.putString("Lang", "ku");
								WQDictionaryActivity.	languageToLoad = "ku";
								} else if (itemselected.contains("en-")) {
									editor.putString("Lang", "en");
									WQDictionaryActivity.		languageToLoad = "en";
								} else if (itemselected.contains("de-")) {
									editor.putString("Lang", "de");
									WQDictionaryActivity.	languageToLoad = "de";
								} else if (itemselected.contains("tr-")) {
									WQDictionaryActivity.	languageToLoad = "tr";
									editor.putString("Lang", "tr");
								}
								else if (itemselected.contains("fa-")) {
									WQDictionaryActivity.		languageToLoad = "fa";
									editor.putString("Lang", "fa");
								}
								editor.commit();

								if (itemselected.contains("-"))
									itemselected = itemselected
											.substring(itemselected
													.indexOf("-"));
							}
						}
					}

				});

		buttonAbout.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

					Intent intent = new Intent(getBaseContext(), AboutActivity.class);
					startActivity(intent);
					// TODO Auto-generated method stub


			}
		});

		// Create interstitial ad
		interstitialAd = new com.facebook.ads.InterstitialAd(WQDictionaryActivity.mContext, "1281695262866543_1281709846198418");
		InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
			@Override
			public void onError(Ad ad, AdError adError) {
				Log.e(TAG, "Interstitial ad onError."+adError.getErrorMessage());
			}

			@Override
			public void onAdLoaded(Ad ad) {
				interstitialAd.show();
				Log.e(TAG, "Interstitial ad onAdLoaded.");
			}

			@Override
			public void onAdClicked(Ad ad) {
				Log.e(TAG, "Interstitial ad onAdClicked.");
			}

			@Override
			public void onLoggingImpression(Ad ad) {
				Log.e(TAG, "Interstitial ad onLoggingImpression.");
			}

			@Override
			public void onInterstitialDisplayed(Ad ad) {
				Log.e(TAG, "Interstitial ad dismissed.");
			}

			@Override
			public void onInterstitialDismissed(Ad ad) {
				Log.e(TAG, "Interstitial ad dismissed.");
			}
		};

		// For auto play video ads, it's recommended to load the ad
		// at least 30 seconds before it is shown
		interstitialAd.loadAd(
				interstitialAd.buildLoadAdConfig()
						.withAdListener(interstitialAdListener)
						.build());
	}

	private void toggletheme(String sender)
	{
		setTheme(WQDictionaryActivity.theme);

		if(!sender.equalsIgnoreCase(""))
			recreate();
	}

	private void findViewsByID() {
		spinnerTheme = (Spinner) findViewById(R.id.spinnerTheme);
		spinnerLanguage= (Spinner) findViewById(R.id.spinnerLang);
		final Context context = WQDictionaryConfig.this;

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
				if(pref==null||pref.equalsIgnoreCase( "")|| pref.equalsIgnoreCase("true"))
					check.setChecked(true);
				else if(pref.equalsIgnoreCase( "false"))
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
						WQDictionaryActivity.ShowSpecialKeys=ch.isChecked();
					else if (tag.equalsIgnoreCase("HeaderTranslation"))
						WQDictionaryActivity.ShowHeaderTranslation=ch.isChecked();
					else if (tag.equalsIgnoreCase("LangTranslation"))
						WQDictionaryActivity.ShowLanguageTranslation=ch.isChecked();
				}
			});
		}
		linearAlfabe = (LinearLayout) findViewById(R.id.linearAlfabe);

		buttonOK = (ImageView) findViewById(R.id.buttonOK);
		buttonAbout = (ImageView) findViewById(R.id.buttonAbout);

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
		prefs.putString(PREF_PREFIX_KEY + "WQDictionary" + key, value);
		prefs.commit();
		Toast.makeText(context, key
		+":"+value, Toast.LENGTH_LONG);
	}

	public static String loadUrlPref(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String value = prefs.getString(PREF_PREFIX_KEY + "WQDictionary" + key,
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
