package com.wqsozluk;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.ump.ConsentInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.wqsozluk.GroupEntity.GroupItemEntity;
import com.wqsozluk.SearchResultAdapter.ViewHolderWords;
import com.wqsozluk.WQDictionaryDB.WQDictionaryDBOpenHelper;


public class WQDictionaryActivity extends AppCompatActivity implements OnClickListener {

	public static List<Words> listofWords;
	public static boolean ShowSpecialKeys=true;
	public static boolean ShowHeaderTranslation=true;
	public static boolean ShowLanguageTranslation=true;
	public static String strtheme;
	public static String linkColor;
	public static List<String> headerList;
	public static List<String> headerTranslations;
	public static int theme;
	public static String wiki;
	com.facebook.ads.AdView adView;
	com.google.android.gms.ads.AdView mAdView;
	private static final int REQUEST_CODE_SPEECH_INPUT=1000;
	private static final int REQUEST_CODE_CONFIG=1001;
	//public static int position=-1;
	Boolean IsButtonsVisible = true;
	public static Map<String, List<String>> mapLanguages;
	public static int LocalisationIndex=-1;
	 //AdView   mAdView;
	 private final String TAG = WQDictionaryActivity.class.getSimpleName();

	CustomAutoCompleteTextView autoCmopletetextView;
	public OnSwipeTouchListener onSwipeListener;
	public static Context cont;
	public ExpandableListView mExpandableListView;	
	private static final String LIST_STATE_KEY = "listState";
	private static final String LIST_POSITION_KEY = "listPosition";
	private static final String ITEM_POSITION_KEY = "itemPosition";
	public static SQLiteDatabase mDatabase;
	ImageView imgvoicebutton;
	public static AppCompatActivity mContext=null;
	private List<GroupEntity> mGroupCollection;
	ExpandableListAdapter adapter;
	LinearLayout layoutListViewContents;
	int height, wwidth;
	public static String typeToSearch = "";
	public static String zimanquery  ;
	public static String letterx = "a";
	int currentVersionNumber = 0;
	String worddef = "";
	ImageView buttonsearch;
	Boolean upgrating = false;
	ImageButton imageButtonBack;
	ImageButton imageButtonFav;
	ImageButton imageButtonForward;
	ImageButton imageButtonGoToWiki;
	private int SearchItemIndex = -1;
	public static String headerEndChar = "¿";
	public static String newlinebreak = "¡";
	static boolean seperatorInitialized = false;
	Boolean IsArabic = false;
	String arabicpattern = "[ء-ي]+";
	Alphabet alphabet = new Alphabet();
	Uri uriDB = WQDictionaryDBProvider.CONTENT_URI;
	ImageView buttonhere;
	TextView textHistory;
	TextProgressBar progressbar2;
	ListView listviewresult;
	TimerTask task;
	Boolean IsCustomKeyboardVisible = false;
	CountDownTimer countDownTimer;
	int ItemCount = WQDictionaryDBOpenHelper.WordList;
	SearchItem currentItem = null;
	static String patternencodedErebic="£(.*?)¢";
	int totalccount = 60000;
	long elapsed;
	public static final String PRIVATE_PREF = "myapp";
	private static final String VERSION_KEY = "version_number";
	Boolean defaultWordedSplashed = false;
	final static long INTERVAL = 2000;
	final static long TIMEOUT = 1800000;
	Boolean CancelRequest = false;
	int intervalForExpand = 100;
	int TIMEOUTForExpand = 500;
	private String SelectedWord;
	public static String languageToLoad = "ku";
	Boolean CancelRequestedForExpand = false;
	public Boolean showarabickeyboard = false;
	String[] columnsDB = new String[] {WQDictionaryDB.KEY_WORD,
			WQDictionaryDB.KEY_DEFINITION};
	static Boolean dialogshowed = false;
	int[] to = new int[] { R.id.word, R.id.definition };
	ArrayList<SearchItem> SearchHistory = new ArrayList<SearchItem>();
	String[] items = new String[5];
	long id = 2;
	WQDictionaryQueryProvider provider;
	boolean[] itemsChecked = new boolean[items.length];
	public Boolean actionBarIsEnabled = false;
	CustomKeys custkeys;
	ArabicKeyboard mCustomKeyboard;
	RelativeLayout mainLayout;	
	private boolean disablemenu = false;
	public Boolean ShouldBreakSearches=false;
    long lastClickTime = 0;
    public static Boolean raisetextChanged=true;
    private static Map<String, String> encoderList; 
    private static Map<String, String> decoderList;
   public static Boolean ShowCategoryDialog=true;
	private LinearLayout linearLayoutcustomkeys;
	private boolean isDarkTheme;
	public static Configuration Config;
	private ConsentInformation consentInformation;
	private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
	public static List<String> listTranslatedLangs;
	public static List<String> listlinkedSections;
	public static List<String> listBinihere;

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext=this;
		wiki= (String) getText(R.string.wiki);
		toggleTheme("");
		SetLanguage();
		//setTheme(R.style.MyCustomTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wqdictionary);
		typeToSearch= (String) getText(R.string.typeToSearch);
		zimanquery = (String) getText(R.string.zimanquery);

        cont = this;
		init();

		encoderList=GetDecodeList();
		headerList=GetHeaderList();

		headerEndChar = getString(R.string.headerEndChar);
		newlinebreak = getString(R.string.newlinebreak);
		if(currentVersionNumber>20)
		{
			headerEndChar="{";
			newlinebreak="}";
		}
		findWiewsbyID();
		if (android.os.Build.VERSION.SDK_INT >= 11)
		{
			actionBarIsEnabled = true;
			androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
			if (actionBar != null) {
				// Set the display options
				actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
				// Set the icon
				actionBar.setIcon(R.drawable.wqsozluk); // Your icon resource
			}
		}
		mGroupCollection = new ArrayList<GroupEntity>();
		adapter = new ExpandableListAdapter(this, mExpandableListView,
				mGroupCollection, GetOnSwipeListener());
		mExpandableListView.setAdapter(adapter);
		imgvoicebutton=findViewById(R.id.search_voice_btn);
		imgvoicebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				speak();
			}
		});
		listviewresult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShouldBreakSearches=true;
				Words w=null;
					if(listviewresult.getAdapter() instanceof  SimpleAdapter)
					{
						SimpleAdapter sadapter=(SimpleAdapter)listviewresult.getAdapter();
						Map<String, String> data=( Map<String, String>)sadapter.getItem(position);
						String word=data.get(WQDictionaryDB.KEY_WORD);

						Intent intent = new Intent(cont, DefinitionActivity.class);

						Bundle b = new Bundle();

						if(word!=null) {
							b.putString("word", word.replace("\"", ""));
						}
						else
						{
							word=w.NormalizedWord;
							b.putString("word", word);
						}
						b.putInt("position", position);
						intent.putExtras(b);

						cont.startActivity(intent);
						overridePendingTransition(android. R.anim.fade_in,android. R.anim.fade_out);
					}
					else 	if(listviewresult.getAdapter() instanceof  SearchResultAdapter)
					{
						Log.d("SearchResultAdapter","SearchResultAdapter");
						SearchResultAdapter myadapter =(SearchResultAdapter)listviewresult.getAdapter();
						
						w=myadapter.listOfWords.get(position);
						
						if(w==null)
							return;
					SaveScrollPosition();
				
					Words ww =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(w.id);
					if(ww!=null)
					{
						SelectedWord =w.peyv;				
					
						worddef=Decode(ww.getwate(), SelectedWord,SelectedWord);
						worddef=worddef.replace(",", ", ");		
					SelectedWord = SelectedWord.replace(".", "").replace(",", "")
							.trim();
					autoCmopletetextView.setText(SelectedWord);

					HideKeyboard();
					listviewresult.setAdapter(null);

					mExpandableListView.setVisibility(View.VISIBLE);
					worddef = ReplaceTempChars(worddef);
					SetExpanderCollection(SelectedWord, worddef);
					w.wate=worddef;
					AddSearchItem(SelectedWord, w.id, "Exact", w);
					ReOrderHistory();
				}
					}
				
		}

		});
		listviewresult.setVisibility(View.GONE);
		progressbar2 = (TextProgressBar) findViewById(R.id.progressBarWithText);
		if (!upgrating)
			progressbar2.setVisibility(View.GONE);
		else {
			progressbar2.setProgress(3);
			progressbar2.setText((String) getText(R.string.peyvtenjebirin));

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					EnableLayoutChildrens(mainLayout, false);
				}
			});
		}
		TimerTask task2 = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Log.d("TAG", "getReadableDatabase()");
				mDatabase =WQDictionaryDB.mWQDictionaryDBOpenHelper.getReadableDatabase();
				if (!upgrating) {
				}
				task = new TimerTask() {
					@Override
					public void run() {
						elapsed += INTERVAL;
						if (elapsed >= TIMEOUT) {
							this.cancel();
							setprogressbar();
							return;
						}
						setprogressbar();
						if (CancelRequest) {
							this.cancel();
						}
					}
				};
				Timer timer = new Timer();
				if (WQDictionaryDBOpenHelper.Loading) {
					Log.d("TAG",
							"timer.scheduleAtFixedRate(task, 10, INTERVAL)");
					timer.scheduleAtFixedRate(task, 10, INTERVAL);
				} else {
					if (upgrating) {
						disablemenu = false;
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								EnableLayoutChildrens(mainLayout, true);

								progressbar2.setVisibility(View.GONE);

								if (actionBarIsEnabled) {
									invalidateOptionsMenu();
									//ActionBar actionBar = getActionBar();
									//actionBar.show();
								}
							}
						});

					}

				}

				this.cancel();
			}
		};
		Timer timer2 = new Timer();
		timer2.scheduleAtFixedRate(task2, 200, 500);
		if (!upgrating)
			AppRater.app_launched(this);
		
		provider=new WQDictionaryQueryProvider(getBaseContext());

		SetCursorAdapter();
		// make last search on restart Activity
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// //"Exact" or "Search"
			if (bundle.containsKey("keyboard")) {
				if (bundle.getString("keyboard").equalsIgnoreCase("Ar.")) {
					ResetArabicKeyboard();
				}
			}
			if (bundle.containsKey("Search")) {
				autoCmopletetextView.setText(bundle.getString("Search"));
				Search();
			} else if (bundle.containsKey("Exact")) {
				autoCmopletetextView.setText(bundle.getString("Exact"));
				GO();
			} else if (bundle.containsKey("widget_word")) {
				autoCmopletetextView.setText(bundle.getString("widget_word"));
				GO();
			}

		}

		if (actionBarIsEnabled) {
			if (!upgrating) {
				getSupportActionBar().show();
				//ActionBar actionBar = getActionBar();
				//actionBar.show();
			}
			custkeys.HideKEys();
		}
		custkeys.SetKeyTexts(wiki);
		if (upgrating) {
			Log.d("TAG", "showing what is new dialog");
			if(false) {
				showWhatsNewDialog();
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}

		} else {
			progressbar2.setVisibility(View.GONE);
			String savedhistory = GetSavedSearchHistory();
		
			if (!savedhistory.equalsIgnoreCase("")) 
			{
				autoCmopletetextView.setText(savedhistory);
				autoCmopletetextView.setSelection(savedhistory.length());
			}

		}
		if(onSwipeListener!=null)
		{
			layoutListViewContents.setOnTouchListener(onSwipeListener);

			mainLayout.setOnTouchListener(onSwipeListener);
		}
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		wwidth = displaymetrics.widthPixels;

		LoadTranslations();

		String pref=WQDictionaryConfig. loadUrlPref(this.getBaseContext(),
				"SpecialKeys");

		if(pref.equalsIgnoreCase("false")) {
			ShowSpecialKeys = false;
			ShowHideSpecialKeys(ShowSpecialKeys);
		}
		pref=WQDictionaryConfig. loadUrlPref(this.getBaseContext(),
					"HeaderTranslation");
		if(pref.equalsIgnoreCase("false"))
			ShowHeaderTranslation=false;

			pref=WQDictionaryConfig. loadUrlPref(this.getBaseContext(),
				"LangTranslation");
		if(pref.equalsIgnoreCase("false"))
			ShowLanguageTranslation=false;
		setListenerToRootView();
		final Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(false)
					test();
			}
		}, 500);
			if(!upgrating) {
				try {
					String word = "lllistCureKurdi";
					String normalized = WQDictionaryDBOpenHelper.Normalize(word);
					Cursor cursor = provider.GetCursor(WQDictionaryDB.KEY_WORD_N + " match ? ", normalized);
					Boolean find = false;
					if (cursor != null && cursor.getCount() > 0) {
						Log.d("TAG", "Cursor found = true");
					} else {
						initSavedVersionNumber(0);
						Log.d("TAG", "Cursor found  = false");
					}
				} catch (Exception e) {

				}
			}
	}

	private List< String> GetHeaderList() {
		String strHeader=(String)getText(R.string.headers);
		headerTranslations=new ArrayList<>();
		headerList=new ArrayList<>();
		listTranslatedLangs=new ArrayList<>();
		listlinkedSections=new ArrayList<>();
		listBinihere=new ArrayList<>();
		if(!strHeader.equalsIgnoreCase(""))
		{
			for (String s : strHeader.split(",")) {
				headerList.add(s.trim());
			}
		}
		String strHeaderTranslation=(String)getText(R.string.translations);
		if(!strHeaderTranslation.equalsIgnoreCase(""))
		{
			for (String s : strHeaderTranslation.split(",")) {
				headerTranslations.add(s.trim());
			}
		}
		String listTranslatedLnags=(String)getText(R.string.translatedLangs);
		if(!listTranslatedLnags.equalsIgnoreCase(""))
		{
			for (String s : listTranslatedLnags.split(",")) {
				listTranslatedLangs.add(s.trim());
			}
		}
		String strlinkedSections=(String)getText(R.string.linkedSections);
		if(!strlinkedSections.equalsIgnoreCase(""))
		{
			for (String s : strlinkedSections.split(",")) {
				listlinkedSections.add(s.trim());
			}
		}
		String strbinihere=(String)getText(R.string.strbinihere);
		if(!strbinihere.equalsIgnoreCase(""))
		{
			for (String s : strbinihere.split(",")) {
				listBinihere.add(s.trim());
			}
		}
		return headerList;
	}

	private void initializeMobileAdsSdk() {
		if (isMobileAdsInitializeCalled.getAndSet(true)) {
			return;
		}
	}
	private void test()  {
		String strcountofword=WQDictionaryDB.mWQDictionaryDBOpenHelper.GetCount("FTSdictionary");
		String strcountofworddefs=WQDictionaryDB.mWQDictionaryDBOpenHelper.GetCount("FTSdictionary_Defs");
		Integer counofWord=Integer.parseInt(strcountofword);
		Integer countofdef=Integer.parseInt(strcountofworddefs);
		if(countofdef!=counofWord)
			makeText("Problem:"+(counofWord+" "+countofdef) +" missing") ;
		else
			makeText("No Problem:"+(counofWord-countofdef) +" missing") ;

		ArrayList<Words> wordswithPaging=WQDictionaryDB.mWQDictionaryDBOpenHelper.GetWordswithPaging("");
		//makeText(wordswithPaging.size()+"");
		int count=0;

		for (int i=0; i<wordswithPaging.size(); i++ )
		{
			Words word=(wordswithPaging.get(i));
			String w=word.peyv;
			//makeText(w);
			if(word.peyv!=null&&word.peyv.length()>0) {
				Words resulted = GetSingleExactWord(word.peyv.replace("-",""));

				if (resulted!=null)
				{
					count++;
					if(count%10000==0)
						makeText(count+"  "+word.NormalizedWord);
				} else if(!word.peyv.contains("-"))
				{
					AddRemoveFromFavList(getBaseContext(),word.peyv, true);
				}
			}
			}
		if(counofWord!=count)
		{
			makeText("Not Equal");

		}

	}

	private void ShowHideSpecialKeys(boolean visible)
	{
		if(!visible)
			linearLayoutcustomkeys.setVisibility(View.GONE);
		else
			linearLayoutcustomkeys.setVisibility(View.VISIBLE);
	}
	Boolean isOpened=false;
	public void setListenerToRootView() {
		final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {

				int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
				if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.

					isOpened = true;
				} else if (isOpened) {

					isOpened = false;
				}
			}
		});
	}
	private void speak()
	{
		Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		if (languageToLoad.equalsIgnoreCase("ku")) {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		}
		else if (languageToLoad.equalsIgnoreCase( "en")) {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		}
		else if (languageToLoad.equals("de")) {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
		}
		else if (languageToLoad.equals("fa")) {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
		}
		else if (languageToLoad.equals( "tr")) {
			Locale locale = new Locale("tr", "TR");
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,  "tr-TR");

		}
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak");
		try
		{
			startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

		}
		catch (Exception e)
		{
			makeText(e.getMessage());
		}
	}
	public static void SelectTheme()
	{
		WQDictionaryActivity.linkColor="#4E275A";
		if (strtheme.equalsIgnoreCase("app")|| strtheme.equalsIgnoreCase(""))
		{
			WQDictionaryActivity.theme=R.style.MyCustomTheme;

			WQDictionaryActivity.linkColor="#552640";
		}
		else if (strtheme.equalsIgnoreCase("Appold"))
		{
			WQDictionaryActivity.theme=R.style.AppTheme_Dark;
			WQDictionaryActivity.linkColor="#4E275A";
		}
		else if(strtheme.equalsIgnoreCase("Dark"))
		{
			WQDictionaryActivity.theme=(R.style.AppTheme_Dark);
			WQDictionaryActivity.linkColor="#4CAF50";
		}
		else if(strtheme.equalsIgnoreCase("Light"))
		{
			WQDictionaryActivity.theme=R.style.AppTheme_Light;
			WQDictionaryActivity.linkColor="#2478B7";
		}
		else if(strtheme.equalsIgnoreCase("Classic"))
		{
			WQDictionaryActivity.theme=R.style.AppTheme_Classic;
			WQDictionaryActivity.linkColor="#2478B7";
		}
		else if(strtheme.equalsIgnoreCase("System"))
		{
			switch ( mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
				case Configuration.UI_MODE_NIGHT_YES: {
					WQDictionaryActivity.theme=(R.style.AppTheme_Dark);
					WQDictionaryActivity.linkColor="#A36A00";
					break;
				}
				case Configuration.UI_MODE_NIGHT_NO: {
					WQDictionaryActivity.theme=R.style.AppTheme_Light;
					WQDictionaryActivity.linkColor="#2478B7";
					break;
				}
			}
		}
		else
		{
			WQDictionaryActivity.theme=R.style.MyCustomTheme;
		}

	}
	private Boolean toggleTheme(String sender)
	{
		WQDictionaryActivity.strtheme =WQDictionaryConfig. loadUrlPref(this.getBaseContext(),
				"Theme");


		SelectTheme();
		setTheme(WQDictionaryActivity.theme);
		if(sender.equalsIgnoreCase("Config"))
			recreate() ;// Recreate the activity to apply the new strtheme
		else
		{

		}
		return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode)
			{
				case REQUEST_CODE_SPEECH_INPUT:
					if(resultCode==RESULT_OK&&data!=null)
					{
						ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						autoCmopletetextView.setText(result.get(0).toLowerCase());
					}
					break;
				case REQUEST_CODE_CONFIG:

						ShowHideSpecialKeys(ShowSpecialKeys);
						toggleTheme("Config");
						break;
			}
	}

	ArrayList<Map<String, String>> CreateHistoryListFromString(
			String savedhistory) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] searchs = savedhistory.split(java.util.regex.Pattern
				.quote(","));
		for (int i = 0; i < searchs.length; i++) {
			String[] searchs2 = searchs[i].split(java.util.regex.Pattern
					.quote("|"));
			if (searchs2.length == 2) {
				String word = searchs2[0];
				String type = searchs2[1];
				list.add(putData(word, type));
			}
		}
		return list;
	}

	private void findWiewsbyID() {

		buttonsearch = (ImageView) findViewById(R.id.buttonsearch);
		buttonsearch.setOnClickListener(this);

		buttonhere = (ImageView) findViewById(R.id.buttonhere);
		buttonhere.setOnClickListener(this);

		autoCmopletetextView = (CustomAutoCompleteTextView) findViewById(R.id.autocomplete_search);
		listviewresult = (ListView) findViewById(R.id.list_result);
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

		imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
		imageButtonBack.setOnClickListener(this);
		imageButtonBack.setVisibility(View.GONE);
		
		imageButtonFav = (ImageButton) findViewById(R.id.imageButtonFav);
		imageButtonFav.setOnClickListener(this);
		imageButtonFav.setVisibility(View.GONE);

		imageButtonForward = (ImageButton) findViewById(R.id.imageButtonForward);
		imageButtonForward.setOnClickListener(this);
		imageButtonForward.setVisibility(View.GONE);
		textHistory = (TextView) findViewById(R.id.textHistory);
		custkeys = (CustomKeys) findViewById(R.id.customkeys);
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		layoutListViewContents = (LinearLayout) findViewById(R.id.layoutListViewContents);
		linearLayoutcustomkeys = (LinearLayout) findViewById(R.id.linearLayoutcustomkeys);
		
		AddFooterView();
			if(!upgrating) {
				if (WQDictionaryActivity.wiki.equalsIgnoreCase("ku")) {
					try {
						AudienceNetworkAds.initialize(this);
						showAdmobMeta();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					com.google.android.gms.ads.	MobileAds.initialize(this, new com.google.android.gms.ads.initialization. OnInitializationCompleteListener() {
						@Override
						public void onInitializationComplete(com.google.android.gms.ads.initialization.InitializationStatus initializationStatus) {
							try {

								showAdmob();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}

	}

	private void showAdmob() throws ClassNotFoundException
	{
		if (android.os.Build.VERSION.SDK_INT >= 14) {

			//MobileAds.initialize(this, "ca-app-pub-4819188859318435/5036961654");
			mAdView =(com.google.android.gms.ads.AdView) this.findViewById(R.id.mAdView);
//			final RelativeLayout imgFAv =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
		com.google.android.gms.ads.	AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
			if(adRequest!=null&&mAdView!=null)
			{
				//makeText(mAdView.toString());
				mAdView.loadAd(adRequest);
				mAdView.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						super.onAdLoaded();

						RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.ABOVE, R.id.mAdView);
						mAdView.setVisibility(View.VISIBLE);
						mAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
						Log.d(TAG, "Admob loaded");
					}

					@Override
					public void onAdClosed() {
						super.onAdClosed();

					}
				});

			}else
			{
				//makeText("aabd");
				mAdView =(com.google.android.gms.ads.AdView) this.findViewById(R.id.mAdView);
				mAdView.setVisibility(View.GONE);
				final RelativeLayout rlayout =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				layoutParams.setMargins(0, 0, 0, 30);
				rlayout.setLayoutParams(layoutParams);
			}
		}
		else
		{
			//makeText("aabd");
			mAdView =(com.google.android.gms.ads.AdView) this.findViewById(R.id.mAdView);
			mAdView.setVisibility(View.GONE);
			final RelativeLayout rlayout =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
			RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.setMargins(0, 0, 0, 30);
			rlayout.setLayoutParams(layoutParams);
		}

	}
	private void showAdmobMeta() throws ClassNotFoundException
	{
		try {
		adView = new com.facebook.ads.AdView(this,  "1281695262866543_1281708692865200", AdSize.BANNER_HEIGHT_50);
		LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
		adContainer.addView(adView);
		RelativeLayout adContainerRelLayout = (RelativeLayout) findViewById(R.id.banner_ad_layout);
		com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
			@Override
			public void onError(Ad ad, AdError adError) {

			}

			@Override
			public void onAdLoaded(Ad ad) {
				adContainerRelLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdClicked(Ad ad) {
				adContainerRelLayout.setVisibility(View.GONE);
				// Ad clicked callback
			}

			@Override
			public void onLoggingImpression(Ad ad) {
				// Ad impression logged callback
			}
		};
		Log.e(TAG,				"Requesting: " );
		adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
		}
		catch (Exception e)
		{
		}
	}
	private void init() {
		try {
			SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
					Context.MODE_PRIVATE);
		int	savedVersionNumber = sharedPref.getInt(VERSION_KEY, 0);
			try {
				PackageInfo pi = getPackageManager().getPackageInfo(
						getPackageName(), 0);
				currentVersionNumber = pi.versionCode;
			} catch (Exception e) {
			}
		
			if (currentVersionNumber > savedVersionNumber) {
				upgrating = true;
				Log.d("TAG", "upgrating = true");
				disablemenu = true;

				Editor editor = sharedPref.edit();

				editor.putInt(VERSION_KEY, currentVersionNumber);
				editor.commit();
			}
			ShowCategoryDialog=sharedPref.getBoolean("ShowCategoryDialog", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	private void initSavedVersionNumber(Integer nNumber) {
		try {
			SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
					Context.MODE_PRIVATE);
			int	savedVersionNumber = sharedPref.getInt(VERSION_KEY, 0);
			try {
				Editor editor = sharedPref.edit();

				editor.putInt(VERSION_KEY, currentVersionNumber);
				editor.commit();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showWhatsNewDialog() {

		try {

			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.dialog_whatsnew, null);
			Builder builder = new AlertDialog.Builder(this);
			builder.setView(view)
					.setTitle(getString(R.string.whatsnew))
					.setPositiveButton("Temam",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();								
								}
							});

			builder.create().show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private void SetLanguage() {
		try {
			SharedPreferences prefs = getBaseContext().getSharedPreferences(
					"Lang", 0);

			languageToLoad = prefs.getString("Lang", "tr");
			Locale locale = new Locale(languageToLoad);
			Configuration config = new Configuration();
			if (languageToLoad == "ku") {
				Locale.setDefault(locale);
				config.locale = locale;
			} else {
				Locale locJa = new Locale(languageToLoad);
				Locale.setDefault(locJa);
				config.locale = locJa;
			}
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			Config=config;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setprogressbar() {
		this.runOnUiThread(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run()
			{
				try {
					int count = WQDictionaryDBOpenHelper.WordList;
					progressbar2.setVisibility(View.VISIBLE);
					totalccount = WQDictionaryDBOpenHelper.totalFileCount;

					int value =0;
					if(totalccount>0) {
						value = ((WQDictionaryDBOpenHelper.LoadedRawWordFileCount * 100) / totalccount);
					}
					if (!WQDictionaryDBOpenHelper.Loading) {

						if (task != null)
							task.cancel();
						CancelRequest = true;
						progressbar2.setVisibility(View.GONE);

						disablemenu = false;
						EnableLayoutChildrens(mainLayout, true);
						if (actionBarIsEnabled)
							invalidateOptionsMenu();

						if (!dialogshowed) {
							upgrating = false;
							showDialog(String.format((String) getText(R.string.ferhengbarbu),"%", count));
						}
						addNotification(

                                (String) getText(R.string.ferhengbarbudialogheader),String.format((String) getText(R.string.ferhengbarbudialogtext), count));
					}

					progressbar2.setProgress(value);
					progressbar2.setText(String.format((String)getText(R.string.ferhengbarbu),value, count));
				}
				catch (Exception e)
				{}
			}
		});
	}

	@Override
	protected void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	private void ViewWordList() {
		Intent intent = new Intent(this, ZimanListActivity.class);
		startActivity(intent);
	}
	private void GoToSettings() {

		Intent intent = new Intent(this, WQDictionaryConfig.class);
		startActivityForResult(intent, REQUEST_CODE_CONFIG);
	}
	private void restartActivity() {
		Intent intent = getIntent();
		Bundle b = new Bundle();
		if (showarabickeyboard) // bundle.getString("keyboard").equalsIgnoreCase("Ar.")
			b.putString("keyboard", "Ar.");
		if (SearchHistory.size() > 0) {
			SearchItem item = SearchHistory.get(SearchHistory.size() - 1);
			b.putString(item.SearchType, item.Query); // Your id

		}
		intent.putExtras(b);
		finish();
		startActivity(intent);
	}

	public static void setDefaultLocale(Context context, String locale) {
		Locale locJa = new Locale(locale);
		Locale.setDefault(locJa);

		Configuration config = new Configuration();
		config.locale = locJa;

		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	private void SetCursorAdapter() {
		String query = "a";
		 final MatrixCursor cursor = null;
	
		@SuppressWarnings("deprecation")
		final
		SimpleCursorAdapter words = new SimpleCursorAdapter(this,
				R.layout.result, cursor,  new String[] { WQDictionaryDB.KEY_WORD,
						WQDictionaryDB.KEY_DEFINITION }, to)
							{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            SearchResultAdapter.ViewHolderWords holder;
            if (v == null) 
            {
                holder = new SearchResultAdapter.ViewHolderWords();
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.result, null);
                holder.textViewWord = (TextView) v.findViewById(R.id.word);
                holder.textViewdef = (TextView) v.findViewById(R.id.definition);
                //other stuff
                v.setTag(holder);
            } else {
                holder = (SearchResultAdapter.ViewHolderWords) v.getTag();
            }
				Cursor cursorc = (Cursor) getItem(position);

				String word = WQDictionaryQueryProvider.GetValue(cursorc, WQDictionaryDB.KEY_WORD);
				String definition = WQDictionaryQueryProvider.GetValue(cursorc,
						WQDictionaryDB.KEY_DEFINITION);
		
				String word_n = WQDictionaryQueryProvider.GetValue(cursorc,
						WQDictionaryDB.KEY_WORD_N);
				if(word==null||word.equalsIgnoreCase(""))
				{
					word=word_n;
					//Log.d("word","word null" );
				}

				if (!(definition.toLowerCase().contains(".ogg") || definition
						.toLowerCase().contains(".oga"))) {
					holder.textViewWord
							.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				} else {
					Drawable img = cont.getResources().getDrawable(
							R.drawable.volume);
					holder.textViewWord
							.setCompoundDrawablesWithIntrinsicBounds(null,
									null, img, null);

				}
				holder.textViewWord.setText(word);
				holder.textViewdef.setText(definition);

				return v;
        }
    };
		 runOnUiThread(new Runnable()
		    {
		        @Override
		        public void run()
		        {	
		        	adapter.notifyDataSetChanged();
		        }
		    });
	
		words.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) 
			{		
				try
				{
				if(!raisetextChanged)
					return null;
			//	Log.d("runQuery",constraint.toString());
				String s = "";
				if (constraint != null)					
					s =constraint.toString();
				s = s.replaceAll("\\p{Punct}|\\d", "");
			
			//	CurrentConstraint=s;		
				if(s.equalsIgnoreCase("")||s.length()<=1)
				{
					return null;
				}				
					String normalized=WQDictionaryDBOpenHelper. Normalize(s);
						normalized= normalized+"*";
					Cursor cursor2 =provider.  GetCursor(WQDictionaryDB.KEY_WORD_N + " match ? ", normalized );
					if(cursor2!=null)
					{				
					FilteredMatrixCursor bb2 = new FilteredMatrixCursor(
							cursor2, "", WQDictionaryDB.KEY_WORD_N );			//No filter						
					return bb2.matrixCursor;
					}
					else 
					{
						return null;
					}

			}
				catch(Exception e)
				{
					return null;
				}
			}
			
		});
		 runOnUiThread(new Runnable()
		    {
		        @Override
		        public void run()
		        {	
		        	autoCmopletetextView.setAdapter(words);
		        }
		    });
		
		autoCmopletetextView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				raisetextChanged=false;
				Uri data = Uri.withAppendedPath(uriDB, String.valueOf(id));
				listviewresult.setVisibility(View.GONE);
				mExpandableListView.setVisibility(View.VISIBLE);
				Words resulted =  PutResults(data);
				
				if (resulted!=null) {
					HideKeyboard();
					SimpleCursorAdapter cadapter=	(SimpleCursorAdapter)autoCmopletetextView.getAdapter();
					Cursor c=(Cursor)cadapter.getItem(position);
			
					String d= WQDictionaryQueryProvider.GetValue(c,  WQDictionaryDB.KEY_WORD);
					if(d==null||d.equalsIgnoreCase(""))
						d= WQDictionaryQueryProvider.GetValue(c,  WQDictionaryDB.KEY_WORD_N);
					resulted.peyv=d;
					autoCmopletetextView.setText(d);
			
					AddSearchItem(d, resulted.id
							, "Exact", resulted);
					ReOrderHistory();
				}

				raisetextChanged=true;
			}
		});
	}

	private Words PutResults(Uri data) 
	{
		Words rword=null;
		Cursor cursor = getContentResolver()
				.query(data, null, null, null, null);
		if (cursor == null) {
			// finish();
			return rword;
		} 
		else {
			cursor.moveToFirst();
			String	id = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_ID);
			SelectedWord = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD);
			String selectedWord_n= WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
			if(SelectedWord==null||SelectedWord.equalsIgnoreCase(""))
			{
				SelectedWord =selectedWord_n;
			 //makeText(SelectedWord+ " : sss");
			}
			//Date currentDate1 = new Date();

		Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
		if(w!=null)
		{
			worddef=Decode(w.getwate(), SelectedWord, selectedWord_n);
			worddef=worddef.replace(",", ", ");
			rword=new Words();
			rword.id=id;
			rword.wate=worddef;
	
			//Log.d("Loaded", diffInMs +" msec");
			worddef = ReplaceTempChars(worddef);
			SetExpanderCollection(SelectedWord, worddef);
			
			return rword;
		}
		else
		{
			return rword;
		}
			
			
		}
		
	}

	private String ReplaceTempChars(String str) {
		worddef = worddef.replaceAll(
				java.util.regex.Pattern.quote(newlinebreak), "\n");
				//.replaceAll("#\n", "").trim(); //replace empty lines
		return worddef;
	}

	public void onClick(View v) {
		if (v == buttonsearch) 
		{
			if(autoCmopletetextView.getText().toString().length()==1)
				showBigDataWarningDialog();
			else
			{
				Search();
				//showInfoActivity();
			}
			
		}

		else if (v == buttonhere) {
			GO();
		}

		else if (v == imageButtonBack) {
			try {
				imageButtonBack.setEnabled(false);
				SaveScrollPosition();
				if (SearchItemIndex > 0 && SearchHistory.size() > 0) {
					SearchItemIndex = SearchItemIndex - 1;

					SearchItem item = SearchHistory.get(SearchItemIndex);

					if (item != null) {
						autoCmopletetextView.setText(item.Query);
						if (item.SearchType == "Exact") 
						{
							GetSingleExactWord(autoCmopletetextView.getText()
									.toString());
						} else 
						{
							Search(WQDictionaryDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
							autoCmopletetextView.dismissDropDown();	
							UpdateAnimatedButtonVisibilities(true);
						}
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
						autoCmopletetextView.setText(item.Query);
						if (item.SearchType == "Exact") {
							GetSingleExactWord(autoCmopletetextView.getText()
									.toString());

						} else 
						{
							Search(WQDictionaryDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
							autoCmopletetextView.dismissDropDown();
							UpdateAnimatedButtonVisibilities(true);
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
			if(adapter!=null&&adapter.Word!=null&&!adapter.Word.equals("")
					&&adapter.definition!=null&&!adapter.definition.equals(""))
				AddRemoveFromFavList(getBaseContext(), adapter.Word, true);
		}

	}
	private void addNotification( String title, String message) {
	    NotificationCompat.Builder builder =
	            new NotificationCompat.Builder(this
				)
	            .setSmallIcon(R.drawable.wqsozluk)
	            .setContentTitle(title)  
	            .setContentText(message);  

	    Intent notificationIntent = new Intent(this, WQDictionaryActivity.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
					PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
			builder.setContentIntent(contentIntent);
		}
		else
		{
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(contentIntent);
		}
	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	    manager.notify(1, builder.build());  
	}
	private void showBigDataWarningDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		try {
			
			TextView title = new TextView(this);
			final TextView textViewmessage = new TextView(this);
			textViewmessage.setTextSize(17);
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextSize(20);

			String s =String.format((String) getText(R.string. toomanyresults),autoCmopletetextView.getText() );

			textViewmessage.setText(s);

			textViewmessage.setPadding(10, 10, 10, 10);

			title.setText(getText(R.string.hayjehebin));
			builder.setCustomTitle(title);

			builder.setView(textViewmessage);
			builder.setNegativeButton(R.string.dialogNa,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}

					});
			builder.setPositiveButton(R.string.dialogEre,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							try {
							Search();

							} catch (Exception e) { // e.toString();
							}

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
	public String ResetArabicKeyboard() {
		showarabickeyboard = !showarabickeyboard;
		String text = "";
		if (showarabickeyboard) {
			if (mCustomKeyboard == null) {
				mCustomKeyboard = new ArabicKeyboard(this, R.id.keyboardview,
						R.xml.keyboard, showarabickeyboard);

				mCustomKeyboard.registerEditText(R.id.autocomplete_search);
				mCustomKeyboard.showCustomKeyboard(autoCmopletetextView);
			}
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			height = displaymetrics.heightPixels;
			wwidth = displaymetrics.widthPixels;
			text = "Lat.";

			if (height != 0)
				autoCmopletetextView.setDropDownHeight((height / 3));

		} else {
			text = getText(R.string.buttonarAlph).toString();
			autoCmopletetextView.setDropDownHeight(LayoutParams.WRAP_CONTENT);

		}

		custkeys.KeyboardChanged(text);
		mCustomKeyboard.SetShouldShown(showarabickeyboard,
				R.id.autocomplete_search);
		autoCmopletetextView.setText("");
		autoCmopletetextView.setSelection(0);
		autoCmopletetextView.setDrawable(showarabickeyboard);
		return text;
	}

	public void GO() {
		String word = autoCmopletetextView.getText().toString();
		Words resulted = GetSingleExactWord(word);
		autoCmopletetextView.setSelection(autoCmopletetextView.getText()
				.length());
		if (resulted!=null) {
			AddSearchItem(word, resulted.id, "Exact", resulted);
		} else {
			makeText(getString(R.string.resultsnotfound, word));
		}

	}

	public void InsertLetter(String unicode) {

		String text=autoCmopletetextView.getText().toString();
		int split=autoCmopletetextView.getSelectionStart();
		String start=text.substring(0, split);
		String end=text.substring( split);
		autoCmopletetextView.setText( start+ unicode+end);
		autoCmopletetextView.setSelection(split+1);

	}

	private void Search() {
		String word = autoCmopletetextView.getText().toString();
	//Log.d("searching", "searching");
		Cursor resulted = Search(WQDictionaryDB.KEY_WORD + " match ? ",word);
		autoCmopletetextView.setSelection(autoCmopletetextView.getText()
				.length());

		if (resulted!=null&&resulted.getCount()>0) {
			AddSearchItem(word,"", "Search",resulted);
			SaveScrollPosition();
			UpdateAnimatedButtonVisibilities(true);
		}
	}

	@SuppressLint("NewApi") 
	private Cursor Search(final String selection, final String query) {
		//Log.d("search", "search");
		Boolean resulted = false;
		if (ItemCount != WQDictionaryDBOpenHelper.WordList) {
			}
		String spaced=query;
			spaced=spaced+"*";
		Cursor cursor =provider.  GetCursor(selection, spaced);
		if (cursor != null) 
		{
			resulted = PutCursorResult(cursor);				
		}
		else
		{
			String normalized= WQDictionaryDB.WQDictionaryDBOpenHelper. Normalize(query);
			//if(normalized.contains(" "))
				normalized=normalized+"*";
			 cursor =provider.  GetCursor(WQDictionaryDB.KEY_WORD_N + " match ? ",
					 normalized);
			 if (cursor != null) 
				{
					resulted = PutCursorResult(cursor);				
				}
		}
		if (resulted) 
		{
			HideKeyboard();
			listviewresult.requestFocus();
			if(actionBarIsEnabled)
				listviewresult.setAlpha((float) 1);
			
		} else
			makeText(getString(R.string.resultsnotfound, query));
		textHistory.setVisibility(View.GONE);
		return cursor;
	}

	private Boolean PutCursorResult(Cursor cursor) {
		Boolean resulted = false;
		
		if (cursor != null) {
			if (cursor.getCount() > 0) 
			{
				resulted = true;
				mExpandableListView.setVisibility(View.GONE);
				imageButtonFav.setVisibility(View.GONE);
				imageButtonGoToWiki.setVisibility(View.GONE);
				listviewresult.setVisibility(View.VISIBLE);
				int x = 0;
				final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				SearchResultAdapter adapter = new SearchResultAdapter(this, list,
						R.layout.result, columnsDB, to) ;
				
				SelectableCursorWrapper bb = new SelectableCursorWrapper(
						cursor, "", WQDictionaryDB.KEY_WORD); //Add all items by ""
				
				if (bb != null) 
				{				
					list.addAll(bb.listOfItems);
					adapter.listOfWords=bb.listOfWords;
					listviewresult.setAdapter(adapter);	
					listofWords=adapter.listOfWords;
				}
				if (cursor.getCount() < 50)
				{
				}
				else
				{
					String result = getString(R.string.resultsfound,
							cursor.getCount());
					makeText(result);	
					//HideLoadingProgressBar();
				}					
				
			} else {
				listviewresult.setVisibility(View.GONE);
				mExpandableListView.setVisibility(View.VISIBLE);

				if (cursor != null && cursor.getCount() > 0) 
				{
					resulted = true;
					cursor.moveToFirst();

					SelectedWord = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD);
					String def = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_DEFINITION);					
				
					String result = getString(R.string.resultsfound,
							cursor.getCount());
					makeText(result);
				} else {
					resulted = false;
					makeText(getString(R.string.resultsnotfound, SelectedWord));
				}
			}
		} else {
			resulted = false;
			makeText(getString(R.string.resultsnotfound, SelectedWord));
		}
		
		return resulted;
	}	


	private HashMap<String, String> putData(String name, String peyv) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(WQDictionaryDB.KEY_WORD, name);
		item.put(WQDictionaryDB.KEY_DEFINITION, peyv);
		return item;
	}

	public static String Decode(String definition, String word, String normalize) 
	{
		String strToreturn=definition;
		if(encoderList==null)
			encoderList=GetDecodeList();
		

		if(word!=null)
		{
			strToreturn=strToreturn.replace("^@", word);
			//Log.d("wordddd",word);
		}
		if(normalize!=null)
		{
			//Log.d("normalize",normalize);
			strToreturn=strToreturn.replace("@^", normalize);		
		}
	//	Log.d("strToreturn",strToreturn);
		for (Entry<String, String> entry : decoderList.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		   
    		strToreturn=strToreturn.replace(value, key);
    	}
		strToreturn=strToreturn.replace(",", ", ")
				.replace("+", " + ").replace(":", ": ").trim();
		//Log.d("strtoreturn", strToreturn);
		return strToreturn;
	}
	public static String Encode(String definition) 
	{
		String strToreturn=definition;
		for (Entry<String, String> entry : encoderList.entrySet()) 
		{
		    String key = entry.getKey();
		    String value = entry.getValue();
		   
    		strToreturn=strToreturn.replace(key, value);

    	}
		return strToreturn;
	}
	private static Map<String, String> GetDecodeList() 
	{
		LinkedHashMap<String, String> mpaofdecoders = new LinkedHashMap<String, String>();


		mpaofdecoders.put("}Özel ad}# özel ad erkek ad}|Köken{}# Eski Türkçe:", "^a");
		mpaofdecoders.put("}İsim (çekim)}|çoğul ad{} # hâl:", "~a");
		mpaofdecoders.put("}|Türk lehçeleri{}Eski Türkçe:", "$a");
		mpaofdecoders.put("}|Türk lehçeleri{}Azerice:", "~b");
		mpaofdecoders.put("}|Türk lehçeleri{}Tatarca:", "&b");
		mpaofdecoders.put("|Sözcük birliktelikleri{}", "$b");
		mpaofdecoders.put("}Ad}# (halk ağzı) # ağız:", "!b");
		mpaofdecoders.put("İngilizce{}Kısaltma}{}#", "^c");
		mpaofdecoders.put("}|Çeviriler{}İngilizce:", "~c");
		mpaofdecoders.put("|Sözcük birliktelikleri", "$c");
		mpaofdecoders.put("|Türetilmiş kavramlar{}", "!c");
		mpaofdecoders.put("}|Çeviriler{}Afrikanca:", "`c");
		mpaofdecoders.put("}Özel ad}# (beldeler):", "^d");
		mpaofdecoders.put("}|Çeviriler{}Almanca:", "~d");
		mpaofdecoders.put("|Türetilmiş kavramlar", "$d");
		mpaofdecoders.put("Türkçe{}Yanlış}|Doğru", "!d");
		mpaofdecoders.put("}Özel ad}# (ilçeler):", "^e");
		mpaofdecoders.put("}|Köken{}# Fransızca:", "$e");
		mpaofdecoders.put("İngilizce{}Kısaltma}#", "<e");
		mpaofdecoders.put("}Özel ad}# (köyler):", ">e");
		mpaofdecoders.put("|Karşıt anlamlılar{}", "!e");
		mpaofdecoders.put("}Ad}# (fizik-kimya):", "^f");
		mpaofdecoders.put("}||İngilizce{}Ad}# :", "~f");
		mpaofdecoders.put("||Karaçay Balkarca{}", "$f");
		mpaofdecoders.put("}|Çeviriler{}Fince:", "<f");
		mpaofdecoders.put("|İlgili sözcükler{}", ">f");
		mpaofdecoders.put("İtalyanca{}Ad}dişil", "!f");
		mpaofdecoders.put("}Ad}|Söyleniş{}IPA:", "`f");
		mpaofdecoders.put("(köyler):Diyarbakır", "^g");
		mpaofdecoders.put("}|Köken{}İngilizce:", "~g");
		mpaofdecoders.put("}|Köken{}Fransızca:", "$g");
		mpaofdecoders.put("}İskoç İngilizcesi:", "!g");
		mpaofdecoders.put("||İngilizce (Eylem)", "`g");
		mpaofdecoders.put("İtalyanca{}Ad}eril", "^h");
		mpaofdecoders.put("|Karşıt anlamlılar", "~h");
		mpaofdecoders.put("}|Köken{}# Türkçe:", "$h");
		mpaofdecoders.put("# özel ad erkek ad", "<h");
		mpaofdecoders.put("Türkmence{}Eylem}#", "!h");
		mpaofdecoders.put("(köyler):Kastamonu", "`h");
		mpaofdecoders.put("Osmanlıca{}Eylem}#", "^i");
		mpaofdecoders.put("İngilizce{}Eylem}#", "~i");
		mpaofdecoders.put("(köyler):Balıkesir", "!i");
		mpaofdecoders.put("Çağatayca{}Eylem}#", "`i");
		mpaofdecoders.put("birliktelikleri{}#", "_j");
		mpaofdecoders.put("}Karaçay Balkarca:", "^j");
		mpaofdecoders.put("Türkçe{}Belirteç}#", "~j");
		mpaofdecoders.put("(köyler):Çanakkale", "&j");
		mpaofdecoders.put("||Karaçay Balkarca", "$j");
		mpaofdecoders.put("}|Köken{}# Arapça:", "<j");
		mpaofdecoders.put("}Ad}# (halk ağzı):", ">j");
		mpaofdecoders.put("İskoç İngilizcesi:", "!j");
		mpaofdecoders.put("}||İsveççe{}Ad}# :", ")j");
		mpaofdecoders.put("||Türkçe (Ön ad){}", "`j");
		mpaofdecoders.put("|İlgili sözcükler", "^k");
		mpaofdecoders.put("}# (belediyeler):", "~k");
		mpaofdecoders.put("Dillerarası{}Özel", "$k");
		mpaofdecoders.put("# Arapça:+Farsça:", "!k");
		mpaofdecoders.put("(köyler):Erzincan", "`k");
		mpaofdecoders.put("|Benzer sözcükler", "^l");
		mpaofdecoders.put("|Ayrıca bakınız{}", "~l");
		mpaofdecoders.put("|Eş anlamlılar {}", "$l");
		mpaofdecoders.put("||Kırım Tatarca{}", "<l");
		mpaofdecoders.put("|Eş anlamlılar{}", "!l");
		mpaofdecoders.put("}|Köken{}Türkçe:", "^m");
		mpaofdecoders.put("}|Söyleniş{}IPA:", "~m");
		mpaofdecoders.put("Karaçay Balkarca", "$m");
		mpaofdecoders.put("Tatarca{}Eylem}#", "<m");
		mpaofdecoders.put("||Türkçe (Ön ad)", "!m");
		mpaofdecoders.put("}|Köken{}Arapça:", "~n");
		mpaofdecoders.put("}Modern Yunanca:", "$n");
		mpaofdecoders.put("(köyler):Erzurum", "!n");
		mpaofdecoders.put("|Üst kavramlar{}", "~o");
		mpaofdecoders.put("durumu}|Köken{}#", "$o");
		mpaofdecoders.put("Azerice{}Eylem}#", "<o");
		mpaofdecoders.put("İspanyolca{}Ad}#", "!o");
		mpaofdecoders.put("biçimi}|Köken{}#", "^p");
		mpaofdecoders.put("(köyler):Antalya", "~p");
		mpaofdecoders.put("(köyler):Giresun", "$p");
		mpaofdecoders.put("(köyler):Kütahya", "<p");
		mpaofdecoders.put("Hollandaca{}Ad}#", "!p");
		mpaofdecoders.put("}Ad} (dişil)}# :", "`p");
		mpaofdecoders.put("|Alt kavramlar{}", "_q");
		mpaofdecoders.put("Türkçe{}Eylem}#", "^q");
		mpaofdecoders.put("Osmanlıca{}Ad}#", "~q");
		mpaofdecoders.put("İngilizce{}Ad}#", "$q");
		mpaofdecoders.put("|Türk lehçeleri", "<q");
		mpaofdecoders.put("Türkmence{}Ad}#", ">q");
		mpaofdecoders.put("Çağatayca{}Ad}#", "!q");
		mpaofdecoders.put("Fransızca{}Ad}#", ")q");
		mpaofdecoders.put("|Bilimsel adı{}", "`q");
		mpaofdecoders.put("(köyler):Samsun", "^r");
		mpaofdecoders.put("||Eski Türkçe{}", "~r");
		mpaofdecoders.put("kimse}|Köken{}#", "$r");
		mpaofdecoders.put("(köyler):Ankara", "!r");
		mpaofdecoders.put("lehçeleri{}Eski", "^s");
		mpaofdecoders.put("İtalyanca{}Ad}#", "~s");
		mpaofdecoders.put("(köyler):Manisa", "$s");
		mpaofdecoders.put("}Kırım Tatarca:", "<s");
		mpaofdecoders.put("}Kısaltma}{}# :", "!s");
		mpaofdecoders.put("||Kırım Tatarca", "~t");
		mpaofdecoders.put("|Ayrıca bakınız", "$t");
		mpaofdecoders.put("(köyler):Yozgat", "!t");
		mpaofdecoders.put("(köyler):Mardin", "`t");
		mpaofdecoders.put("İtalyanca{}Özel", "^u");
		mpaofdecoders.put("(köyler):Elazığ", "~u");
		mpaofdecoders.put("olmak}|Köken{}#", "&u");
		mpaofdecoders.put("(köyler):Mersin", "$u");
		mpaofdecoders.put("}Ad} (eril)}# :", "<u");
		mpaofdecoders.put("|Eş anlamlılar", "!u");
		mpaofdecoders.put("# Eski Türkçe:", "`u");
		mpaofdecoders.put("|Üst kavramlar", "^v");
		mpaofdecoders.put("(çekim)}|çoğul", "~v");
		mpaofdecoders.put("|Doğru yazım{}", "$v");
		mpaofdecoders.put("Balkarca{}Ad}#", "!v");
		mpaofdecoders.put("Türkçe{}Zarf}#", "_w");
		mpaofdecoders.put("}# (beldeler):", "^w");
		mpaofdecoders.put("(köyler):Sivas", "~w");
		mpaofdecoders.put("|Alt kavramlar", "&w");
		mpaofdecoders.put("türü}|Bilimsel", "$w");
		mpaofdecoders.put("Türkçe{}Ad}{}#", "<w");
		mpaofdecoders.put("olan}|Köken{}#", ">w");
		mpaofdecoders.put("İbranice{}Ad}#", "!w");
		mpaofdecoders.put("|Açıklamalar{}", ")w");
		mpaofdecoders.put("(köyler):Çorum", "`w");
		mpaofdecoders.put("(köyler):Bursa", "_x");
		mpaofdecoders.put("(köyler):İzmir", "^x");
		mpaofdecoders.put("(köyler):Tokat", "~x");
		mpaofdecoders.put("(köyler):Konya", "$x");
		mpaofdecoders.put("(köyler):Adana", "<x");
		mpaofdecoders.put("Esperanto dili", ">x");
		mpaofdecoders.put("|Yan kavramlar", "!x");
		mpaofdecoders.put("}Marathi dili:", ")x");
		mpaofdecoders.put("anlamlılar{}#", "`x");
		mpaofdecoders.put("Azerice{}Ad}#", "^y");
		mpaofdecoders.put("|Bilimsel adı", "~y");
		mpaofdecoders.put("Tatarca{}Ad}#", "&y");
		mpaofdecoders.put("işi}|Köken{}#", "$y");
		mpaofdecoders.put("Almanca{}Ad}#", "<y");
		mpaofdecoders.put("Osmanlıca{}Ön", "!y");
		mpaofdecoders.put("İngilizce{}Ön", "_z");
		mpaofdecoders.put("Kırım Tatarca", "^z");
		mpaofdecoders.put("familyasından", "~z");
		mpaofdecoders.put("İsveççe{}Ad}#", "&z");
		mpaofdecoders.put("||Eski Türkçe", "$z");
		mpaofdecoders.put("}Eski Türkçe:", "<z");
		mpaofdecoders.put("Türkçe:}|Türk", "!z");
		mpaofdecoders.put("Özbekçe{}Ad}#", "`z");
		mpaofdecoders.put("Estonca{}Ad}#", "^A");
		mpaofdecoders.put("||İngilizce{}", "~A");
		mpaofdecoders.put("Macarca{}Ad}#", "$A");
		mpaofdecoders.put("Yunanca{}Ad}#", ">A");
		mpaofdecoders.put("||Türkmence{}", "!A");
		mpaofdecoders.put("||Çağatayca{}", "`A");
		mpaofdecoders.put("|Atasözleri{}", "^B");
		mpaofdecoders.put("(köyler):Ağrı", "~B");
		mpaofdecoders.put("(köyler):Bolu", "$B");
		mpaofdecoders.put("|Yazılışlar{}", ">B");
		mpaofdecoders.put("||Osmanlıca{}", "!B");
		mpaofdecoders.put("||Türkçe (Ad)", "`B");
		mpaofdecoders.put("}Kısaltma}# :", "^C");
		mpaofdecoders.put("Türkçe{}Ad}#", "~C");
		mpaofdecoders.put("Türkçe{}Özel", "$C");
		mpaofdecoders.put("}# (köyler):", "<C");
		mpaofdecoders.put("kavramlar{}#", "!C");
		mpaofdecoders.put("İsim (çekim)", "`C");
		mpaofdecoders.put("Türkçe{}İsim", "^D");
		mpaofdecoders.put("}İspanyolca:", "~D");
		mpaofdecoders.put("ad}|Köken{}#", "$D");
		mpaofdecoders.put("}Özel ad}# :", "<D");
		mpaofdecoders.put("}Hollandaca:", "!D");
		mpaofdecoders.put("|Doğru yazım", "`D");
		mpaofdecoders.put("sözcükler{}#", "^E");
		mpaofdecoders.put("}Ad}# tr-ad:", "~E");
		mpaofdecoders.put("|Açıklamalar", "$E");
		mpaofdecoders.put("}Portekizce:", ">E");
		mpaofdecoders.put("Kürtçe{}Ad}#", "!E");
		mpaofdecoders.put("||Hollandaca", "`E");
		mpaofdecoders.put("(köyler):Van", "^F");
		mpaofdecoders.put("||İspanyolca", "~F");
		mpaofdecoders.put("||Portekizce", "&F");
		mpaofdecoders.put("Guarani dili", "$F");
		mpaofdecoders.put("|Kısaltmalar", ">F");
		mpaofdecoders.put("}İngilizce:", "!F");
		mpaofdecoders.put("|Yazılışlar", "`F");
		mpaofdecoders.put("Eski Türkçe", "^G");
		mpaofdecoders.put("}Fransızca:", "~G");
		mpaofdecoders.put("Fince{}Ad}#", "$G");
		mpaofdecoders.put("}İtalyanca:", "<G");
		mpaofdecoders.put("(havacılık)", "!G");
		mpaofdecoders.put("Dillerarası", "`G");
		mpaofdecoders.put("(eczacılık)", "^H");
		mpaofdecoders.put("||İngilizce", "~H");
		mpaofdecoders.put("|Örnekler{}", "&H");
		mpaofdecoders.put("|Atasözleri", "$H");
		mpaofdecoders.put("}Türkmence:", "<H");
		mpaofdecoders.put("||Osmanlıca", ">H");
		mpaofdecoders.put("Tatarca{}Ön", "!H");
		mpaofdecoders.put("||Gagavuzca", "`H");
		mpaofdecoders.put("||Çağatayca", "^I");
		mpaofdecoders.put("||Türkmence", "~I");
		mpaofdecoders.put("}Esperanto:", "$I");
		mpaofdecoders.put("Rusça{}Ad}#", "<I");
		mpaofdecoders.put("}Katalanca:", ">I");
		mpaofdecoders.put("||İtalyanca", "!I");
		mpaofdecoders.put("}Arnavutça:", "`I");
		mpaofdecoders.put("Çekçe{}Ad}#", "_J");
		mpaofdecoders.put("}Galiçyaca:", "^J");
		mpaofdecoders.put("||Tatarca{}", "~J");
		mpaofdecoders.put("Lehçe{}Ad}#", "&J");
		mpaofdecoders.put("|Deyimler{}", "$J");
		mpaofdecoders.put("Beyaz Rusça", "<J");
		mpaofdecoders.put("}|ç.{} IPA:", ">J");
		mpaofdecoders.put("Danca{}Ad}#", "!J");
		mpaofdecoders.put("(çekim)){}#", ")J");
		mpaofdecoders.put("||Azerice{}", "`J");
		mpaofdecoders.put("}Gagavuzca:", "^K");
		mpaofdecoders.put("}İrlandaca:", "~K");
		mpaofdecoders.put("}İzlandaca:", "$K");
		mpaofdecoders.put("}Ukraynaca:", "<K");
		mpaofdecoders.put("||Kumanca{}", "!K");
		mpaofdecoders.put("}Endonezce:", "`K");
		mpaofdecoders.put("||Fransızca", "^L");
		mpaofdecoders.put("|Anagramlar", "~L");
		mpaofdecoders.put("}Gucaratça:", "$L");
		mpaofdecoders.put("Telugu dili", "<L");
		mpaofdecoders.put("|Çeviriler", ">L");
		mpaofdecoders.put("Türkçe{}Ön", "!L");
		mpaofdecoders.put("dili{}Ad}#", "`L");
		mpaofdecoders.put("kullanılan", "^M");
		mpaofdecoders.put("}Norveççe:", "~M");
		mpaofdecoders.put("||Türkçe{}", "$M");
		mpaofdecoders.put("sözcüğünün", "<M");
		mpaofdecoders.put("tarafından", ">M");
		mpaofdecoders.put("}Bulgarca:", "!M");
		mpaofdecoders.put("|Kaynakça{", "`M");
		mpaofdecoders.put("}İbranice:", "^N");
		mpaofdecoders.put("}Bretonca:", "~N");
		mpaofdecoders.put("}Hırvatça:", "&N");
		mpaofdecoders.put("}Slovakça:", "$N");
		mpaofdecoders.put("}Litvanca:", "<N");
		mpaofdecoders.put("}Ön ad}# :", "!N");
		mpaofdecoders.put("}Kırgızca:", "`N");
		mpaofdecoders.put("||Norveççe", "_O");
		mpaofdecoders.put("}Slovence:", "^O");
		mpaofdecoders.put("Sanskritçe", "~O");
		mpaofdecoders.put("||Sırpça{}", "&O");
		mpaofdecoders.put("eril çoğul", "$O");
		mpaofdecoders.put("|Resmi Adı", "<O");
		mpaofdecoders.put("}Ermenice:", ">O");
		mpaofdecoders.put("# Türkçe:", "!O");
		mpaofdecoders.put("|Söyleniş", "`O");
		mpaofdecoders.put("İngilizce", "^P");
		mpaofdecoders.put("Osmanlıca", "~P");
		mpaofdecoders.put("# Arapça:", "&P");
		mpaofdecoders.put("|Heceleme", "$P");
		mpaofdecoders.put("İtalyanca", "<P");
		mpaofdecoders.put("Türkmence", "!P");
		mpaofdecoders.put("Çağatayca", "`P");
		mpaofdecoders.put("|Örnekler", "_Q");
		mpaofdecoders.put("(eskimiş)", "^Q");
		mpaofdecoders.put("}İsveççe:", "~Q");
		mpaofdecoders.put("# Farsça:", "&Q");
		mpaofdecoders.put("(çekim)}#", "$Q");
		mpaofdecoders.put("|Deyimler", "<Q");
		mpaofdecoders.put("|çoğul ad", ">Q");
		mpaofdecoders.put("}Latince:", "!Q");
		mpaofdecoders.put("Bakınız{}", ")Q");
		mpaofdecoders.put("(bilimsel", "`Q");
		mpaofdecoders.put("}Tatarca:", "^R");
		mpaofdecoders.put("}Macarca:", "~R");
		mpaofdecoders.put("belediye.", "$R");
		mpaofdecoders.put("}Estonca:", "<R");
		mpaofdecoders.put("|Köken {}", ">R");
		mpaofdecoders.put("||Azerice", "!R");
		mpaofdecoders.put("(dişil)}#", "`R");
		mpaofdecoders.put("Fince{}Ön", "^S");
		mpaofdecoders.put("anlamında", "~S");
		mpaofdecoders.put("||Tatarca", "&S");
		mpaofdecoders.put("||İsveççe", "$S");
		mpaofdecoders.put("}Japonca:", ">S");
		mpaofdecoders.put("}Rumence:", "!S");
		mpaofdecoders.put("k}|arapça", "`S");
		mpaofdecoders.put("biçiminde", "^T");
		mpaofdecoders.put("birbirine", "~T");
		mpaofdecoders.put("ilçesi.}#", "$T");
		mpaofdecoders.put("}Özbekçe:", "<T");
		mpaofdecoders.put("||Kumanca", ">T");
		mpaofdecoders.put("Afrikanca", "!T");
		mpaofdecoders.put("Başkurtça", "`T");
		mpaofdecoders.put("}Letonca:", "^U");
		mpaofdecoders.put("Pali dili", "~U");
		mpaofdecoders.put("Vietnamca", "&U");
		mpaofdecoders.put("ilçesine", "$U");
		mpaofdecoders.put("Kısaltma", "<U");
		mpaofdecoders.put("||Türkçe", ">U");
		mpaofdecoders.put("belde.}#", "!U");
		mpaofdecoders.put("yazım{}#", "`U");
		mpaofdecoders.put("}Kürtçe:", "_V");
		mpaofdecoders.put("dişil ad", "^V");
		mpaofdecoders.put("Belirteç", "~V");
		mpaofdecoders.put("}Arapça:", "&V");
		mpaofdecoders.put("Ermenice", "$V");
		mpaofdecoders.put("İbranice", "<V");
		mpaofdecoders.put("arasında", ">V");
		mpaofdecoders.put("(eril)}#", "!V");
		mpaofdecoders.put("üzerinde", "`V");
		mpaofdecoders.put("}Farsça:", "_W");
		mpaofdecoders.put("herhangi", "^W");
		mpaofdecoders.put("yapılmış", "~W");
		mpaofdecoders.put("Bulgarca", "&W");
		mpaofdecoders.put("amacıyla", "$W");
		mpaofdecoders.put("||Sırpça", "<W");
		mpaofdecoders.put("sağlayan", "!W");
		mpaofdecoders.put("}Hintçe:", ")W");
		mpaofdecoders.put("}Baskça:", "`W");
		mpaofdecoders.put("}Sırpça:", "_Y");
		mpaofdecoders.put("İdo dili", "^Y");
		mpaofdecoders.put("}Korece:", "~Y");
		mpaofdecoders.put("Bengalce", "$Y");
		mpaofdecoders.put("Yorubaca", "<Y");
		mpaofdecoders.put("Pencapça", "!Y");
		mpaofdecoders.put("# Rumca:", "`Y");
		mpaofdecoders.put("||Farsça", "_Z");
		mpaofdecoders.put("Özel ad", "^Z");
		mpaofdecoders.put("}Ad}# :", "~Z");
		mpaofdecoders.put("bilimi)", "&Z");
		mpaofdecoders.put("Bakınız", "$Z");
		mpaofdecoders.put("Azerice", "<Z");
		mpaofdecoders.put("Tatarca", ">Z");
		mpaofdecoders.put("Almanca", "!Z");
		mpaofdecoders.put("}Fince:", "`Z");
		mpaofdecoders.put("(hayvan", "__a");
		mpaofdecoders.put("Atasözü", "_^a");
		mpaofdecoders.put("eril ad", "_~a");
		mpaofdecoders.put("Karaçay", "_&a");
		mpaofdecoders.put("yapılan", "_$a");
		mpaofdecoders.put("}Rusça:", "_<a");
		mpaofdecoders.put("bulunan", "_>a");
		mpaofdecoders.put("}Lehçe:", "_!a");
		mpaofdecoders.put("Özbekçe", "_)a");
		mpaofdecoders.put("Estonca", "_`a");
		mpaofdecoders.put("}Danca:", "_:a");
		mpaofdecoders.put("}Çekçe:", "__b");
		mpaofdecoders.put("verilen", "_^b");
		mpaofdecoders.put("(Ad){}#", "_~b");
		mpaofdecoders.put("Japonca", "_&b");
		mpaofdecoders.put("üzerine", "_$b");
		mpaofdecoders.put("olmayan", "_<b");
		mpaofdecoders.put("yarayan", "_>b");
		mpaofdecoders.put("biçimde", "_!b");
		mpaofdecoders.put("Abazaca", "_)b");
		mpaofdecoders.put("|arapça", "_`b");
		mpaofdecoders.put("}Çince:", "_:b");
		mpaofdecoders.put("çoğul}#", "__c");
		mpaofdecoders.put("Manksça", "_^c");
		mpaofdecoders.put("Kazakça", "_~c");
		mpaofdecoders.put("(mecaz)", "_&c");
		mpaofdecoders.put("}Galce:", "_$c");
		mpaofdecoders.put("etmek}#", "_<c");
		mpaofdecoders.put("Gürcüce", "_>c");
		mpaofdecoders.put("Yakutça", "_!c");
		mpaofdecoders.put("Çuvaşça", "_)c");
		mpaofdecoders.put("||Fince", "_`c");
		mpaofdecoders.put("Malayca", "_:c");
		mpaofdecoders.put("Altayca", "__d");
		mpaofdecoders.put("Uygurca", "_^d");
		mpaofdecoders.put("Letonca", "_~d");
		mpaofdecoders.put("#Arapça", "_&d");
		mpaofdecoders.put("||Rusça", "_$d");
		mpaofdecoders.put("||Lehçe", "_<d");
		mpaofdecoders.put("||Danca", "_>d");
		mpaofdecoders.put("Amharca", "_!d");
		mpaofdecoders.put("Türkçe", "_)d");
		mpaofdecoders.put("|Köken", "_`d");
		mpaofdecoders.put("köy.}#", "_:d");
		mpaofdecoders.put("Merkez", "__e");
		mpaofdecoders.put("adı{}#", "_^e");
		mpaofdecoders.put("olarak", "_~e");
		mpaofdecoders.put("ad){}#", "_&e");
		mpaofdecoders.put("durumu", "_$e");
		mpaofdecoders.put("}Ad}#:", "_<e");
		mpaofdecoders.put("adlar)", "_>e");
		mpaofdecoders.put("Yanlış", "_!e");
		mpaofdecoders.put("duruma", "_)e");
		mpaofdecoders.put("Kürtçe", "_`e");
		mpaofdecoders.put("içinde", "_:e");
		mpaofdecoders.put("çoğulu", "__f");
		mpaofdecoders.put("(bitki", "_^f");
		mpaofdecoders.put("ilgili", "_~f");
		mpaofdecoders.put("oluşan", "_&f");
		mpaofdecoders.put("olduğu", "_$f");
		mpaofdecoders.put("Korece", "_<f");
		mpaofdecoders.put("yerine", "_>f");
		mpaofdecoders.put("edilen", "_!f");
		mpaofdecoders.put("ilinin", "_)f");
		mpaofdecoders.put("ortaya", "_`f");
		mpaofdecoders.put("yüksek", "_:f");
		mpaofdecoders.put("l:en,#", "__g");
		mpaofdecoders.put("yapmak", "_^g");
		mpaofdecoders.put("Bağlaç", "_~g");
		mpaofdecoders.put("(nötr)", "_&g");
		mpaofdecoders.put("|ja-ad", "_$g");
		mpaofdecoders.put("Osetçe", "_<g");
		mpaofdecoders.put("bağlı", "_>g");
		mpaofdecoders.put("Eylem", "_!g");
		mpaofdecoders.put("Ön ad", "_)g");
		mpaofdecoders.put("Fince", "_`g");
		mpaofdecoders.put("köy}#", "_:g");
		mpaofdecoders.put("(halk", "__h");
		mpaofdecoders.put("erkek", "_^h");
		mpaofdecoders.put("Rakam", "_~h");
		mpaofdecoders.put("çoğul", "_&h");
		mpaofdecoders.put("ağzı)", "_$h");
		mpaofdecoders.put("etmek", "_<h");
		mpaofdecoders.put("kadar", "_>h");
		mpaofdecoders.put("büyük", "_!h");
		mpaofdecoders.put("Kırım", "_)h");
		mpaofdecoders.put("küçük", "_`h");
		mpaofdecoders.put("zaman", "_:h");
		mpaofdecoders.put("sonra", "__i");
		mpaofdecoders.put("şeyin", "_^i");
		mpaofdecoders.put("yapan", "_~i");
		mpaofdecoders.put("başka", "_&i");
		mpaofdecoders.put("olmak", "_$i");
		mpaofdecoders.put("gelen", "_<i");
		mpaofdecoders.put("kimse", "_>i");
		mpaofdecoders.put("karşı", "_!i");
		mpaofdecoders.put("iline", "_)i");
		mpaofdecoders.put("belli", "_`i");
		mpaofdecoders.put("doğru", "_:i");
		mpaofdecoders.put("kendi", "__j");
		mpaofdecoders.put("Çince", "_^j");
		mpaofdecoders.put("Beyaz", "_~j");
		mpaofdecoders.put("içine", "_&j");
		mpaofdecoders.put("uygun", "_$j");
		mpaofdecoders.put("-sı}#", "_<j");
		mpaofdecoders.put("veren", "_>j");
		mpaofdecoders.put("Zamir", "_!j");
		mpaofdecoders.put("bütün", "_)j");
		mpaofdecoders.put("bilgi", "_`j");
		mpaofdecoders.put("kabul", "_:j");
		mpaofdecoders.put("çıkan", "__k");
		mpaofdecoders.put("üzere", "_^k");
		mpaofdecoders.put("türlü", "_~k");
		mpaofdecoders.put("güzel", "_&k");
		mpaofdecoders.put("Şorca", "_$k");
		mpaofdecoders.put("Sıfat", "_<k");
		mpaofdecoders.put("Tayca", "_>k");



		
		List<Map.Entry<String, String>> entries =
				  new ArrayList<Map.Entry<String, String>>(mpaofdecoders.entrySet());
				Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
				  public int compare(Map.Entry<String, String> a, Map.Entry<String, String> b){
				    if(a.getValue().length()>=b.getValue().length())
					  return -1;
				    else
				    	return 1;
				  }
				});
				decoderList=new LinkedHashMap<String, String>();
				for (Map.Entry<String, String> entry : entries) {
					decoderList.put(entry.getKey(), entry.getValue());			
				}

		return mpaofdecoders;
	}
	
	private void showDialog(String message) {

		try {
			if (dialogshowed)
				return;
			dialogshowed = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(R.string.ferhengbarbudialogheader);
			builder.setMessage(message);

			builder.setPositiveButton(R.string.dialogEre,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							restartActivity();
						}

					});
			builder.setNegativeButton(R.string.dialogNa,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			dialogshowed = true;
		}

	}

	private void CollapseExpandableListView() {
		int count = adapter.getGroupCount();
		for (int i = 0; i < count; i++)
			if (mExpandableListView.isGroupExpanded(i))
				mExpandableListView.collapseGroup(i);
	}
	private void LoadTranslations()
	{
		String[][] Translation_Data = {
				{"Kurdî","Kürtçe","Kurdish","Kurdisch","كردي","کردی"},
				{"Kurdî (Soranî)","Kürtçe Soranî","Kurdish Sorani","Kurdisch Sorani","الصورانية الكردية","کردی سورانی"},
				{"Kurdî (Zazakî)","Kürtçe Zazakî","Kurdish Zazaki","Kurdisch Zazaki","زازاكي الكردي","کردی زازاکی"},
				{"Kurdî (şêxbizinî)","Kürtçe Şeyhbizin","Kurdish Sheyhbizin","Kurdisch Scheyhbizin","شيهبزين الكردي","کردی شیهبیزین"},
				{"Kurdî (kirmaşanî)","Kürtçe Kirmaşan","Kurdish Kirmashan","Kurdisch Kirmaschan","كرمشان الكردية","کردی کرماشان"},
				{"Înglîzî","İngilizce","English","Englisch","إنجليزي","انگلیسی"},
				{"Tirkî","Türkçe","Turkish","Turkisch","اللغة التركية","ترکی"},
				{"Erebî","Arapça","Arabic","Arabisch","عربي","عربی"},
				{"Almanî","Almanca","German","Deutsch","ألماني","آلمانی"},
				{"Farisî","Farsça","Persish","Persisch","اللغة الفارسية","فارسی"},
				{"Frensî","Fransızca","French","Franzosisch","فرنسي","فرانسوی"},
				{"Fînlandî","Fince","Finnish","Finnsich","الفنلندية","فنلاندی"},
				{"Holendî","Flamanca","Dutch","Niederlandisch","هولندي","هلندی"},
				{"Spanyolî","İspanyolca","Spanish","Spanisch","الأسبانية","اسپانیایی"},
				{"Rûsî","Rusça","Russish","Rıssisch","ريسيش","ریسیش کن"},
				{"Latînî","Latince","Latin","Latin","اللاتينية","لاتین"},
				{"Koreyî","Korece","Korean","Koreanisch","الكورية","کره ای"},
				{"Kurmancî","Kurmanci","Kurmanci","Kurmanci","الكرمانجية","کورمانی"},
				{"Portûgalî","Portekizce","Portuguese","Portugiesisch","البرتغالية","پرتغالی"},
				{"Swêdî","İsveççe","Swedish","Schwedisch","السويدية","سوئدی"},
				{"Japonî","Japonca","Japanese","japanisch","اليابانية","ژاپنی"},
				{"Îtalyayî","İtalyanca","Italian","Italienisch","ايطالي","ایتالیایی"},
				{"Esperanto","Esperanto","Esperanto","Esperanto","الاسبرانتو","اسپرانتو"},
				{"Polonyayî","Lehçe","dialect","Dialekt","لهجة","گویش"},
				{"Çînî","Çince","Chinese","Chinesisch","صينى","چینی ها"},
				{"Danîmarkî","Danca","Danish","dänisch","دانماركي","دانمارکی"},
				{"Hûngarî","Macarca","Hungarian","ungarisch","المجرية","مجارستانی"},
				{"Yewnanî","Yunanca","Greek","griechisch","اليونانية","یونانی"},
				{"Romanî","Romanca","romanian","rumänisch","روماني","رومانیایی"},
				{"Îbranî","İbranice","Hebrew","hebräisch","اللغة العبرية","عبری"},
				{"Çekî","Çekce","Czech","Tschechisch","التشيكية","کشور چک"},
				{"Tacîkî","Tacikce","Tajik","Tadschikisch","الطاجيكية","تاجیک"},
				{"Afrîkanî","Afrikanca","Afrikaans","Afrikaans","الأفريكانية","آفریقایی"},
				{"Azerbaycanî","Azerice","Azerbaijani","Aserbaidschanisch","أذربيجاني","آذربایجانی"},
				{"Belûcî","Belucice","Balochi","Belutschi","البلوشية","بلوچی"},
				{"Pali","Palice","Palice","Palast","باليس","پالیس"},
				{"Katalanî","Katalanca","Catalan","katalanisch","الكاتالونية","کاتالان"},
				{"Elbanî","Arnavutça","Albanian","albanisch","الألبانية","آلبانیایی"},
				{"Norwecî","Norwecçe","norwegian","norwegisch","النرويجية","نروژی"},
				{"Frîzyanî ya Roava","Batı Frizayince","West Frisian","Westfriesisch","الفريزية الغربية","فریزی غربی"},
				{"Wîetnamî","Vietnamca","Vietnamese","Vietnamesisch","الفيتنامية","ویتنامی"},
				{"Hîndî","Hintçe","Hindi","Hindi","الهندية","هندی"},
				{"Telûgû","Telugu dili","Telugu language","Telugu-Sprache","لغة التيلجو","زبان تلوگو"},
				{"Ermenkî","Ermenice","Armenian","Armenisch","الأرمينية","ارمنی"},
				{"Taî","Taice","thai","thailändisch","التايلاندية","تایلندی"},
				{"Ûkraynî","Ukraynaca","Ukrainian","Ukrainisch","الأوكرانية","اوکراینی"},
				{"Bûlgarî","Bulgarca","Bulgarian","bulgarisch","البلغارية","بلغاری"},
				{"Faroeî","Faroe Dili","Faroese","Färöisch","جزر فارو","فاروئی"},
				{"Papyamentoyî","Papyamentoca","papyamentoca","papyamentoca","بابيامنتوكا","پاپیامنتوکا"},
				{"Zûlûyî","Zuluca","Zulu","Zulu","الزولو","زولو"},
				{"Binihêre","Bakınız","See","Sehen","يرى","دیدن"},
				{"ji wêjeya klasîk","Klasik Metinlerde","In Classical Texts","In klassischen Texten","في النصوص الكلاسيكية","در متون کلاسیک"},
				{"Bide ber","Karşılaştır","Compare","Vergleichen","يقارن","مقایسه کنید"},
				{"Dijwate","Zıtanlam","Opposite","Gegenwörter","المعنى المعاكس","معنی مخالف"},
				{"Hevwate","Eşanlam","Synonym","Synonym","مرادف","مترادف"},
				{"Têkildar","Benzer","Similar","Ähnlich","مشابه","مشابه"},
				{"Bibîne","Bakınız","See","Sehen","يرى","دیدن"},
				{"Bikaranîn","Kullanım","Use","Verwenden","يستخدم","استفاده کنید"},
				{"Nêzîk","Yakınanlam","close meaning","nahe Bedeutung","معنى قريب","معنی نزدیک"},
				{"Jê","Türetim","derivation","Ableitung","الاشتقاق","استخراج"},
				{"Baştir","Doğru Kullanım","Correct usage","Richtige Benutzung","الاستخدام الصحيح","استفاده صحیح"},
				{"#binihêre","Bakınız","See","Sehen","يرى","دیدن"},
				{"واتە","Anlam","Meaning","Bedeutung","معنى","معنی"},
				{"Gotin","Seslendirme","Spelling","Aussprache","الدبلجة","دوبله"},
				{"xwendin","Okunuş","Spelling","Aussprache","نطق","تلفظ"},
				{"bilêvkirin","Seslendirme","Spelling","Aussprache","الدبلجة","دوبله"},
				{"wate","Anlam","Definition","Bedeutung","معنى","تعريف"},
				{"herwiha","Diğer Yazım","Also","Alternative Schreibweisen","كتابتي الأخرى","املای دیگر من"},
				{"ji","Kelime Ayrımı","Word Seperation","Worttrennung","فصل الكلمة","جداسازی کلمات"},
				{"Bi Soranî","Soranice","Sorani","Sorani","الصوراني","سورانی"},
				{"Werger","Çeviri","Translation","Übersetzung","ترجمة","ترجمه"},
				{"Bi zaraveyên Kurdî","Kürtçe Lehçelerinde","In Kurdish Dialects","In kurdischen Dialekten","باللهجات الكردية","در لهجه های کردی"},
				{"bi alfabeyên din","Diğer Alfabelerde","In Other Alphabets","In anderen Alphabeten","في أبجديات أخرى","در الفبای دیگر"},
		};

		mapLanguages = new HashMap();

		for (String[] languageKUrdi : Translation_Data ) {
			String name = languageKUrdi [0].toLowerCase();
			ArrayList neighbors = new ArrayList(Arrays.asList(languageKUrdi ));
			neighbors.remove(0);  // remove ourself
			mapLanguages.put(name, neighbors);
		}
		String languageCOde=Locale.getDefault().getLanguage();
		if (languageCOde.equalsIgnoreCase("ku"))
		{
			LocalisationIndex=1;
		}
		else if (languageCOde.equalsIgnoreCase("tr"))
		{
			LocalisationIndex=0;
		}
		else if (languageCOde.equalsIgnoreCase("en"))
		{
			LocalisationIndex=1;
		}
		if (languageCOde.equalsIgnoreCase("de"))
		{
			LocalisationIndex=2;
		}
		if (languageCOde.equalsIgnoreCase("ar"))
		{
			LocalisationIndex=3;
		}
		if (languageCOde.equalsIgnoreCase("fa"))
		{
			LocalisationIndex=4;
		}


	}

	private void SetExpanderCollection(final String word, String string) {
		mGroupCollection.clear();

		String[] seperated = null;
	//	string=string.replace("#\n", "").replace("#¡", "").replace("#‽‽¡", "").replace("#‽¡", "").trim();
		string=string.replace("#\n", "").replace("#}", "").replace("#!!}", "").replace("#!}", "").trim();

		if (string.contains("||")) {
			seperated = string.split(java.util.regex.Pattern.quote("||"));
		} else {
			seperated = new String[1];
			seperated[0] = string;
		}
		for (int i = 0; i < seperated.length; i++) {
			String sep = seperated[i];
			String Name = sep;

			if (sep.contains(headerEndChar)) {
				Name = sep.substring(0, sep.indexOf(headerEndChar));
				sep = sep.substring(sep.indexOf(headerEndChar));
				// /////////////////////////////////
				if (sep.replace(headerEndChar, "").trim() == "") {
					continue;
				}
				// //////////////////////////////
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
		adapter = new ExpandableListAdapter(this, mExpandableListView,
				mGroupCollection, GetOnSwipeListener());	
		 runOnUiThread(new Runnable()
		    {
		        @Override
		        public void run()
		        {
		        	mExpandableListView.setAdapter(adapter);
		    		UpDateFooterView(word);
		    		mExpandableListView.setFocusableInTouchMode(true);
		    		mExpandableListView.requestFocus();
		        }
		    });
	
		 adapter.Word=word;
			adapter.definition=string;
			
			listviewresult.setVisibility(View.GONE);
			mExpandableListView.setVisibility(View.VISIBLE);
			if(IsButtonsVisible)
			imageButtonFav.setVisibility(View.VISIBLE);
			autoCmopletetextView.dismissDropDown();

		ExpandWithTimer();
		
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
				if (mExpandableListView.getChildCount() > 0)
				{
					mExpandableListView.expandGroup(0);
					int heightofExpanablelistview= adapter.getGroupViewHeight(0);
					
					if(heightofExpanablelistview>0&& adapter.getGroupCount()>1&& heightofExpanablelistview<height-130)
					{
						for	(int i=1; i<adapter.getGroupCount();i++)
						{
							mExpandableListView.expandGroup(1);
							heightofExpanablelistview+=  adapter.getGroupViewHeight(1);
                            if(heightofExpanablelistview>height-130)
								break;
						}
					}
				}
				CancelRequestedForExpand = true;
			}
		});

	}

	private GroupEntity.GroupItemEntity InsertGroupItemEntity(GroupEntity ge, String sep) {
		GroupEntity.GroupItemEntity gi = ge.new GroupItemEntity();
		if (sep.startsWith(headerEndChar + "\n"))
			sep = sep.substring(2);
		if (sep.startsWith(headerEndChar))
			sep = sep.substring(1);
		if (sep.contains(headerEndChar))
			sep = sep.replace(headerEndChar, ":");
		if (sep.startsWith(newlinebreak))
			sep = sep.substring(1);
		int c = 1;
		//////////////////Added 11.10.2017 to
	
		
		if(sep.contains("#!!"))
			sep = sep.replaceAll("#!!", "\t\t");
		if(sep.contains("#!"))
		{
			sep = sep.replaceAll("#!", "\t");	
		}

		/////////////////
		while (sep.contains("#")) 		{
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
		if (sep.endsWith(newlinebreak))
			sep = sep.substring(0, sep.length() - 1);
		if (sep.contains(newlinebreak))
			sep = sep.replaceAll(java.util.regex.Pattern.quote(newlinebreak),
					"\n");

		String html = sep;
		if (ge.Name.equals("بنهێرە"))
			gi.Name = LanguagizeTitle(html);
		else
			gi.Name = LanguagizeTitle(Reverse(html, IsArabic));
		return gi;
	}

	private GroupEntity InsertGroupEntity(String sep, String Name) {
		GroupEntity ge = new GroupEntity();
		String binihere= (String) getText(R.string.Binihere);
		if (Name.contains("$"))
			Name = Name.replaceAll(java.util.regex.Pattern.quote("$"), "")
					.trim();
		if (Name.startsWith("\n"))
			Name = Name.substring(1).trim();
		if (Name.contains("\n"))
			Name = Name.split(java.util.regex.Pattern.quote("\n"))[0].trim();
		if (Name.endsWith("\n"))
			Name = Name.substring(0, Name.length() - 1).trim();

		if (sep.contains("|")) {
			ge.Name = Reverse(Name, IsArabic);
			String[] separated2 = null;
			separated2 = sep.split(java.util.regex.Pattern.quote("|"));
			for (int j = 0; j < separated2.length; j++) {
				String sep2 = separated2[j];
				if (sep2.trim().equalsIgnoreCase(headerEndChar))
					continue;
				if (sep2.trim().equalsIgnoreCase(binihere)
						&& ge.Name.equalsIgnoreCase(binihere))
					continue;
				GroupItemEntity gi = InsertGroupItemEntity(ge, sep2);

				if (!gi.Name.equalsIgnoreCase("\n") && !gi.Name.equals("")
						&& gi.Name != null)
					ge.GroupItemCollection.add(gi);
			}
		} else {
			ge.Name = Reverse(Name, IsArabic);
			GroupItemEntity gi = InsertGroupItemEntity(ge, sep);
			if (!gi.Name.equalsIgnoreCase("\n") && !gi.Name.equals("")
					&& gi.Name != null)
				ge.GroupItemCollection.add(gi);
		}

		return ge;
	}

	private String LanguagizeTitle(String Name) {

		String[] splits = Name.split(java.util.regex.Pattern.quote("\n"));
		String newtext = "";
		for (int i = 0; i < splits.length; i++) {
			String split = splits[i];
			if (i == 0) {
				if (!languageToLoad.equalsIgnoreCase("ku")) {
					if (split.equalsIgnoreCase("wate")) {
						split += " / " + getText(R.string.Wate) + ":";
					} else if (split.equalsIgnoreCase("binihêre")
							|| split.equalsIgnoreCase("binere")) {
						split += " / " + getText(R.string.Binihere) + ":";
					} else if (split.equalsIgnoreCase("bibîne")) {
						split += " / " + getText(R.string.Bibine) + ":";
					} else if (split.equalsIgnoreCase("dijwate")) {
						split += " / " + getText(R.string.Dijwate) + ":";
					} else if (split.equalsIgnoreCase("têkîldar")
							|| split.equalsIgnoreCase("nêzîk")) {
						split += " / " + getText(R.string.Tekildar) + ":";
					} else if (split.equalsIgnoreCase("hevwate")) {
						split += " / " + getText(R.string.Hevwate) + ":";
					} else if (split.equalsIgnoreCase("herwiha")) {
						split += " / " + getText(R.string.herwiha) + ":";
					} else if (split.equalsIgnoreCase("Bi alfabeyên din")) {
						split += " / " + getText(R.string.bialfabeyendin) + ":";
					} else if (split.equalsIgnoreCase("ji")) {
						split += " / " + getText(R.string.Ji) + ":";
					} else if (split.equalsIgnoreCase("bide ber")) {
						split += " / " + getText(R.string.Bideber) + ":";
					} else if (split.equalsIgnoreCase("jê")) {
						split += " / " + getText(R.string.Je) + ":";
					} else if (split.equalsIgnoreCase("wergerr")) {
						split += " / " + getText(R.string.werger) + ":";
					}else if (split.equalsIgnoreCase("werger")) {
						split += " / " + getText(R.string.werger) + ":";
					}
					else if (split.equalsIgnoreCase("bikaranîn")
							|| split.equalsIgnoreCase("bi kar anîn")) {
						split += " / " + getText(R.string.bikaranin) + ":";
					} else if (split.equalsIgnoreCase("ji wêjeyê")) {
						split += " / " + getText(R.string.jiwejeye) + ":";
					} else if (split.equalsIgnoreCase("Tewandin")
							|| split.equalsIgnoreCase("Tewîn")) {
						split += " / " + getText(R.string.tewandin) + ":";
					} else if (split.equalsIgnoreCase("Bi zaravayên kurdî")) {
						split += " / " + getText(R.string.bizaravayendin) + ":";
					}
				}
				newtext += split;
			} else
				newtext += split;
			if (i != splits.length - 1)
				newtext += "\n";
		}

		return newtext;

	}
	public Words GetSingleExactWord(String word) {
		Cursor cursor = null;
		Boolean resulted = false;
		String normalized=word;
	//	String rid="";
		Words wreturn = null;
		SaveScrollPosition();
		if (word.equalsIgnoreCase("")) 
		{
		} 
		else
		{
			normalized=WQDictionaryDBOpenHelper. Normalize(word);
			cursor =provider. GetCursor(WQDictionaryDB.KEY_WORD_N + " match ? ", normalized);
		}
		if (cursor == null) 
		{
			return null;
		}
		CollapseExpandableListView();
		mGroupCollection.clear();
		Boolean find = false;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String wordd = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD);
				String wordd_n = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
				if((wordd==null||wordd.equalsIgnoreCase(""))&&word.equalsIgnoreCase(normalized))
					wordd=wordd_n;
				if (wordd.equals(word)) {
					String id= WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_ID);
					Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
					if(w!=null)
					{
					String def=Decode(w.getwate(), wordd, wordd_n);
					def=def.replace(",", ", ");
					listviewresult.setVisibility(View.GONE);
					mExpandableListView.setVisibility(View.VISIBLE);
					
					SetExpanderCollection(word, def);
					defaultWordedSplashed = true;
					wreturn=new Words();
					wreturn.id=id;
					wreturn.peyv=word;
					wreturn.wate=def;
					find = true;
					break;
					}
				}
				cursor.moveToNext();
			}
			if (find) {
				resulted = true;
				ExpandWithTimer();
				
			} else {
				if (cursor.getCount() > 0) {
					resulted = true;
					mExpandableListView.setVisibility(View.GONE);
					listviewresult.setVisibility(View.VISIBLE);
					imageButtonFav.setVisibility(View.GONE);
					final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
					ArrayList<Words> listofWords = new ArrayList<Words>();
					SearchResultAdapter adapter = new SearchResultAdapter(this, list,
							R.layout.result, columnsDB, to);

					if (cursor != null) {
						cursor.moveToFirst();
						while (!cursor.isAfterLast())
						{
							String peyv=WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD);
							String id=WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_ID);
							if(peyv==null||peyv.equals(""))
								peyv=WQDictionaryQueryProvider. GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
							String  def=WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_DEFINITION);
							list.add(putData(
									peyv,
									def));
							Words ww=new Words();
							ww.peyv=peyv;
							ww.wate=def;
							ww.id=id;
							listofWords.add(ww);
							cursor.moveToNext();
						}
						adapter.listOfWords=listofWords;
						listviewresult.setAdapter(adapter);
						WQDictionaryActivity.listofWords=listofWords;
						UpdateAnimatedButtonVisibilities(true);
					}
					listviewresult.requestFocus();
				}

			}
		}
		if (resulted) {
			HideKeyboard();

		}
	
		return wreturn;

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
			if (SearchHistory.size() > 1 &&IsButtonsVisible)
				imageButtonBack.setVisibility(View.VISIBLE);
			imageButtonForward.setVisibility(View.GONE);
		}
		for(int x=0; x<SearchHistory.size();x++)
		{
			SearchItem itemx=SearchHistory.get(x);
		//	Log.d("Hist", itemx.Query+" "+itemx.Index);
		}
	}
	public void makeText(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
	}


	public String Reverse(String str, Boolean suppressIsArabic) {
		if (suppressIsArabic) {
			if (str.toLowerCase().startsWith("werger")
					|| str.toLowerCase().startsWith("Binihêre")
					|| str.toLowerCase().startsWith("Dijwate")
					|| str.toLowerCase().startsWith("Têkildar")
					|| str.toLowerCase().startsWith("Bide ber")
					|| str.toLowerCase().startsWith("Bibîne"))
				return str;
			String[] lines = str.split(java.util.regex.Pattern.quote("\n"));
			String text = "";
			for (int li = 0; li < lines.length; li++) {
				String line = lines[li];
				line = line.toLowerCase();
				String[] words = line.split(java.util.regex.Pattern.quote(" "));
				for (int kc = 0; kc < words.length; kc++) {
					String word = words[kc];
					String subword = "";
					List<String> syyllable = new ArrayList<String>();
					if (word.length() > 3)
						syyllable = SyllableCount(word);
					else
						syyllable.add(word);
					if (syyllable != null) {
						for (int i = 0; i < syyllable.size(); i++) {
							String syl = syyllable.get(i);
							if (syl.equalsIgnoreCase("û")) {
								subword += "و";
								continue;
							}
							for (int x = 0; x < syl.length(); x++) {
								Boolean Isadded = false;
								char item = syl.charAt(x);
								if (x > 0) {
									if (item == 'i') {
										if ((x == syl.length() - 1)
												&& (i == syyllable.size() - 1)) {
											subword += "ی";
											Isadded = true;
										} else
											continue;
									} else if (item == 'e') {
										subword += "ە";

										Isadded = true;
									}

									else if (item == 'a') {
										subword += "ا";

										Isadded = true;
									}
								}
								if (!Isadded) {
									if (item == 'i' || item == 'î') {
										subword += "ئی";
										Isadded = true;
										continue;
									}
									Letter letter = null;
									for (int k = 0; k < alphabet.Letters.size(); k++) {
										Letter l = alphabet.Letters.get(k);
										if (l.Nav.charAt(0) == item) {
											letter = l;
											break;
										}
									}
									if (letter != null) {
										if (letter != null)
											subword += letter.Beramber;
									} else {
										subword += item;
									}
								}
							}
						}
					}
					if (subword.contains(java.util.regex.Pattern.quote("رر"))) {
						subword = subword.replace("رر", "ڕ");
					}
					text += subword;
					if (kc != (words.length - 1))
						text += " ";
				}
				if (li != (lines.length - 1))
					text += "\n";
			}
			return text;
		} else {
			return str;
		}
	}

	private List<String> SyllableCount(String word) {
		word = word.toLowerCase().trim();
		Boolean lastWasVowel = false;
		List<String> syllableList = new ArrayList<String>();
		String syllabla = "";
		int vowelcount = 0;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			Boolean Istwoconstant = ((word.length() - i) > 2 && (!IsVowel(word
                    .charAt(i + 2))));
			if (IsVowel(c)) {
				vowelcount++;
				if (!lastWasVowel) {
					syllabla += c;
					if (Istwoconstant) {
						char xc = word.charAt(i + 1);
						syllabla += xc;
						i++;
					}
					syllableList.add(syllabla);
					syllabla = "";
				}
				lastWasVowel = true;

			} else {

				if ((i == word.length() - 1) && syllableList.size() > 0) {
					String Item = syllableList.get(syllableList.size() - 1);
					Item += c;
					syllableList.set(syllableList.size() - 1, Item);
				} else
					syllabla += c;
				lastWasVowel = false;
			}

		}
		if (vowelcount == 1) {
			syllableList.clear();
			syllableList.add(word);
		}
		return syllableList;
	}

	private Boolean IsVowel(char c) {
		Boolean isvowel = false;
		if (c == 'a' || c == 'e' || c == 'ê' || c == 'i' || c == 'o'
				|| c == 'u' || c == 'î' || c == 'û' || c == 'A' || c == 'E'
				|| c == 'Ê' || c == 'I' || c == 'O' || c == 'U' || c == 'Î'
				|| c == 'Û')
			isvowel = true;
		return isvowel;

	}

	public String Alphabetize() {
		IsArabic = !IsArabic;

		String search = autoCmopletetextView.getText().toString();
		if (!search.equalsIgnoreCase(""))
			GetSingleExactWord(search);
		else if (SearchHistory != null && SearchHistory.size() > 0) {
			SearchItem searchItem = SearchHistory.get(SearchHistory.size() - 1);
			if (searchItem.SearchType.equalsIgnoreCase("Exact")) 
			{
				
				autoCmopletetextView.setText(searchItem.Query);
				GetSingleExactWord(searchItem.Query);

			}
		}

		if (!IsArabic)
			return getString(R.string.buttonarabic);
		else
			return getString(R.string.buttonlatin);
	}

	public void HideKeyboard() {
		if (!showarabickeyboard) {

			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					autoCmopletetextView.getWindowToken(), 0);
		} else {
			mCustomKeyboard.hideCustomKeyboard();
		}

	}

	public void ShowButton(String string) {
		if (string == "Back") {
			if (SearchItemIndex <= SearchHistory.size() - 1)
				SearchItemIndex = SearchHistory.size();
			imageButtonBack.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onBackPressed() {
		// NOTE Trap the back key: when the CustomKeyboard is still visible hide
		// it, only when it is invisible, finish activity
		super.onBackPressed();
		if (WQDictionaryDBOpenHelper.Loading || upgrating) {
			moveTaskToBack(true);
			return;
		}

		if (mCustomKeyboard != null) {
			if (mCustomKeyboard.isCustomKeyboardVisible())
				mCustomKeyboard.hideCustomKeyboard();
			else {
				SaveSearchHistory();
				this.finish();
			}
		} else {
			SaveSearchHistory();
			this.finish();
		}
	}

	private void SaveSearchHistory() {
		StringBuilder sb = new StringBuilder();
		

		SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
				Context.MODE_PRIVATE);
		// String sOld=sharedPref.getString("SearchHistory", "");
		Editor edit = sharedPref.edit();
		// sOld=sb.toString()+sOld;
		edit.putString("WQDictionary_" + "SearchHistory", autoCmopletetextView
				.getText().toString());
		edit.commit();
		// TODO Auto-generated method stub
	}

	private String GetSavedSearchHistory() {
		SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
				Context.MODE_PRIVATE);
		String sOld = sharedPref.getString("WQDictionary_" + "SearchHistory", "");

		return sOld;
	}

	@Override
	protected void onStop() {
		super.onStop();
		HideKeyboard();
	}

	@Override
	protected void onPause() {
		super.onPause();
		HideKeyboard();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (showarabickeyboard) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			mCustomKeyboard.showCustomKeyboard(autoCmopletetextView);
		}

	}



	public static void dotext(String query) {
		// TODO Auto-generated method stub
		Toast.makeText(cont, query, Toast.LENGTH_LONG).show();
	}

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_wqdictionary, menu);

		if (actionBarIsEnabled) {
			try {
				MenuItem itemaction_erebic = menu.findItem(R.id.action_erebic);
				// MenuItem itemaction_history =
				// menu.findItem(R.id.action_history);
				MenuItem itemaaction_erebickeyboard = menu
						.findItem(R.id.action_erebickeyboard);
				MenuItem itemaction_list = menu.findItem(R.id.action_list);
				itemaction_erebic
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				itemaaction_erebickeyboard
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				itemaction_list.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
				MenuItem itemaction_fav = menu.findItem(R.id.action_fav);
				
				itemaction_fav
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				// itemaction_history.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

				if (disablemenu)
				{
					itemaction_erebic.setEnabled(false);
					itemaction_erebic.getIcon().setAlpha(80);
					itemaaction_erebickeyboard.setEnabled(false);
					itemaaction_erebickeyboard.getIcon().setAlpha(80);
					itemaction_list.setEnabled(false);
					itemaction_list.getIcon().setAlpha(80);
					itemaction_fav.setEnabled(false);
					itemaction_fav.getIcon().setAlpha(80);
					// itemaction_history.setEnabled(false);
					// itemaction_history.getIcon().setAlpha(80);

				} else {
					itemaction_erebic.setEnabled(true);
					itemaction_erebic.getIcon().setAlpha(255);
					itemaaction_erebickeyboard.setEnabled(true);
					itemaaction_erebickeyboard.getIcon().setAlpha(255);
					itemaction_list.setEnabled(true);
					itemaction_list.getIcon().setAlpha(255);
					itemaction_fav.setEnabled(true);
					itemaction_fav.getIcon().setAlpha(255);
				}
				if(wiki.equalsIgnoreCase("tr"))
				{
					itemaction_erebic.setVisible(false);
					itemaaction_erebickeyboard.setVisible(false);
				}

			} catch (Exception exc) {

			}

		} else {
			MenuItem itemaction_erebic = menu.findItem(R.id.action_erebic);
			MenuItem action_erebickeyboard = menu
					.findItem(R.id.action_erebickeyboard);
			itemaction_erebic.setVisible(false);
			action_erebickeyboard.setVisible(false);

			MenuItem action_list = menu.findItem(R.id.action_list);
			action_list.setVisible(true);
			
			MenuItem itemaction_fav = menu.findItem(R.id.action_fav);
			

			if (disablemenu) {
				itemaction_erebic.setEnabled(false);
				itemaction_erebic.getIcon().setAlpha(80);
				action_erebickeyboard.setEnabled(false);
				action_erebickeyboard.getIcon().setAlpha(80);
				action_list.setEnabled(false);
				action_list.getIcon().setAlpha(80);
				itemaction_fav.setEnabled(false);
				itemaction_fav.getIcon().setAlpha(80);
			

			} else {
				itemaction_erebic.setEnabled(true);
				itemaction_erebic.getIcon().setAlpha(255);
				action_erebickeyboard.setEnabled(true);
				action_erebickeyboard.getIcon().setAlpha(255);
				action_list.setEnabled(true);
				action_list.getIcon().setAlpha(255);
				itemaction_fav.setEnabled(true);
				itemaction_fav.getIcon().setAlpha(255);
			}

		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

//
//		if (R.id.derbar == item.getItemId()) {
//			//showInfo();
//			showInfoActivity();
//			return true;
//		}
		if (R.id.action_list == item.getItemId()) {
			//case R.id.action_list:
			ViewWordList();
			return true;
		}
		if (R.id.action_erebic == item.getItemId()) {
			//case R.id.action_erebic:
			Alphabetize();
			if (actionBarIsEnabled) {
				if (IsArabic)
					item.setIcon(R.drawable.kurdill);
				else
					item.setIcon(R.drawable.kurdiee);
			}
			return true;
		}
		if (R.id.action_erebickeyboard == item.getItemId()) {
			//case R.id.action_erebickeyboard:
			ResetArabicKeyboard();
			if (actionBarIsEnabled) {
				if (showarabickeyboard)
					item.setIcon(R.drawable.keyboarda);
				else
					item.setIcon(R.drawable.keyboarde);
			}

			return true;
		}
		if (R.id.action_fav == item.getItemId()) {
			//case R.id.action_fav:
			SetFavList();

			return true;
		}
//		if (R.id.ziman == item.getItemId()) {
//			//case R.id.ziman: {
//			showLanguageChangeDialog();
//			return true;
//		}
//		if( R.id.action_Parveke==item.getItemId()) {
//		//case R.id.action_Parveke: {
//			ShareThisWord();
//			return true;
//		}
		if( R.id.action_eyar==item.getItemId()) {
			//case R.id.action_Parveke: {
			GoToSettings();
			return true;
		}
			
		else
		{
			return super.onOptionsItemSelected(item);

		}
	}
	private void SetFavList() {
		// TODO Auto-generated method stub
		final ArrayList<Words> listofFavwords = WQDictionaryDB.mWQDictionaryDBOpenHelper
				.GetFavWords();
		if (listofFavwords != null && listofFavwords.size() > 0) {
			final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (int ix = 0; ix < listofFavwords.size(); ix++) {
				Words w = listofFavwords.get(ix);
				list.add(putData(w.peyv, ""));
			}
			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.result, columnsDB, to) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View v = convertView;
					final SearchResultAdapter.ViewHolderWords holder;
					if (v == null) {
						holder = new ViewHolderWords();
						LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						v = vi.inflate(R.layout.result_fav, null);
						holder.layoutRow = (LinearLayout) v
								.findViewById(R.id.layoutRow);
						holder.textViewWord = (TextView) v
								.findViewById(R.id.word);
						holder.button = (ImageButton) v
								.findViewById(R.id.imgFav);
						// holder.textViewdef = (TextView)
						// v.findViewById(R.id.definition);
						// other stuff
						v.setTag(holder);
					} else {
						holder = (ViewHolderWords) v.getTag();
					}

					Map<String, String> data = list.get(position);
					String word = data.get(WQDictionaryDB.KEY_WORD);
					//String definition = data.get(WQDictionaryDB.KEY_DEFINITION);

					holder.textViewWord.setText(word);
					holder.textViewWord.setTag(position);
					holder.button.setTag(word);
					holder.button.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {

							switch (event.getAction()) {
							case MotionEvent.ACTION_DOWN: {
								ImageView view = (ImageView) v;
								// overlay is black with transparency of 0x77
								// (119)
								view.getDrawable().setColorFilter(0x77000000,
										PorterDuff.Mode.SRC_ATOP);
								view.invalidate();
								break;
							}
							case MotionEvent.ACTION_UP: {
								// int pos=(Integer)v.getTag();
								ImageView view = (ImageView) v;
								if(view!=null&&view.getTag()!=null)
								{
									String text=view.getTag().toString();
									AddRemoveFromFavList(getBaseContext(),text, false);
									SetFavList();
								}
							}
							case MotionEvent.ACTION_CANCEL: {
								ImageView view = (ImageView) v;
								// clear the overlay
								view.getDrawable().clearColorFilter();
								view.invalidate();
								break;
							}
							}

							return false;
						}
					});
					holder.layoutRow.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							SearchResultAdapter.ViewHolderWords holder = (SearchResultAdapter.ViewHolderWords) v.getTag();
						String word=	holder.textViewWord.getText().toString();
						Intent intent = new Intent(cont, DefinitionActivity.class);
						int i=(Integer) holder.textViewWord.getTag();

						Bundle b = new Bundle();
						
						//String word=w.peyv;
					   listofWords=listofFavwords;
						if(word!=null)
							b.putString("word", word);
						
						b.putInt("position", i);
						intent.putExtras(b);

						cont.startActivity(intent);
					//	GetSingleExactWord(word);
						}

					});
					return v;
				}

			};

			mExpandableListView.setVisibility(View.GONE);
			listviewresult.setVisibility(View.VISIBLE);
			imageButtonFav.setVisibility(View.GONE);
			listviewresult.setAdapter(adapter);

		}
		else
		{
			makeText("Favori listeniz boş");
		}

	}
	public static void AddRemoveFromFavList(Context cont, String word,  Boolean addorRemove) {
		// TODO Auto-generated method stub
		String res="";
		Log.d("word",word);
		if(addorRemove)
		{			
		 res=	WQDictionaryDB.mWQDictionaryDBOpenHelper.InsertFavWord(cont, word);	
		}
		else
		{
			res=WQDictionaryDB.mWQDictionaryDBOpenHelper.DeleteFavWord(word);
		}

		Toast.makeText(cont, res, Toast.LENGTH_SHORT).show();
	}
	private void ShareThisWord() 
	{
		String word=autoCmopletetextView
				.getText().toString();
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
				+ "http://tr.wiktionary.org/wiki/"+wordw+ " \n\n";
		i.putExtra(Intent.EXTRA_TEXT, sAux);
		startActivity(Intent.createChooser(i,
				getString(R.string.select)));
		
	}

	private void showInfoActivity() 
	{
		Intent intent = new Intent(this, AboutActivity.class);

		startActivity(intent);
		// TODO Auto-generated method stub
		
	}


	private void SaveScrollPosition() {

		SearchItem sc = currentItem;
		if (sc == null) {
			return;
		}

		if (sc.SearchType.equalsIgnoreCase("Search")) {
			if (listviewresult.getVisibility() != View.VISIBLE) {
				return;
			}
			Bundle state = new Bundle();
			Parcelable mListState = listviewresult.onSaveInstanceState();
			state.putParcelable(LIST_STATE_KEY, mListState);
			sc.State = state;
		} else {
			ExpandableListView listView = mExpandableListView;
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
			if (listviewresult.getVisibility() != View.VISIBLE)
				return;
			Parcelable mListState = state.getParcelable(LIST_STATE_KEY);
			listviewresult.onRestoreInstanceState(mListState);
		} else {
			ExpandableListView listView = mExpandableListView;
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

	private long[] getExpandedIds() {
		ExpandableListView list = mExpandableListView;

		if (adapter != null) {
			int length = adapter.getGroupCount();
			ArrayList<Long> expandedIds = new ArrayList<Long>();
			for (int i = 0; i < length; i++) {
				if (list.isGroupExpanded(i)) {
					expandedIds.add(adapter.getGroupId(i));
				}
			}
			return toLongArray(expandedIds);
		} else {
			return null;
		}
	}

	private static long[] toLongArray(List<Long> list) {
		long[] ret = new long[list.size()];
		int i = 0;
		for (Long e : list)
			ret[i++] = e.longValue();
		return ret;
	}

	private void restoreExpandedState(long[] expandedIds) {

		if (expandedIds != null) {
			ExpandableListView list = mExpandableListView;

			if (adapter != null) {
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					long id = adapter.getGroupId(i);
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

	@SuppressLint({ "NewApi", "NewApi" })
	private void EnableLayoutChildrens(ViewGroup vg, Boolean enable) {

		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (child instanceof TextProgressBar)
				continue;
			child.setEnabled(enable);
			if (actionBarIsEnabled) {
				if (enable)
					child.setAlpha((float) 1);
				else
					child.setAlpha((float) 0.5);
			}
			if (child instanceof ViewGroup) {
				EnableLayoutChildrens((ViewGroup) child, enable);
			}
		}

	}
	public void UpDateFooterView(String word)
	{		
		if(!word.equalsIgnoreCase(""))
		{
			imageButtonGoToWiki.setTag(word);
			if(IsButtonsVisible)
				imageButtonGoToWiki.setVisibility(View.VISIBLE);
		}
	}
	public static Boolean IsConnected()
	{
		Boolean connected=false;
		ConnectivityManager cm = null;
		if (cont != null) 
		{
			cm = (ConnectivityManager) cont
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		} else if(mContext!=null) {
			Toast.makeText(mContext, "No Connection", Toast.LENGTH_LONG).show();
		}
		//if (cm.getActiveNetworkInfo() != null
		//		&& cm.getActiveNetworkInfo().isConnected())
		//{
			connected=true;	
		//}
		//else
		{
			Log.d("null", "null");

		}
		return connected;
	}

	private void AddFooterView() {
		imageButtonGoToWiki= (ImageButton) this.findViewById(R.id.imageButtonGoToWiki);
//		footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//				.inflate(R.layout.list_footer, null, false);
//		footerButton = (Button) footerView.findViewById(R.id.liwikiferheng);
		imageButtonGoToWiki.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!IsConnected()) {
					Toast.makeText(cont, "No Connection", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if(imageButtonGoToWiki.getTag()==null)
					return;
				String url = "https://ku.wiktionary.org/wiki/"
						+ imageButtonGoToWiki.getTag().toString();
				if (!url.startsWith("http://") && !url.startsWith("https://"))
					url = "http://" + url;

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				cont.startActivity(browserIntent);
			}
		});
		imageButtonGoToWiki.setVisibility(View.GONE);
		//mExpandableListView.addFooterView(footerView);
	}
	public void ReOrderHistory() {
		// TODO Auto-generated method stub		

			    Collections.sort(SearchHistory, new Comparator<SearchItem>() {
			        @Override public int compare(SearchItem p1, SearchItem p2) {
			            return p1.Index - p2.Index; // Ascending
			        }

			    });
			    SearchItemIndex = SearchHistory.size()-1;
			    
			    for(int x=0; x<SearchHistory.size();x++)
				{
					SearchItem itemx=SearchHistory.get(x);
					//Log.d("Hist", itemx.Query+" "+itemx.Index);
				}
	}
	private OnSwipeTouchListener GetOnSwipeListener() {
		// TODO Auto-generated method stub
		if(onSwipeListener!=null)
			return onSwipeListener;
		onSwipeListener=	new OnSwipeTouchListener() {
			
	        public boolean onSwipeTop() {
	            return false;
	        }
	        public boolean onSwipeRight() 
	        {
	        	
//	        	if(WQDictionaryActivity.listofWords!=null)
//	        	{
//	        		if(position>0)
//	        		{
//	    	        	position--;	        		
//	    	        	MoveToPostion(false);
//	        		}
//	        	}
//	        
	            return true;
	        }
	        public boolean onSwipeLeft() 
	        {
	        	
//	        	if(SearchHistory.size()>0)
//	        	{
//	        		if(SearchItemIndex<SearchHistory.size()-1)
//	        		{
//	        			SearchItemIndex++;
//	        	
//	    	        
//	        		}
//	        	}
	        
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
					if(!adapter.handleClick)
					{
						adapter.handleClick=true;
						return;
					}
					UpdateAnimatedButtonVisibilities(!IsButtonsVisible);

				}
			}
   
	    };
	    return onSwipeListener;
	}
	private void UpdateAnimatedButtonVisibilities(Boolean showbuttons)
	{
		RelativeLayout rLayoutButtons =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
		if (showbuttons) 
		{
			if(SearchHistory.size()>0&&	SearchItemIndex > 0)
				slideToLeft(imageButtonBack, 0, View.VISIBLE, 400);

			if(SearchHistory.size()>0&&	SearchItemIndex < SearchHistory.size()-1)
				slideToRight(imageButtonForward, 0, View.VISIBLE);
			if(mExpandableListView.getVisibility()==View.VISIBLE)
			{
			slideToBottom(imageButtonFav,0,View.VISIBLE);
			slideToBottom(imageButtonGoToWiki,0,View.VISIBLE);
			}
//			if(mAdView!=null)
//			slideToBottom(mAdView,0,View.VISIBLE);
			
			IsButtonsVisible=true;
			//adapter.handleClick=true;
			
			
//			 RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); 
//	            params.addRule(RelativeLayout.ABOVE, R.id.adView);	
//	            rLayoutButtons.setLayoutParams(params);
//			RelativeLayout.LayoutParams llParams = (RelativeLayout.LayoutParams)layoutListViewContents.getLayoutParams();
//			llParams.addRule(RelativeLayout.ABOVE, mAdView.getId());
			
		} 
		else 
		{						
			slideToLeft(imageButtonBack, -imageButtonBack.getWidth(), View.GONE, 300);
			slideToRight(imageButtonForward, imageButtonForward.getWidth(), View.GONE);
			
			slideToBottom(imageButtonFav,imageButtonFav.getHeight(),View.GONE);
			slideToBottom(imageButtonGoToWiki,imageButtonGoToWiki.getHeight(),View.GONE);
//			if(mAdView!=null)
//				slideToBottom(mAdView,mAdView.getHeight(),View.GONE);
			
			 
	            
	        	
//				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT); 
//				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//			     layoutParams.setMargins(0, 0, 0, 30);
//			     
//			     rLayoutButtons.setLayoutParams(layoutParams);
			
			IsButtonsVisible=false;
			adapter.handleClick=true;
		}
	
	}
	 public void slideToLeft(View view, int width, int visibility, int duration){
    	TranslateAnimation animate = new TranslateAnimation(0,width,0,0);
    	animate.setDuration(duration);
    	//animate.setFillAfter(true);
    	view.startAnimation(animate);
    	view.setVisibility(visibility);
    	}
    
    public void slideToRight(View view, int width, int visibility){
    	TranslateAnimation animate = new TranslateAnimation(0,width,0,0);
    	animate.setDuration(500);
    	//animate.setFillAfter(true);
    	view.startAnimation(animate);
    	view.setVisibility(visibility);
    	}
    public void slideToBottom(View view, int height, int visibility){
    	TranslateAnimation animate = new TranslateAnimation(0,0,0,height);
    	animate.setDuration(500);
    	//animate.setFillAfter(true);
    	view.startAnimation(animate);
    	view.setVisibility(visibility);
    	}
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev){
//        super.dispatchTouchEvent(ev);    
//        return mExpandableListView.onTouchEvent(ev); 
//    }

}
