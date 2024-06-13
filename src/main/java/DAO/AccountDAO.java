package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthStyle;

public class AccountDAO{ 
    public Account createAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "insert into account (username, password) values (?, ?);";
            PreparedStatement psmt = conn.prepareStatement(sql);
            
            psmt.setString(1, account.getUsername());
            psmt.setString(2, account.getPassword());

            psmt.executeUpdate();

            Account newAccount = this.getAccountByUsername(account.getUsername());
            return newAccount;
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Account getAccountByID(int id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "select * from account where account_id=?;";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setInt(1, id);

            ResultSet rs = psmt.executeQuery();
            if (rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return account;
            }
        } catch( SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public Account getAccountByUsername(String username){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "select * from account where username=?;";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setString(1, username);

            ResultSet rs = psmt.executeQuery();

            if (rs.next()){
                Account newAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return newAccount;
            }

        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
}