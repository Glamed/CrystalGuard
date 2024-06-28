package games.sparking.crystalguard.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MessageCache {

    private String uuid;
    private long time;
    private String message;
    private List<String> recipients;
    private List<String> reportedBy = new ArrayList<>();

    public MessageCache(String uuid, List<String> recipients, long time, String message) {
        this.uuid = uuid;
        this.recipients = recipients;
        this.time = time;
        this.message = message;
    }

}
