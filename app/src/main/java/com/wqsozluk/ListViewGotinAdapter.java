package com.wqsozluk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewGotinAdapter extends BaseAdapter implements OnTouchListener {

	// Declare Variables
	Context myBaseContext;
	private ArrayList<String> items=new ArrayList<String>();
	 HashMap<String, Boolean> IsPlaying=new HashMap<String, Boolean>();
	    HashMap<String, Boolean> IsPrePared=new HashMap<String, Boolean>();	

	public ListViewGotinAdapter(Context context, String text) 
	{
		this.myBaseContext = (Context) context;
		
		ArrayList<String> sounds=new ArrayList<String>();
		String[] lines = text.split(java.util.regex.Pattern.quote("\n"));
		
		if (lines != null && lines.length > 0
				&&( lines[0].toLowerCase().trim().startsWith("gotin")
						||lines[0].toLowerCase().trim().startsWith("xwendin")
						||lines[0].toLowerCase().trim().startsWith("bilêvkirin"))) 
		{
			for (int xx = 1; xx < lines.length; xx++) 
			{
				String line = lines[xx];
				if (line.toLowerCase().contains(".ogg") || line.toLowerCase().contains(".oga")
						|| line.toLowerCase().contains(".ogv") || line.toLowerCase().contains(".ogx"))
					sounds.add(line);
				
			}
			Log.d("sounds count",sounds.size()+"" );
			this.items = sounds;
		}
		else
		{
			this.items=new ArrayList<String>();
		}
			
	}

	@Override
	public int getCount() {
		return items==null?0:items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		ViewContainer container = new ViewContainer();

		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) myBaseContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_gotin, 
					null);
			convertView.setTag(container);
		}
		else
		{
			container=(ViewContainer)convertView.getTag();
		}

		final	String item = getItem(position).toString();
		
		convertView.setClickable(true);
		// Locate the TextViews in listview_item.xml
		container.txtTile = (TextView) convertView.findViewById(R.id.item_title_gotinlist);

		container.button = (Button) convertView.findViewById(R.id.button_gotin);
		container.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_gotin);		
		
		String[] strlinkanddef = item.split(":");
		String link=item.trim();
		if(strlinkanddef.length>1)
		{
			link=strlinkanddef[0].trim();
			container.txtTile.setText(strlinkanddef[1].trim());		
		}
		else
		{
			container.txtTile.setText("Deng");
		}
	
		container.button.setTag(link);
		CreateButtonMedia(container.button, container.progressBar, item);
	
		return convertView;
	}

	private void CreateButtonMedia(final Button button, final ProgressBar progress, String line) {
		
		try {
			String str = "";
					
			button.setFocusable(false);
				button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) 
				{
					 MediaPlayer mediaPlayer = new MediaPlayer();
					String uniqlineksuffix= button.getTag().toString().trim();
					if(!(uniqlineksuffix.endsWith(".ogg"))
							||(uniqlineksuffix.endsWith(".oga"))
							)
					{
						if(uniqlineksuffix.contains(".ogg"))
							uniqlineksuffix=uniqlineksuffix.substring(0, uniqlineksuffix.indexOf(".ogg")+4);
						else if(uniqlineksuffix.contains(".oga"))
							uniqlineksuffix=uniqlineksuffix.substring(0, uniqlineksuffix.indexOf(".oga")+4);						
					}
					if(uniqlineksuffix.contains("Ⓓ"))
					{
						uniqlineksuffix=uniqlineksuffix.substring(uniqlineksuffix.indexOf("Ⓓ")+1).trim();
					}
					
					String newlinkuniqlineksuffix=uniqlineksuffix.trim();
					Log.d("newlinkuniqlineksuffix",newlinkuniqlineksuffix );
					if (newlinkuniqlineksuffix.length()>1&& newlinkuniqlineksuffix.charAt(1) != '/' && newlinkuniqlineksuffix.charAt(4) != '/') {
						for (int xc = 1; xc < uniqlineksuffix.length(); xc++) {
							newlinkuniqlineksuffix = uniqlineksuffix.substring(xc);

							if (newlinkuniqlineksuffix.length()>3&& newlinkuniqlineksuffix.charAt(1) == '/' && newlinkuniqlineksuffix.charAt(4) == '/') {
								uniqlineksuffix = newlinkuniqlineksuffix;
								Log.d("breaking", xc+"");
								break;
							}
						}
					}					
					String afterDecode= "https://upload.wikimedia.org/wikipedia/commons/"
							+newlinkuniqlineksuffix.replace("ç", "%").trim();
					try {
						 afterDecode = URLDecoder.decode(afterDecode, "UTF-8");
						Log.d("afterDecode",afterDecode );
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
					
						e1.printStackTrace();
					}
					final String link =afterDecode ;

					try 
					{	
						mediaPlayer.setDataSource(link);					

					} catch (IllegalArgumentException e) 
					{
					} 
					catch (IllegalStateException e) 
					{
					} catch (IOException e) {
					}
					IsPlaying.put(button.getTag().toString(), false);
					IsPrePared.put(button.getTag().toString(), false);
					
						mediaPlayer
								.setOnErrorListener(new MediaPlayer.OnErrorListener() {
									public boolean onError(MediaPlayer mp, int what,
											int extra) {
										mp.reset();								
										progress.setVisibility(View.INVISIBLE);
										Toast.makeText(myBaseContext,  String.format("Error(%s%s)", what, extra)+"Link: "+link, Toast.LENGTH_LONG).show();
										return false;
									}
								});
						mediaPlayer
								.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
									public void onPrepared(MediaPlayer mp) {
										Log.e("pl", "pl");

										button.setCompoundDrawablesWithIntrinsicBounds( GetDrawable(R.drawable.pause), null, null, null );
										mp.start();
										IsPrePared.put( button.getTag().toString(), true);
										progress.setVisibility(View.INVISIBLE);
									}
								});				
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							
							@Override
							public void onCompletion(MediaPlayer mp) 
							{						
								button.setCompoundDrawablesWithIntrinsicBounds( GetDrawable(R.drawable.play), null, null, null );
							}
						});

						
					if(!IsConnected())
					{
						Toast.makeText(myBaseContext, "No Connection", Toast.LENGTH_LONG).show();					
						return;
					}
					if (!IsPlaying.get(button.getTag().toString())) 
					{										
						mediaPlayer.prepareAsync();	
						IsPlaying.put( button.getTag().toString(), true);
						progress.setVisibility(View.VISIBLE);
					}
					if (IsPrePared.get(button.getTag().toString())&&!mediaPlayer.isPlaying())
					{						
						button.setCompoundDrawablesWithIntrinsicBounds( GetDrawable(R.drawable.pause), null, null, null );
						mediaPlayer.start();						
					}
				}
			});


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Drawable GetDrawable(int id)
	{
		Drawable img = myBaseContext.getResources().getDrawable( id );
		img.setBounds( 0, 0, 60, 60 );
		return img;
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {

	    ListAdapter mAdapter = listView.getAdapter();

	    int totalHeight = 0;

	    for (int i = 0; i < mAdapter.getCount(); i++) {
	        View mView = mAdapter.getView(i, null, listView);

	        mView.measure(
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

	        totalHeight += mView.getMeasuredHeight();
	        Log.w("HEIGHT" + i, String.valueOf(totalHeight));

	    }

	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight
	            + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();

	}
	private Boolean IsConnected()
	{
		Boolean connected=false;
		ConnectivityManager cm = null;
		if (myBaseContext != null) 
		{
			cm = (ConnectivityManager) myBaseContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		} else {
			Toast.makeText(myBaseContext, "No Connection", Toast.LENGTH_LONG).show();		
		}
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnected()) 
		{
			connected=true;	
		} 
		else 
		{
			Log.d("null", "null");

		}
		return connected;
	}

	private class ViewContainer {
		TextView txtTile;		
		Button button;	
		ProgressBar progressBar;	
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
