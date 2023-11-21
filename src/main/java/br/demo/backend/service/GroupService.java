package br.demo.backend.service;


import br.demo.backend.model.Group;
import br.demo.backend.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GroupService {

    GroupRepository groupRepository;

    public Collection<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findOne(Long id) {
        return groupRepository.findById(id).get();
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
