package com.wqferheng;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.provider.Settings;
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
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.wqferheng.GroupEntity.GroupItemEntity;
import com.wqferheng.SearchResultAdapter.ViewHolderWords;
import com.wqferheng.WQFerhengDB.WQFerhengDBOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


public class WQFerhengActivity extends AppCompatActivity implements OnClickListener {

	public static List<Words> listofWords;
	public static boolean ShowSpecialKeys=true;
	public static boolean ShowHeaderTranslation=true;
	public static boolean ShowLanguageTranslation=true;
	public static String strtheme;
	public static String linkColor;
	public static int theme;
	//AdView mAdView;
	com.facebook.ads.AdView adView;
	private static final int REQUEST_CODE_SPEECH_INPUT=1000;
	private static final int REQUEST_CODE_CONFIG=1001;
	//public static int position=-1;
	Boolean IsButtonsVisible = true;
	public static Map<String, List<String>> mapLanguages;
	public static int LocalisationIndex=-1;
	 //AdView   mAdView;
	 private final String TAG = WQFerhengActivity.class.getSimpleName();

	CustomAutoCompleteTextView autoCmopletetextView;
	public OnSwipeTouchListener onSwipeListener;
	public static Context cont;
	public ExpandableListView mExpandableListView;	
	private static final String LIST_STATE_KEY = "listState";
	private static final String LIST_POSITION_KEY = "listPosition";
	private static final String ITEM_POSITION_KEY = "itemPosition";
	public static SQLiteDatabase mDatabase;
	ImageView imgvoicebutton;
	//View footerView ;
	//Button footerButton;
	public static AppCompatActivity mContext=null;
	private List<GroupEntity> mGroupCollection;
	ExpandableListAdapter adapter;
	LinearLayout layoutListViewContents;
	int height, wwidth;
	public static String typeToSearch = "hemû";
	public static String zimanquery = "Kurdî";
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
	Uri uriDB = WQFerhengDBProvider.CONTENT_URI;
	ImageView buttonhere;
	TextView textHistory;
	TextProgressBar progressbar2;
	ListView listviewresult;
	TimerTask task;
	Boolean IsCustomKeyboardVisible = false;
	CountDownTimer countDownTimer;
	int ItemCount = WQFerhengDBOpenHelper.WordList;
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
	// RelativeLayout relativeLayoutKeyboard;
	String[] columnsDB = new String[] {WQFerhengDB.KEY_WORD,
			WQFerhengDB.KEY_DEFINITION};
	static Boolean dialogshowed = false;
	int[] to = new int[] { R.id.word, R.id.definition };
	ArrayList<SearchItem> SearchHistory = new ArrayList<SearchItem>();
	String[] items = new String[5];
	long id = 2;
	WQFerhengQueryProvider provider;
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

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext=this;
		toggleTheme("");
		SetLanguage();
		//setTheme(R.style.MyCustomTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wqferheng);
		// Initialize the Facebook Audience Network SDK
		AudienceNetworkAds.initialize(this);
		cont = this;
		init();

		encoderList=GetDecodeList();
		headerEndChar = getString(R.string.headerEndChar);
		newlinebreak = getString(R.string.newlinebreak);


		if(currentVersionNumber>20)
		{
			headerEndChar="{";
			newlinebreak
			="}";
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
				actionBar.setIcon(R.drawable.wqferheng); // Your icon resource
			}
			//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#11315E")));
		}
		else
		{
			
		}
		AdSettings.addTestDevice("7FA4D05EAE25EA144CF59A8726F126C");

		//showAdmob();
//		String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//		String deviceId = md5(android_id).toUpperCase();
//		Log.i("device id=",deviceId);
//		AdSettings.addTestDevice("6986fd2c-98f0-49fb-a4f0-3935c291fbe0");
//		MobileAds.initialize(this, new OnInitializationCompleteListener() {
//			@Override
//			public void onInitializationComplete(InitializationStatus initializationStatus) {
//				try {
//
//					showAdmob();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		});

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
						//w=GetSingleExactWord(data.get(WQFerhengDB.KEY_WORD));
						String word=data.get(WQFerhengDB.KEY_WORD);
//						Words ww =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(w.id);
//						Log.d("ww.id", w.id);
//						if(ww!=null)
//						{
						//Log.d("SimpleAdapter","SimpleAdapter");
						Intent intent = new Intent(cont, DefinitionActivity.class);

						Bundle b = new Bundle();
						
						//String word=w.peyv;
						
						if(word!=null)
						b.putString("word", word.replace("\"", ""));
						else
						{
							word=w.NormalizedWord;
							b.putString("word", word);
						}
						//Log.d("ww.peyuv", word);
						b.putInt("position", position);
						intent.putExtras(b);

						cont.startActivity(intent);
						overridePendingTransition(android. R.anim.fade_in,android. R.anim.fade_out);
						//}
											
					}
					else 	if(listviewresult.getAdapter() instanceof  SearchResultAdapter)
					{
						Log.d("SearchResultAdapter","SearchResultAdapter");
						SearchResultAdapter myadapter =(SearchResultAdapter)listviewresult.getAdapter();
						
						w=myadapter.listOfWords.get(position);
						
						if(w==null)
							return;
					SaveScrollPosition();
				
					Words ww =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(w.id);
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
			progressbar2.setText("Peyvên berê tên jê birin..");

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
				mDatabase =WQFerhengDB.mWQferhengDBOpenHelper.getReadableDatabase();
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
				if (WQFerhengDBOpenHelper.Loading) {
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
		
		provider=new WQFerhengQueryProvider(getBaseContext());

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
		if (upgrating) {
			Log.d("TAG", "showing what is new dialog");
			showWhatsNewDialog();
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

		String pref=WQFerhengConfig. loadUrlPref(this.getBaseContext(),
				"SpecialKeys");

		if(pref.equalsIgnoreCase("false")) {
			ShowSpecialKeys = false;
			ShowHideSpecialKeys(ShowSpecialKeys);
		}
		pref=WQFerhengConfig. loadUrlPref(this.getBaseContext(),
					"HeaderTranslation");
		if(pref.equalsIgnoreCase("false"))
			ShowHeaderTranslation=false;

			pref=WQFerhengConfig. loadUrlPref(this.getBaseContext(),
				"LangTranslation");
		if(pref.equalsIgnoreCase("false"))
			ShowLanguageTranslation=false;
		setListenerToRootView();
		int themeResId;
		try {
			PackageManager packageManager = getPackageManager();
			//ActivityInfo activityInfo = packageManager.getActivityInfo(getCallingActivity(), PackageManager.GET_META_DATA);
			ActivityInfo activityInfo = packageManager.getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			themeResId = activityInfo.theme;

		}
		catch(PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Could not get themeResId for activity", e);
			themeResId = -1;
		}
		final Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(false)
					test();
			}
		}, 500);
		//AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
	}
//private void consent()
//{
//	// Create a ConsentRequestParameters object.
//	ConsentRequestParameters params = new ConsentRequestParameters
//			.Builder()
//			.build();
//
//
//
//	consentInformation = UserMessagingPlatform.getConsentInformation(this);
//	consentInformation.requestConsentInfoUpdate(
//			this,
//			params,
//			(ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
//				UserMessagingPlatform.loadAndShowConsentFormIfRequired(
//						this,
//						(ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
//							if (loadAndShowError != null) {
//								// Consent gathering failed.
//								Log.w(TAG, String.format("%s: %s",
//										loadAndShowError.getErrorCode(),
//										loadAndShowError.getMessage()));
//							}
//
//							// Consent has been gathered.
//							if (consentInformation.canRequestAds()) {
//								initializeMobileAdsSdk();
//							}
//						}
//				);
//			},
//			(ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
//				// Consent gathering failed.
//				Log.w(TAG, String.format("%s: %s",
//						requestConsentError.getErrorCode(),
//						requestConsentError.getMessage()));
//			});
//
//	// Check if you can initialize the Google Mobile Ads SDK in parallel
//	// while checking for new consent information. Consent obtained in
//	// the previous session can be used to request ads.
//	if (consentInformation.canRequestAds()) {
//		initializeMobileAdsSdk();
//	}
//
//}
public String md5(String s) {
	try {
		// Create MD5 Hash
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		digest.update(s.getBytes());
		byte messageDigest[] = digest.digest();

		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		for (int i=0; i<messageDigest.length; i++)
			hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		return hexString.toString();

	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	return "";
}
	private void initializeMobileAdsSdk() {
		if (isMobileAdsInitializeCalled.getAndSet(true)) {
			return;
		}

		new Thread(
				() -> {
					// Initialize the Google Mobile Ads SDK on a background thread.
					MobileAds.initialize(this, initializationStatus -> {});
					runOnUiThread(
							() -> {
								// TODO: Request an ad.
								// InterstitialAd.load(...);
							});
				})
				.start();
	}

	private void test()  {
		String strcountofword=WQFerhengDB.mWQferhengDBOpenHelper.GetCount("FTSdictionary");
		String strcountofworddefs=WQFerhengDB.mWQferhengDBOpenHelper.GetCount("FTSdictionary_Defs");
		Integer counofWord=Integer.parseInt(strcountofword);
		Integer countofdef=Integer.parseInt(strcountofworddefs);
		if(countofdef!=counofWord)
			makeText("Problem:"+(counofWord+" "+countofdef) +" missing") ;
		else
			makeText("No Problem:"+(counofWord-countofdef) +" missing") ;

		ArrayList<Words> wordswithPaging=WQFerhengDB.mWQferhengDBOpenHelper.GetWordswithPaging("");
		//makeText(wordswithPaging.size()+"");
		int count=0;

		for (int i=0; i<wordswithPaging.size(); i++ )
		{
			Words word=(wordswithPaging.get(i));
			String w=word.peyv;
			//makeText(w);
			if(word.peyv!=null&&word.peyv.length()>0) {


				//autoCmopletetextView.setText(word.peyv);
				//GO();

				Words resulted = GetSingleExactWord(word.peyv.replace("-",""));

				if (resulted!=null)
				{

					count++;
					if(count%10000==0)
						makeText(count+"  "+word.NormalizedWord);
				} else if(!word.peyv.contains("-"))
				{
					//makeText(word.peyv +"  "+word.NormalizedWord);
					AddRemoveFromFavList(getBaseContext(),word.peyv, true);

					//makeText(getString(R.string.resultsnotfound, word.peyv +"  "+count));
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
		WQFerhengActivity.linkColor="#4E275A";
		if (strtheme.equalsIgnoreCase("app")|| strtheme.equalsIgnoreCase(""))
		{
			WQFerhengActivity.theme=R.style.MyCustomTheme;

			WQFerhengActivity.linkColor="#552640";
		}
		else if (strtheme.equalsIgnoreCase("Appold"))
		{
			WQFerhengActivity.theme=R.style.AppTheme_Dark;
			WQFerhengActivity.linkColor="#4E275A";
		}
		else if(strtheme.equalsIgnoreCase("Dark"))
		{
			WQFerhengActivity.theme=(R.style.AppTheme_Dark);
			WQFerhengActivity.linkColor="#4CAF50";
		}
		else if(strtheme.equalsIgnoreCase("Light"))
		{
			WQFerhengActivity.theme=R.style.AppTheme_Light;
			WQFerhengActivity.linkColor="#2478B7";
		}
		else if(strtheme.equalsIgnoreCase("Classic"))
		{
			WQFerhengActivity.theme=R.style.AppTheme_Classic;
			WQFerhengActivity.linkColor="#2478B7";
		}
		else if(strtheme.equalsIgnoreCase("System"))
		{
			switch ( mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
				case Configuration.UI_MODE_NIGHT_YES: {
					WQFerhengActivity.theme=(R.style.AppTheme_Dark);
					WQFerhengActivity.linkColor="#A36A00";
					break;
				}
				case Configuration.UI_MODE_NIGHT_NO: {
					WQFerhengActivity.theme=R.style.AppTheme_Light;
					WQFerhengActivity.linkColor="#2478B7";
					break;
				}
			}
		}
		else
		{
			WQFerhengActivity.theme=R.style.MyCustomTheme;
		}

	}
	private Boolean toggleTheme(String sender)
	{
		WQFerhengActivity.strtheme =WQFerhengConfig. loadUrlPref(this.getBaseContext(),
				"Theme");


		SelectTheme();
		setTheme(WQFerhengActivity.theme);
		if(sender.equalsIgnoreCase("Config"))
			recreate() ;// Recreate the activity to apply the new strtheme
		else
		{

					}
		//isDarkTheme = !isDarkTheme // Toggle the strtheme flag
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
		//nativeAd = new NativeAd(this, "YOUR_PLACEMENT_ID");
		linearLayoutcustomkeys = (LinearLayout) findViewById(R.id.linearLayoutcustomkeys);
		
		AddFooterView();
		try {
			showAdmob();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	private void showAdmob() throws ClassNotFoundException
	{
		adView = new com.facebook.ads.AdView(this, "1107788330735832_1107834944064504", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
		//LinearLayout adViewLayout = (LinearLayout) findViewById(R.id.adView);
		//final RelativeLayout adContainer =(RelativeLayout) this.findViewById(R.id.relativeLayout1);
		// Find the container where you want to place the ad
		LinearLayout adContainer = findViewById(R.id.banner_container);

		// Add the AdView to the container
		adContainer.addView(adView);
		com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
			@Override
			public void onError(Ad ad, AdError adError) {
				// Ad error callback
				Log.e(TAG,
								"AdviewError: " + adError.getErrorMessage());

			}

			@Override
			public void onAdLoaded(Ad ad) {
				// Ad loaded callback
				// This indicates that the ad has been loaded and is ready to be displayed
				Toast.makeText(WQFerhengActivity.this, "Ad successfully loaded", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onAdClicked(Ad ad) {
				// Ad clicked callback
			}

			@Override
			public void onLoggingImpression(Ad ad) {
				// Ad impression logged callback
			}

		};

		// Request an ad
		adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());



	}
//private void showAdmob() throws ClassNotFoundException
//{
//	if (android.os.Build.VERSION.SDK_INT >= 14) {
//		AdSettings.setTestMode(BuildConfig.DEBUG);
//		AdSettings.addTestDevice("793935032354183");
//		AudienceNetworkAds.initialize(this);
//		if(interstitialAd==null)
//			interstitialAd = new InterstitialAd(this, "793935032354183") {
//			};
//		 //MobileAds.initialize(this, "ca-app-pub-4819188859318435/5036961654");
//			    //mAdView =(AdView) this.findViewById(R.id.adView);
//		// Initialize the Audience Network SDK
//
//		// Create listeners for the Interstitial Ad
//		InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
//			@Override
//			public void onInterstitialDisplayed(Ad ad) {
//				// Interstitial ad displayed callback
//				Log.e(TAG, "Interstitial ad displayed.");
//			}
//
//			@Override
//			public void onInterstitialDismissed(Ad ad) {
//				// Interstitial dismissed callback
//				Log.e(TAG, "Interstitial ad dismissed.");
//			}
//
//			@Override
//			public void onError(Ad ad, AdError adError) {
//				// Ad error callback
//				Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//			}
//
//			@Override
//			public void onAdLoaded(Ad ad) {
//				// Interstitial ad is loaded and ready to be displayed
//				Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//				// Show the ad
//				interstitialAd.show();
//			}
//
//			@Override
//			public void onAdClicked(Ad ad) {
//				// Ad clicked callback
//				Log.d(TAG, "Interstitial ad clicked!");
//			}
//
//			@Override
//			public void onLoggingImpression(Ad ad) {
//				// Ad impression logged callback
//				Log.d(TAG, "Interstitial ad impression logged!");
//			}
//		};
//		Log.d(TAG, "interstitialAd:!"+interstitialAd);
//		interstitialAd.loadAd(
//				interstitialAd.buildLoadAdConfig()
//						.withAdListener(interstitialAdListener)
//						.build());
//
//		}
//		else
//		{
//
//		}
//
//}

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

			languageToLoad = prefs.getString("Lang", "ku");
			//makeText(languageToLoad);
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
			public void run() {
				int count = WQFerhengDBOpenHelper.WordList;
				progressbar2.setVisibility(View.VISIBLE);
				totalccount = WQFerhengDBOpenHelper.totalFileCount;
				int value = ((WQFerhengDBOpenHelper.LoadedRawWordFileCount * 100) / totalccount);

				if (!WQFerhengDBOpenHelper.Loading) 
				{

					if (task != null)
						task.cancel();
					CancelRequest = true;
					progressbar2.setVisibility(View.GONE);

					disablemenu = false;
					EnableLayoutChildrens(mainLayout, true);
					if (actionBarIsEnabled)
						invalidateOptionsMenu();

					if (!dialogshowed) 
					{
						upgrating = false;
						showDialog(count
								+ " peyv lê hat(in) bar kirin. \nBo çalakbûna hin taybetiyan, gerek Ferheng ji nû ve"
								+ " bê dan dest pê kirin. Niha ji nû ve bide dest pê kirin?.");	}
					 addNotification("WQFerheng bar bû", count+ " peyv ji bo ferhengê hatin çêkirin") ;
				}

				progressbar2.setProgress(value);
				progressbar2.setText("Peyv têne bar kirin...: %" + value + ", Heta niha: "
						+ count +" sernav");

			}
		});
	}



	private void ViewWordList() {
		
		Intent intent = new Intent(this, ZimanListActivity.class);
		startActivity(intent);
	}
	private void GoToSettings() {

		Intent intent = new Intent(this, WQFerhengConfig.class);
		startActivityForResult(intent, REQUEST_CODE_CONFIG);
	}

//
//	protected void showLanguageChangeDialog() {
//		try {
//			items[0] = "ku-" + getString(R.string.Langku);
//			items[1] = "en-" + getString(R.string.Langen);
//			items[2] = "de-" + getString(R.string.Langde);
//			items[3] = "tr-" + getString(R.string.Langtr);
//			items[4] = "fa-" + getString(R.string.Langfa);
//			final SharedPreferences prefsLang = getBaseContext()
//					.getSharedPreferences("Lang", 0);
//			final String currentlang = prefsLang.getString("Lang", "ku") + "-";
//			// makeText(currentlang);
//			int selectedItem = 0;
//			for (int xx = 0; xx < items.length; xx++) {
//				if (items[xx].toString().contains(currentlang)) {
//					selectedItem = xx;
//					break;
//				}
//
//			}
//
//			new AlertDialog.Builder(this)
//					.setSingleChoiceItems(items, selectedItem, null)
//					.setIcon(R.drawable.refresh)
//					.setTitle(R.string.ziman)
//					.setPositiveButton(R.string.temam,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									dialog.dismiss();
//									int selectedPosition = ((AlertDialog) dialog)
//											.getListView()
//											.getCheckedItemPosition();
//
//									String itemselected = items[selectedPosition]
//											.toString();
//									if (!itemselected.startsWith(currentlang)) {
//
//										SharedPreferences.Editor editor = prefsLang
//												.edit();
//										if (itemselected.contains("ku-")) {
//											editor.putString("Lang", "ku");
//											languageToLoad = "ku";
//
//										} else if (itemselected.contains("en-")) {
//											editor.putString("Lang", "en");
//											languageToLoad = "en";
//										} else if (itemselected.contains("de-")) {
//											editor.putString("Lang", "de");
//											languageToLoad = "de";
//										} else if (itemselected.contains("tr-")) {
//											languageToLoad = "tr";
//											editor.putString("Lang", "tr");
//										}
//										 else if (itemselected.contains("fa-")) {
//												languageToLoad = "fa";
//												editor.putString("Lang", "fa");
//											}
//										editor.commit();
//										SetLanguage();
//										restartActivity();
//										if (itemselected.contains("-"))
//											itemselected = itemselected
//													.substring(itemselected
//															.indexOf("-"));
//										makeText(getText(R.string.langchange)
//												+ " " + itemselected);
//									}
//								}
//							})
//					.setNegativeButton(R.string.betal,
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//
//								}
//							}).show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//		}
//
//	}

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

		locJa = null;
		config = null;
	}

	private void SetCursorAdapter() {
		String query = "a";
		 final MatrixCursor cursor = null;
	
		@SuppressWarnings("deprecation")
		final
		SimpleCursorAdapter words = new SimpleCursorAdapter(this,
				R.layout.result, cursor,  new String[] { WQFerhengDB.KEY_WORD,
						WQFerhengDB.KEY_DEFINITION }, to)
							{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolderWords holder;			            
            if (v == null) 
            {
                holder = new ViewHolderWords();
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.result, null);
                holder.textViewWord = (TextView) v.findViewById(R.id.word);
                holder.textViewdef = (TextView) v.findViewById(R.id.definition);
                //other stuff
                v.setTag(holder);
            } else {
                holder = (ViewHolderWords) v.getTag();
            }
				Cursor cursorc = (Cursor) getItem(position);

				String word = WQFerhengQueryProvider.GetValue(cursorc, WQFerhengDB.KEY_WORD);
				String definition = WQFerhengQueryProvider.GetValue(cursorc,
						WQFerhengDB.KEY_DEFINITION);
		
				String word_n = WQFerhengQueryProvider.GetValue(cursorc,
						WQFerhengDB.KEY_WORD_N);
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
//				String column=WQFerhengDB.KEY_WORD_N;
//				String spaceds=s;

					String normalized=WQFerhengDBOpenHelper. Normalize(s);
					//if(normalized.contains(" "))
						normalized= normalized+"*";
					Cursor cursor2 =provider.  GetCursor(WQFerhengDB.KEY_WORD_N + " match ? ", normalized );
					if(cursor2!=null)
					{				
					FilteredMatrixCursor bb2 = new FilteredMatrixCursor(
							cursor2, "", WQFerhengDB.KEY_WORD_N );			//No filter						
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
			
					String d= WQFerhengQueryProvider.GetValue(c,  WQFerhengDB.KEY_WORD);
					if(d==null||d.equalsIgnoreCase(""))
						d= WQFerhengQueryProvider.GetValue(c,  WQFerhengDB.KEY_WORD_N);
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
			String	id = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_ID);
			SelectedWord = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD);
			String selectedWord_n= WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD_N);
			if(SelectedWord==null||SelectedWord.equalsIgnoreCase(""))
			{
				SelectedWord =selectedWord_n;
			 //makeText(SelectedWord+ " : sss");
			}
			//Date currentDate1 = new Date();

		Words w =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
		if(w!=null)
		{
			worddef=Decode(w.getwate(), SelectedWord, selectedWord_n);
			worddef=worddef.replace(",", ", ");
		//	Date currentDate2 = new Date();
			//long diffInMs = currentDate2.getTime() -currentDate1.getTime();
			
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
							Search(WQFerhengDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
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
							
							Search(WQFerhengDB.KEY_WORD_N + " match ? ",autoCmopletetextView.getText().toString());
							
							// listviewresult.onRestoreInstanceState(item.State);
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
	            .setSmallIcon(R.drawable.wqferheng)  
	            .setContentTitle(title)  
	            .setContentText(message);  

	    Intent notificationIntent = new Intent(this, WQFerhengActivity.class);
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


	    // Add as notification  
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

			String s = "Lêgerrîneka mîna '"+autoCmopletetextView.getText()+"' kurt pirrcaran pirr encaman dertîne. Ew li gor jêhatîbûna cîhazê we dibe"
					+ " ku demeka pirr bigire û bibe sebeba westîn û rawestîna cîhazê we. Ma hûn dîsa jî dixwazine ku lêgerrînê bidomînin?";

			textViewmessage.setText(s);

			textViewmessage.setPadding(10, 10, 10, 10);

			title.setText("Hay Jê Hebin!");
			builder.setCustomTitle(title);

			builder.setView(textViewmessage);
			builder.setNegativeButton("Na",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}

					});
			builder.setPositiveButton("Belê!",
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
		Cursor resulted = Search(WQFerhengDB.KEY_WORD + " match ? ",word);
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
		if (ItemCount != WQFerhengDBOpenHelper.WordList) {
			}
		String spaced=query;
		//if(spaced.contains("" ))
			spaced=spaced+"*";
		Cursor cursor =provider.  GetCursor(selection, spaced);
		if (cursor != null) 
		{
			resulted = PutCursorResult(cursor);				
		}
		else
		{
			String normalized=WQFerhengDBOpenHelper. Normalize(query);
			//if(normalized.contains(" "))
				normalized=normalized+"*";
			 cursor =provider.  GetCursor(WQFerhengDB.KEY_WORD_N + " match ? ",
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
						cursor, "", WQFerhengDB.KEY_WORD); //Add all items by ""
				
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

					SelectedWord = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD);
					String def = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_DEFINITION);					
				
					String result = getString(R.string.resultsfound,
							cursor.getCount());
					makeText(result);
				} else {
					resulted = false;
					makeText(getString(R.string.resultsnotfound, SelectedWord));
				}
				//HideLoadingProgressBar();
			}
		} else {
			resulted = false;
			makeText(getString(R.string.resultsnotfound, SelectedWord));
		}
		
		return resulted;
	}	


	private HashMap<String, String> putData(String name, String peyv) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(WQFerhengDB.KEY_WORD, name);
		item.put(WQFerhengDB.KEY_DEFINITION, peyv);
		return item;
	}

	public static String Decode(String definition, String word, String normalize) 
	{
		String strToreturn=definition;
		if(encoderList==null)
			encoderList=GetDecodeList();
		

		if(word!=null)
		{
			//Log.d("wordddd",word);
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
		LinkedHashMap<String, String> mapofdecoders = new LinkedHashMap<String, String>();
		

		mapofdecoders.put("Kurdî{|Wate{}formnavdêr}#Rewşa navkî ya mê ya yekjimar a nebinavkirî ya ", "@0");
		mapofdecoders.put("Înglîzî{|Wate{}£gotina pêşiyan£}#gotineka pêşiyan a îngîlîzî|Herwiha{}", "^0");
		mapofdecoders.put("kirin||Kurdî{|Wate{}£lêkera hevdudanî,gerguhêz£}#Binihêre{ ", "~0");
		mapofdecoders.put("bûn||Kurdî{|Wate{}£lêkera hevdudanî,negerguhêz£}#Binihêre{", "&0");
		mapofdecoders.put("Înglîzî{}cotepeyv|Wate{}#Cotepeyveka Îngîlîzî|Ji{}", "$0");
		mapofdecoders.put("Almanî{}De|Wate{}#forma komparatîvî ji rengdêr ", "<0");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#£nifir£}nifireka bi Kurd", "!0");
		mapofdecoders.put("|Bi zaravayên kurdî{}Kurdî (Soranî):}Kurdî", "`0");
		mapofdecoders.put(" kirin||Kurdî{|Wate{}£Nav,mê£}#Binihêre{ ", ";0");
		mapofdecoders.put("Kurdî{|Wate{}£rengdêr£}#ya/yê ku hatiye ", "=0");
		mapofdecoders.put("Înglîzî{|Wate{}£biwêj£}#biwêjeka Îngîlîz", "?0");
		mapofdecoders.put(" bûn||Kurdî{|Wate{}£Nav,mê£}#Binihêre{ ", "'0");
		mapofdecoders.put("Înglîzî{|Wate{}£rengdêr£}#pêwendîdarê ", "%0");
		mapofdecoders.put("Kurdî (Zazakî){|Wate{}£lêker£}#hatin ", "{0");
		mapofdecoders.put("Esperanto{|Bilêvkirin{}eo-IPA|Wate{}", "@1");
		mapofdecoders.put("î{|Wate{}£gotina pêşiyan£}#gotineka", "^1");
		mapofdecoders.put("Înglîzî{|Wate{}£Nav£}#navekî jinan ", "&1");
		mapofdecoders.put("Kurdî{|Wate{}£rengdêr£}#Binihêre{ ", "<1");
		mapofdecoders.put("Kurdî{|Wate{}£Nav,mê£}#Binihêre{", ")1");
		mapofdecoders.put("Kurdî{|Wate{}£lêker£}#Binihêre{ ", "`1");
		mapofdecoders.put("Înglîzî{}£Nav£}£rengdêr£|Wate{}#", "=1");
		mapofdecoders.put("Kurdî (Soranî)Soranî{|Wate{}#bn ", "?1");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#Binihêre{ ", "%1");
		mapofdecoders.put("Înglîzî{}£rengdêr£}£Nav£|Wate{}#", "@2");
		mapofdecoders.put("Kurdî{|Wate{}£gotina pêşiyan£}#", "~2");
		mapofdecoders.put("|Bi alfabeyên din{}Kurdî-Erebî:", "&2");
		mapofdecoders.put("|Bi alfabeyên din{}kurdî-erebî:", "<2");
		mapofdecoders.put("Înglîzî{|Wate{}£Nav£}#texlîtek ", ">2");
		mapofdecoders.put("|Bi zaravayên kurdî{}Kurmancî:", "`2");
		mapofdecoders.put("Kurdî{}£Nav£}£rengdêr£|Wate{}#", "=2");
		mapofdecoders.put("Kurdî{}£rengdêr£}£Nav£|Wate{}#", "'2");
		mapofdecoders.put("Kurdî{|Wate{}£Nav£}#Binihêre{ ", "%2");
		mapofdecoders.put("Kurdî{|Wate{}£Nav,mê£}#rewşa ", "@3");
		mapofdecoders.put("Kurdî (Soranî)Soranî{|Wate{}#", "^3");
		mapofdecoders.put("Almanî{}£Partîsîp II£|Wate{}#", "~3");
		mapofdecoders.put("Kurdî (Soranî){|Bilêvkirin{}", "&3");
		mapofdecoders.put("Kurdî{|Wate{}£lêker£}#hatin ", "<3");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#hatin ", ">3");
		mapofdecoders.put("Kurdî{|Wate{}£Nav,mê£}#karê ", "!3");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#kirin ", ")3");
		mapofdecoders.put("Înglîzî{|Wate{}£rengdêr£}#bê", "`3");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#ketin ", "=3");
		mapofdecoders.put("Înglîzî{|Wate{}£hoker£}#bi ", "?3");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#gelek", "'3");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#dema ", "%3");
		mapofdecoders.put("Kurdî{|Wate{}#BERALÎKIRIN ", "@4");
		mapofdecoders.put("Kurdî{|Wate{}£rengdêr£}#bi", "^4");
		mapofdecoders.put("Înglîzî{|Wate{}#Binihêre{ ", "~4");
		mapofdecoders.put("Kurdî{|Wate{}£lêker£}#dan ", "&4");
		mapofdecoders.put("Almaniya Jêrîn{|Wate{}£Nav", "$4");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#bûn ", "<4");
		mapofdecoders.put("Frîzyanî ya Roava{|Wate{}£", ">4");
		mapofdecoders.put("Înglîzî{|Wate{}#pêşgir e k", ")4");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#[bi ", "`4");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#ji ", "=4");
		mapofdecoders.put("Kurdî{|Wate{}£Nav,mê£}#bi", "?4");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#li ", "'4");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#pir", "+4");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#tu ", "%4");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#dan", "@5");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#her", "^5");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#xwe", "~5");
		mapofdecoders.put("}£rengdêr£}£hoker£|Wate{}", "&5");
		mapofdecoders.put("Kurdî (şêxbizinî){|Wate{}", "$5");
		mapofdecoders.put("Tirkî{|Wate{}£biwêj£}#ji ", "<5");
		mapofdecoders.put("Kurdî{}£rengdêr£}£Nav,mê£", ">5");
		mapofdecoders.put("Kurdî{|Wate{}£lêker£}#ji ", ")5");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#bi", "`5");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#di", "=5");
		mapofdecoders.put("||Kurdî (Zazakî){|Wate{}", "?5");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#bi", "'5");
		mapofdecoders.put("||Kurdî (Soranî){|Wate{}", "+5");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#li", "%5");
		mapofdecoders.put("Tirkî{|Wate{}£lêker£}#ji", "@6");
		mapofdecoders.put("Kurdî{|Wate{}#Binihêre{ ", "^6");
		mapofdecoders.put("Tirkî{|Wate{}£hoker£}#bi", "~6");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#ne", "&6");
		mapofdecoders.put("Kurdî{|Wate{}£biwêj£}#ew", "$6");
		mapofdecoders.put("Kurdî (Zazakî){|Wate{}#", "<6");
		mapofdecoders.put("Kurdî{|Wate{}zmn}#ya/ê ", ">6");
		mapofdecoders.put("n||Kurdî{|Wate{}£lêkera", ")6");
		mapofdecoders.put("||Almanî{|Bilêvkirin{}Ⓓ", "`6");
		mapofdecoders.put("||Înglîzî{|Bilêvkirin{}", "=6");
		mapofdecoders.put("Kurdî (Soranî){|Wate{}#", "?6");
		mapofdecoders.put("|Ji{}Ji erebî,têkildarî", "'6");
		mapofdecoders.put("Kurdî (Zazakî){|Wate{}", "+6");
		mapofdecoders.put("Erebî{|واتە{}#جۆره‌كێ ", "%6");
		mapofdecoders.put("||Farisî{|Bilêvkirin{}", "@7");
		mapofdecoders.put("Almanî{|Bilêvkirin{}Ⓓ", "^7");
		mapofdecoders.put("Înglîzî{|Bilêvkirin{}", "~7");
		mapofdecoders.put("}Dîwana Meleyê Cizirî", "&7");
		mapofdecoders.put("}£Partîsîp II£|Wate{}", "$7");
		mapofdecoders.put("Binihêre bo lêkerê{ ", "<7");
		mapofdecoders.put("Farisî{|Bilêvkirin{}", "!7");
		mapofdecoders.put("Frensî{|Bilêvkirin{}", ")7");
		mapofdecoders.put("Kurdî{|Wate{}£lêkera", "`7");
		mapofdecoders.put("Koreyî{|Bilêvkirin{}", "=7");
		mapofdecoders.put("{}£Nav,mê£}£rengdêr£", "?7");
		mapofdecoders.put("|Bikaranîn{}Navdêr:}", "'7");
		mapofdecoders.put("Azerbaycanî{|Wate{}£", "+7");
		mapofdecoders.put("}Nînorskiya Norwecî:", "%7");
		mapofdecoders.put("Kurdî{|Wate{}#rewşa ", "@8");
		mapofdecoders.put("|Bikaranîn{}Lêker:}", "_8");
		mapofdecoders.put("|Ji wêjeya klasîk{}", "^8");
		mapofdecoders.put("}Frîzyanî ya Roava:", "~8");
		mapofdecoders.put("Erebî{|Bilêvkirin{}", "&8");
		mapofdecoders.put("Kurdî{|Wate{}£lêker", "$8");
		mapofdecoders.put("Kurdî{|Wate{}#kesê ", "<8");
		mapofdecoders.put("|Werger{}Danîmarkî:", ">8");
		mapofdecoders.put("}£Nav,nêr£}£Nav,mê£", "!8");
		mapofdecoders.put("Kurdî{|Bilêvkirin{}", ")8");
		mapofdecoders.put("Erebî{|واتە{}#تشتێ ", "`8");
		mapofdecoders.put("Kurdî{|Wate{}#tiştê", ";8");
		mapofdecoders.put("|Werger{}Afrîkanî:", "=8");
		mapofdecoders.put("Kurdî{|Wate{}#wek ", "?8");
		mapofdecoders.put("|Werger{}Fînlandî:", "'8");
		mapofdecoders.put("Înglîzî{|Wate{}#bi", "+8");
		mapofdecoders.put("Erebî{|واتە{}#ئه‌و", "%8");
		mapofdecoders.put("Portûgalî{|Wate{}£", "@9");
		mapofdecoders.put("|Ji{}Ji erebî|Jê{}", "_9");
		mapofdecoders.put("Esperanto{|Wate{}£", "^9");
		mapofdecoders.put("|Wergerr{}Înglîzî:", "~9");
		mapofdecoders.put("Danîmarkî{|Wate{}£", "&9");
		mapofdecoders.put("Polonyayî{|Wate{}£", "$9");
		mapofdecoders.put("Erebî{|واتە{}#جهێ ", "<9");
		mapofdecoders.put("Înglîzî{|Wate{}#ji", ">9");
		mapofdecoders.put("Înglîzî{|Wate{}#li", "!9");
		mapofdecoders.put("Înglîzî{|Wate{}#ya", ")9");
		mapofdecoders.put("Taî{|Wate{}£Nav£}#", "`9");
		mapofdecoders.put("|Ji{}ne-+|Werger{}", ";9");
		mapofdecoders.put("|Werger{}Înglîzî:", "=9");
		mapofdecoders.put("Erebî{|واتە{}#یێ ", "'9");
		mapofdecoders.put("Fînlandî{|Wate{}£", "+9");
		mapofdecoders.put("|Wergerr{}Farisî:", "%9");
		mapofdecoders.put("Proto-hindûewropî", "=a");
		mapofdecoders.put("Spanyolî{|Wate{}£", "%a");
		mapofdecoders.put("||Înglîzî{|Wate{}", "@b");
		mapofdecoders.put("Îtalyayî{|Wate{}£", "^b");
		mapofdecoders.put("|Binêre herwiha{}", "&b");
		mapofdecoders.put("}£rengdêr£|Wate{}", "$b");
		mapofdecoders.put("|Werger{}Bûlgarî:", "=b");
		mapofdecoders.put("Afrîkanî{|Wate{}£", "%b");
		mapofdecoders.put("Wîetnamî{|Wate{}£", "@c");
		mapofdecoders.put("|Binere herwiha{}", "~c");
		mapofdecoders.put("Katalanî{|Wate{}£", "$c");
		mapofdecoders.put("bêwate}Binihêre{ ", ">c");
		mapofdecoders.put("Înglîzî{|Wate{}#", "=c");
		mapofdecoders.put("n||Kurdî{|Wate{}", "%c");
		mapofdecoders.put("|Werger{}Almanî:", "@d");
		mapofdecoders.put("|Werger{}Farisî:", "~d");
		mapofdecoders.put("|Wergerr{}Tirkî:", "$d");
		mapofdecoders.put("}İnglîziya kevn:", ">d");
		mapofdecoders.put("|Wate{}£rengdêr£", "=d");
		mapofdecoders.put("||Erebî{|واتە{}#", "%d");
		mapofdecoders.put("}£Nav,mê£|Wate{}", "@e");
		mapofdecoders.put("Erebî{|واتە{}#خۆ", "=e");
		mapofdecoders.put(" tê bikaranîn}#!", "%e");
		mapofdecoders.put("Hûngarî{|Wate{}£", "@f");
		mapofdecoders.put("Yewnanî{|Wate{}£", "^f");
		mapofdecoders.put("Kurmancî{|Wate{}", "~f");
		mapofdecoders.put("||Farisî{|Wate{}", "$f");
		mapofdecoders.put("î{}hejmar|Wate{}", "=f");
		mapofdecoders.put("}Middle Persian:", "%f");
		mapofdecoders.put("Kurdî{|Wate{}#bi", "@g");
		mapofdecoders.put("Kurdî{|Wate{}#di", "^g");
		mapofdecoders.put("|Werger{}Frensî:", "$g");
		mapofdecoders.put("||Almanî{|Wate{}", ">g");
		mapofdecoders.put("Înglîzî{|Wate{}", "?g");
		mapofdecoders.put("|Werger{}Tirkî:", "%g");
		mapofdecoders.put("Farisî{|Wate{}#", "@h");
		mapofdecoders.put("Almanî{|Wate{}#", "^h");
		mapofdecoders.put("||Tirkî{|Wate{}", "$h");
		mapofdecoders.put("|Werger{}Erebî:", "=h");
		mapofdecoders.put("|Wate{}£Nav,mê£", "%h");
		mapofdecoders.put("Holendî{|Wate{}", "@i");
		mapofdecoders.put("Erebî{|واتە{}#ب", "^i");
		mapofdecoders.put("}Kurdiya başûr:", "=i");
		mapofdecoders.put("Frensî{|Wate{}£", "%i");
		mapofdecoders.put("Japonî{|Wate{}£", "@j");
		mapofdecoders.put("|Navê zanistî{}", "^j");
		mapofdecoders.put("|Ji wêjeyê{}Li ", "~j");
		mapofdecoders.put("Erebî{|واتە{}#ل", "$j");
		mapofdecoders.put("Îbranî{|Wate{}£", "=j");
		mapofdecoders.put("Romanî{|Wate{}£", "%j");
		mapofdecoders.put("|Werger{}Bosnî:", "@k");
		mapofdecoders.put("{}£Nav£}£lêker£", "^k");
		mapofdecoders.put("Erebî{|واتە{}#ژ", "~k");
		mapofdecoders.put("Elbanî{|Wate{}£", "&k");
		mapofdecoders.put("|Werger{}Baskî:", "<k");
		mapofdecoders.put("Belûcî{|Wate{}£", ">k");
		mapofdecoders.put("Telûgû{|Wate{}£", "=k");
		mapofdecoders.put("}Jihevqetandin:", "%k");
		mapofdecoders.put("Ûkraynî{|Wate{}", "@l");
		mapofdecoders.put("Almaniya Jêrîn:", "^l");
		mapofdecoders.put("Erebî{|واتە{}#", "$l");
		mapofdecoders.put("Farisî{|Wate{}", "<l");
		mapofdecoders.put("Kurdî{|Wate{}#", ">l");
		mapofdecoders.put("Tirkî{|Wate{}#", "=l");
		mapofdecoders.put("Almanî{|Wate{}", "%l");
		mapofdecoders.put("}Papyamentoyî:", "@m");
		mapofdecoders.put("}Galiya Skotî:", "~m");
		mapofdecoders.put("|Werger{}Çekî:", "&m");
		mapofdecoders.put("Erebî{|Wate{}#", "$m");
		mapofdecoders.put("|Tewîn{}Lêker:", "<m");
		mapofdecoders.put("|Werger{}Çînî:", "=m");
		mapofdecoders.put("Latînî{|Wate{}", "%m");
		mapofdecoders.put("Swêdî{|Wate{}£", "@n");
		mapofdecoders.put("}Samiya Bakur:", "^n");
		mapofdecoders.put("|Ji wêjeyê{}Di", "=n");
		mapofdecoders.put("Hîndî{|Wate{}£", "%n");
		mapofdecoders.put("Kurdî{|Wate{}", "@o");
		mapofdecoders.put("Tirkî{|Wate{}", "^o");
		mapofdecoders.put("|Bilêvkirin{}", "~o");
		mapofdecoders.put("}Înterlîngûa:", "$o");
		mapofdecoders.put("Çînî{|Wate{}£", "<o");
		mapofdecoders.put("}Îbriya kevn:", ">o");
		mapofdecoders.put("}Înterlîngue:", "`o");
		mapofdecoders.put("Çekî{|Wate{}£", "=o");
		mapofdecoders.put("Pali{|Wate{}£", "%o");
		mapofdecoders.put("}Lûksembûrgî:", "@p");
		mapofdecoders.put("}Azerbaycanî:", "~p");
		mapofdecoders.put("|Ji wêjeyê{}", "$p");
		mapofdecoders.put("|Bikaranîn{}", "%p");
		mapofdecoders.put("Rûsî{|Wate{}", "@q");
		mapofdecoders.put("|Bi soranî{}", "^q");
		mapofdecoders.put("|Bide ber{}", "<q");
		mapofdecoders.put("}Portûgalî:", ">q");
		mapofdecoders.put("}Esperanto:", "=q");
		mapofdecoders.put("}Polonyayî:", "%q");
		mapofdecoders.put("|Têkildar{}", "@r");
		mapofdecoders.put("}Danîmarkî:", "^r");
		mapofdecoders.put("Binihêre{bi", "$r");
		mapofdecoders.put(" Kurdistanê", "=r");
		mapofdecoders.put("Binihêre{ji", "%r");
		mapofdecoders.put("wek termeka", "@s");
		mapofdecoders.put("Binihêre{li", "^s");
		mapofdecoders.put("wek termeke", "$s");
		mapofdecoders.put("}Sanskrîtî:", "=s");
		mapofdecoders.put("Binihêre{di", "%s");
		mapofdecoders.put(" serekaniya", "@t");
		mapofdecoders.put("{}£Nav,nêr£", "%t");
		mapofdecoders.put(" د ناڤبه‌را", "@u");
		mapofdecoders.put("|Bide Ber{}", "~u");
		mapofdecoders.put("}Sîcîlyanî:", "$u");
		mapofdecoders.put(" (rêzimanî)", "<u");
		mapofdecoders.put("}Bambarayî:", ">u");
		mapofdecoders.put("kirin,kirin", "=u");
		mapofdecoders.put("Binihêre{ne", "?u");
		mapofdecoders.put(" دكه‌ڤیته‌ ", "%u");
		mapofdecoders.put("|Herwiha{}", "@v");
		mapofdecoders.put("}Fînlandî:", "^v");
		mapofdecoders.put("}Spanyolî:", "$v");
		mapofdecoders.put("}Îtalyayî:", "<v");
		mapofdecoders.put("}Katalanî:", "=v");
		mapofdecoders.put("|Wergerr{}", "%v");
		mapofdecoders.put("|Hevwate{}", "@w");
		mapofdecoders.put("|Dijwate{}", "<w");
		mapofdecoders.put("bi awayekî", ">w");
		mapofdecoders.put("}Îndonezî:", "=w");
		mapofdecoders.put("}Wîetnamî:", "%w");
		mapofdecoders.put("}Sirananî:", "@x");
		mapofdecoders.put("}Latvîanî:", "&x");
		mapofdecoders.put("}Makedonî:", "$x");
		mapofdecoders.put("}Oksîtanî:", "<x");
		mapofdecoders.put(" (tiştekî)", ">x");
		mapofdecoders.put("Binihêre{}", "=x");
		mapofdecoders.put("}Belarûsî:", "%x");
		mapofdecoders.put(" bikaranîn", "@y");
		mapofdecoders.put(" derdikeve", "^y");
		mapofdecoders.put("|Têkildar{", "$y");
		mapofdecoders.put("}Mandarîn:", "=y");
		mapofdecoders.put(" tiştekê/î", "%y");
		mapofdecoders.put(" Tirkiyeyê", "@z");
		mapofdecoders.put("}Gûjiratî:", "~z");
		mapofdecoders.put("}Lîmbûrgî:", "$z");
		mapofdecoders.put("Înglîzî{|", "=z");
		mapofdecoders.put("|Werger{}", "?z");
		mapofdecoders.put("}Înglîzî:", "%z");
		mapofdecoders.put("Binihêre{", "@A");
		mapofdecoders.put("}Holendî:", "~A");
		mapofdecoders.put("Erebî;1. ", "$A");
		mapofdecoders.put("}Hûngarî:", "<A");
		mapofdecoders.put("}Yewnanî:", ">A");
		mapofdecoders.put("Înglîzî{}", "=A");
		mapofdecoders.put("}Norwecî:", "?A");
		mapofdecoders.put("}Îzlandî:", "%A");
		mapofdecoders.put("}Slowenî:", "@B");
		mapofdecoders.put("}Slowakî:", "^B");
		mapofdecoders.put("|Herwiha{", "~B");
		mapofdecoders.put("}Tagalog:", "&B");
		mapofdecoders.put("}Lîtvanî:", "$B");
		mapofdecoders.put("}Ûkraynî:", "<B");
		mapofdecoders.put("}Swahîlî:", ">B");
		mapofdecoders.put("}Ermenkî:", "=B");
		mapofdecoders.put("Kurdî;1. ", "?B");
		mapofdecoders.put(" gotin}#!", "%B");
		mapofdecoders.put("}Îrlandî:", "@C");
		mapofdecoders.put(" zimanên ", "^C");
		mapofdecoders.put("}Hewramî:", "~C");
		mapofdecoders.put(" rewşeke ", "$C");
		mapofdecoders.put("}Maltayî:", "<C");
		mapofdecoders.put("Afrîkanî:", ">C");
		mapofdecoders.put("derxistin", ")C");
		mapofdecoders.put("}Reşwanî:", "`C");
		mapofdecoders.put("}Friyolî:", ";C");
		mapofdecoders.put(" zimanekî", "=C");
		mapofdecoders.put("|Dijwate{", "?C");
		mapofdecoders.put("}Volapük:", "'C");
		mapofdecoders.put("|Hevwate{", "%C");
		mapofdecoders.put("}Mongolî:", "@D");
		mapofdecoders.put("gelemperî", "^D");
		mapofdecoders.put(" tevgerên", "~D");
		mapofdecoders.put("|Wate 2{}", "$D");
		mapofdecoders.put(",hevreha ", "<D");
		mapofdecoders.put(" nehatiye", ")D");
		mapofdecoders.put("Norwecî{|", "=D");
		mapofdecoders.put("}Bengalî:", "?D");
		mapofdecoders.put("|Wate 1{}", "%D");
		mapofdecoders.put("}Pûnjabî:", "@E");
		mapofdecoders.put("{}£hoker£", "^E");
		mapofdecoders.put(" bûyerekê", "~E");
		mapofdecoders.put("Ermenkî{|", "$E");
		mapofdecoders.put("kirin]}#!", "<E");
		mapofdecoders.put(" kesekî/ê", ">E");
		mapofdecoders.put("}Gûaranî:", "=E");
		mapofdecoders.put(" dikevîte", "?E");
		mapofdecoders.put("}Sonxayî:", "%E");
		mapofdecoders.put("Bûlgarî{|", "@F");
		mapofdecoders.put("kirin,xwe", "^F");
		mapofdecoders.put("|Têkilî{}", "~F");
		mapofdecoders.put("{|واتە{}", "$F");
		mapofdecoders.put("Înglîzî:", "<F");
		mapofdecoders.put("Farisî{|", ">F");
		mapofdecoders.put("}Farisî:", ")F");
		mapofdecoders.put("}Almanî:", "`F");
		mapofdecoders.put("}Frensî:", "=F");
		mapofdecoders.put("}Japonî:", "'F");
		mapofdecoders.put("}Latînî:", "@G");
		mapofdecoders.put("}Romanî:", "^G");
		mapofdecoders.put("}Koreyî:", "~G");
		mapofdecoders.put("}Îbranî:", "&G");
		mapofdecoders.put("}Faroeî:", "$G");
		mapofdecoders.put("kirin}#!", "<G");
		mapofdecoders.put("yekjimar", ")G");
		mapofdecoders.put(" taybetî", "=G");
		mapofdecoders.put("}Elbanî:", "?G");
		mapofdecoders.put("}Estonî:", "%G");
		mapofdecoders.put(" navbera", "@H");
		mapofdecoders.put(" mirovan", "^H");
		mapofdecoders.put("}Breton:", "~H");
		mapofdecoders.put("}Telûgû:", "&H");
		mapofdecoders.put("}Galîkî:", "$H");
		mapofdecoders.put("}Zûlûyî:", "<H");
		mapofdecoders.put("kurmancî", ")H");
		mapofdecoders.put("}Mayayî:", "=H");
		mapofdecoders.put("}Zazakî:", "%H");
		mapofdecoders.put("}Kirîlî:", "@I");
		mapofdecoders.put(" zarokan", "^I");
		mapofdecoders.put("girêdayî", "~I");
		mapofdecoders.put("|Nêzîk{}", "$I");
		mapofdecoders.put("kişandin", "<I");
		mapofdecoders.put("}Ûrdûyî:", ">I");
		mapofdecoders.put("çêkirina", "`I");
		mapofdecoders.put("mirovekî", ";I");
		mapofdecoders.put("kirin,bi", "=I");
		mapofdecoders.put(" zelaman", "?I");
		mapofdecoders.put("berdewam", "%I");
		mapofdecoders.put("derketin", "@J");
		mapofdecoders.put("}Tamîlî:", "^J");
		mapofdecoders.put("tişta/ê ", "~J");
		mapofdecoders.put(" dewletê", "&J");
		mapofdecoders.put("}Yidişî:", "$J");
		mapofdecoders.put("Tacîkî{|", "<J");
		mapofdecoders.put(" dikarin", ">J");
		mapofdecoders.put("}Astûrî:", ")J");
		mapofdecoders.put("}Walonî:", "`J");
		mapofdecoders.put("kirin,ji", "=J");
		mapofdecoders.put(" derdora", "?J");
		mapofdecoders.put("girtina ", "%J");
		mapofdecoders.put("}Tatarî:", "^K");
		mapofdecoders.put(" tesîra ", "~K");
		mapofdecoders.put("kirin,li", "&K");
		mapofdecoders.put("destpêka", "$K");
		mapofdecoders.put("alfabeya", "<K");
		mapofdecoders.put(" deryayê", ">K");
		mapofdecoders.put("}Qereçî:", "=K");
		mapofdecoders.put("avestayî", "?K");
		mapofdecoders.put("}Maratî:", "%K");
		mapofdecoders.put("|Wate{}", "@L");
		mapofdecoders.put("Kurdî{|", "^L");
		mapofdecoders.put("}Tirkî:", "&L");
		mapofdecoders.put("Almanî{", "$L");
		mapofdecoders.put("Almanî:", ">L");
		mapofdecoders.put("}Erebî:", ")L");
		mapofdecoders.put(" hatiye", "?L");
		mapofdecoders.put("}Swêdî:", "'L");
		mapofdecoders.put("tiştekî", "%L");
		mapofdecoders.put("Kurdî{}", "@M");
		mapofdecoders.put(" kesekî", "^M");
		mapofdecoders.put("}Îdoyî:", "~M");
		mapofdecoders.put("}Krotî:", "$M");
		mapofdecoders.put("}Malay:", "<M");
		mapofdecoders.put(" dikare", "%M");
		mapofdecoders.put("گه‌له‌ك", "@N");
		mapofdecoders.put(" dihête", "^N");
		mapofdecoders.put(" دهێته‌", "~N");
		mapofdecoders.put(" dibîte", "&N");
		mapofdecoders.put(" دبیته‌", "$N");
		mapofdecoders.put("}Sirbî:", "<N");
		mapofdecoders.put("}Welşî:", "=N");
		mapofdecoders.put(" (tiştê", "?N");
		mapofdecoders.put("}Hîndî:", "'N");
		mapofdecoders.put(" tiştan", "%N");
		mapofdecoders.put(" civatî", "@O");
		mapofdecoders.put("inglîzî", "^O");
		mapofdecoders.put("dinyayê", "~O");
		mapofdecoders.put("}Gurcî:", "&O");
		mapofdecoders.put("xwarinê", "$O");
		mapofdecoders.put("dibêjin", "<O");
		mapofdecoders.put(" kurdan", ")O");
		mapofdecoders.put("gotinên", "`O");
		mapofdecoders.put(" maneya", "=O");
		mapofdecoders.put("zêdetir", "?O");
		mapofdecoders.put("nekirin", "%O");
		mapofdecoders.put("mirovên", "@P");
		mapofdecoders.put("zarokên", "^P");
		mapofdecoders.put("xwarina", "~P");
		mapofdecoders.put("dixwaze", "&P");
		mapofdecoders.put("daxwaza", "$P");
		mapofdecoders.put("hemberî", "<P");
		mapofdecoders.put("nivîsîn", ">P");
		mapofdecoders.put(" derekê", "?P");
		mapofdecoders.put("}Skotî:", "%P");
		mapofdecoders.put("vekirin", "@Q");
		mapofdecoders.put(" عه‌ردی", "_Q");
		mapofdecoders.put("hevreha", "^Q");
		mapofdecoders.put(" jiyanê", "~Q");
		mapofdecoders.put("pehlewî", "&Q");
		mapofdecoders.put("xwediyê", "$Q");
		mapofdecoders.put("}Kornî:", "<Q");
		mapofdecoders.put("bandora", ">Q");
		mapofdecoders.put("dikevin", ")Q");
		mapofdecoders.put(" dişibe", "`Q");
		mapofdecoders.put(" peyvan", "=Q");
		mapofdecoders.put("carinan", "'Q");
		mapofdecoders.put("leşkerî", "%Q");
		mapofdecoders.put("nehatin", "@R");
		mapofdecoders.put("nikarin", "^R");
		mapofdecoders.put("}Osetî:", "~R");
		mapofdecoders.put("bûn]}#!", "$R");
		mapofdecoders.put("dewleta", "<R");
		mapofdecoders.put(" sedema", ">R");
		mapofdecoders.put("xwestin", ")R");
		mapofdecoders.put("rakirin", "=R");
		mapofdecoders.put("welatên", "?R");
		mapofdecoders.put("}Maorî:", "'R");
		mapofdecoders.put("tevgera", "%R");
		mapofdecoders.put("}Qûçûa:", "@S");
		mapofdecoders.put(" muzîkê", "^S");
		mapofdecoders.put("deverên", "~S");
		mapofdecoders.put("deryayî", "$S");
		mapofdecoders.put("pergala", "<S");
		mapofdecoders.put("zayendî", "`S");
		mapofdecoders.put(" dikin.", "=S");
		mapofdecoders.put("malbata", "?S");
		mapofdecoders.put("Erebî{", "%S");
		mapofdecoders.put("Tirkî:", "@T");
		mapofdecoders.put("Erebî:", "^T");
		mapofdecoders.put("(yekî)", "~T");
		mapofdecoders.put("}Rûsî:", "&T");
		mapofdecoders.put("bûn}#!", "$T");
		mapofdecoders.put("}Çekî:", "<T");
		mapofdecoders.put("}Çînî:", ">T");
		mapofdecoders.put(" kesek", "=T");
		mapofdecoders.put("tiştek", "?T");
		mapofdecoders.put("tiştên", "'T");
		mapofdecoders.put(" (mec)", "%T");
		mapofdecoders.put(" tiştî", "@U");
		mapofdecoders.put(" (kesê", "^U");
		mapofdecoders.put("bilind", "~U");
		mapofdecoders.put("mirovî", "&U");
		mapofdecoders.put("farisî", "$U");
		mapofdecoders.put("derbas", "<U");
		mapofdecoders.put("hatine", ">U");
		mapofdecoders.put("avêtin", ")U");
		mapofdecoders.put("nikare", "`U");
		mapofdecoders.put("wateya", ";U");
		mapofdecoders.put("frensî", "=U");
		mapofdecoders.put("karekî", "?U");
		mapofdecoders.put("ketiye", "%U");
		mapofdecoders.put("dijwar", "@V");
		mapofdecoders.put("latînî", "_V");
		mapofdecoders.put("dibêje", "^V");
		mapofdecoders.put("hatîye", "~V");
		mapofdecoders.put("peyvên", "&V");
		mapofdecoders.put("}Lekî:", "$V");
		mapofdecoders.put("cihekî", "<V");
		mapofdecoders.put("kiriye", ">V");
		mapofdecoders.put("(argo)", ")V");
		mapofdecoders.put(" ئانكو", "`V");
		mapofdecoders.put("هاتیه‌", "=V");
		mapofdecoders.put("kirin)", "?V");
		mapofdecoders.put("gelekî", "'V");
		mapofdecoders.put(" dike.", "+V");
		mapofdecoders.put("nexweş", "%V");
		mapofdecoders.put("destan", "@W");
		mapofdecoders.put(" sofya", "^W");
		mapofdecoders.put(" سۆفیا", "~W");
		mapofdecoders.put(" مرۆڤی", "&W");
		mapofdecoders.put("mirinê", "<W");
		mapofdecoders.put("jiyana", ">W");
		mapofdecoders.put("|Wate{", ")W");
		mapofdecoders.put("dawiyê", "`W");
		mapofdecoders.put("peyvik", "=W");
		mapofdecoders.put("fransî", "?W");
		mapofdecoders.put("kirin.", "%W");
		mapofdecoders.put("berdan", "@Y");
		mapofdecoders.put("girîng", "^Y");
		mapofdecoders.put("baranê", "~Y");
		mapofdecoders.put("dizane", "&Y");
		mapofdecoders.put("nizane", "$Y");
		mapofdecoders.put(" peyvê", "<Y");
		mapofdecoders.put("carekê", ">Y");
		mapofdecoders.put(" bike.", ")Y");
		mapofdecoders.put(" dikim", "=Y");
		mapofdecoders.put("dibêjî", "?Y");
		mapofdecoders.put("dan}#!", "'Y");
		mapofdecoders.put(" wê/wî", "%Y");
		mapofdecoders.put("hindik", "@Z");
		mapofdecoders.put("vermek", "^Z");
		mapofdecoders.put("hinekî", "~Z");
		mapofdecoders.put("kirine", "&Z");
		mapofdecoders.put("vekirî", "$Z");
		mapofdecoders.put("siyasî", "<Z");
		mapofdecoders.put("hevalê", ">Z");
		mapofdecoders.put("taybet", ")Z");
		mapofdecoders.put("nêzîkî", "=Z");
		mapofdecoders.put("zehmet", "?Z");
		mapofdecoders.put("kesa/ê", "'Z");
		mapofdecoders.put("xweşik", "%Z");
		mapofdecoders.put("ketina", "@X");
		mapofdecoders.put("dîtina", "_X");
		mapofdecoders.put(" xelkî", "^X");
		mapofdecoders.put("pêwîst", "~X");
		mapofdecoders.put("derman", "&X");
		mapofdecoders.put("bibêje", "$X");
		mapofdecoders.put("têkilî", "<X");
		mapofdecoders.put(" هه‌تا", ")X");
		mapofdecoders.put("dawiya", ";X");
		mapofdecoders.put("yapmak", "=X");
		mapofdecoders.put("herêma", "?X");
		mapofdecoders.put("hatina", "'X");
		mapofdecoders.put("destên", "%X");
		mapofdecoders.put("birayê", "@ç");
		mapofdecoders.put(" (gava", "^ç");
		mapofdecoders.put(" Iraqê", "&ç");
		mapofdecoders.put("gotin)", "$ç");
		mapofdecoders.put(" (cihê", ">ç");
		mapofdecoders.put("|Ji{}", "=ç");
		mapofdecoders.put("kirin", "?ç");
		mapofdecoders.put("Kurdî", "%ç");
		mapofdecoders.put("|Jê{}", "@Ç");
		mapofdecoders.put("mirov", "_Ç");
		mapofdecoders.put("kurdî", "^Ç");
		mapofdecoders.put("gelek", "~Ç");
		mapofdecoders.put("mezin", "&Ç");
		mapofdecoders.put("kesên", "$Ç");
		mapofdecoders.put("hatin", "<Ç");
		mapofdecoders.put("gotin", "?Ç");
		mapofdecoders.put("destê", "'Ç");
		mapofdecoders.put("rewşa", "%Ç");
		mapofdecoders.put("dirêj", "@ö");
		mapofdecoders.put("ketin", "_ö");
		mapofdecoders.put("tiştê", "^ö");
		mapofdecoders.put("biçûk", "~ö");
		mapofdecoders.put("erebî", "&ö");
		mapofdecoders.put("etmek", "$ö");
		mapofdecoders.put("bûyîn", "<ö");
		mapofdecoders.put("xelkê", ">ö");
		mapofdecoders.put("xwedê", "!ö");
		mapofdecoders.put("Erebî", "`ö");
		mapofdecoders.put("piştî", ";ö");
		mapofdecoders.put("dengê", "=ö");
		mapofdecoders.put("aliyê", "-ö");
		mapofdecoders.put("tirkî", "?ö");
		mapofdecoders.put("çavên", "'ö");
		mapofdecoders.put("awayî", ".ö");
		mapofdecoders.put("peyda", "/ö");
		mapofdecoders.put("وه‌كی", "%ö");
		mapofdecoders.put("}Taî:", "@Ö");
		mapofdecoders.put("giran", "_Ö");
		mapofdecoders.put("pişta", "^Ö");
		mapofdecoders.put("nîşan", "~Ö");
		mapofdecoders.put("xwedî", "&Ö");
		mapofdecoders.put("bawer", "$Ö");
		mapofdecoders.put("diyar", "<Ö");
		mapofdecoders.put("digel", ">Ö");
		mapofdecoders.put("olmak", "!Ö");
		mapofdecoders.put("xirab", ")Ö");
		mapofdecoders.put("kesan", "`Ö");
		mapofdecoders.put("dibin", ";Ö");
		mapofdecoders.put("caran", "=Ö");
		mapofdecoders.put("heman", "-Ö");
		mapofdecoders.put("danîn", "?Ö");
		mapofdecoders.put("belav", "'Ö");
		mapofdecoders.put("Xwedê", ".Ö");
		mapofdecoders.put("diket", "/Ö");
		mapofdecoders.put("çavan", "%Ö");
		mapofdecoders.put("birin", "{Ö");
		mapofdecoders.put("bikin", "}Ö");
		mapofdecoders.put("îranî", "@ğ");
		mapofdecoders.put("rastî", "_ğ");
		mapofdecoders.put("didin", "^ğ");
		mapofdecoders.put("winda", "~ğ");
		mapofdecoders.put("xerab", "&ğ");
		mapofdecoders.put("zirav", "$ğ");
		mapofdecoders.put("zanîn", "<ğ");
		mapofdecoders.put("ده‌مێ", ">ğ");
		mapofdecoders.put("rengê", "!ğ");
		mapofdecoders.put("karên", ")ğ");
		mapofdecoders.put("awayê", "`ğ");
		mapofdecoders.put("herdu", ";ğ");
		mapofdecoders.put("xelas", "=ğ");
		mapofdecoders.put("sivik", "?ğ");
		mapofdecoders.put("giştî", "'ğ");
		mapofdecoders.put("bêhed", "+ğ");
		mapofdecoders.put("yekem", ".ğ");
		mapofdecoders.put("piştê", "/ğ");
		mapofdecoders.put("alîyê", "%ğ");
		mapofdecoders.put("mijûl", "@Ğ");
		mapofdecoders.put("xwînê", "_Ğ");
		mapofdecoders.put("amade", "^Ğ");
		mapofdecoders.put("aramî", "~Ğ");
		mapofdecoders.put("xeber", "&Ğ");
		mapofdecoders.put("stûyê", "$Ğ");
		mapofdecoders.put("qebûl", "<Ğ");
		mapofdecoders.put("pîroz", ">Ğ");
		mapofdecoders.put("zarok", "!Ğ");
		mapofdecoders.put("daran", ")Ğ");
		mapofdecoders.put("salan", "`Ğ");
		mapofdecoders.put("mêran", ";Ğ");
		mapofdecoders.put("ye,di", "=Ğ");
		mapofdecoders.put("piyan", "-Ğ");
		mapofdecoders.put("bibin", "(Ğ");
		mapofdecoders.put("derve", "?Ğ");
		mapofdecoders.put("bûyin", "'Ğ");
		mapofdecoders.put("paqij", "+Ğ");
		mapofdecoders.put("de,di", ".Ğ");
		mapofdecoders.put("hezar", "/Ğ");
		mapofdecoders.put("nebaş", "%Ğ");
		mapofdecoders.put("سه‌رێ", ",Ğ");
		mapofdecoders.put("rojan", "{Ğ");
		mapofdecoders.put("fireh", "}Ğ");
		mapofdecoders.put("dibe.", "@ü");
		mapofdecoders.put("bidin", "_ü");




		
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

			builder.setTitle("Ferheng bar bû");
			builder.setMessage(message);

			builder.setPositiveButton("Belê",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							restartActivity();
						}

					});
			builder.setNegativeButton("Na",
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

	private GroupItemEntity InsertGroupItemEntity(GroupEntity ge, String sep) {
		GroupItemEntity gi = ge.new GroupItemEntity();
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
				if (sep2.trim().equalsIgnoreCase("Binihêre")
						&& ge.Name.equalsIgnoreCase("Binihêre"))
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
			//word = word.replaceAll("\\p{Punct}|\\d", "");
	
			normalized=WQFerhengDBOpenHelper. Normalize(word);
			cursor =provider. GetCursor(WQFerhengDB.KEY_WORD_N + " match ? ", normalized);
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
				String wordd = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD);
				String wordd_n = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD_N);
				if((wordd==null||wordd.equalsIgnoreCase(""))&&word.equalsIgnoreCase(normalized))
					wordd=wordd_n;
				if (wordd.equals(word)) {
					String id= WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_ID);
					Words w =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
					if(w!=null)
					{
				//	String def = GetValue(cursor, WQFerhengDB.KEY_DEFINITION);
					//def=WQFerhengActivity.Decode(def);
					String def=Decode(w.getwate(), wordd, wordd_n);
					def=def.replace(",", ", ");
					listviewresult.setVisibility(View.GONE);
					mExpandableListView.setVisibility(View.VISIBLE);
					
					SetExpanderCollection(word, def);
					// System.out.println(word);
					defaultWordedSplashed = true;
				//	rid=id;
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
							String peyv=WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD);
							String id=WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_ID);
							if(peyv==null||peyv.equals(""))
								peyv=WQFerhengQueryProvider. GetValue(cursor, WQFerhengDB.KEY_WORD_N);
							String  def=WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_DEFINITION);
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
						WQFerhengActivity.listofWords=listofWords;
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
		if (WQFerhengDBOpenHelper.Loading || upgrating) {
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
		edit.putString("wqferheng_" + "SearchHistory", autoCmopletetextView
				.getText().toString());
		edit.commit();
		// TODO Auto-generated method stub
	}

	private String GetSavedSearchHistory() {
		SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
				Context.MODE_PRIVATE);
		String sOld = sharedPref.getString("wqferheng_" + "SearchHistory", "");

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
		inflater.inflate(R.menu.menu_wqferheng, menu);

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

				if (disablemenu) {
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
		final ArrayList<Words> listofFavwords = WQFerhengDB.mWQferhengDBOpenHelper
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
					String word = data.get(WQFerhengDB.KEY_WORD);
					//String definition = data.get(WQFerhengDB.KEY_DEFINITION);

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

							ViewHolderWords holder = (ViewHolderWords) v.getTag();
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
		 res=	WQFerhengDB.mWQferhengDBOpenHelper.InsertFavWord(cont, word);	
		}
		else
		{
			res=WQFerhengDB.mWQferhengDBOpenHelper.DeleteFavWord(word);
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
				+ "http://ku.wiktionary.org/wiki/"+wordw+ " \n\n";
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
	        	
//	        	if(WQFerhengActivity.listofWords!=null)
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
