package Main_Connector;

import android.os.Handler;

import Constant_Package.Constant_BT_Messages;
import Constant_Package.Handle_Constants;

/**
 * Created by Philipp on 12/04/2015.
 */
public class MessageBridge
{
        static String currentMessage;
        static Handler myHandler;

        public static void SetHandler(Handler handler)
        {
          myHandler = handler;
        }

        public static void SetCurrentMessage(String message)
        {
                currentMessage = message;
            if(myHandler != null)
            {


                switch (message)
                {
                    case Constant_BT_Messages.positionsSet:
                        myHandler.obtainMessage(Handle_Constants.
                                POSITIONS_SETS,currentMessage).sendToTarget();
                    break;

                    case Constant_BT_Messages.positionMissed:
                        myHandler.obtainMessage(Handle_Constants.
                                MISS,currentMessage).sendToTarget();
                        break;

                    case Constant_BT_Messages.positionHit:
                        myHandler.obtainMessage(Handle_Constants.
                                HIT,currentMessage).sendToTarget();
                        break;

                    case Constant_BT_Messages.ENEMY_LOSE:
                        myHandler.obtainMessage(Handle_Constants.
                                GAME_OVER,currentMessage).sendToTarget();
                        break;
                    case Constant_BT_Messages.ENEMY_WIN:
                            myHandler.obtainMessage(Handle_Constants.
                                    GAME_OVER,currentMessage).sendToTarget();
                        break;
                    case Constant_BT_Messages.RESET_GAME:
                        myHandler.obtainMessage(Handle_Constants.
                                TRY_AGAIN,currentMessage).sendToTarget();
                        break;

                    case Constant_BT_Messages.EXIT:
                        myHandler.obtainMessage(Handle_Constants.
                                EXIT,currentMessage).sendToTarget();
                        break;

                }


                try {


                if(  Integer.valueOf(message) > -1 && Integer.valueOf(message)  < 100 )
                {
                    myHandler.obtainMessage(Handle_Constants.
                            FIRE,currentMessage).sendToTarget();
                }
                }
                catch(Exception e)
                {

                }
            }

        }

    public static String RetrieveCurrentMessage()
    {
        return currentMessage;
    }

}
