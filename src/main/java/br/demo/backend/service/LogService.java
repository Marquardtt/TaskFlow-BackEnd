package br.demo.backend.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.interfaces.ILogged;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.Intervals;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
                .map(prop -> updateProperty(obj, old, prop))
                .filter(Objects::nonNull).toList();
        obj.getLogs().addAll(logs);
    }

    private Log updateProperty(ILogged obj, ILogged old, PropertyValue prop) {
        PropertyValue first = old.getPropertiesValues().stream()
                .filter(p -> p.getValue().getId().equals(prop.getValue().getId()))
                .findFirst()
                .orElse(null);

        if (testIfIsDiferent(prop, first)) {
            PropertyValue propertyValue = new PropertyValue(first);
            return new Log(null,  Action.UPDATE, getUser(), LocalDateTime.now(), propertyValue);
        }
        return null; // Se não há alteração, retorna null
    }

    private boolean testIfIsDiferent(PropertyValue prop, PropertyValue first) {
        return first != null  && !(prop.getValue().getValue() == null && first.getValue().getValue() == null) &&
                ((first.getValue().getValue() == null && prop.getValue().getValue() != null) ||
                        (prop.getValue().getValue() == null && first.getValue().getValue() != null) ||
                        (!prop.getValue().getValue().equals(first.getValue().getValue()))
                );
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
