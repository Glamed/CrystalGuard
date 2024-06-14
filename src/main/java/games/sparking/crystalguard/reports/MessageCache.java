package games.sparking.crystalguard.reports;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MessageCache {

    private String uuid;
    private long time;
    private String message;
    private List<UUID> recipients;
    private List<UUID> reportedBy = new ArrayList<>();

    public MessageCache(String uuid, List<UUID> recipients, long time, String message) {
        this.uuid = uuid;
        this.recipients = recipients;
        this.time = time;
        this.message = message;
    }

}
