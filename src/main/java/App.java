/**
 * @author Leonid
 * @project CR-193 project ASO
 */

import Controllers.BotController;
import Modules.Token;


public class App extends Token
{
    public static void main(String[] args)
    {
        BotController bot = new BotController();

        try
        {
            bot.runAndSendHi();
        }
        catch (Exception e)
        {
            System.out.println("Ooops!");
        }
    }
}
