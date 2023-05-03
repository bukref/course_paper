import java.sql.SQLException;

public class Main {
    static public void main(String[] args) throws ClassNotFoundException, SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        User user = new User();
        while (!user.autorization()){};
        System.out.println("");
        while(!user.menu()){}
    }
}
