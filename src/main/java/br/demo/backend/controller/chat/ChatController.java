package br.demo.backend.controller.chat;


import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.service.chat.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatService chatService;
    @PostMapping
    public void insert(@RequestBody Chat chat){
        chatService.save(chat);
    }
    @PutMapping
    public void upDate(@RequestBody Chat chat){
        chatService.update(chat, false);
    }

    @PatchMapping
    public void patch(@RequestBody Chat chat){
        chatService.update(chat, true);
    }

    @PutMapping("/visualized/{userId}")
    public void upDateToVisualized(@RequestBody Chat chat, @PathVariable Long userId){
        chatService.updateMessagesToVisualized(chat, userId);
    }
    @GetMapping("/{id}")
    public Chat findOne(@PathVariable Long id){
        return chatService.findOne(id);
    }
    @GetMapping("/name/{name}")
    public Collection<Chat> findByName(@PathVariable String name){
        return chatService.findByName(name);
    }
    @GetMapping("/private/{userId}")
    public Collection<Chat> findAllPrivate( @PathVariable Long userId){
        return chatService.findAllPrivate(userId);
    }
    @GetMapping("/group/{userId}")
    public Collection<Chat> findAllGroup( @PathVariable Long userId){
        return chatService.findAllGroup(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        chatService.delete(id);
    }

}
