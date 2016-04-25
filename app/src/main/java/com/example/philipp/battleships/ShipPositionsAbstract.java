package com.example.philipp.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import Constant_Package.Constant_BT_Messages;
import Constant_Package.Handle_Constants;
import Custom_views.Rectangle;
import Custom_views.Ship;
import Custom_views.SubView;
import Custom_views.TwoDShipFactory;
import Main_Connector.Connector;

/**
 * Created by Philipp on 13/04/2015.
 */
public abstract class ShipPositionsAbstract  extends ActionBarActivity
{

    abstract void ContinueToNextScreen();
    abstract void Init();
    protected boolean meReady  = false;
    protected boolean OpponentReady = false;

    Connector myConnector;
    Intent FightWindow;
    Activity thisClass;


    Handler ShipPositionHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int code = msg.what;

            switch (code)
            {
                case Handle_Constants.POSITIONS_SETS:
                    Toast.makeText(thisClass,"Opponent ready", Toast.LENGTH_LONG).show();
                    OpponentReady = true;
            }

        }
    };

    public int GRID_WIDTH = 10;
    public int GRID_HEIGHT = 10;
    public int GRID_SIZE = 65;

    ViewGroup MainView;

    ArrayList<Ship> displayedShips;

    TwoDShipFactory ShipCreator;

    ArrayList<Integer> takenSqaures;

    Handler threadHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            startActivity(FightWindow);

        }
    };

    protected void Ready()
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(meReady && OpponentReady)
                    {
                        threadHandler.sendEmptyMessage(0);
                        break;
                    }
                }

            }
        };

        Thread checkerThread = new Thread(r);

        checkerThread.start();

    }



    public void btnRotate(View view)
    {
        float rotation = ShipCreator.getLastImageView().getRotation();

        if(rotation == 90 )
        {
            ShipCreator.getLastImageView().setRotation(0);
        }else{
            ShipCreator.getLastImageView().setRotation(90);
        }
    }




    private ArrayList<Rectangle> GetMarkedRectangles( Ship ship)
    {
        int height;
        int width;
        ArrayList<Rectangle> imgRectangles = new ArrayList<>();

        for (int i = 0; i< 100; i++)
        {
            int [] RawlocationShip = new int[2];
            ship.getLocationInWindow(RawlocationShip);

            int  shipX = RawlocationShip[0];
            int  shipY = RawlocationShip[1];

            View rectangle =  findViewById(i);

            //if center point of this rectangle is within the ship`s area
            int [] RawlocationSquare =  new int[2];
            rectangle.getLocationOnScreen(RawlocationSquare);
            int CenterX = RawlocationSquare[0] + GRID_SIZE/2;
            int CenterY = RawlocationSquare[1] + GRID_SIZE/2;


            if(ship.getRotation() == 90)
            {
                height = ship.getWidth();
                width = ship.getHeight();

            }else{
                height = ship.getHeight();
                width = ship.getWidth();
            }


            int FinalShipPosition = ship.getRotation() == 90 ? shipX - width : shipX + width;
            int FinalShipLeft = Math.min(FinalShipPosition , shipX);
            int FinalShipRight = Math.max(FinalShipPosition , shipX);

            int FinalBottomShip = shipY + height ;

            if(isPointWithin(CenterX , CenterY , FinalShipLeft, FinalShipRight, shipY,  FinalBottomShip ) )
            {
                if(!takenSqaures.contains(i))
                {
                    ship.OccupiedSqaures.add(i);
                    takenSqaures.add(i);
                }
                else
                {
                    Toast.makeText(this, "Multiple ships cannot intersect at the same square", Toast.LENGTH_LONG).show();
                    ErrorUndo();
                    return new ArrayList<>();
                }
            }
        }
        return imgRectangles;
    }




    private boolean isPointWithin(int x, int y, int left, int right, int top, int bottom) {

        Log.d("asd", String.valueOf(x) + " " + String.valueOf(y) + "  " + String.valueOf(left)
                + "   " + String.valueOf(right) + "   " + String.valueOf(top) + "   " + String.valueOf(bottom));

        return (x <= right && x >= left && y <= bottom && y >= top);
    }

    private void ErrorUndo()
    {
        takenSqaures.clear();
        for(Ship myship : displayedShips)
        {
            myship.OccupiedSqaures.clear();
        }
    }


protected void AcceptFunction()
{

    ErrorUndo();
    for(Ship myship : displayedShips)
    {
        GetMarkedRectangles((Ship ) findViewById(myship.getId()));
    }

    SetNextActivity();
    String Squares = "";
    for (int i = 0 ;i < displayedShips.size() ; i ++ )
    {
        for (int x  = 0 ; x < displayedShips.get(i).OccupiedSqaures.size(); x++)
        {
            Squares+= String.valueOf(displayedShips.get(i).OccupiedSqaures.get(x)) + ",";
        }
        FightWindow.putExtra("Ship" + i , Squares.substring(0,Squares.length() -1));
        Squares ="";
    }

    if(myConnector != null) {
        myConnector.SendMessage(Constant_BT_Messages.positionsSet);
    }

    ContinueToNextScreen();

}

    abstract void SetNextActivity();



    protected void SetLayout()
    {
        SubView subViewGroup = new SubView(this);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(650,650);
        subViewGroup.setLayoutParams(layoutParams);

        ShipCreator = new TwoDShipFactory(GRID_SIZE , subViewGroup , this , 15);

        RelativeLayout.LayoutParams mlayoutParam =   new RelativeLayout.LayoutParams(GRID_SIZE,GRID_SIZE);


        int counter = 0;
        for(int i = 0; i < GRID_WIDTH; i++) {
            for(int j = 0; j <GRID_HEIGHT ; j++)
            {
                View rectangle = new View(this);
                rectangle.setId(counter);
                rectangle.setLayoutParams(mlayoutParam);

                counter++;

                subViewGroup.addView(rectangle);

            }
        }


        MainView.addView(subViewGroup);

        Ship TwoSqaureShip = ShipCreator.RequestShip(1,R.drawable.boattwo, 455,900 , 6 , -15 , 30 , 6 , +10);
        TwoSqaureShip.setId(R.id.ShipTwoSquaresOne);

        Ship  AnotherTwoSqaureShip = ShipCreator.RequestShip(2, R.drawable.boattwo, 455, 1000, 6, -15, 30, 6, +10);
        AnotherTwoSqaureShip.setId(R.id.ShipTwoSquaresTwo);

        Ship ThreeSqaureShip = ShipCreator.RequestShip(3, R.drawable.boatthree, 355, 950, 6, -35, 10, -35, -36);
        ThreeSqaureShip.setId(R.id.ShipThreeSquaresOne);

        Ship AnotherThreeSqaureShip = ShipCreator.RequestShip(4, R.drawable.boatthree, 255, 950, 6, -35, 10, -35, -36);
        AnotherThreeSqaureShip.setId(R.id.ShipThreeSquaresTwo);

        Ship FourSqaureShip = ShipCreator.RequestShip(5, R.drawable.boatfour, 155, 900, 6, -35, 35, -65, -65);
        FourSqaureShip.setId(R.id.ShipFourSquaresOne);

        displayedShips.add(TwoSqaureShip);
        displayedShips.add(AnotherTwoSqaureShip);

        displayedShips.add(ThreeSqaureShip);
        displayedShips.add(AnotherThreeSqaureShip);
        displayedShips.add(FourSqaureShip);

        MainView.addView(TwoSqaureShip, 110,110);
        MainView.addView(AnotherTwoSqaureShip , 110,110);

        MainView.addView(ThreeSqaureShip , 110,200);
        MainView.addView(AnotherThreeSqaureShip , 110,200);
        MainView.addView(FourSqaureShip , 110,250);



    }


}
