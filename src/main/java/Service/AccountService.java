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

    public Boolean validateLogin(Account account) {
        return accountDao.validateLogin(account.getUsername(), account.getPassword());
    }

    public Boolean createAccount(Account account) throws Exception {
        if (getAccountByUsername(account.getUsername()) != null) {
            throw new Exception("Account already exists");
        }
        Boolean accountCreated = accountDao.insert(account);
        return accountCreated;
    }

    public Boolean updateAccount(Account account) {
        return accountDao.update(account);
    }

    public Boolean deleteAccount(Account account) {
        return accountDao.delete(account);
    } 
}
