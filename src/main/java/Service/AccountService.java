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
        return accountDao.validateLogin(account.getUsername(), account.getPassword());
    }

    public Boolean validCombo(Account account) {
        if (account.getUsername().trim().isEmpty()) return false;
        if (account.getPassword().trim().length() < 4) return false;
        if (accountDao.usernameExists(account.getUsername().trim())) return false;
        return true;
    }

    public Account createAccount(Account account) throws Exception {
        if (validCombo(account)) {
            Account accountCreated = accountDao.insert(account);
            return accountCreated;
        }
        else return null;
    }

    public Boolean updateAccount(Account account) {
        return accountDao.update(account);
    }

    public Boolean deleteAccount(Account account) {
        return accountDao.delete(account);
    } 
}
