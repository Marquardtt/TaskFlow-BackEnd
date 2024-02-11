package br.demo.backend.controller.chat;


import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.dtos.chat.ChatGetDTO;
import br.demo.backend.model.dtos.chat.ChatGroupGetDTO;
import br.demo.backend.model.dtos.chat.ChatPrivateGetDTO;
import br.demo.backend.service.chat.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatService chatService;
    @PostMapping("/group")
    public void saveGroup(@RequestBody ChatGroup chat){
        chatService.save(chat);
    }
    @PostMapping("/private")
    public void savePrivate(@RequestBody ChatPrivate chat){
        chatService.save(chat);
    }
    @PutMapping("/group")
    public void upDate(@RequestBody ChatGroup chat){
        chatService.update(chat, false);
    }

    @PatchMapping("/group")
    public void patch(@RequestBody ChatGroup chat){
        chatService.update(chat, true);
    }

    @PutMapping("/private")
    public void upDate(@RequestBody ChatPrivate chat){
        chatService.update(chat, false);
    }

    @PatchMapping("/private")
    public void patch(@RequestBody ChatPrivate chat){
        chatService.update(chat, true);
    }

    @PatchMapping("/group/visualized/{userId}")
    public void upDateToVisualized(@RequestBody ChatGroup chat, @PathVariable Long userId){
        chatService.updateMessagesToVisualized(chat, userId);
    }
    @PatchMapping("/private/visualized/{userId}")
    public void upDateToVisualized(@RequestBody ChatPrivate chat, @PathVariable Long userId){
        chatService.updateMessagesToVisualized(chat, userId);
    }
    @GetMapping("/name/{userId}")
    public Collection<ChatGetDTO> findByName(@RequestBody String name, @PathVariable Long userId){
        return chatService.findGroupByName(name, userId);
    }
    @GetMapping("/private/{userId}")
    public Collection<ChatPrivateGetDTO> findAllPrivate(@PathVariable Long userId){
        return chatService.findAllPrivate(userId);
    }
    @GetMapping("/group/{userId}")
    public Collection<ChatGroupGetDTO> findAllGroup(@PathVariable Long userId){
        return chatService.findAllGroup(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        chatService.delete(id);
    }

}
