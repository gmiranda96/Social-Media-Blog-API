package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();

    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password")
                    );
                accounts.add(account);
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountById(int account_id){
       
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password")
                );
                return account;
            }
            // while(rs.next()){
            //     Account getaccount = new Account(
            //         rs.getInt("account_id"), 
            //         rs.getString("username"),
            //         rs.getString("password")
            //     );
            //     return getaccount;
            // }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account addAccount(String username, String password) {
        Account addaccount = new Account();
       
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            // ResultSet pkeyResultSet = ps.getGeneratedKeys();
            // if(pkeyResultSet.next()){
            //     int generated_account_id = (int) pkeyResultSet.getLong(1);
            //     if (username == "" || password.length() < 4) {
            //         return null;
            //     } else 
            //     return new Account(
            //         generated_account_id, 
            //         username, 
            //         password
            //     );
            // }

            String sql2 = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);

            ps2.setString(1, username);
            ps2.setString(2, password);

            ResultSet rs = ps2.executeQuery();
            if(rs.next()) {
                addaccount.setAccount_id(rs.getInt("account_id"));
                addaccount.setUsername(rs.getString("username"));
                addaccount.setPassword(rs.getString("password"));
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
            return addaccount;
    }

    // public void updateAccountById(Account account) {
    //     Account account = new Account();
        
    //     try {
    //         String sql = "UPDATE account SET username = ? AND password = ? WHERE account_id = ?";
    //         PreparedStatement ps = connection.prepareStatement(sql);
            
    //         ps.setString(1, account.getUsername());
    //         ps.setString(2, account.getPassword());
    //         ps.setInt(3, );
    //         ps.executeUpdate();

    //         String sql2 = "SELECT * FROM account WHERE account_id = ?";
    //         PreparedStatement ps2 = connection.prepareStatement(sql2);

    //     } catch(SQLException e) {
    //         System.out.println(e.getMessage());
    //     }
    // }
}
