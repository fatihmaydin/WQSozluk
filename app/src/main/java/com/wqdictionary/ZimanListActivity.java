package com.wqdictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ZimanListActivity extends AppCompatActivity {

	WQDictionaryQueryProvider queryProvider;
	Boolean actionBarIsEnabled = false;
	Activity myActivity;
	ListView listviewziman;
	List<String> listhemuZiman;
	//AdView mAdView;
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "ZimanListActivity";
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(WQDictionaryActivity.theme);
		setContentView(R.layout.zimanlist);

		queryProvider = new WQDictionaryQueryProvider(this);
		WQDictionaryDB.columntoSearch = WQDictionaryDB.KEY_WORD;
		listhemuZiman = new ArrayList<String>();
		listviewziman = (ListView) findViewById(R.id.listziman);
		myActivity = this;

			androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
				// Set the icon
				actionBar.setIcon(R.drawable.wqsozluk); // Your icon resource
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayShowHomeEnabled(true);

		}

		TimerTask task2 = new TimerTask() {

			@Override
			public void run() {
				//this.cancel();
				setListAdapter();

			}
		};
		Timer timer2 = new Timer();
		timer2.scheduleAtFixedRate(task2, 100, 5000);
		AdRequest adRequest = new AdRequest.Builder().build();

if(false)
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
							mInterstitialAd.show(ZimanListActivity.this);
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

	private void showAdmob() throws ClassNotFoundException
	{
		final AdView mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		if(adRequest!=null&&mAdView!=null)
		{
			mAdView.loadAd(adRequest);
			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					// LinearLayout adContainer = (LinearLayout) findViewById(R.id.adContainer);
					mAdView.setVisibility(View.VISIBLE);
				}
			});
		}



	}


	private void setListAdapter() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				Log.d(TAG,"%s"+String.valueOf(R.string.AllLangs));
				//	ArrayList<Map<String, String>> listmaphemuZiman = queryProvider
				//	.GetSingleExactWord("Hemû Ziman");

				Words wword = queryProvider
						.GetSingleExactWord((String) getText(R.string.AllLangs));
				if (wword != null) {
					//Map<String, String> map = listmaphemuZiman.get(0);
					String word = wword.getpeyv(); //map.get(WQDictionaryDB.KEY_WORD);
					//String def = map.get(WQDictionaryDB.KEY_DEFINITION);
					String id = wword.getid();//map.get(WQDictionaryDB.KEY_ID);
					//Log.d("idHmeu", id);
					Words ww = queryProvider.GetSingleWord(id);
					String def = WQDictionaryActivity.Decode(ww.getwate(), word, word);

					String[] zimanha = def.split(java.util.regex.Pattern
							.quote("|"));
					String hedaer = getString(R.string.header);
					for (int x = 0; x < zimanha.length; x++) {
						String item = zimanha[x];
						if (item.contains(":"))
							item += " " + hedaer;
						listhemuZiman.add(item);
					}
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						myActivity, R.layout.zimanrow,
						listhemuZiman);
				listviewziman.setAdapter(adapter);

				listviewziman.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						TextView column = (TextView) ((TextView) view);
						String word = column.getText().toString();

						Intent intent = new Intent(myActivity,
								WordTypeListActivity.class);
						String[] wordpieces = word
								.split(java.util.regex.Pattern.quote(":"));
						if (wordpieces != null && wordpieces.length > 0)
							word = wordpieces[0].trim();

						Bundle b = new Bundle();
						b.putString("ziman", word);
						intent.putExtras(b);
						myActivity.startActivity(intent);
					}
				});
				if (actionBarIsEnabled)
					setTitle("Ziman (" + listhemuZiman.size() + ")");

			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_zimanlist, menu);
		if (actionBarIsEnabled) {
			try {

				MenuItem itemaaction_order = menu.findItem(R.id.action_order);

				itemaaction_order
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


			} catch (Exception exc) {

			}

		} else {

			MenuItem itemaaction_order = menu.findItem(R.id.action_order);

			itemaaction_order.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//switch (item.getItemId()) {

		if (item.getItemId() == android.R.id.home) {
			finsihActivity();
			return true;
		}
		if (item.getItemId() == R.id.action_order) {
			//case R.id.action_order:
			Order();
			return true;
		}
		else {
			return true;
		}



}

	int pressedToOrder = 0;

	private void Order() {

		Collections.sort(listhemuZiman);
		if (pressedToOrder % 2 != 0) {
			List<String> newlist = new ArrayList<String>();
			for (int a = listhemuZiman.size() - 1; a > 0; a--) {
				newlist.add(listhemuZiman.get(a));
			}
			listhemuZiman = newlist;
		}

//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, listhemuZiman);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				myActivity, R.layout.zimanrow,
				listhemuZiman);

		listviewziman.setAdapter(adapter);

		listviewziman.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView column = (TextView) ((TextView) view);
				String word = column.getText().toString();

				Intent intent = new Intent(myActivity,
						WordTypeListActivity.class);
				
				String[] wordpieces = word.split(java.util.regex.Pattern.quote(":"));
				if(wordpieces!=null&&wordpieces.length>0)
					word=wordpieces[0].trim();

				Bundle b = new Bundle();
				b.putString("ziman", word);

				intent.putExtras(b);
				myActivity.startActivity(intent);
			}
		});
		pressedToOrder++;

	}

	private void finsihActivity() {
		this.finish();
	}

}
