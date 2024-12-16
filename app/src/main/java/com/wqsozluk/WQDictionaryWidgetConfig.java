package com.wqsozluk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class WQDictionaryWidgetConfig extends Activity {
	Spinner spinnerZiman;
	Spinner spinnerCure;	
	Button buttonOK;
	private static final String PREFS_NAME = "com.wqsozluk";
	private static final String PREF_PREFIX_KEY = "widget_";
	LinearLayout linearAlfabe;
	public static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	public static Boolean IsCOnfiguring=true;
	WQDictionaryQueryProvider queryProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_configuration);
		setResult(RESULT_CANCELED);
		queryProvider = new WQDictionaryQueryProvider(this);
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {		
			finish();
		}
	
		Words word=queryProvider
						.GetSingleExactWord("Hemû Ziman");
		final List<String> listhemuZiman = new ArrayList<String>();
		if (word!=null) {
			
			String def =word.getwate(); //map.get(WQDictionaryDB.KEY_DEFINITION);
			def=WQDictionaryActivity.Decode(def,word.peyv, word.peyv);
			String[] zimanha = def.split(java.util.regex.Pattern.quote("|"));
			for (int x = 0; x < zimanha.length; x++) {

				String ziman = zimanha[x];
				String[] wordpieces = ziman.split(java.util.regex.Pattern
						.quote(":"));
				if (wordpieces != null && wordpieces.length > 0)
					ziman = wordpieces[0].trim();
				listhemuZiman.add(ziman);
			}
		}

		findViewsByID();
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				listhemuZiman);
		spinnerZiman.setAdapter(spinnerArrayAdapter);
		spinnerZiman
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
						WQDictionaryWidget.ziman = str;
						SetSpinnerCureyaPeyvan();
						SetAlfabeRowVisibility();

					}

				});

		WQDictionaryWidget.ziman = loadUrlPref(this.getBaseContext(),
				mAppWidgetId, "ziman");
		int currentindex = 0;
		if (!WQDictionaryWidget.ziman.equalsIgnoreCase(""))
			for (int x = 0; x < listhemuZiman.size(); x++) {
				if (listhemuZiman.get(x)
						.equalsIgnoreCase(WQDictionaryWidget.ziman)) {
					spinnerZiman.setSelection(x);
					currentindex = x;
					break;
				}
			}
		if (listhemuZiman != null && listhemuZiman.size() > 0)
			WQDictionaryWidget.ziman = listhemuZiman.get(currentindex);

		SetSpinnerCureyaPeyvan();
		buttonOK.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				final Context context = WQDictionaryWidgetConfig.this;

				Intent resultValue = new Intent();
//
//				AppWidgetManager appWidgetManager = AppWidgetManager
//						.getInstance(context);
				
				System.out.println(WQDictionaryWidget.ziman);
				saveUrlPref(context, mAppWidgetId, "ziman",
						WQDictionaryWidget.ziman);
				saveUrlPref(context, mAppWidgetId, "cûreya peyvê",
						WQDictionaryWidget.cure);

				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						mAppWidgetId);

				String configuring = extras.getString("configuring");
				if (configuring != null
						&& configuring.equalsIgnoreCase("configuring")) {
					System.out.println("configuring");
					WQDictionaryWidget.GetWord(context, mAppWidgetId);
				}

				setResult(RESULT_OK, resultValue);

				finish();
			}
		});

	}

	private void SetSpinnerCureyaPeyvan() {
		List<String> cureyenpeyvan = new ArrayList<String>();
	
		Words word= queryProvider
				.GetSingleExactWord("_lllistCûre" + WQDictionaryWidget.ziman);
		if (word==null)
			word = queryProvider.GetSingleExactWord("Cûreyên Peyvan");

		if (word!=null) {
		
			String def =word.getwate();// map.get(WQDictionaryDB.KEY_DEFINITION);
			def=WQDictionaryActivity.Decode(def,word.peyv, word.peyv);
		
			String[] cureArray = def.split(java.util.regex.Pattern.quote("|"));

			cureyenpeyvan.add("Hemû");
            Collections.addAll(cureyenpeyvan, cureArray);

		}

		ArrayAdapter<String> spinnerArrayAdapterAlfabe = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				cureyenpeyvan);

		spinnerCure.setAdapter(spinnerArrayAdapterAlfabe);
		spinnerCure
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					
					}

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String item = arg0.getItemAtPosition(arg2).toString();
						String[] wordpieces = item
								.split(java.util.regex.Pattern.quote(":"));
						if (wordpieces != null && wordpieces.length > 0)
							item = wordpieces[0].trim();
						WQDictionaryWidget.cure = item;
						}

				});

		WQDictionaryWidget.cure = loadUrlPref(this.getBaseContext(), mAppWidgetId,
				"cûreya peyvê");
		int indexof = 0;
		if (!WQDictionaryWidget.cure.equalsIgnoreCase(""))
			for (int x = 0; x < cureyenpeyvan.size(); x++) {
				if (cureyenpeyvan.get(x).equalsIgnoreCase(WQDictionaryWidget.cure)) {
					spinnerCure.setSelection(x);
					indexof = x;
					break;
				}
			}
		if (cureyenpeyvan.size() > 0)
			WQDictionaryWidget.cure = cureyenpeyvan.get(indexof);
	}

	private void findViewsByID() {
		spinnerZiman = (Spinner) findViewById(R.id.spinnerZiman);
		spinnerCure = (Spinner) findViewById(R.id.spinnerCure);

		linearAlfabe = (LinearLayout) findViewById(R.id.linearAlfabe);
	
		buttonOK = (Button) findViewById(R.id.buttonOK);
	}

	private void SetAlfabeRowVisibility() {
		if (!WQDictionaryWidget.ziman.equalsIgnoreCase("soranî")) {
			linearAlfabe.setVisibility(View.GONE);
		} else
			linearAlfabe.setVisibility(View.VISIBLE);
	}

	// Write the prefix to the SharedPreferences object for this widget
	static void saveUrlPref(Context context, int appWidgetId, String key,
			String value) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putString(PREF_PREFIX_KEY + "WQDictionary" + key, value);
		prefs.commit();
	}

	static String loadUrlPref(Context context, int appWidgetId, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String value = prefs.getString(PREF_PREFIX_KEY + "WQDictionary" + key,
				null);
		if (value != null) {
			return value;
		} else {
			return "";
		}
	}
}
