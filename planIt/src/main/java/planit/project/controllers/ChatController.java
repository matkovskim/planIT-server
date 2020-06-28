package planit.project.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    
    @MessageMapping("/hello")
    @SendTo("/chat/public")
    private void game(String message) {
    	System.out.println("Pogodjen socket");
    }
    
}
