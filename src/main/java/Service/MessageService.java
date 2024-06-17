package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Account;
import Model.Message;

import java.util.LinkedList;
import java.util.List;

public class MessageService{
    MessageDAO messageDAO;
    AccountDAO accountDAO;
    
    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message newMessage(Message message){
// The creation of the message will be successful if and only if the message_text is not blank, 
// is under 255 characters, and posted_by refers to a real, existing user. 
// If successful, the response body should contain a JSON of the message, including its message_id. 
// The response status should be 200, wxhich is the default. The new message should be persisted to the database.
        String message_text = message.getMessage_text();
        int posted_by = message.getPosted_by();

        if (message_text.length() < 1 || message_text.length() > 255 || this.accountDAO.getAccountByID(posted_by) == null){
            return null;
        }

        return this.messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageByMessageId(int message_id){
        return this.messageDAO.getMessageByMessageId(message_id);
    }

    public Message deleteMessage(int message_id){
        return this.messageDAO.deleteMessage(message_id);
    }

    public Message updateMessageByMessageId(int message_id, String message_text){
        return this.messageDAO.updateMessageByMessageId(message_id, message_text);
    }

    public List<Message> getMessagesByAccountId(int account_id){
        if (this.accountDAO.getAccountByID(account_id) != null){
            return this.messageDAO.getMessagesByAccountId(account_id);
        }
        
        return new LinkedList<>();
    }
}