package Custom_views;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Philipp on 07/04/2015.
 */
public class ImageOnTouchListsner {


private View.OnTouchListener listener;

    public ImageOnTouchListsner(final int GRID_SIZE ,final View subView)
    {



                        listener = new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event)
                            {
                                if(event.getAction() == MotionEvent.ACTION_MOVE)
                                {
                                    if(isPointWithin((int) event.getRawX(), (int) event.getRawY(),subView.getLeft(),
                                            subView.getRight() , subView.getTop() , subView.getBottom()
                                    )){
                                        int fingerX = (int) (event.getRawX()  - v.getWidth() / 2) - 15;
                                        int fingerY = (int) (event.getRawY() - v.getHeight() / 2) - 15;

                                        int howcloseX = fingerX % GRID_SIZE;
                                        int howcloseY = fingerY % GRID_SIZE;

                                        if(howcloseX > GRID_SIZE/2)
                                        {
                                            v.setX( fingerX - howcloseX + GRID_SIZE + 6 );
                                        }
                                        if(howcloseY > GRID_SIZE/2)
                                        {
                                            v.setY(fingerY - howcloseY + GRID_SIZE - 15);
                                        }

                                    }else {
                                        v.setX(event.getRawX() - v.getWidth() / 2);
                                        v.setY(event.getRawY() - v.getHeight() / 2);
                                    }
                                }
                                        return true;
                            }
                        };



    }


    public View.OnTouchListener ReturnImageListener( )
    {
            return listener;
    }


    private boolean isPointWithin(int x, int y, int x1, int x2, int y1, int y2) {
        return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
    }
}