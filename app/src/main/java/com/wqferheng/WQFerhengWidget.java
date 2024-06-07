package com.wqferheng;

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
import android.widget.Toast;

public class WQFerhengWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "com.wqferheng.ACTION_WIDGET_RECEIVER";
	public static final String OPEN_URL_ACTION = "com.wqferheng.OPEN_URL_ACTION";
	public static final String Refresh = "com.wqferheng.Refresh";
	public static final String LAUNCH_WQFERHENG = "com.wqferheng.LAUNCH_WQFERHENG";
	public static final String Web_Link = "com.wqferheng.Web_Link";
	public static final String Zareva = "com.wqferheng.Zareva";
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
					WQFerhengWidgetConfig.class);
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
			int id =WQFerhengWidgetConfig.mAppWidgetId;
			if(extras!=null)
			{
				 id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
			}
			GetWord(context, id);
			
		} 
 
		else if (LAUNCH_WQFERHENG.equals(intent.getAction())) {

			Bundle extras = intent.getExtras();
			 extras.putString("widget_word", word);
			 Intent onClickDone = new Intent(context.getApplicationContext(),
			 WQFerhengActivity.class);
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
						WQFerhengWidget.class));
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
						WQFerhengWidget.class));
	
	}


	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int mAppWidgetId) {
		System.out.print("0");
		Log.println(Log.ERROR,"d","aa");
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		cure = WQFerhengWidgetConfig.loadUrlPref(context, mAppWidgetId,
				"cûreya peyvê");
		ziman = WQFerhengWidgetConfig.loadUrlPref(context, mAppWidgetId,
				"ziman");

		Intent intentx = new Intent(context, WQFerhengWidget.class);
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


		Intent intentref=  new Intent(context, WQFerhengWidget.class);
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

		Intent intentGoToMalper = new Intent(context, WQFerhengWidget.class);
		intentGoToMalper.setAction(WQFerhengWidget.LAUNCH_WQFERHENG);
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

		WQFerhengDB.columntoSearch = WQFerhengDB.KEY_DEFINITION;
	

		WQFerhengQueryProvider queryProvider = new WQFerhengQueryProvider(ctxt);
		WQFerhengWidget.cure = WQFerhengWidgetConfig.loadUrlPref(ctxt, appWidgetId,
				"cûreya peyvê");
		WQFerhengWidget.ziman = WQFerhengWidgetConfig.loadUrlPref(ctxt, appWidgetId,
				"ziman");
		
		WQFerhengActivity.zimanquery = WQFerhengWidget.ziman;
		WQFerhengActivity.typeToSearch = WQFerhengWidget.cure;
		
	
		if(WQFerhengWidget.ziman.equalsIgnoreCase("")||(WQFerhengWidgetConfig.IsCOnfiguring))
		{
			//return;
		}
		String selection = WQFerhengDB.KEY_DEFINITION + " match ? ";
		
		Uri uri = WQFerhengDBProvider.CONTENT_URI;
		String query ="";// "'" + WQFerhengActivity.zimanquery + "%*'";
		String type= WQFerhengWidget.cure;
		if (!type.toLowerCase().contains("hemû"))
		{
			
			query+= WQFerhengActivity.zimanquery+":"+
					type.replace("*", "")+";";
			
	
		}
		else
		{
			query+= WQFerhengActivity.zimanquery+":";
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
			
				
				word= WQFerhengQueryProvider. GetValue(cursor, WQFerhengDB.KEY_WORD);
				String id= WQFerhengQueryProvider. GetValue(cursor, WQFerhengDB.KEY_ID);
				if(word==null||word.equals(""))
					word= WQFerhengQueryProvider. GetValue(cursor, WQFerhengDB.KEY_WORD_N);
				Words ww=	queryProvider.GetSingleWord(id);
				definition=WQFerhengActivity.Decode(ww.getwate(),word, word);
				definition=definition.replace("[","").replace("]", "");
				definition=definition.replace("£","").replace("£", "");
			//	definition=queryProvider. GetValue(cursor, WQFerhengDB.KEY_DEFINITION);
			xy++;
			
				if(xy>5)
					break;
			}
			String wate=definition;
			String curex="";
			WQFerhengActivity.headerEndChar="{";
			WQFerhengActivity.newlinebreak
			="}";
			if(definition.toLowerCase().contains("wate"))
			{				
			
				String sep="wate"+WQFerhengActivity.headerEndChar+WQFerhengActivity.newlinebreak;
				int curestart=wate.indexOf("{}");
				int cureend=wate.toLowerCase().indexOf(sep) ;
				if(cureend>curestart)
				{
					curex=wate.substring(curestart,cureend);
					curex=curex.replace(WQFerhengActivity.headerEndChar, "")
							.replace("|", "").replace(WQFerhengActivity.newlinebreak, "\n").trim();
					int a=0;
					while(curex.startsWith(WQFerhengActivity.newlinebreak))
					{
						curex=curex.substring(1).trim();
						a++;
						if(a>5)
							break;
					}
					a=0;
					while(curex.endsWith(WQFerhengActivity.newlinebreak))
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
		
				String sep2=WQFerhengActivity.newlinebreak+"|";
			
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
			wate=wate.replaceAll(java.util.regex.Pattern.quote(WQFerhengActivity.newlinebreak),
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
			String mesg="Tu peyvên "+ WQFerhengWidget.ziman;
			if(WQFerhengWidget.cure!="")
				mesg+= " yên bi cûreya " +WQFerhengWidget.cure;
			mesg+= " nehatin dîtin";
			rv.setTextViewText(R.id.textDefinition, mesg);
			System.out.println(mesg);
		}
			
		WQFerhengDB.columntoSearch = WQFerhengDB.KEY_WORD;
	
		rv.setViewVisibility(R.id.progressBarLayout, View.GONE);

		appWidgetManager.updateAppWidget(appWidgetId, rv);

	}
	
}