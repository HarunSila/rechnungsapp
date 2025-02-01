package com.fhswf.rechnungsapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 *  Dieser Controller leitet alle Anfragen auf die Startseite weiter, welche in src/main/resources/static/index.html liegt.
 *  Dies ist notwendig, da Angular Routing verwendet wird und die Angular App sonst nicht richtig funktioniert.
 * 
 *  Anfragen auf die API (REST-Controller) werden nicht weitergeleitet, da diese mit /api/ beginnen und somit nicht von diesem Controller abgefangen werden.
 */

@Controller
public class WebController {

    @GetMapping(value = "/{path:[^\\.]*}", produces = "text/html") 
    public String forward() {
        return "forward:/index.html";
    }
}
