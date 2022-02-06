/**
 * @author Leonid
 * @project CR-193 project ASO
 */

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.concurrent.TimeUnit;


public class App
{
    public static void main(String[] args) throws Exception{
        

        JDA jda = JDABuilder.createDefault(token).build();

        jda.awaitReady().getCategories().get(0).getTextChannels().get(0).sendMessage("Test")
                .timeout(5, TimeUnit.SECONDS)
                .submit();
    }
}
