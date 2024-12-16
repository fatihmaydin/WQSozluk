package com.wqdictionary;

import com.wqdictionary.WQDictionaryDB.WQDictionaryDBOpenHelper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

public class WQDictionaryQueryProvider extends ContentResolver
{
	Context context;
	public Uri uriDB = WQDictionaryDBProvider.CONTENT_URI;
	public String[] columnsDB = new String[] { WQDictionaryDB.KEY_WORD,
			WQDictionaryDB.KEY_DEFINITION, WQDictionaryDB.KEY_WORD,WQDictionaryDB.KEY_ID };
	public int[] to = new int[] { R.id.word, R.id.definition };
	public WQDictionaryQueryProvider(Context cont) {
		super(cont);
		context=(Context)cont;
	}

	public Cursor GetCursor(String selection, String query) {
	
		String	s=query.replace("-", "");
		Cursor cursor =context. getContentResolver().query(uriDB, columnsDB, selection,
				new String[] { s }, WQDictionaryDB.KEY_WORD_N + " ASC");
		return cursor;
	}
	public Words GetSingleWord(String id) 
	{
		return  WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
	}
	public Words GetSingleExactWord(String word) {
		Cursor cursor = null;	
		String normalized=WQDictionaryDBOpenHelper. Normalize(word);
		String nn= DatabaseUtils.sqlEscapeString(normalized.replace("-", "'-'"));
		cursor = GetCursor(WQDictionaryDB.KEY_WORD_N + " match ? ",nn);
		if(cursor==null)
		{
			 nn= DatabaseUtils.sqlEscapeString(word.replace("-", "'-'"));
			cursor = GetCursor(WQDictionaryDB.KEY_WORD + " match ? ",nn);
		}
		Words wword=null;
		Boolean find = false;
		if (cursor != null) 
		{			
			if(cursor.getCount()==0)
				return wword;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String wordd = GetValue(cursor, WQDictionaryDB.KEY_WORD);
				String wordd_n = GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
			
				if((wordd==null||wordd.equalsIgnoreCase(""))&&word.equals(normalized))
				{
					wordd=wordd_n;
				}
				if (wordd.equals(word)) 
				{
					String id = GetValue(cursor, WQDictionaryDB.KEY_ID);
				
					Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
					if(w!=null)
					{
					wword=w;
					
					find = true;
					break;
					}
				}
				cursor.moveToNext();
			}
			if (find) 
			{

			} 
			else 
			{
				if (cursor.getCount() > 0) 
				{
					if (cursor != null) {
						cursor.moveToFirst();
						while (!cursor.isAfterLast()) 
						{
							String wordd = GetValue(cursor, WQDictionaryDB.KEY_WORD);
							String wordd_n = GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
							if (wordd.equalsIgnoreCase(word)||wordd_n.equalsIgnoreCase(word)) 
							{
								//String def = GetValue(cursor, WQDictionaryDB.KEY_DEFINITION);
								String id = GetValue(cursor, WQDictionaryDB.KEY_ID);
								//list.add(putData(wordd, def));
								Log.d("id", id);
								Words w =	WQDictionaryDB.mWQDictionaryDBOpenHelper.GetSingleWord(id);
								if(w!=null)
								{
								wword=w;
								}}
				
							cursor.moveToNext();
						}
					}

				}

			}
		}

		return wword;

	}

	public static String GetValue(Cursor cursor, String ColumnName) {
		int dIndex = cursor.getColumnIndexOrThrow(ColumnName);
		String def = cursor.getString(dIndex);
		return def;
	}


}
