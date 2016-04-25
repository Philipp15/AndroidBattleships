package Custom_views;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Philipp on 11/04/2015.
 */


public class StaticMethods
{
    public static boolean isPointWithin(float x, float y, float left, float right, float top, float bottom)
    {
        return (x <= right && x >= left && y <= bottom && y >= top);
    }

    public static void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

}




