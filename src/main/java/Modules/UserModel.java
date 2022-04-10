package Modules;

public class UserModel
{
    private String birthday;

    private String chatId;

    private String username;

    private String birthdayUser;

    private String user_id;

    public UserModel(String chatId, String username, String birthday, String birthdayUser) {
        this.birthday = birthday;
        this.chatId = chatId;
        this.username = username;
        this.birthdayUser = birthdayUser;
    }

    public UserModel(String chatId, String user_id) {
        this.chatId = chatId;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() { return user_id; }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void addBirthday()
    {
        DBModule db = new DBModule();

        String query = "INSERT INTO users (`from_user`, `chat_id`, `birthday`, `to_user`) VALUES ('"+this.username+"','"+this.chatId+"','"+this.birthday+"', '"+this.birthdayUser+"')";

        db.connection();
        db.queryInsert(query);
        db.close();

    }

    public int addTask(String text)
    {
        DBModule db = new DBModule();
        int idTask;

        String query = "INSERT INTO tasks (`user_id`, `chat_id`, `task_text`) VALUES ('" + this.user_id + "','" + this.chatId + "','" + text + "')";

        db.connection();
        db.queryInsert(query);
        idTask = db.getIdTask(this.chatId, text, this.user_id);
        db.close();

        return idTask;
    }

    public int addTaskTime(String idTask, String stopDate, String user_id)
    {
        DBModule db = new DBModule();
        String query;
        db.connection();
        switch (db.checkAccessForTask(idTask, user_id))
        {
            case 1:                                 //таск существует и пользователь его владелец
                query = "UPDATE `tasks` SET `stop_date` = '" + stopDate + "' WHERE `id` = " + idTask;
                db.queryInsert(query);
                db.close();
                return 1;

            case 0:                                 //таск существует но пользователь не его владелец
                db.close();
                return 0;

            case 2:                                //таск не существует
                db.close();
                return 2;
        }
        db.close();
        return -1;
    }

    public int addTaskStartTime(String idTask, String startDate, String user_id)
    {
        DBModule db = new DBModule();
        String query;
        db.connection();
        switch (db.checkAccessForTask(idTask, user_id))
        {
            case 1:                                 //таск существует и пользователь его владелец
                query = "UPDATE `tasks` SET `start_date` = '" + startDate + "' WHERE `id` = " + idTask;
                db.queryInsert(query);
                db.close();
                return 1;

            case 0:                                 //таск существует но пользователь не его владелец
                db.close();
                return 0;

            case 2:                                //таск не существует
                db.close();
                return 2;
        }
        db.close();
        return -1;
    }

    public int closeTask(String idTask, String user_id)
    {
        DBModule db = new DBModule();
        String query;
        db.connection();
        switch (db.checkAccessForTask(idTask, user_id))
        {
            case 1:                                 //таск существует и пользователь его владелец
                query = "UPDATE `tasks` SET `is_closed` = 1 WHERE `id` = " + idTask;
                db.queryInsert(query);
                db.close();
                return 1;

            case 0:                                 //таск существует но пользователь не его владелец
                db.close();
                return 0;

            case 2:                                //таск не существует
                db.close();
                return 2;
        }
        db.close();
        return -1;
    }

    public String getBirthdays(String chatId)
    {
        String[] congratsText = {
                "Поздравляю с днем рождения! Желаю безмерного счастья, крепкого здоровья, настоящей любви, удачи, достатка, исполнения желаний! Пусть жизнь будет наполнена положительными эмоциями, верными друзьями, радостными днями. Ярких, светлых, счастливых тебе событий! \nС наилучшими пожеланиями, ",
                "Поздравляю с днем рождения! Яркого позитивного настроения, высоких достижений, душевной гармонии, процветания, крепкого здоровья, успехов во всём! Желаю никогда не останавливаться на достигнутом. Удачи в познании новых идей, саморазвития и стремления только к самому лучшему! \nС наилучшими пожеланиями, ",
                "Поздравляем с днем рождения! Желаем здоровья, удачи, любви, везения, мира, добра, улыбок, благополучия. Пусть все мечты исполняются. Пусть жизнь будет долгой и гладкой, полной ярких и запоминающихся событий! \nС уважением, ",
                "Поздравляю тебя с днём рождения! Желаю не болеть, не грустить, много улыбаться, сиять от счастья, с годами только хорошеть, верить в лучшее и в мечту, которая, несомненно, сбудется. Пусть удача будет твоим путеводителем, перед тобой открываются золотые горы благополучия и сладких грёз. Волшебные моменты пусть озаряют твой взгляд, любовь всегда будет рядом и доставляет тебе только удовольствие! \nС уважением, ",
                "Поздравляю с днем рождения! Кроме стандартных пожеланий — счастья, здоровья и всего наилучшего — я желаю, чтобы сбывались все мечты, чтобы каждый день приносил много приятных неожиданностей, чтобы Вас окружали только добрые и нужные люди. А еще везения. Везения во всём и всегда. И чтобы каждое начатое дело заканчивалось успешно! \nС наилучшими пожеланиями, ",
                "С днем рождения! Пусть каждый новый день начинается с улыбки, прилива сил и желания свернуть горы! Пусть крепкое здоровье будет неизменным спутником, а удача поджидает за каждым углом! Не переставайте мечтать, и пусть всегда чудесным образом находятся возможности для осуществления самых безумных желаний! \nС наилучшими пожеланиями, ",
                "Поздравляю с днем рождения! Пусть счастье сопровождает вас на каждом шагу, здоровье оберегает при любых обстоятельствах, удача и везение станут вашими надежными спутниками по жизни. Желаю вдохновения во всём, позитивного настроения и исполнения всех планов.\n С уважением, ",
                "Прими мои поздравления с днем рождения! В твой лучший день хочу пожелать самого лучшего, чтобы в жизни было как можно больше хороших эмоций, приятных моментов и постоянного круговорота позитивных событий. Чтобы окружали только любимые, родные, дорогие сердцу люди. Чтобы во всем сопутствовал успех и везение. Ты этого заслуживаешь!\nС уважением, ",
                "С днем рождения! Пусть каждый день будет полон добра, позитива, любви и положительных эмоций. Желаю исполнения всех мечтаний, оставайся таким же прекраснейшим человеком! Пусть тебя окружают лишь честные, добрые и искренние люди. Изо дня в день желаю тебе развиваться, становиться лучше и наполнять свою жизнь незабываемыми моментами.\nС наилучшими пожеланиями, ",
                "Сегодня, в твой день рождения, хочется пожелать тебе самого лучшего, самого важного, самого главного. Самое лучшее ― окружение, их поддержка и понимание. Самое важное — здоровье. И самое главное — финансовое благополучие и независимость. Чтобы все мечты сбывались, и все двери были открыты!\nС уважением, ",

        };
        DBModule db = new DBModule();
        String birthdaysToday = null;
        String message;

        db.connection();
        birthdaysToday = db.getBirthdays(chatId);

        if(birthdaysToday.equals(""))
        {
            message = "Сегодня именинников нет!";
            return message;
        }

        message = " ";

        String[] users = birthdaysToday.split("\n");
        for (String user : users) {
            String[] getUser = user.split(" ");

            message += getUser[1] + ", " + congratsText[0 + (int) (Math.random() * congratsText.length)] + getUser[0] + "\n\n";        //генерация случайного поздравления
            message += "\uD83C\uDF81\uD83C\uDF89\uD83C\uDF88\uD83C\uDF8A\uD83C\uDF81\uD83C\uDF89\uD83C\uDF88\uD83C\uDF8A\uD83C\uDF81\uD83C\uDF89\uD83C\uDF88\uD83C\uDF8A\uD83C\uDF81\uD83C\uDF89\uD83C\uDF88\uD83C\uDF8A" + "\n\n";
        }

        db.close();

        return message;
    }

    public String getTasksToday(String chatId)
    {
        DBModule db = new DBModule();
        String tasksToday = null;
        String message;

        db.connection();
        tasksToday = db.getTasksToday(chatId);
        System.out.println(tasksToday);
        if(tasksToday.equals(""))
        {
            message = "Сегодня заданий нет!";
            return message;
        }

        message = "Задания на сегодня: \n\n";

        String[] tasks = tasksToday.split("\n");
        for (String task : tasks) {
            message += task + "\n";
        }

        db.close();

        return message;
    }

    public String getTasksTodayByUser()
    {
        DBModule db = new DBModule();
        String tasksToday = null;
        String message;

        db.connection();
        tasksToday = db.getTasksTodayByUser();
        System.out.println(tasksToday);
        if(tasksToday.equals(""))
        {
            message = "Сегодня заданий нет!";
            return message;
        }

        message = "Задания на сегодня: \n\n";

        String[] tasks = tasksToday.split("\n");
        for (String task : tasks) {
            message += task + "\n";
        }

        db.close();

        return message;
    }

    public String getTasksSoon(String chatId)
    {
        DBModule db = new DBModule();
        String tasksSoon = null;
        String message;

        db.connection();
        tasksSoon = db.getTasksSoon(chatId);
        System.out.println(tasksSoon);
        if(tasksSoon.equals(""))
        {
            return "";
        }

        message = "Задания заканчивающиеся завтра: \n\n";

        String[] tasks = tasksSoon.split("\n");
        for (String task : tasks) {
            message += task + "\n";
        }

        db.close();

        return message;
    }

    public String getTaskClose(String chatId, int hour)
    {
        DBModule db = new DBModule();
        String tasksSoon = null;
        String message;

        db.connection();
        tasksSoon = db.getTaskAfterOneHourClose(chatId, hour);
        System.out.println(tasksSoon);
        if(tasksSoon.equals(""))
        {
            return "";
        }

        message = "Задания заканчивающиеся в течении часа: \n\n";

        String[] tasks = tasksSoon.split("\n");
        for (String task : tasks) {
            message += task + "\n";
        }

        db.close();

        return message;
    }
}
