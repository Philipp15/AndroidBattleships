package Custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Philipp on 10/04/2015.
 */
public class SubView extends ViewGroup {



    private int mColumnCount =10 ;

    public SubView(Context context) {
        super(context);
    }

    public SubView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row, col, left, top;
        for (int i = 0; i < getChildCount(); i++) {
            row = i / mColumnCount;
            col = i % mColumnCount;
            View child = getChildAt(i);
            left = col * child.getMeasuredWidth();
            top = row * child.getMeasuredHeight();



            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        }

    }



      @Override
    protected void dispatchDraw(Canvas canvas) {
        //Let the framework do its thing
        super.dispatchDraw(canvas);

          Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
          mGridPaint.setStyle(Paint.Style.STROKE);
          mGridPaint.setColor(Color.BLUE);
          mGridPaint.setStrokeWidth(2);

        //Draw the grid lines
        for (int i=0; i <= getWidth(); i += (getWidth() / mColumnCount)) {
            canvas.drawLine(i, 0, i, getHeight(), mGridPaint);
        }
        for (int i=0; i <= getHeight(); i += (getHeight() / mColumnCount)) {
            canvas.drawLine(0, i, getWidth(), i, mGridPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;

        //Get the width based on the measure specs
        widthSize = getDefaultSize(0, widthMeasureSpec);

        //Get the height based on measure specs
        heightSize = getDefaultSize(0, heightMeasureSpec);

        int majorDimension = Math.min(widthSize, heightSize);
        //Measure all child views
        int blockDimension = majorDimension / mColumnCount;
        int blockSpec = MeasureSpec.makeMeasureSpec(blockDimension, MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(majorDimension, majorDimension);
    }
}
