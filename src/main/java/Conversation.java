import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.User;

import java.util.List;

public class Conversation {

    private int id;
    private String name;
    private List<Message> messages;
    private List<User> users;

    public Conversation(int id, String name, List<Message> messages, List<User> users) {
        this.id = id;
        this.name = name;
        this.messages = messages;
        this.users = users;
    }

    public Conversation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
