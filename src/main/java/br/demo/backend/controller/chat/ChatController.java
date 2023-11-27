package br.demo.backend.controller.chat;


import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGetDTO;
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
        chatService.update(chat);
    }
    @PutMapping("/visualized")
    public void upDateToVisualized(@RequestBody Chat chat){
        chatService.updateMessagesToVisualized(chat);
    }
    @GetMapping("/{id}")
    public Chat findOne(@PathVariable Long id){
        return chatService.findOne(id);
    }
    @GetMapping("/name/{name}")
    public Collection<Chat> findByName(@PathVariable String name){
        return chatService.findByName(name);
    }
    @GetMapping("/private/{id}")
    public Collection<Chat> findAllPrivate( @PathVariable Long id){
        return chatService.findAllPrivate(id);
    }
    @GetMapping("/group/{id}")
    public Collection<Chat> findAllGroup( @PathVariable Long id){
        return chatService.findAllGroup(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        chatService.delete(id);
    }

}
