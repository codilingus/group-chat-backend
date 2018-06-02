package messagesRepository;


import messages.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessagesRepository extends CrudRepository<Message, Integer> {
}
