package Modules;

import Config.BotConfig;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter
{
    public void onMessageReceived (MessageReceivedEvent event)
    {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "info"))
        {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("This is info message").queue();
        }
    }
}
