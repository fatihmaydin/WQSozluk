package com.wqferheng;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

class ArabicKeyboard {

    /** A link to the KeyboardView that is used to render this CustomKeyboard. */ 
    private KeyboardView mKeyboardView;
    /** A link to the activity that hosts the {@link #mKeyboardView}. */ 
    private WQFerhengActivity     mHostActivity;
    
    private Boolean IsShouldShown;
    CustomAutoCompleteTextView edittext;
    /** The key (code) handler. */
    private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

        public final static int CodeDelete   = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel   = -3; // Keyboard.KEYCODE_CANCEL
        public final static int CodePrev     = 55000;
        public final static int CodeAllLeft  = 55001;
        public final static int CodeLeft     = 55002;
        public final static int CodeRight    = 55003;
        public final static int CodeAllRight = 55004;
        public final static int CodeNext     = 55005;
        public final static int CodeClear    = 55006;
          Key key;
        @Override public void onKey(int primaryCode, int[] keyCodes) 
        {
            // NOTE We can say '<Key android:codes="49,50" ... >' in the xml file; all codes come in keyCodes, the first in this list in primaryCode 
            // Get the EditText and its Editable
        	
            try {
				View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
				if( focusCurrent==null || focusCurrent.getClass()!=CustomAutoCompleteTextView.class ) 
					return;
				 edittext = (CustomAutoCompleteTextView) focusCurrent;
				Editable editable = edittext.getText();
				int start = edittext.getSelectionStart();
				if( primaryCode==CodeCancel ) 
				{
				    hideCustomKeyboard();
				} else if( primaryCode==CodeDelete ) {
				    if( editable!=null && start>0 ) editable.delete(start - 1, start);
				} else if( primaryCode==CodeClear ) {
				    if( editable!=null ) editable.clear();
				} else if( primaryCode==CodeLeft ) {
				    if( start>0 ) edittext.setSelection(start - 1);
				} else if( primaryCode==CodeRight ) {
				    if (start < edittext.length()) edittext.setSelection(start + 1);
				} else if( primaryCode==CodeAllLeft ) {
				    edittext.setSelection(0);
				} else if( primaryCode==CodeAllRight ) {
				    edittext.setSelection(edittext.length());
				} else if( primaryCode==CodePrev ) {
				    View focusNew= edittext.focusSearch(View.FOCUS_BACKWARD);
				    if( focusNew!=null ) focusNew.requestFocus(); 
				} else if( primaryCode==CodeNext ) {
				    View focusNew= edittext.focusSearch(View.FOCUS_FORWARD);
				    if( focusNew!=null ) focusNew.requestFocus(); 
				} else 
				{ // insert character
					if(primaryCode==45000)
				    editable.insert(start, "ض");
					else if(primaryCode==45001)
						 editable.insert(start, "ص");
					else if(primaryCode==45002)
					 editable.insert(start, "ق");
				 	else if(primaryCode==450022)
				  		 editable.insert(start, "ث");
					else if(primaryCode==45003)
					 editable.insert(start, "ف");
					else if(primaryCode==45004)
					 editable.insert(start, "ع");
					else if(primaryCode==450044)
				  		 editable.insert(start, "غ");
					else if(primaryCode==45005)
					 editable.insert(start, "ه");
					else if(primaryCode==45006)
				  		 editable.insert(start, "خ");
					else if(primaryCode==45007)
				  		 editable.insert(start, "ح");
					else if(primaryCode==45008)
				  		 editable.insert(start, "ج");
					else if(primaryCode==45009)
				  		 editable.insert(start, "چ");
					else if(primaryCode==45010)
				  		 editable.insert(start, "ش");
					
					else if(primaryCode==45011)
				 		 editable.insert(start, "س");
					else if(primaryCode==45012)
				 		 editable.insert(start, "ی");
					else if(primaryCode==45013)
				 		 editable.insert(start, "ب");
					else if(primaryCode==45014)
				 		 editable.insert(start, "ل");
					
					else if(primaryCode==45015)
						 editable.insert(start, "ا");
				else if(primaryCode==45016)
						 editable.insert(start, "ت");
				else if(primaryCode==45017)
						 editable.insert(start, "ن");
				else if(primaryCode==45018)
						 editable.insert(start, "م");
				else if(primaryCode==45019)
				 editable.insert(start, "ک");
				else if(primaryCode==45020)
					 editable.insert(start, "گ");
					
				else if(primaryCode==45021)
					 editable.insert(start, "ظ");
				else if(primaryCode==45022)
					 editable.insert(start, "ط");
				else if(primaryCode==45023)
					 editable.insert(start, "ژ");
				else if(primaryCode==45024)
					 editable.insert(start, "ز");
				else if(primaryCode==45025)
					 editable.insert(start, "ر");
					
				else if(primaryCode==45026)
					 editable.insert(start, "ذ");
				else if(primaryCode==45027)
					 editable.insert(start, "د");
				else if(primaryCode==45028)
					 editable.insert(start, "پ");
				else if(primaryCode==45029)
					 editable.insert(start, "و");
					
				else if(primaryCode==445045)
				{
					hideCustomKeyboard();
					IsShouldShown=false;
					mHostActivity.ResetArabicKeyboard();
					
				}
				else if(primaryCode==445055)
				 editable.insert(start, " ");
				else if(primaryCode==445065)
				{
					
				mHostActivity.GO();
				}
      
        
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        @Override public void onPress(int arg0) {
        }

        @Override public void onRelease(int primaryCode)
		{

        }

        @Override public void onText(CharSequence text) {
        }

        @Override public void swipeDown() {
        }

        @Override public void swipeLeft() {
        }

        @Override public void swipeRight() {
        }

        @Override public void swipeUp() {
        }
    };

    /**
     * Create a custom keyboard, that uses the KeyboardView (with resource id <var>viewid</var>) of the <var>host</var> activity,
     * and load the keyboard layout from xml file <var>layoutid</var> (see {@link Keyboard} for description).
     * Note that the <var>host</var> activity must have a <var>KeyboardView</var> in its layout (typically aligned with the bottom of the activity).
     * Note that the keyboard layout xml file may include key codes for navigation; see the constants in this class for their values.
     * Note that to enable EditText's to use this custom keyboard, call the {@link #registerEditText(int)}.
     *
     * @param host The hosting activity.
     * @param viewid The id of the KeyboardView.
     * @param layoutid The id of the xml file containing the keyboard layout.
     */
    public ArabicKeyboard(Activity host, int viewid, int layoutid) {
        mHostActivity= (WQFerhengActivity)host;
       
        mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
       // mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		host.setTheme(WQFerhengActivity.theme);
    }
    
    public ArabicKeyboard(Activity host, int viewid, int layoutid, Boolean isshouldshown) {

        mHostActivity= (WQFerhengActivity)host;
       
        mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
       // mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        IsShouldShown=isshouldshown;
		host.setTheme(WQFerhengActivity.theme);
    }


    /** Returns whether the CustomKeyboard is visible. */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /** Make the CustomKeyboard visible, and hide the system keyboard for view v. */
    public void showCustomKeyboard( View v ) {
    	
    	if(mKeyboardView!=null)
    	{
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        }
    
    }

    /** Make the CustomKeyboard invisible. */
    public void hideCustomKeyboard() 
    {
    	if(mKeyboardView!=null)
    	{
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);

		}
        
       
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the hosting activity) for using this custom keyboard. 
     *
     * @param resid The resource id of the EditText that registers to the custom keyboard.
     */
    
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
    	edittext= (CustomAutoCompleteTextView)mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override public void onFocusChange(View v, boolean hasFocus) {
            	
                if( hasFocus)
                	if(IsShouldShown)
                	showCustomKeyboard(v); 
                	
                else 
                	if(IsShouldShown)
                	hideCustomKeyboard(); 
            }
        });
        edittext.setOnClickListener(new OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override public void onClick(View v) 
            {
            	if(IsShouldShown)
            	{
            		showCustomKeyboard(v); 
            	}
            	
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                //edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }
    
    public void SetShouldShown(Boolean isshouldshown, int resid)
    {
    	IsShouldShown=isshouldshown;
     
    	if(!IsShouldShown)
    	{
    		 InputMethodManager imm = (InputMethodManager) mHostActivity.getSystemService(mHostActivity.INPUT_METHOD_SERVICE);
    			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    		hideCustomKeyboard();
    	}
    	else
    	{
    		 if( edittext!=null ) ((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    		showCustomKeyboard(edittext);
    		
    	}
      	
    	edittext.setText("");
    	edittext.setSelection(0);
    	edittext.clearFocus();
    	edittext.requestFocus();    	
    	
    }

}


// NOTE How can a change the background color of some keys (like the shift/ctrl/alt)?
// NOTE What does android:keyEdgeFlags do/mean
