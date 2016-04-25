package Custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Philipp on 11/04/2015.
 */
public class CenterLineView extends View {
    public CenterLineView(Context context) {
        super(context);
    }

    public CenterLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);

        canvas.drawLine(-150, getHeight()/2 + 2, getWidth() + 150, getHeight()/2 + 2, paint);



    }
}
