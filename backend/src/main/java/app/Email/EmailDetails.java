package app.email;

import java.util.ArrayList;
import java.util.List;

import app.model.Game;
import app.model.Purchase;
import app.model.User;

import org.apache.commons.lang3.RandomStringUtils;
// Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetails {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;

    public EmailDetails(String recipient, String subject) {
        this.recipient = recipient;
        this.subject = subject;
    }

    public void generatePurchaseMessage(Purchase purchase, User user) {
        String title = "Hi, " + user.getName() + "!, you have purchased:\n";
        List<String> games = new ArrayList<>();
        float total = 0f;
        for (Game game : purchase.getGames()) {
            String gameInfo = "Game: " + game.getName() + "\nPrice: " + game.getPrice() + "$\nKey: "
                    + RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            total += game.getPrice();
            games.add(gameInfo);
        }
        String totalInfo = "\nTotal: " + total + "$";

        this.msgBody = title + String.join("\n", games) + totalInfo;
    }
}