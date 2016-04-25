package com.example.philipp.battleships;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import Constant_Package.Constant_BT_Messages;


public class GameOverSinglePlayer extends ActionBarActivity {

    Activity CurrentActivity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_single_player);
        CurrentActivity = this;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over_single_player, menu);
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





    public void tryAgain (View view)
    {
        Intent newSinglePlayer = new Intent(this , ShipPositionSinglePlayerActivity.class);

        startActivity(newSinglePlayer);

    }

    public void Exit (View view)
    {

        mainMenu();

    }



    private void mainMenu()
    {
//Main menu
        Intent intent = new Intent(this , MainActivity.class);

        startActivity(intent);


    }

}
