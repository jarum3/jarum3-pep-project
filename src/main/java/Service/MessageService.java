package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDao messageDao;
    
    public MessageService() {
        messageDao = new MessageDao();
    }

    public Message getMessageById(int id) {
        return messageDao.getItemById(id);
    }

    public List<Message> getAllMessages() {
        return messageDao.getAllItems();
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDao.getAllItemsByAccountId(accountId);
    }

    public Boolean createMessage(Message message, Account account) {
        if (validateMessage(message) && account.account_id == message.posted_by) {
            return messageDao.insert(message);
        }
        return false;
    }

    public Boolean updateMessage(Message message) throws Exception {
        Message original = this.getMessageById(message.getMessage_id());
        if (original == null || (message.posted_by != original.posted_by)) {
            return false;
        }
        original.setMessage_text(message.getMessage_text());
        if (!validateMessage(original)) {
            return false;
        }
        return messageDao.update(original);
    }

    public Boolean deleteMessage(Message message) {
        return messageDao.delete(message);
    }

    private Boolean validateMessage(Message message) {
        return (message.getMessage_text() != null && !message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 254);
    }
}
