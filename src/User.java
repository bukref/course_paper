import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.random;

public class User {
    int id;
    String name;
    String phone;
    int duration;
    private Scanner input = new Scanner(System.in);
    public String SQLcommand;
    public ResultSet result;
    public Random random = new Random();
    static int countPeople;

    public boolean autorization() throws SQLException {
        System.out.print("Введите номер телефона: ");
        phone = input.nextLine();
        SQLcommand = "SELECT * FROM `person` WHERE phone = " + phone;
        result = DbConnect.statement.executeQuery(SQLcommand);
        if(result.next()){
            id = result.getInt(1);
            name = result.getString(2);
            System.out.println("*Псевдо-авторизация*");
            System.out.println("Введите код из СМС: "+ random.nextInt(10000));
            System.out.println("Здравствуйте, "+ name);
            return true;

        }
        else {
            System.out.println("Номер не найден, обратитесь к администратору");
            return false;
        }
    }
    public boolean menu() throws SQLException {
        int choise;
        System.out.println("Выберите команду:");
        System.out.println("1. Забронировать столик");
        System.out.println("2. Просмотреть/отменить бронь");
        //System.out.println("3. Просмотреть расписание");
        System.out.println("0. Выход");
        System.out.print("Номер команды >>> ");
        choise = input.nextInt();
        input.nextLine();
        switch (choise){
            case 1:{
                reserve();
                break;
            }
            case 2:{
                showReserves();
                break;
            }
            case 3:{
                System.out.println();
                break;
            }
            case 0:{
                return true;
            }
            default:{
                System.out.println("Такой команды нет");
                break;
            }
        }
        return false;
    }
    public void reserve() throws SQLException {
        Date currentDate = new Date();
        Date userDate = new Date();
        int choiseDate;
        System.out.print("Сколько  будет человек? ");
        countPeople = input.nextInt();
        input.nextLine();
        if(countPeople>OtherMethods.getInstance().maxCount()) System.out.println("За одно бронирование можно посадить не более "+ OtherMethods.getInstance().maxCount() + " человек. Просто сделайте доп.бронь на это же время. Для организации мероприятий позвоните по номеру 76-20-45. Извините за предоставленные неудобства");
        else {
            boolean check = false;
            while (!check) {
                System.out.println("Укажите месяц:");
                System.out.println("1. Текущий ("+ OtherMethods.getInstance().monthtoString(userDate.getMonth())+")");
                System.out.println("2. Следующий ("+ OtherMethods.getInstance().monthtoString(userDate.getMonth()+1)+")");
                System.out.println("3. Другое");
                choiseDate = input.nextInt();
                input.nextLine();
                switch (choiseDate) {
                    case 1: {
                        userDate.setMonth(currentDate.getMonth());
                        check = true;
                        break;
                    }
                    case 2: {
                        userDate.setMonth(currentDate.getMonth() + 1);
                        check = true;
                        break;
                    }
                    case 3: {
                        System.out.println("Для долгосрочных записей позвоните по по номеру 76-20-45. Извините за предоставленные неудобства.");
                        return;
                    }
                    default: {
                        System.out.println("Введите корректное число!");
                        break;
                    }
                }
            }
            check = false;
            while (!check) {
                System.out.println("Укажите число месяца:");
                System.out.println("0. Сегодня ("+ currentDate.getDate()+")");
                System.out.println("1-"+OtherMethods.getInstance().maxDays(userDate.getMonth())+ ". Остальные числа.");
                choiseDate = input.nextInt();
                input.nextLine();
                if(choiseDate==0 ){
                    userDate.setDate(currentDate.getDate());
                    check = true;
                }
                else if(choiseDate<=OtherMethods.getInstance().maxDays(userDate.getMonth()) && choiseDate>=currentDate.getDate()){
                    userDate.setDate(choiseDate);
                    check = true;
                }
                else System.out.println("Неккоректное значение! Введите заново");
            }
            //System.out.println("Время (Ч): \t\t| 9:00 | 11:00 | 12:00 | 13:00 | 14:00 | 15:00 | 16:00 | 17:00 | 18:00 | 19:00 | 20:00 | 21:00 | 22:00 | 23:00 |");
            //OtherMethods.getInstance().daySchedule();

            check = false;
            while(!check){
                System.out.println("На какое время забронировать столик (в часах)?");
                int hour = input.nextInt();
                input.nextLine();
                if(hour>23 || hour<9) System.out.println("В это время ресторан не работает.");
                else{
                    userDate.setHours(hour);
                    check = true;
                }
            }
            /*check = false;
            while (!check){
                System.out.println("На сколько забронировать столик (в часах)? Доступно не более "+ (23-userDate.getHours()));
                duration= input.nextInt();
                input.nextLine();
                if(duration>(23-userDate.getHours()) || duration<1) System.out.println("Введите корректное значение");
                else check= true;
            }*/
            int currentCount = 0;
            int count = 0;
            int idStolik = 0;
            SQLcommand = "SELECT COUNT(*) FROM `reserve` JOIN stolik ON id_stolik = stolik.id WHERE date = '2023-"+(userDate.getMonth()+1)+"-"+userDate.getDate()+" "+userDate.getHours()+":00:00' AND stolik.mesta>="+countPeople;
            //System.out.println(SQLcommand);
            result = DbConnect.statement.executeQuery(SQLcommand);
            while(result.next()){
                currentCount = result.getInt(1);
            }
            SQLcommand = "SELECT COUNT(*) FROM `stolik`WHERE mesta>="+countPeople;
            //System.out.println(SQLcommand);
            result = DbConnect.statement.executeQuery(SQLcommand);
            while(result.next()){
                count = result.getInt(1);
            }
            //System.out.println("текущая бронь:"+ currentCount+" / общая бронь:"+count);
            if(count<=currentCount){
                System.out.println("Свободных мест нет! Выберите другое время.");
            }
            else {
                System.out.print("Можете оставить комментарий: ");
                String comm = input.nextLine();
                SQLcommand = "SELECT id FROM stolik WHERE mesta>="+countPeople+" AND id NOT IN( SELECT DISTINCT stolik.id FROM `reserve` JOIN stolik ON id_stolik = stolik.id WHERE date = '2023-"+(userDate.getMonth()+1)+"-"+userDate.getDate()+" "+userDate.getHours()+":00:00')";
                //System.out.println(SQLcommand);
                result = DbConnect.statement.executeQuery(SQLcommand);
                while(result.next()){
                    idStolik = result.getInt(1);
                }
                SQLcommand = "INSERT INTO `reserve` (`id`, `id_stolik`, `id_person`, `date`, `comment`) VALUES (NULL, '"+idStolik+"', '"+id+"', '2023-"+(userDate.getMonth()+1)+"-"+userDate.getDate()+" "+userDate.getHours()+":00:00', '"+comm+"')";
                //System.out.println(SQLcommand);
                DbConnect.statement.executeUpdate(SQLcommand);
                System.out.println("Ваш столик забронирован, спасибо!");
            }
        }
    }
    public void showReserves() throws SQLException {
        System.out.println("Записи на ваше имя:");
        SQLcommand = "SELECT * FROM `reserve` WHERE id_person = "+id+" AND date> CURRENT_DATE ORDER BY date ASC";
        //System.out.println(SQLcommand);
        result = DbConnect.statement.executeQuery(SQLcommand);
        while(result.next()){
            System.out.print("Код:" + result.getInt(1)+"\t");
            System.out.print(" / Месяц:" + OtherMethods.getInstance().monthtoString(result.getDate(4).getMonth()));
            System.out.print(" / Число:" + result.getDate(4).getDate());
            System.out.print(" / Время:" + result.getTime(4).getHours()+":00\t");
            System.out.print(" / Комментарий:" + result.getString(5));
            System.out.println("");
        }
        int choise=1;
        while (choise!=0) {
            System.out.println("0 - Выход / 1 - Удалить запись");
            choise = input.nextInt();
            input.nextLine();
            if(choise==1){
                System.out.print("Введите номер записи: ");
                choise = input.nextInt();
                input.nextLine();
                deleteReserves(choise);
                choise =0;
            }
        }
    }
    public void deleteReserves(int idReserve) throws SQLException {
        SQLcommand = "DELETE FROM reserve WHERE `reserve`.`id` = "+idReserve+" AND id_person="+id;
        //System.out.println(SQLcommand);
        DbConnect.statement.executeUpdate(SQLcommand);
        System.out.println("Запись удалена");
    }
}
