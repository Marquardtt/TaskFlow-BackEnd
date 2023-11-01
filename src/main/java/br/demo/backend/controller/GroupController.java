package br.demo.backend.controller;

import br.demo.backend.model.GroupModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;
    @PostMapping
    public void insert(@RequestBody GroupModel group) {
        groupService.save(group);
    }

    @PutMapping
    public void upDate(@RequestBody GroupModel group) {
        groupService.save(group);
    }

    @GetMapping("/{id}")
    public GroupModel findOne(@RequestParam Long id) {
        return groupService.findOne(id);
    }

    @GetMapping
    public List<GroupModel> findAll() {
        return groupService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        groupService.delete(id);
    }

}
