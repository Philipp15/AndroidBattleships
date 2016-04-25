package Bluetooth_Library;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import Constant_Package.Handle_Constants;
import Constant_Package.MY_UUID;

/**
 * Created by Philipp on 10/02/2015.
 */
//Server Thread
public class AcceptThread extends  Thread
{
    private final BluetoothServerSocket mmServerSocket;
    private final Handler mHandler;

    public AcceptThread(BluetoothAdapter adapter, String applicationName, Handler handler)
    {
        this.setName("Server Thread");
        Log.d("ME", "in server constructor");
        BluetoothServerSocket tmp = null;
        this.mHandler = handler;

        try {
            // MY_UUID is the app's UUID string, also used by the server code
           tmp = adapter.listenUsingRfcommWithServiceRecord(applicationName, MY_UUID.Get_Local_UUID()); //Open Bluetooth server Socket
          //  tmp = adapter.
        }
        catch (IOException e)
        {
            Log.d("Bluetooth Con Exception", e.toString());
        }
        mmServerSocket = tmp;
    }


    public void run()
    {
        BluetoothSocket socket = null;

        while (true) {
            try {
                //Timeout after discovery is not active

                int x = Handle_Constants.ServerSocketAcceptTime;
                Log.d("ME","Sec timeout : " + String.valueOf(x*1000));
                socket = mmServerSocket.accept(x*1000); // milliseconds
                Log.d("ME","Conn Accepted");
                mHandler.obtainMessage(Handle_Constants.PEERS_CONNECTED, socket ).sendToTarget();

                //    socket.
                //Log.d
            } catch (IOException e) {
                Log.d("ME",e.getMessage());

                break;
            }
            // If a connection was accepted
            if (socket != null) {

            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
