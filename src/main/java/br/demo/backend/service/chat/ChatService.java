package br.demo.backend.service.chat;


import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.*;
import br.demo.backend.model.dtos.chat.ChatGetDTO;
import br.demo.backend.model.dtos.chat.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.ChatPrivateGetDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.repository.chat.ChatGroupRepository;
import br.demo.backend.repository.chat.ChatPrivateRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
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

    private ObjectMapper objectMapper;
    private AutoMapper<ChatGroup> mapperGroup;
    private AutoMapper<ChatPrivate> mapperPrivate;
    private AutoMapper<Message> mapperMessage;


    public Collection<ChatPrivateGetDTO> findAllPrivate(Long userId) {
        Collection<ChatPrivate> chats = chatPrivateRepository
                .findChatsByUsersContainingOrderByLastMessage_DateCreateDesc(new User(userId));
        return chats.stream().map(c -> privateToGetDTO(c, userId)).toList();
    }

    public Collection<ChatGroupGetDTO> findAllGroup(Long userId) {
        Collection<ChatGroup> chatGroups = chatGroupRepository
                .findChatsByGroup_UsersContainingOrderByLastMessage_DateCreateDesc(new User(userId));
        return chatGroups.stream().map(c -> groupToGetDTO(c, userId)).toList();
    }

    private ChatPrivateGetDTO privateToGetDTO(ChatPrivate chat, Long userId) {
        ResolveStackOverflow.resolveStackOverflow(chat);
        ChatPrivateGetDTO chatGetDTO = new ChatPrivateGetDTO();
        BeanUtils.copyProperties(chat, chatGetDTO);
        chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
        User destination = chat.getUsers().stream().filter(u -> !u.getId().equals(userId)).findFirst().get();
        chatGetDTO.setPicture(destination.getPicture());
        chatGetDTO.setName(destination.getName());
        return chatGetDTO;
    }

    private ChatGroupGetDTO groupToGetDTO(ChatGroup chat, Long userId) {
        ResolveStackOverflow.resolveStackOverflow(chat);
        ChatGroupGetDTO chatGetDTO = new ChatGroupGetDTO();
        BeanUtils.copyProperties(chat, chatGetDTO);
        chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
        chatGetDTO.setPicture(chat.getGroup().getPicture());
        chatGetDTO.setName(chat.getGroup().getName());
        return chatGetDTO;
    }

    private ChatGetDTO chatToGetDTO(Chat chat, Long userId) {
        if(chat.getType().equals(TypeOfChat.PRIVATE)){
            return privateToGetDTO((ChatPrivate) chat, userId);
        }else{
            return groupToGetDTO((ChatGroup) chat, userId);
        }
    }

    private Integer setQuantityUnvisualized(Chat chat, Long userId) {
        return chat.getMessages().stream().filter(m ->
                m.getDestination().stream().anyMatch(d ->
                        d.getUser().getId().equals(userId) && !d.getVisualized()
                )).toList().size();
    }


    public void updateMessagesToVisualized(ChatPrivate chat, Long userId) {
        chatPrivateRepository.save((ChatPrivate) updateMessagesToVisualized((Chat) chat, userId));
    }

    public void updateMessagesToVisualized(ChatGroup chat, Long userId) {
        chatGroupRepository.save((ChatGroup) updateMessagesToVisualized((Chat) chat, userId));
    }

    private Chat updateMessagesToVisualized(Chat chat, Long userId) {
        chat.setMessages(chat.getMessages().stream().map(m -> {
            m.setDestination(m.getDestination().stream().map(d -> {
                if (d.getUser().getId().equals(userId)) {
                    d.setVisualized(true);
                }
                return d;
            }).toList());
            return m;
        }).toList());
        return chat;
    }

    public Collection<ChatGetDTO> findGroupByName(String name, Long userId) {
        Collection<ChatGroup> chatsGroups = chatGroupRepository.
                findAllByGroup_UsersContaining(new User(userId));
        Collection<ChatPrivate> chatsPrivate = chatPrivateRepository.
                findAllByUsersContaining(new User(userId));
        Collection<Chat> chats = new HashSet<>();
        chats.addAll(chatsGroups);
        chats.addAll(chatsPrivate);
        return chats.stream().map(c -> chatToGetDTO(c, userId)).filter(c -> c.getName().contains(name)).toList();
    }


    public void save(ChatGroup chatGroup) {
        chatGroup.setType(TypeOfChat.GROUP);
        chatGroupRepository.save(chatGroup);
    }

    public void save(ChatPrivate chatPrivate) {
        chatPrivate.setType(TypeOfChat.PRIVATE);
        chatPrivateRepository.save(chatPrivate);
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

    public void updateMessages(Message message, Long chatId) {
        Chat chat = chatRepository.findById(chatId).get();
        message = getMessage(chat, message);
        chat.getMessages().remove(message);
        chat.getMessages().add(message);
        updateLastMessage(chat);
        if(chat.getType().equals(TypeOfChat.PRIVATE)){
            chatPrivateRepository.save((ChatPrivate) chat);
        }else{
            chatGroupRepository.save((ChatGroup)chat );
        }
    }

    public void updateMessages(MultipartFile annex, String messageString, Long chatId) {
        Chat chat = chatRepository.findById(chatId).get();
        Message message = objectMapper.convertValue(messageString, Message.class);
        message = getMessage(chat, message);
        message.setAnnex(new Archive(annex));
        chat.getMessages().remove(message);
        chat.getMessages().add(message);
        updateLastMessage(chat);
        if(chat.getType().equals(TypeOfChat.PRIVATE)){
            chatPrivateRepository.save((ChatPrivate) chat);
        }else{
            chatGroupRepository.save((ChatGroup)chat );
        }
    }

    private Message getMessage(Chat chat, Message message) {
        if(chat.getMessages().contains(message)){
            Message finalMessage = message;
            Message oldMessage = chat.getMessages().stream().filter(m -> m.equals(finalMessage)).findFirst().get();
            mapperMessage.map(message, oldMessage, true);
            message = oldMessage;
            message.setDateUpdate(LocalDateTime.now());
        }else{
            message.setDateCreate(LocalDateTime.now());
        }
        return message;
    }
}
