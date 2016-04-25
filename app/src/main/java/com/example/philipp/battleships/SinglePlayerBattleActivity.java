package com.example.philipp.battleships;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import BattleShip_AI.AI;
import BattleShip_AI.AI_TargetMode;
import Constant_Package.Constant_BT_Messages;
import Custom_views.Rectangle;
import Custom_views.Ship;
import Custom_views.StaticMethods;
import Main_Connector.MessageBridge;

/**
 * Created by Philipp on 13/04/2015.
 */
public class SinglePlayerBattleActivity extends  BattleAbstract {

    AI enemy ;
    int BestAI_Shot =22;
    int MyShipCount = 14;
        boolean Target_Mode_On = false;
    ArrayList<Integer> square_Edges;
    ArrayList<Integer> MissedSquares;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        Init();
        GetPassedCoordinates(extras);


        setContentView(R.layout.activity_battle);
        CreateLayout();
        enemy = new AI(this);
    }

    @Override
    void OnFire(int square, ArrayList<Ship> ships)
    {
        ArrayList<Integer> squares;


        int[] location = new int[2];
        View shotSquare = findViewById(square);
        shotSquare.getLocationOnScreen(location);

        for(Ship ship : ships)
        {
            squares = ship.OccupiedSqaures;

            for (int i = 0; i < squares.size(); i++)
            {
                if(squares.get(i) == square)
                {
                    TotalShipSqaures -= 1;
                    MarkShot(true , location[0] , location[1]);

                    return;
                }

            }
        }

        MarkShot(false , location[0] , location[1]);


    }
    @Override
    void Fire(int square)
    {
        StaticMethods.disableEnableControls(false, subViewGroupEnemy);
        CurrentShot = square;
        TotalShipSqaures = enemy.getAiShipCount();
        OnFire(square , enemy.AIShips);
        enemy.setAiShipCount(TotalShipSqaures);

        if(enemy.getAiShipCount() == 0)
        {
            GameOver(Constant_BT_Messages.ENEMY_LOSE);
        }

        AIMove();

        if(MyShipCount == 0)
        {
            GameOver(Constant_BT_Messages.ENEMY_WIN);
        }
        StaticMethods.disableEnableControls(true, subViewGroupEnemy);

    }


    private void AIMove()
    {

        TotalShipSqaures =  MyShipCount;
        CurrentShot = BestAI_Shot = GetBestProbability();

        OnFire(BestAI_Shot , meShips);

        Rectangle rectangle = (Rectangle) findViewById(BestAI_Shot);

        if(Target_Mode_On)
        {
            if(MyShipCount > TotalShipSqaures)//We hit something
            {

                rectangle.setHit(true);
            }else{
                rectangle.setMiss(true);

                MissedSquares.add(rectangle.getId());
            }

            TargetMode(BestAI_Shot);
            return;
        }


        if(MyShipCount > TotalShipSqaures)//We hit something
        {
            rectangle.setHit(true);
            TargetMode(BestAI_Shot);


            MyShipCount = TotalShipSqaures;
            return;
        }else                               //We missed
        {

            rectangle.setMiss(true);
            MissedSquares.add(rectangle.getId());
        }


        AIResetProbabilities();
        MyShipCount = TotalShipSqaures;

    }
    AI_TargetMode ai_targetMode;
    ArrayList<Integer> HighProbSquares;

    private void TargetMode(int Hitsquare)
    {

        if(!Target_Mode_On) {
            ai_targetMode = new AI_TargetMode(Hitsquare);
            ai_targetMode.setMisses(MissedSquares);

            HighProbSquares = ai_targetMode.PopulateInitialDeductions(square_Edges);

            Target_Mode_On = true;

        }else
        {
            ai_targetMode.setMisses(MissedSquares);

            HighProbSquares = ai_targetMode.ContinueTargetMode(Hitsquare, MyShipCount > TotalShipSqaures, meShips);
            MyShipCount = TotalShipSqaures;
            if (HighProbSquares.isEmpty()) // we sank the ship
            {
                meShips = ai_targetMode.getShipsLeft();
                Target_Mode_On = false;
                return;
            }
        }

        if(!HighProbSquares.isEmpty() ) {
            IncreaseProbabilities(HighProbSquares);
        }



    }


    private void IncreaseProbabilities(ArrayList<Integer> squareIds)
    {
        AIResetProbabilities();

        for (int id : squareIds)
        {
                Rectangle rect = (Rectangle) findViewById(id);
                rect.setProbability(rect.getProbability()  + 20);
                UpdateLabel(id);
            }

    }



    private void UpdateLabel(int rectId )
    {
           Rectangle rect =  (Rectangle) findViewById(rectId);

            TextView label = (TextView) findViewById(rectId + 500);
            label.setText(String.valueOf(rect.getProbability()));
            //label.postInvalidate();
    }



    private int GetBestProbability()
    {
            int HighestProb =0 ;
        int counter = 0;
        int HighestSquare =0;
        for(int i = 0; i < 10; i++)
        {

            for(int j = 0; j <10 ; j++)
            {
                Rectangle rectangle = (Rectangle) findViewById(counter);
                   if(HighestProb < rectangle.getProbability() && !rectangle.isMiss() && !rectangle.isHit())
                   {
                       HighestProb = rectangle.getProbability();
                       HighestSquare = counter;
                   }

                counter ++;
            }

        }

        return HighestSquare;

    }

    private void AIResetProbabilities()
    {
        int BestProbability = 0;
        int Edge=9;

        int counter = 0;
        for(int i = 0; i < 10; i++) {
                    square_Edges.add(Edge);
            for(int j = 0; j <10 ; j++) {

                Rectangle rectangle = (Rectangle) findViewById(counter);
                rectangle.setProbability(0);

                if (!rectangle.isMiss() || !rectangle.isHit()) // if rectangle is undiscovered
                {
                    //AliveShipsLeft
                    for (int a = 0; a < meShips.size(); a++)
                    {

                        int shipSquares = meShips.get(a).OccupiedSqaures.size();

                            if (CheckShipFitsHorizontally(shipSquares, counter, Edge)) {
                                rectangle.IncrementProbability();
                            }

                            if (CheckShipFitsHorizontallyReversed(shipSquares, counter, Edge)) {
                                rectangle.IncrementProbability();
                            }

                            if (CheckShipFitsVertically(shipSquares, counter)) {
                                rectangle.IncrementProbability();
                            }

                            if (CheckShipFitsVerticallyReversed(shipSquares, counter)) {
                                rectangle.IncrementProbability();
                            }
                        }

                        if (BestProbability < rectangle.getProbability()) {
                            BestProbability = rectangle.getProbability();

                            BestAI_Shot = rectangle.getId();
                        }


                        TextView prob = (TextView) findViewById(rectangle.getId() + 500);

                        prob.setText(String.valueOf(rectangle.getProbability()));

                    }

                    counter++;

                }

            Edge +=10;
        }


    }

    public void GenerateLabels()
    {

        for (int i = 0 ; i < 100; i++)
        {

            TextView prob = new TextView(this);
            prob.setId(i + 500);
            BattleView.addView(prob);
            Rectangle rect =(Rectangle) findViewById(i);
            int[] location = new int[2];

            rect.getLocationOnScreen(location);
            prob.setRotation(90);
            prob.setX(location[0] - 20);
            prob.setY(location[1] - 20);
            prob.setTextColor(Color.RED);
        }

    }

    public void HideLabels()
    {

        for (int i = 0 ; i < 100; i++)
        {
            TextView prob = (TextView)findViewById(i+500);
            prob.setVisibility(View.INVISIBLE);
        }

    }




    private boolean CheckShipFitsHorizontally(int ShipSquares, int counter, int Edge )
    {
            //Horizontally

        for(int shipLength = 0; shipLength < ShipSquares; shipLength++)
        {

            Rectangle rectangle = (Rectangle) findViewById(counter + shipLength);

            if (rectangle == null)
            {return false;}
            // if next rectangle is a miss or a hit   ||  rectangle.isHit()
            int CheckEdges = counter + shipLength;


            if(CheckEdges > Edge)
            {
                return false;
            }

            if(rectangle.isMiss() || rectangle.isHit())
            {
                return false;
            }

        }
        return true;
    }




    private boolean CheckShipFitsHorizontallyReversed(int ShipSquares, int counter, int Edge )
    {
        //Horizontally

        for(int shipLength = 0; shipLength > ShipSquares * -1; shipLength--)
        {

            Rectangle rectangle = (Rectangle) findViewById(counter + shipLength);

            if (rectangle == null)
            {return false;}
            // if next rectangle is a miss or a hit   ||  rectangle.isHit()
            int CheckEdges = counter + shipLength;

            if(CheckEdges < Edge -9)
            {
                return false;
            }

            if(rectangle.isMiss() || rectangle.isHit() )
            {

                return false;

            }

        }
        return true;
    }


    @Override
    protected void GameOver(String WINLOSE)
    {
        Intent gameOverActivity = new Intent(this , GameOverSinglePlayer.class );

        gameOverActivity.putExtra("GAME_OVER" , WINLOSE);

        startActivity(gameOverActivity);


    }




    private boolean CheckShipFitsVertically(int ShipSquares, int counter)
    {
       ShipSquares *=10;

        for(int shipLength = 0; shipLength < ShipSquares; shipLength +=10)
        {

            Rectangle rectangle = (Rectangle) findViewById(counter + shipLength);
            if (rectangle == null)
            {return false;}
            // if next rectangle is a miss or a hit  ||  rectangle.isHit()
            int CheckEdges = counter +shipLength;
            if(rectangle.isMiss() || CheckEdges >= 100 || rectangle.isHit())
            {

                return false;

            }

        }

        return true;
    }


    private boolean CheckShipFitsVerticallyReversed(int ShipSquares, int counter)
    {
        ShipSquares *=-10;

        for(int shipLength = 0; shipLength > ShipSquares; shipLength -=10)
        {

            Rectangle rectangle = (Rectangle) findViewById(counter + shipLength);
            if (rectangle == null)
            {return false;}
            // if next rectangle is a miss or a hit  ||  rectangle.isHit()
            int CheckEdges = counter + shipLength;
            if(rectangle.isMiss() || CheckEdges >= 100 || rectangle.isHit())
            {

                return false;

            }

        }

        return true;
    }



    @Override
    void Init()
    {
        viewShoot = new ImageView(this);
        CurrentContext = this;
        ShipsPopulated = false;
        square_Edges = new ArrayList<>();

        MessageBridge.SetHandler(battleHandler);

        squaresShot = new ArrayList<>();
        MissedSquares = new ArrayList<>();
    }

    void HideEnemy()
    {

        for(Ship ship : enemy.AIShips)
        {
            ship.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(!ShipsPopulated) {
            PopulateViewWithShips(meShips);
            PopulateViewWithShips(enemy.AIShips);

           HideEnemy();

            GenerateLabels();
            AIResetProbabilities();
            //HideLabels();
        }

    }




}
