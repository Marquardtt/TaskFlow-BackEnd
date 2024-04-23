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
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatService chatService;

    //precisa ser o owner do grupo
    @PostMapping("/group/{groupId}")

    public ChatGroupGetDTO saveGroup(@RequestBody ChatGroupPostDTO chat){
        return chatService.save(chat);
    }

    //precisa estar em um grupo com essa pessoa ou trabalhar em um mesmo projeto
    @PostMapping("/private/{userId}")
    public ChatPrivateGetDTO savePrivate(@RequestBody ChatPrivatePostDTO chat){
        return chatService.save(chat);
    }

    //precisa estar logado
    @PatchMapping("/visualized/{chatId}")
    public ChatGetDTO upDateToVisualized(@PathVariable Long chatId){
        return chatService.updateMessagesToVisualized(chatId);
    }

    //precisa estar logado
    @GetMapping("/private")
    public Collection<ChatPrivateGetDTO> findAllPrivate(){
        return chatService.findAllPrivate();
    }

    //precisa estar logado
    @GetMapping("/group")
    public Collection<ChatGroupGetDTO> findAllGroup(){
        return chatService.findAllGroup();
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
