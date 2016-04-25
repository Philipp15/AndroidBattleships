package Custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Philipp on 05/04/2015.
 */
public class Rectangle extends View  {

    private boolean Hit;
    private boolean Miss;
    private int Probability =0;


    public Rectangle(Context context) {
        super(context);
        Hit = false;
        Miss = false;
        Probability = 0;
    }

    public Rectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Rectangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isHit() {
        return Hit;
    }

    public void setHit(boolean hit) {
        Hit = hit;
    }

    public boolean isMiss() {
        return Miss;
    }

    public void setMiss(boolean miss) {
        Miss = miss;
    }

    public int getProbability() {
        return Probability;
    }

    public void setProbability(int probability) {
        Probability = probability;
    }
    
    
    public void IncrementProbability()
    {
        Probability++;
    }

    public void DecrementProbability()
    {
        Probability--;
    }
}
