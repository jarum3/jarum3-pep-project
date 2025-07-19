package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao implements BaseDao<Account> {

        private Account rsToAccount(ResultSet rs) throws SQLException {
            int accountId = rs.getInt("account_id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            return new Account(accountId, username, password);
    }

    public Account validateLogin(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ?;";
        Connection conn = ConnectionUtil.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account found = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                if (Objects.equals(password, found.getPassword())) return found; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean usernameExists (String username) {
        String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return (rs.getInt(1) > 0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Account getItemById(int id) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rsToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getItemByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rsToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAllItems() {
        String sql = "SELECT * FROM account;";
        Connection conn = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(rsToAccount(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account insert(Account item) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, item.getUsername());
            ps.setString(2, item.getPassword());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                return new Account(id, item.getUsername(), item.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean update(Account item) {
        String sql = "UPDATE account SET username = ?, password = ? WHERE account_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, item.getUsername());
            ps.setString(2, item.getPassword());
            ps.setInt(3, item.getAccount_id());
            int updated = ps.executeUpdate();
            return (updated > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Account item) {
        String sql = "DELETE FROM account WHERE account_id = ?;";
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getAccount_id());
            int updated = ps.executeUpdate();
            return (updated > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
