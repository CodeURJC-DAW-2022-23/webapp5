package app.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GameController {
        

        @GetMapping("/")
	public String showBooks(Model model) {

		return "index";
	}

}
