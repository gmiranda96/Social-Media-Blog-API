package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Retrieve a list of all accounts
    public List<Account> getAllAccounts() {
        return this.accountDAO.getAllAccounts();
    }

    // Add new account
    public Account addAccount(String username, String password) {
        return accountDAO.addAccount(username, password);
    }

    // Retrieve account by id
    public Account getAccount(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

    // public Account addAccount(Account account, int account_id) {
    //     if (accountDAO.getAccountById(account_id, account) == null) {
    //         return accountDAO.addAccount(account);
    //     } else return null;
    // }

    // Update an existing account
    // public void updateAccount(int account_id, Account account){ 
    //     if (accountDAO.getAccountById(account.getAccount_id(), account) != null) {
    //         accountDAO.updateAccountById(account_id, account);
    //     }
    // }

    // Login to existing account
    public Account loginAccount(int account_id) {
        if (accountDAO.getAccountById(account_id) != null) {
            return accountDAO.getAccountById(account_id);
        } else return null;
    }

    public boolean checkValidId(int id) {
        List<Account> accounts = this.accountDAO.getAllAccounts();

        boolean isValid = false;
        for(Account account : accounts) {
            if(account.getAccount_id() == id) {
                isValid = true;
            }
        }
        return isValid;
    }

    public boolean checkValidRegistration(String username, String password) {
        boolean usernameLength = (username.length() > 0);
        boolean passwordLength = (password.length() > 3);
        boolean usernameValid = true;

        for(Account account : this.accountDAO.getAllAccounts()) {
            if(account.getUsername().equalsIgnoreCase(username)) {
                usernameValid = false;
            }
        }
        return (usernameValid && usernameLength && passwordLength);
    }

    public boolean checkValidPassword(String password) {
        boolean validPassword = true;

        for(Account account : this.accountDAO.getAllAccounts()) {
            if(account.getPassword().length() > 8) {
                validPassword = false;
            }
        }
        return validPassword;
    }

    public int checkValidLogin(String username, String password) {
        int validAccountId = -1;
        List<Account> accounts = this.accountDAO.getAllAccounts();

        for(Account account : accounts) {
            if(account.getUsername().equalsIgnoreCase(username) && account.getPassword().equalsIgnoreCase(password)) {
                validAccountId = account.getAccount_id();
            }
        }
        return validAccountId;
    }

}
