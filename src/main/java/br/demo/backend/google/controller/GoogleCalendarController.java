package br.demo.backend.google.controller;

import br.demo.backend.google.service.GoogleCalendarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GoogleCalendarController {

    private final GoogleCalendarService service;




}
