package com.example.philipp.battleships;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import Constant_Package.Constant_BT_Messages;
import Main_Connector.MessageBridge;

/**
 * Created by Philipp on 13/04/2015.
 */
public class ShipPositionSinglePlayerActivity extends ShipPositionsAbstract {
    @Override
    void ContinueToNextScreen()
    {
        startActivity(FightWindow);


    }

    @Override
    void Init() {
        MessageBridge.SetHandler(ShipPositionHandler);

        thisClass = this;
        takenSqaures = new ArrayList<>();
        displayedShips = new ArrayList<>();
        TextView txtPos = (TextView) findViewById(R.id.posShip);
        Button btnAccept = (Button) findViewById(R.id.btnContinue);

        txtPos.setRotation(90);
        txtPos.setY(1000);
        txtPos.setX(450);
        txtPos.setTextColor(Color.rgb(131, 28, 28));
        txtPos.setTextSize(20);

        btnAccept.setRotation(90);
        btnAccept.setY(850);
        btnAccept.setX(-50);

        MainView = (ViewGroup) findViewById(R.id.MainView);

        MainView.setBackgroundColor(Color.rgb(40,40,45));

    }

    @Override
    void SetNextActivity() {
        FightWindow = new Intent(this , SinglePlayerBattleActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_positions);

        Init();

        SetLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void btnAccept(View view)
    {
        AcceptFunction();

    }








}
