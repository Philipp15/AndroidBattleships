package com.example.philipp.battleships;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import Main_Connector.Connector;
import Main_Connector.StaticConnector;


public class NFC_Option extends ActionBarActivity {

    Connector conn;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc__option);
        conn = new Connector(this);
        StaticConnector.SaveConnector(conn);

    }

    //for incoming
    private void processIntent(Intent intent)
    {
        conn.ProcessIncomingNFC_msg(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        conn.ProcessIncomingNFC_msg(getIntent());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfc__option, menu);
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

    public void btnNfcConnect(View view)
    {
        try
        {
            conn.InitiateConnection();


        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Could not initiate a connection", Toast.LENGTH_LONG);
            Log.d("ME", e.getMessage());
        }

    }



}
