package com.wqferheng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;

public class FilteredMatrixCursor extends CursorWrapper {

	public int[] filterMap = new int[] { 0, 1, 3, 5, 6 };
	private int mPos = -1;
	public MatrixCursor matrixCursor;
    public ArrayList<Words> Items=new ArrayList<Words>();
    public ArrayList<Map<String, String>> listOfItems=new ArrayList<
    		Map<String, String>>();
	public FilteredMatrixCursor(Cursor cursor, String filter, String column) {
		super(cursor);
		if (cursor != null)
			Filter(cursor, filter, column);
	}

	private void Filter(Cursor cursor, String filter, String column) {
		try {
		
			matrixCursor=new MatrixCursor(new String[]{BaseColumns._ID, WQFerhengDB.KEY_WORD,WQFerhengDB.KEY_WORD_N, WQFerhengDB.KEY_DEFINITION});
			List<Integer> list = new ArrayList<Integer>();	
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int x = 0;
				int totalCount=cursor.getCount();
				String filterlower=filter.toLowerCase();
				while (!cursor.isAfterLast())
				{				
					String word_n =WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD_N);
					if(filter.equalsIgnoreCase( "")||word_n.toLowerCase().contains(filterlower))
					{				
						list.add(x);
			
						String wordd =WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_WORD);
						String def = WQFerhengQueryProvider.GetValue(cursor, WQFerhengDB.KEY_DEFINITION);
						String id = WQFerhengQueryProvider.GetValue(cursor, BaseColumns._ID);
						if(wordd==null||wordd.equalsIgnoreCase(""))
							wordd=word_n;
						
						Words wword=new Words();
						wword.id=id;
						wword.peyv=wordd;
						wword.NormalizedWord=word_n;
						wword.IndexOfSearch=word_n.indexOf(filter);
						wword.wate=def;					
						Items.add(wword);
						
						}
						
					
					if(x<totalCount-1)
					cursor.moveToNext();
					else
						break;
					x++;
				}
			}
			 Collections.sort(Items, new Comparator<Words>() {
			        @Override public int compare(Words w1, Words w2) {
			        	  int compare = w1.NormalizedWord.length()-w2.NormalizedWord.length();			            
			                  return compare;

			        }

			    });
	
			for(int i=0;i<Items.size(); i++)
			{
				Words w=Items.get(i);
		
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(WQFerhengDB.KEY_WORD, w.peyv);
				item.put(WQFerhengDB.KEY_DEFINITION, w.wate);
				listOfItems.add(item);
				matrixCursor.addRow(new Object[]{w.id, w.peyv,w.NormalizedWord, w.wate});
			}
	
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