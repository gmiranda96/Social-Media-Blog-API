package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Retrieve a list of all messages
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    // Get all messages from a particular user
    public List<Message> getAllMessagesFromUser(int message_id) {
        return this.messageDAO.getAllMessagesFromUser(message_id);
    }
    
    // Add new message
    public Message addMessage(int posted_by, String message_text, Long time_posted_epoch) {
        return this.messageDAO.addMessage(posted_by, message_text, time_posted_epoch);
    }

    // Retrieve a message by id
    public Message getMessageById(int message_id) {
        return this.messageDAO.getMessageById(message_id);
    }

    // Delete a message by id
    public Message deleteMessageById(int message_id) {
        return this.messageDAO.deleteMessageById(message_id);
    }

    // Update a message by id
    public Message updateMessageById(int message_id, String message_text){
        return this.messageDAO.updateMessageById(message_id, message_text);
    }

    public AccountService accountService = new AccountService();

    public boolean PostedByValid(int posted_by) {
        boolean isPostedByIdValid = accountService.checkValidId(posted_by);
        return isPostedByIdValid;
    }

    public boolean NewMessageValid(int posted_by, String message_text) {
        boolean isPostedByIdValid = accountService.checkValidId(posted_by);
        boolean isMessageFilled = (message_text.length() > 0 && message_text.length() < 255);
        return (isPostedByIdValid && isMessageFilled);
    }

    public boolean NewMessageValid(String message_text) {
        return (message_text.length() > 0 && message_text.length() < 255);
    }

    public boolean MessageIdValid(int id) {
        List<Message> messages = this.messageDAO.getAllMessages();

        boolean isValid = false;
        for(Message message : messages) {
            if(message.getMessage_id() == id) {
                isValid = true;
            }
        }
        return isValid;
    }
}
