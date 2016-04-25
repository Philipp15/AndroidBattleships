package NFC_Library;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by Philipp on 10/02/2015.
 */
public class Ndef_Messages
{
    byte statusByte;
    private String payload ="";

    private NdefMessage[] getNdefMessages(Intent intent)
    {
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            else
            {
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord( NdefRecord.TNF_UNKNOWN, empty, empty, empty );
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
        }
        else {


        }
        return msgs;
    }

    public NdefMessage createNdefMessage(String textToSend)
    {

        Locale locale = new Locale("en","US");
        byte[] langBytes = locale.getLanguage().getBytes( Charset.forName("US-ASCII"));

        boolean encodeInUtf8 = false;

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8"):Charset.forName("UTF-16");

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        byte status = (byte) (utfBit + langBytes.length);
        byte[] textBytes = textToSend.getBytes(utfEncoding);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);

        NdefMessage message= new NdefMessage(new NdefRecord[] { textRecord});
        return message;

    }

    public String processIntent(Intent intent) {

        NdefMessage[] messages = getNdefMessages(intent);
        for (int i = 0; i < messages.length; i++) {
            for (int j = 0; j < messages[0].getRecords().length; j++) {
                NdefRecord record = messages[i].getRecords()[j];
                statusByte = record.getPayload()[0];
                int languageCodeLength = statusByte & 0x3F;
                int isUTF8 = statusByte - languageCodeLength;
                if (isUTF8 == 0x00) {
                    payload = new String(record.getPayload(),
                            1 + languageCodeLength,
                            record.getPayload().length - 1 - languageCodeLength,
                            Charset.forName("UTF-8"));
                } else if (isUTF8 == -0x80) {
                    payload = new String(record.getPayload(),
                            1 + languageCodeLength,
                            record.getPayload().length - 1 - languageCodeLength,
                            Charset.forName("UTF-16"));
                }
               // messageText.setText("Text received: " + payload);
                return payload;

            }


        }
        return "";
    }


}
