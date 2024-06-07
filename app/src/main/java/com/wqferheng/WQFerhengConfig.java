package com.wqferheng;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class WQFerhengConfig extends AppCompatActivity {
	ImageView buttonOK;

	private InterstitialAd mInterstitialAd;
	private static final String TAG = "ConfigActivity";
	private Spinner spinnerTheme;

	private Spinner spinnerLanguage;

	List<CheckBox> checkBoxes	=new ArrayList<>();
	private static final String PREFS_NAME = "com.wqferheng";
	private static final String PREF_PREFIX_KEY = "GenSett_";
	LinearLayout linearAlfabe;

	private boolean isSpinnerTouched = false;

	private boolean isSpinnerLangTouched = false;
	private ImageView buttonAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBaseContext().getResources().updateConfiguration(WQFerhengActivity.Config,
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
				final Context context = WQFerhengConfig.this;
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
							WQFerhengActivity.strtheme="App";
						else if(arg2==1)
							WQFerhengActivity.strtheme="Dark";
						else if(arg2==2)
							WQFerhengActivity.strtheme="Light";
						else if(arg2==3)
							WQFerhengActivity.strtheme="System";
						str=WQFerhengActivity.strtheme;

						//if(spinnerTheme.getTag()!=null&&!spinnerTheme.getTag().toString().equalsIgnoreCase(""))

						saveUrlPref(WQFerhengConfig.this,"Theme", str);
						if (isSpinnerTouched)
						{
							WQFerhengActivity.SelectTheme();
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
						//WQFerhengActivity.languageToLoad=str;


						//if(spinnerTheme.getTag()!=null&&!spinnerTheme.getTag().toString().equalsIgnoreCase(""))

						if (isSpinnerLangTouched)
						{
							String itemselected = str;
							if (!itemselected.startsWith(currentlang)) {

								SharedPreferences.Editor editor = prefsLang
										.edit();
								if (itemselected.contains("ku-")) {
									editor.putString("Lang", "ku");
								WQFerhengActivity.	languageToLoad = "ku";
								} else if (itemselected.contains("en-")) {
									editor.putString("Lang", "en");
									WQFerhengActivity.		languageToLoad = "en";
								} else if (itemselected.contains("de-")) {
									editor.putString("Lang", "de");
									WQFerhengActivity.	languageToLoad = "de";
								} else if (itemselected.contains("tr-")) {
									WQFerhengActivity.	languageToLoad = "tr";
									editor.putString("Lang", "tr");
								}
								else if (itemselected.contains("fa-")) {
									WQFerhengActivity.		languageToLoad = "fa";
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


		AdRequest adRequest = new AdRequest.Builder().build();

		InterstitialAd.load(this,"ca-app-pub-4819188859318435/1191877649", adRequest,
				new InterstitialAdLoadCallback() {
					@Override
					public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
						// The mInterstitialAd reference will be null until
						// an ad is loaded.
						mInterstitialAd = interstitialAd;
						Log.i(TAG, "onAdLoaded");
						if (mInterstitialAd != null) {
							mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
								@Override
								public void onAdClicked() {
									// Called when a click is recorded for an ad.
									Log.d(TAG, "Ad was clicked.");
								}

								@Override
								public void onAdDismissedFullScreenContent() {
									// Called when ad is dismissed.
									// Set the ad reference to null so you don't show the ad a second time.
									Log.d(TAG, "Ad dismissed fullscreen content.");
									mInterstitialAd = null;
								}

								@Override
								public void onAdFailedToShowFullScreenContent(AdError adError) {
									// Called when ad fails to show.
									Log.e(TAG, "Ad failed to show fullscreen content.");
									mInterstitialAd = null;
								}

								@Override
								public void onAdImpression() {
									// Called when an impression is recorded for an ad.
									Log.d(TAG, "Ad recorded an impression.");
								}

								@Override
								public void onAdShowedFullScreenContent() {
									// Called when ad is shown.
									Log.d(TAG, "Ad showed fullscreen content.");
								}
							});
							mInterstitialAd.show(WQFerhengConfig.this);
						} else {
							Log.d("TAG", "The interstitial ad wasn't ready yet.");
						}
					}

					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						// Handle the error
						Log.d(TAG, loadAdError.toString());
						mInterstitialAd = null;
					}
				});
	}

	private void toggletheme(String sender)
	{
		setTheme(WQFerhengActivity.theme);

		if(!sender.equalsIgnoreCase(""))
			recreate();
	}

	private void findViewsByID() {
		spinnerTheme = (Spinner) findViewById(R.id.spinnerTheme);
		spinnerLanguage= (Spinner) findViewById(R.id.spinnerLang);
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
