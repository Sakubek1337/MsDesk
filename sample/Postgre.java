package sample;

import java.sql.*;
import java.util.ArrayList;

public class Postgre{
    protected  String jdbcURL = "jdbc:postgresql://host";
    protected String username = "nickname";
    protected String password = "password";

    Connection connection;


    public void sql(String q, String nick, int score) {

        if (q.equals("All")) {
            try {
                if (connection == null)
                    connection = DriverManager.getConnection(jdbcURL, username, password);
                //System.out.println("Connected to Postgresql server successfully");
                //String inserting= "INSERT INTO users(nick,score) VALUES ('elmir','7000')";
                Statement statement = connection.createStatement();
                //statement.executeUpdate(inserting);

                String query = "SELECT * FROM users WHERE nick = '" + nick + "'";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String nickname = resultSet.getString("nick");
                    String score1 = resultSet.getString("score");
                    System.out.printf("nickname - %s | score - %s", nickname, score1);
                }

            } catch (SQLException e) {
                System.out.println("Error in connecting to Postgresql server");
                e.printStackTrace();
            }
        }else if(q.equals("Update")){
            try {
                if (connection == null)
                    connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement();
                String query = "UPDATE users SET score = " + score + " WHERE nick = '" + nick + "'";
                statement.executeUpdate(query);

            } catch (SQLException e) {
                System.out.println("Error in connecting to Postgresql server");
                e.printStackTrace();
            }
        }
    }

    public String sql(String q, String nick){

        String r = "0";

        if (q.equals("getScore")) {
            try {
                if (connection == null)
                    connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement();

                String query = "SELECT score FROM users WHERE nick = '" + nick + "'";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    r = resultSet.getString("score");
                }
            } catch (SQLException e) {
                System.out.println("Error in connecting to Postgresql server");
                e.printStackTrace();
            }
        }
        return r;
    }

    public void makeOnline(String nickname){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "UPDATE users SET is_online = 1 WHERE nick = '" + nickname +  "'";
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
    }

    public void makeOffline(String nickname){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "UPDATE users SET is_online = 0 WHERE nick = '" + nickname +  "'";
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getOnlinePlayers(String nickname){
        ArrayList<String> players = new ArrayList<>();
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT nick FROM users WHERE is_online = 1 AND nick != '" + nickname +  "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nick = resultSet.getString("nick");
                players.add(nick);
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return players;
    }

    public void addUser(String nickname, String pass) throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to Postgresql server successfully");
        String inserting= "INSERT INTO users VALUES ('" + nickname + "', '0', '" + pass + "', 0)";
        Statement statement = connection.createStatement();
        statement.executeUpdate(inserting);
    }

    public Boolean checkInternet(){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            return true;
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkUser(String nickname){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT nick FROM users WHERE nick = '" + nickname + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkPass(String nickname, String pass){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT nick, password FROM users WHERE nick = '" + nickname + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String actualPass = resultSet.getString("password");
            if (actualPass.equals(pass)){
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }

        return false;
    }

    public void closeConnection() throws SQLException {
        try{
            System.out.println("Connection closed.");
            connection.close();
        }catch (Exception e){
            System.out.println("Connection was not closed for some reason!");
        }
    }
}
