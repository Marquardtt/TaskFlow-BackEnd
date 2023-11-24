package br.demo.backend.controller.chat;


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
        chatService.save(chat);
    }

    @GetMapping("/{id}")
    public Chat findOne(@PathVariable Long id){
        return chatService.findOne(id);
    }

    @GetMapping
    public Collection<Chat> findAll(){
        return chatService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        chatService.delete(id);
    }

}
