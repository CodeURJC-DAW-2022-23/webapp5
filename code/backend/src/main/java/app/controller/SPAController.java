package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {
 @GetMapping({"/spa/**/{path:[^\\.]*}", "/{path:spa[^\\.]*}"})
 public String redirect() {
 return "forward:/spa/index.html";
 }
}