package Main_Connector;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.philipp.battleships.ShipPositionsActivity;

import Bluetooth_Library.Bluetooth_Connector;
import Bluetooth_Library.ConnectionManager;
import Bluetooth_Library.Text_Codec;
import Constant_Package.Handle_Constants;
import NFC_Library.Ndef_Messages;
import NFC_Library.Nfc_Connector;

/**
 * Created by Philipp on 10/02/2015.
 */

//  This class is a bridge between NFC and Bluetooth Connectors
public class Connector {
    public Bluetooth_Connector btConn;
    public Nfc_Connector nfcConnector;
    Ndef_Messages ndefMessage;
    private Activity CurrentActivity;
    private ConnectionManager connectionManager;


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int code = msg.what;

            switch (code) {
                case Handle_Constants.MESSAGE_READ:
                {
                    String text = "";
                    try {
                        text =  Text_Codec.DecodeInUTF16((byte[]) (msg.obj));
                        Log.d("ME ","Decoded text: " + text);

                        MessageBridge.SetCurrentMessage(text);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Main connector","Decoder broken");
                    }
                    break;
                }
                case Handle_Constants.PEERS_CONNECTED:
                {
                    nfcConnector.getmNfcAdapter().disableForegroundNdefPush(getCurrentActivity());
                    btConn.getMbtAdapter().cancelDiscovery();
                    Log.d("ME","CONNECTED NOW");
                    BluetoothSocket connectedSocket = (BluetoothSocket)msg.obj; //get connected socket

                    connectionManager = new ConnectionManager(connectedSocket, getMyHandler());
                    connectionManager.start();
                    Intent ShipPositionsActiv = new Intent(CurrentActivity , ShipPositionsActivity.class);
                    CurrentActivity.startActivity(ShipPositionsActiv);

                }

            }
        }
    };

    public Connector(Activity activity) {
        CurrentActivity = activity;
        btConn = new Bluetooth_Connector(this);
        nfcConnector = new Nfc_Connector(getCurrentActivity());
        ndefMessage = new Ndef_Messages();
        this.connectionManager = null;
    }

    // If I send my MAC
    public void InitiateConnection() {
        if (nfcConnector.NfcOnboardAndEnabled()) {
            String MAC_address = btConn.GetThisMacAddress(); // Get my mac
            nfcConnector.SendMessage(MAC_address);  // send to touched mobile via nfc
        }

        btConn.Initiate_Server();// open socket
    }

    public void ResumeNfc(Intent intent)
    {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

                ndefMessage.processIntent(intent);
        }

    }
    // If I recieve a MAC try to connect as client
    public void ProcessIncomingNFC_msg(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {


            String MAC_address = ndefMessage.processIntent(intent);

            try {
                btConn.Connect_To_Remote_Device(MAC_address);
            } catch (Exception e) {
                Log.d("ME", "Client failed to connect");
            }
        }
    }

    public void SendMessage(String msg)
    {
        if(getConnectionManager() != null)
        {
            btConn.SendMessage(msg, getConnectionManager());
        }
    }


    public Activity getCurrentActivity() {
        return CurrentActivity;
    }

    public Handler getMyHandler() {
        return myHandler;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
