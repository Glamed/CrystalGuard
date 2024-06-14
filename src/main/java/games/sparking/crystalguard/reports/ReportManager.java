package games.sparking.crystalguard.reports;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import games.sparking.crystalguard.CrystalGuard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class ReportManager {

    private static final int TOKEN_CHARS = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateToken() {
        // 6 bits per character, round to nearest byte
        int byteAmount = (int) Math.ceil((TOKEN_CHARS * 6) / 8.0);
        byte[] bytes = new byte[byteAmount];
        SECURE_RANDOM.nextBytes(bytes);

        String token = Base64.getUrlEncoder().encodeToString(bytes);
        token = replaceDashes(token);

        return token;
    }

    private static String replaceDashes(String token) {
        for (int i = 0; i < token.length(); i++) {
            char originalChar = token.charAt(i);
            char newChar = originalChar;

            while (newChar == '-') {
                byte[] replacementBytes = new byte[1];
                SECURE_RANDOM.nextBytes(replacementBytes);
                newChar = Base64.getUrlEncoder().encodeToString(replacementBytes).charAt(0);
            }

            token = token.replaceFirst(String.valueOf(originalChar), String.valueOf(newChar));
        }

        return token;
    }


    @Getter
    private static final Cache<MessageCache, Boolean> messages = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();


    // Method to get a report by its reportID
    public static Report getReportByID(String reportID) {
        for (Report report : CrystalGuard.getReports()) {
            if (report.getReportID().equals(reportID)) {
                return report;
            }
        }
        return null; // Return null if report with given reportID is not found
    }

    public static void create(Player creator, Player target, PunishmentTypes type) {
        Bukkit.broadcastMessage("Creating report for " + target.getName() + " with category " + type.getTypes().toString());

        ArrayList<Reason> reasons = new ArrayList<>();
        Report existingReport = null;

        // Iterate through existing reports to find a matching report
        for (Report report : CrystalGuard.getReports()) {
            if (report.getSuspectUUID().equals(target.getUniqueId().toString())
                    && report.getCategory().equals(type.getTypes().toString())) {
                existingReport = report;
                break;
            }
        }

        // Create a new reason for the report
        Reason newReason = new Reason();
        newReason.setUuid(creator.getUniqueId().toString());
        newReason.setServer(creator.getWorld().getName());
        newReason.setMessage(type);
        newReason.setTimeStamp(System.currentTimeMillis());
        reasons.add(newReason);

        // If a matching report is found, merge reasons & messages
        if (existingReport != null) {
            Bukkit.broadcastMessage("Found existing report for " + target.getName() + " with category " + type.getTypes().toString());

            // Add new reason to existing reasons
            existingReport.getReasons().add(newReason);

            // If type is CHAT, merge messages
            if (type.getTypes() == ReportTypes.CHAT) {
                Bukkit.broadcastMessage("Merging messages into existing report...");

                for (MessageCache messageCache : messages.asMap().keySet()) {
                    // Check if the message is not already in the existing report
                    if (!existingReport.getMessages().contains(messageCache)) {
                        // Add the message to the existing report
                        existingReport.getMessages().add(messageCache);
                        // Add the reporter to the message if not already reported by
                        if (!messageCache.getReportedBy().contains(creator.getUniqueId())) {
                            messageCache.getReportedBy().add(creator.getUniqueId());
                        }
                    }
                }
            }

            // Update status to PENDING if not already
            if (!existingReport.getStatus().equals("PENDING")) {
                existingReport.setStatus("PENDING");
                Bukkit.broadcastMessage("Updating status of existing report to PENDING");
            }
        } else {
            Bukkit.broadcastMessage("No existing report found for " + target.getName() + " with category " + type.getTypes().toString() + ". Creating new report...");

            // Create a new report if no matching report is found
            Report newReport = new Report();
            newReport.setReportID(generateToken());
            newReport.setSuspectUUID(target.getUniqueId().toString());
            newReport.setCategory(type.getTypes().toString());
            newReport.setReasons(reasons);
            newReport.setStatus("PENDING");

            if (type.getTypes() == ReportTypes.CHAT) {
                Bukkit.broadcastMessage("Adding messages to the new report...");
                // Add all messages to the new report
                newReport.setMessages(new ArrayList<>(messages.asMap().keySet()));
                // Add the reporter to all messages
                for (MessageCache messageCache : newReport.getMessages()) {
                    messageCache.getReportedBy().add(creator.getUniqueId());
                }
            }

            CrystalGuard.getReports().add(newReport);
            Bukkit.broadcastMessage("New report created successfully.");
        }
    }


}
