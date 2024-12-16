package com.wqsozluk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import android.database.Cursor;
import android.database.CursorWrapper;

public class SelectableCursorWrapper extends CursorWrapper {

	private int[] filterMap = new int[] { 0, 1, 3, 5, 6 };
	private int mPos = -1;
    public ArrayList<Map<String, String>> listOfItems=new ArrayList<
    		Map<String, String>>();
	 public List<Words> listOfWords=new ArrayList<Words>();
	public SelectableCursorWrapper(Cursor cursor, String filter, String column) {
		super(cursor);

		if (cursor != null)
			Filter(cursor, filter, column);

	}
	
	private void Filter(Cursor cursor, String filter, String column) {
		try {
			List<Integer> list = new ArrayList<Integer>();
			String filterlower=filter.toLowerCase();
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int x = 0;
				while (!cursor.isAfterLast())
				{
				//	int dIndex = cursor
						//	.getColumnIndexOrThrow(column);
					String word =WQDictionaryQueryProvider.GetValue(cursor, column); //cursor.getString(dIndex);
					if(filterlower.equalsIgnoreCase("")||word.toLowerCase().startsWith(filterlower))
					{
						list.add(x);				
						String wordd = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD);
						String id = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_ID);
						String w_n = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
						String def = WQDictionaryQueryProvider.GetValue(cursor, WQDictionaryDB.KEY_DEFINITION);
						HashMap<String, String> item = new HashMap<String, String>();
						if(wordd==null||wordd.equalsIgnoreCase("")	)
						{
							wordd= w_n;
						}
						
							item.put(WQDictionaryDB.KEY_WORD, wordd);
						item.put(WQDictionaryDB.KEY_DEFINITION, def);
						Words w=new Words();
						w.id=id;
						w.peyv=wordd;		
						w.NormalizedWord=w_n;
						listOfWords.add(w);
						listOfItems.add(item);
					}

					cursor.moveToNext();
					x++;
				}
			}
			//Log.e("filtering", "filtering:" +list.size());
			filterMap = toIntArray(list);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	int[] toIntArray(List<Integer> list) {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)
			ret[i++] = e.intValue();
		return ret;
	}

	@Override
	public int getCount() {
		return filterMap.length;
	}

	@Override
	public boolean moveToPosition(int pos) {
		boolean moved = super.moveToPosition(filterMap[pos]);
		if (moved)
			mPos = pos;
		return moved;
	}

	@Override
	public final boolean move(int offset) {
		return moveToPosition(mPos + offset);
	}

	@Override
	public final boolean moveToFirst() {
		return moveToPosition(0);
	}

	@Override
	public final boolean moveToLast() {
		return moveToPosition(getCount() - 1);
	}

	@Override
	public final boolean moveToNext() {
		return moveToPosition(mPos + 1);
	}

	@Override
	public final boolean moveToPrevious() {
		return moveToPosition(mPos - 1);
	}

	@Override
	public final boolean isFirst() {
		return mPos == 0 && getCount() != 0;
	}

	@Override
	public final boolean isLast() {
		int cnt = getCount();
		return mPos == (cnt - 1) && cnt != 0;
	}

	@Override
	public final boolean isBeforeFirst() {
		if (getCount() == 0) {
			return true;
		}
		return mPos == -1;
	}

	@Override
	public final boolean isAfterLast() {
		if (getCount() == 0) {
			return true;
		}
		return mPos == getCount();
	}

	@Override
	public int getPosition() {
		return mPos;
	}
}