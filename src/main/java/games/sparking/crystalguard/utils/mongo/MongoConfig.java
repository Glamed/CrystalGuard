package games.sparking.crystalguard.utils.mongo;

import lombok.Data;

@Data
public class MongoConfig {

    private String host = "localhost";
    private int port = 27017;
    private boolean authEnabled = false;
    private String authUsername = "username";
    private String authPassword = "password";
    private String authDatabase = "admin";

}
