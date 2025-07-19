package Service;

import java.util.List;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    private AccountDao accountDao;

    public AccountService() {
        accountDao = new AccountDao();
    }

    public Account getAccountById(int id) {
        return accountDao.getItemById(id);
    }

    public Account getAccountByUsername(String username) {
        return accountDao.getItemByUsername(username);
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAllItems();
    }

    public Account validateLogin(Account account) {
        if (accountDao.validateLogin(account.getUsername(), account.getPassword())) return account;
        return null;
    }

    public Account createAccount(Account account) throws Exception {
        if (getAccountByUsername(account.getUsername()) != null) {
            throw new Exception("Account already exists");
        }
        Account accountCreated = accountDao.insert(account);
        return accountCreated;
    }

    public Boolean updateAccount(Account account) {
        return accountDao.update(account);
    }

    public Boolean deleteAccount(Account account) {
        return accountDao.delete(account);
    } 
}
