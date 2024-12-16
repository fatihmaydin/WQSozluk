package com.wqdictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WordTypeListActivity extends TabActivity {
	ListView listwordType;
	ListView listalfabe;

	WQDictionaryQueryProvider queryProvider;
	Boolean actionBarIsEnabled = false;
	Activity myActivity;
	TabHost tabHost;
	List<String> ListCure;
	List<String> letterList;
	String zimantemp = "";
	ImageButton buttonHome;
	ImageButton buttonBack;

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(WQDictionaryActivity.theme);
		setContentView(R.layout.wordtype);
		zimantemp = (String) getText(R.string.zimantemp);
		myActivity = this;
		queryProvider = new WQDictionaryQueryProvider(this);
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		WQDictionaryDB.columntoSearch = WQDictionaryDB.KEY_WORD;
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			zimantemp = extras.getString("ziman");
		}
		final String ziman = zimantemp;
		listwordType = (ListView) findViewById(R.id.listwordType);
		listalfabe = (ListView) findViewById(R.id.listalfabe);
		buttonBack = (ImageButton) findViewById(R.id.buttonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GoBack();
			}
		});
		String d = ziman;
//		if (ziman.startsWith("Î"))
//			d = ziman.replace("Î", "î").trim();

		Words wordmapcure=queryProvider
				.GetSingleExactWord(getText(R.string.listwordtypes) + d);
		if (wordmapcure==null)
			wordmapcure = queryProvider.GetSingleExactWord((String) getText(R.string.cureyepeyvan));
		if (wordmapcure!=null) 
		{
			String id=wordmapcure.getid();
			Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
			if(w!=null&&w.getwate()!=null)
			{

				String def=WQDictionaryActivity.Decode(w.getwate(),wordmapcure.peyv,wordmapcure.peyv);

				String[] cureArray = def.split(java.util.regex.Pattern.quote("|"));
				ListCure = new ArrayList<String>();

				String hedaer = getString(R.string.header);
				for (int x = 0; x < cureArray.length; x++) {
					String item = cureArray[x].toLowerCase();
					if (item.contains(":"))
						item += " " + hedaer;
					ListCure.add(item);
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						R.layout.zimanrow, ListCure);

				listwordType.setAdapter(adapter);
			}
		}
		listwordType.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// LinearLayout row = (LinearLayout)((LinearLayout)view);
				TextView column = (TextView) ((TextView) view);
				String word = column.getText().toString();

				String[] wordpieces = word.split(java.util.regex.Pattern
						.quote(":"));
				if (wordpieces != null && wordpieces.length > 0)
					word = wordpieces[0].trim();

				Intent intent = new Intent(myActivity,
						ListViewCursorLoaderActivity.class);
		
				Bundle b = new Bundle();

				b.putString("type", word);

				b.putString("ziman", ziman);

				intent.putExtras(b);

				long l = 0;
				if (wordpieces.length > 1) {
					Log.d(wordpieces[1], l+"");
					Matcher m=pattern.matcher(wordpieces[1]);
					String str="";
					 while (m.find()) {
						   str+=m.group();
						 }					
					l = Long.parseLong(str);
					
				}
				if (l > 5000) 
				{
					showBigDataWarningDialog(l, intent);
				} 
				else
					myActivity.startActivity(intent);
			}
		});


		Words wordAlfabe=queryProvider
				.GetSingleExactWord("_lllistAlfabe" + d);
	

		if (wordAlfabe==null
			//	listmapalfabe.size() <= 0
				)
			wordAlfabe = queryProvider.GetSingleExactWord((String) getText(R.string.hemuherf));

		if (wordAlfabe!=null) 
		{
			String id=wordAlfabe.getid();
			Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
			if(w!=null)
			{
				String defalfabe =WQDictionaryActivity.  Decode(w.getwate(), wordAlfabe.peyv,wordAlfabe.peyv
						); 
			String[] alfabet = defalfabe.split(java.util.regex.Pattern
					.quote("|"));
			letterList = new ArrayList<String>();
			String hedaer = getString(R.string.header);
			for (int x = 0; x < alfabet.length; x++) {
				String item = alfabet[x].toLowerCase();

				if (item.contains(":")) {
					String[] items = item.split(java.util.regex.Pattern
							.quote(":"));
					if (items.length > 0) {
						if (items[0].replace("-", "").trim()
								.equalsIgnoreCase(""))
							continue;
					}
					item += " " + hedaer;
				}
				letterList.add(item);
			}
			ArrayAdapter<String> adapterAlfabe = new ArrayAdapter<String>(this,
					R.layout.zimanrow, letterList);
			// Set list adapter for the ListView
			listalfabe.setAdapter(adapterAlfabe);
			}
		}

		listalfabe.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// LinearLayout row = (LinearLayout)((LinearLayout)view);
				TextView column = (TextView) ((TextView) view);
				String word = column.getText().toString();

				String[] wordpieces = word.split(java.util.regex.Pattern
						.quote(":"));
				if (wordpieces != null && wordpieces.length > 0)
					word = wordpieces[0].trim();

				Intent intent = new Intent(myActivity,
						ListViewCursorLoaderActivity.class);
				// String word=listItems.get(pos);
				Bundle b = new Bundle();

				b.putString("letter", word);

				b.putString("ziman", ziman);

				intent.putExtras(b);

				long l = 0;
				if (wordpieces.length > 1) {
					Log.d(wordpieces[1], l+"");
					Matcher m=pattern.matcher(wordpieces[1]);
					String str="";
					 while (m.find()) {
						   str+=m.group();
						 }					
					l = Long.parseLong(str);
					
				}
				if (l > 5000) 
				{
					showBigDataWarningDialog(l, intent);
				} 
				else
					myActivity.startActivity(intent);
			}
		});

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			//androidx.appcompat.app.ActionBar actionBar=WQDictionaryActivity.mContext.getSupportActionBar();

//			ActionBar actionBar = getActionBar();
//			actionBar.show();
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			if (ziman != null)
//				setTitle(ziman);
//			actionBarIsEnabled = true;
//			//ActionBar actionBar=getActionBar();
//			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#11315E")));
		}
		spec = tabHost.newTabSpec("Tab 1 Tag").setIndicator(getText(R.string.strsort))
				.setContent(R.id.listwordType);
		tabHost.setFadingEdgeLength(3);
		tabHost.setLeftTopRightBottom(3,3,3,3);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("Tab 2 Tag").setIndicator("ABC")
				.setContent(R.id.listalfabe);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
		tabHost.getTabWidget().setDividerPadding(20);
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				updateTabs();
            }
		});
		tabHost.getTabWidget()
				.getChildAt(0)
				.setBackgroundColor(Color.parseColor(WQDictionaryActivity.linkColor));

	}
	protected void updateTabs() {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

			if (tabHost.getTabWidget().getChildAt(i).isSelected()) {

				tabHost.getTabWidget()
						.getChildAt(i)
						.setBackgroundColor(Color.parseColor(WQDictionaryActivity.linkColor));
			} else {

				tabHost.getTabWidget()
						.getChildAt(i)
						.setBackgroundResource(
								R.drawable.unselected_tab);

			}
		}

	}

	private void GoBack() {
		finsihActivity();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_wordtypelist, menu);
		if (actionBarIsEnabled) {
			try {

				MenuItem itemaaction_order = menu
						.findItem(R.id.action_order_wordlist);

				itemaaction_order
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

			} catch (Exception exc) {

			}

		} else {

			MenuItem itemaaction_order = menu
					.findItem(R.id.action_order_wordlist);

			itemaaction_order.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//switch (item.getItemId()) {
			if(item.getItemId()==android.R.id.home) {
				//case android.R.id.home:
				finsihActivity();
				return true;
			}
		if(item.getItemId()==R.id.action_order_wordlist) {
			//case R.id.action_order_wordlist:
			Order();
			return true;
		}
		else
			return true;

	}

	int pressedToOrderLetter = 0;
	int pressedToOrderCure = 0;

	private void Order() {
		if (tabHost.getCurrentTab() == 0) {
			Collections.sort(ListCure);
			if (pressedToOrderCure % 2 != 0) {
				List<String> newlist = new ArrayList<String>();
				for (int a = ListCure.size() - 1; a > 0; a--) {
					newlist.add(ListCure.get(a));
				}
				ListCure = newlist;
			}
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_list_item_1, ListCure);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					myActivity, R.layout.zimanrow,
					ListCure);

			listwordType.setAdapter(adapter);

			listwordType.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// LinearLayout row = (LinearLayout)((LinearLayout)view);
					TextView column = (TextView) ((TextView) view);
					String word = column.getText().toString();

					String[] wordpieces = word.split(java.util.regex.Pattern
							.quote(":"));
					if (wordpieces != null && wordpieces.length > 0)
						word = wordpieces[0].trim();

					Intent intent = new Intent(myActivity,
							ListViewCursorLoaderActivity.class);
					// String word=listItems.get(pos);
					Bundle b = new Bundle();

					b.putString("type", word);

					b.putString("ziman", zimantemp);

					intent.putExtras(b);

					long l = 0;
					if (wordpieces.length > 1) {
						Log.d(wordpieces[1], l+"");
						Matcher m=pattern.matcher(wordpieces[1]);
						String str="";
						 while (m.find()) {
							   str+=m.group();
							 }					
						l = Long.parseLong(str);
						
					}
					if (l > 5000) 
					{
						showBigDataWarningDialog(l, intent);
					} 
					else
						myActivity.startActivity(intent);
				}
			});
			pressedToOrderCure++;
		} else {
			Collections.sort(letterList);

			if (pressedToOrderLetter % 2 != 0) {
				List<String> newlist = new ArrayList<String>();
				for (int a = letterList.size() - 1; a > 0; a--) {
					newlist.add(letterList.get(a));
				}
				letterList = newlist;
			}
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_list_item_1, letterList);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					myActivity, R.layout.zimanrow,
					letterList);

			listalfabe.setAdapter(adapter);

			listalfabe.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// LinearLayout row = (LinearLayout)((LinearLayout)view);
					TextView column = (TextView) ((TextView) view);
					String word = column.getText().toString();

					String[] wordpieces = word.split(java.util.regex.Pattern
							.quote(":"));
					if (wordpieces != null && wordpieces.length > 0)
						word = wordpieces[0].trim();

					Intent intent = new Intent(myActivity,
							ListViewCursorLoaderActivity.class);
					// String word=listItems.get(pos);
					Bundle b = new Bundle();

					b.putString("letter", word);

					b.putString("ziman", zimantemp);

					intent.putExtras(b);
					long l = 0;
					if (wordpieces.length > 1) {
						Log.d(wordpieces[1], l+"");
						Matcher m=pattern.matcher(wordpieces[1]);
						String str="";
						 while (m.find()) {
							   str+=m.group();
							 }					
						l = Long.parseLong(str);
						
					}
					if (l > 5000) 
					{
						showBigDataWarningDialog(l, intent);
					} 
					else
						myActivity.startActivity(intent);

				}
			});
			pressedToOrderLetter++;
		}

	}

	private void finsihActivity() {
		this.finish();
	}

	Pattern pattern = Pattern.compile("\\d+");

	private void showBigDataWarningDialog(long hejmar, final Intent intent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this
		);
		try {

			TextView title = new TextView(this);
			//title.setBackgroundColor(Color.parseColor("#264055"));
			//title.setTextColor(Color.parseColor("#e6e6e6"));
			final TextView textViewmessage = new TextView(this);
			//textViewmessage.setBackgroundColor(Color.parseColor("#4D76A1"));
			//textViewmessage.setTextColor(Color.parseColor("#e6e6e6"));
			textViewmessage.setTextSize(17);
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextSize(20);

//			String s = "Hûn dixwazine ku bi careke re "
//					+ hejmar
//					+ " sernavan bar bikin. Ew li gor jêhatîbûna cîhazê we dibe"
//					+ " ku demeka pirr bigire û bibe sebeba westîn û rawestîna cîhazê we. Ma hûn dîsa jî dixwazine ku van peyvan bar bikin?";
			String s=String.format((String) getText(R.string. herekategoriyemesaj), hejmar+"" );
			textViewmessage.setText(s);

			textViewmessage.setPadding(10, 10, 10, 10);

			title.setText((String) getText(R.string. hayjehebin));
			builder.setCustomTitle(title);

			builder.setView(textViewmessage);
			builder.setNegativeButton((String) getText(R.string. dialogNa),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}

					});
			builder.setPositiveButton((String) getText(R.string. dialogEre),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							try {
								myActivity.startActivity(intent);

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

}
