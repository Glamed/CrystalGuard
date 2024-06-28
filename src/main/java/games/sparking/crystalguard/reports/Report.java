package games.sparking.crystalguard.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Report {

    private String reportID = null;
    private String suspectUUID = null;
    private String category = null;
    private List<Reason> reasons = null;
    private List<MessageCache> messages = null;
    private String status = null;
    private String handler = null;
    private String statusReason = null;
    private Long statusTime = null;

}