package Custom_views;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Philipp on 07/04/2015.
 */
public class TwoDShipFactory
{
    Activity CurrentActivity;

    private ImageView LastImageView;

    private View.OnTouchListener listener;

    final int GRID_SIZE;
    final View subView;
    final int padding;
    public int Ship_Id;


    public TwoDShipFactory(final int gridSize, final View subView, Activity currentActivity, int padding)
    {
        this.CurrentActivity = currentActivity;
        this.GRID_SIZE = gridSize;
        this.subView = subView;
            this.padding = padding - 6;
    }


    private View.OnTouchListener CreateCustomListener( final int xOffSet, final int yOffSet
    , final int RotatetedXoffset ,final int RotatetedYoffset , final int SET_Y )
    {

   return new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {

                if (v.getRotation() == 0) {

                    if (isPointWithin((int) event.getRawX(), (int) event.getRawY(), subView.getLeft(),
                            subView.getRight(), subView.getTop(), subView.getBottom()
                    )) {
                        int fingerX = (int) (event.getRawX() - v.getWidth() / 2) ;
                        int fingerY = (int) (event.getRawY() - v.getHeight() / 2) ;

                        int howcloseX = fingerX % GRID_SIZE;
                        int howcloseY = fingerY % GRID_SIZE;

                        if (howcloseX > GRID_SIZE / 2) {
                            v.setX(fingerX - howcloseX + GRID_SIZE + xOffSet);
                        }
                        if (howcloseY > GRID_SIZE / 2) {
                            v.setY(fingerY - howcloseY + GRID_SIZE + yOffSet);
                        }

                        if(fingerX < GRID_SIZE /2)
                        {
                            v.setX(0 + padding );
                        }

                        if(fingerY < GRID_SIZE /2)
                        {
                            v.setY(0 + padding);
                        }


                    } else {
                        v.setX(event.getRawX() - v.getWidth() / 2);
                        v.setY(event.getRawY() - v.getHeight() / 2);
                    }
                } else {

                    if (isPointWithin((int) event.getRawX(), (int) event.getRawY(), subView.getLeft(),
                            subView.getRight(), subView.getTop(), subView.getBottom()
                    )) {
                        int fingerX = (int) (event.getRawX() -  v.getWidth() / 2) ;
                        int fingerY = (int) (event.getRawY() -  v.getHeight() / 2) ;
                        Log.d("ASd", "X : " + String.valueOf(event.getRawX()) + " Y " + String.valueOf(event.getRawY()));
                        int howcloseX = fingerX % GRID_SIZE;
                        int howcloseY = fingerY % GRID_SIZE;

                        if (howcloseX > GRID_SIZE / 2) {
                            v.setX(fingerX - howcloseX + GRID_SIZE + RotatetedXoffset);
                        }
                        if(event.getRawY() < GRID_SIZE )
                        {
                            v.setY(SET_Y  );
                        }

                        if (howcloseY > GRID_SIZE / 2) {
                            v.setY(fingerY - howcloseY + GRID_SIZE + RotatetedYoffset );
                        }
                        if(fingerX < GRID_SIZE /2)
                        {
                            v.setX(0 + 35 );
                        }



                    } else {
                        v.setX(event.getRawX() - v.getWidth() / 2);
                        v.setY(event.getRawY() - v.getHeight() / 2);
                    }


                }

                LastImageView = (ImageView) v;


            }
            return true;
        }
    };


}




    public Ship RequestShip( int shipID, int drawableResource , int x , int y,
                                 int xOffset , int yOffset, int rotatedXoffset ,int rotatedYoffset,int  SET_Y)
    {
        this.Ship_Id = shipID;
        Ship ship = new Ship(CurrentActivity);
        ship.setX(x);
        ship.setY(y);
        ship.setImageResource(drawableResource);

        ship.setOnTouchListener(CreateCustomListener( xOffset , yOffset, rotatedXoffset , rotatedYoffset, SET_Y));
        return ship;
    }



    private boolean isPointWithin(int x, int y, int x1, int x2, int y1, int y2) {
        return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
    }

    public ImageView getLastImageView() {
        if(LastImageView == null)
        {
            LastImageView = new ImageView(CurrentActivity);
            return  LastImageView;
        }
        return LastImageView;
    }


}
