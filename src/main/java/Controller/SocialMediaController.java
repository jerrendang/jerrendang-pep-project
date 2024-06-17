package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import java.util.HashMap;

import io.javalin.Javalin;
import io.javalin.http.Context;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController{
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registrationHandler); // register new user
        app.post("/login", this::loginHandler); // login user

        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesWithAccountIdHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageId);

        // app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void registrationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = this.accountService.addAccount(account);

        if (addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = this.accountService.getAccount(account);

        if (loginAccount != null){
            ctx.json(mapper.writeValueAsString(loginAccount));
        }
        else{
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = this.messageService.newMessage(message);

        if (newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
        }
        else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> allMessages = this.messageService.getAllMessages();

        ctx.json(allMessages);
    }

    private void getMessageByMessageId(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = this.messageService.getMessageByMessageId(message_id);
        if (message != null){
            ObjectMapper mapper = new ObjectMapper();
    
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = this.messageService.deleteMessage(message_id);

        if (deletedMessage != null){
            ObjectMapper mapper = new ObjectMapper();

            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        Map body = mapper.readValue(ctx.body(), Map.class);
        String message_text = (String) body.get("message_text");

        Message updatedMessage = this.messageService.updateMessageByMessageId(message_id, message_text);

        if (updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
        else{
            ctx.status(400);
        }
    }

    private void getMessagesWithAccountIdHandler(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        List <Message> messages = this.messageService.getMessagesByAccountId(account_id);

        ctx.json(messages);
    }
}