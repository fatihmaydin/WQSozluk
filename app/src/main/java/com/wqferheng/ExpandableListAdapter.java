package com.wqferheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private List<GroupEntity> mGroupCollection;
	private int[] groupStatus;
	private AutoCompleteTextView textView;
	 OnTouchListener onSwipeListener;
	 public Boolean handleClick=true;
	String startofLink = "[";
    String endofLink = "]";
    String startofCategoryLink = "£";
    String endofCategoryLink = "£";
	WQFerhengActivity wFerhengActivity;
	DefinitionActivity definitionActivity;

	
	AboutActivity aboutActivity;	
	public String Word;
	public String definition;
	
	private static final long DOUBLE_CLICK_TIME_DELTA = 300;// milliseconds
	private int previousgroupClickIndex = -1;

	long lastClickTime = 0;
    HashMap<String, Boolean> IsPlaying=new HashMap<String, Boolean>();
    HashMap<String, Boolean> IsPrePared=new HashMap<String, Boolean>();
	public ExpandableListAdapter(WQFerhengActivity pContext,
			ExpandableListView pExpandableListView,
			List<GroupEntity> pGroupCollection, OnSwipeTouchListener onswipelistener) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];
		wFerhengActivity = (WQFerhengActivity) pContext;
		textView = (AutoCompleteTextView) wFerhengActivity
				.findViewById(R.id.autocomplete_search);
		onSwipeListener=onswipelistener;
		setListEvent();
		setOnTouch();
	}

	public ExpandableListAdapter(DefinitionActivity pContext,
			ExpandableListView pExpandableListView,
			List<GroupEntity> pGroupCollection, OnSwipeTouchListener onswipelistener) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];
		definitionActivity = (DefinitionActivity) pContext;
		textView = (AutoCompleteTextView) definitionActivity
				.findViewById(R.id.autocomplete_search);
		onSwipeListener=onswipelistener;
		setListEvent();
		setOnTouch();
	}

	private void setOnTouch() {
		// TODO Auto-generated method stub
	
	   
		mExpandableListView.setOnTouchListener(onSwipeListener);
	}

	public ExpandableListAdapter(AboutActivity pContext,
			ExpandableListView pExpandableListView,
			List<GroupEntity> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];
		aboutActivity = (AboutActivity) pContext;
		textView = (AutoCompleteTextView) aboutActivity
				.findViewById(R.id.autocomplete_search);
		setListEvent();
	}

	private void setListEvent() {

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 1;	
					}					
				});

		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 0;
					}
				});
		mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				handleClick=false;
				Log.d("group clicked", "click");
				return false;
			}
		});

		mExpandableListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						long clickTime = System.currentTimeMillis();

						if ((clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA)
								&& previousgroupClickIndex == groupPosition) {
								        	
						}

						previousgroupClickIndex = groupPosition;
						lastClickTime = clickTime;
						return false;
					}
				});

		mExpandableListView
				.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						final int groupPosition = ExpandableListView
								.getPackedPositionGroup(arg3);
					//	mExpandableListView.collapseGroup(groupPosition);
						return false;
					}
				});
		
	   
		
	}

	@Override
	public String getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).GroupItemCollection.get(arg1).Name;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	@SuppressLint("NewApi")
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		ChildHolder childHolder;	
		String text = mGroupCollection.get(arg0).GroupItemCollection.get(arg1).Name;
		//wFerhengActivity.makeText(text);
		Pattern regexberal = Pattern.compile("[0-9]\\s*\\.\\s*BERALÎKIRIN");
		Matcher regexMatcherberal = regexberal.matcher(text);
		
		
		if(regexMatcherberal.find())
		{
			text=regexMatcherberal.replaceFirst("Binihêre:");
		}
		
		String[] lines = text.split(java.util.regex.Pattern.quote("\n"));
		String line0=lines[0].toLowerCase();
		if(WQFerhengActivity.ShowHeaderTranslation) {
			if (WQFerhengActivity.mapLanguages.containsKey(line0.replace(":", ""))) {
				String translation = GetTranslation(line0.replace(":", ""));
				if(!translation.trim().equalsIgnoreCase("")) {
					lines[0] = lines[0].replace(":", "") + " (" + translation + ")" + ":";
				}
//				if (wFerhengActivity != null)
//					wFerhengActivity.makeText(line0.replace(":", ""));
			}
		}
		String newtext="";
		for(int i=0; i<lines.length; i++)
		{
			newtext+=lines[i];
			if(i<(lines.length-1))
				newtext+="\n";
		}
		text=newtext;
		
		if (lines != null && lines.length > 0
				&& (lines[0].toLowerCase().trim().equalsIgnoreCase("gotin:")||lines[0].toLowerCase().trim().equalsIgnoreCase("xwendin:")
						||lines[0].toLowerCase().trim().equalsIgnoreCase("bilêvkirin:")
				||lines[0].toLowerCase().trim().startsWith("bilêvkirin (")
				||lines[0].toLowerCase().trim().startsWith("bilêvkirin")
						)) 
		{
			String newtextWithoutgotin="";
		
			for (int xx = 0; xx < lines.length; xx++) {
				String line = lines[xx];	
			
				if (line.toLowerCase().contains(".ogg") || line.toLowerCase().contains(".oga")
						|| line.toLowerCase().contains(".ogv") || line.toLowerCase().contains(".ogx"))
					continue;
				else
					newtextWithoutgotin += line + "\n";
			}
			text=newtextWithoutgotin.endsWith("\n")?newtextWithoutgotin.substring(0, newtextWithoutgotin.length()-1):newtextWithoutgotin;
			
		}
		if (arg3 == null) 
		{
			childHolder = new ChildHolder();

			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.list_group_item_gotin, arg4, false);	
			childHolder.title = (TextView) arg3
					.findViewById(R.id.item_title_gotin);
			childHolder.listViewGotin = (ListView) arg3
					.findViewById(R.id.listviewgotin);		

			arg3.setTag(childHolder);
		} 
		else 
		{			
			childHolder = (ChildHolder) arg3.getTag();		
		}
		
		childHolder.listViewGotin.setAdapter(new ListViewGotinAdapter(mContext,
					 mGroupCollection.get(arg0).GroupItemCollection.get(arg1).Name));	
		ListViewGotinAdapter.setListViewHeightBasedOnChildren(	childHolder.listViewGotin);

		String parentName = mGroupCollection.get(arg0).Name;
		TextView tv = childHolder.title;
	
		ArrayList< Pair<Integer, Integer>> defaultLinks=null;
		ArrayList< Pair<Integer, Integer>> defaultCategoryLinks=null;
		if(text.contains(startofCategoryLink)||text.contains(endofCategoryLink))
		{
			defaultCategoryLinks=GetCategoryLinks(text);
									text=text.replace(startofCategoryLink, "").replace(endofCategoryLink, "");
									lines = text.split(java.util.regex.Pattern.quote("\n"));
		}
		if(text.contains(startofLink)||text.contains(endofLink))
		{
		defaultLinks=GetDefaultLinks(text);
									text=text.replace(startofLink, "").replace(endofLink, "");
									lines = text.split(java.util.regex.Pattern.quote("\n"));
		}		
	
		final Spannable span = Spannable.Factory.getInstance().newSpannable(
				text);
		String ziman = parentName;
		if (ziman.contains(";"))
			ziman = ziman.substring(0, ziman.indexOf(";"));
		
	
		if(defaultCategoryLinks!=null&& defaultCategoryLinks.size()>0)
		{
			Log.d("defaultCategoryLinks",defaultCategoryLinks.size()+"");
			for(int t=0;t<defaultCategoryLinks.size();t++)
			{
				Pair<Integer, Integer> pair=	defaultCategoryLinks.get(t);
				int start=pair.first;
				int end=pair.second;
				if(end>=text.length())
					end=text.length();
				if(start>0&&end<=text.length())
				{
				String toSpan=text.substring(start, end);
		
				SetSpan(span, toSpan, start, end, ziman, true);
				
				}
			}
		}
		if(defaultLinks!=null&& defaultLinks.size()>0)
		{
			for(int t=0;t<defaultLinks.size();t++)
			{
				Pair<Integer, Integer> pair=	defaultLinks.get(t);
				int start=pair.first;
				int end=pair.second;
				if(end>=text.length())
					end=text.length();
				if(start>0&&end<=text.length())
				{
				String toSpan=text.substring(start, end);
		
				SetSpan(span, toSpan, start, end);
				}
			}
		}
		if (wFerhengActivity != null) {
			if (arg1 == 0
					&& arg1 < mGroupCollection.get(arg0).GroupItemCollection
							.size() - 1) // if line startswith indexer do
											// linkifying
			{
				String aftersection = mGroupCollection.get(arg0).GroupItemCollection
						.get(arg1 + 1).Name;
				// wFerhengActivity.makeText(arg0+"  "+arg1+"  "+ aftersection);

				if ((aftersection.toLowerCase().startsWith("wate") || aftersection
						.startsWith("واتە")||aftersection.toLowerCase().startsWith("xwendin")||
						aftersection.toLowerCase().startsWith("gotin")||
						aftersection.toLowerCase().startsWith("bilêvkirin"))&&!lines[0].contains(":")) {
					int offset = 0;
				
					for (int xx = 0; xx < lines.length; xx++) {
						String line = lines[xx];

						SetSpan(span, line, offset, offset + line.length(),
								ziman, true);
						offset += line.length() + 1;
						// wFerhengActivity.makeText(line);
					}
				}
			}
		}

		if (lines.length == 0) {
			lines = new String[1];
			lines[0] = text;
		}
     
		MakeBold(span, lines[0], lines, text);

		   int offset=lines[0].length()+1;
		if (lines[0].toLowerCase().startsWith("wate")||lines[0].toLowerCase().startsWith("واتە")) 
		{
			for (int xx = 1; xx < lines.length; xx++) {
				String line = lines[xx];
				
				if(line.startsWith("£")&&line.endsWith("£"))
				{
					line=line.replace("£", "");
					SetSpan(span, line, offset, offset + line.length(),
							ziman, true);
				}
				
				Pattern regex = Pattern.compile("[0-9]+\\s*\\.\\s*");
				Matcher regexMatcher = regex.matcher(line);
			
				if (regexMatcher.find()) //if line startswith indexer do linkifying
				{					
					SpanLine(span, lines, text, line, offset);
				}
				else if(line.trim().toLowerCase().startsWith("binihêre"))
				{
					SpanLine(span, lines, text, line, offset);
				}
				else if(line.trim().toLowerCase().startsWith("#binihêre"))
				{
					SpanLine(span, lines, text, line, offset);
				}
				else
				{
					MakeItalic(span, lines, text, line, offset);
				}
				offset+=line.length()+1;
			}
			
			if(lines[0].toLowerCase().startsWith("واتە"))
			{
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(0,0,0,10);
				
				tv.setLayoutParams(params);
			}

		} 
		else if (lines[0].startsWith("Gotin")) { 

			tv.setText(span);
			return arg3;

		}	
		else if (lines[0].toLowerCase().startsWith("ji wêjeya klasîk"))
		{		
			for (int xx = 1; xx < lines.length-1; xx++) 
			{
		
				String line = lines[xx];
				MakeItalic(span, lines, text, line, offset);
				offset+=line.length()+1;
			}
	
		} 		
		else if (lines[0].startsWith("Ji")) {
			for (int xx = 1; xx < lines.length; xx++) {
				String line = lines[xx];
				SpanLine(span, lines, text, line, offset);
				offset+=line.length()+1;

			}

		}
		else if (lines[0].toLowerCase().replace(" ", "").startsWith("bizaravay")||
				lines[0].toLowerCase().replace(" ", "").startsWith("bikurmanc")) {
			for (int xx = 1; xx < lines.length; xx++) {
				String line = lines[xx];
				SpanLine(span, lines, text, line, offset);
				offset+=line.length()+1;

			}

		}
		else if (lines[0].toLowerCase().startsWith("werger")) {

			for (int xx = 1; xx < lines.length; xx++) {

				String line = lines[xx];

				if (line.toLowerCase().contains("elmanî")
						|| line.toLowerCase().contains("înglîzî")
							|| line.toLowerCase().contains("erebî")
						|| line.toLowerCase().contains("tirkî")
						|| line.toLowerCase().contains("farisî")
						|| line.toLowerCase().contains("almanî"))
				{
					SpanLine(span, lines, text, line, offset);
				}
				offset+=line.length()+1;
			}

		} else if (lines[0].startsWith("Bide ber")
				|| lines[0].startsWith("Bide Ber")
				|| lines[0].startsWith("Binihêre")
				|| lines[0].startsWith("Dijwate")
				|| lines[0].startsWith("Hevwate")
				|| lines[0].startsWith("Têkildar")
				|| lines[0].startsWith("Bibîne")
				|| lines[0].startsWith("Bikaranîn")
				|| lines[0].startsWith("Nêzîk") || lines[0].startsWith("Jê")
				|| lines[0].startsWith("Baştir")) {
			String[] str = new String[1];

			//if (text.contains("\n")) 
			//{
				str = lines;//text.split(java.util.regex.Pattern.quote("\n"));
				if (str.length > 1) 
				{
					offset=str[0].length()+1;
					for (int x = 1; x < str.length; x++) {

						final String strsub = str[x].trim();
						if ((strsub.toLowerCase().contains("rengdêr")
								|| strsub.toLowerCase().contains("navdêr") || strsub
								.toLowerCase().contains("lêker"))
								&& lines[0].startsWith("Bikaranîn")) 
						{
							offset+=str[x].length()+1;
							continue;
						}

						SpanLine(span, lines, text, strsub, offset);
						offset+=str[x].length()+1;

					}
				//} 
				//else 
				//{

				//}
			} else 
			{
				str[0] = text;
				for (int x = 0; x < str.length; x++) {
					final String strsub = str[x];
					int startAt = 0;
					if (text.startsWith("Bide ber"))
						startAt = strsub.indexOf("Bide ber") + 1;
					else
						startAt = strsub.indexOf(" ") + 1;
					SetSpan(span, strsub.substring(startAt), startAt,
							strsub.length());
				}
			}

		} else if (lines[lines.length - 1].startsWith("Binihêre")
				|| lines[lines.length - 1].startsWith("binihêre")) {

			
			String line = lines[lines.length - 1];
			int astart = line.indexOf("Binihêre") + 8;
			int linestart = text.indexOf(line);
			String strsubspan = line.substring(astart).trim();
			if(strsubspan.trim().startsWith(":"))
			{
				strsubspan=strsubspan.trim().substring(1);					
				astart++;				
			}
		
			if (IsSpannable(strsubspan)) {

				SetSpan(span,
						strsubspan,
						linestart + astart,
						linestart + line.indexOf(strsubspan)
								+ strsubspan.length());
			}

		} else if ((lines.length > 1 && lines[lines.length - 2]
				.startsWith("Binihêre"))) {

			String line = lines[lines.length - 1];

			int astart = 0;
			int linestart = text.indexOf(line);
			String strsubspan = line.substring(astart).trim();
		
			if (!strsubspan.trim().equalsIgnoreCase("")
					&& IsSpannable(strsubspan)) {

				SetSpan(span,
						strsubspan,
						linestart + astart,
						linestart + line.indexOf(strsubspan)
								+ strsubspan.length());

			}

		} else if (lines[lines.length - 1].toLowerCase().startsWith(
				"pirrjimara")
				|| lines[lines.length - 1].startsWith("Baştir")) {

			String line = lines[lines.length - 1];
			int astart = line.indexOf(" ");
			int linestart = text.indexOf(line);
			if (astart < 0)
				astart = 0;

			String strsubspan = line.substring(astart).trim();

			if (IsSpannable(strsubspan)) {

				SetSpan(span,
						strsubspan,
						linestart + astart,
						linestart + line.indexOf(strsubspan)
								+ strsubspan.length());
			}

		} else if (parentName.contains("Binihêre")
				|| parentName.contains("Binere")
				|| parentName.contains("بنهێرە")) {

			SetSpan(span, text, 0, text.length());

		}
	
		if (aboutActivity != null) {

			Linkify.addLinks(span, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
			ArrayList<String> strings = new ArrayList<String>();
			strings.add("Veguherîn");
			strings.add("Koloniya Tawanê");
			strings.add("Franz Kafka");
			strings.add("Sefernameya Hezar û Yek Fersengî");
			strings.add("Weşanên Lîs");
			strings.add("Fatîh Aydin");
			strings.add("Wîkîferheng");
			strings.add("Nûçexane");
			strings.add("WQFerheng");
			strings.add("wergêrî");
			strings.add("nivîskari");
			for (int i = 0; i < strings.size(); i++) {
				String item = strings.get(i);
				ArrayList<Integer> indexes = getIndexes(span.toString(), item);
				if (indexes.size() > 0) {
					int length = item.length();
					for (int j = 0; j < indexes.size(); j++) {
						int index = indexes.get(j);
						if (index > 0) {
							span.setSpan(new StyleSpan(
									android.graphics.Typeface.BOLD_ITALIC),
									index, index + length,
									Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//							span.setSpan(new
//											ForegroundColorSpan(Color.parseColor(linkcolor)),
//									index,index+length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		}
		tv.setText(span);

		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setOnTouchListener(onSwipeListener);

		return arg3;

	}

	public int getGroupViewHeight(int group) {
		try
		{
		ExpandableListAdapter listAdapter = (ExpandableListAdapter) mExpandableListView
				.getExpandableListAdapter();
		int totalHeight = 0;
		if(listAdapter==null)
			return -1;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(
				mExpandableListView.getWidth(), View.MeasureSpec.EXACTLY);

		if(group>=listAdapter.getGroupCount())
			return -1;
		View groupItem = listAdapter.getGroupView(group, false, null,
				mExpandableListView);
		if(groupItem==null)
			return -1;
		groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

		totalHeight += groupItem.getMeasuredHeight();

		for (int j = 0; j < listAdapter.getChildrenCount(group); j++) {
			View listItem = listAdapter.getChildView(group, j, false, null,
					mExpandableListView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

			totalHeight += listItem.getMeasuredHeight();

		}
		return totalHeight;
		}
		catch(Exception e)
		{
			return -1;}
		finally
		{
			
		}
	}
	private void MakeItalic(Spannable span, String[] lines, String text,
			String line, int offset) {
		
		int start=offset;
		int end=offset+line.length();
		if(end>text.length())
			end=text.length();
		
		span.setSpan( new StyleSpan(android.graphics.Typeface.ITALIC),start,
				end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//		span.setSpan(new
//						ForegroundColorSpan(Color.parseColor(linkcoloritalic)),
//				start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//span.setSpan(new BulletSpan(15), start, end, 0);
	}
	private ArrayList< Pair<Integer, Integer>> GetCategoryLinks(String text) {
		ArrayList< Pair<Integer, Integer>> map=new ArrayList< Pair<Integer, Integer>>();
		if(text.contains(startofCategoryLink)||text.contains(endofCategoryLink))
		{
			Pattern regexstartofLink =Pattern.compile( Pattern.quote(startofCategoryLink));
			Matcher regexMatcherstartofLink = regexstartofLink.matcher(text);
			
			Pattern regexendofLink = Pattern.compile(Pattern.quote( endofCategoryLink));
			Matcher regexMatcherendofLink = regexendofLink.matcher(text);
			while(regexMatcherstartofLink.find())
				{
				int end=-1;
				int start=-1;
				start=regexMatcherstartofLink.start();
				text=regexMatcherstartofLink.replaceFirst("");
				
				regexMatcherendofLink = regexendofLink.matcher(text);
				if(regexMatcherendofLink.find())
				{
					end=regexMatcherendofLink.start();
					text=regexMatcherendofLink.replaceFirst("");
					regexMatcherstartofLink = regexstartofLink.matcher(text);
				}
				if(start!=-1&&end!=-1)
				{
				Pair<Integer, Integer> keyValue = new Pair(start, end);
				map.add(keyValue);
				}
				}			
		}
		return map;
	}
	private ArrayList< Pair<Integer, Integer>> GetDefaultLinks(String text) {
		ArrayList< Pair<Integer, Integer>> map=new ArrayList< Pair<Integer, Integer>>();
		if(text.contains(startofLink)||text.contains(endofLink))
		{
			Pattern regexstartofLink =Pattern.compile( Pattern.quote(startofLink));
			Matcher regexMatcherstartofLink = regexstartofLink.matcher(text);
			
			Pattern regexendofLink = Pattern.compile(Pattern.quote( endofLink));
			Matcher regexMatcherendofLink = regexendofLink.matcher(text);
			while(regexMatcherstartofLink.find())
				{
				int end=-1;
				int start=-1;
				start=regexMatcherstartofLink.start();
				text=regexMatcherstartofLink.replaceFirst("");
				
				regexMatcherendofLink = regexendofLink.matcher(text);
				if(regexMatcherendofLink.find())
				{
					end=regexMatcherendofLink.start();
					text=regexMatcherendofLink.replaceFirst("");
					regexMatcherstartofLink = regexstartofLink.matcher(text);
				}
				if(start!=-1&&end!=-1)
				{
				Pair<Integer, Integer> keyValue = new Pair(start, end);
				map.add(keyValue);
				}
				}			
		}
		return map;
	}

	private ArrayList<Integer> getIndexes(String str, String search) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String strlower = str.toLowerCase();
		int index = strlower.indexOf(search.toLowerCase());
		if (index > 0)
			list.add(index);
		while (index >= 0) {
			index = strlower.indexOf(search.toLowerCase(), index + 1);
			if (!list.contains(index))
				list.add(index);
		}
		return list;
	}

	private void MakeBold(Spannable span, String line, String[] lines,
			String text) {
		try {
			if (line.endsWith(":")) {
				span.setSpan(new UnderlineSpan(), 0, line.length(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				span.setSpan(new StyleSpan(
						android.graphics.Typeface.BOLD_ITALIC), 0, line
						.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//				span.setSpan(new
//								ForegroundColorSpan(Color.parseColor(linkcolor)),
//						0,line.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} 
			
			if (line.startsWith("Werger") || line.startsWith("Wergerr")
					|| line.toLowerCase().startsWith("bi zaravayên kurd")
					|| line.toLowerCase().startsWith("bi alfab")
					|| line.startsWith("Ji")
					|| line.startsWith("Bikaranîn")) {
				
				for (int xx = 1; xx < lines.length; xx++) {
					String lineToBold = lines[xx];

					int start = text.indexOf(lineToBold);

					int end = start + lineToBold.length();
					if (start < 0)
						continue;

					if (end < start)
						continue;
					if (lineToBold.contains(":")) {
						end = start + lineToBold.indexOf(":");

						if (end <= 1)
							continue;
						span.setSpan(new StyleSpan(
								android.graphics.Typeface.BOLD), start, end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//						span.setSpan(new
//										ForegroundColorSpan(Color.parseColor(linkcolor)),
//								start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}

				}

			}
		} finally {

		}

	}

	private void SpanLine(Spannable span, String[] lines, String text,
			String line, int offset) {

		try {			
			int startIndexofLine=0;
			int offSetInline = 0;
			String[] str = null;
			if (lines[0].startsWith("Ji")) {
				str = line.split("\\+|:");
				
				if(line.contains("+")&&!line.contains(":"))
					startIndexofLine=0;
				else					
				{
					startIndexofLine=1;
					offSetInline+=str[0].length()+1;
				}
				
			}
			else if(lines[0].startsWith("واتە"))
			{
				str = line.split(",|:|،|،");
				Log.d("str", str.length+ " heb");
			}
			else
			{
				str = line.split(",|:|;");
		//	str = line.split(",|:|\\(|\\)");
			}
			if (str == null)
				return;
			Boolean IswergerSection = false;
	
			if (lines[0].toLowerCase().startsWith("werger")) {
				IswergerSection = true;
				startIndexofLine=1;
				offSetInline+=str[0].length()+1;
			}
			else if(lines[0].toLowerCase().replace(" ", "").startsWith("bizarav"))
			{
				startIndexofLine=1;
				offSetInline+=str[0].length()+1;
			}

		//	wFerhengActivity.makeText(line);
			
			int offSetLine = offset;//text.indexOf(line); start of line in whole text
			for (int x = startIndexofLine; x < str.length; x++) {
				String strsub = str[x].trim();
				int lengthtoSPan = str[x].length();

				if (strsub.trim().equalsIgnoreCase("")
						|| (IswergerSection && x == 0)) 
				{
					offSetInline += str[x].length() + 1;
					continue;
				}			
				
				String strsubspan = str[x];
				int offSetinDef = 0;
				int offSetSpace=0;
				Pattern regex = Pattern.compile("[0-9]+\\s*\\.\\s*");
				Matcher regexMatcher = regex.matcher(strsubspan);
				
				if (regexMatcher.find()) 
				{
					
					offSetinDef = regexMatcher.group().length();
					strsubspan = strsubspan.substring(offSetinDef);
					lengthtoSPan=strsubspan.length();
				
				}
				
				if(lines[0].toLowerCase().startsWith("wate")&&strsubspan.toLowerCase().trim().startsWith("binihêre")) //linkfy binihêre underneath wate
				{
					offSetinDef=offSetinDef+8;
					strsubspan = strsubspan.substring(8);
					lengthtoSPan=strsubspan.length();
				}
				
				if(strsubspan.contains("(") &&!(strsubspan.replace(" ", "").contains("(yek")||strsubspan.replace(" ", "").contains("(tiştek"))&&!strsubspan.trim().startsWith("("))
				{					
					strsubspan=strsubspan.substring(0, strsubspan.lastIndexOf("("));
					lengthtoSPan=strsubspan.length();					
				}
				if(strsubspan.contains(":"))
				{
					strsubspan=strsubspan.substring(0, strsubspan.lastIndexOf(":"));
					lengthtoSPan=strsubspan.length();
				}
				
				if(strsubspan.trim().startsWith("(")&&strsubspan.contains(")"))
				{
					
					offSetinDef+=strsubspan.indexOf(")")+1;
						strsubspan = strsubspan.substring(strsubspan.indexOf(")")+1);
						lengthtoSPan=strsubspan.length();					
				}	
				if(x>0&&str[x-1].trim().toLowerCase().contains("binihêre"))
				{
					//wFerhengActivity.makeText(x+";"+strsubspan+":"+strsub); 
					SetSpan(span, strsubspan, offSetLine + offSetInline + offSetinDef+offSetSpace,
							offSetLine + offSetInline +offSetinDef+offSetSpace+ lengthtoSPan);
				}
				
				strsubspan=strsubspan.replace(".",	"");	
				if(strsubspan.startsWith(" "))
				{
					while(strsubspan.startsWith(" "))
					{
						strsubspan=	strsubspan.substring(1);
						offSetSpace++;
					}
					lengthtoSPan=lengthtoSPan-offSetSpace;
				}
				
				
				if (IsSpannable(strsubspan)) {
					SetSpan(span, strsubspan, offSetLine + offSetInline + offSetinDef+offSetSpace,
							offSetLine + offSetInline +offSetinDef+offSetSpace+ lengthtoSPan);
				} else if (IsSpannableNot(strsubspan)) {
					int startspace = 3;
					if (strsubspan.contains(" "))
					{
						startspace = strsubspan.indexOf(" ") + 1;
					
					}
					if (strsubspan.length() > startspace)
					{
						strsubspan = strsubspan.substring(startspace);
						
					}
					offSetinDef+=startspace;
					lengthtoSPan=strsubspan.length();	
					SetSpan(span, strsubspan, offSetLine + offSetInline
							+  offSetinDef+offSetSpace, offSetLine + offSetInline+offSetinDef+offSetSpace
							+ lengthtoSPan);
				} 				
				else if (IswergerSection) 
				{
					SetSpan(span, strsubspan, offSetLine + offSetInline + offSetinDef+offSetSpace,
							offSetLine + offSetInline +offSetSpace+ lengthtoSPan);
				} 

				
				offSetInline += str[x].length() + 1;
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Boolean IsSpannable(String strsubspan) {
		String[] strsubx = new String[1];
		strsubspan = strsubspan.trim();
		strsubx = strsubspan.split(java.util.regex.Pattern.quote(" "));
		Boolean isspannable = false;
		if (strsubx.length > 1 && strsubx.length <= 3) 
		{
			String tocontrol = strsubx[strsubx.length - 1];
			String tocontrol_Start = strsubx[0];
			if (tocontrol.equalsIgnoreCase("kirin")
					|| tocontrol.equalsIgnoreCase("bûn")
						|| tocontrol.equalsIgnoreCase("borîn")
					|| tocontrol.equalsIgnoreCase("hebûn")
					|| tocontrol.equalsIgnoreCase("xwestin")
					|| tocontrol.equalsIgnoreCase("xistin")
					|| tocontrol.equalsIgnoreCase("dan")
					|| tocontrol.equalsIgnoreCase("çûn")
					|| tocontrol.equalsIgnoreCase("kişandin")
					|| tocontrol.equalsIgnoreCase("birin")
					|| tocontrol.equalsIgnoreCase("man")
					|| tocontrol.equalsIgnoreCase("hîştin")
					|| tocontrol.equalsIgnoreCase("ketin")
					|| tocontrol.equalsIgnoreCase("danîn")
					|| tocontrol.equalsIgnoreCase("girtin")
					|| tocontrol.equalsIgnoreCase("vedan")
					|| tocontrol.equalsIgnoreCase("wergirtin")
					|| tocontrol.equalsIgnoreCase("berdan")
					|| tocontrol.equalsIgnoreCase("vegirtin")
					|| tocontrol.equalsIgnoreCase("anîn")
					|| tocontrol.equalsIgnoreCase("nan")
					|| tocontrol.equalsIgnoreCase("pirsîn")
					|| tocontrol.equalsIgnoreCase("derxistin")
					|| tocontrol.equalsIgnoreCase("vedan")
					|| tocontrol.equalsIgnoreCase("hatin")
					|| tocontrol.equalsIgnoreCase("vegerandin")
					|| tocontrol.equalsIgnoreCase("dîtin")
					|| tocontrol.equalsIgnoreCase("spartin")
					|| tocontrol.equalsIgnoreCase("êşîn")
					|| tocontrol.equalsIgnoreCase("peyivîn")
					|| tocontrol.equalsIgnoreCase("axaftin")
										|| tocontrol.equalsIgnoreCase("koşîn")
					|| tocontrol.equalsIgnoreCase("sekinîn")
					|| tocontrol.equalsIgnoreCase("vexwarin")
					|| tocontrol.equalsIgnoreCase("derketin")
					|| tocontrol.equalsIgnoreCase("xwarin")
					|| tocontrol.equalsIgnoreCase("çûn")
					|| tocontrol.equalsIgnoreCase("danîn")
					|| tocontrol.equalsIgnoreCase("danan")
					|| tocontrol.equalsIgnoreCase("çandin")
					|| tocontrol.equalsIgnoreCase("şewitîn")
					|| tocontrol.equalsIgnoreCase("birin")
					|| tocontrol.equalsIgnoreCase("radan")
					|| tocontrol.equalsIgnoreCase("dadan")
					|| tocontrol.equalsIgnoreCase("çandin")
					|| tocontrol.equalsIgnoreCase("gihandin")
					|| tocontrol.equalsIgnoreCase("ragihandin")
					|| tocontrol.equalsIgnoreCase("kişandin")
					|| tocontrol.equalsIgnoreCase("çêkirin")
					|| tocontrol.equalsIgnoreCase("hilhatin")
						|| tocontrol.equalsIgnoreCase("firandin")
							|| tocontrol.equalsIgnoreCase("firrandin")
					|| tocontrol.equalsIgnoreCase("lêdan")
					|| tocontrol.equalsIgnoreCase("lêxistin")
					|| tocontrol.equalsIgnoreCase("gerrîn")
					|| tocontrol.equalsIgnoreCase("ve")
					|| tocontrol.equalsIgnoreCase("re")
					|| tocontrol.equalsIgnoreCase("va")
					|| tocontrol.equalsIgnoreCase("ra")
					|| tocontrol.equalsIgnoreCase("de")
					|| tocontrol.equalsIgnoreCase("da")) 
			{
				isspannable = true;
			}
			else if(tocontrol_Start.equalsIgnoreCase("dan")||tocontrol_Start.equalsIgnoreCase("hatin")
					||tocontrol_Start.equalsIgnoreCase("bûn")||tocontrol_Start.equalsIgnoreCase("kirin")||tocontrol_Start.equalsIgnoreCase("bi"))
			{
				isspannable = true;
			}

		} else if (strsubx.length == 1) 
		{
		
			isspannable = true;
		}
		return isspannable;
	}

	private Boolean IsSpannableNot(String strsubspan) {
		String[] strsubx = new String[1];
		strsubspan = strsubspan.trim();
		strsubx = strsubspan.split(java.util.regex.Pattern.quote(" "));
		Boolean isspannable = false;
		if (strsubx.length > 1 && strsubx.length <= 3) {
			String tocontrol = strsubx[strsubx.length - 1];

			if (strsubx.length == 2) {
				String tocontrol2 = strsubx[0];
				if (tocontrol2.equalsIgnoreCase("ne")
						|| tocontrol2.equalsIgnoreCase("to")
						|| tocontrol2.toLowerCase()
								.equalsIgnoreCase("binihêre"))
					isspannable = true;
			}

		}

		return isspannable;
	}
	

	private void SetSpan(Spannable span, final String strsub, int start, int end, final String lang, Boolean isCat) {
		try {
			if (end < 0)
				return;
			if (start < 0)
				return;
			if (end <= start)
				return;
			MyClickableSpan myclickableSpan=new MyClickableSpan() {
				@Override
				public void onClick(View v) {
					handleClick=false;
//					if(definitionActivity!=null)
//						definitionActivity.makeText("aaa"+WQFerhengActivity.ShowCategoryDialog);
					Log.d("clc", "cj,"+WQFerhengActivity.ShowCategoryDialog);
					
					try {
						if(WQFerhengActivity.ShowCategoryDialog)
						{
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

						builder.setTitle(mContext.getString(R.string.herekategoriye));
						builder.setMessage(mContext.getString(R.string.herekategoriyemesaj, lang+":"+strsub));

						builder.setPositiveButton("Belê",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {

										dialog.dismiss();
										String word = strsub.toString().replace(".", "")
												.replace(",", "").replace(":", "").trim();
//										Log.d("clc", "cj,");
//										if(definitionActivity!=null)
//											definitionActivity.makeText("aaa"+word);
										Intent intent = new Intent(mContext, ListViewCursorLoaderActivity.class);

										Bundle b = new Bundle();

										b.putString("type", word);

										b.putString("ziman", lang);

										intent.putExtras(b);

										mContext.startActivity(intent);
										
									}
								});
						builder.setNegativeButton("Na",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						
						builder.setNeutralButton("Êdî Nepirse",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										SharedPreferences sharedPref =mContext. getSharedPreferences(WQFerhengActivity.PRIVATE_PREF,
												Context.MODE_PRIVATE);
										Editor editor = sharedPref.edit();
										WQFerhengActivity.ShowCategoryDialog=false;
										editor.putBoolean("ShowCategoryDialog", false);
										editor.commit();
										
										dialog.dismiss();
									}
								});

						AlertDialog alert = builder.create();
						alert.show();
						}
						else
							
						{
							String word = strsub.toString().replace(".", "")
									.replace(",", "").replace(":", "").trim();
							
							Intent intent = new Intent(mContext, ListViewCursorLoaderActivity.class);

							Bundle b = new Bundle();

							b.putString("type", word);

							b.putString("ziman", lang);

							intent.putExtras(b);

							mContext.startActivity(intent);
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//dialogshowed = true;
					}

				}
			};
			myclickableSpan.Language=lang;
			myclickableSpan.IsCategory=isCat;
			span.setSpan(myclickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start,
					end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			//if (!isCat)
			span.setSpan(new
							ForegroundColorSpan(Color.parseColor(WQFerhengActivity. linkColor)),
					start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("xetag:");
			e.printStackTrace();
		}
	}

	private void SetSpan(Spannable span, final String strsub, int start, int end) {
		try {
			if (end < 0)
				return;
			if (start < 0)
				return;
			if (end <= start)
				return;
			MyClickableSpan myclickableSpan=new MyClickableSpan() {
				@Override
				public void onClick(View v) {
					//Log.d("MyClickableSpan clicked", "click");
					handleClick=false;

					String word = strsub.toString().replace(".", "")
							.replace(":", "").replace("!", "").replace("?", "").trim();
					if (wFerhengActivity != null) 
					{
						
						Words resulted = wFerhengActivity
								.GetSingleExactWord(word);
						if (resulted!=null)
						{
							//Log.d("find", "find");
							wFerhengActivity.AddSearchItem(word, resulted.id, "Exact", resulted);
							wFerhengActivity.ReOrderHistory();
							textView.setText(word);
							
						}
						
						else {
							wFerhengActivity.makeText(wFerhengActivity
									.getString(R.string.resultsnotfound, word));
							// wFerhengActivity.ShowButton("Back");
						}
					} else if (definitionActivity != null) 
					{

					Words wordd=	definitionActivity.Research(word);
					if(wordd!=null)
					{
						definitionActivity.	AddSearchItem(word, wordd.id, "Exact", wordd);
						definitionActivity.	ReOrderHistory();
					}
					}
				}
			};
			myclickableSpan.Language="";
			myclickableSpan.IsCategory=false;
			span.setSpan(myclickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start,
					end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			span.setSpan(new
							ForegroundColorSpan(Color.parseColor(WQFerhengActivity. linkColor)),
			 start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("xetag:");
			e.printStackTrace();
		}
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		if (mGroupCollection.size() > 0)
			return mGroupCollection.get(arg0).GroupItemCollection.size();
		else
			return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_group,
					null);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
			groupHolder.title = (TextView) arg2.findViewById(R.id.group_title);
			groupHolder.titleTranslate = (TextView) arg2.findViewById(R.id.group_titletr);
			groupHolder.volume= (ImageView) arg2.findViewById(R.id.tag_volume);
			arg2.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) arg2.getTag();
		}
		if (groupStatus[arg0] == 0) 
		{
			groupHolder.img.setImageResource(R.drawable.expandmore);
		} 
		else 
		{
			groupHolder.img.setImageResource(R.drawable.expandless);
		}
		GroupEntity myEntity= mGroupCollection.get(arg0);
		String definition=myEntity.Body;		
		if(!(definition.toLowerCase().contains(".ogg")||definition.toLowerCase().contains(".oga")))		   
       	{
			groupHolder.volume.setVisibility(View.GONE);
       	}
       	else
       	{
       		groupHolder.volume.setVisibility(View.VISIBLE);       		
       	}
		groupHolder.title.setText(myEntity.Name);
		if(WQFerhengActivity.ShowLanguageTranslation) {
		   String translation=GetTranslation(myEntity.Name.toLowerCase());
			//Toast.makeText(mContext,myEntity.Name.toLowerCase()+"  :"+ translation+";", Toast.LENGTH_LONG).show();
			if(!translation.replace(" ","").trim().equalsIgnoreCase(""));
			{
				if(translation.length()>0) {
					groupHolder.titleTranslate.setText("(" + translation + ")");
					groupHolder.titleTranslate.setVisibility(View.VISIBLE);
				}
				else {
					groupHolder.titleTranslate.setText("");
					groupHolder.titleTranslate.setVisibility(View.GONE);
				}

			}
		}

		return arg2;
	}

	private String GetTranslation(String name)
	{
		String translation="";
		if(WQFerhengActivity.mapLanguages.containsKey(name)&&WQFerhengActivity.LocalisationIndex>-1) {
			List listtranslation=WQFerhengActivity.mapLanguages.get(name);
			translation=listtranslation.get(WQFerhengActivity.LocalisationIndex).toString();
		}

		return  translation;
	}


	class GroupHolder {
		ImageView img;
		ImageView volume;
		TextView title;
		TextView titleTranslate;
	}

	class ChildHolder 
	{
		TextView title;
		ListView listViewGotin;	
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}

class MyClickableSpan extends ClickableSpan
{

	public boolean IsCategory;
	public String Language;

	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub
		
	}
}


