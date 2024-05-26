package com.wqferheng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppRater {
    private final static String APP_TITLE = "WQFerheng";
    private final static String APP_PNAME = "com.wqferheng";
    
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 2;
    static Activity activity=null;

    public static void app_launched(Context mContext)
    {
        try {
			activity=(Activity) mContext;
			SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
			if (prefs.getBoolean("dontshowagain", false)) { return ; }
			
			SharedPreferences.Editor editor = prefs.edit();
			
			// Increment launch counter
			long launch_count = prefs.getLong("launch_count", 0) + 1;
			editor.putLong("launch_count", launch_count);

			// Get date of first launch
			Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
			if (date_firstLaunch == 0) {
			    date_firstLaunch = System.currentTimeMillis();
			    editor.putLong("date_firstlaunch", date_firstLaunch);
			}
			
			// Wait at least n days before opening
			if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			    if (System.currentTimeMillis() >= date_firstLaunch + 
			            (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
			        showRateDialog(mContext, editor);
			    }
			}
			
			editor.commit();
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) 
    {
    	
        try {
			final Dialog dialog = new Dialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.apprater);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			String rate=mContext.getString(R.string.Rate, APP_TITLE);

TextView txt=dialog.findViewById(R.id.txt_content);
txt.setText(mContext.getString(R.string.AppRateString, APP_TITLE));
			TextView txtrate=dialog.findViewById(R.id.txt_dialogtitle);
			txtrate.setText(rate);
			Button mDialogNo = dialog.findViewById(R.id.btn_yes);
			mDialogNo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
					try {
						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
					}
			        catch (Exception e) {
						// TODO Auto-generated catch block
			        	 mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PNAME)));
						e.printStackTrace();
					}
			        if (editor != null)
			        {
			            editor.putBoolean("dontshowagain", true);
			            editor.commit();
			        }
			        dialog.dismiss();
				}
			});

			Button mDialogOk = dialog.findViewById(R.id.btn_no);
			mDialogOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
					if (editor != null)
			        {
			            editor.putBoolean("dontshowagain", true);
			            editor.commit();
			        }
			        dialog.dismiss();
				}
			});

			Button btn_later = dialog.findViewById(R.id.btn_later);
			btn_later.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});

			dialog.show();

//			final Dialog dialog = new Dialog(mContext);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.setCancelable(false);
//			dialog.setContentView(R.layout.apprater);
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//			Button yes=mContext.vi
//			mDialogNo.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
//					dialog.dismiss();
//				}
//			});
//
//			FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
//			mDialogOk.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
//					dialog.cancel();
//				}
//			});
//
//			dialog.show();
//
//			final Dialog dialog = new Dialog(mContext);
//
//
//			String rate=mContext.getString(R.string.Rate, APP_TITLE);
//			dialog.setTitle(rate);
//
//			LinearLayout ll = new LinearLayout(mContext);
//			ll.setOrientation(LinearLayout.VERTICAL);
//			ll.setBackgroundColor(Color.parseColor("#3f6699"));
//
//			TextView tv = new TextView(mContext);
//			tv.setBackgroundColor(Color.parseColor("#2f6699"));
//			tv.setText(mContext.getString(R.string.AppRateString, APP_TITLE));
//			tv.setWidth(240);
//			tv.setPadding(4, 0, 4, 10);
//			ll.addView(tv);
//
//			Button b1 = new Button(mContext);
//			b1.setBackgroundColor(Color.parseColor("#3f6699"));
//			b1.setText(rate);
//			b1.setOnClickListener(new OnClickListener() {
//			    public void onClick(View v)
//			    {
//			        try {
//						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
//					}
//			        catch (Exception e) {
//						// TODO Auto-generated catch block
//			        	 mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PNAME)));
//						e.printStackTrace();
//					}
//			        if (editor != null)
//			        {
//			            editor.putBoolean("dontshowagain", true);
//			            editor.commit();
//			        }
//			        dialog.dismiss();
//			    }
//			});
//			ll.addView(b1);
//
//			Button b2 = new Button(mContext);
//			b2.setText(R.string.Remind);
//			b2.setBackgroundColor(Color.parseColor("#2f6699"));
//			b2.setOnClickListener(new OnClickListener() {
//			    public void onClick(View v) {
//			        dialog.dismiss();
//			    }
//			});
//			ll.addView(b2);
//
//			Button b3 = new Button(mContext);
//			b3.setText(R.string.Thanks);
//			b3.setBackgroundColor(Color.parseColor("#3f6699"));
//			b3.setOnClickListener(new OnClickListener() {
//			    public void onClick(View v) {
//			        if (editor != null)
//			        {
//			            editor.putBoolean("dontshowagain", true);
//			            editor.commit();
//			        }
//			        dialog.dismiss();
//			    }
//			});
//			ll.addView(b3);
//
//			dialog.setContentView(ll);
//			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
    	
    	
    }
}
