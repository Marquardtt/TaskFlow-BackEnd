package br.demo.backend.controller.chat;


import br.demo.backend.model.chat.Message;
import br.demo.backend.service.chat.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private MessageService messageService;
    @PostMapping
    public void insert(@RequestBody Message message){
        messageService.save(message);
    }

    @PutMapping
    public void upDate(@RequestBody Message message){
        messageService.save(message);
    }

    @GetMapping("/{id}")
    public Message findOne(@PathVariable Long id){
        return messageService.findOne(id);
    }

    @GetMapping
    public Collection<Message> findAll(){
        return messageService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        messageService.delete(id);
    }

}
