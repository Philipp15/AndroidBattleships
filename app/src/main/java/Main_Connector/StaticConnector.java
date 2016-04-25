package Main_Connector;

/**
 * Created by Philipp on 11/04/2015.
 */
public class StaticConnector
{

    static Connector ConstantConnector;

    public static void SaveConnector(Connector connector)
    {
        ConstantConnector = connector;
    }

    public static Connector GetConnector()
    {
        return ConstantConnector;
    }


}
