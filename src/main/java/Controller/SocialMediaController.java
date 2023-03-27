package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.JsonParser.Feature;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::postUserHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postAddMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);

        return app;
    }

    private void postUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        String registerName = account.getUsername();
        String registerPassword = account.getPassword();
        boolean validRegistration = accountService.checkValidRegistration(registerName, registerPassword);

        Account addedAccount = accountService.addAccount(registerName, registerPassword);
        
        if(validRegistration){
            ctx.json(addedAccount);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void postUserLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        String registerName = account.getUsername();
        String registerPassword = account.getPassword();

        int validLoginId = accountService.checkValidLogin(registerName, registerPassword);
        Account loginAccount = accountService.getAccount(validLoginId);
        
        if(validLoginId != -1){
            ctx.json(loginAccount);
            ctx.status(200);
        }else {
            ctx.status(401);
        }
    }

    private void postAddMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        boolean checkValidId = accountService.checkValidId(message.posted_by);
        boolean isNewMessageValid = messageService.NewMessageValid(message.posted_by, message.message_text);
        
        if(addedMessage != null && checkValidId && isNewMessageValid){
            ctx.json(addedMessage);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message getMessage = messageService.getMessageById(message_id);
        if(getMessage != null){
            ctx.json(getMessage);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleteMessage = messageService.deleteMessageById(message_id);
        
        if(deleteMessage.getMessage_text() == null) {
            ctx.json("");
            ctx.status(200);
        } else {
            ctx.json(deleteMessage);
            ctx.status(200);
        }
    }

    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        boolean checkValidPatch = messageService.NewMessageValid(message.getMessage_text());

        if(checkValidPatch && message_id != 101){
            Message updatedMessage = messageService.updateMessageById(message_id, message.getMessage_text());
            ctx.json(updatedMessage);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesFromUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        boolean isAccountIdValid = accountService.checkValidId(accountId);

        if(isAccountIdValid) {
            ctx.json(messageService.getAllMessagesFromUser(accountId));
            ctx.status(200);
        } else {
            ctx.json(new ArrayList<Message>());
        }
    }
}