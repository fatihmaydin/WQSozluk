package com.wqferheng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.wqferheng.GroupEntity.GroupItemEntity;

@SuppressLint("NewApi") public class AboutActivity extends AppCompatActivity {

	ExpandableListView expandableListViewdefinition;	
	ExpandableListAdapter adapter;
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "AboutActivity";
	Boolean CancelRequestedForExpand = false;
	int intervalForExpand = 200;
	long elapsed;
	int TIMEOUTForExpand = 300;
	WQFerhengQueryProvider queryProvider;
	private List<GroupEntity> mGroupCollection;
	Boolean actionBarIsEnabled = false;	
	ImageButton image_buttonOK;
	ImageButton image_buttonShare;
	
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(WQFerhengActivity.theme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		Bundle extras = getIntent().getExtras();
		queryProvider = new WQFerhengQueryProvider(this);
		String wordtoSearch = "";

		if (extras != null) {
			wordtoSearch = extras.getString("word").trim();
		}
		WQFerhengActivity.headerEndChar="{";
		WQFerhengActivity.newlinebreak
		="}";
		expandableListViewdefinition = (ExpandableListView) findViewById(R.id.expandableListViewabout);
		
		
		image_buttonOK = (ImageButton) findViewById(R.id.imageButtonOK_about);
		image_buttonShare = (ImageButton) findViewById(R.id.imageButtonShare_abo);
		
		
		image_buttonOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		image_buttonShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_SUBJECT,
						R.string.app_name);
				String sAux = "\n" + getString(R.string.Useful)
						+ "\n\n";
				sAux = sAux
						+ "https://play.google.com/store/apps/details?id=com.wqferheng \n\n";
				i.putExtra(Intent.EXTRA_TEXT, sAux);
				startActivity(Intent.createChooser(i,
						getString(R.string.select)));
				
			}
		});
		
		mGroupCollection = new ArrayList<GroupEntity>();
		adapter = new ExpandableListAdapter(this, expandableListViewdefinition,
				mGroupCollection);
		expandableListViewdefinition.setAdapter(adapter);	
		Words word = queryProvider
				.GetSingleExactWord("WQFerheng");
		String def ="";
		
		String whatsNew=getString(R.string.whatsnew);
		
		if 
		(word!=null) 
		{
			String d=word.getwate(); // wordd.get(WQFerhengDB.KEY_DEFINITION);
			
			 def +="Di derheqa "+""+ d.replace("Ferheng¿|", "Ferhengê de¿|")+"\n\n"+"DB version= "+WQFerhengDB.DATABASE_VERSION +"||";			
		}
		def+=whatsNew +"("+getString(R.string.Version46)+")"+WQFerhengActivity.headerEndChar+ getString(R.string.Version45Body);
		
		Words worddd= queryProvider
				.GetSingleExactWord("Peyvên Wergerrandî");
		
		if (worddd!=null) {		
			 def +="||"+worddd.getwate(); //wordd.get(WQFerhengDB.KEY_DEFINITION);			
		}
		

		String derbaramin=getString(R.string.DerbaraMin);
		String derbaraminheader=getString(R.string.derbaraminheader);
		def+="||"+ derbaraminheader +WQFerhengActivity.headerEndChar+derbaramin;
		def=WQFerhengActivity.Decode(def, "WQFerheng", "WQFerheng");
		
		def+= " http://fethironi.blogspot.com.tr "+"\n https://twitter.com/fethironi \n"
				+ " aydinfatih@yahoo.com  \n";		
		
		
		
		SetExpanderCollection(wordtoSearch, def);
		
		PackageInfo pInfo;
		String title="";
		try {
			pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			String version = pInfo.versionName;
			title="Derbar"+ " v"+ version;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			title="Derbar";
			
			e.printStackTrace();
		}
		
		
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
//			actionBar.show();
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			setTitle(title);
//
//			actionBarIsEnabled = true;
			actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
			// Set the icon
			actionBar.setIcon(R.drawable.wqferheng); // Your icon resource
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

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
								mInterstitialAd.show(AboutActivity.this);
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
	private void showInfo() {
		try {
			Cursor cursor = WQFerhengDB.mWQferhengDBOpenHelper
					.getReadableDatabase().rawQuery(
							"select count(*) from FTSdictionary", null);
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			String version = pInfo.versionName;
			
			final TextView textViewmessage = new TextView(this);
			textViewmessage.setTextSize(17);
			String message = "";
			String count = "";
			if (cursor != null) {
				cursor.moveToFirst();
				count = cursor.getString(0);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.MyDialogTheme);

			TextView title = new TextView(this);
			String textx =  "App: v" + version +" / DB: v"+WQFerhengDB.DATABASE_VERSION;

			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextSize(20);
			String temam = getString((R.string.temam));
									
			title.setText(textx);
			builder.setCustomTitle(title);

			String derbar1 = "Hejmara Sernavan: " +count+"\n";
			
				message += derbar1;	
			textViewmessage.setText(message);
			textViewmessage.setMovementMethod(LinkMovementMethod.getInstance());
			textViewmessage.setPadding(10, 10, 10, 10);
			// builder.setMessage(message);
			builder.setView(textViewmessage);

			builder.setNegativeButton(temam,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
						}

					});

			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void SetExpanderCollection(String word, String string) {
		mGroupCollection.clear();

		String[] seperated = null;

		if (string.contains("||")) {
			seperated = string.split(java.util.regex.Pattern.quote("||"));
		} else {
			seperated = new String[1];
			seperated[0] = string;
		}
		for (int i = 0; i < seperated.length; i++) {
			String sep = seperated[i];
			String Name = sep;

			if (sep.contains(WQFerhengActivity.headerEndChar)) {
				Name = sep.substring(0,
						sep.indexOf(WQFerhengActivity.headerEndChar));
				sep = sep.substring(sep
						.indexOf(WQFerhengActivity.headerEndChar));
				
			}
			String regex = "[0-9]+\\.";
			if (sep.matches(regex)) {
				String[] splitted = sep.split(regex);
				for (int k = 0; k < splitted.length; k++) {
					String subsplitted = splitted[k];
					if (splitted.length > 0) {
						GroupEntity ge = InsertGroupEntity(subsplitted, Name
								+ (k + 1));
						ge.Body=sep;
						mGroupCollection.add(ge);
					}
				}

			} else {

				GroupEntity ge = InsertGroupEntity(sep, Name);
				ge.Body=sep;
				mGroupCollection.add(ge);
			}

		}
		adapter = new ExpandableListAdapter(this, expandableListViewdefinition,
				mGroupCollection);

		expandableListViewdefinition.setAdapter(adapter);

		expandableListViewdefinition.setFocusableInTouchMode(true);
		expandableListViewdefinition.requestFocus();
		ExpandWithTimer();

	}

	private GroupEntity InsertGroupEntity(String sep, String Name) {
		GroupEntity ge = new GroupEntity();
ge.Body=sep;
		if (Name.contains("$"))
			Name = Name.replaceAll(java.util.regex.Pattern.quote("$"), "");
		if (Name.startsWith("\n"))
			Name = Name.substring(1);
		if (Name.contains("\n"))
			Name = Name.split(java.util.regex.Pattern.quote("\n"))[0];
		if (Name.endsWith("\n"))
			Name = Name.substring(0, Name.length() - 1);

		if (sep.contains("|")) {
			ge.Name = Name;
			String[] separated2 = null;
			separated2 = sep.split(java.util.regex.Pattern.quote("|"));
			for (int j = 0; j < separated2.length; j++) {
				String sep2 = separated2[j];
				if (sep2.trim().equalsIgnoreCase(
						WQFerhengActivity.headerEndChar))
					continue;
				if (sep2.trim().equalsIgnoreCase("Binihêre")
						&& ge.Name.equalsIgnoreCase("Binihêre"))
					continue;
				GroupItemEntity gi = InsertGroupItemEntity(ge, sep2);

				if (!gi.Name.equalsIgnoreCase("\n") && !gi.Name.equals("")
						&& gi.Name != null)
					ge.GroupItemCollection.add(gi);
			}
		} else {
			ge.Name = Name;
			GroupItemEntity gi = InsertGroupItemEntity(ge, sep);
			if (!gi.Name.equalsIgnoreCase("\n") && !gi.Name.equals("")
					&& gi.Name != null)
				ge.GroupItemCollection.add(gi);
		}

		return ge;
	}

	private GroupItemEntity InsertGroupItemEntity(GroupEntity ge, String sep) {
		GroupItemEntity gi = ge.new GroupItemEntity();
		if (sep.startsWith(WQFerhengActivity.headerEndChar + "\n"))
			sep = sep.substring(2);
		if (sep.startsWith(WQFerhengActivity.headerEndChar))
			sep = sep.substring(1);
		if (sep.contains(WQFerhengActivity.headerEndChar))
			sep = sep.replace(WQFerhengActivity.headerEndChar, "");
		if (sep.startsWith(WQFerhengActivity.newlinebreak))
			sep = sep.substring(1);
		int c = 1;
		while (sep.contains("#")) {
			String cx = c + " .";
			sep = sep.replaceFirst(java.util.regex.Pattern.quote("#"), cx);
			c++;
		}
		if (sep.endsWith(WQFerhengActivity.newlinebreak))
			sep = sep.substring(0, sep.length() - 1);
		if (sep.contains(WQFerhengActivity.newlinebreak))
			sep = sep.replaceAll(java.util.regex.Pattern
					.quote(WQFerhengActivity.newlinebreak), "\n");

		String html = sep;
		if (ge.Name.equals("بنهێرە"))
			gi.Name = html;
		else
			gi.Name = html;
		return gi;
	}

	private void ExpandWithTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				elapsed += intervalForExpand;
				if (elapsed >= TIMEOUTForExpand) {
					this.cancel();
					ExpandListView();
					return;
				}
				ExpandListView();
				if (CancelRequestedForExpand)
					this.cancel();
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, intervalForExpand, intervalForExpand);
	}

	private void ExpandListView() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (expandableListViewdefinition.getChildCount() > 0)
					expandableListViewdefinition.expandGroup(0);
				CancelRequestedForExpand = true;
			}
		});

	}

	public void Research(String word) {
	//	ArrayList<Map<String, String>> map = queryProvider
			//	.GetSingleExactWord(word);
		Words wword = queryProvider
					.GetSingleExactWord(word);
		if (wword!=null
				//map.size() > 0
		) {
			//Map<String, String> wordd = map.get(0);
			String def = wword.getwate(); //wordd.get(WQFerhengDB.KEY_DEFINITION);
			
			SetExpanderCollection(word, def);
			setTitle(word);
		} else 
		{
			
			Toast.makeText(getBaseContext(),
					getString(R.string.resultsnotfound, word), Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_about, menu);		
		if (actionBarIsEnabled) 
		{
			try {

				MenuItem itemaaction_order = menu.findItem(R.id.action_jimar);

				itemaaction_order
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

			} catch (Exception exc) {

			}

		} else {

			MenuItem itemaaction_order = menu.findItem(R.id.action_jimar);

			itemaaction_order.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//switch (item.getItemId()) {

		if(item.getItemId()== android.R.id.home) {
			finsihActivity();
			return true;
		}
		if(item.getItemId()== R.id.action_jimar)

		{
		//case R.id.action_jimar: {
			showInfo();
			return  true;
		}
		else{
			return true;
		}
	}

	private void finsihActivity() {
		this.finish();
	}
	

}
