package com.wqferheng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.wqferheng.SearchResultAdapter.ViewHolderWords;
import com.wqferheng.WQFerhengDB.WQFerhengDBOpenHelper;

public class ListViewCursorLoaderActivity extends AppCompatActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public static SimpleCursorAdapter mAdapter;

	Cursor cursor;
	ListView mListView;
	String[] columnsDB = new String[] { WQFerhengDB.KEY_WORD,
			WQFerhengDB.KEY_DEFINITION };
	static Boolean dialogshowed = false;
	int[] to = new int[] { R.id.word, R.id.definition };
	String type = "hemû";

	String ziman = "Kurdî";
	String letter = "";

	Boolean actionBarIsEnabled = false;
	Activity myActivity;
	ProgressDialog pd;
	Date d;
	HashMap<String, FilteredMatrixCursor> searchedItems = new HashMap<String, FilteredMatrixCursor>();

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(WQFerhengActivity.theme);
		setContentView(R.layout.wordlist);
		Log.d("loader", "oncreate");
		pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.processingwords));
		pd.show();

		mListView = (ListView) findViewById(R.id.list_resultwordlist);
		myActivity = this;

		mAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.result,
				null, columnsDB, to, 0) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				ViewHolderWords holder;
				if (v == null) {
					holder = new ViewHolderWords();
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.result, null);
					holder.textViewWord = (TextView) v.findViewById(R.id.word);
					holder.textViewdef = (TextView) v
							.findViewById(R.id.definition);
					v.setTag(holder);
				} else {
					holder = (ViewHolderWords) v.getTag();
				}
				Cursor cursorc = (Cursor) getItem(position);

				String word = WQFerhengQueryProvider.GetValue(cursorc,	WQFerhengDB.KEY_WORD);
				String definition = WQFerhengQueryProvider.GetValue(cursorc,WQFerhengDB.KEY_DEFINITION);
				if (word == null || word.equalsIgnoreCase(""))
				{
					word = WQFerhengQueryProvider.GetValue(cursorc,	WQFerhengDB.KEY_WORD_N);
				}

				if (!(definition.toLowerCase().contains("bideng;") || definition
						.toLowerCase().contains(".oga"))) {
					holder.textViewWord
							.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				} else {
					Drawable img = getDrawable(
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

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				LinearLayout row = (LinearLayout) ((LinearLayout) view);
				TextView column = (TextView) row.getChildAt(0);
				String word = column.getText().toString();

				Intent intent = new Intent(myActivity, DefinitionActivity.class);

				Bundle b = new Bundle();

				b.putString("word", word);
				b.putInt("position", position);
				intent.putExtras(b);

				myActivity.startActivity(intent);
			}
		});

		Bundle extras = getIntent().getExtras();

		type = "hemû";
		if (extras != null) {
			WQFerhengActivity.zimanquery = extras.getString("ziman");
			if (WQFerhengActivity.zimanquery != null)
				ziman = "'*" + WQFerhengActivity.zimanquery + "*'";
			WQFerhengActivity.typeToSearch = extras.getString("type");
			if (WQFerhengActivity.typeToSearch != null)
				type = "*" + WQFerhengActivity.typeToSearch + "*";

			letter = extras.getString("letter");
			// Log.d("letter", letter);
			WQFerhengActivity.letterx = letter;
		}
		if (android.os.Build.VERSION.SDK_INT >= 11) {

			androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
			actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO);
			// Set the icon
			actionBar.setIcon(R.drawable.wqferheng); // Your icon resource
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);

//			ActionBar actionBar = getActionBar();
//			actionBar.show();
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			setTitle(WQFerhengActivity.zimanquery);
//			actionBarIsEnabled = true;
//			//ActionBar actionBar=getActionBar();
//			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#11315E")));

		}

		final EditText searchET = (EditText) findViewById(R.id.edittext_filter);
		searchET.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
										  int i2, int i3) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
									  int i3) {

				String mCurFilter = TextUtils.isEmpty(charSequence) ? null
						: charSequence.toString();
				if (cursor != null) {
					if ((mCurFilter == null || mCurFilter.length() <= 1)) {

						if( mAdapter.getCount()!=cursor.getCount()||mListView.getAdapter()!=mAdapter) //means listview already have items
						{
							mAdapter.swapCursor(cursor);
							mListView.setAdapter(mAdapter);
							WQFerhengActivity.listofWords=ToList(cursor);
							//WQFerhengActivity.position = 0;
							//mCurFilter="";
							if (letter != null && !letter.equalsIgnoreCase("")) {
								setTitle(WQFerhengActivity.zimanquery + "(" + letter + "):"
										+ cursor.getCount() + " sernav");
							} else {
								setTitle(WQFerhengActivity.zimanquery + "("
										+ type.replace("*", "") + "):" + cursor.getCount()
										+ " sernav");
							}
						}
						return;
					}
					FilteredMatrixCursor matrix;
					if (!searchedItems.containsKey(mCurFilter)) {

						String filternormalized = WQFerhengDBOpenHelper
								.Normalize(mCurFilter);

						matrix = new FilteredMatrixCursor(cursor, filternormalized,	WQFerhengDB.KEY_WORD_N);
						if (matrix != null)
						{
							SearchResultAdapter adapter = new SearchResultAdapter(getBaseContext(), matrix.listOfItems,
									R.layout.result, columnsDB, to) ;


							//adapter.listOfWords=matrix.listOfItems;
							mListView.setAdapter(adapter);
							WQFerhengActivity.listofWords =  matrix.Items;

							//mAdapter.swapCursor(matrix);
							if (!searchedItems.containsKey(mCurFilter))
								searchedItems.put(mCurFilter, matrix);

							//WQFerhengActivity.listofWords = matrix.Items;

							//String title=matrix.getCount()+"/"+cursor.getCount();
							//setTitle(title);
							//Log.d("matrix.Items", matrix.Items.size() + "");
						}
					} else {
						matrix = searchedItems
								.get(mCurFilter);
						if (matrix != null) {
							//mAdapter.swapCursor(matrix);
							SearchResultAdapter adapter = new SearchResultAdapter(getBaseContext(), matrix.listOfItems,
									R.layout.result, columnsDB, to) ;
							mListView.setAdapter(adapter);
							WQFerhengActivity.listofWords = matrix.Items;
							//Log.d("matrix.Items", matrix.Items.size() + "");
						}
					}
					if(matrix!=null&&matrix.getCount()>0)
					{
						String title=matrix.listOfItems.size()+"/"+cursor.getCount();
						if (letter != null && !letter.equalsIgnoreCase("")) {
							setTitle(WQFerhengActivity.zimanquery + "(" + letter + "):"
									+ title + " sernav");
						} else {
							setTitle(WQFerhengActivity.zimanquery + "("
									+ type.replace("*", "") + "):" + title
									+ " sernav");
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		/** Creating a loader for populating listview from sqlite database */
		/** This statement, invokes the method onCreatedLoader() */
		getSupportLoaderManager().initLoader(0, null, this);

	}

	/** A callback method invoked by the loader when initLoader() is called */
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		if (letter != null && !letter.replace("-", "").equalsIgnoreCase("")) {

			WQFerhengDB.columntoSearch="both";
			String query = WQFerhengActivity.zimanquery + "*";
			String selection = WQFerhengDB.KEY_WORD_N + " match ? ";
			Uri uri = WQFerhengDBProvider.CONTENT_URI;
			query = WQFerhengDBOpenHelper.Normalize(letter) + "*";
			Log.d("query", query);
			WQFerhengActivity.letterx = letter;
			CursorLoader loader = new CursorLoader(this, uri, columnsDB,
					selection, new String[] { query }, null);
			return loader;

		} else if (type.replace("*", "").equalsIgnoreCase("bi deng")) {
			WQFerhengDB.columntoSearch = WQFerhengDB.KEY_DEFINITION;
			WQFerhengActivity.typeToSearch = type.replace("bi deng", "bideng");
			String query = "'" + WQFerhengActivity.zimanquery + "*"
					+ "*bideng*" + "*" + "'";
			if (!type.toLowerCase().contains("hemû"))
				query = "'" + WQFerhengActivity.zimanquery + "%*"
						+ type.replace("bi deng", "bideng") + "*" + "'" + "*";
			String selection = WQFerhengDB.KEY_DEFINITION + " LIKE ? ";
			Uri uri = WQFerhengDBProvider.CONTENT_URI;
			return new CursorLoader(this, uri, columnsDB, selection,
					new String[] { query }, null);
		} else {

			WQFerhengDB.columntoSearch = WQFerhengDB.KEY_DEFINITION;

			String query = "";// "'" + WQFerhengActivity.zimanquery + "%*'";
			if (!type.toLowerCase().contains("hemû")) {

				query += WQFerhengActivity.zimanquery + ":"
						+ type.replace("*", "") + ";";

			}

			String selection = WQFerhengDB.KEY_DEFINITION + " match ? ";

			Uri uri = WQFerhengDBProvider.CONTENT_URI;

			query = "'" + query.replace("ı", "i") + "'";
			return new CursorLoader(this, uri, columnsDB, selection,
					new String[] { query }, null);
		}
	}



	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

		if (arg1 == null)
			return;
		mAdapter.swapCursor(arg1);

		if (arg1 != null) {
			cursor = arg1;
			Log.d("loader", "finished");
			if (WQFerhengActivity.listofWords == null) {
				WQFerhengActivity.listofWords = ToList(arg1);
				Log.d("loader", "null");
			} else {
				WQFerhengActivity.listofWords = ToList(arg1);
				Log.d("loader", WQFerhengActivity.listofWords.size()
						+ " Cursour ssize" + arg1.getCount());
			}
			if (WQFerhengActivity.listofWords != null)
				Log.d("listofWords", WQFerhengActivity.listofWords.size() + "");

			if (actionBarIsEnabled) {
			}

			if (letter != null && !letter.equalsIgnoreCase("")) {
				setTitle(WQFerhengActivity.zimanquery + "(" + letter + "):"
						+ arg1.getCount() + " sernav");
			} else {
				setTitle(WQFerhengActivity.zimanquery + "("
						+ type.replace("*", "") + "):" + arg1.getCount()
						+ " sernav");
			}
		} else
			Toast.makeText(getBaseContext(), "Tu encam nehatin dîtin",
					Toast.LENGTH_SHORT).show();

		WQFerhengDB.columntoSearch = WQFerhengDB.KEY_WORD;
		pd.dismiss();
	}

	public List<Words> ToList(Cursor c) {
		List<Words> listOfWords = new ArrayList<Words>();
		try {
			//Log.d("cursor", c.getPosition() + "");
			if (c != null && c.getCount() > 0) {
				//c.moveToFirst();
				//Log.d("cursor", c.getPosition() + "");
				int x = 0;
				int totalcount = c.getCount();
				for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext() )  {
					String wordd = WQFerhengQueryProvider.GetValue(c,WQFerhengDB.KEY_WORD);

					String w_n = WQFerhengQueryProvider.GetValue(c,		WQFerhengDB.KEY_WORD_N);

					if (wordd == null || wordd.equalsIgnoreCase("")) {
						wordd = w_n;
					}

					Words w = new Words();
					w.id = WQFerhengQueryProvider.GetValue(c,
							WQFerhengDB.KEY_ID);
					w.peyv = wordd;
					listOfWords.add(w);
					if (x == totalcount - 1)
						break;
//					if(x<10)
//						Log.d( x+": ", wordd);
					x++;
				}
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfWords;

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
		WQFerhengDB.columntoSearch = WQFerhengDB.KEY_WORD;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

			case android.R.id.home:
				finish();
				return true;

			default:
				return true;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}

	private void finsihActivity() {
		android.os.Process.killProcess(android.os.Process.myPid());
		WQFerhengDB.columntoSearch = WQFerhengDB.KEY_WORD;
		this.finish();
	}
}