package sample;

import java.sql.*;
import java.util.ArrayList;

public class Postgre{
    protected String jdbcURL = "URL";
    protected String username = "USERNAME";
    protected String password = "DATABASE";

    Connection connection;

    public void getCredentials(){

    }

    public void sql(String q, String nick, int score) {

        if (q.equals("All")) {
            try {
                if (connection == null)
                    connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement();

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

            String query = "SELECT nick FROM users WHERE is_online = 1 AND nick != '"
                    + nickname +  "' ORDER BY title DESC";
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

    public ArrayList<String> getOnlinePlayersTitles(String nickname){
        ArrayList<String> ranks = new ArrayList<>();
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT title FROM users WHERE is_online = 1 AND nick != '"
                    + nickname +  "' ORDER BY title DESC";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nick = resultSet.getString("title");
                ranks.add(nick);
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return ranks;
    }

    public ArrayList<String> getRank(String nickname){
        ArrayList<String> rank = new ArrayList<>();
        rank.add("default");
        try {
            if (connection == null)
                getCredentials();
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT rank, custom_rank, custom_color FROM users WHERE nick = '" + nickname +  "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                rank.clear();
                String rank1 = resultSet.getString("rank");
                rank.add(rank1);
                String customRank = resultSet.getString("custom_rank");
                rank.add(customRank);
                String customColor = resultSet.getString("custom_color");
                rank.add(customColor);
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return rank;
    }

    public void addUser(String nickname, String pass) throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to Postgresql server successfully");
        String inserting= "INSERT INTO users VALUES ('" + nickname + "', '0', '" + pass + "', '', 0, 'Adventurer')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(inserting);
    }

    public Boolean checkInternet(){
        try {
            if (connection == null)
                getCredentials();
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            return true;
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return false;
    }

    public String getTitle(String nickname){
        String title = "Unknown";
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT title FROM users WHERE nick = '" + nickname +  "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                title = resultSet.getString("title");
            }
            return title;
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return title;
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

    public Boolean usernameTaken(String nickname) throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to Postgresql server successfully");
        String query = "SELECT nick FROM users WHERE nick = '" + nickname + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.next();
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
