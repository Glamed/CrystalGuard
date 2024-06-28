package games.sparking.crystalguard.reports;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import games.sparking.crystalguard.CrystalGuard;

public class ReportService {

    public static FindIterable<Report> getPending() {
        return CrystalGuard.getMongoService().getReports().find(Filters.eq("status", "PENDING"));
    }

    public static FindIterable<Report> getUnhandled() {
        return CrystalGuard.getMongoService().getReports().find(
                Filters.or(
                        Filters.eq("status", "PENDING"),
                        Filters.eq("status", "IN_PROGRESS")
                )
        );
    }

    public static Report getByID(String id) {
        return CrystalGuard.getMongoService().getReports().find(Filters.eq("reportID", id)).first();
    }

    public static Report getByHandler(String uuid) {
        return CrystalGuard.getMongoService().getReports().find(
                Filters.and(
                        Filters.eq("status", "PENDING"),
                        Filters.eq("handler", uuid)
                )
        ).first();
    }

    public static Report getBySuspect(String uuid) {
        return CrystalGuard.getMongoService().getReports().find(
                Filters.and(
                        Filters.eq("status", "PENDING"),
                        Filters.eq("suspectUUID", uuid)
                )
        ).first();
    }

    public static void updateStatus(Report report, String status, String handler) {
        report.setStatus(status);
        report.setHandler(handler);
        report.setStatusTime(System.currentTimeMillis());
        CrystalGuard.getMongoService().getReports().replaceOne(Filters.eq("reportID", report.getReportID()), report);
    }

    public static void updateStatus(Report report, String status, String handler, String reason) {
        report.setStatus(status);
        report.setStatusReason(reason);
        report.setHandler(handler);
        report.setStatusTime(System.currentTimeMillis());
        CrystalGuard.getMongoService().getReports().replaceOne(Filters.eq("reportID", report.getReportID()), report);
    }


}
