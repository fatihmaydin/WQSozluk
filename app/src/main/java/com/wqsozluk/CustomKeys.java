package com.wqsozluk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class CustomKeys extends LinearLayout {

	
	Button buttonc;
	Button buttone;
	Button buttoni;
	Button buttons;
	Button buttonu;
	Button buttonii;
	Button buttonue;
	Button buttonoe;
	Button buttong;	
	Button buttonarabickeyboard;
	Button buttonarabic;
	
	WQDictionaryActivity WQDictionaryActivity;
	
	public CustomKeys(Context context) {
		super(context);
		WQDictionaryActivity = (WQDictionaryActivity) context;
		loadViews();
		
	}
	
	public CustomKeys(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customkeys, this);
		WQDictionaryActivity = (WQDictionaryActivity) context;
		loadViews();
		
	}
	
	private void loadViews() {

		buttonc = (Button) findViewById(R.id.buttonc);
		setBUttonOnClickListeners(buttonc);
		
		buttoni = (Button) findViewById(R.id.buttoni);
		setBUttonOnClickListeners(buttoni);
		
		buttone = (Button) findViewById(R.id.buttone);
		setBUttonOnClickListeners(buttone);
		
		buttonu = (Button) findViewById(R.id.buttonu);
		setBUttonOnClickListeners(buttonu);
		
		buttonii = (Button) findViewById(R.id.buttonii);
		setBUttonOnClickListeners(buttonii);
		
		buttonue = (Button) findViewById(R.id.buttonue);
		setBUttonOnClickListeners(buttonue);
		
		buttonoe = (Button) findViewById(R.id.buttonoe);
		setBUttonOnClickListeners(buttonoe);
		
		buttong = (Button) findViewById(R.id.buttong);
		setBUttonOnClickListeners(buttong);
		
		buttons = (Button) findViewById(R.id.buttons);
		setBUttonOnClickListeners(buttons);
		
		buttonarabickeyboard = (Button) findViewById(R.id.buttonarabickeyboard);
		setBUttonOnClickListeners(buttonarabickeyboard);
		
		buttonarabic = (Button) findViewById(R.id.buttonarabic);
		setBUttonOnClickListeners(buttonarabic);
		}
	
	private void setBUttonOnClickListeners(final Button button) {
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) 
			{
				if(v==buttonarabickeyboard)
				{
					String lang=WQDictionaryActivity.ResetArabicKeyboard();
					buttonarabickeyboard.setText(lang);
					SetKeyTexts(lang);
					
				}
				else if(v==buttonarabic)
				{
					buttonarabic.setText(WQDictionaryActivity.Alphabetize());					
				}
				else 
				{
					WQDictionaryActivity.InsertLetter(button.getTag().toString());
				} 
				
			}

			
		});
	}
	public void SetKeyTexts(String lang) 
	{
			try {
				if(!lang.contains("Lat"))
				{
					String s=WQDictionaryActivity.getString( R.string.buttonc);
					buttonc.setText(s);
					buttonc.setTag(s);
					
					s=WQDictionaryActivity.getString( R.string.buttone);
					if(lang.equalsIgnoreCase("tr")) {
						s = "x";
					}
					buttone.setText(s);
					buttone.setTag(s);

					s=WQDictionaryActivity.getString( R.string.buttoni);
					if(lang.equalsIgnoreCase("tr")) {
						s = "İ";
					}
					buttoni.setText(s);
					buttoni.setTag(s);						
					
					s=WQDictionaryActivity.getString( R.string.buttonu);
					buttonu.setText(s);
					buttonu.setTag(s);
					buttonu.setVisibility(View.GONE);
					s=WQDictionaryActivity.getString( R.string.buttonoe);
					buttonoe.setText(s);
					buttonoe.setTag(s);
					
					s=WQDictionaryActivity.getString( R.string.buttonii);
					buttonii.setText(s);
					buttonii.setTag(s);
					
					s=WQDictionaryActivity.getString( R.string.buttong);
					buttong.setText(s);
					buttong.setTag(s);
					
					buttonue.setVisibility(View.VISIBLE);
					buttons.setVisibility(View.VISIBLE);
					
				}
				else
				{
					buttonc.setText("ئا");
					buttonc.setTag("ئا");
					
					buttone.setText("ێ");
					buttone.setTag("ێ");
					
					buttoni.setText("ڵ");
					buttoni.setTag("ڵ");
					
					buttonu.setText("وو");
					buttonu.setTag("وو");
					
					buttonoe.setText("ۆ");
					buttonoe.setTag("ۆ");
					
					buttonii.setText("ڕ");
					buttonii.setTag("ڕ");
					
					buttong.setText("ڤ");
					buttong.setTag("ڤ");	
					
					buttonue.setVisibility(View.GONE);
					buttons.setVisibility(View.GONE);
				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public void KeyboardChanged(String showHideArabicKeyboard) 
	{        
		buttonarabickeyboard.setText(showHideArabicKeyboard);
		SetKeyTexts(showHideArabicKeyboard);	
		
	}

	public void HideKEys() {
		buttonarabic.setVisibility(View.GONE);
		buttonarabickeyboard.setVisibility(View.GONE);
	}

}
