package Modules;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.concurrent.TimeUnit;

public class BotModule extends Token
{

    private JDA jda;

    public BotModule() throws Exception
    {
        this.jda = JDABuilder.createDefault(token).build();
    }

    public void sendMessage (String msg) throws Exception
    {
        this.jda.awaitReady().getCategories().get(0).getTextChannels().get(0).sendMessage(msg)
                .timeout(5, TimeUnit.SECONDS)
                .submit();
    }
}
