package br.demo.backend.service.chat;


import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.*;
import br.demo.backend.model.dtos.chat.get.ChatGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatPrivateGetDTO;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.chat.post.ChatGroupPostDTO;
import br.demo.backend.model.dtos.chat.post.ChatPrivatePostDTO;
import br.demo.backend.model.dtos.chat.post.MessagePostPutDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.ids.DestinationId;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.chat.ChatGroupRepository;
import br.demo.backend.repository.chat.ChatPrivateRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.service.NotificationService;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.repository.chat.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private ChatPrivateRepository chatPrivateRepository;
    private ChatGroupRepository chatGroupRepository;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;
    private ObjectMapper objectMapper;
    private AutoMapper<Message> mapperMessage;


    public Collection<ChatPrivateGetDTO> findAllPrivate() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        Collection<ChatPrivate> chats = chatPrivateRepository
                .findAllByUsersContainingOrderByLastMessage_DateCreateDesc(user);
        return chats.stream().map(c -> privateToGetDTO(c, username)).toList();
    }

    public Collection<ChatGroupGetDTO> findAllGroup() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        Collection<ChatGroup> chatGroups = chatGroupRepository
                .findAllByGroup_OwnerOrGroup_UsersContainingOrderByLastMessage_DateCreateDesc(user, user);
        return chatGroups.stream().map(this::groupToGetDTO).toList();
    }

    private ChatPrivateGetDTO privateToGetDTO(ChatPrivate chat, String userId) {
        ChatPrivateGetDTO chatGetDTO = (ChatPrivateGetDTO) ModelToGetDTO.tranform(chat);
        //getting the user that is not the user that is logged
        User destination = chat.getUsers().stream().filter(u ->
                !u.getUserDetailsEntity().getUsername().equals(userId)).findFirst().get();
        chatGetDTO.setPicture(destination.getPicture());
        chatGetDTO.setName(destination.getName());
        return chatGetDTO;
    }

    private ChatGroupGetDTO groupToGetDTO(ChatGroup chat) {
        ChatGroupGetDTO chatGetDTO = (ChatGroupGetDTO) ModelToGetDTO.tranform(chat);
        chatGetDTO.setPicture(chat.getGroup().getPicture());
        chatGetDTO.setName(chat.getGroup().getName());
        return chatGetDTO;
    }

    public ChatGetDTO updateMessagesToVisualized(Long chatId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();

        Chat chat = chatRepository.findById(chatId).get();
        setAllMessagesToVisualized(chat, user);
        //set all notifications to visualized
        user.getNotifications().stream().filter(n -> n.getType().equals(TypeOfNotification.CHAT)
                && n.getLink().contains("chat/" + chatId)).forEach(n -> {
            n.setVisualized(true);
            notificationService.updateNotification(n);
        });
        return ModelToGetDTO.tranform(chat);
    }

    private void setAllMessagesToVisualized(Chat chat, User user) {
        //set all messages to visualized
        Collection<Message> messages = chat.getMessages().stream().peek(m -> {
            m.setDestinations(m.getDestinations().stream().peek(d -> {
                //if the user is the destination of the message, set the message to visualized
                if (d.getUser().equals(user)) {
                    d.setVisualized(true);
                }
            }).toList());
            messageRepository.save(m);
        }).toList();
        chat.setMessages(messages);
    }

    public ChatGroupGetDTO save(ChatGroupPostDTO chatGroup) {
        ChatGroup chat = new ChatGroup();
        BeanUtils.copyProperties(chatGroup, chat);
        chat.setType(TypeOfChat.GROUP);
        return (ChatGroupGetDTO) ModelToGetDTO.tranform(chatGroupRepository.save(chat));
    }

    public ChatPrivateGetDTO save(ChatPrivatePostDTO chatPrivate) {
        ChatPrivate chat = new ChatPrivate();
        BeanUtils.copyProperties(chatPrivate, chat);
        chat.setType(TypeOfChat.PRIVATE);
        return (ChatPrivateGetDTO) ModelToGetDTO.tranform(chatPrivateRepository.save(chat));
    }

    //that method update the last message of a chat
    private void updateLastMessage(Chat chat) {
        HashSet<Message> messages = new HashSet<>(chat.getMessages());
        if (messages.isEmpty()) return;
        chat.setLastMessage(messages.stream().max(Comparator.comparing(Message::getDateCreate)).get());
    }


    public void delete(Long id) {
        chatRepository.deleteById(id);
    }

//    public void updateMessages(MessagePostPutDTO messageDto, Long chatId) {
//        Chat chat = chatRepository.findById(chatId).get();
//        Message message = getMessage(chat, messageDto);
//        chat.getMessages().remove(message);
//        chat.getMessages().add(message);
//        updateLastMessage(chat);
//        saveTheUpdatableMessage(chat, message);
//    }

    //that method update the messages list, so it update a message or insert a new one
    public MessageGetDTO updateMessages(MultipartFile annex, String messageString, Long chatId) throws JsonProcessingException {
        Chat chat = chatRepository.findById(chatId).get();
        //map the string to a message
        MessagePostPutDTO messageDto = objectMapper.readValue(messageString, MessagePostPutDTO.class);
        Message message = getMessage(chat, messageDto);
        //set the annex to the message
        if (annex != null) {
            message.setAnnex(new Archive(annex));
        }
        return saveTheUpdatableMessage(chat, message);
    }

    private MessageGetDTO saveTheUpdatableMessage(Chat chat, Message message) {
        createDestinations(chat, message);
        //saving the chat with the new message
        if (chat.getType().equals(TypeOfChat.PRIVATE)) {
            chat = chatPrivateRepository.save((ChatPrivate) chat);
        } else {
            chat = chatGroupRepository.save((ChatGroup) chat);
        }
        Message messageWithId = getmessageWithId(chat, message);
        //generating the notification for each user of the chat
        MessageGetDTO messageGetDTO = ModelToGetDTO.tranform(messageWithId);
        notificationService.generateNotification(TypeOfNotification.CHAT, messageGetDTO.getId(), chat.getId());
        return messageGetDTO;
    }

    private Message getmessageWithId(Chat chat, Message message) {
        if (message.getId() == null) {
            return new ArrayList<>(chat.getMessages()).get(chat.getMessages().size() - 1);
        }
        return message;
    }

    private void createDestinations(Chat chat, Message message) {
        //getting the users of a chat
        Collection<User> users = null;
        if (chat.getType().equals(TypeOfChat.PRIVATE)) {
            users = ((ChatPrivate) chat).getUsers();
        } else {
            users = ((ChatGroup) chat).getGroup().getUsers();
        }
        //creating a destination for each user of the chat
        message.setDestinations(users.stream().filter(u -> !u.equals(message.getSender())).map(u -> new Destination(
                new DestinationId(u.getId(), message.getId()), u, message, false)
        ).toList());
    }


    //this method recognize if is a new message or a update message
    private Message getMessage(Chat chat, MessagePostPutDTO messageDto) {
        Message message = null;
        try {
            message = chat.getMessages().stream().filter(m -> m.getId().equals(messageDto.getId())).findFirst().get();
            mapperMessage.map(messageDto, message, true);
            message.setDateCreate(message.getDateCreate());
            message.setDateUpdate(LocalDateTime.now());
        } catch (NoSuchElementException e) {
            message = new Message();
            mapperMessage.map(messageDto, message, true);
            message.setDateCreate(LocalDateTime.now());
            chat.getMessages().add(message);
            //that sets the last message of the chat to the new message
            updateLastMessage(chat);
        }
        return message;
    }
}
