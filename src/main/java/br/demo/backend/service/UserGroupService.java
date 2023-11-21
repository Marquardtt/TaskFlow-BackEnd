package br.demo.backend.service;


import br.demo.backend.model.UserGroupId;
import br.demo.backend.model.UserGroup;
import br.demo.backend.repository.UserGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserGroupService {

    UserGroupRepository userGroupRepository;

    public Collection<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    public UserGroup findOne(UserGroupId id) {
        return userGroupRepository.findById(id).get();
    }

    public void save(UserGroup userGroup) {
       userGroupRepository.save(userGroup);
    }

    public void delete(UserGroupId id) {
        userGroupRepository.deleteById(id);
    }
}
