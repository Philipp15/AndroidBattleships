package com.example.philipp.battleships;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Constant_Package.Handle_Constants;
import Custom_views.CenterLineView;
import Custom_views.Rectangle;
import Custom_views.Ship;
import Custom_views.StaticMethods;
import Custom_views.SubView;
import Main_Connector.Connector;

/**
 * Created by Philipp on 13/04/2015.
 */
public abstract class BattleAbstract extends ActionBarActivity
{

    abstract void OnFire(int square, ArrayList<Ship> ships);
    abstract void Fire(int square);
    abstract void Init();

    abstract void GameOver(String WINLOSE);



    ArrayList<Ship> meShips = new ArrayList<>();
    ViewGroup BattleView;

    ArrayList<Integer> squaresShot;

    boolean ShipsPopulated;

    int TotalShipSqaures = 14;

    Context CurrentContext;
    ImageView viewShoot;
    Connector conn;


    int CurrentShot;

    Handler battleHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int code = msg.what;

            switch (code)
            {
                case Handle_Constants.FIRE:

                    OnFire(Integer.parseInt( String.valueOf(msg.obj)) , meShips);
                    break;
                case Handle_Constants.MISS:

                    Missed();
                    break;
                case Handle_Constants.HIT:

                    Hit();
                    break;

                case Handle_Constants.GAME_OVER:
                    GameOver(String.valueOf(msg.obj));

                    break;
            }
        }
    };


    public View.OnTouchListener listener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int[] location = new int[2];

            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {

                for (int i = 100; i < 200; i++) {
                    View rectangle = findViewById(i);

                    rectangle.getLocationOnScreen(location);

                    int left = location[0];
                    int right = left + rectangle.getWidth();
                    int top = location[1];
                    int bottom = top + rectangle.getHeight();

                    // get me the square number which was fired at
                    if (StaticMethods.isPointWithin(event.getRawX(), event.getRawY(),
                            left, right, top, bottom))
                    {
                        if(squaresShot.contains(i))
                        {
                            Toast.makeText(CurrentContext, "Try new position", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Fire(i);
                            squaresShot.add(i);
                            break;
                        }
                    }
                }
            }
            return true;
        }


    };


    protected void CreateLayout()
    {
        BattleView =(ViewGroup) findViewById(R.id.BattleView);

        BattleView.setBackgroundColor(Color.rgb(40,40,45));

        CenterLineView centerLine = new CenterLineView(this);

        TextView enemy = (TextView) findViewById(R.id.txtEnemy);
        TextView you = (TextView) findViewById(R.id.txtYou);

        you.setRotation(90);
        enemy.setRotation(90);

        you.setX(600);
        you.setY(250);
        you.setTextColor(Color.rgb(131,28,28));

        enemy.setX(570);
        enemy.setY(900);
        enemy.setTextColor(Color.rgb(131,28,28));

        BattleView.addView(centerLine);

        subViewGroupMe = new SubView(this);
        subViewGroupEnemy = new SubView(this);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(GRID_SIZE,GRID_SIZE);
        layoutParams.leftMargin = 20;
        layoutParams.rightMargin = 20;
        layoutParams.topMargin= 2;
        subViewGroupMe.setLayoutParams(layoutParams);


        RelativeLayout.LayoutParams EnemylayoutParams =
                new RelativeLayout.LayoutParams(GRID_SIZE,GRID_SIZE);
        EnemylayoutParams.leftMargin = 20;
        EnemylayoutParams.rightMargin = 20;
        EnemylayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM );

        subViewGroupEnemy.setLayoutParams(EnemylayoutParams);

        PopulateViewRectangles(subViewGroupMe, new RelativeLayout.LayoutParams(RECTANGLE_SIZE, RECTANGLE_SIZE)   , null , 0);
        PopulateViewRectangles(subViewGroupEnemy,new RelativeLayout.LayoutParams(RECTANGLE_SIZE, RECTANGLE_SIZE) , this.listener, 100);

        BattleView.addView(subViewGroupMe);
        BattleView.addView(subViewGroupEnemy);

        you.bringToFront();
        enemy.bringToFront();
        you.invalidate();
        enemy.invalidate();

        you.requestLayout();
        enemy.requestLayout();


    }



    protected void MarkShot(boolean hit ,int x , int y )
    {
        ImageView img = new ImageView(this);
        img.setRotation(90);

        img.setX(x - 32);
        img.setY(y  - 29);

        if(hit)
        {
            img.setImageResource(R.drawable.transparentfire);
        }
        else
        {
            img.setImageResource(R.drawable.bomb);
        }

        BattleView.addView(img , RECTANGLE_SIZE, RECTANGLE_SIZE);

    }


    protected void Hit()
    {
        int[] location  = new int[2];

        View square =  findViewById(CurrentShot);

        square.getLocationOnScreen(location);

        MarkShot(true , location[0] , location[1]);
    }

    protected void Missed()
    {
        int[] location  = new int[2];

        View square =  findViewById(CurrentShot);

        square.getLocationOnScreen(location);

        MarkShot(false , location[0] , location[1]);
    }


    int GRID_SIZE = 580;
    int RECTANGLE_SIZE = 58;

    SubView subViewGroupMe;
    SubView subViewGroupEnemy;

    //IF YOU EVER GET ALL SHIPS NOT SHOWING THIS FUNCTION CHANGE TO 5
    protected void GetPassedCoordinates(Bundle extras )
    {
        //change this 1 to 5
        for (int i = 0 ; i < 5 ; i++)
        {
            String value = extras.getString("Ship"+i);
            Ship mShip = new Ship(this);
            mShip.OccupiedSqaures = SquareParser(value);

            switch(mShip.OccupiedSqaures.size())
            {
                case 2:
                    mShip.setImageResource(R.drawable.boattwo);
                    break;
                case 3:
                    mShip.setImageResource(R.drawable.boatthree);
                    break;
                case 4:
                    mShip.setImageResource(R.drawable.boatfour);
                    break;
            }

            meShips.add(mShip);
        }
    }


    protected void PopulateViewWithShips(ArrayList<Ship> ships)
    {
        int[] location = new int[2];

        for(Ship ship : ships)
        {

            View rectangle = findViewById(ship.OccupiedSqaures.get(0));
            rectangle.getLocationOnScreen(location);
            //Not rotated
            if(ship.OccupiedSqaures.get(0) + 1 == ship.OccupiedSqaures.get(1))
            {
                ship.setRotation(90);
                switch(ship.OccupiedSqaures.size())
                {
                    case 2:
                        ship.setY(location[1]  - 52);
                        ship.setX(location[0]  - 32);

                        break;
                    case 3:
                        ship.setY(location[1]  - 92);
                        ship.setX(location[0] -  2 );
                        break;
                    case 4:
                        ship.setY(location[1]  - 122);
                        ship.setX(location[0] +  30 );

                }

            }
            else // Rotated
            {
                ship.setX(location[0] - 52 );
                ship.setY(location[1] - 32 );
            }

            switch(ship.OccupiedSqaures.size())
            {

                case 2:
                    BattleView.addView(ship , 100 , 100);
                    break;
                case 3:
                    BattleView.addView(ship , 100, 175);
                    break;

                case 4:
                    BattleView.addView(ship , 100 , 230);
                    break;
            }

            ship.getLocationOnScreen(location);
            Log.d("345", String.valueOf(location[0]) + String.valueOf(location[1]));
        }
        ShipsPopulated = true;

    }



    protected void PopulateViewRectangles(ViewGroup viewGroup , RelativeLayout.LayoutParams mlayoutParam,
                                        View.OnTouchListener listener , int Curcounter)
    {

        int counter = Curcounter;
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j <10 ; j++)
            {
                Rectangle rectangle = new Rectangle(this);
                rectangle.setId(counter);
                rectangle.setLayoutParams(mlayoutParam);
                rectangle.setOnTouchListener(listener);

                counter++;
                rectangle.setBackgroundColor(Color.rgb(40, 40, 45));
                viewGroup.addView(rectangle);

            }
        }


    }



    protected ArrayList<Integer> SquareParser(String values)
    {
        ArrayList<Integer> myINts = new ArrayList<>();

        String[] meVals= values.split(",");

        for(String val : meVals)
        {
            myINts.add(Integer.valueOf(val));
        }
        return  myINts;
    }

}
