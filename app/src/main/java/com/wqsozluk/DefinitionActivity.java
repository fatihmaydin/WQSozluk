package com.wqsozluk;

import static com.wqsozluk.R.id.action_Parveke_Peyv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.wqsozluk.GroupEntity.GroupItemEntity;


public class DefinitionActivity extends AppCompatActivity implements android.view.View.OnClickListener  {

	ExpandableListView expandableListViewdefinition;
    //public static SimpleCursorAdapter mAdapter;
	public int position=-1;
	TextView definitionHedaer;
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "DefinitionActivity";
	ImageButton imageButtonGoToWiki;
	ExpandableListAdapter expandableListViewadapter;
	LinearLayout linearlayout;
	RelativeLayout mainLayoutR;
	Boolean CancelRequestedForExpand = false;
	Boolean IsButtonsVisible = true;
	static int counter=0;
	int intervalForExpand = 100;
	long elapsed;
	int touchcount=0;
	int TIMEOUTForExpand = 700;
	WQDictionaryQueryProvider queryProvider;
	private List<GroupEntity> mGroupCollection;
	Boolean actionBarIsEnabled = false;
	String wordtoSearch = "";
	public OnSwipeTouchListener onSwipeListener;
	RelativeLayout relativeLayoutlistButtons;
	ImageButton imageButtonlistBack;
	ImageButton imageButtonlistforward;
	
	ImageButton imageButtonBack;
	ImageButton imageButtonFav;
	ImageButton imageButtonForward;
	SearchItem currentItem = null;
	
	private static final String LIST_STATE_KEY = "listState";
	private static final String LIST_POSITION_KEY = "listPosition";
	private static final String ITEM_POSITION_KEY = "itemPosition";
	ArrayList<SearchItem> SearchHistory = new ArrayList<SearchItem>();
	private int SearchItemIndex = -1;

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		toggletheme("");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.definition);
		Bundle extras = getIntent().getExtras();
		queryProvider = new WQDictionaryQueryProvider(this);
		

		if (extras != null) {
			wordtoSearch = extras.getString("word").trim();
				position= extras.getInt("position");
		}
		
		WQDictionaryActivity.headerEndChar="{";
		WQDictionaryActivity.newlinebreak
		="}";
		imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
		imageButtonBack.setOnClickListener(this);
		imageButtonBack.setVisibility(View.GONE);
		
		imageButtonFav = (ImageButton) findViewById(R.id.imageButtonFav);
		imageButtonFav.setOnClickListener(this);
		//imageButtonFav.setVisibility(View.GONE);

		imageButtonForward = (ImageButton) findViewById(R.id.imageButtonForward);
		imageButtonForward.setOnClickListener(this);
		imageButtonForward.setVisibility(View.GONE);
		
		imageButtonlistBack = (ImageButton) findViewById(R.id.imagelistBack);
		imageButtonlistBack.setOnClickListener(this);
		if(position<=0)
		{
			imageButtonlistBack.setVisibility(View.GONE);
		}
		
		imageButtonlistforward = (ImageButton) findViewById(R.id.imagelistForward);
		imageButtonlistforward.setOnClickListener(this);
		
		if(WQDictionaryActivity. listofWords!=null&& position>=WQDictionaryActivity. listofWords.size()-1)
		{
			imageButtonlistforward.setVisibility(View.GONE);
		}
		
		expandableListViewdefinition = (ExpandableListView) findViewById(R.id.expandableListViewdefinition);
		definitionHedaer = (TextView) findViewById(R.id.definitionHedaer);
		linearlayout = (LinearLayout) findViewById(R.id.mainLayout);
		mainLayoutR = (RelativeLayout) findViewById(R.id.mainLayoutR);
		relativeLayoutlistButtons = (RelativeLayout) findViewById(R.id.relativeLayoutlistButtons);
		imageButtonGoToWiki= (ImageButton) this.findViewById(R.id.imageButtonGoToWiki);
		imageButtonGoToWiki.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!WQDictionaryActivity. IsConnected()) {
					Toast.makeText(WQDictionaryActivity. cont, "No Connection", Toast.LENGTH_LONG)
							.show();
					return;
				}

				String url = "https://"+WQDictionaryActivity.wiki+".wiktionary.org/wiki/"
						+ imageButtonGoToWiki.getTag().toString();
				if (!url.startsWith("http://") && !url.startsWith("https://"))
					url = "http://" + url;

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
			WQDictionaryActivity.	cont.startActivity(browserIntent);
			}
		});
		imageButtonGoToWiki.setVisibility(View.GONE);
		
		
		
		mGroupCollection = new ArrayList<GroupEntity>();
		
		
		
		expandableListViewadapter = new ExpandableListAdapter(this, expandableListViewdefinition,
				mGroupCollection, GetOnSwipeListener());
		expandableListViewdefinition.setAdapter(expandableListViewadapter);
		
		Words wordd=queryProvider
						.GetSingleExactWord(wordtoSearch);
		//Log.d("wordtoSearch", wordtoSearch);
	//	Log.d("wordtoSearch_n", WQDictionaryDBOpenHelper.Normalize( wordtoSearch));
	
		if (wordd != null) 
		{	
			String def=WQDictionaryActivity. Decode(wordd.getwate(), wordtoSearch,wordtoSearch);	
			definitionHedaer.setText(wordtoSearch);
			SetExpanderCollection(wordtoSearch, def);	
		
			AddSearchItem(wordtoSearch, wordd.id, "Exact", wordd);
			ReOrderHistory();		
			
		}

		androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
			// Set the icon
			actionBar.setIcon(R.drawable.wqsozluk); // Your icon resource
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);

		}
		String size="";
		if(WQDictionaryActivity.listofWords!=null)
			size=WQDictionaryActivity.listofWords.size()+"";
		String title="("+(position+1)+"/"+size+") "+wordtoSearch;
		setTitle(title);

		if(true) {
			AdRequest adRequest = new AdRequest.Builder().build();
			String interstitial= (String) getText(R.string.Admob_Intersitital);
			//Toast.makeText(getBaseContext(),interstitial+" ",Toast.LENGTH_LONG ).show();
			InterstitialAd.load(this, interstitial, adRequest,
					new InterstitialAdLoadCallback() {
						@Override
						public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
							// The mInterstitialAd reference will be null until
							// an ad is loaded.
							mInterstitialAd = interstitialAd;
							Log.i(TAG, "onAdLoaded");
							if (mInterstitialAd != null) {
								mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
								mInterstitialAd.show(DefinitionActivity.this);
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
		else
		{


			try {
				showAdmob();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		if(onSwipeListener!=null)
		{
			linearlayout.setOnTouchListener(onSwipeListener);
			mainLayoutR.setOnTouchListener(onSwipeListener);
		}
	}
	private void showAdmob() throws ClassNotFoundException {
		final AdView mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();

		if (adRequest != null && mAdView != null) {
			mAdView.loadAd(adRequest);
			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					mAdView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAdClosed() {
					super.onAdClosed();

				}
			});
		}
	}
	private OnSwipeTouchListener GetOnSwipeListener() {
		// TODO Auto-generated method stub
		if(onSwipeListener!=null)
			return onSwipeListener;
		onSwipeListener=	new OnSwipeTouchListener() {
			
	        public boolean onSwipeTop() {
	           // Toast.makeText(WQDictionaryActivity.cont, "top", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        public boolean onSwipeRight() 
	        {
	        	
	        	if(WQDictionaryActivity.listofWords!=null)
	        	{
	        		if(position>0)
	        		{
	    	        	position--;	        		
	    	        	MoveToPostion(false);
	        		}
	        	}
	        
	            return true;
	        }
	        public boolean onSwipeLeft() 
	        {
	        	
	        	if(WQDictionaryActivity.listofWords!=null)
	        	{
	        		if(position<WQDictionaryActivity.listofWords.size()-1)
	        		{
	    	        	position++;
	        	
	    	        	MoveToPostion(true);
	        		}
	        	}
	        
	            return true;
	        }
	        public boolean onSwipeBottom() {	    
	            return false;
	        }
	        @Override
	        public void onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (e.getAction() == MotionEvent.ACTION_DOWN) {					
			
					Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
					if(!expandableListViewadapter.handleClick)
					{
						expandableListViewadapter.handleClick=true;
						return;
					}
					if (!IsButtonsVisible) 
					{
						if(position>0)
							slideToLeft(imageButtonlistBack, 0, View.VISIBLE, 400);

						if(WQDictionaryActivity.listofWords!=null&& position<WQDictionaryActivity.listofWords.size()-1)
							slideToRight(imageButtonlistforward, 0, View.VISIBLE);

						slideToBottom(imageButtonFav,0,View.VISIBLE);
						slideToBottom(imageButtonGoToWiki,0,View.VISIBLE);
						IsButtonsVisible=true;
					} 
					else 
					{						
						slideToLeft(imageButtonlistBack, -imageButtonlistBack.getWidth(), View.GONE, 300);
						slideToRight(imageButtonlistforward, imageButtonlistforward.getWidth(), View.GONE);
						slideToBottom(imageButtonFav,imageButtonFav.getHeight(),View.GONE);
						slideToBottom(imageButtonGoToWiki,imageButtonGoToWiki.getHeight(),View.GONE);
					
						IsButtonsVisible=false;
					}
				

				}
			}
   
	    };
	    return onSwipeListener;
	}
	private void toggletheme(String sender)
	{
		setTheme(WQDictionaryActivity.theme);

		if(!sender.equalsIgnoreCase(""))
			recreate();
	}

	public void slideToLeft(View view, int width, int visibility, int duration){
     	TranslateAnimation animate = new TranslateAnimation(0,width,0,0);
     	animate.setDuration(duration);
     	animate.setFillAfter(true);
     	view.startAnimation(animate);
     	view.setVisibility(visibility);
     	}
     
     public void slideToRight(View view, int width, int visibility){
     	TranslateAnimation animate = new TranslateAnimation(0,width,0,0);
     	animate.setDuration(500);
     	animate.setFillAfter(true);
     	view.startAnimation(animate);
     	view.setVisibility(visibility);
     	}
     public void slideToBottom(View view, int height, int visibility){
     	TranslateAnimation animate = new TranslateAnimation(0,0,0,height);
     	animate.setDuration(500);
     	animate.setFillAfter(true);
     	view.startAnimation(animate);
     	view.setVisibility(visibility);
     	}
	private void MoveToPostion(Boolean swipetoleft)
	{
		if(swipetoleft)
		{
			expandableListViewdefinition.startAnimation(outToLeftAnimation());			
		}
		else
			expandableListViewdefinition.startAnimation(outToRightAnimation());
		
		UpdateButtonVisibility(position);
		if(WQDictionaryActivity.listofWords!=null&&position<WQDictionaryActivity.listofWords.size())
		{
			Words wordf = WQDictionaryActivity.listofWords.get(position);		
		//	makeText(wordf.peyv+":"+wordf.NormalizedWord);
			Research(wordf.peyv);
		}
		
		if(swipetoleft)
		{		
			expandableListViewdefinition.startAnimation(inFromRightAnimation());
		}
		else
		{			
			expandableListViewdefinition.startAnimation(inFromLeftAnimation());
		}
		
		
	}
	 private Animation inFromLeftAnimation() {
		    Animation inFromLeft = new TranslateAnimation(
		        Animation.RELATIVE_TO_PARENT, -1.0f,
		        Animation.RELATIVE_TO_PARENT, 0.0f,
		        Animation.RELATIVE_TO_PARENT, 0.0f,
		        Animation.RELATIVE_TO_PARENT, 0.0f);
		    inFromLeft.setDuration(200);
		    inFromLeft.setInterpolator(new AccelerateInterpolator());
		    return inFromLeft;
		    }
	    private Animation outToRightAnimation() {
	        Animation outtoRight = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, +1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoRight.setDuration(200);
	        outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	        }
	
	 private Animation inFromRightAnimation() {

	        Animation inFromRight = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromRight.setDuration(200);
	        inFromRight.setInterpolator(new AccelerateInterpolator());
	        return inFromRight;
	        }
	 
	 private Animation outToLeftAnimation() {
		    Animation outtoLeft = new TranslateAnimation(
		        Animation.RELATIVE_TO_PARENT, 0.0f,
		        Animation.RELATIVE_TO_PARENT, -1.0f,
		        Animation.RELATIVE_TO_PARENT, 0.0f,
		        Animation.RELATIVE_TO_PARENT, 0.0f);
		    outtoLeft.setDuration(200);
		    outtoLeft.setInterpolator(new AccelerateInterpolator());
		    return outtoLeft;
		    }

	private void SetExpanderCollection(String word, String string) {
		mGroupCollection.clear();

		String[] langs = null;
		//string=string.replace("#\n", "").replace("#¡", "").replace("#‽‽¡", "").replace("#‽¡", "").trim();
		string=string.replace("#\n", "").replace("#}", "").replace("#!!}", "").replace("#!}", "").trim();

		if (string.contains("||")) {
			langs = string.split(java.util.regex.Pattern.quote("||"));
		} else {
			langs = new String[1];
			langs[0] = string;
		}
		for (int i = 0; i < langs.length; i++) {
			String lang = langs[i];
			String Name = lang;

			if (lang.contains(WQDictionaryActivity.headerEndChar)) {
				Name = lang.substring(0,
						lang.indexOf(WQDictionaryActivity.headerEndChar));
				lang = lang.substring(lang
						.indexOf(WQDictionaryActivity.headerEndChar));
				
			}
			String regex = "[0-9]+\\.";
			if (lang.matches(regex)) {
				String[] splitted = lang.split(regex);
				for (int k = 0; k < splitted.length; k++) {
					String subsplitted = splitted[k];
					if (splitted.length > 0) {
						GroupEntity ge = InsertGroupEntity(subsplitted, Name
								+ (k + 1));
						ge.Body=lang;
						mGroupCollection.add(ge);
					}
				}

			} else {

				GroupEntity ge = InsertGroupEntity(lang, Name);
				ge.Body=lang;
				mGroupCollection.add(ge);
			}

		}
		
		expandableListViewadapter = new ExpandableListAdapter(this, expandableListViewdefinition,
				mGroupCollection, GetOnSwipeListener());
		expandableListViewadapter.Word=word;
		expandableListViewadapter.definition=string;
		//Toast.makeText(getBaseContext(), word+"  ", Toast.LENGTH_LONG);

		expandableListViewdefinition.setAdapter(expandableListViewadapter);

		expandableListViewdefinition.setFocusableInTouchMode(true);
		expandableListViewdefinition.requestFocus();
		if(!word.equalsIgnoreCase(""))
		{
			imageButtonGoToWiki.setTag(word);
			imageButtonGoToWiki.setVisibility(View.VISIBLE);
		}
		ExpandWithTimer();
	}

	private GroupEntity InsertGroupEntity(String sep, String Name) {
		GroupEntity ge = new GroupEntity();

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
						WQDictionaryActivity.headerEndChar))
					continue;
				if (expandableListViewadapter!=null&&
						expandableListViewadapter.	IsListContaining(WQDictionaryActivity.listBinihere,sep2.trim())
						&&expandableListViewadapter.	IsListContaining(WQDictionaryActivity.listBinihere,ge.Name.trim())
						//sep2.trim().equalsIgnoreCase("Binihêre")
						//&& ge.Name.equalsIgnoreCase("Binihêre")

				)
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
		if (sep.startsWith(WQDictionaryActivity.headerEndChar + "\n"))
			sep = sep.substring(2);
		if (sep.startsWith(WQDictionaryActivity.headerEndChar))
			sep = sep.substring(1);
		if (sep.contains(WQDictionaryActivity.headerEndChar))
			sep = sep.replace(WQDictionaryActivity.headerEndChar, ":");
		if (sep.startsWith(WQDictionaryActivity.newlinebreak))
			sep = sep.substring(1);
		
			//////////////////Added 11.10.2017 to
		
		if(sep.contains("#!!"))
			sep = sep.replaceAll("#!!", "\t\t");
		if(sep.contains("#!"))
		{
			sep = sep.replaceAll("#!", "\t");	
		}
		
//			if(sep.contains("#‽‽"))
//				sep = sep.replaceAll("#‽‽", "\t\t");
//			if(sep.contains("#‽"))
//			{
//				sep = sep.replaceAll("#‽", "\t");	
//			}
			/////////////////
		
		int c = 1;
		while (sep.contains("#")) {
			if(sep.startsWith("واتە"))
			{
				 String arabNUm = (c+"")
				            .replaceAll("1", "١").replaceAll("2", "٢")
				            .replaceAll("3", "٣").replaceAll("4", "٤")
				            .replaceAll("5", "٥").replaceAll("6", "٦")
				            .replaceAll("7", "٧").replaceAll("8", "٨")
				            .replaceAll("9", "٩").replaceAll("0", "٠");
				 sep = sep.replaceFirst(java.util.regex.Pattern.quote("#"),"  "+ arabNUm+". ");
			}
			else
			{
				
			
			String cx = c + " .";
			sep = sep.replaceFirst(java.util.regex.Pattern.quote("#"), cx);
		}
			c++;
		}
		if (sep.endsWith(WQDictionaryActivity.newlinebreak))
			sep = sep.substring(0, sep.length() - 1);
		if (sep.contains(WQDictionaryActivity.newlinebreak))
			sep = sep.replaceAll(java.util.regex.Pattern
					.quote(WQDictionaryActivity.newlinebreak), "\n");

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
				if (elapsed >= TIMEOUTForExpand) 
				{
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

	public Words Research(String word) {
	
		Words wordd= queryProvider
					.GetSingleExactWord(word);
		if (wordd!=null	) {		
			String def = wordd.getwate(); 
			def=WQDictionaryActivity.Decode(def, word, word);
			if (!actionBarIsEnabled)
				definitionHedaer.setText(word);
			SetExpanderCollection(word, def);
			String title="("+(position+1)+"/"+WQDictionaryActivity.listofWords.size()+") "+word;
			setTitle(title);						
			
		} else 
		{
			
			Toast.makeText(getBaseContext(),
					getString(R.string.resultsnotfound, word), Toast.LENGTH_SHORT)
					.show();
		}
		return wordd;

	}
	public void ReOrderHistory() {
	
			    Collections.sort(SearchHistory, new Comparator<SearchItem>() {
			        @Override public int compare(SearchItem p1, SearchItem p2) {
			            return p1.Index - p2.Index; // Ascending
			        }

			    });
			    SearchItemIndex = SearchHistory.size()-1;
			    
			    for(int x=0; x<SearchHistory.size();x++)
				{
					SearchItem itemx=SearchHistory.get(x);					
				}
	}
	int maxIndex=0;
	public void AddSearchItem(String item, String id, String type, Object result) {
		//Log.d("adding", "adding");
		if (!item.equalsIgnoreCase("")) {
			int b = -1;
			SearchItem itemc = null;
			for (int i = 0; i < SearchHistory.size(); i++) {
				itemc = SearchHistory.get(i);
				if (itemc.Query.equals(item)
						&& itemc.SearchType.equalsIgnoreCase(type)) {
					itemc.Index=maxIndex+1;
					maxIndex=maxIndex+1;
					break;
				} else
					itemc = null;
			}

			if (itemc != null)
			{
				//SearchHistory.add(itemc);
			}
			else 
			{
				itemc = new SearchItem(item,id, type, result);
				itemc.Index=maxIndex+1;
				maxIndex=maxIndex+1;
				SearchHistory.add(itemc);
			}
			currentItem = itemc;
			SearchItemIndex = SearchHistory.size() - 1;
			if (SearchHistory.size() > 1)
				imageButtonBack.setVisibility(View.VISIBLE);
			imageButtonForward.setVisibility(View.GONE);
		}
		for(int x=0; x<SearchHistory.size();x++)
		{
			SearchItem itemx=SearchHistory.get(x);
		//	Log.d("Hist", itemx.Query+" "+itemx.Index);
		}
	}
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_definition, menu);		
		if (actionBarIsEnabled) 
		{
			try {

				MenuItem itemaaction_order = menu.findItem(action_Parveke_Peyv);
				itemaaction_order
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			} catch (Exception exc) {

			}

		} else {

			MenuItem itemaaction_order = menu.findItem(action_Parveke_Peyv);
			itemaaction_order.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if( android.R.id.home==item.getItemId()) {
			finsihActivity();
			return true;
		}
		else if( action_Parveke_Peyv==item.getItemId()) {
			ShareThisWord();
			return true;
		}
		else{
			return super.onOptionsItemSelected(item);
		}
	}
	private void ShareThisWord() 
	{
		String word=wordtoSearch;
		if(word.equalsIgnoreCase(""))
		{
			Toast.makeText(getBaseContext(), R.string.nowordSelected, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT,
				R.string.app_name);
		String sAux = "\n" + getString(R.string.peyvebisine)
				+ "\n";
		sAux = sAux+"\""+word+"\""+"\n\n";
		String wordw=word.replace(" ", "_");
		sAux = sAux
				+ "http://"+WQDictionaryActivity.wiki+".wiktionary.org/wiki/"+wordw+ " \n\n";
		i.putExtra(Intent.EXTRA_TEXT, sAux);
		startActivity(Intent.createChooser(i,
				getString(R.string.select)));
		
	}
	private void finsihActivity() {
		this.finish();
	}
	
	public void onClick(View v) {
		if (v == imageButtonBack) {
			try {
				imageButtonBack.setEnabled(false);
				SaveScrollPosition();
				if (SearchItemIndex > 0 && SearchHistory.size() > 0) {
					SearchItemIndex = SearchItemIndex - 1;

					SearchItem item = SearchHistory.get(SearchItemIndex);

					if (item != null) {
					//	autoCmopletetextView.setText(item.Query);
						if (item.SearchType == "Exact") {
							Research(item.Query);
						}
							//GetSingleExactWord(autoCmopletetextView.getText()
								//	.toString());

						//} else 
						//{
						//	Search(WQDictionaryDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
							//autoCmopletetextView.dismissDropDown();						
					//	}
						imageButtonForward.setVisibility(View.VISIBLE);
						currentItem = item;
						RestoreScrollPosition(item);
					}
					if (SearchItemIndex == 0)
						imageButtonBack.setVisibility(View.GONE);
				} else {
					imageButtonBack.setVisibility(View.GONE);
				}
			} finally {
				imageButtonBack.setEnabled(true);
			}

		} else if (v == imageButtonForward) {
			try {
				SaveScrollPosition();
				imageButtonForward.setEnabled(false);
				if (SearchItemIndex < SearchHistory.size() - 1) {
					SearchItemIndex = SearchItemIndex + 1;
					SearchItem item = SearchHistory.get(SearchItemIndex);

					if (item != null) {
					//	autoCmopletetextView.setText(item.Query);
						if (item.SearchType == "Exact") {
						//	GetSingleExactWord(autoCmopletetextView.getText()
							//		.toString());
							Research(item.Query);

						} else 
						{
							
						//	Search(WQDictionaryDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
							
							// listviewresult.onRestoreInstanceState(item.State);
						//	autoCmopletetextView.dismissDropDown();
						}
						currentItem = item;
						RestoreScrollPosition(item);
					}
					if (SearchItemIndex == SearchHistory.size() - 1)
						imageButtonForward.setVisibility(View.GONE);

				} else {
					imageButtonForward.setVisibility(View.GONE);
				}
				if (SearchItemIndex > 0)
					imageButtonBack.setVisibility(View.VISIBLE);
			} finally {
				imageButtonForward.setEnabled(true);
			}
		}
		else if (v == imageButtonFav) 
		{
						
			if(
					
					expandableListViewadapter!=null&&expandableListViewadapter.Word!=null&&!expandableListViewadapter.Word.equals("")
					&&expandableListViewadapter.definition!=null&&!expandableListViewadapter.definition.equals(""))
	WQDictionaryActivity.		AddRemoveFromFavList(getBaseContext(),expandableListViewadapter.Word, true);
			//else makeText("problem:"+adapter.Word);
		}
		else if (v == imageButtonlistBack) 
		{
			if(WQDictionaryActivity.listofWords!=null)
        	{
        		if(position>0)
        		{
    	        	position--;
    	        	MoveToPostion(false);
        		}
        	}
		}
		else if (v == imageButtonlistforward) 
		{
			if(WQDictionaryActivity.listofWords!=null)
        	{
        		if(position<WQDictionaryActivity.listofWords.size()-1)
        		{
    	        	position++;
    	        	MoveToPostion(true);
        		}
        	}
		}

	}
	public void makeText(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
	}
	private static long[] toLongArray(List<Long> list) {
		long[] ret = new long[list.size()];
		int i = 0;
		for (Long e : list)
			ret[i++] = e.longValue();
		return ret;
	}
	private long[] getExpandedIds() {
		ExpandableListView list = expandableListViewdefinition;

		if (expandableListViewadapter != null) {
			int length = expandableListViewadapter.getGroupCount();
			ArrayList<Long> expandedIds = new ArrayList<Long>();
			for (int i = 0; i < length; i++) {
				if (list.isGroupExpanded(i)) {
					expandedIds.add(expandableListViewadapter.getGroupId(i));
				}
			}
			return toLongArray(expandedIds);
		} else {
			return null;
		}
	}
	private void SaveScrollPosition() {

		SearchItem sc = currentItem;
		if (sc == null) {
			return;
		}

		if (sc.SearchType.equalsIgnoreCase("Search")) {
//			if (listviewresult.getVisibility() != View.VISIBLE) {
//				return;
//			}
			Bundle state = new Bundle();
			//Parcelable mListState = listviewresult.onSaveInstanceState();
		//	state.putParcelable(LIST_STATE_KEY, mListState);
			sc.State = state;
		} else {
			ExpandableListView listView = expandableListViewdefinition;
			if (listView.getVisibility() != View.VISIBLE) {
				return;
			}
			Bundle state = new Bundle();
			Parcelable mListState = listView.onSaveInstanceState();
			state.putParcelable(LIST_STATE_KEY, mListState);

			// Save position of first visible item
			int mListPosition = listView.getFirstVisiblePosition();
			state.putInt(LIST_POSITION_KEY, mListPosition);

			// Save scroll position of item
			View itemView = listView.getChildAt(0);
			int mItemPosition = itemView == null ? 0 : itemView.getTop();
			state.putInt(ITEM_POSITION_KEY, mItemPosition);
			long[] expandedIds = getExpandedIds();
			state.putLongArray("ExpandedIds", expandedIds);

			sc.State = state;
		}
		

	}

	private void RestoreScrollPosition(SearchItem item) {
		if (item == null) {

			return;
		}
		Bundle state = item.State;
		if (state == null) {
			return;
		}

		if (item.SearchType.equalsIgnoreCase("Search")) {
		
			Parcelable mListState = state.getParcelable(LIST_STATE_KEY);
			
		} else {
			ExpandableListView listView = expandableListViewdefinition;
			if (listView.getVisibility() != View.VISIBLE)
				return;

			Parcelable mListState = state.getParcelable(LIST_STATE_KEY);
			int mListPosition = state.getInt(LIST_POSITION_KEY);

			int mItemPosition = state.getInt(ITEM_POSITION_KEY);

			if (mListState != null)
				listView.onRestoreInstanceState(mListState);

			listView.setSelectionFromTop(mListPosition, mItemPosition);

			long[] expandedIds = state.getLongArray("ExpandedIds");
			if (expandedIds != null) {
				restoreExpandedState(expandedIds);
			}
		}

	}
	private void restoreExpandedState(long[] expandedIds) {

		if (expandedIds != null) {
			ExpandableListView list = expandableListViewdefinition;

			if (expandableListViewadapter != null) {
				for (int i = 0; i < expandableListViewadapter.getGroupCount(); i++) {
					long id = expandableListViewadapter.getGroupId(i);
					if (inArray(expandedIds, id))
						list.expandGroup(i);
				}
			}
		}
	}
	private static boolean inArray(long[] array, long element) {
		for (long l : array) {
			if (l == element) {
				return true;
			}
		}
		return false;
	}

	public void UpdateButtonVisibility(int position) {
		// TODO Auto-generated method stub
		//makeText("WQDictionaryActivity.position"+WQDictionaryActivity.position+" visible:"+IsButtonsVisible);
		if(position>=WQDictionaryActivity.listofWords.size()-1)
		{
		//	imageButtonlistforward.setVisibility(View.GONE);
			slideToRight(imageButtonlistforward, imageButtonlistforward.getWidth(), View.GONE);
			
		}
		else if(IsButtonsVisible)
		{
			slideToRight(imageButtonlistforward, 0, View.VISIBLE);
			
			//imageButtonlistforward.setVisibility(View.VISIBLE);
		}
		if(position<=0)
		{
			slideToLeft(imageButtonlistBack, -imageButtonlistBack.getWidth(), View.GONE,400);
			//imageButtonlistBack.setVisibility(View.GONE);
		}
		else if(IsButtonsVisible)
		{
			slideToLeft(imageButtonlistBack, 0, View.VISIBLE,400);
		}
		
	}

}

class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector = new GestureDetector(WQDictionaryActivity.cont, new GestureListener());

    public boolean onTouch(final View v, final MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            result = onSwipeRight();
                        } else {
                            result = onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            result = onSwipeBottom();
                        } else {
                            result = onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	OnSwipeTouchListener.this.onSingleTapConfirmed(e);        
            return super.onSingleTapConfirmed(e);
        }
		
    }
   
    public void onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
    public boolean onSwipeRight() {
        return false;
    }

    public boolean onSwipeLeft() {
        return false;
    }

    public boolean onSwipeTop() {
        return false;
    }

    public boolean onSwipeBottom() {
        return false;
    }
}
