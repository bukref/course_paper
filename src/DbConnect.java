import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {                              //connect to DataBase  
    public static final String LOGIN = "root";            //пользователь
    public static final String PASSWORD = "root";         //пароль
    public static final String LINK = "jdbc:mysql://localhost:3306/restaurant";  //jdbc:mysql://   хост:порт/имя базы данных. Кстати чтобы найти значение поля URL, можно открыть DB Navigator, правая кнопка мыши по соединению и выбрать Connection Info, найти Connection URL
    public static Statement statement;
    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(LINK,LOGIN,PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}