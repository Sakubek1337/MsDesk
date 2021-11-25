package sample;

import java.sql.*;

public class Postgre{
    protected String jdbcURL = "jdbc:postgresql:host:port/database";
    protected String username = "username";
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

        String r = "No result";

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

    public void closeConnection() throws SQLException {
        try{
            System.out.println("Connection closed.");
            connection.close();
        }catch (Exception e){
            System.out.println("Connection was not closed for some reason!");
        }
    }
}
