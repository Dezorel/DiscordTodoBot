package Controllers;

import Modules.BotModule;

public class BotController
{
    public void run() throws Exception
    {
        BotModule bot = new BotModule();

        System.out.println("Bot is ON!");
        //bot.sendMessage("hello, my name is ToDo bot");
    }
}
