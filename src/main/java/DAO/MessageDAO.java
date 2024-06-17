package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessageDAO{
    // create new message \\
    // get all messages 
    // get message with message id
    // delete message with message id
    // update message with message id
    // get all messages foxr user id
    // public Message createMessage(String message_text, int account_id, int time_posted_epoch){
    public Message createMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?);";
            PreparedStatement psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            psmt.setInt(1, message.getPosted_by());
            psmt.setString(2, message.getMessage_text());
            psmt.setLong(3, message.getTime_posted_epoch());

            psmt.executeUpdate();

            ResultSet rs = psmt.getGeneratedKeys();

            if (rs.next()){
                int generated_message_id = (int) rs.getLong(1);

                message.setMessage_id(generated_message_id);
                return message;
            }
        } catch (SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> res = new LinkedList<>();

        try{
            String sql = "select * from message;";
            PreparedStatement psmt = conn.prepareStatement(sql);

            ResultSet rs = psmt.executeQuery();

            while (rs.next()){
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                Long time_posted_epoch = rs.getLong("time_posted_epoch");

                res.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return res;
    }

    public Message getMessageByMessageId(int message_id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "select * from message where message_id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setInt(1, message_id);

            ResultSet rs = psmt.executeQuery();

            if (rs.next()){
                String message_text = rs.getString("message_text");
                int posted_by = rs.getInt("posted_by");
                Long time_posted_epoch = rs.getLong("time_posted_epoch");

                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                return message;
            }
        } catch(SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public Message deleteMessage(int message_id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String querysql = "select * from message where message_id=?";
            PreparedStatement querypsmt = conn.prepareStatement(querysql);

            querypsmt.setInt(1, message_id);

            ResultSet queryRS = querypsmt.executeQuery();

            if (queryRS.next()){
                String sql = "delete from message where message_id=?";
                PreparedStatement psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                psmt.setInt(1, message_id);

                psmt.executeUpdate();

                int posted_by = queryRS.getInt("posted_by");
                String message_text = queryRS.getString("message_text");
                Long time_posted_epoch = queryRS.getLong("time_posted_epoch");

                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                return message;
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    // update message with message id
    public Message updateMessageByMessageId(int message_id, String message_text){
        // message id exists
        // message text not blank
        // message text not over 255 chars
        if (this.getMessageByMessageId(message_id) == null || message_text.length() < 1 || message_text.length() > 255){
            return null;
        }
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "update message set message_text=? where message_id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setString(1, message_text);
            psmt.setInt(2, message_id);

            psmt.executeUpdate();

            String querysql = "select * from message where message_id=?";
            PreparedStatement querypsmt = conn.prepareStatement(querysql);

            querypsmt.setInt(1, message_id);

            ResultSet query = querypsmt.executeQuery();

            if (query.next()){
                int posted_by = query.getInt("posted_by");
                String newMessageText = query.getString("message_text");
                Long time_posted_epoch = query.getLong("time_posted_epoch");

                Message message = new Message(message_id, posted_by, newMessageText, time_posted_epoch);
                return message;
            }
        } catch(SQLException e){
            System.out.println(e);
        }

        return null;
    }

    // get all messages with account id
    public List<Message> getMessagesByAccountId(int account_id){
        List<Message> res = new LinkedList<>();

        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "select * from message where posted_by=?";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setInt(1, account_id);

            ResultSet rs = psmt.executeQuery();

            while (rs.next()){
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                Long time_posted_epoch = rs.getLong("time_posted_epoch");

                res.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
        } catch(SQLException e){
            System.out.println(e);
        }

        return res;
    }
}