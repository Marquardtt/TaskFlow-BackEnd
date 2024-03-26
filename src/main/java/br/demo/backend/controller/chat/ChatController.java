package br.demo.backend.controller.chat;


import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.chat.get.ChatGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatPrivateGetDTO;
import br.demo.backend.model.dtos.chat.post.ChatGroupPostDTO;
import br.demo.backend.model.dtos.chat.post.ChatPrivatePostDTO;
import br.demo.backend.model.dtos.chat.post.MessagePostPutDTO;
import br.demo.backend.service.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatService chatService;
    @PostMapping("/group")
    public void saveGroup(@RequestBody ChatGroupPostDTO chat){
        chatService.save(chat);
    }
    @PostMapping("/private")
    public void savePrivate(@RequestBody ChatPrivatePostDTO chat){
        chatService.save(chat);
    }


    @PatchMapping("/visualized/{userId}")
    public void upDateToVisualized(@RequestBody Message message, @PathVariable String userId){
        chatService.updateMessagesToVisualized(message, userId);
    }

    @GetMapping("/name/{userId}/{name}")
    public Collection<ChatGetDTO> findByName(@PathVariable String name, @PathVariable String userId){
        return chatService.findGroupByName(name, userId);
    }
    @GetMapping("/private/{userId}")
    public Collection<ChatPrivateGetDTO> findAllPrivate(@PathVariable String userId){
        return chatService.findAllPrivate(userId);
    }
    @GetMapping("/group/{userId}")
    public Collection<ChatGroupGetDTO> findAllGroup(@PathVariable String userId){
        return chatService.findAllGroup(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        chatService.delete(id);
    }

    @PatchMapping("/message/{chatId}")
    public void updateMessages(@RequestBody MessagePostPutDTO message, @PathVariable Long chatId){
        chatService.updateMessages(message, chatId);
    }

    @PatchMapping("/annex/{chatId}")
    public void updateMessages(@RequestBody MultipartFile annex, @RequestParam String message, @PathVariable Long chatId) throws JsonProcessingException {
        chatService.updateMessages(annex, message, chatId);
    }

}
