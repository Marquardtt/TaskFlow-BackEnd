package br.demo.backend.controller;

import br.demo.backend.model.Group;
import br.demo.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;
    @PostMapping
    public void insert(@RequestBody Group group) {
        groupService.save(group);
    }

    @PutMapping
    public void upDate(@RequestBody Group group) {
        groupService.save(group);
    }

    @GetMapping("/{id}")
    public Group findOne(@PathVariable Long id) {
        return groupService.findOne(id);
    }

    @GetMapping
    public Collection<Group> findAll() {
        return groupService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

}
