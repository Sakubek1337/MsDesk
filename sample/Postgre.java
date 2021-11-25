package sample;

import java.sql.*;

public class Postgre{
    protected  String jdbcURL = "jdbc:postgresql://ec2-52-48-137-75.eu-west-1.compute.amazonaws.com:5432/d3g3g6r529vc82";
    protected String username = "ncxfuopnggexwd";
    protected String password = "9580b37322a2510577a6558369ebe941fc87eac98eb4328aa9d5b23ad1c2f802";

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
