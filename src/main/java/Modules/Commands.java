package Modules;

import Config.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter
{
    public void onMessageReceived (MessageReceivedEvent event)
    {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "info"))
        {
            //create window in answer
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(":information_source:  Information");
            info.setDescription("This is info message");
            info.addField("Creator", "Dezorel", false);
            info.setColor(0xf45642);
            info.setFooter("This message created", event.getMember().getUser().getAvatarUrl());

            event.getChannel().sendTyping().queue();
            //event.getChannel().sendMessage("This is info message").queue();
            event.getChannel().sendMessageEmbeds(info.build()).queue();

            info.clear();
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "test"))
        {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("This is test message").queue();
        }
    }
}
