package Bluetooth_Library;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import Constant_Package.Handle_Constants;
import Constant_Package.MY_UUID;

/**
 * Created by Philipp on 10/02/2015.
 */
//Client code
public class ConnectThread  extends Thread {

    private final BluetoothSocket mmSocket;
    private BluetoothAdapter btAdapter;
    private Handler mHandler;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Handler mHandler)
    {
        this.setName("Client Thread");
        Log.d("ME", "We are in run constructor");
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        this.btAdapter = bluetoothAdapter;
        this.mHandler = mHandler;
        BluetoothSocket tmp = null;

        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID.Get_Local_UUID());
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run()
    {
        Log.d("ME", "We are in Client thread in run ");

        // Cancel discovery because it will slow down the connection
        btAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            // if connected notify the handler
            mHandler.obtainMessage(Handle_Constants.PEERS_CONNECTED,-1, -1, mmSocket).sendToTarget();
        } catch (IOException connectException) {
            Log.d("ME",connectException.getMessage());
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}