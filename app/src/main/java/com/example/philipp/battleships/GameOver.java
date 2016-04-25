package com.example.philipp.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import Constant_Package.Constant_BT_Messages;
import Constant_Package.Handle_Constants;
import Main_Connector.Connector;
import Main_Connector.MessageBridge;
import Main_Connector.StaticConnector;


public class GameOver extends ActionBarActivity {

    boolean opponentPlayAgain = false;
    boolean mePlayAgain = false;

Activity CurrentActivity ;

    Handler GameOverHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int code = msg.what;

            switch (code)
            {
                case Handle_Constants.TRY_AGAIN:
                    opponentPlayAgain = true;
                    Toast.makeText(CurrentActivity ,
                            "Opponent wants to play again press try again for one more game", Toast.LENGTH_LONG).show();

                    break;
                case Handle_Constants.EXIT:
                    mainMenu();
                    break;
            }
        }
    };


    Connector conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentActivity = this;
        Init();
        setContentView(R.layout.activity_game_over);

        TextView title = (TextView) findViewById(R.id.GameTitle);

        Bundle extras = getIntent().getExtras();

        switch (extras.getString("GAME_OVER"))
        {
            case Constant_BT_Messages.ENEMY_LOSE:
                title.setText("YOU WIN !!!!");
                break;
            case Constant_BT_Messages. ENEMY_WIN:
                title.setText("YOU LOSE !!!!");
                break;
        }


    }

    private void Init() {

        conn = StaticConnector.GetConnector();
        MessageBridge.SetHandler(GameOverHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over, menu);
        return true;
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

    Handler threadHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Intent PositionShips = new Intent(CurrentActivity, ShipPositionsActivity.class);
            startActivity(PositionShips);

        }
    };
    private void AnotherGame()
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(mePlayAgain && opponentPlayAgain)
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


    public void tryAgain (View view)
    {
       //
        mePlayAgain = true;
        conn.SendMessage(Constant_BT_Messages.RESET_GAME);
        Toast.makeText(this , "Waiting for opponent to confirm" , Toast.LENGTH_LONG).show();
        AnotherGame();

    }

    public void Exit (View view)
    {
        conn.SendMessage(Constant_BT_Messages.EXIT);
        mainMenu();

    }



    private void mainMenu()
    {
//Main menu
        Intent intent = new Intent(this , MainActivity.class);

        startActivity(intent);


    }

}
