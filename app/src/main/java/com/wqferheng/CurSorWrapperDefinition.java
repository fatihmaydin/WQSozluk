package com.wqferheng;

import java.util.ArrayList;
import java.util.List;



import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

public class CurSorWrapperDefinition extends CursorWrapper {

	private int[] filterMap = new int[] { 0, 1, 3, 5, 6 };
	private int mPos = -1;
	public List<Words> listOfWords=new ArrayList<Words>();

	public CurSorWrapperDefinition(Cursor cursor, String filter, String letter) {
		super(cursor);

		if (cursor != null)
			Filter(cursor, filter, letter);

	}
	
	public CurSorWrapperDefinition(Cursor cursor, String filter) {
		super(cursor);

		if (cursor != null)
			Filter(cursor, filter);

	}

	private void Filter(Cursor cursor, String filter, String letter) {
		try {

			List<Integer> list = new ArrayList<Integer>();
			String query = WQFerhengActivity.zimanquery;
			Log.d("CursorWrapper", query);
			
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int x = 0;
				//Log.d("CursorWrapper","letter: "+ letter+", query:"+filter+", Cursor count:"+cursor.getCount());
				while (!cursor.isAfterLast()) {
					//int dIndex = cursor
						//	.getColumnIndexOrThrow(WQFerhengDB.KEY_DEFINITION);
					String ddef = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_DEFINITION ) ;//cursor.getString(dIndex);
					//int dIndexw = cursor
						//	.getColumnIndexOrThrow(WQFerhengDB.KEY_WORD);
					String wword = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD );//cursor.getString(dIndexw);
					if(wword==null||wword.equalsIgnoreCase(""))
					{
						//int dIndexw_n = cursor
							//	.getColumnIndexOrThrow(WQFerhengDB.KEY_WORD_N);
						String wword_n =WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD_N ); //cursor.getString(dIndexw_n);
						wword=wword_n;
					}

					if (ddef.contains(filter)&&wword.toLowerCase().startsWith(letter.toLowerCase()))
					{
						list.add(x);
						
						Words word=new Words();
						word.peyv=wword;
						word.NormalizedWord=wword;
						word.wate=ddef;
						listOfWords.add(word);
					}
					cursor.moveToNext();
					x++;
				}
			}
			filterMap = toIntArray(list);
			WQFerhengActivity.listofWords=listOfWords;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void Filter(Cursor cursor, String filter) {
		try {

			List<Integer> list = new ArrayList<Integer>();

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int x = 0;
				while (!cursor.isAfterLast()) {
				//	int dIndex = cursor
					//		.getColumnIndexOrThrow(WQFerhengDB.KEY_DEFINITION);
					String word =WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_DEFINITION ); //cursor.getString(dIndex);
					//int dIndexw = cursor
						//	.getColumnIndexOrThrow(WQFerhengDB.KEY_WORD);
					String def = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD );//cursor.getString(dIndexw);

					if (word.contains(filter))
					{
						list.add(x);
						
						Words wordF=new Words();
						wordF.peyv=word;
						wordF.NormalizedWord=word;
						wordF.wate=def;
						listOfWords.add(wordF);
					}
					cursor.moveToNext();
					x++;
				}
			}
			filterMap = toIntArray(list);
			WQFerhengActivity.listofWords=listOfWords;
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