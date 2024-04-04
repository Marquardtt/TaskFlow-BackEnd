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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Service
public class LogService {

    private UserRepository userRepository;

    private User getUser(){
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUserDetailsEntity_Username(username).get();
    }
    public void generateLog(Action action, ILogged obj, ILogged aux){
        if (Objects.requireNonNull(action) == Action.UPDATE) {
            updateLogs(obj, aux);
        }
    }
    public void generateLog(Action action, ILogged obj){
        switch (action){
            case CREATE -> createLog(obj);
            case DELETE -> deleteLog(obj);
            case REDO -> redoLog(obj);
            case COMPLETE -> completeLog(obj);
        }
    }

    private void completeLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null, typeObj+" completed", Action.COMPLETE, getUser(), LocalDateTime.now(), null));

    }

    private void redoLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null, typeObj + " redo", Action.REDO, getUser(), LocalDateTime.now(), null));

    }

    private void deleteLog(ILogged obj) {
        String typeObj = obj.getClass().getSimpleName();
        obj.getLogs().add(new Log(null, typeObj + " deleted", Action.DELETE, getUser(), LocalDateTime.now(), null));
    }

    private void createLog(ILogged obj){
        String typeObj = obj.getClass().getSimpleName();
        obj.setLogs(new HashSet<>());
        obj.getLogs().add(new Log(null, typeObj + " created", Action.CREATE, getUser(), LocalDateTime.now(), null) );
    }

    private void updateLogs(ILogged obj, ILogged old){
        String typeObj = obj instanceof Task ? "task" : "project";
        updateName(obj, old, typeObj);
        Collection<Log> logs = obj.getPropertiesValues().stream()
                .map(prop ->  updateProperty(obj, old, prop))
                .filter(Objects::nonNull).toList();
        obj.getLogs().addAll(logs);
    }

    private Log updateProperty(ILogged obj, ILogged old, PropertyValue prop) {
        PropertyValue first = old.getPropertiesValues().stream()
                .filter(p-> p.getValue().getId().equals(prop.getValue().getId()))
                .findFirst()
                .orElse(null);
        if (first != null && !prop.getValue().getValue().equals(first.getValue().getValue())) {
            PropertyValue propertyValue = new PropertyValue(first);
            return new Log(null, descriptionUpdate(prop), Action.UPDATE,getUser(), LocalDateTime.now(), propertyValue);
        }
        return null; // Se não há alteração, retorna null
    }

    private void updateName(ILogged obj, ILogged old, String typeObj) {
        if(!obj.getName().equals(old.getName())){
            obj.getLogs().add(new Log(null, "The "+typeObj+"'s name was changed to '"+
                    obj.getName()+"'", Action.UPDATE, getUser(),
                    LocalDateTime.now(), null));
        }
    }

    private String descriptionUpdate(PropertyValue value){
        String base = "The property '"+value.getProperty().getName()+"' was changed to '";
        base += switch (value.getProperty().getType()){
            case DATE -> formatDate(value)+"'";
            case SELECT, RADIO -> ((Option)value.getValue().getValue()).getName()+"'";
            case TIME -> (formateDuration(((Intervals)value.getValue().getValue()).getTime()))+"'";
            case CHECKBOX, TAG -> listString(((Collection<Option>)value.getValue().getValue())
                    .stream().map(Option::getName).toList())+"'";
            case USER -> listString(((Collection<User>)value.getValue().getValue())
                    .stream().map(u -> u.getUserDetailsEntity().getUsername()).toList())+"'";
            case ARCHIVE -> ((Archive)value.getValue().getValue()).getName() +"."+
                    ((Archive)value.getValue().getValue()).getType()+"'";
            case TEXT, NUMBER -> value.getValue().getValue()+"'";
            case PROGRESS -> value.getValue().getValue()+"%'";
        };
        return base;
    }


    private String listString(Collection<String> strs){
        if(strs == null || strs.isEmpty()){
            return "";
        }
        StringJoiner base = new StringJoiner(", ", "", " and ");
        for(String str : strs){
            base.add(str);
        }
        return base.toString();
    }

    private String formateDuration (Duration value){
        long hours = value.toHours();
        long minutes = value.minusHours(hours).toMinutes();
        long seconds = value.minusHours(hours).minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String formatDate (PropertyValue value){
        DateTimeFormatter formatter = null;
        if(((Date)value.getProperty()).getIncludesHours()){
            formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(Locale.getDefault());
        }else{
            formatter =  DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.getDefault());
        }
        return ((LocalDateTime)value.getValue().getValue()).format(formatter);
    }

    public void updatePicture(Project project){
        project.getLogs().add(
                new Log(null, "The project's picture was changed",
                        Action.UPDATE, getUser(), LocalDateTime.now(), null)
        );
    }
    public void updateOwner(Project project){
        project.getLogs().add(
                new Log(null, "The project's owner was changed, now he is '"+
                        project.getOwner().getUserDetailsEntity().getUsername()+"'",
                        Action.UPDATE, getUser(), LocalDateTime.now(), null)
        );
    }
}
