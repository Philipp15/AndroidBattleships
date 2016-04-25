package com.example.philipp.battleships;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Constant_Package.Constant_BT_Messages;
import Custom_views.CenterLineView;
import Custom_views.Ship;
import Custom_views.StaticMethods;
import Custom_views.SubView;
import Main_Connector.MessageBridge;
import Main_Connector.StaticConnector;


public class BattleActivity extends BattleAbstract
{

    public void Fire(int square )
    {
        CurrentShot = square ;

        conn.SendMessage(String.valueOf(square - 100));

        StaticMethods.disableEnableControls(false, subViewGroupEnemy);

    }

    @Override
    protected void GameOver(String WINLOSE)
    {
        Intent gameOverActivity = new Intent(this , GameOver.class );

        gameOverActivity.putExtra("GAME_OVER" , WINLOSE);

        startActivity(gameOverActivity);


    }



    @Override
    public void OnFire(int square,  ArrayList<Ship> ships)
    {
        ArrayList<Integer> squares;


        int[] location = new int[2];
        View shotSquare = findViewById(square );
        shotSquare.getLocationOnScreen(location);
        for(Ship ship : ships)
        {
            squares = ship.OccupiedSqaures;


            for (int i = 0; i < squares.size(); i++)
            {
                if(squares.get(i) == square)
                {
                    TotalShipSqaures -= 1;
                    conn.SendMessage(Constant_BT_Messages.positionHit);
                    MarkShot(true , location[0] , location[1]);
                    StaticMethods.disableEnableControls(true, subViewGroupEnemy);

                    if(TotalShipSqaures == 0)
                    {
                        conn.SendMessage(Constant_BT_Messages.ENEMY_LOSE);
                        GameOver(Constant_BT_Messages.ENEMY_WIN);

                    }

                    return;
                }

            }
        }

        conn.SendMessage(Constant_BT_Messages.positionMissed);
        MarkShot(false , location[0] , location[1]);
        StaticMethods.disableEnableControls(true, subViewGroupEnemy);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        Init();

        GetPassedCoordinates(extras);

        setContentView(R.layout.activity_battle);

        CreateLayout();
    }
@Override
     void Init()
    {
        viewShoot = new ImageView(this);
        CurrentContext = this;
        ShipsPopulated = false;
        conn = StaticConnector.GetConnector();
        MessageBridge.SetHandler(battleHandler);

        squaresShot = new ArrayList<>();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(!ShipsPopulated) {
            PopulateViewWithShips(meShips);
        }

    }



}
