package com.wqferheng;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.os.StatFs;
import android.provider.BaseColumns;
import android.util.Log;

public class WQFerhengDB {
	private static final String TAG = "WQFerhengDB";

	// The columns we'll include in the dictionary table
	public static final String KEY_WORD = "word";	
	public static final String KEY_DEFINITION = "definition";
	public static final String KEY_WORD_N = "word_N";
	public static final String KEY_ID= "id";

	private static String DATABASE_NAME = "wqferheng";
	private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
	private static final String FTS_VIRTUAL_TABLE2 = "FTSdictionary_Defs";
	private static final String FTS_VIRTUAL_TABLE_FAV = "FTSdictionary_FAVs";
	public static final int DATABASE_VERSION = 17;
	//int Index = 0;

	static WQFerhengDBOpenHelper mWQferhengDBOpenHelper = null;

	public static String columntoSearch = "";
	private static final HashMap<String, String> mColumnMap = buildColumnMap();

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The Context within which to work, used to create the DB
	 */
	public WQFerhengDB(Context context) {

			mWQferhengDBOpenHelper = new WQFerhengDBOpenHelper(context);
	}

	private long GetFreeSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);

		return megAvailable;
	}

	private long GetFreeSize(File path) {

		StatFs stat = new StatFs(path.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);

		return megAvailable;
	}

	private static HashMap<String, String> buildColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(KEY_WORD, KEY_WORD);
		map.put(KEY_DEFINITION, KEY_DEFINITION);
		map.put(KEY_WORD_N, KEY_WORD_N);
		map.put(KEY_ID, KEY_ID);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
		map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
		map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
		return map;
	}

	/**
	 * Returns a Cursor positioned at the word specified by rowId
	 * 
	 * @param rowId
	 *            id of word to retrieve
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor positioned to matching word, or null if not found.
	 */
	public Cursor getWord(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };

		return query(selection, selectionArgs, columns);

	}
	
	/**
	 * Returns a Cursor over all words that match the given query
	 * 
	 * @param query
	 *            The string to search for
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor over all words that match, or null if none found.
	 */
	public Cursor getWordMatches(String selection, String query, String[] columns) {

	
		Cursor cursor;
		String[] selectionArgs = new String[] { query  };
		
		if (columntoSearch.equalsIgnoreCase(KEY_DEFINITION)) 
		{
			selection = KEY_DEFINITION + " MATCH ?";
		
			if (WQFerhengActivity.typeToSearch.equalsIgnoreCase("hemû")) 
			{
				String query1=WQFerhengActivity.zimanquery
						+ WQFerhengActivity.headerEndChar + "*";
				selectionArgs = new String[] { query1 };
				cursor = query(selection, selectionArgs, columns);				
			} 
			else 
			{
				if(WQFerhengActivity.typeToSearch.contains( "Ⓓ"))
				{

				String query1=WQFerhengActivity.zimanquery
					+ WQFerhengActivity.headerEndChar

					+"*"
					+ "ogg"  ;
			selectionArgs = new String[] { query1};	
				}

				selectionArgs = new String[] { query};	
				cursor = query(selection, selectionArgs, columns);
			}
		
		} else if (columntoSearch.equalsIgnoreCase("both")) {
			selection = KEY_WORD_N + " MATCH ?";
		//	Log.d("WQFerhengActivity.zimanquery", WQFerhengActivity.zimanquery);
			//Log.d("query", query);
			//Log.d("selectionArgs", selectionArgs[0].toString());
			cursor= query(selection, selectionArgs, columns);
			if(cursor!=null)
			System.out.println("Cursor returned, now wrapping:"+ cursor.getCount());
			return new CurSorWrapperDefinition(cursor,
					WQFerhengActivity.zimanquery
						,
					WQFerhengActivity.letterx);			
			
		} 
		else 
		{
			String deglect=selection;
			cursor = query(deglect, selectionArgs, columns);			
		}

		return cursor;

	}

	/**
	 * Performs a database query.
	 * 
	 * @param selection
	 *            The selection clause
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param columns
	 *            The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(String selection, String[] selectionArgs,
			String[] columns) {

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(FTS_VIRTUAL_TABLE);
		builder.setProjectionMap(mColumnMap);

		Cursor cursor = builder.query(
				mWQferhengDBOpenHelper.getReadableDatabase(), columns,
				selection, selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}

	/**
	 * This creates/opens the database.
	 */
	public static class WQFerhengDBOpenHelper extends SQLiteOpenHelper {

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		public static int WordList = 0;
		public static int LoadedRawWordFileCount = 0;
		public static int Index = 0;
		public static int totalFileCount = 30;
		public static Boolean Loading = false;
		public static Boolean IsTableDropped = false;
		public static Boolean IsUpgrading = false;
		public static Boolean InstallToExternal = false;	
		private static final String FTS_TABLE_CREATE = "CREATE VIRTUAL TABLE "
				+ FTS_VIRTUAL_TABLE + " USING fts3 (" + KEY_WORD + ", "
				+ KEY_DEFINITION+ ", "
						+ KEY_WORD_N + ", "
						+ KEY_ID + ");";
		
		private static final String FTS_TABLE_CREATE2 = "CREATE TABLE "
				+ FTS_VIRTUAL_TABLE2 + " (" + KEY_ID + ", "
				+ KEY_DEFINITION+ ");";
		
		private static final String FTS_TABLE_CREATE_FAV = "CREATE VIRTUAL TABLE "
				+ FTS_VIRTUAL_TABLE_FAV + " USING fts3 (" + KEY_WORD +");";

		WQFerhengDBOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}

		public WQFerhengDBOpenHelper(Context context,
				final String DATABASE_NAME, int version) {

			super(context, DATABASE_NAME, null, version);
			mHelperContext = context;
			InstallToExternal = true;

		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{	
			
			mDatabase = db;
			if (InstallToExternal) 
			{
				
				if (!IsTableDropped)
					db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);

				mDatabase = db;
			}	
			try
			{
			mDatabase.execSQL(FTS_TABLE_CREATE);
			}
			finally
			{				
			}
			try
			{
			mDatabase.execSQL(FTS_TABLE_CREATE2);
		//	mDatabase.execSQL("CREATE INDEX id_index "+
				//	"on "+FTS_VIRTUAL_TABLE2+" ("+KEY_ID+");");
			}
			finally
			{				
			}

			try 
			{
				if(!IsUpgrading)
					mDatabase.execSQL(FTS_TABLE_CREATE_FAV);

			} 
			finally 
			{

			}
		
			loadDictionary();
			
		
			
		}
		public boolean isTableExists(String tableName, boolean openDb) {
		    if(openDb) {
		        if(mDatabase == null || !mDatabase.isOpen()) {
		            mDatabase = getReadableDatabase();
		        }

		        if(!mDatabase.isReadOnly()) {
		            mDatabase.close();
		            mDatabase = getReadableDatabase();
		        }
		    }

		    Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
		    if(cursor!=null) {
		        if(cursor.getCount()>0) {
		                            cursor.close();
		            return true;
		        }
		                    cursor.close();
		    }
		    return false;
		}

		/**
		 * Starts a thread to load the database table with words
		 */
		private void loadDictionary() {
			new Thread(new Runnable() {
				public void run() {
					try {
						Log.d(TAG, "Loading words...");

					   // Date currentDate = new Date();
						Loading = true;
						ArrayList<String> listRaw=GetRawResList();
						int count=0;

						totalFileCount=listRaw.size();
						
						for (int i = 0; i < listRaw.size(); ++i)
						{
							int id =mHelperContext.getResources().getIdentifier(listRaw.get(i), "raw", mHelperContext.getPackageName());
							Log.d("Loading", listRaw.get(i));
							loadWords(id);
							LoadedRawWordFileCount++;
						}
						try
						{
						Log.d("Index", "creating");
						mDatabase.execSQL("CREATE INDEX id_index "+
								"on "+FTS_VIRTUAL_TABLE2+" ("+KEY_ID+");");
						Log.d("Index", "created");
						}
						finally
						{				
						}
						
						Loading = false;
						Log.d(TAG, "DONE loading words.");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
		}
		private String GetValue(Cursor cursor, String ColumnName) {
			int dIndex = cursor.getColumnIndexOrThrow(ColumnName);
			String def = cursor.getString(dIndex);
			return def;
		}
		public Words GetSingleWord(String id)
		{
			Date currentDate1 = new Date();
			Cursor c=this.getReadableDatabase().rawQuery("select * from "+FTS_VIRTUAL_TABLE2 +" where "+KEY_ID+"='"+id+"'", null);
			
			Words ww=null;
			if(c!=null&&c.moveToFirst())
			{
				String	id2 = GetValue(c, WQFerhengDB.KEY_ID);			
				String def = GetValue(c, WQFerhengDB.KEY_DEFINITION);
		
				ww=new Words();
				ww.id=id2;
				ww.wate=def;		
			}
			Date currentDate2 = new Date();
			long diffInMs = currentDate2.getTime() -currentDate1.getTime();

			long diffInSec = TimeUnit.MILLISECONDS.toMillis(diffInMs);
		//	Log.d("Selected word in ", diffInSec +" msecs");
			return ww;
		}
		
		public Words GetFavWord(String word)
		{
			
			Cursor c=this.getReadableDatabase().rawQuery("select * from "+FTS_VIRTUAL_TABLE_FAV+" where "+KEY_WORD+"='"+word+"'", null);
		
			Words ww=null;
			if(c!=null&&c.moveToFirst())
			{
				String	id2 = GetValue(c, WQFerhengDB.KEY_WORD);
				ww=new Words();
				ww.peyv=id2;
			}			
			return ww;
		}
		public String DeleteFavWord(String word)
		{
			
			try {
				this.getReadableDatabase().execSQL("delete from "+FTS_VIRTUAL_TABLE_FAV+" where "+KEY_WORD+"='"+word+"'");
				return "'"+word+ "' hat jê birin"; 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "'"+word+ "' nekarî bê jê birin."; 
			}
		
		}
		public String InsertFavWord(Context cont, String word)
		{
			try {
				Words w=GetFavWord(word);
				if(w!=null)
				{
					return cont.getString(R.string.favwordexists, word);
				}
				else
				{
		
				this. getReadableDatabase().execSQL("INSERT INTO "+FTS_VIRTUAL_TABLE_FAV+" (" + KEY_WORD +") VALUES ('"+word+"')");
				
				return cont.getString(R.string.addedtofav, word);
				}
			} 
			catch (SQLiteException e){
			    if (e.getMessage().contains("no such table"))
			    {
			            Log.d(TAG, "Tabloyê " + FTS_VIRTUAL_TABLE_FAV + " tune, loma dike bê çêkirin!" );
			           			        	
			        	try
			        	{
			        	if(mDatabase==null)
			        	 mDatabase =WQFerhengDB.mWQferhengDBOpenHelper.getReadableDatabase();
			        	
					     mDatabase.execSQL(FTS_TABLE_CREATE_FAV);
					     Log.d(TAG, "Tabloyê " + FTS_VIRTUAL_TABLE_FAV + " hat çêkirin!" );
			        	this. getReadableDatabase().execSQL("INSERT INTO "+FTS_VIRTUAL_TABLE_FAV+" (" + KEY_WORD +") VALUES ('"+word+"')");
						
						return cont.getString(R.string.addedtofav, word);
			        	}
			        	catch (SQLException ex) 
						{
						
							ex.printStackTrace();
							return  cont.getString(R.string.notaddedtofav, word);
						}
			        
			            // re-run query, etc.
			    }
			    else
			    	return  cont.getString(R.string.notaddedtofav, word);
			    	
			}
			
			
		}
		public ArrayList< Words> GetFavWords()
		{
			try {
				//Date currentDate1 = new Date();
				Cursor c=this.getReadableDatabase().rawQuery("select * from "+FTS_VIRTUAL_TABLE_FAV, null);
				//Cursor defTable =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
				ArrayList<Words> words=new ArrayList<Words>();

				if (c != null) {

					c.moveToFirst();
					while (c.isAfterLast() == false) {
						Words ww = null;
						String id2 = GetValue(c, WQFerhengDB.KEY_WORD);
				
						ww = new Words();
						ww.peyv = id2;
						ww.wate = "";
					
						words.add(ww);
					
						c.moveToNext();
					}
				}		
				return words;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		private void loadWords(int id) throws IOException {
		
			InputStream inputStream = null;
			BufferedReader reader = null;
			int itemcounMax = 25000;
			
			final ArrayList<Words> words = new ArrayList<Words>();
			try {
				
				if (android.os.Build.VERSION.SDK_INT < 11) {
					itemcounMax = 5000;
				}
				final Resources resources = mHelperContext.getResources();
				inputStream = resources.openRawResource(id);
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) 
				{
					if (DELIM.equalsIgnoreCase("\\*")) 
					{		
						Words w=splitIt(line);				
							words.add(w);
						
						WordList++;
						if (WordList % itemcounMax == 0) 
						{			
							addWords(words);
							words.clear();
						}
					} 
					else if (line.contains("¬")) 
					{				

						Words w=splitIt(line);
				
							words.add(w);
						
						WordList++;
						if (WordList % itemcounMax == 0) 
						{
							
							addWords(words);
							words.clear();

						}
					} 
					else if (line.contains("::")) 
					{
					
						Words w = new Words(line.substring(0,
								line.indexOf("::")).trim(), line.substring(
								line.indexOf("::") + 2).trim());						
						if (!w.getpeyv().equalsIgnoreCase(""))
							words.add(w);
						
						WordList++;
						if (WordList % itemcounMax == 0) {
							addWords(words);
							words.clear();
						}
					} 
					else
						continue;

				}
			} finally 
			{
				if (reader != null)
					reader.close();				
			}
			addWords(words);	
		//	Log.d("readed",""+id);
		}
		private static final String DELIM = "\\*";		
		public Words splitIt(String input) {
	
		   
		    String wordd="";
		    String def="";
		    String tags="";
		    String normalized="";
		    String[] splitS = input.split(DELIM);
		
		    for(int i =0; i < splitS.length; i++)
		    {
		    String next=splitS[i].trim();
		        if(i==0)
		        	wordd=next;
		        else if(i==1)
		        	normalized=next;
		        else if(i==2)
		        	def=next;	
		        else
		        	tags=next;	
		        //i++;
		        
		    }
		    Words word=new Words(wordd, normalized, def, tags);
		    return word;
		}
		public Words splitIt2(String input) {

			int indexof1= input.indexOf(DELIM);
			int indexof2=input.indexOf(DELIM, indexof1+1);			  
			    Words word=new Words(input.substring(0,indexof1).trim(), input.substring(indexof1+1, indexof2).trim(), input.substring(indexof2+1).trim(),"");			  
			    return word;
			}

		public int GetLineCount(int resId) throws IOException {
			Resources resources = mHelperContext.getResources();
			InputStream is = null;
			try {
				is = resources.openRawResource(resId);
				if (is == null)
					return 100000;
				byte[] c = new byte[1024];
				int count = 0;
				int readChars = 0;
				boolean empty = true;
				while ((readChars = is.read(c)) != -1) {
					empty = false;
					for (int i = 0; i < readChars; ++i) {
						if (c[i] == '\n')
							++count;
					}
				}
				return (count == 0 && !empty) ? 1 : count;
			} finally {
				is.close();
			}
		}
		public ArrayList<String> GetRawResList(){
		    java.lang.reflect.Field[] fields=R.raw.class.getFields();
		    ArrayList<String> fieldlist=new ArrayList<String>();
		    for(int count=0; count < fields.length; count++)
		    {
		    	fieldlist.add(fields[count].getName());		    
		    }
		    Collections.sort(fieldlist);
		    return fieldlist;
		}
		/**
		 * Add a word to the dictionary.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addWord(String word, String definition) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_WORD, word);
			initialValues.put(KEY_DEFINITION, definition);
			initialValues.put(KEY_WORD_N, Normalize(word));
			return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
		}
		public static String Normalize(String toNormalize)
		{	
			toNormalize=toNormalize.replace("أ", "ا") .replace("آ", "ا").replace("ي", "ی").replace( "ئ", "ی").replace("ك", "ک")
					.replace("إ", "ا").replace("اً", "ا");
			return deAccent(toNormalize);
		}
		public static String deAccent(String str) {
			str=str.replace('ı', 'i').replace('ğ', 'g').replace('I', 'İ');
		    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
		    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		    return pattern.matcher(nfdNormalizedString).replaceAll("");
		}
		public void addWords(ArrayList<Words> words) {
			
			
		//	Date currentDate1 = new Date();
			
			String sql = "INSERT INTO FTSdictionary (" + KEY_WORD + ", "
					+ KEY_DEFINITION+ ", "
							+ KEY_WORD_N +", "+KEY_ID+ ") VALUES (?, ?, ?, ?)";
			String sql2 = "INSERT INTO FTSdictionary_Defs (" + KEY_ID + ", "
					+ KEY_DEFINITION+") VALUES (?, ?)";
			SQLiteStatement stmt2 = mDatabase.compileStatement(sql2);
			
			/////////////
			//mDatabase.execSQL("PRAGMA synchronous=OFF");
			//mDatabase.setLockingEnabled(false);
			mDatabase.beginTransaction();
			/////////////////
			SQLiteStatement stmt = mDatabase.compileStatement(sql);
			Log.d("inserting","inserting");
			for (int i = 0; i < words.size(); i++) 
			{
				Words w=words.get(i);
				String id= ""+ Integer.toString(Index, 16);
//				if(w.getpeyv().length()<id.length())
//					id=w.getpeyv();
				if(w.getpeyv()==null&& w.getpeyv().equalsIgnoreCase(""))
				{
					//stmt.bindString(1, w.getpeyv());
				}
				else
					stmt.bindString(1, w.getpeyv());
				
//				if(Index<6)
//					stmt.bindString(2, w.getwate());	
//				else
//				{
					stmt.bindString(2, w.gettags());//w.getwate());		
				//}
				stmt.bindString(3, w.getNormalized());
				stmt.bindString(4,id);
				stmt.execute();

				stmt.clearBindings();
				
				stmt2.bindString(1,id);//w.getpeyv());
				stmt2.bindString(2,w.getwate());				
			//	stmt2.bindString(3,"");//words.get(i).getNormalized());
				
				stmt2.execute();

				stmt2.clearBindings();
				Index++;
			}
			
			mDatabase.setTransactionSuccessful();
			mDatabase.endTransaction();
			

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			IsUpgrading=true;
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			try
			{
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
			}
			finally
			{
				
			}
			try {

				db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE2);
			} 
			finally 
			{

			}
			Log.d(TAG, "table dropped for upgrating");
			IsTableDropped = true;
			onCreate(db);
			IsUpgrading=false;
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			IsUpgrading=true;
			Log.w(TAG, "Downgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			try
			{
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
			}
			finally
			{
				
			}
			try
			{
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE2);
			}
			finally
			{
				
			}
			Log.d(TAG, "table dropped for downgrading");
			IsTableDropped = true;
			
			onCreate(db);
			IsUpgrading=false;
		}
	}

}
