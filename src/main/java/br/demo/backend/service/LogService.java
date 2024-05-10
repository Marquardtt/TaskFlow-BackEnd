package br.demo.backend.service;

import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.ArchiveValued;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class LogService {

    private UserRepository userRepository;



    private User getUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUserDetailsEntity_Username(username).get();
    }

    public void generateLog(Action action, ILogged obj, ILogged aux) {
        if (Objects.requireNonNull(action) == Action.UPDATE) {
            updateLogs(obj, aux);
        }
    }

    public void generateLog(Action action, ILogged obj) {
        switch (action) {
            case CREATE -> createLog(obj);
            case DELETE -> deleteLog(obj);
            case REDO -> redoLog(obj);
            case COMPLETE -> completeLog(obj);
        }
    }

    private void completeLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null,  Action.COMPLETE, getUser(), LocalDateTime.now(), null));

    }

    private void redoLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null,  Action.REDO, getUser(), LocalDateTime.now(), null));

    }

    private void deleteLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null, Action.DELETE, getUser(), LocalDateTime.now(), null));
    }

    private void createLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.setLogs(new HashSet<>());
        obj.getLogs().add(new Log(null,  Action.CREATE, getUser(), LocalDateTime.now(), null));
    }

    private void updateLogs(ILogged obj, ILogged old) {

        String typeObj = obj instanceof Task ? "task" : "project";
        updateName(obj, old, typeObj);
        Collection<Log> logs = obj.getPropertiesValues().stream()
                .map(prop ->  updateProperty(obj, old, prop)
                )
                .filter(Objects::nonNull).toList();
        obj.getLogs().addAll(logs);
    }

    public void updateLogsArchive(ILogged obj, PropertyValue newArvchive) {
        obj.getLogs().add(new Log(null,  Action.UPDATE, getUser(), LocalDateTime.now(), new PropertyValue(newArvchive) ));
    }

    private Log updateProperty(ILogged obj, ILogged old, PropertyValue prop) {
        if (prop.getValue()==null)return null;
        if (prop.getValue().getId()==null) return null;
        PropertyValue first = old.getPropertiesValues().stream()
                .filter(p -> p.getValue().getId().equals(prop.getValue().getId()))
                .findFirst()
                .orElse(null);

        if (testIfIsDiferent(prop, first)) {
            PropertyValue propertyValue = new PropertyValue(prop);
            // antes era o first, agora é o prop tá, fica ligadinho
            return new Log(null,  Action.UPDATE, getUser(), LocalDateTime.now(),propertyValue );
        }
        return null; // Se não há alteração, retorna null
    }

    private boolean testIfIsDiferent(PropertyValue prop, PropertyValue first) {
        if(first == null){
            return false;
        } else if (first.getProperty().getType().equals(TypeOfProperty.ARCHIVE)) {
            return false;
        } else if(first.getValue().getValue() == null && prop.getValue().getValue() == null){
            return  false;
        } else if (first.getValue().getValue() != null && prop.getValue().getValue() != null) {
            if (List.of(TypeOfProperty.USER, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG).contains(prop.getProperty().getType())){
                return !testIfListsHaveSameElements((Collection<?>)first.getValue().getValue(), (Collection<?>) prop.getValue().getValue());
            }else{
                return !first.getValue().getValue().equals(prop.getValue().getValue());
            }
        }
        return true;
    }

    private boolean testIfListsHaveSameElements(Collection<?> first, Collection<?> second) {
        if (first.size() != second.size()) return false;
        for (Object o : first) {
            if (!second.contains(o)) return false;
        }
        return true;
    }


    private void updateName(ILogged obj, ILogged old, String typeObj) {
        if (obj.getName() == null && old.getName() == null) return;
        if (obj.getName() == null && old.getName() != null ||
                obj.getName() != null && old.getName() == null ||
                !obj.getName().equals(old.getName())) {
            obj.getLogs().add(new Log(null, Action.UPDATENAME, getUser(),
                    LocalDateTime.now(), null));
        }
    }
    public void updatePicture(Project project) {
        project.getLogs().add(
                new Log(null,
                        Action.UPDATEPICTURE, getUser(), LocalDateTime.now(), null)
        );
    }

    public void updateOwner(Project project) {
        project.getLogs().add(
                new Log(null,
                        Action.UPDATEOWNER, getUser(), LocalDateTime.now(), null)
        );
    }
    public void updateDescription(Project project) {
        project.getLogs().add(
                new Log(null,
                        Action.UPDATEDESCRIPTION, getUser(), LocalDateTime.now(), null)
        );
    }
}
