package games.sparking.crystalguard.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Report {

    private String reportID;
    private String suspectUUID;
    private String category;
    private List<Reason> reasons;
    private List<MessageCache> messages;
    private String status;
    private String handler;
    private String statusReason;
    private Long statusTime;

}