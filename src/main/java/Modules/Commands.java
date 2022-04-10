package Modules;

import Config.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Commands extends ListenerAdapter
{

    private JDA jda;

    public Commands(JDA jda)
    {
        this.jda = jda;
    }

    public void onMessageReceived (MessageReceivedEvent event)
    {
        System.out.println("Message: " + event.getMessage().getContentRaw());

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

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "add_task_stop_date"))
        {
            if (args.length > 3)
            {
                String idTask = args[1];
                String stopDate = args[2];
                stopDate += " ";
                stopDate += args[3];


                UserModel userModel = new UserModel(event.getChannel().getId(), event.getAuthor().getId());

                try {
                    switch (userModel.addTaskTime(idTask, stopDate, event.getChannel().getId()))
                    {
                        case 0:
                            sendMessage( event.getAuthor().getName() + ", к сожалению ты не владелец данной задачи, обратись к владельцу что бы добавить время");
                            break;

                        case 1:
                            sendMessage("Время для задачи #" + idTask + " было успешно обновлено!");
                            break;

                        case 2:
                            sendMessage(event.getAuthor().getName() + ", ты ошибся, я не смог найти задачу #" + idTask + ", исправь данные и попробуй снова");
                            break;

                        default:
                            sendMessage( "Упс... Что-то пошло не так...");
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    sendMessage( "Ты указал неверное количество параметров, из трёх необходимых, пожалуйста повтори запрос с параметрами ID_Задания Дата_Окончания Время_Окончания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "add_task"))
        {
            if (args.length > 1)
            {
                int idTask;
                String[] textArray = Arrays.copyOfRange(args, 1, args.length);
                String taskText =  String.join(" ", textArray);
                String stopDate = "";

                UserModel userModel = new UserModel(event.getChannel().getId(), event.getAuthor().getId());

                idTask = userModel.addTask(taskText);

                System.out.println( event.getAuthor().getId() + " user generate task with text: " + taskText);

                try {
                    sendMessage(event.getAuthor().getName() + ", таск был успешно создан под id: #" + idTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
            {
                System.out.println("Params task not exitst");
                try {
                    sendMessage( event.getAuthor().getName()  + ", ты не указал ни одного параметра, пожалуйста повтори запрос с параметром Текст_Задания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }



        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + ""))
        {

        }
    }

    public void sendMessage (String msg) throws Exception
    {
        this.jda.awaitReady().getCategories().get(0).getTextChannels().get(1).sendMessage(msg)
                .timeout(5, TimeUnit.SECONDS)
                .submit();
    }
}
