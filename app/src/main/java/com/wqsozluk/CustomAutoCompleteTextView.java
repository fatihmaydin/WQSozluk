package com.wqsozluk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

@SuppressLint("NewApi")
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
	private Drawable dRight;
	private Drawable dLeft;
	private Rect rBounds;
    WQDictionaryActivity WQDictionaryactivity;
    
    
    public static final float MIN_TEXT_SIZE = 20;

    // Interface for resize notifications
    public interface OnTextResizeListener {
        void onTextResize(AutoCompleteTextView textView, float oldSize, float newSize);
    }

    // Our ellipse string
    private static final String mEllipsis = "...";

    // Registered resize listener
    private OnTextResizeListener mTextResizeListener;

    // Flag for text and/or size changes to force a resize
    private boolean mNeedsResize = false;

    // Text size that is set from code. This acts as a starting point for resizing
    private float mTextSize;

    // Temporary upper bounds on the starting text size
    private float mMaxTextSize = 0;

    // Lower bounds for text size
    private float mMinTextSize = MIN_TEXT_SIZE;

    // Text view line spacing multiplier
    private float mSpacingMult = 1.0f;

    // Text view additional line spacing
    private float mSpacingAdd = 0.0f;

    // Add ellipsis to text that overflows at the smallest text size
    private boolean mAddEllipsis = true;
    
    
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		WQDictionaryactivity = (WQDictionaryActivity) context;
	}

	public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WQDictionaryactivity = (WQDictionaryActivity) context;
	}

	public CustomAutoCompleteTextView(Context context) {
		super(context);
		WQDictionaryactivity = (WQDictionaryActivity) context;
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (right != null) {
			dRight = right;
		}
		if (left != null) {
			dLeft = left;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) 
		{
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			if (dRight != null) 
			{
				rBounds = dRight.getBounds();

				if (x > this.getWidth() - rBounds.width()) 
				{
					this.setText("");
					this.setSelection(0);
			
					this.requestFocus();
				}				
			}
			Rect bounds;

			if (dLeft != null) 
			{

				bounds = dLeft.getBounds();

				if (x < bounds.width()) 
				{
					this.setText("");
					this.setSelection(0);
			
					this.requestFocus();
				}		
			}
			if(!isFocused())
			{
				this.requestFocus();
				this.setSelection(getText().length());
				if(!getText().toString().equalsIgnoreCase(""))
				{
					showDropDown();
					if (WQDictionaryactivity.actionBarIsEnabled)
						WQDictionaryactivity.listviewresult.setAlpha((float) 0.3);
				}
				
			}
			
			if(this.getDropDownHeight()<0)
				showDropDown();
		}

		if(WQDictionaryactivity.showarabickeyboard)
		{
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		dRight = null;
		dLeft = null;
		rBounds = null;
		super.finalize();
	}

	
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		Cursor c = (Cursor) selectedItem;

		return c.getString(c.getColumnIndex(WQDictionaryDB.KEY_WORD));

	}
	
	
	 @Override
	    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
	        mNeedsResize = true;
	        // Since this view may be reused, it is good to reset the text size
	        resetTextSize();
	    }

	    /**
	     * If the text view size changed, set the force resize flag to true
	     */
	    @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        if (w != oldw || h != oldh) {
	            mNeedsResize = true;
	        }
	    }

	    /**
	     * Register listener to receive resize notifications
	     * @param listener
	     */
	    public void setOnResizeListener(OnTextResizeListener listener) {
	        mTextResizeListener = listener;
	    }

	    /**
	     * Override the set text size to update our internal reference values
	     */
	    @Override
	    public void setTextSize(float size) {
	        super.setTextSize(size);
	        mTextSize = getTextSize();
	    }

	    /**
	     * Override the set text size to update our internal reference values
	     */
	    @Override
	    public void setTextSize(int unit, float size) {
	        super.setTextSize(unit, size);
	        mTextSize = getTextSize();
	    }

	    /**
	     * Override the set line spacing to update our internal reference values
	     */
	    @Override
	    public void setLineSpacing(float add, float mult) {
	        super.setLineSpacing(add, mult);
	        mSpacingMult = mult;
	        mSpacingAdd = add;
	    }

	    /**
	     * Set the upper text size limit and invalidate the view
	     * @param maxTextSize
	     */
	    public void setMaxTextSize(float maxTextSize) {
	        mMaxTextSize = maxTextSize;
	        requestLayout();
	        invalidate();
	    }

	    /**
	     * Return upper text size limit
	     * @return
	     */
	    public float getMaxTextSize() {
	        return mMaxTextSize;
	    }

	    /**
	     * Set the lower text size limit and invalidate the view
	     * @param minTextSize
	     */
	    public void setMinTextSize(float minTextSize) {
	        mMinTextSize = minTextSize;
	        requestLayout();
	        invalidate();
	    }

	    /**
	     * Return lower text size limit
	     * @return
	     */
	    public float getMinTextSize() {
	        return mMinTextSize;
	    }

	    /**
	     * Set flag to add ellipsis to text that overflows at the smallest text size
	     * @param addEllipsis
	     */
	    public void setAddEllipsis(boolean addEllipsis) {
	        mAddEllipsis = addEllipsis;
	    }

	    /**
	     * Return flag to add ellipsis to text that overflows at the smallest text size
	     * @return
	     */
	    public boolean getAddEllipsis() {
	        return mAddEllipsis;
	    }

	    /**
	     * Reset the text to the original size
	     */
	    public void resetTextSize() {
	        if(mTextSize > 0) {
	            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
	            mMaxTextSize = mTextSize;
	        }
	    }

	    /**
	     * Resize text after measuring
	     */
	    @Override
	    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	        if(changed || mNeedsResize) {
	            int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
	            int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
	            resizeText(widthLimit, heightLimit);
	        }
	        super.onLayout(changed, left, top, right, bottom);
	    }


	    /**
	     * Resize the text size with default width and height
	     */
	    public void resizeText() {
	        int heightLimit = getHeight() - getPaddingBottom() - getPaddingTop();
	        int widthLimit = getWidth() - getPaddingLeft() - getPaddingRight();
	        resizeText(widthLimit, heightLimit);
	    }

	    /**
	     * Resize the text size with specified width and height
	     * @param width
	     * @param height
	     */
	    public void resizeText(int width, int height) {
	        CharSequence text = getText();
	        // Do not resize if the view does not have dimensions or there is no text
	        if(text == null || text.length() == 0 || height <= 0 || width <= 0 || mTextSize == 0) {
	            return;
	        }

	        // Get the text view's paint object
	        TextPaint textPaint = getPaint();

	        // Store the current text size
	        float oldTextSize = textPaint.getTextSize();
	        // If there is a max text size set, use the lesser of that and the default text size
	        float targetTextSize = mMaxTextSize > 0 ? Math.min(mTextSize, mMaxTextSize) : mTextSize;

	        // Get the required text height
	        int textHeight = getTextHeight(text, textPaint, width, targetTextSize);

	        // Until we either fit within our text view or we had reached our min text size, incrementally try smaller sizes
	        while(textHeight > height && targetTextSize > mMinTextSize) {
	            targetTextSize = Math.max(targetTextSize - 2, mMinTextSize);
	            textHeight = getTextHeight(text, textPaint, width, targetTextSize);
	        }

	        // If we had reached our minimum text size and still don't fit, append an ellipsis
	        if(mAddEllipsis && targetTextSize == mMinTextSize && textHeight > height) {
	            // Draw using a static layout
	            StaticLayout layout = new StaticLayout(text, textPaint, width, Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
	            // Check that we have a least one line of rendered text
	            if(layout.getLineCount() > 0) {
	                // Since the line at the specific vertical position would be cut off,
	                // we must trim up to the previous line
	                int lastLine = layout.getLineForVertical(height) - 1;
	                // If the text would not even fit on a single line, clear it
	                if(lastLine < 0) {
	                    setText("");
	                }
	                // Otherwise, trim to the previous line and add an ellipsis
	                else {
	                    int start = layout.getLineStart(lastLine);
	                    int end = layout.getLineEnd(lastLine);
	                    float lineWidth = layout.getLineWidth(lastLine);
	                    float ellipseWidth = textPaint.measureText(mEllipsis);

	                    // Trim characters off until we have enough room to draw the ellipsis
	                    while(width < lineWidth + ellipseWidth) {
	                        lineWidth = textPaint.measureText(text.subSequence(start, --end + 1).toString());
	                    }
	                    setText(text.subSequence(0, end) + mEllipsis);
	                }
	            }
	        }

	        // Some devices try to auto adjust line spacing, so force default line spacing
	        // and invalidate the layout as a side effect
	        textPaint.setTextSize(targetTextSize);
	        setLineSpacing(mSpacingAdd, mSpacingMult);

	        // Notify the listener if registered
	        if(mTextResizeListener != null) {
	            mTextResizeListener.onTextResize(this, oldTextSize, targetTextSize);
	        }

	        // Reset force resize flag
	        mNeedsResize = false;
	    }

	    // Set the text size of the text paint object and use a static layout to render text off screen before measuring
	    private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {
	        // Update the text paint object
	        paint.setTextSize(textSize);
	        // Measure using a static layout
	        StaticLayout layout = new StaticLayout(source, paint, width, Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
	        return layout.getHeight();
	    }

		public void setDrawable(Boolean showarabickeyboard) 
		{
//			if(showarabickeyboard)
//			{
//				dLeft.
//			}
			// TODO Auto-generated method stub
			
		}
}
