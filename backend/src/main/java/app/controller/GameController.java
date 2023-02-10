package app.controller;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.service.*;
import app.model.*;

@Controller
public class GameController {
        
        @Autowired
        private GameService games;


}
