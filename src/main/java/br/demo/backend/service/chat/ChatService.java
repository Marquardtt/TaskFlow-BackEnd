package br.demo.backend.service.chat;


import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.*;
import br.demo.backend.model.dtos.chat.get.ChatGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatPrivateGetDTO;
import br.demo.backend.model.dtos.chat.post.ChatGroupPostDTO;
import br.demo.backend.model.dtos.chat.post.ChatPrivatePostDTO;
import br.demo.backend.model.dtos.chat.post.MessagePostPutDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.model.ids.DestinationId;
import br.demo.backend.repository.chat.ChatGroupRepository;
import br.demo.backend.repository.chat.ChatPrivateRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ModelToGetDTO;
import br.demo.backend.repository.chat.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private ChatPrivateRepository chatPrivateRepository;
    private ChatGroupRepository chatGroupRepository;
    private MessageRepository messageRepository;

    private ObjectMapper objectMapper;
    private AutoMapper<ChatGroup> mapperGroup;
    private AutoMapper<ChatPrivate> mapperPrivate;
    private AutoMapper<Message> mapperMessage;


    public Collection<ChatPrivateGetDTO> findAllPrivate(String userId) {
        Collection<ChatPrivate> chats = chatPrivateRepository
                .findChatsByUsersContainingOrderByLastMessage_DateCreateDesc(new User(userId));
        return chats.stream().map(c -> privateToGetDTO(c, userId)).toList();
    }

    public Collection<ChatGroupGetDTO> findAllGroup(String userId) {
        Collection<ChatGroup> chatGroups = chatGroupRepository
                .findChatsByGroup_UsersContainingOrderByLastMessage_DateCreateDesc(new User(userId));
        return chatGroups.stream().map(c -> groupToGetDTO(c, userId)).toList();
    }

    private ChatPrivateGetDTO privateToGetDTO(ChatPrivate chat, String userId) {
        ChatPrivateGetDTO chatGetDTO = (ChatPrivateGetDTO) ModelToGetDTO.tranform(chat);
        chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
        User destination = chat.getUsers().stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals(userId)).findFirst().get();
        chatGetDTO.setPicture(destination.getPicture());
        chatGetDTO.setName(destination.getName());
        return chatGetDTO;
    }

    private ChatGroupGetDTO groupToGetDTO(ChatGroup chat, String userId) {
        ChatGroupGetDTO chatGetDTO = (ChatGroupGetDTO) ModelToGetDTO.tranform(chat);
        chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
        chatGetDTO.setPicture(chat.getGroup().getPicture());
        chatGetDTO.setName(chat.getGroup().getName());
        return chatGetDTO;
    }

    private ChatGetDTO chatToGetDTO(Chat chat, String userId) {
        if(chat.getType().equals(TypeOfChat.PRIVATE)){
            return privateToGetDTO((ChatPrivate) chat, userId);
        }else{
            return groupToGetDTO((ChatGroup) chat, userId);
        }
    }

    private Integer setQuantityUnvisualized(Chat chat, String userId) {
        return chat.getMessages().stream().filter(m ->
                m.getDestinations().stream().anyMatch(d ->
                        d.getUser().getUserDetailsEntity().getUsername().equals(userId) && !d.getVisualized()
                )).toList().size();
    }

    public void updateMessagesToVisualized(Message messagePut, String userId) {
        Message message = messageRepository.findById(messagePut.getId()).get();
        message.setDestinations(message.getDestinations().stream().map(d->{
            if(d.getUser().getUserDetailsEntity().getUsername().equals(userId)){
                d.setVisualized(true);
            }
            return d;
        }).toList());
        messageRepository.save(message);
    }

    public Collection<ChatGetDTO> findGroupByName(String name, String userId) {
        Collection<ChatGroup> chatsGroups = chatGroupRepository.
                findAllByGroup_UsersContaining(new User(userId));
        Collection<ChatPrivate> chatsPrivate = chatPrivateRepository.
                findAllByUsersContaining(new User(userId));
        Collection<Chat> chats = new HashSet<>();
        chats.addAll(chatsGroups);
        chats.addAll(chatsPrivate);
        return chats.stream().map(c -> chatToGetDTO(c, userId)).filter(c -> c.getName().contains(name)).toList();
    }


    public void save(ChatGroupPostDTO chatGroup) {
        ChatGroup chat = new ChatGroup();
        BeanUtils.copyProperties(chatGroup, chat);
        chat.setType(TypeOfChat.GROUP);
        chatGroupRepository.save(chat);
    }

    public void save(ChatPrivatePostDTO chatPrivate) {
        ChatPrivate chat = new ChatPrivate();
        BeanUtils.copyProperties(chatPrivate, chat);
        chat.setType(TypeOfChat.PRIVATE);
        chatPrivateRepository.save(chat);
    }

    public void update(ChatGroup chatGroupDto, Boolean patching) {
        ChatGroup chat = patching ?
                chatGroupRepository.findById(chatGroupDto.getId()).get() :
                new ChatGroup();
        mapperGroup.map(chatGroupDto, chat, patching);
        ChatGroup oldChat = chatGroupRepository.findById(chatGroupDto.getId()).get();
        chat.setMessages(oldChat.getMessages());
        chat.setType(TypeOfChat.GROUP);
        chatGroupRepository.save(chat);
    }

    public void update(ChatPrivate chatGroupDto, Boolean patching) {
        ChatPrivate chat = patching ?
                chatPrivateRepository.findById(chatGroupDto.getId()).get() :
                new ChatPrivate();
        mapperPrivate.map(chatGroupDto, chat, patching);
        ChatPrivate oldChat = chatPrivateRepository.findById(chatGroupDto.getId()).get();
        chat.setMessages(oldChat.getMessages());
        chat.setType(TypeOfChat.PRIVATE);
        chatPrivateRepository.save(chat);
    }

    private void updateLastMessage(Chat chat) {
        HashSet<Message> messages = new HashSet<>(chat.getMessages());
        if(messages.isEmpty()) return;
        chat.setLastMessage(messages.stream().max(Comparator.comparing(Message::getDateCreate)).get());
    }


    public void delete(Long id) {
        chatRepository.deleteById(id);
    }

    public void updateMessages(MessagePostPutDTO messageDto, Long chatId) {
        Chat chat = chatRepository.findById(chatId).get();
        Message message = getMessage(chat, messageDto);
        chat.getMessages().remove(message);
        chat.getMessages().add(message);
        updateLastMessage(chat);
        saveTheUpdatableMessage(chat, message);
    }

    public void updateMessages(MultipartFile annex, String messageString, Long chatId) throws JsonProcessingException {
        Chat chat = chatRepository.findById(chatId).get();
        MessagePostPutDTO messageDto = objectMapper.readValue(messageString, MessagePostPutDTO.class);
        Message message = getMessage(chat, messageDto);
        message.setAnnex(new Archive(annex));
        chat.getMessages().remove(message);
        chat.getMessages().add(message);
        updateLastMessage(chat);
        saveTheUpdatableMessage(chat, message);
    }

    private void saveTheUpdatableMessage(Chat chat, Message message){
        Collection<User> users = chat.getType().equals(TypeOfChat.PRIVATE) ?
                ((ChatPrivate) chat).getUsers() : ((ChatGroup) chat).getGroup().getUsers();
        message.setDestinations(users.stream().filter(u-> !u.equals(message.getSender())).map(u -> new Destination(
                            new DestinationId(u.getId(), message.getId()), u, message, false)
                    ).toList());
        if(chat.getType().equals(TypeOfChat.PRIVATE)){
            chatPrivateRepository.save((ChatPrivate) chat);
        }else{
            chatGroupRepository.save((ChatGroup)chat );
        }
    }


    private Message getMessage(Chat chat, MessagePostPutDTO messageDto) {
        Message message;
        if(chat.getMessages().stream().anyMatch(m -> m.getId().equals(messageDto.getId()))){
            message = messageRepository.findById(messageDto.getId()).get();
            mapperMessage.map(messageDto, message, true);
            message.setDateCreate(message.getDateCreate());
            message.setDateUpdate(LocalDateTime.now());
        }else{
            message = new Message();
            mapperMessage.map(messageDto, message, true);
            message.setDateCreate(LocalDateTime.now());
        }
        return message;
    }
}
