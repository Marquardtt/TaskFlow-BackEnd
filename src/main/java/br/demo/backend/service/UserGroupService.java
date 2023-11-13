package br.demo.backend.service;


import br.demo.backend.model.UserGroupModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserGroupService {

    UserGroupRepository userGroupRepository;

    public Collection<UserGroupModel> findAll() {
        return userGroupRepository.findAll();
    }

    public UserGroupModel findOne(Long id) {
        return userGroupRepository.findById(id).get();
    }

    public void save(UserGroupModel userGroupModel) {
        userGroupRepository.save(userGroupModel);
    }

    public void delete(Long id) {
        userGroupRepository.deleteById(id);
    }
}
