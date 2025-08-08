package buildweeek.bw2.services;


import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.api-key}")
    private String apiKey;

    @Value("${mailgun.from-email}")
    private String fromEmail;

    public void sendRegistrationEmail(String recipient) {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domain + "/messages")
                    .basicAuth("api", this.apiKey)
                    .field("from", "Registrazione <" + this.fromEmail + ">")
                    .field("to", recipient)
                    .field("subject", "Registrazione avvenuta con successo!")
                    .field("text", "Ti informiamo che la tua registrazione è avvenuta correttamente")
                    .asJson();
            
            //da togliere poi
            System.out.println("Mailgun Status: " + response.getStatus());
            System.out.println("Mailgun Body: " + response.getBody());

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
