import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class OtherMethods {
    public String SQLCommand;
    public ResultSet result;

    public static OtherMethods instance;

    private OtherMethods(){};
    public static OtherMethods getInstance(){
        if(instance == null){
            instance = new OtherMethods();
        }
        return instance;
    }
    public int maxCount() throws SQLException {
        int max=0;
        SQLCommand = "SELECT MAX(mesta) FROM `stolik`";
        result = DbConnect.statement.executeQuery(SQLCommand);
        while(result.next())
            max = result.getInt(1);
        return max;
    }
    public int stolikCount() throws SQLException {
        int count=0;
        SQLCommand = "SELECT COUNT(*) FROM stolik";
        result = DbConnect.statement.executeQuery(SQLCommand);
        while(result.next())
            count = result.getInt(1);
        return count;
    }
    public int maxDays(int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022,month,1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) ;
    }
    public String monthtoString(int i){
        switch (i){
            case 0: return "Январь";
            case 1: return "Февраль";
            case 2: return "Март";
            case 3: return "Апрель";
            case 4: return "Май";
            case 5: return "Июнь";
            case 6: return "Июль";
            case 7: return "Август";
            case 8: return "Сентябрь";
            case 9: return "Октябрь";
            case 10: return "Ноябрь";
            case 11: return "Декабрь";
            default: return "Error";
        }
    }

    public void daySchedule() throws SQLException {
        int stolikCount = 0;
        SQLCommand = "SELECT count(*) FROM `stolik` WHERE mesta >="+User.countPeople;
        result = DbConnect.statement.executeQuery(SQLCommand);
        while(result.next()) stolikCount = result.getInt(1);
        for(int i=0;i<stolikCount;i++){
            System.out.println("Столик №"+(i+1)+". \t\t|      |       |       |       |       |       |       |       |       |       |       |       |       |       |");
        }
    }
    public boolean getCheck(int month,int day,int hour, int countPeople) throws SQLException {

        return true;
    }

}
