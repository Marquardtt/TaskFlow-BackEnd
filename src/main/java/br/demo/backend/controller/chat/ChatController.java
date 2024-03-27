package br.demo.backend.controller.chat;


import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.chat.get.ChatGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatPrivateGetDTO;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.chat.post.ChatGroupPostDTO;
import br.demo.backend.model.dtos.chat.post.ChatPrivatePostDTO;
import br.demo.backend.model.dtos.chat.post.MessagePostPutDTO;
import br.demo.backend.service.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatService chatService;

    //precisa ser o owner do grupo
    @PostMapping("/group")
    public ChatGroupGetDTO saveGroup(@RequestBody ChatGroupPostDTO chat){
        return chatService.save(chat);
    }

    //precisa estar em um grupo com essa pessoa ou trabalhar em um mesmo projeto
    @PostMapping("/private")
    public ChatPrivateGetDTO savePrivate(@RequestBody ChatPrivatePostDTO chat){
        return chatService.save(chat);
    }

    //precisa estar logado
    @PatchMapping("/visualized/{chatId}/{userId}")
    public ChatGetDTO upDateToVisualized(@PathVariable Long chatId, @PathVariable String userId){
        return chatService.updateMessagesToVisualized(chatId, userId);
    }

    //precisa estar logado
    @GetMapping("/name/{userId}/{name}")
    public Collection<ChatGetDTO> findByName(@PathVariable String name, @PathVariable String userId){
        return chatService.findGroupByName(name, userId);
    }

    //precisa estar logado
    @GetMapping("/private/{userId}")
    public Collection<ChatPrivateGetDTO> findAllPrivate(@PathVariable String userId){
        return chatService.findAllPrivate(userId);
    }

    //precisa estar logado
    @GetMapping("/group/{userId}")
    public Collection<ChatGroupGetDTO> findAllGroup(@PathVariable String userId){
        return chatService.findAllGroup(userId);
    }

    //precisa ser um usuario do chat
//    @PatchMapping("/message/{chatId}")
//    public void updateMessages(@RequestBody MessagePostPutDTO message, @PathVariable Long chatId){
//        chatService.updateMessages(message, chatId);
//    }

    //precisa ser um usuario do chat
    @PatchMapping("/{chatId}")
    public MessageGetDTO updateMessages(@RequestBody(required = false) MultipartFile annex, @RequestParam String message,
                                        @PathVariable Long chatId) throws JsonProcessingException {
        return chatService.updateMessages(annex, message, chatId);
    }

}
