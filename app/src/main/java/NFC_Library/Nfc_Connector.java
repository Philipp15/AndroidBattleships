package NFC_Library;
import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Philipp on 10/02/2015.
 */

public class Nfc_Connector
{

    private NfcAdapter mNfcAdapter;
    private Activity ActivityCaller;

    public Nfc_Connector(Activity activity)
    {
        this.ActivityCaller = activity;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
    }


    public void SendMessage(String text)
    {
        Ndef_Messages messageCreator = new Ndef_Messages();

        NdefMessage message= messageCreator.createNdefMessage(text);
        Log.d("a", message.toString());
        getmNfcAdapter().enableForegroundNdefPush(ActivityCaller, message);
        Log.d("a", "msg Sent");
        Toast.makeText(ActivityCaller, "Touch another mobile to share Details",
                Toast.LENGTH_SHORT).show();

    }

    public  boolean NfcOnboardAndEnabled()
    {
        if (getmNfcAdapter() == null && mNfcAdapter.isEnabled())
        {
            return false;
        }
        return true;
    }

    public NfcAdapter getmNfcAdapter() {
        return mNfcAdapter;
    }



}



