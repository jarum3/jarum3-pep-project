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

    public Message createMessage(Message message, Account account) {
        if (validateMessage(message) && account.account_id == message.posted_by) {
            return messageDao.insert(message);
        }
        return null;
    }

    public Message updateMessage(Message message) throws Exception {
        Message original = this.getMessageById(message.getMessage_id());
        if (original == null) {
            return null;
        }
        original.setMessage_text(message.getMessage_text());
        if (!validateMessage(original)) {
            return null;
        }
        if (messageDao.update(original)) return original;
        return null;
    }

    public Boolean deleteMessage(Message message) {
        return messageDao.delete(message);
    }

    public Boolean validateMessage(Message message) {
        return (message.getMessage_text() != null && !message.getMessage_text().trim().isEmpty() && message.getMessage_text().length() <= 255);
    }
}
