package DAO;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao implements BaseDao<Message> {

    private Message rsToMessage(ResultSet rs) throws SQLException {
        int messageId = rs.getInt("message_id");
        int postedBy = rs.getInt("posted_by");
        String messageText = rs.getString("message_text");
        long timePostedEpoch = rs.getLong("time_posted_epoch");
        return new Message(messageId, postedBy, messageText, timePostedEpoch);
    }

    public List<Message> getAllItemsByAccountId(int accountId) {
        String sql = "SELECT * FROM message WHERE posted_by = ?;";
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(rsToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message getItemById(int id) {
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rsToMessage(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getAllItems() {
        String sql = "SELECT * FROM message;";
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(rsToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message insert(Message item) {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, item.getPosted_by());
            ps.setString(2, item.getMessage_text());
            ps.setLong(3, item.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                return new Message(id, item.getPosted_by(), item.getMessage_text(), item.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    public Boolean update(Message item) {
        String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ?, WHERE message_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getPosted_by());
            ps.setString(2, item.getMessage_text());
            ps.setLong(3, item.getTime_posted_epoch());
            ps.setInt(4, item.getMessage_id());

            int updated = ps.executeUpdate();
            return (updated > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(Message item) {
        String sql = "DELETE FROM message WHERE message_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getMessage_id());

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
