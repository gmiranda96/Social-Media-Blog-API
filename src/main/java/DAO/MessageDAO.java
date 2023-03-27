package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();
    
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );

                messages.add(message);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public List<Message> getAllMessagesFromUser(int message_id){
        
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

            List<Message> allMessagesFromUser = new ArrayList<>();

            while(rs.next()){
                Message singleMessage = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                allMessagesFromUser.add(singleMessage);
            }
            return allMessagesFromUser;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message getMessageById(int message_id){

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                    );
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message addMessage(int posted_by, String message_text, Long time_posted_epoch) {
        Message addmessage = new Message();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            // PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, posted_by);
            ps.setString(2, message_text);
            ps.setLong(3, time_posted_epoch);
            ps.executeUpdate();

            // ResultSet rs = ps.getGeneratedKeys();
            // if(rs.next()){
            //     int generated_message_id = (int) rs.getLong(1);
            //     if (message.getMessage_text() == "") {
            //         return null;
            //     } else
            //     return new Message(
            //         generated_message_id, 
            //         message.getPosted_by(), 
            //         message.getMessage_text(),
            //         message.getTime_posted_epoch()
            //     );
            // }

            String sql2 = "SELECT * FROM message WHERE posted_by = ? AND message_text = ? AND time_posted_epoch = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);

            ps2.setInt(1, posted_by);
            ps2.setString(2, message_text);
            ps2.setLong(3, time_posted_epoch);

            ResultSet rs = ps2.executeQuery();
            if(rs.next()){
                addmessage.setMessage_id(rs.getInt("message_id"));
                addmessage.setPosted_by(rs.getInt("posted_by"));
                addmessage.setMessage_text(rs.getString("message_text"));
                addmessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return addmessage;
    }

    public Message updateMessageById(int message_id, String message_text) {
        Message updatemessage = new Message();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            ps.executeUpdate();

            String sql2 = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);

            ps2.setInt(1, message_id);

            ResultSet rs = ps2.executeQuery();
            rs.next();

            updatemessage = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return updatemessage;
    }

    public Message deleteMessageById(int message_id) {
        Message message = new Message();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql);
            ps1.setInt(1, message_id);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );


            String sql2 = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, message_id);
            ps2.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } 
        
        return message;
    }
}
