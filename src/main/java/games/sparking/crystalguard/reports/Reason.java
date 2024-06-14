package games.sparking.crystalguard.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Data
@NoArgsConstructor
public class Reason {

    private String uuid;
    private PunishmentTypes message;
    private String server;
    private long timeStamp;

    public Duration getTimeElapsedSinceReport() {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        Instant now = Instant.now();
        return Duration.between(instant, now);
    }
}
