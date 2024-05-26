package com.wqferheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wqferheng.WQFerhengDB.WQFerhengDBOpenHelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;
import android.widget.SimpleAdapter;

public class WQFerhengQueryProvider extends ContentResolver
{
	Context context;
	public Uri uriDB = WQFerhengDBProvider.CONTENT_URI;
	public String[] columnsDB = new String[] { WQFerhengDB.KEY_WORD,
			WQFerhengDB.KEY_DEFINITION, WQFerhengDB.KEY_WORD,WQFerhengDB.KEY_ID };
	public int[] to = new int[] { R.id.word, R.id.definition };
	public WQFerhengQueryProvider(Context cont) {
		super(cont);
		context=(Context)cont;
	}

	public Cursor GetCursor(String selection, String query) {
	
		String	s=query.replace("-", "");
		Cursor cursor =context. getContentResolver().query(uriDB, columnsDB, selection,
				new String[] { s }, WQFerhengDB.KEY_WORD_N + " ASC");
		return cursor;
	}
	public Words GetSingleWord(String id) 
	{
		return  WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
	}
	public Words GetSingleExactWord(String word) {
		Cursor cursor = null;	
		String normalized=WQFerhengDBOpenHelper. Normalize(word);
		String nn=""+ DatabaseUtils.sqlEscapeString(normalized.replace("-", "'-'"))+"";
		cursor = GetCursor(WQFerhengDB.KEY_WORD_N + " match ? ",nn);
		if(cursor==null)
		{
			 nn=""+ DatabaseUtils.sqlEscapeString(word.replace("-", "'-'"))+"";
			cursor = GetCursor(WQFerhengDB.KEY_WORD + " match ? ",nn);
		}
		Words wword=null;
		Boolean find = false;
		if (cursor != null) 
		{			
			if(cursor.getCount()==0)
				return wword;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String wordd = GetValue(cursor, WQFerhengDB.KEY_WORD);
				String wordd_n = GetValue(cursor, WQFerhengDB.KEY_WORD_N);
			
				if((wordd==null||wordd.equalsIgnoreCase(""))&&word.equals(normalized))
				{
					wordd=wordd_n;
				}
				if (wordd.equals(word)) 
				{
					String id = GetValue(cursor, WQFerhengDB.KEY_ID);
				
					Words w =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
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
							String wordd = GetValue(cursor, WQFerhengDB.KEY_WORD);
							String wordd_n = GetValue(cursor, WQFerhengDB.KEY_WORD_N);
							if (wordd.equalsIgnoreCase(word)||wordd_n.equalsIgnoreCase(word)) 
							{
								//String def = GetValue(cursor, WQFerhengDB.KEY_DEFINITION);
								String id = GetValue(cursor, WQFerhengDB.KEY_ID);
								//list.add(putData(wordd, def));
								Log.d("id", id+"");
								Words w =	WQFerhengDB.mWQferhengDBOpenHelper.GetSingleWord(id);
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
