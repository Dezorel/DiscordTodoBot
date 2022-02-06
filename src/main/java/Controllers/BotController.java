package Controllers;

import Modules.BotModule;

public class BotController
{
    public void runAndSendHi() throws Exception
    {
        BotModule bot = new BotModule();
        bot.sendMessage("hello, my name is ToDo bot");
    }
}
