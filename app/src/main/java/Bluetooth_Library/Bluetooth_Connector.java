package Bluetooth_Library;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

import Constant_Package.Handle_Constants;
import Main_Connector.Connector;

/**
 * Created by Philipp on 10/02/2015.
 */
public class Bluetooth_Connector
{
    private BluetoothAdapter mbtAdapter;
    Activity currentActivity;
    Connector MainConnector;

    public Bluetooth_Connector(Connector connector)
    {
        mbtAdapter = BluetoothAdapter.getDefaultAdapter();

        if(getMbtAdapter() == null)
        {throw new  UnsupportedOperationException("No bluetooth device was found");}

        if(!getMbtAdapter().isEnabled())
        {
            getMbtAdapter().enable();
        }
        MainConnector = connector;
        this.currentActivity = connector.getCurrentActivity();
    }


    public String GetThisMacAddress()
    {
        return getMbtAdapter().getAddress();
    }

    public void Initiate_Server( )
    {
        String appName = currentActivity.getApplicationContext().
                            getString(currentActivity.getApplicationContext().getApplicationInfo().labelRes);

        currentActivity.startActivityForResult(Bluetooth_DiscoverMe(), Handle_Constants.REQUEST_ENABLE_DISCOVERY);
        Log.d("ME","In OPen");

        AcceptThread ServerThread = new AcceptThread(getMbtAdapter(),appName,MainConnector.getMyHandler());
        ServerThread.start();

    }

    public void  Connect_To_Remote_Device(String MAC_address) throws IOException
    {
      BluetoothDevice Remote_device =  getMbtAdapter().getRemoteDevice(MAC_address);

       ConnectThread connThread = new ConnectThread(Remote_device, getMbtAdapter(),MainConnector.getMyHandler());
       connThread.start(); // Start thread
    }

    public void SendMessage(String msg, ConnectionManager connMan)
    {
        if(MainConnector.getConnectionManager() == null)
        {
            Log.d("ME" , "Not connected");
            return;
        }
        byte[] encodedMsg = Text_Codec.EncodeInUTF16(msg);

        MainConnector.getConnectionManager().write(encodedMsg);

    }


    public  Intent Bluetooth_DiscoverMe()
    {
        Intent discoverableIntent = new  Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, Handle_Constants.ServerSocketAcceptTime);
        return discoverableIntent;
    }

    public BluetoothAdapter getMbtAdapter() {
        return mbtAdapter;
    }
}

