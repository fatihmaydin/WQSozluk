package com.wqsozluk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends SimpleAdapter
{
 //Context mContext;
 public List<Words> listOfWords=new ArrayList<Words>();
 public List< Map<String, String>> list;
	public SearchResultAdapter(Context context,
			List< Map<String, String>> data, int resource, String[] from,
 int[] to) {
		super(context, data, resource, from, to);
		//mContext = context;
		list = data;
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolderWords holder;			            
        if (v == null) {
            holder = new ViewHolderWords();

            v =LayoutInflater.from( WQDictionaryActivity.mContext).inflate(R.layout.result,
					null);           
            holder.textViewWord = (TextView) v.findViewById(R.id.word);
            holder.textViewdef = (TextView) v.findViewById(R.id.definition); 
            v.setTag(holder);
        } else {
            holder = (ViewHolderWords) v.getTag();
        }

        Map<String, String> data = list.get(position);
        String word=data.get(WQDictionaryDB.KEY_WORD);
        String definition=data.get(WQDictionaryDB.KEY_DEFINITION);
        
       	if(!(definition.toLowerCase().contains("bideng;")||definition.toLowerCase().contains(".oga")))		   
       	{
        	holder.textViewWord.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
       	}
       	else
       	{
       		Drawable img = WQDictionaryActivity.mContext.getResources().getDrawable( R.drawable.volume );
       		holder.textViewWord.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null );
       		
       	}
            holder.textViewWord.setText(word);		
            holder.textViewdef.setText(definition);
        return v;
    }
	 public static class ViewHolderWords {
         TextView textViewWord;
         TextView textViewdef;
         ImageButton button;
         LinearLayout layoutRow;
         //your other views
     }

}
