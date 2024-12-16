package com.wqsozluk;

import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WQDictionaryWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "com.wqsozluk.ACTION_WIDGET_RECEIVER";
	public static final String OPEN_URL_ACTION = "com.wqsozluk.OPEN_URL_ACTION";
	public static final String Refresh = "com.wqsozluk.Refresh";
	public static final String LAUNCH_WQDictionary = "com.wqsozluk.LAUNCH_WQSozluk";
	public static final String Web_Link = "com.wqsozluk.Web_Link";
	public static final String Zareva = "com.wqsozluk.Zareva";
	public static String UPDATE_LIST = "UPDATE_LIST";

	public static String ziman = "";
	public static String cure = "";
	static String word;
	String definition;

	@Override
	public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.print("0");
		Log.println(Log.ERROR,"d","aa");
		for (int i = 0; i < appWidgetIds.length; i++) 
		{
			GetWord(ctxt, appWidgetIds[i]);
			updateAppWidget(ctxt, appWidgetManager, appWidgetIds[i]);
						
		}
		super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		//Log.println(Log.ERROR,"d","aa");
		System.out.print(intent.getAction());
		if (intent.getAction().equalsIgnoreCase(UPDATE_LIST)) {
			updateWidget(context);
			System.out.println("0");
		} 
		
		else if (ACTION_WIDGET_RECEIVER.equals(intent.getAction())) {
			
			System.out.print("1");
			Bundle extras = intent.getExtras();
		//	String refresh = extras.getString("refresh");
extras.putString("configuring", "configuring");
			Intent onClickDone = new Intent(context.getApplicationContext(),
					WQDictionaryWidgetConfig.class);
			onClickDone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);

			onClickDone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			onClickDone.putExtras(extras);

			context.startActivity(onClickDone);

		}
		else if (intent.getAction().equalsIgnoreCase(
				"android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")||
				intent.getAction().equalsIgnoreCase(
						"android.appwidget.action.APPWIDGET_UPDATE")
						||Refresh.equals(intent.getAction())) 
		{
			System.out.print("2");
			Bundle extras = intent.getExtras();
		//	String refresh = extras.getString("refresh");	
			int id =WQDictionaryWidgetConfig.mAppWidgetId;
			if(extras!=null)
			{
				 id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
			}
			GetWord(context, id);
			
		} 
 
		else if (LAUNCH_WQDictionary.equals(intent.getAction())) {

			Bundle extras = intent.getExtras();
			 extras.putString("widget_word", word);
			 Intent onClickDone = new Intent(context.getApplicationContext(),
			 WQDictionaryActivity.class);
			 onClickDone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
			 | Intent.FLAG_ACTIVITY_CLEAR_TOP
			 | Intent.FLAG_ACTIVITY_NEW_TASK);
			
			 onClickDone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			 onClickDone.putExtras(extras);
			
			 context.startActivity(onClickDone);
		} 
else
{System.out.print("10");}
	}

	@Override
    public void onEnabled(Context context) {
		Log.println(Log.ERROR,"d","aa");
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager
				.getAppWidgetIds(new ComponentName(context,
						WQDictionaryWidget.class));
		System.out.print("enabling");
		for (int i = 0; i < appWidgetIds.length; i++) 
		{
			GetWord(context, appWidgetIds[i]);
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
						
		}
    }
	

	private void updateWidget(Context context) {
		System.out.print("0");
		Log.println(Log.ERROR,"d","aa");
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager
				.getAppWidgetIds(new ComponentName(context,
						WQDictionaryWidget.class));
	
	}


	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int mAppWidgetId) {
		System.out.print("0");
		Log.println(Log.ERROR,"d","aa");
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		cure = WQDictionaryWidgetConfig.loadUrlPref(context, mAppWidgetId,
				"cûreya peyvê");
		ziman = WQDictionaryWidgetConfig.loadUrlPref(context, mAppWidgetId,
				"ziman");

		Intent intentx = new Intent(context, WQDictionaryWidget.class);
		intentx.setAction(ACTION_WIDGET_RECEIVER);
		Bundle extras = new Bundle();
		extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

		intentx.putExtras(extras);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
					intentx, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

		rv.setOnClickPendingIntent(R.id.buttonConfig, pendingIntent);
		}
		else
		{
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
						intentx, PendingIntent.FLAG_UPDATE_CURRENT);

				rv.setOnClickPendingIntent(R.id.buttonConfig, pendingIntent);
			}


		Intent intentref=  new Intent(context, WQDictionaryWidget.class);
		//intentref.setAction(ACTION_WIDGET_RECEIVER);
		 intentref.setAction(Refresh);
		Bundle extrasrefr = new Bundle();
		extrasrefr.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		extrasrefr.putString("refresh", "refresh");
		intentref.putExtras(extrasrefr);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			PendingIntent pendingIntentrefresh = PendingIntent.getBroadcast(
					context, 0, intentref, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
			rv.setOnClickPendingIntent(R.id.buttonrefresh, pendingIntentrefresh);
		}
		else
		{
			PendingIntent pendingIntentrefresh = PendingIntent.getBroadcast(
					context, 0, intentref, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.buttonrefresh, pendingIntentrefresh);
		}

		//GetRssItems(context, mAppWidgetId);

		Intent intentGoToMalper = new Intent(context, WQDictionaryWidget.class);
		intentGoToMalper.setAction(WQDictionaryWidget.LAUNCH_WQDictionary);
		Bundle extrasGoToMalper = new Bundle();
		extrasGoToMalper.putString("widget_word", word);

		extrasGoToMalper.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

		intentGoToMalper.putExtras(extrasGoToMalper);
	//	intentGoToMalper.setAction(ACTION_WIDGET_RECEIVER);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
		PendingIntent pendingIntentGoToMalper  = PendingIntent.getBroadcast(context, 0,
				intentGoToMalper, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
		rv.setOnClickPendingIntent(R.id.buttonHome, pendingIntentGoToMalper);
		rv.setOnClickPendingIntent(R.id.textWord, pendingIntentGoToMalper);

		appWidgetManager.updateAppWidget(mAppWidgetId, rv);

	}
	else
	{
		PendingIntent pendingIntentGoToMalper  = PendingIntent.getBroadcast(context, 0,
				intentGoToMalper, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.buttonHome, pendingIntentGoToMalper);
		rv.setOnClickPendingIntent(R.id.textWord, pendingIntentGoToMalper);

		appWidgetManager.updateAppWidget(mAppWidgetId, rv);
	}
	}

	public static void GetWord(Context ctxt, int appWidgetId) {
		Log.println(Log.ERROR,"d","aa");

		RemoteViews rv = new RemoteViews(ctxt.getPackageName(), R.layout.widget);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctxt);
		rv.setViewVisibility(R.id.progressBarLayout, View.VISIBLE);

		appWidgetManager.updateAppWidget(appWidgetId, rv);

		WQDictionaryDB.columntoSearch = WQDictionaryDB.KEY_DEFINITION;
	

		WQDictionaryQueryProvider queryProvider = new WQDictionaryQueryProvider(ctxt);
		WQDictionaryWidget.cure = WQDictionaryWidgetConfig.loadUrlPref(ctxt, appWidgetId,
				"cûreya peyvê");
		WQDictionaryWidget.ziman = WQDictionaryWidgetConfig.loadUrlPref(ctxt, appWidgetId,
				"ziman");
		
		WQDictionaryActivity.zimanquery = WQDictionaryWidget.ziman;
		WQDictionaryActivity.typeToSearch = WQDictionaryWidget.cure;
		
	
		if(WQDictionaryWidget.ziman.equalsIgnoreCase("")||(WQDictionaryWidgetConfig.IsCOnfiguring))
		{
			//return;
		}
		String selection = WQDictionaryDB.KEY_DEFINITION + " match ? ";
		
		Uri uri = WQDictionaryDBProvider.CONTENT_URI;
		String query ="";// "'" + WQDictionaryActivity.zimanquery + "%*'";
		String type= WQDictionaryWidget.cure;
		if (!type.toLowerCase().contains("hemû"))
		{
			
			query+= WQDictionaryActivity.zimanquery+":"+
					type.replace("*", "")+";";
			
	
		}
		else
		{
			query+= WQDictionaryActivity.zimanquery+":";
		}
			Log.d("query_raw", query);
		query="'"+query.replace("ı", "i")+"'";		
	
		
		System.out.println("selection="+ selection+ " query="+query);
		Cursor cursor = queryProvider.GetCursor(selection, query);

		if (cursor != null && cursor.getCount() > 0) 
		{	
			System.out.println("Cursor="+ cursor.getCount());
			String definition = "";
			
			int xy=0;
			while (!definition.toLowerCase().contains("wate")) {
				Random r = new Random();
				int i1 = r.nextInt(cursor.getCount());
				
				cursor.moveToPosition(i1);
			
				
				word= WQDictionaryQueryProvider. GetValue(cursor, WQDictionaryDB.KEY_WORD);
				String id= WQDictionaryQueryProvider. GetValue(cursor, WQDictionaryDB.KEY_ID);
				if(word==null||word.equals(""))
					word= WQDictionaryQueryProvider. GetValue(cursor, WQDictionaryDB.KEY_WORD_N);
				Words ww=	queryProvider.GetSingleWord(id);
				definition=WQDictionaryActivity.Decode(ww.getwate(),word, word);
				definition=definition.replace("[","").replace("]", "");
				definition=definition.replace("£","").replace("£", "");
			//	definition=queryProvider. GetValue(cursor, WQDictionaryDB.KEY_DEFINITION);
			xy++;
			
				if(xy>5)
					break;
			}
			String wate=definition;
			String curex="";
			WQDictionaryActivity.headerEndChar="{";
			WQDictionaryActivity.newlinebreak
			="}";
			if(definition.toLowerCase().contains("wate"))
			{				
			
				String sep="wate"+WQDictionaryActivity.headerEndChar+WQDictionaryActivity.newlinebreak;
				int curestart=wate.indexOf("{}");
				int cureend=wate.toLowerCase().indexOf(sep) ;
				if(cureend>curestart)
				{
					curex=wate.substring(curestart,cureend);
					curex=curex.replace(WQDictionaryActivity.headerEndChar, "")
							.replace("|", "").replace(WQDictionaryActivity.newlinebreak, "\n").trim();
					int a=0;
					while(curex.startsWith(WQDictionaryActivity.newlinebreak))
					{
						curex=curex.substring(1).trim();
						a++;
						if(a>5)
							break;
					}
					a=0;
					while(curex.endsWith(WQDictionaryActivity.newlinebreak))
					{
						curex=curex.substring(0, curex.length()-1).trim();
						if(a>5)
							break;
					}
			
					
					rv.setTextViewText(R.id.textCure, curex);
					rv.setViewVisibility(R.id.textCure, View.VISIBLE);
				}
				else
				{
					rv.setViewVisibility(R.id.textCure, View.GONE);
				}
			
				if(wate.toLowerCase().contains(sep))
					wate=definition.substring(definition.toLowerCase().indexOf(sep)+sep.length());
		
				String sep2=WQDictionaryActivity.newlinebreak+"|";
			
				if(wate.contains(sep2))
					wate=wate.substring(0, wate.indexOf(sep2));
				else if(wate.contains("|"))
					wate=wate.substring(0, wate.indexOf("|"));
				if(wate.contains("#!!"))
					wate = wate.replaceAll("#!!", "\t\t");
				if(wate.contains("#!"))
				{
					wate = wate.replaceAll("#!", "\t");	
				}
			}
			wate=wate.replaceAll(java.util.regex.Pattern.quote(WQDictionaryActivity.newlinebreak),
					"\n");
			int c = 1;
			while (wate.contains("#")) 
			{
				String cx = c + " .";
				wate = wate.replaceFirst(java.util.regex.Pattern.quote("#"), cx);
				c++;
				
			}
			
			rv.setTextViewText(R.id.textWord, word);
			rv.setTextViewText(R.id.textDefinition, wate);
		} 
		else
		{
			String mesg="Tu peyvên "+ WQDictionaryWidget.ziman;
			if(WQDictionaryWidget.cure!="")
				mesg+= " yên bi cûreya " +WQDictionaryWidget.cure;
			mesg+= " nehatin dîtin";
			rv.setTextViewText(R.id.textDefinition, mesg);
			System.out.println(mesg);
		}
			
		WQDictionaryDB.columntoSearch = WQDictionaryDB.KEY_WORD;
	
		rv.setViewVisibility(R.id.progressBarLayout, View.GONE);

		appWidgetManager.updateAppWidget(appWidgetId, rv);

	}
	
}