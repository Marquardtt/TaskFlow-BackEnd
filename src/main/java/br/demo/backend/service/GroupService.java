package br.demo.backend.service;


import br.demo.backend.model.GroupModel;
import br.demo.backend.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GroupService {

    GroupRepository groupRepository;

    public Collection<GroupModel> findAll() {
        return groupRepository.findAll();
    }

    public GroupModel findOne(Long id) {
        return groupRepository.findById(id).get();
    }

    public void save(GroupModel groupModel) {
        groupRepository.save(groupModel);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
