package Modules;

import java.sql.*;

public class DBModule
{
    private final String url = "jdbc:mysql://localhost:3306/TelegramBotDB";
    private final String user = "tg_bot";
    private final String pass = "tg_bot";

    private Statement stmt;
    private ResultSet rs;
    private Connection con;

    public void connection(){

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("--------Successful connect to DB--------");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public ResultSet querySelect(String query)
    {
        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet queryInsert(String query)
    {
        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int getIdTask(String chatId, String text, String user_id)
    {
        int idChat = -1;
        String query = "SELECT `id` FROM tasks WHERE `chat_id` = '" + chatId + "' AND `task_text` = '" + text + "' AND `user_id` = '" + user_id + "'";
        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                idChat = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idChat;
    }

    public int checkAccessForTask(String idTask, String user_id)
    {
        String queryCheckIfTaskExist = "SELECT IFNULL((SELECT `user_id` FROM `tasks` WHERE `id` = " + idTask + "), -1) as `user_id` ";
        String user = "-1";
        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryCheckIfTaskExist);
            rs = stmt.executeQuery(queryCheckIfTaskExist);

            while(rs.next())
            {
                user = rs.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!user.equalsIgnoreCase("-1"))
        {
            if (user.equalsIgnoreCase(user_id))
            {
                System.out.println("Task id #" + idTask + " exist in BD, user " + user_id + " it's owner. SUCCESS");
                return 1;
            }
            else
            {
                System.out.println("Task id #" + idTask + " exist in BD, but user " + user_id + " not it's owner");
                return 0;
            }
        }
        else
        {
            System.out.println("Task id #" + idTask + " not found in DB, exit");
            return 2;
        }

    }

    public String getBirthdays(String chatId)
    {
        String queryDate = "SELECT DATE_FORMAT(NOW(), '%m-%d') as date";
        String now = "00-00";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryDate);
            rs = stmt.executeQuery(queryDate);

            while(rs.next())
            {
                now = rs.getString("date");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT `from_user`, `to_user`, `birthday` FROM users WHERE `chat_id` = '" + chatId + "' AND `birthday` LIKE '%" + now + "'";
        String birthdaysToday = "";
        try {

            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            int i = 0;
            while(rs.next())
            {
                birthdaysToday += rs.getString("from_user") + " " + rs.getString("to_user") + "\n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return birthdaysToday;
    }

    public String getTasksToday(String chatId)
    {
        //String queryDate = "SELECT DATE(NOW()) as date";
        String queryDate = "SELECT CURRENT_TIMESTAMP() as date";
        String now = "0000-00-00 00:00:00";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryDate);
            rs = stmt.executeQuery(queryDate);

            while(rs.next())
            {
                now = rs.getString("date");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT `id`, `user_id`, `task_text`, `stop_date` FROM tasks WHERE `chat_id` = '" + chatId + "' AND `start_date` <= '" + now + "' AND `is_closed` = 0";
        String tasksToday = "";
        try {

            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                tasksToday += "\uD83D\uDC64 [Автор](tg://user?id=" + rs.getString("user_id") + ")\n" + "❗ Задача #" + rs.getInt("id") + ": " + rs.getString("task_text") + "\n" + "⏱ Крайний срок: ";

                if(rs.getString("stop_date") == null)
                {
                    tasksToday += " Отсутсвует" + "\n\n";
                }
                else
                {
                    tasksToday += " " + rs.getString("stop_date") + "\n\n";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasksToday;
    }

    public String getTasksTodayByUser()
    {
        String queryDate = "SELECT DATE(NOW()) as date";
        String now = "0000-00-00";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryDate);
            rs = stmt.executeQuery(queryDate);

            while(rs.next())
            {
                now = rs.getString("date");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT `id`, `user_id`, `task_text`, `stop_date` FROM tasks WHERE `start_date` LIKE '" + now + "%' AND `is_closed` = 0";
        String tasksToday = "";
        try {

            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                tasksToday += "\uD83D\uDC64 \" [Автор](tg://user?id=" + rs.getString("user_id") + ")\n" + "❗ Задача #" + rs.getInt("id") + ": " + rs.getString("task_text") + "\n" + "⏱ Крайний срок: ";

                if(rs.getString("stop_date") == null)
                {
                    tasksToday += " Отсутсвует" + "\n\n";
                }
                else
                {
                    tasksToday += " " + rs.getString("stop_date") + "\n\n";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasksToday;
    }

    public String getTasksSoon(String chatId)
    {
        String queryDate = "SELECT DATE(CURDATE() + INTERVAL 1 DAY) as date";
        String now = "0000-00-00";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryDate);
            rs = stmt.executeQuery(queryDate);

            while(rs.next())
            {
                now = rs.getString("date");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT `id`, `user_id`, `task_text`, `stop_date` FROM tasks WHERE `chat_id` = '" + chatId + "' AND `stop_date` LIKE '" + now + "%'";
        String tasksToday = "";
        try {

            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                tasksToday += "\uD83D\uDC64 [Автор](tg://user?id=" + rs.getString("user_id") + ")\n" + "❗ Задача #" + rs.getInt("id") + ": " + rs.getString("task_text") + "\n" +
                        "⏱ Крайний срок: " + rs.getString("stop_date")+"\n\n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasksToday;
    }

    public String getTaskAfterOneHourClose(String chatId, int hour)
    {
        String queryDate = "SELECT DATE(CURDATE()) as date";
        String now = "0000-00-00";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + queryDate);
            rs = stmt.executeQuery(queryDate);

            while(rs.next())
            {
                now = rs.getString("date");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        now += " " + hour;

        String query = "SELECT `id`, `user_id`, `task_text`, `stop_date` FROM tasks WHERE `chat_id` = '" + chatId + "' AND `stop_date` LIKE '" + now + "%'";
        String tasksToday = "";
        try {

            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                tasksToday += "\uD83D\uDC64 [Автор](tg://user?id=" + rs.getString("user_id") + ")\n" + "❗ Задача #" + rs.getInt("id") + ": " + rs.getString("task_text") + "\n" +
                        "⏱ Крайний срок: " + rs.getString("stop_date")+"\n\n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasksToday;
    }

    public String getTaskById(String taskId, String chatId)
    {
        String task ="";
        String query = "SELECT * FROM tasks WHERE `id` = '" + taskId + "' AND `chat_id` = '" + chatId + "'";
        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                //task += "\uD83D\uDC64 [Автор](tg://user?id=" + rs.getString("user_id") + ")\n";
                task += "\uD83D\uDC64 Автор: <@" + rs.getString("user_id") + ">\n";
                task += "❗ Задача #" + taskId + ": " + rs.getString("task_text") + "\n";
                task += "⏱ Крайний срок: ";

                if(rs.getString("stop_date") == null)
                {
                    task += " Отсутсвует" + "\n\n";
                }
                else
                {
                    task += " " + rs.getString("stop_date") + "\n\n";
                }

                task += "Статус: ";
                if(rs.getString("is_closed").equals("0"))
                {
                    task += " Открыт ✏" + "\n\n";
                }
                else
                {
                    task += " Закрыт \uD83D\uDCA4 \n\n";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public boolean addUserToCompleteTask(String taskId, int user_id)
    {
        String query = "INSERT INTO `users_complete_task` (`id_task` , `user_id`) VALUES ( " + taskId + ", '" + user_id + "')";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCompleteUsers(String taskId)
    {
        String answer = "";
        int count = 0;
        String query = "SELECT user_id FROM users_complete_task WHERE `id_task` = '" + taskId + "'";

        try {
            stmt = con.createStatement();
            System.out.println("Execute query: " + query);
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                count ++;
                answer += "[\uD83D\uDC64](tg://user?id=" + rs.getString("user_id") + ")  ";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String startString = "Задачу #"+ taskId + " выполнил: " + count;
        if(count == 2 || count == 3 || count == 4)
        {
            startString += " человека\n";
        }
        else
        {
            startString += " человек\n";
        }

        return startString + answer;
    }

    public void automaticallyCloseTask()
    {
        String query = "UPDATE `tasks` SET `is_closed` = 1 WHERE `stop_date` < CURRENT_TIMESTAMP";
        this.queryInsert(query);
    }

    public void close()
    {
        try {
            con.close();
            System.out.println("--------DB connection close--------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
