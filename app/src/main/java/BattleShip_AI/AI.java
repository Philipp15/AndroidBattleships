package BattleShip_AI;

import android.content.Context;

import com.example.philipp.battleships.R;

import java.util.ArrayList;
import java.util.Random;

import Custom_views.Rectangle;
import Custom_views.Ship;

/**
 * Created by Philipp on 15/04/2015.
 */
public class AI
{

   public ArrayList<Ship> AIShips = new ArrayList<>();
    ArrayList<Integer> attackedSquares = new ArrayList<>();
    public int aiShipCount = 14;
    Context CurrentActivity;
    int CurrentShot;
    int NextShot;

    boolean targetModeOn = false;

    ArrayList<Integer> squaresUsed = new ArrayList<>();


    public AI(Context TheCalling)
    {
        CurrentActivity = TheCalling;
        Init();
    }



    public void Init()
    {
        AIPositionShips(randInt(1,2) == 1 ? true : false , 4 );

        AIPositionShips(randInt(1,2) == 1 ? true : false , 3 );

        AIPositionShips(randInt(1,2) == 1 ? true : false , 3 );

        AIPositionShips(randInt(1,2) == 1 ? true : false , 2 );

        AIPositionShips(randInt(1,2) == 1 ? true : false , 2 );
    }


    public  int randInt(int min, int max)
    {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;


    }



    public  int randSquare(int min, int max, boolean Rotated,  int numberofSquares)
    {

        Random rand = new Random();
        while(true)
        {
            int randomNum = rand.nextInt((max - min) + 1) + min;

            if(!squaresUsed.contains(randomNum)  )
            {
                if(!Rotated)
                {
                    if(!squaresUsed.contains(randomNum+10) && !squaresUsed.contains(randomNum+20) &&
                            !squaresUsed.contains(randomNum+30)  )
                    {
                        squaresUsed.add(randomNum);
                        return randomNum;
                    }

                }else
                {
                    if(!squaresUsed.contains(randomNum+1) && !squaresUsed.contains(randomNum+2) &&
                            !squaresUsed.contains(randomNum+3) && randomNum % 10 < 9 - numberofSquares )
                    {
                        squaresUsed.add(randomNum);
                        return randomNum;
                    }
                }

            }

        }

    }



    private void AIPositionShips(boolean Rotated, int SquareNumber)
    {

        int square;

        Ship ship = new Ship(CurrentActivity );

        if(Rotated)
        {
            int max = 200 - SquareNumber * 10;

            square= randSquare(100 ,max, !Rotated, SquareNumber); // Not to go over edges

            for(int i = square; i<square+SquareNumber*10; i+=10)
            {
                squaresUsed.add(i);

                ship.OccupiedSqaures.add(i);
            }

        }
        else
        {
            int max = 200 - SquareNumber ;

            square= randSquare(100 ,  max , !Rotated, SquareNumber); // Not to go over edges



            for(int i = square ; i<square+SquareNumber ; i++)
            {
                squaresUsed.add(i);
                ship.OccupiedSqaures.add(i);
            }
        }

        switch(ship.OccupiedSqaures.size())
        {
            case 2:
                ship.setImageResource(R.drawable.boattwo);
                break;
            case 3:
                ship.setImageResource(R.drawable.boatthree);
                break;
            case 4:
                ship.setImageResource(R.drawable.boatfour);
                break;
        }

        AIShips.add(ship);
    }


    public void setAiShipCount(int aiShipCount) {
        this.aiShipCount = aiShipCount;
    }

    public int getAiShipCount() {
        return aiShipCount;
    }
}