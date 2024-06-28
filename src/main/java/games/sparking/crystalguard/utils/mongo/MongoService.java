package games.sparking.crystalguard.utils.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import games.sparking.crystalguard.reports.Report;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Getter
@RequiredArgsConstructor
public class MongoService {

    private MongoCollection<Report> reports;

    public void connect() {
        // Create the PojoCodecProvider
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        // Combine the default codec registry with the custom UUID and POJO codec registries
        CodecRegistry combinedCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
        );

        // Build the MongoClientSettings with the combined codec registry
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb+srv://Glamify:KzoWEEkHMKGnKRCR@cluster0.64h54dc.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"))
                .codecRegistry(combinedCodecRegistry)
                .build();

        // Create the MongoClient with the settings
        MongoClient mongoClient = MongoClients.create(settings);

        // Get the MongoDatabase with the combined codec registry
        MongoDatabase mongoDatabase = mongoClient.getDatabase("Crystal").withCodecRegistry(combinedCodecRegistry);

        // Get the collection with the specified class type and combined codec registry
        reports = mongoDatabase.getCollection("reports", Report.class);
    }

}