package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService{
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        // verify username and password
        // username should be not empty
        // password should be at least 4 chars
        if (account.getUsername().length() < 1 || account.getPassword().length() < 4){
            return null;
        }

        return this.accountDAO.createAccount(account);
    }

    public Account getAccount(Account account){
        return this.accountDAO.getAccount(account);
    }
}