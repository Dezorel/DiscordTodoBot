package Modules;

import Config.BotConfig;
import Config.Token;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.TimeUnit;

public class BotModule extends Token
{

    private JDA jda;

    public BotModule() throws Exception
    {
        this.jda = JDABuilder.createDefault(token).build();
        this.jda.getPresence().setStatus(OnlineStatus.ONLINE);
        this.jda.getPresence().setActivity(Activity.watching(BotConfig.whatWatchingBot));

        this.jda.addEventListener(new Commands());
    }

    public void sendMessage (String msg) throws Exception
    {
        this.jda.awaitReady().getCategories().get(0).getTextChannels().get(1).sendMessage(msg)
                .timeout(5, TimeUnit.SECONDS)
                .submit();
    }
}
