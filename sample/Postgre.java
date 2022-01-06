package sample;

import java.sql.*;
import java.util.ArrayList;

public class Postgre{
    protected String jdbcURL = "jdbc:postgresql://hostname:port/databasename";
    protected String username = "username";
    protected String password = "password";

    Connection connection;

    public void getCredentials(){

    }

    public ArrayList<String> getChats(String nickname){
        ArrayList<String> chats = new ArrayList<>();
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT nick1, nick2 FROM chats WHERE nick1 = '" + nickname + "' OR nick2 = '"
                    + nickname + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nick1 = resultSet.getString("nick1");
                String nick2 = resultSet.getString("nick2");
                if(nick1.equals(nickname)){
                    chats.add(nick2);
                }else{
                    chats.add(nick1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return chats;
    }

    public void addChat(String nickname1, String nickname2) throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(jdbcURL, username, password);
        String inserting= "INSERT INTO chats(nick1, nick2) VALUES ('" + nickname1 + "', '" + nickname2 + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(inserting);
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

    public Integer sql(String q, String nick){

        int r = 0;

        if (q.equals("getScore")) {
            try {
                if (connection == null)
                    connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement();

                String query = "SELECT score FROM users WHERE nick = '" + nick + "'";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    r = resultSet.getInt("score");
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

    public Integer getChatID(String nickname1, String nickname2){
        int id = 0;
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT chat_id FROM chats WHERE (nick1 = '" + nickname1 + "' OR nick2 = '"
                    + nickname1 + "')" + " AND (nick1 = '" + nickname2 + "' OR nick2 = '" + nickname2 + "')";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getInt("chat_id");
            }
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return id;
    }

    public Boolean checkLastVersion(String v){
        String version;
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM info";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                version  = resultSet.getString("last_version");
                if (version.equals(v)){
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ArrayList<String>> getMessages(String nickname1, String nickname2){
        ArrayList<ArrayList<String>> messages = new ArrayList<>();
        int chatID = getChatID(nickname1, nickname2);
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT msg_text, sender_nick FROM messages WHERE chat_id = " + chatID +
                    " ORDER BY message_id";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String sender  = resultSet.getString("sender_nick");
                String msg = resultSet.getString("msg_text");
                ArrayList<String> add = new ArrayList<>();
                add.add(sender);
                add.add(msg);
                messages.add(add);
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return messages;
    }

    public ArrayList<ArrayList<String>> getLeaderboard(){
        ArrayList<ArrayList<String>> out = new ArrayList<>();
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT nick, score, rank, custom_rank, custom_color FROM users ORDER BY score DESC";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nick = resultSet.getString("nick");
                String score = resultSet.getString("score");
                String title = resultSet.getString("rank");
                String customTitle = resultSet.getString("custom_rank");
                String customColor = resultSet.getString("custom_color");
                ArrayList<String> add = new ArrayList<>();
                add.add(nick);
                add.add(score);
                add.add(title);
                add.add(customTitle);
                add.add(customColor);
                out.add(add);
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return out;
    }

    public void addMessage(String sender, int chat_id, String msg) throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(jdbcURL, username, password);
        String inserting= "INSERT INTO messages(msg_text, chat_id, sender_nick) VALUES ('" +
                msg + "', " + chat_id + ", '" + sender + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(inserting);
    }

    public String getStatus(String nickname){
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT is_online FROM users WHERE nick = '" + nickname +"'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int is = resultSet.getInt("is_online");
                if (is == 1){
                    return "Online";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in connecting to Postgresql server");
            e.printStackTrace();
        }
        return "Offline";
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
        String inserting = "INSERT INTO users(nick, password, title, is_online, rank, score) VALUES ('" +
                nickname + "','" + pass + "', '', 0, 'Adventurer', 0)";
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
