package Bluetooth_Library;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Constant_Package.Handle_Constants;

/**
 * Created by Philipp on 10/02/2015.
 */
public class ConnectionManager extends Thread
{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler mHandler;


    public ConnectionManager(BluetoothSocket Socket, Handler handle)
    {
        this.mmSocket = Socket;
        this.mHandler = handle;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;


        try {
            tmpIn = Socket.getInputStream();
            tmpOut = Socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(Handle_Constants.MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
                buffer = new byte[1024];
            } catch (IOException e) {
                break;
            }

        }
    }
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }


}
