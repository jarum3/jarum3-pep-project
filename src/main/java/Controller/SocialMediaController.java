package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
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
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages",
                this::getMessagesByAccountId);

        return app;

    }

    private void registerAccount(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Account account = map.readValue(ctx.body(), Account.class);
        try {
            Boolean registered = accountService.createAccount(account);
            ctx.json(map.writeValueAsString(registered));
        } catch(Exception e) {
            ctx.status(400);
        }
    }

    private void loginAccount(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Account account = map.readValue(ctx.body(), Account.class);

            Account loggedIn = accountService.validateLogin(account);
            if (loggedIn != null) {
                ctx.json(map.writeValueAsString(loggedIn));
                ctx.sessionAttribute("logged_in_account", loggedIn);
                ctx.json(loggedIn);
            }
            else {
                ctx.status(400);
            }
    }

    private void createMessage(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Message message = map.readValue(ctx.body(), Message.class);
        Account account = accountService.getAccountById(message.getPosted_by());
        if (account != null) {
            Message createdMessage = messageService.createMessage(message, account);
            ctx.json(createdMessage);
        } else ctx.status(400);
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(id);
            if (message != null) ctx.json(message);
            else {
                ctx.status(200);
                ctx.result("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void deleteMessageById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));

            Message message = messageService.getMessageById(id);
            if (message != null) {
                messageService.deleteMessage(message);
                ctx.status(200);
                ctx.json(message);
            } else ctx.status(200);
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void updateMessageById(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        Message message = map.readValue(ctx.body(), Message.class);
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            message.setMessage_id(id);

            Message updated = messageService.updateMessage(message);
            if (updated != null) {
                ctx.json(updated);
            }
            else {
                ctx.status(400);
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        } catch (Exception e) {
            ctx.status(400);
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            if (!messages.isEmpty()) {
                ctx.json(messages);
            } else {
                ctx.json(messages); // Empty response
                ctx.status(200);
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }
}