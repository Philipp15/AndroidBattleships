package Bluetooth_Library;

import android.util.Log;

import java.nio.charset.Charset;

/**
 * Created by Philipp on 10/02/2015.
 */
public class Text_Codec
{
    public static byte[] EncodeInUTF16(String message)
    {
        byte[] myencoded = null;

        try {
            Log.d("ME", message);
            myencoded = message.getBytes(Charset.forName("UTF-16"));

        }catch(Exception e)
        {
            Log.d("txt_CODEC","wrong charset data");
        }
        return myencoded == null ? new byte[]{} : myencoded;
    }

    public static String DecodeInUTF16(byte[] encoded)
    {
        String output = "";
        try
        {
            output = new String(encoded, Charset.forName("UTF-16"));
        } catch(Exception e)
        {
            Log.d("txt_CODEC","wrong charset data");
        }
        return AvoidNullTermination(output);
    }


    private static String AvoidNullTermination(String input)
    {
        if(input.contains("\u0000")) {
            return input.substring(0, input.indexOf('\u0000'));
        }

        return input;
    }

}
