package org.example;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.Arrays;

public class Connection {

    private MongoClient mongoClient;

    // Constructor
    public Connection(String host, int port) {
        try {
            // Create MongoClient instance using the sync driver
            this.mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
                                    builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                            .build()
            );
            System.out.println("Successfully connected to MongoDB at " + host + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter for MongoClient
    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    // Close MongoClient
    public void close() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            System.out.println("MongoClient connection closed.");
        }
    }
}
