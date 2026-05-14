package games.sparking.crystalguard.reports;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.client.model.Filters;
import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.utils.messages.CC;
import games.sparking.crystalguard.utils.messages.MessageType;
import lombok.Getter;
import org.bukkit.command.CommandSender;
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

    public static void create(CommandSender creator, Player target, ReportCategoryType type) {
        String creatorId;
        String creatorServer;

        // Determine if creator is a Player or Console
        if (creator instanceof Player player) {
            creatorId = player.getUniqueId().toString();
            creatorServer = player.getWorld().getName();
        } else {
            creatorId = "63644fed-6a20-4c35-bef4-be5e1d785a2e";
            creatorServer = "global";
        }

        if (type == ReportCategoryType.CHAT_ABUSE && creator instanceof Player player) {
            boolean receivedMessage = ReportManager.getMessages().asMap().keySet().stream()
                    .flatMap(messageCache -> messageCache.getRecipients().stream())
                    .anyMatch(uuid -> uuid.equals(player.getUniqueId().toString()));

            if (!receivedMessage) {
                creator.sendMessage(MessageType.ERROR.format("Invalid Report.", "You have not received any messages from this player."));
                return;
            }
        }

        ArrayList<Reason> reasons = new ArrayList<>();
        Report existingReport = ReportService.getBySuspect(target.getUniqueId().toString());

        // Create a new reason for the report
        Reason newReason = new Reason();
        newReason.setUuid(creatorId);
        newReason.setServer(creatorServer);
        newReason.setMessage(type.toString());
        newReason.setTimeStamp(System.currentTimeMillis());
        reasons.add(newReason);

        if (existingReport != null && existingReport.getCategory().equals(type.getTypes().toString())) {
            existingReport.getReasons().add(newReason);

            if (type.getTypes() == ReportTypes.CHAT) {
                for (MessageCache messageCache : ReportManager.getMessages().asMap().keySet()) {
                    if (!existingReport.getMessages().contains(messageCache)) {
                        existingReport.getMessages().add(messageCache);
                        if (!messageCache.getReportedBy().contains(creatorId)) {
                            messageCache.getReportedBy().add(creatorId);
                        }
                    }
                }
            }

            if (!existingReport.getStatus().equals("PENDING")) {
                existingReport.setStatus("PENDING");
            }

            CrystalGuard.getMongoService().getReports().replaceOne(
                    Filters.eq("reportID", existingReport.getReportID()), existingReport
            );
            creator.sendMessage(CC.format("&5&l✦ &7%s has been reported for %s.", target.getName(), type.getName()));
        } else {
            Report newReport = new Report();
            newReport.setReportID(generateToken());
            newReport.setSuspectUUID(target.getUniqueId().toString());
            newReport.setCategory(type.getTypes().toString());
            newReport.setReasons(reasons);
            newReport.setStatus("PENDING");

            if (type.getTypes() == ReportTypes.CHAT) {
                newReport.setMessages(new ArrayList<>(ReportManager.getMessages().asMap().keySet()));
                for (MessageCache messageCache : newReport.getMessages()) {
                    messageCache.getReportedBy().add(creatorId);
                }
            }

            CrystalGuard.getMongoService().getReports().insertOne(newReport);
            creator.sendMessage(CC.format("&5&l✦ &7%s has been reported for %s.", target.getName(), type.getName()));
        }
    }


}
