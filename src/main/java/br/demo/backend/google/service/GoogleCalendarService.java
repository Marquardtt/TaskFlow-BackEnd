package br.demo.backend.google.service;

import br.demo.backend.google.config.GoogleCalendarConfig;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.service.PropertyValueService;
import br.demo.backend.service.tasks.TaskService;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class GoogleCalendarService {

    private final UserRepository userRepository;
    private final PropertyValueService propertyValueService;

    public String create(Collection<OtherUsersDTO>users,
                         TaskGetDTO task, OffsetDateTime dateTime)
            throws IOException, GeneralSecurityException {
        if(users.isEmpty()) return "";
        Calendar service = GoogleCalendarConfig.createCalendar();

        Event event = createEvent(task, dateTime, users);

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        System.out.println("event" + event);
        return event.getId();
    }

    private static Event createEvent(TaskGetDTO task, OffsetDateTime dateTime, Collection<OtherUsersDTO> users) {
        Event event = new Event()
                .setSummary(task.getName());

        //TODO: ZONA NÃO FUNCIONA
        ;
        Date date = Date.from(dateTime.toInstant());
        DateTime startDateTime = new DateTime(date,  TimeZone.getTimeZone(dateTime.toZonedDateTime().getZone()));
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(TimeZone.getTimeZone(dateTime.toZonedDateTime().getZone()).getID());
        event.setStart(start);

        Date date2 = Date.from(dateTime.plusHours(1).toInstant());
        DateTime endDateTime = new DateTime(date2,  TimeZone.getTimeZone(dateTime.toZonedDateTime().getZone()));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)

                .setTimeZone(TimeZone.getTimeZone(dateTime.toZonedDateTime().getZone()).getID());
        event.setEnd(end);

        System.out.println("start "+ startDateTime  + " end " + endDateTime);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));

        List<EventAttendee> eventAttendees = new ArrayList<>(users.stream().map(u -> new EventAttendee().setEmail(u.getMail())).toList());
        eventAttendees.add(new EventAttendee().setEmail("gestaoprojetosweg@gmail.com"));
        event.setAttendees(eventAttendees);
        return event;
    }

    public String update(Collection<OtherUsersDTO>users,
                         TaskGetDTO task, OffsetDateTime dateTime, String oldId)
            throws IOException, GeneralSecurityException {
        if(users.isEmpty()){
            try {
                delete(oldId);
            } catch (GoogleJsonResponseException ignore) {
            }
            return "";
        }

        Calendar service = GoogleCalendarConfig.createCalendar();
        Event event = createEvent(task, dateTime, users);
        String calendarId = "primary";
        event = service.events().update(calendarId, oldId,  event).execute();
        System.out.println("event" + event);
        return event.getId();
    }

    public void delete(String id)
            throws IOException, GeneralSecurityException {
        Calendar service = GoogleCalendarConfig.createCalendar();
        String calendarId = "primary";
        try {
            service.events().delete(calendarId, id).execute();
        } catch (GoogleJsonResponseException ignore) {
        }
    }
}
