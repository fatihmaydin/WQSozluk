package com.wqworterbuch;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wqworterbuch.GroupEntity.GroupItemEntity;
import com.wqworterbuch.SearchResultAdapter.ViewHolderWords;
import com.wqworterbuch.WQDictionaryDB.WQDictionaryDBOpenHelper;


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
	RelativeLayout relativeLayoutBottom;
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
	public static String languageToLoad = "de";
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
		if(currentVersionNumber>0)
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
				actionBar.setIcon(R.drawable.wqwoerterbuch); // Your icon resource

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

						SelectedWord = SelectedWord.replace(".", "").replace(",", "")
								.trim();
						String wordDecoded=SelectedWord;
						if(SelectedWord.contains("^")&&w.NormalizedWord.length()!=SelectedWord.length())
							wordDecoded=ReplaceEncodedChars(wordDecoded,ww.NormalizedWord);
						worddef=Decode(ww.getwate(), wordDecoded,ww.NormalizedWord);
						worddef=worddef.replace(",", ", ");
						autoCmopletetextView.setText(wordDecoded);

						HideKeyboard();
						listviewresult.setAdapter(null);

						mExpandableListView.setVisibility(View.VISIBLE);
						worddef = ReplaceTempChars(worddef);

						SetExpanderCollection(wordDecoded, worddef);
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
		relativeLayoutBottom=(RelativeLayout) findViewById(R.id.relativeLayoutBottom);
		
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
			//final RelativeLayout relativeLayout1 =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
			com.google.android.gms.ads.	AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
			if(adRequest!=null&&mAdView!=null)
			{
				//makeText(mAdView.toString());
				Log.d(TAG, "Admob request");
				mAdView.loadAd(adRequest);
				mAdView.setVisibility(View.VISIBLE);
//				RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) relativeLayout1.getLayoutParams();
//				params.removeRule(RelativeLayout.ABOVE);
//				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//				relativeLayout1.setLayoutParams(params);
				//relativeLayoutBottom.getLayoutParams().height=1;
				//mAdView.setVisibility(View.GONE);
				mAdView.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						super.onAdLoaded();
//						RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//						params.addRule(RelativeLayout.ABOVE, R.id.mAdView);
						//relativeLayoutBottom.getLayoutParams().height=50;
						mAdView.setVisibility(View.VISIBLE);
						mAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
						Log.d(TAG, "Admob loaded");
					}

					@Override
					public void onAdClosed() {
						super.onAdClosed();
						//relativeLayoutBottom.getLayoutParams().height=1;
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

			languageToLoad = prefs.getString("Lang", "de");
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
			Log.d("word","word_n:"+word_n +"  "+"word:"+word );
				if(word==null||word.equalsIgnoreCase(""))
				{
					word=word_n;
					//Log.d("word","word null" );
				}
				if(word.contains("^")&&word_n.length()>word.length())
				{
					word=WQDictionaryActivity. ReplaceEncodedChars(word, word_n);
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
					String d_N=WQDictionaryQueryProvider.GetValue(c,  WQDictionaryDB.KEY_WORD_N);;
					if(d==null||d.equalsIgnoreCase(""))
						d= WQDictionaryQueryProvider.GetValue(c,  WQDictionaryDB.KEY_WORD_N);

					String wordDecoded=d;
					wordDecoded=ReplaceEncodedChars(d,d_N);
					resulted.peyv=wordDecoded;
					//makeText(d+",  "+d_N+" dec:"+wordDecoded);
					autoCmopletetextView.setText(wordDecoded);
			
					AddSearchItem(wordDecoded, resulted.id
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
			if(SelectedWord.contains("^")&&selectedWord_n.length()!=SelectedWord.length()) {
				SelectedWord=ReplaceEncodedChars(SelectedWord,selectedWord_n);
				//makeText("Clicked:" + SelectedWord + "," + selectedWord_n);
			}
				//Date currentDate1 = new Date();

		Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
		if(w!=null)
		{

			rword=new Words();
			rword.id=id;
			rword.wate=worddef;
			String wordDecoded=SelectedWord;

			worddef=Decode(w.getwate(), wordDecoded, selectedWord_n);
			worddef=worddef.replace(",", ", ");
			//Log.d("Loaded", diffInMs +" msec");
			worddef = ReplaceTempChars(worddef);
			SetExpanderCollection(wordDecoded, worddef);
			
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
	            .setSmallIcon(R.drawable.wqwoerterbuch)
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
	public static String ReplaceEncodedChars(String wordd, String word_n)
	{

		String toreplace=wordd.replace("^","");
		//Toast.makeText(mContext.getBaseContext(), toreplace,Toast.LENGTH_LONG).show();
		if(toreplace.equals("ß")) {
			wordd = word_n.replace("ß","ss");
		}
		else if(toreplace.equals("ö")) {
			wordd = word_n.replace("o","ö");
		}
		else if(toreplace.equals("Ö")) {
			wordd = word_n.replace("O","Ö");
		}
		else if(toreplace.equals("ü")) {
			wordd = word_n.replace("u","ü");
		}
		else if(toreplace.equals("Ü")) {
			wordd = word_n.replace("U","Ü");
		}
		else if(toreplace.equals("Ä")) {
			wordd = word_n.replace("A","Ä");
		}
		else if(toreplace.equals("ä")) {
			wordd = word_n.replace("a","ä");
		}
		else if(toreplace.equals("ää")) {
			wordd = word_n.replace("a","ä");
		}
		else if (wordd.contains("★")||word_n.contains("★"))
		{
			wordd=word_n.replace("★","*");
			word_n=word_n.replace("★","*");
		}
		return wordd;
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
					//makeText("SetListaadapter1");
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
		

//		if(word!=null)
//		{
//			strToreturn=strToreturn.replace("^@", word);
//			//Log.d("wordddd",word);
//		}
//		if(normalize!=null)
//		{
//			//Log.d("normalize",normalize);
//			strToreturn=strToreturn.replace("@^", normalize);
//		}
	//	Log.d("strToreturn",strToreturn);
		for (Entry<String, String> entry : decoderList.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
			Log.d("Decoding:"+key, value);
    		strToreturn=strToreturn.replace(value, key);
    	}
		if(normalize!=null&&!word.equalsIgnoreCase(normalize))
		{
			//Log.d("normalize",normalize);
			strToreturn=strToreturn.replace("^@", word);
			strToreturn=strToreturn.replace("@^", normalize);
			if(strToreturn.contains("&_"))
			{
				strToreturn=strToreturn.replace("&_",normalize.replace(" ","_"));
			}
			if(strToreturn.contains("&~"))
			{
				strToreturn=strToreturn.replace("&~",word.replace(" ","_"));
			}
		}
		else
		{
			if(word!=null) {
				strToreturn = strToreturn.replace("@^", word);
				strToreturn = strToreturn.replace("^@", word);
			}
			if(strToreturn.contains("&_"))
			{
				strToreturn=strToreturn.replace("&_",normalize.replace(" ","_"));
			}
			if(strToreturn.contains("@_"))
			{
				strToreturn=strToreturn.replace("@_",normalize.replace(" ","_"));
			}
			if(strToreturn.contains("&~"))
			{
				strToreturn=strToreturn.replace("&~",word.replace(" ","_"));
			}
		}
		if(word!=null) {
			strToreturn = ReplaceEncodedHeader(strToreturn, word);
		}
		strToreturn=strToreturn.replace(",", ", ")
				.replace("+", " + ").replace(":", ": ").trim();
		//Log.d("strtoreturn", strToreturn);
		return strToreturn;
	}
	public static String ReplaceEncodedHeader(String text, String word)
	{
		String strNewTex=text;
		Pattern regexStrSubWord = Pattern.compile("\\@r\\-[0-9]");
		Matcher regexMatcherstrSub = regexStrSubWord.matcher(text);
		String reversed="";
		//Log.d("strtoreturn", text+", "+word);
		String[] wordsplits=word.split(" ");
		for(int x=wordsplits.length-1; x>=0;x--)
		{
				if(wordsplits[x].trim()=="")
				{
					continue;
				}
			reversed+=wordsplits[x];
		}
		if(reversed!="") {
			int t=0;
			while (regexMatcherstrSub.find()) {
				String textMatc = regexMatcherstrSub.group();
				int num = 1;

				if (textMatc.contains("-")) {
					String sub = textMatc.substring(textMatc.indexOf("-") + 1);
					num = Integer.parseInt(sub);
				}
				String strSubToReplace = reversed.substring(0, reversed.length() - num);
				//Log.d("strtoreturn", strNewTex+", "+strSubToReplace+", "+", "+textMatc);
				//Toast.makeText(mContext, "textMatc:"+textMatc+" : "+" sub:"+strSubToReplace, Toast.LENGTH_LONG).show();;
				strNewTex = strNewTex.replaceAll(textMatc, strSubToReplace);
				regexMatcherstrSub = regexStrSubWord.matcher(strNewTex);
				t++;
				if(t>20)
					break;
			}
		}
		//strNewTex=text;
		regexStrSubWord = Pattern.compile("@\\-[0-9]");
		regexMatcherstrSub = regexStrSubWord.matcher(text);
		while (regexMatcherstrSub.find()) {
			String textMatc=regexMatcherstrSub.group();
			int num=1;
			if(textMatc.contains("-")) {
				String sub = textMatc.substring(textMatc.indexOf("-")+1);
				num=Integer.parseInt(sub);
			}
			String strSubToReplace=word.substring(0, word.length()-num);
			//Toast.makeText(mContext, "textMatc:"+textMatc+" : "+" sub:"+strSubToReplace, Toast.LENGTH_LONG).show();;
			strNewTex=strNewTex.replace(textMatc,strSubToReplace );
			regexMatcherstrSub = regexStrSubWord.matcher(strNewTex);
		}
		regexStrSubWord = Pattern.compile("[0-9]\\-Ĭ");
		regexMatcherstrSub = regexStrSubWord.matcher(text);
		while (regexMatcherstrSub.find()) {
			String textMatc=regexMatcherstrSub.group();
			int num=1;
			if(textMatc.contains("-")) {
				String sub = textMatc.substring(0,textMatc.indexOf("-"));
				num=Integer.parseInt(sub);
			}
			String strSubToReplace=word.substring(num);
			//Toast.makeText(mContext, "textMatc2:"+textMatc+" : "+" sub2:"+strSubToReplace, Toast.LENGTH_LONG).show();;
			strNewTex=strNewTex.replace(textMatc,strSubToReplace );
			regexMatcherstrSub = regexStrSubWord.matcher(strNewTex);
		}


		return strNewTex;
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
		LinkedHashMap<String, String> mapofdecoders = new LinkedHashMap<String, String>();
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c7/De-@^.ogg|Adjective}#Form of [@-2]", "<I");//127
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/16/De-@^.ogg|Adjective}#Form of [@-2]", "`I");//127
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/65/De-@^.ogg|Adjective}#Form of [@-2]", "=I");//127
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f4/De-@^.ogg|Adjective}#Form of [@-2]", "%I");//126
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/47/De-@^.ogg|Adjective}#Form of [@-2]", "@J");//123
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d1/De-@^.ogg|Adjective}#Form of [@-2]", "~J");//122
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b7/De-@^.ogg|Adjective}#Form of [@-2]", "&J");//121
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2c/De-@^.ogg|Adjective}#Form of [@-2]", "<J");//120
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/91/De-@^.ogg|Adjective}#Form of [@-2]", "`J");//119
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/64/De-@^.ogg|Adjective}#Form of [@-2]", "=J");//118
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/58/De-@^.ogg|Adjective}#Form of [@-2]", "%J");//117
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e6/De-@^.ogg|Adjective}#Form of [@-2]", "{J");//117
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/33/De-@^.ogg|Adjective}#Form of [@-2]", "£J");//117
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/99/De-@^.ogg|Adjective}#Form of [@-2]", "@K");//116
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/72/De-@^.ogg|Adjective}#Form of [@-2]", "~K");//115
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/35/De-@^.ogg|Adjective}#Form of [@-2]", "$K");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9b/De-@^.ogg|Adjective}#Form of [@-2]", "<K");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ca/De-@^.ogg|Adjective}#Form of [@-2]", "`K");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4c/De-@^.ogg|Adjective}#Form of [@-2]", "£K");//113
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/08/De-@^.ogg|Adjective}#Form of [@-2]", "éK");//113
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5c/De-@^.ogg|Adjective}#Form of [@-2]", "@L");//113
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/de/De-@^.ogg|Adjective}#Form of [@-2]", "~L");//113
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cd/De-@^.ogg|Adjective}#Form of [@-2]", "$L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bd/De-@^.ogg|Adjective}#Form of [@-2]", "<L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fa/De-@^.ogg|Adjective}#Form of [@-2]", ">L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e7/De-@^.ogg|Adjective}#Form of [@-2]", "`L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e8/De-@^.ogg|Adjective}#Form of [@-2]", "=L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6c/De-@^.ogg|Adjective}#Form of [@-2]", "?L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/96/De-@^.ogg|Adjective}#Form of [@-2]", "%L");//112
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/54/De-@^.ogg|Adjective}#Form of [@-2]", "{L");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/07/De-@^.ogg|Adjective}#Form of [@-2]", "£L");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e5/De-@^.ogg|Adjective}#Form of [@-2]", "éL");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3e/De-@^.ogg|Adjective}#Form of [@-2]", "~M");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/14/De-@^.ogg|Adjective}#Form of [@-2]", "&M");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/77/De-@^.ogg|Adjective}#Form of [@-2]", "$M");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3a/De-@^.ogg|Adjective}#Form of [@-2]", "<M");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d0/De-@^.ogg|Adjective}#Form of [@-2]", "`M");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1a/De-@^.ogg|Adjective}#Form of [@-2]", "=M");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9e/De-@^.ogg|Adjective}#Form of [@-2]", "%M");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/09/De-@^.ogg|Adjective}#Form of [@-2]", "{M");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/55/De-@^.ogg|Adjective}#Form of [@-2]", "£M");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/71/De-@^.ogg|Adjective}#Form of [@-2]", "éM");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/be/De-@^.ogg|Adjective}#Form of [@-2]", "@N");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bf/De-@^.ogg|Adjective}#Form of [@-2]", "~N");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8f/De-@^.ogg|Adjective}#Form of [@-2]", "`N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9f/De-@^.ogg|Adjective}#Form of [@-2]", "=N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/66/De-@^.ogg|Adjective}#Form of [@-2]", "?N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e2/De-@^.ogg|Adjective}#Form of [@-2]", "%N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/13/De-@^.ogg|Adjective}#Form of [@-2]", "{N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a1/De-@^.ogg|Adjective}#Form of [@-2]", "£N");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c6/De-@^.ogg|Adjective}#Form of [@-2]", "éN");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/62/De-@^.ogg|Adjective}#Form of [@-2]", "@O");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8d/De-@^.ogg|Adjective}#Form of [@-2]", "~O");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b4/De-@^.ogg|Adjective}#Form of [@-2]", "$O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/86/De-@^.ogg|Adjective}#Form of [@-2]", "<O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6a/De-@^.ogg|Adjective}#Form of [@-2]", "`O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f7/De-@^.ogg|Adjective}#Form of [@-2]", "=O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/53/De-@^.ogg|Adjective}#Form of [@-2]", "?O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/30/De-@^.ogg|Adjective}#Form of [@-2]", "%O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/52/De-@^.ogg|Adjective}#Form of [@-2]", "{O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/00/De-@^.ogg|Adjective}#Form of [@-2]", "£O");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d8/De-@^.ogg|Adjective}#Form of [@-2]", "éO");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d9/De-@^.ogg|Adjective}#Form of [@-2]", "~P");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/82/De-@^.ogg|Adjective}#Form of [@-2]", "&P");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6e/De-@^.ogg|Adjective}#Form of [@-2]", "$P");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4e/De-@^.ogg|Adjective}#Form of [@-2]", "!P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4d/De-@^.ogg|Adjective}#Form of [@-2]", "`P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/29/De-@^.ogg|Adjective}#Form of [@-2]", "=P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/44/De-@^.ogg|Adjective}#Form of [@-2]", "?P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0e/De-@^.ogg|Adjective}#Form of [@-2]", "%P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5f/De-@^.ogg|Adjective}#Form of [@-2]", "{P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7c/De-@^.ogg|Adjective}#Form of [@-2]", "£P");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8e/De-@^.ogg|Adjective}#Form of [@-2]", "éP");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dd/De-@^.ogg|Adjective}#Form of [@-2]", "@Q");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/22/De-@^.ogg|Adjective}#Form of [@-2]", "_Q");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8b/De-@^.ogg|Adjective}#Form of [@-2]", "~Q");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8a/De-@^.ogg|Adjective}#Form of [@-2]", "<Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/10/De-@^.ogg|Adjective}#Form of [@-2]", ">Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d5/De-@^.ogg|Adjective}#Form of [@-2]", "!Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b3/De-@^.ogg|Adjective}#Form of [@-2]", "`Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2d/De-@^.ogg|Adjective}#Form of [@-2]", "=Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a2/De-@^.ogg|Adjective}#Form of [@-2]", "?Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/da/De-@^.ogg|Adjective}#Form of [@-2]", ".Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a0/De-@^.ogg|Adjective}#Form of [@-2]", "/Q");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/90/De-@^.ogg|Adjective}#Form of [@-2]", "&R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c0/De-@^.ogg|Adjective}#Form of [@-2]", "$R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0d/De-@^.ogg|Adjective}#Form of [@-2]", "<R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/42/De-@^.ogg|Adjective}#Form of [@-2]", "`R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7b/De-@^.ogg|Adjective}#Form of [@-2]", "=R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3b/De-@^.ogg|Adjective}#Form of [@-2]", "?R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/76/De-@^.ogg|Adjective}#Form of [@-2]", "%R");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/37/De-@^.ogg|Adjective}#Form of [@-2]", "£R");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/41/De-@^.ogg|Adjective}#Form of [@-2]", "éR");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5d/De-@^.ogg|Adjective}#Form of [@-2]", "@S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2a/De-@^.ogg|Adjective}#Form of [@-2]", "~S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/73/De-@^.ogg|Adjective}#Form of [@-2]", "&S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dc/De-@^.ogg|Adjective}#Form of [@-2]", "$S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6b/De-@^.ogg|Adjective}#Form of [@-2]", "<S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5a/De-@^.ogg|Adjective}#Form of [@-2]", "`S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/26/De-@^.ogg|Adjective}#Form of [@-2]", "=S");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/20/De-@^.ogg|Adjective}#Form of [@-2]", "&T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/97/De-@^.ogg|Adjective}#Form of [@-2]", "$T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c8/De-@^.ogg|Adjective}#Form of [@-2]", "<T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/31/De-@^.ogg|Adjective}#Form of [@-2]", "`T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1b/De-@^.ogg|Adjective}#Form of [@-2]", "=T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c2/De-@^.ogg|Adjective}#Form of [@-2]", "?T");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a3/De-@^.ogg|Adjective}#Form of [@-2]", "@U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b9/De-@^.ogg|Adjective}#Form of [@-2]", "_U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a7/De-@^.ogg|Adjective}#Form of [@-2]", "~U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/46/De-@^.ogg|Adjective}#Form of [@-2]", "&U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cf/De-@^.ogg|Adjective}#Form of [@-2]", "$U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ec/De-@^.ogg|Adjective}#Form of [@-2]", "<U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3f/De-@^.ogg|Adjective}#Form of [@-2]", "`U");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9a/De-@^.ogg|Adjective}#Form of [@-2]", "<V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6d/De-@^.ogg|Adjective}#Form of [@-2]", ">V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/27/De-@^.ogg|Adjective}#Form of [@-2]", "`V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d2/De-@^.ogg|Adjective}#Form of [@-2]", "=V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/12/De-@^.ogg|Adjective}#Form of [@-2]", "?V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fd/De-@^.ogg|Adjective}#Form of [@-2]", "%V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ef/De-@^.ogg|Adjective}#Form of [@-2]", "{V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f5/De-@^.ogg|Adjective}#Form of [@-2]", "£V");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c5/De-@^.ogg|Adjective}#Form of [@-2]", "`W");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/36/De-@^.ogg|Adjective}#Form of [@-2]", "=W");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/84/De-@^.ogg|Adjective}#Form of [@-2]", "%W");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d4/De-@^.ogg|Adjective}#Form of [@-2]", "{W");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/81/De-@^.ogg|Adjective}#Form of [@-2]", "£W");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b6/De-@^.ogg|Adjective}#Form of [@-2]", "éW");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7e/De-@^.ogg|Adjective}#Form of [@-2]", "`Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/19/De-@^.ogg|Adjective}#Form of [@-2]", ";Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/11/De-@^.ogg|Adjective}#Form of [@-2]", "=Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f2/De-@^.ogg|Adjective}#Form of [@-2]", ".Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/83/De-@^.ogg|Adjective}#Form of [@-2]", "%Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e9/De-@^.ogg|Adjective}#Form of [@-2]", "{Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f1/De-@^.ogg|Adjective}#Form of [@-2]", "£Y");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/34/De-@^.ogg|Adjective}#Form of [@-2]", "éY");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/79/De-@^.ogg|Adjective}#Form of [@-2]", "@Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4f/De-@^.ogg|Adjective}#Form of [@-2]", "&Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/18/De-@^.ogg|Adjective}#Form of [@-2]", "$Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ae/De-@^.ogg|Adjective}#Form of [@-2]", "<Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e3/De-@^.ogg|Adjective}#Form of [@-2]", "!Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bc/De-@^.ogg|Adjective}#Form of [@-2]", "`Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/ff/De-@^.ogg|Adjective}#Form of [@-2]", "=Z");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b8/De-@^.ogg|Adjective}#Form of [@-2]", "@X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/89/De-@^.ogg|Adjective}#Form of [@-2]", "_X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/28/De-@^.ogg|Adjective}#Form of [@-2]", "^X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/56/De-@^.ogg|Adjective}#Form of [@-2]", "~X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a8/De-@^.ogg|Adjective}#Form of [@-2]", "&X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/02/De-@^.ogg|Adjective}#Form of [@-2]", "$X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d6/De-@^.ogg|Adjective}#Form of [@-2]", "<X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/38/De-@^.ogg|Adjective}#Form of [@-2]", "!X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/63/De-@^.ogg|Adjective}#Form of [@-2]", ")X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2e/De-@^.ogg|Adjective}#Form of [@-2]", "`X");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c9/De-@^.ogg|Adjective}#Form of [@-2]", "%X");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b1/De-@^.ogg|Adjective}#Form of [@-2]", "{X");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ab/De-@^.ogg|Adjective}#Form of [@-2]", "£X");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ad/De-@^.ogg|Adjective}#Form of [@-2]", "éX");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0c/De-@^.ogg|Adjective}#Form of [@-2]", "@ç");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e0/De-@^.ogg|Adjective}#Form of [@-2]", "_ç");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/69/De-@^.ogg|Adjective}#Form of [@-2]", "^ç");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2f/De-@^.ogg|Adjective}#Form of [@-2]", "~ç");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b2/De-@^.ogg|Adjective}#Form of [@-2]", "&ç");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6f/De-@^.ogg|Adjective}#Form of [@-2]", "éç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/49/De-@^.ogg|Adjective}#Form of [@-2]", "@Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/05/De-@^.ogg|Adjective}#Form of [@-2]", "_Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/51/De-@^.ogg|Adjective}#Form of [@-2]", "^Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/43/De-@^.ogg|Adjective}#Form of [@-2]", "~Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7d/De-@^.ogg|Adjective}#Form of [@-2]", "&Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1c/De-@^.ogg|Adjective}#Form of [@-2]", "$Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/78/De-@^.ogg|Adjective}#Form of [@-2]", "<Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0b/De-@^.ogg|Adjective}#Form of [@-2]", ">Ç");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c1/De-@^.ogg|Adjective}#Form of [@-2]", "=Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9d/De-@^.ogg|Adjective}#Form of [@-2]", "-Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a5/De-@^.ogg|Adjective}#Form of [@-2]", "(Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ce/De-@^.ogg|Adjective}#Form of [@-2]", "?Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/eb/De-@^.ogg|Adjective}#Form of [@-2]", "+Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/80/De-@^.ogg|Adjective}#Form of [@-2]", ".Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/db/De-@^.ogg|Adjective}#Form of [@-2]", "/Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0a/De-@^.ogg|Adjective}#Form of [@-2]", "%Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/74/De-@^.ogg|Adjective}#Form of [@-2]", ",Ç");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/04/De-@^.ogg|Adjective}#Form of [@-2]", "=ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cc/De-@^.ogg|Adjective}#Form of [@-2]", "?ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/48/De-@^.ogg|Adjective}#Form of [@-2]", "/ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/df/De-@^.ogg|Adjective}#Form of [@-2]", "%ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/67/De-@^.ogg|Adjective}#Form of [@-2]", "{ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0f/De-@^.ogg|Adjective}#Form of [@-2]", "£ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fe/De-@^.ogg|Adjective}#Form of [@-2]", "éö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ed/De-@^.ogg|Adjective}#Form of [@-2]", "@Ö");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/98/De-@^.ogg|Adjective}#Form of [@-2]", ">ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/45/De-@^.ogg|Adjective}#Form of [@-2]", "!ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4a/De-@^.ogg|Adjective}#Form of [@-2]", ")ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2b/De-@^.ogg|Adjective}#Form of [@-2]", "`ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f2/De-@^.ogg|Adjective}#Form of [@-4]", ":ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fb/De-@^.ogg|Adjective}#Form of [@-2]", ";ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1d/De-@^.ogg|Adjective}#Form of [@-2]", "=ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b0/De-@^.ogg|Adjective}#Form of [@-2]", "-ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3d/De-@^.ogg|Adjective}#Form of [@-2]", "(ğ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ac/De-@^.ogg|Adjective}#Form of [@-2]", "@Ğ");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/77/De-@^.ogg|Adjective}#Form of [@-4]", "_Ğ");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/24/De-@^.ogg|Adjective}#Form of [@-2]", "^Ğ");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/af/De-@^.ogg|Adjective}#Form of [@-2]", "-Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7f/De-@^.ogg|Adjective}#Form of [@-2]", "(Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8c/De-@^.ogg|Adjective}#Form of [@-2]", "?Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e1/De-@^.ogg|Adjective}#Form of [@-2]", "'Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d7/De-@^.ogg|Adjective}#Form of [@-2]", "+Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1f/De-@^.ogg|Adjective}#Form of [@-2]", ".Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cb/De-@^.ogg|Adjective}#Form of [@-2]", "/Ğ");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/59/De-@^.ogg|Adjective}#Form of [@-2]", "$ü");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/93/De-@^.ogg|Adjective}#Form of [@-2]", "<ü");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/75/De-@^.ogg|Adjective}#Form of [@-2]", ">ü");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/39/De-@^.ogg|Adjective}#Form of [@-2]", "~Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/50/De-@^.ogg|Adjective}#Form of [@-2]", "&Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/32/De-@^.ogg|Adjective}#Form of [@-2]", "$Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b5/De-@^.ogg|Adjective}#Form of [@-2]", "<Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/61/De-@^.ogg|Adjective}#Form of [@-2]", "!Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/40/De-@^.ogg|Adjective}#Form of [@-2]", ")Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/17/De-@^.ogg|Adjective}#Form of [@-2]", "`Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/03/De-@^.ogg|Adjective}#Form of [@-2]", "=Ü");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ef/De-@^.ogg|Adjective}#Form of [@-4]", "$é");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/25/De-@^.ogg|Adjective}#Form of [@-2]", "<é");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a9/De-@^.ogg|Adjective}#Form of [@-2]", "!é");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/68/De-@^.ogg|Adjective}#Form of [@-2]", "_İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ae/De-@^.ogg|Adjective}#Form of [@-4]", "^İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/21/De-@^.ogg|Adjective}#Form of [@-2]", "~İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a6/De-@^.ogg|Adjective}#Form of [@-2]", "&İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bb/De-@^.ogg|Adjective}#Form of [@-2]", "$İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c4/De-@^.ogg|Adjective}#Form of [@-2]", "<İ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fb/De-@^.ogg|Adjective}#Form of [@-4]", ".İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a4/De-@^.ogg|Adjective}#Form of [@-2]", "/İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/85/De-@^.ogg|Adjective}#Form of [@-2]", "%İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/25/De-@^.ogg|Adjective}#Form of [@-4]", ",İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5e/De-@^.ogg|Adjective}#Form of [@-2]", "{İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f6/De-@^.ogg|Adjective}#Form of [@-2]", "}İ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9c/De-@^.ogg|Adjective}#Form of [@-2]", "<ı");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fc/De-@^.ogg|Adjective}#Form of [@-2]", ">ı");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e4/De-@^.ogg|Adjective}#Form of [@-2]", "!ı");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/92/De-@^.ogg|Adjective}#Form of [@-2]", ")ı");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f8/De-@^.ogg|Adjective}#Form of [@-2]", "`ı");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/23/De-@^.ogg|Adjective}#Form of [@-2]", "}ı");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c5/De-@^.ogg|Adjective}#Form of [@-4]", "£ı");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/71/De-@^.ogg|Adjective}#Form of [@-4]", "éı");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/ba/De-@^.ogg|Adjective}#Form of [@-2]", "@ß");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/de/De-@^.ogg|Adjective}#Form of [@-4]", "?ß");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5b/De-@^.ogg|Adjective}#Form of [@-2]", ".ß");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c3/De-@^.ogg|Adjective}#Form of [@-2]", ";Ƒ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3b/De-@^.ogg|Adjective}#Form of [@-4]", "=Ƒ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/01/De-@^.ogg|Adjective}#Form of [@-2]", "-Ƒ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1f/De-@^.ogg|Adjective}#Form of [@-4]", "(Ƒ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/74/De-@^.ogg|Adjective}#Form of [@-4]", "}Ƒ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1e/De-@^.ogg|Adjective}#Form of [@-2]", "£Ƒ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a2/De-@^.ogg|Adjective}#Form of [@-4]", "-ƒ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/88/De-@^.ogg|Adjective}#Form of [@-2]", "(ƒ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/15/De-@^.ogg|Adjective}#Form of [@-2]", "?ƒ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f9/De-@^.ogg|Adjective}#Form of [@-2]", "'ƒ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/37/De-@^.ogg|Adjective}#Form of [@-4]", "+ƒ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/95/De-@^.ogg|Adjective}#Form of [@-2]", "&Ɠ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f0/De-@^.ogg|Adjective}#Form of [@-2]", "$Ɠ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/06/De-@^.ogg|Adjective}#Form of [@-4]", "<Ɠ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/aa/De-@^.ogg|Adjective}#Form of [@-2]", ">Ɠ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/94/De-@^.ogg|Adjective}#Form of [@-2]", "!Ɠ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c6/De-@^.ogg|Adjective}#Form of [@-4]", "{Ɠ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c9/De-@^.ogg|Adjective}#Form of [@-4]", "}Ɠ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/07/De-@^.ogg|Adjective}#Form of [@-4]", "£Ɠ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/43/De-@^.ogg|Adjective}#Form of [@-4]", "éƓ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/09/De-@^.ogg|Adjective}#Form of [@-4]", "@Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ea/De-@^.ogg|Adjective}#Form of [@-2]", "_Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2d/De-@^.ogg|Adjective}#Form of [@-4]", "^Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/87/De-@^.ogg|Adjective}#Form of [@-2]", "~Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3c/De-@^.ogg|Adjective}#Form of [@-4]", "&Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d3/De-@^.ogg|Adjective}#Form of [@-2]", "$Ɣ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9c/De-@^.ogg|Adjective}#Form of [@-4]", "'Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/82/De-@^.ogg|Adjective}#Form of [@-4]", "+Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/57/De-@^.ogg|Adjective}#Form of [@-4]", ".Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8c/De-@^.ogg|Adjective}#Form of [@-4]", "/Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9e/De-@^.ogg|Adjective}#Form of [@-4]", "%Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6d/De-@^.ogg|Adjective}#Form of [@-4]", ",Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/35/De-@^.ogg|Adjective}#Form of [@-4]", "{Ɣ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5d/De-@^.ogg|Adjective}#Form of [@-4]", "=ƕ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b8/De-@^.ogg|Adjective}#Form of [@-4]", "-ƕ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d7/De-@^.ogg|Adjective}#Form of [@-4]", "(ƕ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2f/De-@^.ogg|Adjective}#Form of [@-4]", "?ƕ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/20/De-@^.ogg|Adjective}#Form of [@-4]", "'ƕ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7f/De-@^.ogg|Adjective}#Form of [@-4]", "^Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/32/De-@^.ogg|Adjective}#Form of [@-4]", "~Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/02/De-@^.ogg|Adjective}#Form of [@-4]", "&Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b2/De-@^.ogg|Adjective}#Form of [@-4]", "$Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/75/De-@^.ogg|Adjective}#Form of [@-4]", "<Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/60/De-@^.ogg|Adjective}#Form of [@-2]", ">Ɩ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/23/De-@^.ogg|Adjective}#Form of [@-4]", "éƖ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7e/De-@^.ogg|Adjective}#Form of [@-4]", "@Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/12/De-@^.ogg|Adjective}#Form of [@-4]", "_Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3a/De-@^.ogg|Adjective}#Form of [@-4]", "^Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/61/De-@^.ogg|Adjective}#Form of [@-4]", "~Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d4/De-@^.ogg|Adjective}#Form of [@-4]", "&Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c3/De-@^.ogg|Adjective}#Form of [@-4]", "$Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6b/De-@^.ogg|Adjective}#Form of [@-4]", "<Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3d/De-@^.ogg|Adjective}#Form of [@-4]", ">Ɨ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/06/De-@^.ogg|Adjective}#Form of [@-2]", "£Ɨ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e5/De-@^.ogg|Adjective}#Form of [@-4]", "éƗ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0a/De-@^.ogg|Adjective}#Form of [@-4]", "@Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/38/De-@^.ogg|Adjective}#Form of [@-4]", "_Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4b/De-@^.ogg|Adjective}#Form of [@-2]", "^Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/55/De-@^.ogg|Adjective}#Form of [@-4]", "~Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e2/De-@^.ogg|Adjective}#Form of [@-4]", "&Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7c/De-@^.ogg|Adjective}#Form of [@-4]", "$Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f8/De-@^.ogg|Adjective}#Form of [@-4]", "<Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/96/De-@^.ogg|Adjective}#Form of [@-4]", ">Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/88/De-@^.ogg|Adjective}#Form of [@-4]", "!Ƙ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/40/De-@^.ogg|Adjective}#Form of [@-4]", "'Ƙ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5f/De-@^.ogg|Adjective}#Form of [@-4]", "+Ƙ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d8/De-@^.ogg|Adjective}#Form of [@-4]", ".Ƙ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/df/De-@^.ogg|Adjective}#Form of [@-4]", "/Ƙ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/19/De-@^.ogg|Adjective}#Form of [@-4]", "%Ƙ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/68/De-@^.ogg|Adjective}#Form of [@-4]", "(ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/31/De-@^.ogg|Adjective}#Form of [@-4]", "?ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e1/De-@^.ogg|Adjective}#Form of [@-4]", "'ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d6/De-@^.ogg|Adjective}#Form of [@-4]", "+ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b3/De-@^.ogg|Adjective}#Form of [@-4]", ".ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a8/De-@^.ogg|Adjective}#Form of [@-4]", "/ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/04/De-@^.ogg|Adjective}#Form of [@-4]", "%ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5b/De-@^.ogg|Adjective}#Form of [@-4]", ",ƙ");//70
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1d/De-@^.ogg|Adjective}#Form of [@-4]", ":ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3e/De-@^.ogg|Adjective}#Form of [@-4]", ";ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e4/De-@^.ogg|Adjective}#Form of [@-4]", "=ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/94/De-@^.ogg|Adjective}#Form of [@-4]", "-ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3c/De-@^.ogg|Adjective}#Form of [@-2]", "(ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/af/De-@^.ogg|Adjective}#Form of [@-4]", "?ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b4/De-@^.ogg|Adjective}#Form of [@-4]", "'ƚ");//69
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ac/De-@^.ogg|Adjective}#Form of [@-4]", "`ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7a/De-@^.ogg|Adjective}#Form of [@-2]", ":ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/29/De-@^.ogg|Adjective}#Form of [@-4]", ";ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4d/De-@^.ogg|Adjective}#Form of [@-4]", "=ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/52/De-@^.ogg|Adjective}#Form of [@-4]", "-ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/01/De-@^.ogg|Adjective}#Form of [@-4]", "(ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/27/De-@^.ogg|Adjective}#Form of [@-4]", "?ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/21/De-@^.ogg|Adjective}#Form of [@-4]", "'ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/08/De-@^.ogg|Adjective}#Form of [@-4]", "+ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/13/De-@^.ogg|Adjective}#Form of [@-4]", ".ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c0/De-@^.ogg|Adjective}#Form of [@-4]", "/ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/97/De-@^.ogg|Adjective}#Form of [@-4]", "%ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bc/De-@^.ogg|Adjective}#Form of [@-4]", ",ƛ");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b5/De-@^.ogg|Adjective}#Form of [@-4]", "`Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/aa/De-@^.ogg|Adjective}#Form of [@-4]", ":Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/42/De-@^.ogg|Adjective}#Form of [@-4]", ";Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/99/De-@^.ogg|Adjective}#Form of [@-4]", "=Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/73/De-@^.ogg|Adjective}#Form of [@-4]", "-Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/58/De-@^.ogg|Adjective}#Form of [@-4]", "(Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0b/De-@^.ogg|Adjective}#Form of [@-4]", "?Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e8/De-@^.ogg|Adjective}#Form of [@-4]", "'Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/64/De-@^.ogg|Adjective}#Form of [@-4]", "+Ɯ");//67
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bd/De-@^.ogg|Adjective}#Form of [@-4]", ".Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3f/De-@^.ogg|Adjective}#Form of [@-4]", "/Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/eb/De-@^.ogg|Adjective}#Form of [@-4]", "%Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cd/De-@^.ogg|Adjective}#Form of [@-4]", ",Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a4/De-@^.ogg|Adjective}#Form of [@-4]", "{Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6f/De-@^.ogg|Adjective}#Form of [@-4]", "}Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/84/De-@^.ogg|Adjective}#Form of [@-4]", "£Ɲ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/34/De-@^.ogg|Adjective}#Form of [@-4]", "éƝ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2a/De-@^.ogg|Adjective}#Form of [@-4]", "@ƞ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a1/De-@^.ogg|Adjective}#Form of [@-4]", "_ƞ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2e/De-@^.ogg|Adjective}#Form of [@-4]", "^ƞ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ed/De-@^.ogg|Adjective}#Form of [@-4]", "~ƞ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/70/De-@^.ogg|Adjective}#Form of [@-2]", ".ƞ");//65
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a7/De-@^.ogg|Adjective}#Form of [@-4]", "/ƞ");//65
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/53/De-@^.ogg|Adjective}#Form of [@-4]", "%ƞ");//65
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d2/De-@^.ogg|Adjective}#Form of [@-4]", ",ƞ");//65
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/44/De-@^.ogg|Adjective}#Form of [@-4]", "{ƞ");//65
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/15/De-@^.ogg|Adjective}#Form of [@-4]", "'Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/46/De-@^.ogg|Adjective}#Form of [@-4]", "+Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/79/De-@^.ogg|Adjective}#Form of [@-4]", ".Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d5/De-@^.ogg|Adjective}#Form of [@-4]", "/Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/24/De-@^.ogg|Adjective}#Form of [@-4]", "%Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/03/De-@^.ogg|Adjective}#Form of [@-4]", ",Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/78/De-@^.ogg|Adjective}#Form of [@-4]", "{Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5c/De-@^.ogg|Adjective}#Form of [@-4]", "}Ɵ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/63/De-@^.ogg|Adjective}#Form of [@-4]", ",Ɔ");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0c/De-@^.ogg|Adjective}#Form of [@-4]", "{Ɔ");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fc/De-@^.ogg|Adjective}#Form of [@-4]", "}Ɔ");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/92/De-@^.ogg|Adjective}#Form of [@-4]", "£Ɔ");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c4/De-@^.ogg|Adjective}#Form of [@-4]", "éƆ");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2c/De-@^.ogg|Adjective}#Form of [@-4]", "@É");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f0/De-@^.ogg|Adjective}#Form of [@-4]", "_É");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ab/De-@^.ogg|Adjective}#Form of [@-4]", "^É");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e3/De-@^.ogg|Adjective}#Form of [@-4]", "~É");//63
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d9/De-@^.ogg|Adjective}#Form of [@-4]", "^Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/16/De-@^.ogg|Adjective}#Form of [@-4]", "~Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ee/De-@^.ogg|Adjective}#Form of [@-2]", "&Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/18/De-@^.ogg|Adjective}#Form of [@-4]", "$Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1e/De-@^.ogg|Adjective}#Form of [@-4]", "<Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/98/De-@^.ogg|Adjective}#Form of [@-4]", ">Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8f/De-@^.ogg|Adjective}#Form of [@-4]", "!Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5e/De-@^.ogg|Adjective}#Form of [@-4]", ")Ƕ");//62
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f5/De-@^.ogg|Adjective}#Form of [@-4]", "}Ƕ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/41/De-@^.ogg|Adjective}#Form of [@-4]", "£Ƕ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/da/De-@^.ogg|Adjective}#Form of [@-4]", "éǶ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/db/De-@^.ogg|Adjective}#Form of [@-4]", "@Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f9/De-@^.ogg|Adjective}#Form of [@-4]", "_Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/83/De-@^.ogg|Adjective}#Form of [@-4]", "^Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c7/De-@^.ogg|Adjective}#Form of [@-4]", "~Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/22/De-@^.ogg|Adjective}#Form of [@-4]", "&Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/86/De-@^.ogg|Adjective}#Form of [@-4]", "$Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9f/De-@^.ogg|Adjective}#Form of [@-4]", "<Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1a/De-@^.ogg|Adjective}#Form of [@-4]", ">Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/80/De-@^.ogg|Adjective}#Form of [@-4]", "!Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6c/De-@^.ogg|Adjective}#Form of [@-4]", ")Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fa/De-@^.ogg|Adjective}#Form of [@-4]", "`Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c2/De-@^.ogg|Adjective}#Form of [@-4]", ":Ƚ");//61
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7a/De-@^.ogg|Adjective}#Form of [@-4]", "^Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5a/De-@^.ogg|Adjective}#Form of [@-4]", "~Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b1/De-@^.ogg|Adjective}#Form of [@-4]", "&Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/26/De-@^.ogg|Adjective}#Form of [@-4]", "$Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/49/De-@^.ogg|Adjective}#Form of [@-4]", "<Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1c/De-@^.ogg|Adjective}#Form of [@-4]", ">Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e6/De-@^.ogg|Adjective}#Form of [@-4]", "!Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/60/De-@^.ogg|Adjective}#Form of [@-4]", ")Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8d/De-@^.ogg|Adjective}#Form of [@-4]", "`Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0f/De-@^.ogg|Adjective}#Form of [@-4]", ":Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/90/De-@^.ogg|Adjective}#Form of [@-4]", ";Ƞ");//60
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3e/De-@^.ogg|Adjective}#Form of ", "%g");//271
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/16/De-@^.ogg|Adjective}#Form of ", "!h");//265
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ae/De-@^.ogg|Adjective}#Form of ", "`h");//265
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/77/De-@^.ogg|Adjective}#Form of ", "=h");//264
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/63/De-@^.ogg|Adjective}#Form of ", "%h");//264
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ef/De-@^.ogg|Adjective}#Form of ", "~i");//263
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/09/De-@^.ogg|Adjective}#Form of ", "`i");//259
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/47/De-@^.ogg|Adjective}#Form of ", "%i");//257
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/35/De-@^.ogg|Adjective}#Form of ", "{i");//257
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/08/De-@^.ogg|Adjective}#Form of ", "$j");//254
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f2/De-@^.ogg|Adjective}#Form of ", ">j");//253
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/99/De-@^.ogg|Adjective}#Form of ", "!j");//252
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/42/De-@^.ogg|Adjective}#Form of ", "`j");//252
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4d/De-@^.ogg|Adjective}#Form of ", "=j");//252
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bf/De-@^.ogg|Adjective}#Form of ", "?j");//251
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5c/De-@^.ogg|Adjective}#Form of ", "%j");//251
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2c/De-@^.ogg|Adjective}#Form of ", "{j");//251
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d1/De-@^.ogg|Adjective}#Form of ", "£j");//250
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9e/De-@^.ogg|Adjective}#Form of ", "&k");//250
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/de/De-@^.ogg|Adjective}#Form of ", "$k");//250
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/43/De-@^.ogg|Adjective}#Form of ", ">k");//250
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/71/De-@^.ogg|Adjective}#Form of ", "`k");//248
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0e/De-@^.ogg|Adjective}#Form of ", "=k");//248
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8e/De-@^.ogg|Adjective}#Form of ", "?k");//248
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3a/De-@^.ogg|Adjective}#Form of ", "%k");//248
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a2/De-@^.ogg|Adjective}#Form of ", "{k");//247
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bd/De-@^.ogg|Adjective}#Form of ", "£k");//247
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c5/De-@^.ogg|Adjective}#Form of ", "$l");//247
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/07/De-@^.ogg|Adjective}#Form of ", "=l");//246
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f4/De-@^.ogg|Adjective}#Form of ", "%l");//246
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e5/De-@^.ogg|Adjective}#Form of ", "£l");//246
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2e/De-@^.ogg|Adjective}#Form of ", "$m");//246
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8f/De-@^.ogg|Adjective}#Form of ", "!m");//245
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e6/De-@^.ogg|Adjective}#Form of ", "`m");//245
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/33/De-@^.ogg|Adjective}#Form of ", "%m");//244
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c6/De-@^.ogg|Adjective}#Form of ", "{m");//244
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cd/De-@^.ogg|Adjective}#Form of ", "$n");//243
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3b/De-@^.ogg|Adjective}#Form of ", "`n");//243
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0f/De-@^.ogg|Adjective}#Form of ", "=n");//243
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d0/De-@^.ogg|Adjective}#Form of ", "?n");//242
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b2/De-@^.ogg|Adjective}#Form of ", "%n");//242
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a1/De-@^.ogg|Adjective}#Form of ", "£n");//242
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/44/De-@^.ogg|Adjective}#Form of ", "~o");//242
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/55/De-@^.ogg|Adjective}#Form of ", "$o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/54/De-@^.ogg|Adjective}#Form of ", "<o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dc/De-@^.ogg|Adjective}#Form of ", "!o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/58/De-@^.ogg|Adjective}#Form of ", "`o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/02/De-@^.ogg|Adjective}#Form of ", "=o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8d/De-@^.ogg|Adjective}#Form of ", "?o");//241
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/75/De-@^.ogg|Adjective}#Form of ", "%o");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/20/De-@^.ogg|Adjective}#Form of ", "{o");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e7/De-@^.ogg|Adjective}#Form of ", "£o");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c7/De-@^.ogg|Adjective}#Form of ", "$p");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fa/De-@^.ogg|Adjective}#Form of ", "!p");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/62/De-@^.ogg|Adjective}#Form of ", "=p");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7c/De-@^.ogg|Adjective}#Form of ", "%p");//240
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b4/De-@^.ogg|Adjective}#Form of ", "$q");//239
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6b/De-@^.ogg|Adjective}#Form of ", "!q");//239
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/29/De-@^.ogg|Adjective}#Form of ", "`q");//238
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/86/De-@^.ogg|Adjective}#Form of ", "=q");//238
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/73/De-@^.ogg|Adjective}#Form of ", "?q");//238
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/82/De-@^.ogg|Adjective}#Form of ", "%q");//238
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6e/De-@^.ogg|Adjective}#Form of ", "£q");//238
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/11/De-@^.ogg|Adjective}#Form of ", "!r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d5/De-@^.ogg|Adjective}#Form of ", "`r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1a/De-@^.ogg|Adjective}#Form of ", "=r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d3/De-@^.ogg|Adjective}#Form of ", "?r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b1/De-@^.ogg|Adjective}#Form of ", "%r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ab/De-@^.ogg|Adjective}#Form of ", "£r");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/72/De-@^.ogg|Adjective}#Form of ", "$s");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d4/De-@^.ogg|Adjective}#Form of ", "`s");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ca/De-@^.ogg|Adjective}#Form of ", "=s");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/26/De-@^.ogg|Adjective}#Form of ", "£s");//237
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c9/De-@^.ogg|Adjective}#Form of ", "!t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/34/De-@^.ogg|Adjective}#Form of ", "`t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d2/De-@^.ogg|Adjective}#Form of ", "=t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c0/De-@^.ogg|Adjective}#Form of ", "%t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/76/De-@^.ogg|Adjective}#Form of ", "{t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/64/De-@^.ogg|Adjective}#Form of ", "£t");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e8/De-@^.ogg|Adjective}#Form of ", "&u");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/96/De-@^.ogg|Adjective}#Form of ", "$u");//236
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5f/De-@^.ogg|Adjective}#Form of ", "`u");//235
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/78/De-@^.ogg|Adjective}#Form of ", "=u");//235
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1f/De-@^.ogg|Adjective}#Form of ", "?u");//235
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/13/De-@^.ogg|Adjective}#Form of ", "%u");//235
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8b/De-@^.ogg|Adjective}#Form of ", "{u");//235
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0d/De-@^.ogg|Adjective}#Form of ", "éu");//234
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6f/De-@^.ogg|Adjective}#Form of ", "$v");//234
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2d/De-@^.ogg|Adjective}#Form of ", "<v");//234
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b3/De-@^.ogg|Adjective}#Form of ", "!v");//234
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/90/De-@^.ogg|Adjective}#Form of ", "`v");//234
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7d/De-@^.ogg|Adjective}#Form of ", "{v");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/30/De-@^.ogg|Adjective}#Form of ", "£v");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a5/De-@^.ogg|Adjective}#Form of ", "&w");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d9/De-@^.ogg|Adjective}#Form of ", "$w");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4c/De-@^.ogg|Adjective}#Form of ", "<w");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/21/De-@^.ogg|Adjective}#Form of ", "!w");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/31/De-@^.ogg|Adjective}#Form of ", "`w");//233
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b8/De-@^.ogg|Adjective}#Form of ", "?w");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/41/De-@^.ogg|Adjective}#Form of ", "%w");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/46/De-@^.ogg|Adjective}#Form of ", "{w");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e4/De-@^.ogg|Adjective}#Form of ", "£w");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e1/De-@^.ogg|Adjective}#Form of ", "@x");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/db/De-@^.ogg|Adjective}#Form of ", "_x");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e2/De-@^.ogg|Adjective}#Form of ", "~x");//232
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/32/De-@^.ogg|Adjective}#Form of ", "$x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a8/De-@^.ogg|Adjective}#Form of ", "<x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/66/De-@^.ogg|Adjective}#Form of ", ">x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a3/De-@^.ogg|Adjective}#Form of ", "!x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f6/De-@^.ogg|Adjective}#Form of ", "`x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/be/De-@^.ogg|Adjective}#Form of ", ";x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/38/De-@^.ogg|Adjective}#Form of ", "=x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/18/De-@^.ogg|Adjective}#Form of ", "?x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e3/De-@^.ogg|Adjective}#Form of ", "%x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/22/De-@^.ogg|Adjective}#Form of ", "{x");//231
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7e/De-@^.ogg|Adjective}#Form of ", "£x");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9f/De-@^.ogg|Adjective}#Form of ", "éx");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2a/De-@^.ogg|Adjective}#Form of ", "~y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4e/De-@^.ogg|Adjective}#Form of ", "&y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/14/De-@^.ogg|Adjective}#Form of ", "$y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6a/De-@^.ogg|Adjective}#Form of ", "<y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/65/De-@^.ogg|Adjective}#Form of ", ">y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dd/De-@^.ogg|Adjective}#Form of ", "!y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/97/De-@^.ogg|Adjective}#Form of ", "`y");//230
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f7/De-@^.ogg|Adjective}#Form of ", "=y");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6d/De-@^.ogg|Adjective}#Form of ", "?y");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cf/De-@^.ogg|Adjective}#Form of ", "%y");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/40/De-@^.ogg|Adjective}#Form of ", "{y");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0b/De-@^.ogg|Adjective}#Form of ", "£y");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5e/De-@^.ogg|Adjective}#Form of ", "&z");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ea/De-@^.ogg|Adjective}#Form of ", "$z");//229
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0c/De-@^.ogg|Adjective}#Form of ", "<z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/12/De-@^.ogg|Adjective}#Form of ", "!z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fd/De-@^.ogg|Adjective}#Form of ", "`z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1d/De-@^.ogg|Adjective}#Form of ", "=z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c8/De-@^.ogg|Adjective}#Form of ", "?z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b7/De-@^.ogg|Adjective}#Form of ", "%z");//228
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5d/De-@^.ogg|Adjective}#Form of ", "{z");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/79/De-@^.ogg|Adjective}#Form of ", "£z");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8c/De-@^.ogg|Adjective}#Form of ", "@ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fb/De-@^.ogg|Adjective}#Form of ", "_ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2f/De-@^.ogg|Adjective}#Form of ", "^ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9c/De-@^.ogg|Adjective}#Form of ", "&ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/37/De-@^.ogg|Adjective}#Form of ", "$ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f8/De-@^.ogg|Adjective}#Form of ", "<ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1b/De-@^.ogg|Adjective}#Form of ", ">ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3d/De-@^.ogg|Adjective}#Form of ", "!ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ed/De-@^.ogg|Adjective}#Form of ", "`ʁ");//227
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c3/De-@^.ogg|Adjective}#Form of ", "=ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4f/De-@^.ogg|Adjective}#Form of ", "?ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f1/De-@^.ogg|Adjective}#Form of ", "'ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ac/De-@^.ogg|Adjective}#Form of ", "%ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fe/De-@^.ogg|Adjective}#Form of ", "{ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/52/De-@^.ogg|Adjective}#Form of ", "£ʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f5/De-@^.ogg|Adjective}#Form of ", "éʁ");//226
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ce/De-@^.ogg|Adjective}#Form of ", "_ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/80/De-@^.ogg|Adjective}#Form of ", "^ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2b/De-@^.ogg|Adjective}#Form of ", "~ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/81/De-@^.ogg|Adjective}#Form of ", "&ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/19/De-@^.ogg|Adjective}#Form of ", "$ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bc/De-@^.ogg|Adjective}#Form of ", "<ɔ");//225
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/df/De-@^.ogg|Adjective}#Form of ", ">ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/89/De-@^.ogg|Adjective}#Form of ", "!ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a7/De-@^.ogg|Adjective}#Form of ", "`ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5b/De-@^.ogg|Adjective}#Form of ", ";ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ec/De-@^.ogg|Adjective}#Form of ", "=ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6c/De-@^.ogg|Adjective}#Form of ", "(ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b6/De-@^.ogg|Adjective}#Form of ", "?ɔ");//224
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/27/De-@^.ogg|Adjective}#Form of ", "'ɔ");//223
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/74/De-@^.ogg|Adjective}#Form of ", "%ɔ");//223
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a4/De-@^.ogg|Adjective}#Form of ", ",ɔ");//223
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/53/De-@^.ogg|Adjective}#Form of ", "{ɔ");//223
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a0/De-@^.ogg|Adjective}#Form of ", "£ɔ");//223
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3c/De-@^.ogg|Adjective}#Form of ", "_ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d6/De-@^.ogg|Adjective}#Form of ", "^ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d8/De-@^.ogg|Adjective}#Form of ", "~ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d7/De-@^.ogg|Adjective}#Form of ", "&ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/eb/De-@^.ogg|Adjective}#Form of ", "$ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/17/De-@^.ogg|Adjective}#Form of ", "<ɪ");//222
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/49/De-@^.ogg|Adjective}#Form of ", ">ɪ");//221
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/61/De-@^.ogg|Adjective}#Form of ", "!ɪ");//221
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8a/De-@^.ogg|Adjective}#Form of ", "`ɪ");//221
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/10/De-@^.ogg|Adjective}#Form of ", ";ɪ");//221
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9b/De-@^.ogg|Adjective}#Form of ", "=ɪ");//221
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7f/De-@^.ogg|Adjective}#Form of ", "?ɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9d/De-@^.ogg|Adjective}#Form of ", "'ɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/51/De-@^.ogg|Adjective}#Form of ", "%ɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/85/De-@^.ogg|Adjective}#Form of ", "{ɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/84/De-@^.ogg|Adjective}#Form of ", "£ɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/25/De-@^.ogg|Adjective}#Form of ", "éɪ");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/00/De-@^.ogg|Adjective}#Form of ", "@̯");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/69/De-@^.ogg|Adjective}#Form of ", "_̯");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/93/De-@^.ogg|Adjective}#Form of ", "^̯");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f3/De-@^.ogg|Adjective}#Form of ", "~̯");//220
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/83/De-@^.ogg|Adjective}#Form of ", "$̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/06/De-@^.ogg|Adjective}#Form of ", "<̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/67/De-@^.ogg|Adjective}#Form of ", ">̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/04/De-@^.ogg|Adjective}#Form of ", "!̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/98/De-@^.ogg|Adjective}#Form of ", ")̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7b/De-@^.ogg|Adjective}#Form of ", "`̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/24/De-@^.ogg|Adjective}#Form of ", ":̯");//219
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/50/De-@^.ogg|Adjective}#Form of ", ";̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cc/De-@^.ogg|Adjective}#Form of ", "=̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/23/De-@^.ogg|Adjective}#Form of ", "-̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/da/De-@^.ogg|Adjective}#Form of ", "(̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/05/De-@^.ogg|Adjective}#Form of ", "?̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ad/De-@^.ogg|Adjective}#Form of ", "'̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/af/De-@^.ogg|Adjective}#Form of ", "+̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/88/De-@^.ogg|Adjective}#Form of ", ".̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/ff/De-@^.ogg|Adjective}#Form of ", "%̯");//218
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/57/De-@^.ogg|Adjective}#Form of ", ",̯");//217
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e0/De-@^.ogg|Adjective}#Form of ", "{̯");//217
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/92/De-@^.ogg|Adjective}#Form of ", "£̯");//216
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0a/De-@^.ogg|Adjective}#Form of ", "é̯");//216
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/28/De-@^.ogg|Adjective}#Form of ", "@A");//216
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fc/De-@^.ogg|Adjective}#Form of ", "$A");//215
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b5/De-@^.ogg|Adjective}#Form of ", "<A");//215
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4a/De-@^.ogg|Adjective}#Form of ", ">A");//215
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c4/De-@^.ogg|Adjective}#Form of ", "`A");//215
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/03/De-@^.ogg|Adjective}#Form of ", "=A");//215
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e9/De-@^.ogg|Adjective}#Form of ", "{A");//214
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/45/De-@^.ogg|Adjective}#Form of ", "£A");//214
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/91/De-@^.ogg|Adjective}#Form of ", "@B");//213
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a9/De-@^.ogg|Adjective}#Form of ", "~B");//213
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b9/De-@^.ogg|Adjective}#Form of ", "&B");//212
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/36/De-@^.ogg|Adjective}#Form of ", "$B");//212
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/ba/De-@^.ogg|Adjective}#Form of ", "<B");//212
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/48/De-@^.ogg|Adjective}#Form of ", "!B");//212
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9a/De-@^.ogg|Adjective}#Form of ", "£B");//211
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/68/De-@^.ogg|Adjective}#Form of ", "éB");//211
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/aa/De-@^.ogg|Adjective}#Form of ", "~C");//211
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/01/De-@^.ogg|Adjective}#Form of ", "&C");//211
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f9/De-@^.ogg|Adjective}#Form of ", "$C");//211
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/60/De-@^.ogg|Adjective}#Form of ", "<C");//210
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c1/De-@^.ogg|Adjective}#Form of ", "!C");//210
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a6/De-@^.ogg|Adjective}#Form of ", "?C");//209
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/59/De-@^.ogg|Adjective}#Form of ", "éC");//208
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/56/De-@^.ogg|Adjective}#Form of ", "~D");//208
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5a/De-@^.ogg|Adjective}#Form of ", "$D");//208
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/95/De-@^.ogg|Adjective}#Form of ", "<D");//208
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f0/De-@^.ogg|Adjective}#Form of ", "`D");//207
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/94/De-@^.ogg|Adjective}#Form of ", "=D");//206
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cb/De-@^.ogg|Adjective}#Form of ", "%D");//206
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/39/De-@^.ogg|Adjective}#Form of ", "£D");//205
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/15/De-@^.ogg|Adjective}#Form of ", "éD");//205
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1c/De-@^.ogg|Adjective}#Form of ", "@E");//204
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b0/De-@^.ogg|Adjective}#Form of ", "$E");//204
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4b/De-@^.ogg|Adjective}#Form of ", "<E");//204
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3f/De-@^.ogg|Adjective}#Form of ", "`E");//204
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c2/De-@^.ogg|Adjective}#Form of ", "=E");//204
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ee/De-@^.ogg|Adjective}#Form of ", "éE");//196
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7a/De-@^.ogg|Adjective}#Form of ", "&F");//195
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1e/De-@^.ogg|Adjective}#Form of ", "$F");//194
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bb/De-@^.ogg|Adjective}#Form of ", "<F");//193
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/87/De-@^.ogg|Adjective}#Form of ", "%F");//190
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/70/De-@^.ogg|Adjective}#Form of ", "éF");//186
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e4/De-@^.ogg|Verb}#verb form of ", "éJ");//126
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/87/De-@^.ogg|Verb}#verb form of ", "{K");//123
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/ba/De-@^.ogg|Verb}#verb form of ", "&L");//122
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ae/De-@^.ogg|Verb}#verb form of ", "&N");//118
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d8/De-@^.ogg|Verb}#verb form of ", "$N");//118
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/65/De-@^.ogg|Verb}#verb form of ", "&O");//117
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/67/De-@^.ogg|Verb}#verb form of ", "<P");//116
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/76/De-@^.ogg|Verb}#verb form of ", "%Q");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/58/De-@^.ogg|Verb}#verb form of ", "{Q");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ce/De-@^.ogg|Verb}#verb form of ", "£Q");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f3/De-@^.ogg|Verb}#verb form of ", "éQ");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cb/De-@^.ogg|Verb}#verb form of ", "@R");//114
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/00/De-@^.ogg|Verb}#verb form of ", "~R");//113
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/68/De-@^.ogg|Verb}#verb form of ", "%S");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5e/De-@^.ogg|Verb}#verb form of ", "{S");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c8/De-@^.ogg|Verb}#verb form of ", "£S");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/16/De-@^.ogg|Verb}#verb form of ", "éS");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2e/De-@^.ogg|Verb}#verb form of ", "@T");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4c/De-@^.ogg|Verb}#verb form of ", "~T");//111
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6a/De-@^.ogg|Verb}#verb form of ", "{T");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f9/De-@^.ogg|Verb}#verb form of ", "£T");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bc/De-@^.ogg|Verb}#verb form of ", "éT");//110
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0a/De-@^.ogg|Verb}#verb form of ", "=U");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/31/De-@^.ogg|Verb}#verb form of ", "%U");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/44/De-@^.ogg|Verb}#verb form of ", "{U");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8a/De-@^.ogg|Verb}#verb form of ", "£U");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6f/De-@^.ogg|Verb}#verb form of ", "éU");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/92/De-@^.ogg|Verb}#verb form of ", "~V");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/46/De-@^.ogg|Verb}#verb form of ", "&V");//109
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/35/De-@^.ogg|Verb}#verb form of ", "éV");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5a/De-@^.ogg|Verb}#verb form of ", "@W");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c0/De-@^.ogg|Verb}#verb form of ", "~W");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/60/De-@^.ogg|Verb}#verb form of ", "&W");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6e/De-@^.ogg|Verb}#verb form of ", "$W");//108
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d5/De-@^.ogg|Verb}#verb form of ", "_Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fd/De-@^.ogg|Verb}#verb form of ", "^Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9e/De-@^.ogg|Verb}#verb form of ", "~Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/30/De-@^.ogg|Verb}#verb form of ", "&Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a6/De-@^.ogg|Verb}#verb form of ", "$Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5b/De-@^.ogg|Verb}#verb form of ", "<Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/41/De-@^.ogg|Verb}#verb form of ", ">Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2a/De-@^.ogg|Verb}#verb form of ", ")Y");//107
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9a/De-@^.ogg|Verb}#verb form of ", "?Z");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ec/De-@^.ogg|Verb}#verb form of ", "%Z");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3f/De-@^.ogg|Verb}#verb form of ", "{Z");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/20/De-@^.ogg|Verb}#verb form of ", "£Z");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/04/De-@^.ogg|Verb}#verb form of ", "éZ");//106
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f5/De-@^.ogg|Verb}#verb form of ", ";X");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/39/De-@^.ogg|Verb}#verb form of ", "=X");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a9/De-@^.ogg|Verb}#verb form of ", "?X");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/52/De-@^.ogg|Verb}#verb form of ", "+X");//105
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6b/De-@^.ogg|Verb}#verb form of ", "$ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d6/De-@^.ogg|Verb}#verb form of ", "<ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d7/De-@^.ogg|Verb}#verb form of ", ">ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d4/De-@^.ogg|Verb}#verb form of ", "!ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/36/De-@^.ogg|Verb}#verb form of ", "`ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5c/De-@^.ogg|Verb}#verb form of ", ";ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/80/De-@^.ogg|Verb}#verb form of ", "=ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/75/De-@^.ogg|Verb}#verb form of ", "?ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/33/De-@^.ogg|Verb}#verb form of ", "'ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/49/De-@^.ogg|Verb}#verb form of ", "%ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/72/De-@^.ogg|Verb}#verb form of ", ",ç");//104
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/32/De-@^.ogg|Verb}#verb form of ", "!Ç");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/54/De-@^.ogg|Verb}#verb form of ", ")Ç");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a2/De-@^.ogg|Verb}#verb form of ", "`Ç");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/84/De-@^.ogg|Verb}#verb form of ", ";Ç");//103
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0f/De-@^.ogg|Verb}#verb form of ", "{Ç");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/19/De-@^.ogg|Verb}#verb form of ", "£Ç");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8c/De-@^.ogg|Verb}#verb form of ", "éÇ");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7c/De-@^.ogg|Verb}#verb form of ", "_ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0d/De-@^.ogg|Verb}#verb form of ", "&ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b7/De-@^.ogg|Verb}#verb form of ", "$ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/62/De-@^.ogg|Verb}#verb form of ", "<ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d2/De-@^.ogg|Verb}#verb form of ", ">ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/27/De-@^.ogg|Verb}#verb form of ", "!ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/82/De-@^.ogg|Verb}#verb form of ", "`ö");//102
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/81/De-@^.ogg|Verb}#verb form of ", "_Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/70/De-@^.ogg|Verb}#verb form of ", "^Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a1/De-@^.ogg|Verb}#verb form of ", "~Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/03/De-@^.ogg|Verb}#verb form of ", "&Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e5/De-@^.ogg|Verb}#verb form of ", "$Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a0/De-@^.ogg|Verb}#verb form of ", "<Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0b/De-@^.ogg|Verb}#verb form of ", ">Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/eb/De-@^.ogg|Verb}#verb form of ", "!Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/10/De-@^.ogg|Verb}#verb form of ", "`Ö");//101
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2d/De-@^.ogg|Verb}#verb form of ", ".Ö");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9b/De-@^.ogg|Verb}#verb form of ", "%Ö");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cc/De-@^.ogg|Verb}#verb form of ", "{Ö");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/24/De-@^.ogg|Verb}#verb form of ", "£Ö");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3e/De-@^.ogg|Verb}#verb form of ", "éÖ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1b/De-@^.ogg|Verb}#verb form of ", "@ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/71/De-@^.ogg|Verb}#verb form of ", "_ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/15/De-@^.ogg|Verb}#verb form of ", "^ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f7/De-@^.ogg|Verb}#verb form of ", "~ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/78/De-@^.ogg|Verb}#verb form of ", "&ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e3/De-@^.ogg|Verb}#verb form of ", "$ğ");//100
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b2/De-@^.ogg|Verb}#verb form of ", "<ğ");//100
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/ae/De-^@.ogg|Adjective}#Form of ", "&é");//93
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/9c/De-^@.ogg|Adjective}#Form of ", "{ı");//89
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e0/De-^@.ogg|Adjective}#Form of ", "=ß");//88
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/aa/De-^@.ogg|Adjective}#Form of ", "?Ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/8c/De-^@.ogg|Adjective}#Form of ", "'Ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/ce/De-^@.ogg|Adjective}#Form of ", "+Ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/2d/De-^@.ogg|Adjective}#Form of ", "éƑ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/be/De-^@.ogg|Adjective}#Form of ", "@ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/95/De-^@.ogg|Adjective}#Form of ", "_ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/90/De-^@.ogg|Adjective}#Form of ", "^ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/ee/De-^@.ogg|Adjective}#Form of ", "~ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d7/De-^@.ogg|Adjective}#Form of ", "&ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/dd/De-^@.ogg|Adjective}#Form of ", ".ƒ");//84
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/02/De-^@.ogg|Adjective}#Form of ", "/ƒ");//84
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/29/De-^@.ogg|Adjective}#Form of ", ")Ɠ");//83
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/26/De-^@.ogg|Adjective}#Form of ", "`Ɠ");//83
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/46/De-^@.ogg|Adjective}#Form of ", ":Ɠ");//83
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c1/De-^@.ogg|Adjective}#Form of ", ";Ɠ");//83
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/ed/De-^@.ogg|Adjective}#Form of ", ">Ɣ");//82
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/03/De-^@.ogg|Adjective}#Form of ", "!Ɣ");//82
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/8e/De-^@.ogg|Adjective}#Form of ", "£Ɣ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/99/De-^@.ogg|Adjective}#Form of ", "éƔ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/6d/De-^@.ogg|Adjective}#Form of ", "@ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/16/De-^@.ogg|Adjective}#Form of ", "_ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/70/De-^@.ogg|Adjective}#Form of ", "^ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d2/De-^@.ogg|Adjective}#Form of ", "~ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/0a/De-^@.ogg|Adjective}#Form of ", "&ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c3/De-^@.ogg|Adjective}#Form of ", "$ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/3e/De-^@.ogg|Adjective}#Form of ", "+ƕ");//80
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c6/De-^@.ogg|Adjective}#Form of ", ".ƕ");//80
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/8f/De-^@.ogg|Adjective}#Form of ", "/ƕ");//80
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/eb/De-^@.ogg|Adjective}#Form of ", "%ƕ");//80
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/13/De-^@.ogg|Adjective}#Form of ", "!Ɩ");//79
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/63/De-^@.ogg|Adjective}#Form of ", ")Ɩ");//79
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/05/De-^@.ogg|Adjective}#Form of ", "`Ɩ");//79
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/34/De-^@.ogg|Adjective}#Form of ", ":Ɩ");//79
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c7/De-^@.ogg|Adjective}#Form of ", "%Ɩ");//78
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/1e/De-^@.ogg|Adjective}#Form of ", ",Ɩ");//78
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/f2/De-^@.ogg|Adjective}#Form of ", "{Ɩ");//78
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/76/De-^@.ogg|Adjective}#Form of ", "}Ɩ");//78
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/78/De-^@.ogg|Adjective}#Form of ", "=Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/bc/De-^@.ogg|Adjective}#Form of ", "-Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/43/De-^@.ogg|Adjective}#Form of ", "(Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e7/De-^@.ogg|Adjective}#Form of ", "?Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/7e/De-^@.ogg|Adjective}#Form of ", "'Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/24/De-^@.ogg|Adjective}#Form of ", "+Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/9f/De-^@.ogg|Adjective}#Form of ", ".Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d5/De-^@.ogg|Adjective}#Form of ", "/Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/5c/De-^@.ogg|Adjective}#Form of ", "%Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/0c/De-^@.ogg|Adjective}#Form of ", ",Ɨ");//77
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/94/De-^@.ogg|Adjective}#Form of ", ";Ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/58/De-^@.ogg|Adjective}#Form of ", "=Ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/51/De-^@.ogg|Adjective}#Form of ", "-Ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/31/De-^@.ogg|Adjective}#Form of ", "(Ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/15/De-^@.ogg|Adjective}#Form of ", "?Ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/25/De-^@.ogg|Adjective}#Form of ", "&ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/84/De-^@.ogg|Adjective}#Form of ", "$ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/61/De-^@.ogg|Adjective}#Form of ", "<ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/62/De-^@.ogg|Adjective}#Form of ", ">ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/82/De-^@.ogg|Adjective}#Form of ", "!ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/4b/De-^@.ogg|Adjective}#Form of ", ")ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/88/De-^@.ogg|Adjective}#Form of ", "`ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/a9/De-^@.ogg|Adjective}#Form of ", ":ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c5/De-^@.ogg|Adjective}#Form of ", ";ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e6/De-^@.ogg|Adjective}#Form of ", "=ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/6f/De-^@.ogg|Adjective}#Form of ", "-ƙ");//75
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/85/De-^@.ogg|Adjective}#Form of ", "^ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/56/De-^@.ogg|Adjective}#Form of ", "~ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/b1/De-^@.ogg|Adjective}#Form of ", "&ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/8b/De-^@.ogg|Adjective}#Form of ", "$ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d1/De-^@.ogg|Adjective}#Form of ", "<ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/47/De-^@.ogg|Adjective}#Form of ", ">ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/91/De-^@.ogg|Adjective}#Form of ", "!ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/2a/De-^@.ogg|Adjective}#Form of ", ")ƚ");//74
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/68/De-^@.ogg|Adjective}#Form of ", "%ƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/52/De-^@.ogg|Adjective}#Form of ", ",ƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/f4/De-^@.ogg|Adjective}#Form of ", "{ƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/35/De-^@.ogg|Adjective}#Form of ", "}ƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/da/De-^@.ogg|Adjective}#Form of ", "£ƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/7a/De-^@.ogg|Adjective}#Form of ", "éƚ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/19/De-^@.ogg|Adjective}#Form of ", "@ƛ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/69/De-^@.ogg|Adjective}#Form of ", "_ƛ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/81/De-^@.ogg|Adjective}#Form of ", "^ƛ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/b7/De-^@.ogg|Adjective}#Form of ", "~ƛ");//73
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/1c/De-^@.ogg|Adjective}#Form of ", "£ƛ");//72
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/67/De-^@.ogg|Adjective}#Form of ", "éƛ");//72
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e9/De-^@.ogg|Adjective}#Form of ", "@Ɯ");//72
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/f1/De-^@.ogg|Adjective}#Form of ", "_Ɯ");//72
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/83/De-^@.ogg|Adjective}#Form of ", "^Ɯ");//72
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/ca/De-^@.ogg|Adjective}#Form of ", "/Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/6b/De-^@.ogg|Adjective}#Form of ", "%Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/07/De-^@.ogg|Adjective}#Form of ", ",Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/cc/De-^@.ogg|Adjective}#Form of ", "{Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/6e/De-^@.ogg|Adjective}#Form of ", "}Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/54/De-^@.ogg|Adjective}#Form of ", "£Ɯ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/2f/De-^@.ogg|Adjective}#Form of ", "éƜ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/79/De-^@.ogg|Adjective}#Form of ", "@Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/bf/De-^@.ogg|Adjective}#Form of ", "_Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e3/De-^@.ogg|Adjective}#Form of ", "^Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/e5/De-^@.ogg|Adjective}#Form of ", "~Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/9b/De-^@.ogg|Adjective}#Form of ", "&Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/1b/De-^@.ogg|Adjective}#Form of ", "$Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/73/De-^@.ogg|Adjective}#Form of ", "<Ɲ");//71
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/1d/De-^@.ogg|Adjective}#Form of ", ")ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d8/De-^@.ogg|Adjective}#Form of ", "`ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/3a/De-^@.ogg|Adjective}#Form of ", ":ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/89/De-^@.ogg|Adjective}#Form of ", ";ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/b6/De-^@.ogg|Adjective}#Form of ", "=ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/01/De-^@.ogg|Adjective}#Form of ", "-ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/fe/De-^@.ogg|Adjective}#Form of ", "(ƞ");//70
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c4/De-^@.ogg|Adjective}#Form of ", "_Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/72/De-^@.ogg|Adjective}#Form of ", "^Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/cb/De-^@.ogg|Adjective}#Form of ", "~Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c2/De-^@.ogg|Adjective}#Form of ", "&Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/93/De-^@.ogg|Adjective}#Form of ", "$Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/28/De-^@.ogg|Adjective}#Form of ", "<Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/49/De-^@.ogg|Adjective}#Form of ", ">Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/ab/De-^@.ogg|Adjective}#Form of ", "!Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/5f/De-^@.ogg|Adjective}#Form of ", ")Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/0f/De-^@.ogg|Adjective}#Form of ", "`Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ0/0e/De-^@.ogg|Adjective}#Form of ", ":Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/40/De-^@.ogg|Adjective}#Form of ", ";Ɵ");//69
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/f7/De-^@.ogg|Adjective}#Form of ", "&Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/4d/De-^@.ogg|Adjective}#Form of ", "$Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/8d/De-^@.ogg|Adjective}#Form of ", "<Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/1a/De-^@.ogg|Adjective}#Form of ", ">Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ5/5e/De-^@.ogg|Adjective}#Form of ", "!Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d0/De-^@.ogg|Adjective}#Form of ", ")Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/cd/De-^@.ogg|Adjective}#Form of ", "`Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/42/De-^@.ogg|Adjective}#Form of ", ":Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/dc/De-^@.ogg|Adjective}#Form of ", ";Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/a8/De-^@.ogg|Adjective}#Form of ", "=Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/48/De-^@.ogg|Adjective}#Form of ", "-Ɔ");//68
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/7b/De-^@.ogg|Adjective}#Form of ", "$É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ8/87/De-^@.ogg|Adjective}#Form of ", "<É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/74/De-^@.ogg|Adjective}#Form of ", ">É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/77/De-^@.ogg|Adjective}#Form of ", "!É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/41/De-^@.ogg|Adjective}#Form of ", ")É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/ba/De-^@.ogg|Adjective}#Form of ", "`É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/17/De-^@.ogg|Adjective}#Form of ", ":É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d3/De-^@.ogg|Adjective}#Form of ", ";É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/27/De-^@.ogg|Adjective}#Form of ", "=É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/f0/De-^@.ogg|Adjective}#Form of ", "-É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/3b/De-^@.ogg|Adjective}#Form of ", "(É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/12/De-^@.ogg|Adjective}#Form of ", "?É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/23/De-^@.ogg|Adjective}#Form of ", "+É");//67
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/af/De-^@.ogg|Adjective}#Form of ", ";Ƕ");//66
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/3d/De-^@.ogg|Adjective}#Form of ", "=Ƕ");//66
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/a5/De-^@.ogg|Adjective}#Form of ", "-Ƕ");//66
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ4/45/De-^@.ogg|Adjective}#Form of ", "(Ƕ");//66
		mapofdecoders.put("German{}|Pronunciation}^Ⓓd/d9/De-^@.ogg|Adjective}#Form of ", "=Ƚ");//65
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/a6/De-^@.ogg|Adjective}#Form of ", "-Ƚ");//65
		mapofdecoders.put("German{}|Pronunciation}^Ⓓc/c0/De-^@.ogg|Adjective}#Form of ", "(Ƚ");//65
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ7/7d/De-^@.ogg|Adjective}#Form of ", "?Ƚ");//65
		mapofdecoders.put("German{}|Pronunciation}^Ⓓa/a4/De-^@.ogg|Adjective}#Form of ", "'Ƚ");//65
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/18/De-^@.ogg|Adjective}#Form of ", "=Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/21/De-^@.ogg|Adjective}#Form of ", "-Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ1/10/De-^@.ogg|Adjective}#Form of ", "(Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ6/66/De-^@.ogg|Adjective}#Form of ", "?Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/98/De-^@.ogg|Adjective}#Form of ", "'Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ3/33/De-^@.ogg|Adjective}#Form of ", "+Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓe/ea/De-^@.ogg|Adjective}#Form of ", ".Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/fd/De-^@.ogg|Adjective}#Form of ", "/Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ9/97/De-^@.ogg|Adjective}#Form of ", "%Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ2/2b/De-^@.ogg|Adjective}#Form of ", ",Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓb/b8/De-^@.ogg|Adjective}#Form of ", "{Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}^Ⓓf/ff/De-^@.ogg|Adjective}#Form of ", "}Ƞ");//64
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f1/De-@^.ogg|Verb}#verb form of ", ".ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/66/De-@^.ogg|Verb}#verb form of ", "/ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/be/De-@^.ogg|Verb}#verb form of ", "%ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/59/De-@^.ogg|Verb}#verb form of ", ",ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/93/De-@^.ogg|Verb}#verb form of ", "{ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fc/De-@^.ogg|Verb}#verb form of ", "}ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2b/De-@^.ogg|Verb}#verb form of ", "£ğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c7/De-@^.ogg|Verb}#verb form of ", "éğ");//99
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8d/De-@^.ogg|Verb}#verb form of ", "&Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bd/De-@^.ogg|Verb}#verb form of ", "$Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0c/De-@^.ogg|Verb}#verb form of ", "<Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a8/De-@^.ogg|Verb}#verb form of ", ">Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ad/De-@^.ogg|Verb}#verb form of ", "!Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9f/De-@^.ogg|Verb}#verb form of ", ")Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/25/De-@^.ogg|Verb}#verb form of ", "`Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f8/De-@^.ogg|Verb}#verb form of ", ":Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c5/De-@^.ogg|Verb}#verb form of ", ";Ğ");//98
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d0/De-@^.ogg|Verb}#verb form of ", "%Ğ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3d/De-@^.ogg|Verb}#verb form of ", ",Ğ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/74/De-@^.ogg|Verb}#verb form of ", "{Ğ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/da/De-@^.ogg|Verb}#verb form of ", "}Ğ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/df/De-@^.ogg|Verb}#verb form of ", "£Ğ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0e/De-@^.ogg|Verb}#verb form of ", "éĞ");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c9/De-@^.ogg|Verb}#verb form of ", "~ü");//97
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c4/De-@^.ogg|Verb}#verb form of ", "`ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/85/De-@^.ogg|Verb}#verb form of ", "=ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7b/De-@^.ogg|Verb}#verb form of ", "?ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a3/De-@^.ogg|Verb}#verb form of ", "%ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c3/De-@^.ogg|Verb}#verb form of ", "{ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9c/De-@^.ogg|Verb}#verb form of ", "£ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/40/De-@^.ogg|Verb}#verb form of ", "éü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cf/De-@^.ogg|Verb}#verb form of ", "@Ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/79/De-@^.ogg|Verb}#verb form of ", "^Ü");//96
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/17/De-@^.ogg|Verb}#verb form of ", "%Ü");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/61/De-@^.ogg|Verb}#verb form of ", "{Ü");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dc/De-@^.ogg|Verb}#verb form of ", "£Ü");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f2/De-@^.ogg|Verb}#verb form of ", "éÜ");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3b/De-@^.ogg|Verb}#verb form of ", "@é");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4a/De-@^.ogg|Verb}#verb form of ", "_é");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e2/De-@^.ogg|Verb}#verb form of ", "^é");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b0/De-@^.ogg|Verb}#verb form of ", "~é");//95
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/91/De-@^.ogg|Verb}#verb form of ", ")é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ef/De-@^.ogg|Verb}#verb form of ", "`é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/22/De-@^.ogg|Verb}#verb form of ", ";é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e7/De-@^.ogg|Verb}#verb form of ", "=é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/14/De-@^.ogg|Verb}#verb form of ", "(é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a5/De-@^.ogg|Verb}#verb form of ", "?é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1c/De-@^.ogg|Verb}#verb form of ", "+é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ea/De-@^.ogg|Verb}#verb form of ", ".é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3a/De-@^.ogg|Verb}#verb form of ", "/é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b4/De-@^.ogg|Verb}#verb form of ", "%é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e8/De-@^.ogg|Verb}#verb form of ", "{é");//94
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/07/De-@^.ogg|Verb}#verb form of ", ">İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c1/De-@^.ogg|Verb}#verb form of ", "!İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/ff/De-@^.ogg|Verb}#verb form of ", ")İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e1/De-@^.ogg|Verb}#verb form of ", "`İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7a/De-@^.ogg|Verb}#verb form of ", ":İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/db/De-@^.ogg|Verb}#verb form of ", ";İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f0/De-@^.ogg|Verb}#verb form of ", "=İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/06/De-@^.ogg|Verb}#verb form of ", "(İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fe/De-@^.ogg|Verb}#verb form of ", "?İ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/64/De-@^.ogg|Verb}#verb form of ", "£İ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c2/De-@^.ogg|Verb}#verb form of ", "éİ");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ed/De-@^.ogg|Verb}#verb form of ", "@ı");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4e/De-@^.ogg|Verb}#verb form of ", "_ı");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4d/De-@^.ogg|Verb}#verb form of ", "^ı");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2c/De-@^.ogg|Verb}#verb form of ", "~ı");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/77/De-@^.ogg|Verb}#verb form of ", "&ı");//92
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/95/De-@^.ogg|Verb}#verb form of ", "=ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/90/De-@^.ogg|Verb}#verb form of ", "-ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ab/De-@^.ogg|Verb}#verb form of ", "(ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b6/De-@^.ogg|Verb}#verb form of ", "?ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/89/De-@^.ogg|Verb}#verb form of ", "'ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/12/De-@^.ogg|Verb}#verb form of ", "+ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2f/De-@^.ogg|Verb}#verb form of ", ".ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6d/De-@^.ogg|Verb}#verb form of ", "/ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d1/De-@^.ogg|Verb}#verb form of ", "%ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b8/De-@^.ogg|Verb}#verb form of ", ",ı");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/01/De-@^.ogg|Verb}#verb form of ", "_ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a7/De-@^.ogg|Verb}#verb form of ", "~ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/09/De-@^.ogg|Verb}#verb form of ", "&ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/13/De-@^.ogg|Verb}#verb form of ", "$ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fb/De-@^.ogg|Verb}#verb form of ", "<ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f6/De-@^.ogg|Verb}#verb form of ", ">ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7e/De-@^.ogg|Verb}#verb form of ", "!ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/21/De-@^.ogg|Verb}#verb form of ", ")ß");//90
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e9/De-@^.ogg|Verb}#verb form of ", "{ß");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b3/De-@^.ogg|Verb}#verb form of ", "£ß");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/48/De-@^.ogg|Verb}#verb form of ", "éß");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6c/De-@^.ogg|Verb}#verb form of ", "@Ƒ");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/56/De-@^.ogg|Verb}#verb form of ", "_Ƒ");//89
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7d/De-@^.ogg|Verb}#verb form of ", "$Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b9/De-@^.ogg|Verb}#verb form of ", "<Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/dd/De-@^.ogg|Verb}#verb form of ", ">Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/51/De-@^.ogg|Verb}#verb form of ", "!Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d9/De-@^.ogg|Verb}#verb form of ", ")Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fa/De-@^.ogg|Verb}#verb form of ", "`Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1f/De-@^.ogg|Verb}#verb form of ", ":Ƒ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/43/De-@^.ogg|Verb}#verb form of ", ".Ƒ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/29/De-@^.ogg|Verb}#verb form of ", "/Ƒ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d3/De-@^.ogg|Verb}#verb form of ", "%Ƒ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/88/De-@^.ogg|Verb}#verb form of ", ",Ƒ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1a/De-@^.ogg|Verb}#verb form of ", "{Ƒ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b5/De-@^.ogg|Verb}#verb form of ", "<ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/11/De-@^.ogg|Verb}#verb form of ", ">ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/96/De-@^.ogg|Verb}#verb form of ", "!ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4b/De-@^.ogg|Verb}#verb form of ", ")ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/18/De-@^.ogg|Verb}#verb form of ", "`ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1e/De-@^.ogg|Verb}#verb form of ", ":ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ca/De-@^.ogg|Verb}#verb form of ", ";ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/45/De-@^.ogg|Verb}#verb form of ", "=ƒ");//86
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5d/De-@^.ogg|Verb}#verb form of ", ",ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/57/De-@^.ogg|Verb}#verb form of ", "{ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b1/De-@^.ogg|Verb}#verb form of ", "}ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e6/De-@^.ogg|Verb}#verb form of ", "£ƒ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/42/De-@^.ogg|Verb}#verb form of ", "éƒ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/4f/De-@^.ogg|Verb}#verb form of ", "@Ɠ");//85
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/86/De-@^.ogg|Verb}#verb form of ", "-Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/53/De-@^.ogg|Verb}#verb form of ", "(Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/63/De-@^.ogg|Verb}#verb form of ", "?Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/de/De-@^.ogg|Verb}#verb form of ", "'Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/26/De-@^.ogg|Verb}#verb form of ", "+Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/50/De-@^.ogg|Verb}#verb form of ", ".Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1d/De-@^.ogg|Verb}#verb form of ", "/Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bf/De-@^.ogg|Verb}#verb form of ", "%Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e0/De-@^.ogg|Verb}#verb form of ", ",Ɠ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/69/De-@^.ogg|Verb}#verb form of ", ":Ɣ");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/37/De-@^.ogg|Verb}#verb form of ", ";Ɣ");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/af/De-@^.ogg|Verb}#verb form of ", "!ƕ");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/99/De-@^.ogg|Verb}#verb form of ", ")ƕ");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bb/De-@^.ogg|Verb}#verb form of ", "`ƕ");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/34/De-@^.ogg|Verb}#verb form of ", "{ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/98/De-@^.ogg|Verb}#verb form of ", "}ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/55/De-@^.ogg|Verb}#verb form of ", "£ƕ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/23/De-@^.ogg|Verb}#verb form of ", "éƕ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/cd/De-@^.ogg|Verb}#verb form of ", "-Ɩ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8f/De-@^.ogg|Verb}#verb form of ", "(Ɩ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/02/De-@^.ogg|Verb}#verb form of ", "?Ɩ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7f/De-@^.ogg|Verb}#verb form of ", "'Ɩ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9d/De-@^.ogg|Verb}#verb form of ", "!Ɨ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/ee/De-@^.ogg|Verb}#verb form of ", ")Ɨ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ4/47/De-@^.ogg|Verb}#verb form of ", "`Ɨ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8b/De-@^.ogg|Verb}#verb form of ", "{Ƙ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/ac/De-@^.ogg|Verb}#verb form of ", "}Ƙ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/8e/De-@^.ogg|Verb}#verb form of ", "£Ƙ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5f/De-@^.ogg|Verb}#verb form of ", "éƘ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/94/De-@^.ogg|Verb}#verb form of ", "{ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/08/De-@^.ogg|Verb}#verb form of ", "}ƙ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/05/De-@^.ogg|Verb}#verb form of ", "`ƚ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3c/De-@^.ogg|Verb}#verb form of ", "!ƛ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/83/De-@^.ogg|Verb}#verb form of ", ")ƛ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/97/De-@^.ogg|Verb}#verb form of ", "<Ɯ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/aa/De-@^.ogg|Verb}#verb form of ", ">Ɯ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a4/De-@^.ogg|Verb}#verb form of ", "?ƞ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/73/De-@^.ogg|Verb}#verb form of ", "/É");//68
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/38/De-@^.ogg|Verb}#verb form of ", ".Ƚ");//66
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5e/De-@^.ogg|Noun}#Form of ", "_Ɠ");//93
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/91/De-@^.ogg|Noun}#Form of ", "`Ɣ");//91
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/97/De-@^.ogg|Noun}#Form of ", "_Ɩ");//88
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/75/De-@^.ogg|Noun}#Form of ", ".Ɩ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/ff/De-@^.ogg|Noun}#Form of ", "/Ɩ");//87
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e3/De-@^.ogg|Noun}#Form of ", "@ƙ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/19/De-@^.ogg|Noun}#Form of ", "_ƙ");//84
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2f/De-@^.ogg|Noun}#Form of ", "£ƙ");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c1/De-@^.ogg|Noun}#Form of ", "éƙ");//83
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0b/De-@^.ogg|Noun}#Form of ", "+ƚ");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/e6/De-@^.ogg|Noun}#Form of ", ".ƚ");//82
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/56/De-@^.ogg|Noun}#Form of ", "&ƛ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2d/De-@^.ogg|Noun}#Form of ", "$ƛ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3f/De-@^.ogg|Noun}#Form of ", "<ƛ");//81
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2a/De-@^.ogg|Noun}#Form of ", "~Ɯ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c6/De-@^.ogg|Noun}#Form of ", "&Ɯ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/6d/De-@^.ogg|Noun}#Form of ", "$Ɯ");//80
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/38/De-@^.ogg|Noun}#Form of ", ">Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/87/De-@^.ogg|Noun}#Form of ", "!Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b5/De-@^.ogg|Noun}#Form of ", ")Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓe/eb/De-@^.ogg|Noun}#Form of ", "`Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ9/9e/De-@^.ogg|Noun}#Form of ", ":Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d6/De-@^.ogg|Noun}#Form of ", ";Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/81/De-@^.ogg|Noun}#Form of ", "=Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓd/d9/De-@^.ogg|Noun}#Form of ", "-Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/c5/De-@^.ogg|Noun}#Form of ", "(Ɲ");//79
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ca/De-@^.ogg|Noun}#Form of ", "$ƞ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fa/De-@^.ogg|Noun}#Form of ", "<ƞ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓc/ce/De-@^.ogg|Noun}#Form of ", ">ƞ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/1a/De-@^.ogg|Noun}#Form of ", "!ƞ");//78
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b9/De-@^.ogg|Noun}#Form of ", "£ƞ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/69/De-@^.ogg|Noun}#Form of ", "éƞ");//77
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/57/De-@^.ogg|Noun}#Form of ", "£Ɵ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ2/2e/De-@^.ogg|Noun}#Form of ", "éƟ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/fc/De-@^.ogg|Noun}#Form of ", "@Ɔ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/77/De-@^.ogg|Noun}#Form of ", "_Ɔ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0a/De-@^.ogg|Noun}#Form of ", "^Ɔ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/b8/De-@^.ogg|Noun}#Form of ", "~Ɔ");//76
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/04/De-@^.ogg|Noun}#Form of ", "+Ɔ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3d/De-@^.ogg|Noun}#Form of ", ".Ɔ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓf/f1/De-@^.ogg|Noun}#Form of ", "/Ɔ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bc/De-@^.ogg|Noun}#Form of ", "%Ɔ");//75
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/70/De-@^.ogg|Noun}#Form of ", "}É");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/67/De-@^.ogg|Noun}#Form of ", "£É");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓb/bf/De-@^.ogg|Noun}#Form of ", "éÉ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/34/De-@^.ogg|Noun}#Form of ", "@Ƕ");//74
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/55/De-@^.ogg|Noun}#Form of ", "?Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/18/De-@^.ogg|Noun}#Form of ", "'Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/3c/De-@^.ogg|Noun}#Form of ", "+Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓa/a0/De-@^.ogg|Noun}#Form of ", ".Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/13/De-@^.ogg|Noun}#Form of ", "/Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7d/De-@^.ogg|Noun}#Form of ", "%Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0c/De-@^.ogg|Noun}#Form of ", ",Ƕ");//73
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/01/De-@^.ogg|Noun}#Form of ", "/Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ6/66/De-@^.ogg|Noun}#Form of ", "%Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/73/De-@^.ogg|Noun}#Form of ", ",Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ8/80/De-@^.ogg|Noun}#Form of ", "{Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ1/16/De-@^.ogg|Noun}#Form of ", "}Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ0/0e/De-@^.ogg|Noun}#Form of ", "£Ƚ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ7/7b/De-@^.ogg|Noun}#Form of ", "éȽ");//72
		mapofdecoders.put("German{}|Pronunciation}Ⓓ3/39/De-@^.ogg|Noun}#Form of ", "£Ƞ");//71
		mapofdecoders.put("German{}|Pronunciation}Ⓓ5/5f/De-@^.ogg|Noun}#Form of ", "éȠ");//71
		mapofdecoders.put("De-@^.ogg|Adjective}#@-4|Adjective}#Form of [@-4]", "@5");//2200
		mapofdecoders.put("De-@^.ogg|Participle}#past participle of [@-1en]", "%7");//1155
		mapofdecoders.put("De-^@.ogg|Adjective}#@-4|Adjective}#Form of [@-4]", "é9");//654
		mapofdecoders.put("De-@^.ogg|Adjective}#@-5|Adjective}#Form of [@-5]", "`a");//624
		mapofdecoders.put("De-@^.ogg|Noun}(neut.,genitive:@^es,plural:[@^er]", "&H");//185
		mapofdecoders.put("De-^@.ogg|Adjective}#@-5|Adjective}#Form of [@-5]", "/X");//124
		mapofdecoders.put("German{}|Adjective}#@-4|Adjective}#Form of [@-4]", "%c");//467
		mapofdecoders.put("De-^@.ogg|Participle}#past participle of [@-1en]", "@H");//190
		mapofdecoders.put("German{}|Adjective}#@-5|Adjective}#Form of [@-5]", ";ı");//110
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,genitive:@^es,plural:[@^e]", "=Ɣ");//100
		mapofdecoders.put("@^.ogg|Adjective}#@-4|Adjective}#Form of [@-4]", "<5");//2200
		mapofdecoders.put("De-@^.ogg|Participle}#past participle of [@-1n]", ";ʁ");//279
		mapofdecoders.put("German{}|Etymology}Borrowed fr.|Pronunciation}Ⓓ", "£F");//231
		mapofdecoders.put("German{}|Etymology}Borrowed la.|Pronunciation}Ⓓ", "£ç");//128
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,genitive:@^s,plural:[@^s]", ";ß");//111
		mapofdecoders.put("German{}|Participle}#past participle of [@-1en]", "?Ɣ");//102
		mapofdecoders.put("@^.ogg|Participle}#past participle of [@-1en]", "^8");//1148
		mapofdecoders.put("@^.ogg|Adjective}#@-5|Adjective}#Form of [@-5]", "£a");//624
		mapofdecoders.put("@^.ogg|Noun}(neut.,genitive:@^es,plural:[@^er]", "£I");//170
		mapofdecoders.put("German{}|Etymology}Borrowed en.|Pronunciation}Ⓓ", "_ƚ");//93
		mapofdecoders.put("@.ogg|Adjective}#@-4|Adjective}#Form of [@-4]", "?a");//654
		mapofdecoders.put("@.ogg|Adjective}#@-5|Adjective}#Form of [@-5]", "!ü");//124
		mapofdecoders.put("/De-@^.ogg|Participle}#past participle of [", "=3");//3924
		mapofdecoders.put("German{}|Adjective}#alternative spelling of ", "éA");//282
		mapofdecoders.put("@^.ogg|Participle}#past participle of [@-1n]", "{B");//279
		mapofdecoders.put("@.ogg|Participle}#past participle of [@-1en]", "@I");//188
		mapofdecoders.put("@^.ogg|Noun}(mask.,genitive:@^s,plural:[@^s]", "<Ɣ");//110
		mapofdecoders.put("@^.ogg|Noun}(mask.,genitive:@^es,plural:[@^e]", "&É");//88
		mapofdecoders.put("German{}|Etymology}From Middle High German", "£3");//3790
		mapofdecoders.put(".ogg|Adjective}#@-4|Adjective}#Form of @-4", "+4");//2855
		mapofdecoders.put("German{}|Adjective}#@-4|Adjective}#Form of ", "<e");//467
		mapofdecoders.put("German{}|Adjective}#@-5|Adjective}#Form of ", ":ƕ");//110
		mapofdecoders.put("De-@^.ogg|Adjective}#comparative of [@-2]", "`4");//2966
		mapofdecoders.put(".ogg|Participle}#past participle of @-1en", "{7");//1347
		mapofdecoders.put(".ogg|Adjective}#@-5|Adjective}#Form of @-5", "!a");//748
		mapofdecoders.put("German{}|Etymology}from Middle High German", "=G");//229
		mapofdecoders.put(".ogg|Noun}(neut.,genitive:@^es,plural:@^er", "éI");//185
		mapofdecoders.put("German{}|Noun}+)}#alternative spelling of ", "}Ɨ");//108
		mapofdecoders.put("De-^@.ogg|Adjective}#comparative of [@-2]", "^9");//910
		mapofdecoders.put(".ogg|Noun}(mask.,genitive:@^es,plural:@^e", "+ƞ");//100
		mapofdecoders.put(".ogg|Adjective}#@-4|Adjective}#Form of ", "£4");//2855
		mapofdecoders.put(".ogg|Participle}#past participle of @-1n", "=m");//355
		mapofdecoders.put("German{}|Participle}#past participle of ", ";y");//333
		mapofdecoders.put("German{}|Adjective}#comparative of [@-2]", "=H");//219
		mapofdecoders.put("German{}|Adjective}#alternative form of ", "}é");//136
		mapofdecoders.put(".ogg|Noun}(mask.,genitive:@^s,plural:@^s", "£Ɩ");//115
		mapofdecoders.put("@^.ogg|Adjective}#comparative of [@-2]", "{4");//2963
		mapofdecoders.put(".ogg|Adjective}#@-5|Adjective}#Form of ", "%a");//748
		mapofdecoders.put("German{}|Etymology}From Old High German", "!H");//229
		mapofdecoders.put(".ogg(Austria)(Austria)|Noun}#plural of ", "!W");//160
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-5ehen]", "@İ");//139
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-4ehen]", "`Ƕ");//100
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,plural:[@^(e)s]", "é8");//1051
		mapofdecoders.put("German{}|Etymology}@-2|Pronunciation}Ⓓ", "!M");//184
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [aus@-6n]", ")Ɣ");//127
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [ein@-6n]", "&ƞ");//109
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [aus@-5n]", ";Ƚ");//101
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [auf@-6n]", "_Ƞ");//100
		mapofdecoders.put(".ogg|Participle}#past participle of ", "{3");//4592
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-2en]", "$5");//2901
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-3en]", "?5");//2749
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-4en]", "~8");//1428
		mapofdecoders.put("@.ogg|Adjective}#comparative of [@-2]", ")9");//910
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-3en]", "&b");//773
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-2en]", "%b");//687
		mapofdecoders.put("De-@^.ogg|Noun}(neut.,plural:[@^(e)s]", "$c");//647
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-1en]", "=d");//588
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-4en]", "{f");//453
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [ab@-5n]", "<N");//184
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [ab@-4n]", "{ç");//163
		mapofdecoders.put("De-^@.ogg|Noun}(neut.,plural:[^@(e)s]", "£é");//147
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [an@-5n]", "`ß");//141
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [ab@-3n]", ":ß");//141
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-2ern]", "^Ɠ");//133
		mapofdecoders.put("De-@^.ogg|Noun}#archaic Form of [@-1]", ",ƕ");//127
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [an@-4n]", ">ƛ");//116
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [aus@-4]", "?Ɵ");//109
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [an@-3n]", "{Ƕ");//104
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-2]", ")0");//26663
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-4]", "%0");//18603
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-2n]", "é4");//3155
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-1n]", "@6");//2623
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-3n]", "?8");//1213
		mapofdecoders.put("De-@^.ogg|Participle}#Form of [@-2]", "&9");//1044
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-1en]", "=a");//836
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-2en]", "`f");//478
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-3en]", "£f");//460
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-2n]", "!g");//439
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-1n]", "=i");//415
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-4en]", "`H");//245
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-3n]", "&K");//201
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [@r-4n]", "%T");//178
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [ab@-3]", "+ğ");//160
		mapofdecoders.put("De-@^.ogg|Proper noun}#Form of [@-1]", "+İ");//149
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-5ehen]", "$ƒ");//139
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-1en]", ":Ɨ");//127
		mapofdecoders.put("De-@_.ogg|Verb}#verb form of [an@-3]", "{ƛ");//118
		mapofdecoders.put(".ogg|Adjective}#alternative form of ", "}ƛ");//118
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-5el]", "'Ɲ");//116
		mapofdecoders.put("German{}|Noun}(mask.,plural:[@^(e)s]", "=Ɵ");//113
		mapofdecoders.put("German{}|Verb}#verb form of [@-4ern]", "(Ɔ");//111
		mapofdecoders.put("German{}|Verb}#verb form of [@-3ern]", "?Ɔ");//111
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-1]", "<2");//7284
		mapofdecoders.put("De-^@.ogg|Adjective}#Form of [@-2]", "`2");//6885
		mapofdecoders.put("De-^@.ogg|Adjective}#Form of [@-4]", "&3");//5600
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-3]", "!3");//5094
		mapofdecoders.put(".ogg|Adjective}#comparative of @-2", "$4");//3876
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@^n]", "£5");//2769
		mapofdecoders.put("De-^@.ogg|Adjective}#Form of [@-1]", "<7");//1965
		mapofdecoders.put("De-@^.ogg|Adjective}#Form of [@-5]", ")7");//1886
		mapofdecoders.put("De-^@.ogg|Adjective}#Form of [@-3]", "@8");//1566
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@^en]", "£b");//726
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-2n]", "$e");//586
		mapofdecoders.put("De-@^.ogg|Verb}#verb form of [@-4n]", "!e");//570
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-1n]", "£g");//444
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-3n]", "$G");//296
		mapofdecoders.put("De-@^.ogg|Participle}#Form of [@-1]", "~H");//260
		mapofdecoders.put("German{}|Adjective}#comparative of ", "&I");//233
		mapofdecoders.put("German{}|Verb}#verb form of [@-2en]", "%K");//204
		mapofdecoders.put("German{}|Verb}#verb form of [@-3en]", "$V");//180
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [^@en]", "?ğ");//165
		mapofdecoders.put("De-^@.ogg|Participle}#Form of [@-2]", "'İ");//154
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [@-4n]", ";ƕ");//135
		mapofdecoders.put("@_.ogg|Verb}#verb form of [aus@-6n]", "~ƙ");//127
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,plural:[@^en]", "@ƚ");//125
		mapofdecoders.put("@_.ogg|Verb}#verb form of [ein@-6n]", "@Ƞ");//109
		mapofdecoders.put("/De-@^.ogg|Adjective}#Form of [@", "~0");//59770
		mapofdecoders.put("/De-@^.ogg|Verb}#verb form of [@", "+0");//20337
		mapofdecoders.put("German{}|Adjective}#Form of [@-2]", "~4");//4639
		mapofdecoders.put("German{}|Adjective}#Form of [@-4]", "=4");//3675
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-2en]", "{5");//2901
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-3en]", "^6");//2749
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-4en]", ")8");//1428
		mapofdecoders.put("German{}|Adjective}#Form of [@-1]", "{8");//1276
		mapofdecoders.put("@^.ogg|Noun}(mask.,plural:[@^(e)s]", ";9");//988
		mapofdecoders.put("German{}|Adjective}#Form of [@-3]", "?9");//1015
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-3en]", "=b");//773
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-2en]", "!c");//686
		mapofdecoders.put("@^.ogg|Noun}(neut.,plural:[@^(e)s]", "%d");//613
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-1en]", "`e");//586
		mapofdecoders.put("De-^@.ogg|Verb}#verb form of [^@n]", "%f");//497
		mapofdecoders.put("German{}|Verb}#verb form of [@-2n]", "`g");//464
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-4en]", "$h");//453
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,plural:[@^s]", "$i");//448
		mapofdecoders.put("De-^@.ogg|Adjective}#Form of [@-5]", "=w");//397
		mapofdecoders.put("German{}|Verb}#verb form of [@-3n]", "=C");//357
		mapofdecoders.put("De-@^.ogg|Noun}(neut.,plural:[@^s]", "`F");//325
		mapofdecoders.put("@_.ogg|Verb}#verb form of [ab@-5n]", "<W");//184
		mapofdecoders.put("@_.ogg|Verb}#verb form of [ab@-4n]", "?Ü");//163
		mapofdecoders.put("@_.ogg|Verb}#verb form of [an@-5n]", "-Ɣ");//141
		mapofdecoders.put("@_.ogg|Verb}#verb form of [ab@-3n]", "(Ɣ");//141
		mapofdecoders.put("German{}|Verb}#verb form of [@-1n]", "@Ɩ");//138
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-2ern]", "`Ƙ");//133
		mapofdecoders.put("@_.ogg|Verb}#verb form of [an@-4n]", ".É");//116
		mapofdecoders.put("@^.ogg|Noun}#archaic Form of [@-1]", "_Ƕ");//115
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-2]", "=0");//26653
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-4]", "£0");//18603
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-2n]", ")5");//3155
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-1n]", "%6");//2623
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-3n]", "@9");//1213
		mapofdecoders.put("@^.ogg|Participle}#Form of [@-2]", "{9");//1044
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-1en]", "!b");//835
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-2n]", "!k");//439
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-1n]", "$t");//415
		mapofdecoders.put("German{}|Adjective}#Form of [@-5]", "&G");//315
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-3n]", "&Q");//201
		mapofdecoders.put("@_.ogg|Verb}#verb form of [@r-4n]", ";ö");//178
		mapofdecoders.put("German{}|Verb}#verb form of [@^n]", "?Ö");//176
		mapofdecoders.put("@_.ogg|Verb}#verb form of [ab@-3]", ":ı");//160
		mapofdecoders.put("De-@^.ogg|Noun}(fem.,plural:[@^s]", "&Ƒ");//155
		mapofdecoders.put("@^.ogg|Proper noun}#Form of [@-1]", "~Ɠ");//149
		mapofdecoders.put("@.ogg|Noun}(neut.,plural:[^@(e)s]", ";Ɨ");//138
		mapofdecoders.put("@_.ogg|Verb}#verb form of [an@-3]", ":Ƕ");//118
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-5el]", "+Ƚ");//116
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-1]", ";2");//7284
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-3]", "é3");//5094
		mapofdecoders.put(".ogg|Adjective}#comparative of ", ")4");//4029
		mapofdecoders.put("De-@^.ogg|Noun}#plural of [@-1]", "=5");//3197
		mapofdecoders.put("De-@^.ogg|Noun}#plural of [@-2]", "`6");//2747
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@^n]", "=6");//2747
		mapofdecoders.put("@^.ogg|Adjective}#Form of [@-5]", "'7");//1886
		mapofdecoders.put("Participle}#past participle of ", "$8");//1603
		mapofdecoders.put("De-@^.ogg|Noun}(mask.,genitive:", ";8");//1459
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@^en]", "=c");//722
		mapofdecoders.put("@^.ogg|Verb}#verb form of [@-4n]", "£e");//570
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-2en]", "£h");//478
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-3en]", "<j");//460
		mapofdecoders.put("@^.ogg|Participle}#Form of [@-1]", "éH");//260
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-4en]", "{I");//245
		mapofdecoders.put("De-@^.ogg|Numeral}#Form of [@-1]", ">ƕ");//149
		mapofdecoders.put(".ogg|Verb}#verb form of @r-5ehen", "^ƙ");//139
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-1en]", "-Ɵ");//127
		mapofdecoders.put("@^.ogg|Noun}(mask.,plural:[@^en]", ",É");//123
		mapofdecoders.put("De-@^.ogg|Noun}#Form of [@-1]", "`1");//12309
		mapofdecoders.put("@.ogg|Adjective}#Form of [@-2]", "?2");//6885
		mapofdecoders.put("@.ogg|Adjective}#Form of [@-4]", "?3");//5600
		mapofdecoders.put("/De-@^.ogg|Noun}(mask.,plural:", "&7");//2409
		mapofdecoders.put("@.ogg|Adjective}#Form of [@-1]", "?7");//1965
		mapofdecoders.put("@.ogg|Adjective}#Form of [@-3]", "`8");//1566
		mapofdecoders.put("German{}|Noun}#plural of [@-1]", "$9");//1170
		mapofdecoders.put("German{}|Noun}#plural of [@-2]", "<9");//1166
		mapofdecoders.put(".ogg|Noun}(mask.,plural:@^(e)s", "$a");//1060
		mapofdecoders.put("De-^@.ogg|Noun}#plural of [@-2]", "£c");//712
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-2n]", "$f");//586
		mapofdecoders.put("De-@^.ogg|Noun}#plural of [@-3]", "$g");//533
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-2en]", "!l");//461
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-1n]", ">r");//444
		mapofdecoders.put("De-^@.ogg|Noun}#plural of [@-1]", "@ɔ");//421
		mapofdecoders.put("@^.ogg|Noun}(mask.,plural:[@^s]", "@ɪ");//416
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-3en]", "£E");//376
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-1en]", "!F");//358
		mapofdecoders.put("@^.ogg|Noun}(neut.,plural:[@^s]", "{G");//305
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-3n]", "éG");//296
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-4en]", "~Ğ");//184
		mapofdecoders.put("@^.wav|Adjective}#Form of [@-2]", "=Ğ");//183
		mapofdecoders.put("De-^@.ogg|Noun}#plural of [@-3]", "$ı");//171
		mapofdecoders.put("@.ogg|Verb}#verb form of [^@en]", "~Ƒ");//165
		mapofdecoders.put("@.ogg|Participle}#Form of [@-2]", "<ƕ");//154
		mapofdecoders.put("German{}|Etymology}Compound of ", ":Ƙ");//145
		mapofdecoders.put("@.ogg|Verb}#verb form of [@-4n]", "?Ɲ");//135
		mapofdecoders.put("@^.wav|Adjective}#Form of [@-4]", "(Ɵ");//131
		mapofdecoders.put(".ogg|Verb}#verb form of aus@-6n", "%É");//127
		mapofdecoders.put("German{}|Adjective}#Form of ", "%1");//11200
		mapofdecoders.put("De-@^.ogg|Noun}#Form of [@-2]", "£2");//6601
		mapofdecoders.put("De-^@.ogg|Noun}#Form of [@-1]", "`5");//3420
		mapofdecoders.put(".ogg|Verb}#verb form of @-2en", "%5");//3379
		mapofdecoders.put(".ogg|Verb}#verb form of @-3en", "é5");//3209
		mapofdecoders.put("De-^@.ogg|Noun}#Form of [@-2]", "=7");//2058
		mapofdecoders.put(".ogg|Verb}#verb form of @-4en", "<8");//1673
		mapofdecoders.put(".ogg|Verb}#verb form of @r-3en", "`c");//773
		mapofdecoders.put(".ogg|Verb}#verb form of @r-2en", "£d");//688
		mapofdecoders.put(".ogg|Noun}(neut.,plural:@^(e)s", "?e");//655
		mapofdecoders.put(".ogg|Verb}#verb form of @r-1en", "!f");//588
		mapofdecoders.put("German{}|Etymology}from German", "?f");//567
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-2n]", "!i");//505
		mapofdecoders.put("@.ogg|Verb}#verb form of [^@n]", "&j");//493
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-1n]", "<u");//455
		mapofdecoders.put(".ogg|Verb}#verb form of @r-4en", "£u");//453
		mapofdecoders.put("@.ogg|Adjective}#Form of [@-5]", "{D");//397
		mapofdecoders.put("German{}|Noun}#plural of [@-3]", "=K");//238
		mapofdecoders.put(".ogg|Adjective}#relational of ", "&ü");//187
		mapofdecoders.put(".ogg|Verb}#verb form of ab@-5n", ".Ü");//184
		mapofdecoders.put(".ogg|Verb}#verb form of @-2ern", "%ƒ");//165
		mapofdecoders.put(".ogg|Verb}#verb form of ab@-4n", "=Ɠ");//163
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-3n]", ")Ƙ");//151
		mapofdecoders.put(".ogg|Noun}(neut.,plural:^@(e)s", ",Ƙ");//149
		mapofdecoders.put(".ogg|Verb}#verb form of an@-5n", "!Ɯ");//141
		mapofdecoders.put(".ogg|Verb}#verb form of ab@-3n", ")Ɯ");//141
		mapofdecoders.put("German{}|Etymology}From German", ".Ɯ");//140
		mapofdecoders.put("@^.ogg|Noun}(fem.,plural:[@^s]", "+Ɲ");//139
		mapofdecoders.put(".ogg|Noun}#archaic Form of @-1", "'Ɔ");//133
		mapofdecoders.put(".ogg|Adjective}#Form of @-2", "`0");//33565
		mapofdecoders.put(".ogg|Adjective}#Form of @-4", "?0");//24205
		mapofdecoders.put("German{}|Noun}#Form of [@-1]", ";3");//6037
		mapofdecoders.put("German{}|Verb}#verb form of ", "?4");//4328
		mapofdecoders.put(".ogg|Verb}#verb form of @-2n", "&5");//3741
		mapofdecoders.put("@^.ogg|Noun}#plural of [@-1]", "~6");//3185
		mapofdecoders.put(".ogg|Verb}#verb form of @-1n", "<6");//3067
		mapofdecoders.put("German{}|Noun}#Form of [@-2]", "é6");//2885
		mapofdecoders.put("@^.ogg|Noun}#plural of [@-2]", "^7");//2732
		mapofdecoders.put(".ogg|Verb}#verb form of @-3n", "+8");//1509
		mapofdecoders.put(".ogg|Participle}#Form of @-2", "=9");//1198
		mapofdecoders.put(".ogg|Verb}#verb form of @-1en", "$b");//964
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@^n]", "=v");//467
		mapofdecoders.put(".ogg|Verb}#verb form of @r-2n", "&̯");//439
		mapofdecoders.put(".ogg|Verb}#verb form of @r-1n", "£C");//416
		mapofdecoders.put("De-^@.ogg|Noun}#Form of [@-3]", "=F");//381
		mapofdecoders.put("De-@^.ogg|Noun}#Form of [@-3]", "%G");//327
		mapofdecoders.put("De-@^.ogg|Verb}#Form of [@-1]", "$H");//311
		mapofdecoders.put(".ogg|Verb}#verb form of @r-3n", "=Ö");//201
		mapofdecoders.put(".ogg|Proper noun}#Form of @-1", "/ß");//178
		mapofdecoders.put(".ogg|Verb}#verb form of @r-4n", "%ß");//178
		mapofdecoders.put(".ogg|Verb}#verb form of ab@-3", "=Ɩ");//160
		mapofdecoders.put("@^.ogg|Numeral}#Form of [@-1]", "/ƚ");//149
		mapofdecoders.put("@^.ogg|Noun}#Form of [@-1]", "?1");//12192
		mapofdecoders.put(".ogg|Adjective}#Form of @-1", "$2");//9253
		mapofdecoders.put("German{}|Alternative forms}", "{2");//7615
		mapofdecoders.put(".ogg|Adjective}#Form of @-3", "$3");//6660
		mapofdecoders.put(".ogg|Verb}#verb form of @^n", "~7");//2773
		mapofdecoders.put(".ogg|Adjective}#Form of @-5", "`7");//2283
		mapofdecoders.put(".ogg|Verb}#verb form of @^en", "{d");//739
		mapofdecoders.put(".ogg|Verb}#verb form of @-4n", "=e");//705
		mapofdecoders.put("@^.ogg|Noun}#plural of [@-3]", "£i");//531
		mapofdecoders.put("@-4|Adjective}#Form of [@-4]", "%v");//483
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-2en]", "éɔ");//461
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-3en]", "~G");//376
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-1en]", "`G");//358
		mapofdecoders.put(".ogg|Participle}#Form of @-1", "{H");//301
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-4en]", "^Ƒ");//184
		mapofdecoders.put(".ogg|Verb}#verb form of ^@en", "+Ɩ");//165
		mapofdecoders.put("German{}|Noun}#Form of [@-3]", "{Ɨ");//162
		mapofdecoders.put("German{}|Pronunciation}Ⓓ", "^0");//226948
		mapofdecoders.put("German{}|Pronunciation}^Ⓓ", "$0");//45248
		mapofdecoders.put(".ogg|Adjective}#Form of [", "<1");//18531
		mapofdecoders.put("@^.ogg|Noun}#Form of [@-2]", "`3");//6509
		mapofdecoders.put("}#colloquial verb form of [", "{c");//829
		mapofdecoders.put(".ogg|Verb}#zu-inifinite of ", "`d");//813
		mapofdecoders.put("@.ogg|Noun}#plural of [@-2]", "%e");//712
		mapofdecoders.put("De-@^.ogg|Noun}(fem.,plural", "{e");//679
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-2n]", "!u");//505
		mapofdecoders.put(".ogg|Verb}#verb form of ^@n", "&x");//498
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-1n]", "`B");//455
		mapofdecoders.put(".ogg|Noun}(mask.,plural:@^s", "=B");//455
		mapofdecoders.put("@.ogg|Noun}#plural of [@-1]", "~F");//421
		mapofdecoders.put(".ogg|Noun}(neut.,plural:@^s", "<H");//332
		mapofdecoders.put("|Proper noun}(mask.,plural:", "£H");//310
		mapofdecoders.put("@.ogg|Noun}#plural of [@-3]", ";Ɩ");//172
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-3n]", "@Ɵ");//151
		mapofdecoders.put("German{}|Pronunciation}", "@0");//266655
		mapofdecoders.put(".ogg|Adjective}#Form of ", "&0");//77857
		mapofdecoders.put(".ogg|Verb}#verb form of ", "<0");//45859
		mapofdecoders.put("@.ogg|Noun}#Form of [@-1]", "?6");//3404
		mapofdecoders.put("German{}|Noun}#plural of ", "$7");//2820
		mapofdecoders.put("@.ogg|Noun}#Form of [@-2]", "&8");//2047
		mapofdecoders.put(".ogg|Participle}#Form of ", "_9");//1516
		mapofdecoders.put("@^.ogg|Verb}#Form of [@^n]", "`C");//467
		mapofdecoders.put("@^.wav|Noun}#Form of [@-1]", "@G");//414
		mapofdecoders.put("@^.ogg|Noun}#Form of [@-3]", "%H");//327
		mapofdecoders.put("@^.ogg|Verb}#Form of [@-1]", "$I");//311
		mapofdecoders.put(".ogg|Proper noun}#Form of ", "@Y");//239
		mapofdecoders.put("@^.wav|Noun}#Form of [@-2]", "(ß");//199
		mapofdecoders.put(".ogg|Noun}(fem.,plural:@^s", "'ƞ");//158
		mapofdecoders.put("From Middle High German ", "<4");//5395
		mapofdecoders.put(".ogg|Noun}#plural of @-1", "&6");//3648
		mapofdecoders.put(".ogg|Noun}#plural of @-2", "£6");//3472
		mapofdecoders.put("De-@^.ogg|Noun}(mask.)}#", "_7");//3226
		mapofdecoders.put("from Proto-West Germanic", "é7");//2271
		mapofdecoders.put("@.ogg|Noun}#Form of [@-3]", "£G");//378
		mapofdecoders.put(".ogg|Numeral}#Form of @-1", "{É");//157
		mapofdecoders.put(".ogg|Noun}#Form of @-1", "=1");//15783
		mapofdecoders.put("|Adjective}#Form of [@", "é1");//11772
		mapofdecoders.put("German{}|Noun}#Form of ", "=2");//9622
		mapofdecoders.put("from Middle High German", "£9");//1449
		mapofdecoders.put("#alternative spelling of", "?c");//945
		mapofdecoders.put(".ogg|Noun}#plural of @-3", "=f");//709
		mapofdecoders.put(".ogg|Verb}#Form of @-2en", "£p");//578
		mapofdecoders.put(".ogg|Verb}#Form of @-3en", "{E");//491
		mapofdecoders.put(".ogg|Verb}#Form of @-1en", "!G");//426
		mapofdecoders.put(".ogg|Verb}#Form of @-4en", "'ğ");//240
		mapofdecoders.put(".ogg|Noun}#Form of @-2", "~3");//8682
		mapofdecoders.put("|Noun}(neut.,genitive:", "!9");//1552
		mapofdecoders.put("German{}|Verb}#Form of ", "$d");//959
		mapofdecoders.put(".ogg|Verb}#Form of @-2n", "$r");//600
		mapofdecoders.put(".ogg|Verb}#Form of @-1n", "}̯");//545
		mapofdecoders.put(".ogg|Verb}#Form of @-3n", "}ƞ");//178
		mapofdecoders.put(".ogg|Noun}#plural of ", "<3");//8539
		mapofdecoders.put("German{}|Proper noun}", "!8");//2258
		mapofdecoders.put(".ogg|Noun}#Form of @-3", "=g");//715
		mapofdecoders.put(".ogg|Verb}#Form of @^n", "<G");//467
		mapofdecoders.put("from Middle Low German", "?H");//393
		mapofdecoders.put(".ogg|Verb}#Form of @-1", "_I");//371
		mapofdecoders.put("German{}|Etymology}", "!0");//53561
		mapofdecoders.put(".ogg|Noun}#Form of ", "&1");//26246
		mapofdecoders.put("German{}|Adjective}", "&2");//13459
		mapofdecoders.put("Verb}#verb form of [", ")6");//4270
		mapofdecoders.put("De-@^.ogg|Adjective}", "@7");//3912
		mapofdecoders.put("|Noun}(neut.,plural:", "!7");//3247
		mapofdecoders.put("|Noun}(mask.,plural:", "£7");//2747
		mapofdecoders.put("from Old High German", "`b");//1373
		mapofdecoders.put("German{}|Etymology 1}", "$J");//360
		mapofdecoders.put("From Old High German ", "$Q");//315
		mapofdecoders.put(".ogg|Verb}#Form of ", "!4");//6714
		mapofdecoders.put("German{}from German", "£8");//2206
		mapofdecoders.put("German{}|Participle}", "£m");//705
		mapofdecoders.put("|Pronunciation}", "é0");//35955
		mapofdecoders.put("German{}|Noun}", "£1");//21315
		mapofdecoders.put("|Noun}#Form of ", "&4");//9713
		mapofdecoders.put("German{}|Adverb}", "}Ɣ");//299
		mapofdecoders.put("German{}|Verb}", "$6");//6186
		mapofdecoders.put("|Noun}(fem.)}#", "=8");//3109
		mapofdecoders.put("|Proper noun}", "`9");//2588
		mapofdecoders.put("|Adjective}#", "?b");//2131
		mapofdecoders.put("@^", "@^");//xxx



		List<Map.Entry<String, String>> entries =
				  new ArrayList<Map.Entry<String, String>>(mapofdecoders.entrySet());
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

		return mapofdecoders;
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
		string=string.replace("#\n", "").replace("#}", "").trim();
		//Toast.makeText(mContext, word+": "+ string, Toast.LENGTH_LONG).show();
		//string= ReplaceEncodedHeader(string,word);
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
					String wordDecoded=w.peyv;
					if(SelectedWord!=null&&SelectedWord.contains("^")&&w.NormalizedWord.length()!=SelectedWord.length())
							wordDecoded=ReplaceEncodedChars(wordDecoded,w.NormalizedWord);
					SetExpanderCollection(wordDecoded, def);
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
							String peyv_n=WQDictionaryQueryProvider. GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
							if(peyv==null||peyv.equals(""))
								peyv=peyv_n;
							else if(peyv.contains("^")&&peyv_n.length()!=peyv.length())
								peyv=ReplaceEncodedChars(peyv,peyv_n);

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
						//makeText("SetListaadapter2");
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
				if(wiki.equalsIgnoreCase("tr")||wiki.equalsIgnoreCase("de"))
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
	private void ViewWordTypeList(String strLang)
	{
		Activity myActivity=this;

		Intent intent = new Intent(getBaseContext(),
				WordTypeListActivity.class);

		String[] wordpieces = strLang.split(java.util.regex.Pattern.quote(":"));
		if(wordpieces!=null&&wordpieces.length>0)
			strLang=wordpieces[0].trim();

		Bundle b = new Bundle();
		b.putString("ziman", strLang);

		intent.putExtras(b);
		myActivity.startActivity(intent);
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
			if(wiki.equalsIgnoreCase("de"))
			{
				ViewWordTypeList("German");
			}
			else {
				ViewWordList();
			}
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
			//makeText("SetListaadapter3");

		}
		else
		{
			makeText("Favourite List is empty");
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
				+ "http://en.wiktionary.org/wiki/"+wordw+ " \n\n";
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
		if(word!= null&&!word.equalsIgnoreCase(""))
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
				String url = "https://en.wiktionary.org/wiki/"
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
