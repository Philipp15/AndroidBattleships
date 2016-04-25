package BattleShip_AI;

import java.util.ArrayList;

import Custom_views.Rectangle;
import Custom_views.Ship;

/**
 * Created by Philipp on 15/04/2015.
 */

enum CHECK
{
    CHECK_LEFT,
    CHECK_RIGHT,
    CHECK_TOP,
    CHECK_BOTTOM,
}



public class AI_TargetMode
{
    CHECK lastCheck;
boolean FiringPlaneSet = false;
    int InitialSquare;
    private int NextShot;

    private  boolean shipIsVertical;

    private boolean shipIsHorizontal;

    ArrayList<Integer> InitialDeductions ;

    ArrayList<Integer> Hits;
                private ArrayList<Integer> Misses;
    private ArrayList<Ship> shipsLeft;

    public AI_TargetMode(int initSquareHit)
    {
        Hits = new ArrayList<>();
        Hits.add( initSquareHit);
        InitialSquare = initSquareHit;

        InitialDeductions = new ArrayList<>();
    }


    public ArrayList<Integer> PopulateInitialDeductions(ArrayList<Integer> Edges)
    {
        int edgeMin =0;
        int edgeMax =0;

        for (int edge : Edges) // find appropriate edges
        {
            if (InitialSquare < edge && InitialSquare> edge-9)
            {
                edgeMin = edge - 10;
                edgeMax = edge + 10;
                break;
            }

        }

        InitialDeductions.add(CheckLeftLane());
        InitialDeductions.add(CheckRightLane());

        for (int id : InitialDeductions)
        {
            if(id < edgeMin && id>edgeMax) //Check the edges
            {
               InitialDeductions.remove(id);
            }
        }

        if (CheckBottomLane() < 100)
        {InitialDeductions.add(CheckBottomLane());}

        if(CheckTopLane() > 0)
        {
        InitialDeductions.add(CheckTopLane());
        }



        return InitialDeductions;
    }


    public int CheckLeftLane()
    {
        lastCheck = CHECK.CHECK_LEFT;
        return  InitialSquare - 1;

    }

    public int CheckRightLane()
    {
        lastCheck = CHECK.CHECK_RIGHT;
        return InitialSquare + 1;
    }

    public int CheckTopLane()
    {
        lastCheck = CHECK.CHECK_TOP;
        return InitialSquare - 10;

    }

    public int CheckBottomLane()
    {
        lastCheck = CHECK.CHECK_BOTTOM;
        return InitialSquare + 10;

    }


    private int GoLeft(int square)
    {
        return   square += -1;
    }


    private int GoRight(int square)
    {
        return square += 1;
    }

    private int GoTop(int square)
    {
        return   square += -10;
    }

    private int GoBottom(int square)
    {
        return   square += 10;
    }

        boolean GoingTop;
        boolean GoingBottom;
        boolean GoingRight;
        boolean GoingLeft;


    public ArrayList<Integer> ContinueTargetMode(int NextSquare , boolean Hit, ArrayList<Ship> ships)
        {
            ArrayList<Integer> PotentialProbabilities = new ArrayList<>() ;


                if(Hit) {

                    Hits.add(NextSquare);


                    if (CheckSinks(ships))
                    {

                        shipsLeft = ships;
                        FiringPlaneSet = false;
                        shipIsVertical = false;
                        shipIsHorizontal = false;

                        GoingBottom = false;
                        GoingTop = false;
                        GoingLeft = false;
                        GoingRight = false;

                        return new ArrayList<>();
                    }


                    if (GoingTop) {
                        PotentialProbabilities.add(  GoTop(NextSquare));
                    }

                    if (GoingBottom) {
                        PotentialProbabilities.add(  GoBottom(NextSquare));
                    }

                    if (GoingLeft) {
                        PotentialProbabilities.add(  GoLeft(NextSquare));
                    }

                    if (GoingRight) {
                        PotentialProbabilities.add(  GoRight(NextSquare));
                    }



                    if (!FiringPlaneSet)
                    {
                        // If next square I hit is on vertical plane and one to the top
                        if (NextSquare - InitialSquare == 10) //Vertical
                        {

                            shipIsVertical = true;
                            GoingBottom = true;
                            FiringPlaneSet= true;
                            PotentialProbabilities.add(InitialSquare + 20);
                        }

                        // If next square I hit is on vertical plane and one to the bottom
                        if(NextSquare - InitialSquare == -10)
                        {
                            shipIsVertical = true;
                            GoingTop = true;
                            FiringPlaneSet= true;
                            PotentialProbabilities.add(InitialSquare - 20);
                        }

                        // If next square I hit is on horizontal plane and 1 to the right
                        if(NextSquare - InitialSquare == 1){

                            shipIsHorizontal = true;
                            GoingRight = true;
                            FiringPlaneSet = true;
                            PotentialProbabilities.add(InitialSquare + 2);


                        }
                        // If next square I hit is on horizontal plane and 1 to the left
                        if(NextSquare - InitialSquare == -1)
                        {
                            GoingLeft = true;
                            shipIsHorizontal = true;
                            FiringPlaneSet = true;
                            PotentialProbabilities.add(InitialSquare - 2);
                        }

                    }

                }else
                {

                    //IF there was a miss on the same plane and ships not sank means start next side


                    if(GoingTop) // If I missed going top and ship did not sink yet
                    {
                        PotentialProbabilities.add(  GoBottom(InitialSquare));
                        GoingTop = false;
                        GoingBottom = true;
                    }

                    if(GoingBottom)
                    {
                        PotentialProbabilities.add(    GoTop(InitialSquare));
                        GoingTop = true;
                        GoingBottom =false ;
                    }

                    if(GoingLeft)
                    {
                        PotentialProbabilities.add(  GoRight(InitialSquare));
                        GoingLeft = false;
                        GoingRight = true;

                    }

                    if(GoingRight)
                    {
                        PotentialProbabilities.add(  GoLeft(InitialSquare));
                        GoingLeft = true;
                        GoingRight =false ;
                    }


                    if (!FiringPlaneSet)

                    {


                        if (NextSquare - InitialSquare == -10) //Vertical top
                        {
                            PotentialProbabilities.add(InitialSquare + 1);
                            PotentialProbabilities.add(InitialSquare - 1);
                            PotentialProbabilities.add(InitialSquare + 10);
                        }

                        if (NextSquare - InitialSquare == 10) {
                            PotentialProbabilities.add(InitialSquare + 1);
                            PotentialProbabilities.add(InitialSquare - 1);
                            PotentialProbabilities.add(InitialSquare - 10);

                        }
                        if (NextSquare - InitialSquare == 1) {

                            PotentialProbabilities.add(InitialSquare - 1);
                            PotentialProbabilities.add(InitialSquare - 10);
                            PotentialProbabilities.add(InitialSquare + 10);

                        }

                        if (NextSquare - InitialSquare == -1) {

                            PotentialProbabilities.add(InitialSquare + 1);
                            PotentialProbabilities.add(InitialSquare - 10);
                            PotentialProbabilities.add(InitialSquare + 10);

                        }

                    }
                }

return  PotentialProbabilities;
        }


    public int getNextShot() {
        return NextShot;
    }



    public boolean CheckSinks( ArrayList<Ship> ships)
    {
        boolean destroyed = false;
        int shipIndexToDestroy = 100;
        ArrayList<Ship> shipsLeft = ships;

        for(Ship ship : ships)
        {
            for (int  square =0; square < ship.OccupiedSqaures.size() ; square++)
            {
                if (Hits.contains(ship.OccupiedSqaures.get(square)))
                {
                    destroyed = true;
                }else{
                    destroyed = false; // did not hit all the squares NextShip
                    break;
                }
            }

            if(destroyed)
            {
                shipsLeft.remove(ship);
                return true;
            }

        }



return false;
    }

    public ArrayList<Ship> getShipsLeft() {
        return shipsLeft;
    }

    public void setMisses(ArrayList<Integer> misses) {
        Misses = misses;
    }
}
