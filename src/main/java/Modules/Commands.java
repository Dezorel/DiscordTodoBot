package Modules;

import Config.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "help"))
        {
            String helpText;
            helpText = "Приветствую " + event.getAuthor().getName() + "! \n";
            helpText += "Я помогу тебе разобраться в функционале\n\n";
            helpText += "Ты можешь управлять мной используя следующие команды: \n\n";
            helpText += "help показать это окно ещё раз \n\n";
            helpText += "hi получить приветствие от Бота \n\n";
            helpText += "set-birthday (дата в формате год-месяц-день) (имя человека, которого необходимо поздравить) устанавливает день рождения\n\n";
            helpText += "add-task (дата начала в формате ГОД-МЕСЯЦ-ДЕНЬ-ЧАС-МИНУТА-СЕКУНДЫ) (дата окончания в формате ГОД-МЕСЯЦ-ДЕНЬ-ЧАС-МИНУТА-СЕКУНДЫ) (текст задачи) устанавливает определённую задачу \n\n";
            helpText += "change-task-stop-date (# задачи) (дата окончания в формате ГОД-МЕСЯЦ-ДЕНЬ) устанавливает день окончания задачи\n\n";
            helpText += "change-task-start-date (# задачи) (дата начала в формате ГОД-МЕСЯЦ-ДЕНЬ) устанавливает день начала задачи\n\n";
            helpText += "get-task (# задачи) показывает информацию о задаче\n\n";
            helpText += "close-task (# задачи) закрывает возможность выполнения задачи\n\n";
            helpText += "start-congrats Запуск ежедневной рассылки поздравлений с днём рождения\n\n";
            helpText += "start-get-tasks Запуск ежедневной рассылки текущих заданий и заданий близких к дате завершений\n\n";
            try {
                sendMessage(helpText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "get_task"))
        {
            if (args.length > 1)
            {
                String idTask = args[1];

                DBModule db = new DBModule();
                db.connection();
                String task = db.getTaskById(idTask, event.getChannel().getId());

                if(task.length() > 0)
                {
                    sendInlineKeyboard(event, task);
                }
                else
                {
                    try {
                        sendMessage("Задача с таким идентификатором не найдена \uD83D\uDE1E");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                db.close();

            }

            else
            {
                System.out.println("Date not exist");
                try {
                    sendMessage(event.getAuthor().getName() + ", ты не указал # задачи, как же так?! Попробуй повтори, но уже с идентификатором");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "close_task"))
        {
            if (args.length > 1)
            {
                String idTask = args[1];

                UserModel userModel = new UserModel(event.getChannel().getId(), event.getAuthor().getId());

                try {
                    switch (userModel.closeTask(idTask, event.getAuthor().getId()))
                    {
                        case 0:
                            sendMessage(event.getAuthor().getName() + ", к сожалению ты не владелец данной задачи, обратись к владельцу что бы закрыть задачу");
                            break;

                        case 1:
                            sendMessage("Задача #" + idTask + " была успешно закрыта!");
                            break;

                        case 2:
                            sendMessage(event.getAuthor().getName() + ", ты ошибся, я не смог найти задачу #" + idTask + ", исправь данные и попробуй снова");
                            break;

                        default:
                            sendMessage("Упс... Что-то пошло не так...");
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
                try {
                    sendMessage(event.getAuthor().getName() + ", ты указал всего лишь один параметр, из двух необходимых, пожалуйста повтори запрос с параметрами ID_Задания Время_Окончания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "change_task_start_date"))
        {
            if (args.length > 2)
            {
                String idTask = args[1];
                String startDate = args[2];

                UserModel userModel = new UserModel(event.getChannel().getId(), event.getAuthor().getId());

               try {
                   switch (userModel.addTaskStartTime(idTask, startDate, event.getAuthor().getId()))
                   {
                       case 0:
                           sendMessage(event.getAuthor().getName() + ", к сожалению ты не владелец данной задачи, обратись к владельцу что бы изменить время");
                           break;

                       case 1:
                           sendMessage("Время для задачи #" + idTask + " было успешно обновлено!");
                           break;

                       case 2:
                           sendMessage(event.getAuthor().getName() + ", ты ошибся, я не смог найти задачу #" + idTask + ", исправь данные и попробуй снова");
                           break;

                       default:
                           sendMessage("Упс... Что-то пошло не так...");
                           break;
                   }
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }

            }
            else if (args.length > 1)
            {
                try {
                    sendMessage(event.getAuthor().getName() + ", ты указал всего лишь один параметр, из двух необходимых, пожалуйста повтори запрос с параметрами ID_Задания Время_Окончания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Params task not exitst");
                try {
                    sendMessage(event.getAuthor().getName() + ", ты не указал ни одного параметра, пожалуйста повтори запрос с параметрами ID_Задания Время_Окончания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "start_get_tasks"))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run() {
                    UserModel userModel = new UserModel(event.getChannel().getId(), event.getAuthor().getId());
                    while (true)
                    {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                        LocalDateTime now = LocalDateTime.now();
                        int hour = LocalDateTime.now().getHour();
                        try {

                            if(dtf.format(now).equals("10:00"))
                            {
                                DBModule db = new DBModule();
                                db.connection();
                                db.automaticallyCloseTask();        //close task what need ot close
                                db.close();

                                try {
                                    sendMessage(userModel.getTasksToday(event.getChannel().getId()));
                                    Thread.sleep(1000);
                                    sendMessage(userModel.getTasksSoon(event.getChannel().getId()));
                                    Thread.sleep(1000);
                                    sendMessage(userModel.getTaskClose(event.getChannel().getId(), hour));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        if (args[0].equalsIgnoreCase(BotConfig.prefixCommand + "complete_task"))
        {
            if (args.length > 1)
            {
                String idTask = args[1];

                DBModule db = new DBModule();
                db.connection();
                try
                {
                    if (db.addUserToCompleteTask(idTask, event.getAuthor().getId()))
                    {
                        sendMessage(event.getAuthor().getName() + ", данный таск успешно добавлен в выполненные!");
                    }
                    else
                    {
                        sendMessage("Упс... Что-то пошло не так. Попробуй позже");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                db.close();
            }
            else
            {
                try {
                    sendMessage(event.getAuthor().getName() + ", ты указал ни одного параметра, пожалуйста повтори запрос с параметрами ID_Задания");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessage (String msg) throws Exception
    {
        this.jda.awaitReady().getCategories().get(0).getTextChannels().get(1).sendMessage(msg)
                .timeout(5, TimeUnit.SECONDS)
                .submit();
    }

    public void sendInlineKeyboard (MessageReceivedEvent event, String taskText)
    {
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle(":information_source:  Information");
        info.setDescription(taskText);
        info.setColor(0xf45642);
        info.setFooter("This message generated by " + event.getAuthor().getAsTag(), event.getMember().getUser().getAvatarUrl());

        event.getChannel().sendTyping().queue();
        //event.getChannel().sendMessage("This is info message").queue();
        event.getChannel().sendMessageEmbeds(info.build()).queue();

        info.clear();
    }
}
