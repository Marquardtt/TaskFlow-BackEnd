package br.demo.backend.service.chat;


import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.chat.ChatGetDTO;
import br.demo.backend.model.dtos.chat.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.ChatPrivateGetDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.repository.chat.ChatGroupRepository;
import br.demo.backend.repository.chat.ChatPrivateRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private ChatPrivateRepository chatPrivateRepository;
    private ChatGroupRepository chatGroupRepository;

    private AutoMapper<Chat> mapper;
    private AutoMapper<ChatGroup> mapperGroup;
    private AutoMapper<ChatPrivate> mapperPrivate;


    public Collection<ChatPrivateGetDTO> findAllPrivate(Long userId) {
        Collection<ChatPrivate> chats = chatPrivateRepository
                .findChatsByUsersContainingOrderByMessagesDateTimeDesc(new User(userId));
        return chats.stream().map(c -> {
                    ChatPrivate chat = ResolveStackOverflow.resolveStackOverflow(c);
                    ChatPrivateGetDTO chatGetDTO = new ChatPrivateGetDTO();
                    BeanUtils.copyProperties(chat, chatGetDTO);
                    chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
                    return chatGetDTO;
                }
        ).toList();

    }

    public Collection<ChatGroupGetDTO> findAllGroup(Long userId) {
        Collection<ChatGroup> chatGroups = chatGroupRepository
                .findChatsByGroup_UsersContainingOrderByMessagesDateTimeDesc(new User(userId));
        return chatGroups.stream().map(c -> {
            ChatGroup chat = ResolveStackOverflow.resolveStackOverflow(c);
            ChatGroupGetDTO chatGetDTO = new ChatGroupGetDTO();
            BeanUtils.copyProperties(chat, chatGetDTO);
            chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
            return chatGetDTO;
        }).toList();
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
        Collection<ChatGroup> chatsGroups = chatGroupRepository.findAll();
        Collection<ChatPrivate> chatsPrivate = chatPrivateRepository.findAll();

        return Stream.concat(chatsGroups.stream(), chatsPrivate.stream())
                .filter(c -> {
                    if (c.getType().equals(TypeOfChat.GROUP)) {
                        return ((ChatGroup) c).getGroup().getName().contains(name) &&
                                ((ChatGroup) c).getGroup().getUsers().contains(new User(userId));
                    } else {
                        return ((ChatPrivate) c).getUsers().stream().anyMatch(u -> !u.getId().equals(userId) &&
                                u.getName().contains(name) && ((ChatPrivate) c).getUsers().contains(new User(userId)));
                    }
                }).map(c -> {
                            if (c.getType().equals(TypeOfChat.GROUP)) {
                                ChatGroup chat = ResolveStackOverflow.resolveStackOverflow((ChatGroup) c);
                                ChatGroupGetDTO chatGetDTO = new ChatGroupGetDTO();
                                BeanUtils.copyProperties(chat, chatGetDTO);
                                chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
                                return chatGetDTO;
                            } else {
                                ChatPrivate chat = ResolveStackOverflow.resolveStackOverflow((ChatPrivate) c);
                                ChatPrivateGetDTO chatGetDTO = new ChatPrivateGetDTO();
                                BeanUtils.copyProperties(chat, chatGetDTO);
                                chatGetDTO.setQuantityUnvisualized(setQuantityUnvisualized(chat, userId));
                                return chatGetDTO;
                            }
                        }
                ).toList();

    }


    public void save(ChatGroup chatGroup) {
        chatGroupRepository.save(chatGroup);
    }

    public void save(ChatPrivate chatPrivate) {
        chatPrivateRepository.save(chatPrivate);
    }

    public void update(ChatGroup chatGroupDto, Boolean patching) {
        ChatGroup chat = patching ?
                chatGroupRepository.findById(chatGroupDto.getId()).get() :
                new ChatGroup();
        mapperGroup.map(chatGroupDto, chat, patching);
        chat = (ChatGroup) updateLastMessage(chat);
        chatGroupRepository.save(chat);
    }

    public void update(ChatPrivate chatGroupDto, Boolean patching) {
        ChatPrivate chat = patching ?
                chatPrivateRepository.findById(chatGroupDto.getId()).get() :
                new ChatPrivate();
        mapperPrivate.map(chatGroupDto, chat, patching);
        chat = (ChatPrivate) updateLastMessage(chat);
        chatPrivateRepository.save(chat);
    }

    private Chat updateLastMessage(Chat chat) {
        HashSet<Message> messages = new HashSet<>(chat.getMessages());
        chat.setLastMessage(messages.stream().max(Comparator.comparing(Message::getDateTime)).get());
        return chat;
    }


    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
