package br.demo.backend.service.relations;


import br.demo.backend.model.relations.PermissionProject;
import br.demo.backend.repository.relations.UserGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserProjectService {

    private UserGroupRepository userGroupRepository;

    public void save(PermissionProject userGroup) {
       userGroupRepository.save(userGroup);
    }

    public void delete(Long id) {
        userGroupRepository.deleteById(id);
    }
}
