package br.demo.backend.service.chat;


import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.service.ProjectService;
import br.demo.backend.service.ResolveStackOverflow;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private ProjectService projectService;


    public Collection<Chat> findAllPrivate(Long userId) {
        Collection<Chat> chats = chatRepository
                .findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(userId), TypeOfChat.PRIVATE);
        setQuantityUnvisualized(chats, userId);
        return chats.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();

    }

    private void setQuantityUnvisualized(Collection<Chat> chats, Long userId) {
        chats.forEach(
                chat ->
                        chat.setQuantitityUnvisualized(
                                chat.getMessages().stream().filter(
                                                message -> !message.getVisualized() && !message.getUser().getId().equals(userId))
                                        .count()));
    }

    public Collection<Chat> findAllGroup(Long userId) {
        Collection<Chat> chats = chatRepository
                .findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(userId), TypeOfChat.GROUP);
        chats.forEach(ResolveStackOverflow::resolveStackOverflow);
        setQuantityUnvisualized(chats, userId);
        return chats;
    }


    public void updateMessagesToVisualized(Chat chat, Long userId) {
        Collection<Message> messages = chat.getMessages().stream().map(m -> {
            if (!m.getUser().getId().equals(userId)) m.setVisualized(true);
            return m;
        }).toList();
        chat.setMessages(messages);
        chatRepository.save(chat);
    }

    public Collection<Chat> findByName(String name) {
        Collection<Chat> chats = chatRepository.findChatsByNameContains(name);
        return chats.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public Chat findOne(Long id) {
        Chat chat = chatRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(chat);
    }

    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public void update(Chat chat) {
        HashSet<Message> messages = new HashSet<>(chat.getMessages());
        chat.setLastMessage(messages.stream().max(Comparator.comparing(Message::getDateTime)).get());
        chatRepository.save(chat);
    }

    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
