package org.example;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataOperations {

    private final MongoClient mongoClient;
    public DataOperations(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }



    public void CreateCustomersCollection(String dbName, String collectionName){
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.createCollection(collectionName);
    }

    public void CreateOrdersCollection(String dbName, String collectionName){
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.createCollection(collectionName);
    }

    public void deleteOneDocument(String dbname, String collectionName, Document filter){
        MongoDatabase database = mongoClient.getDatabase(dbname);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Document deletedDoc = collection.findOneAndDelete(filter);

        if(deletedDoc != null){
            System.out.println("Deleted a document from collection "+ collectionName);
        }else{
            System.out.println("No docuemnt found to be deleted ");
        }
    }


    public void Insert(String dbName, String collectionName, Document document) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
        //System.out.println("Document inserted into collection: " + collectionName);
    }


    public List<Document> AddNumbersToIndex () {
        List<Document> doc = Arrays.asList(
                new Document("$set", new Document("Score", new Document("$ifNull", Arrays.asList("$Score", Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))))),
                new Document("$set", new Document("Score", Arrays.asList(
                        new Document("$arrayElemAt", Arrays.asList("$Score", 0)),
                        new Document("$arrayElemAt", Arrays.asList("$Score", 1)),
                        new Document("$cond", Arrays.asList(
                                new Document("$eq", Arrays.asList("$_id", 1)),
                                5.0,
                                new Document("$arrayElemAt", Arrays.asList("$Score", 2))
                        )),
                        new Document("$cond", Arrays.asList(
                                new Document("$ne", Arrays.asList("$_id", 1)),
                                6.0,
                                new Document("$arrayElemAt", Arrays.asList("$Score", 3))
                        )),
                        new Document("$arrayElemAt", Arrays.asList("$Score", 4)),
                        new Document("$arrayElemAt", Arrays.asList("$Score", 5))
                ))));
        return doc;
    }

    public List<Document> MultiplyBy20 (){
       List<Document> doc = Arrays.asList(
                new Document("$set", new Document("Score", new Document("$ifNull", Arrays.asList("$Score", Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))))),
                new Document("$set", new Document("Score", new Document("$map", new Document("input", "$Score")
                        .append("as", "score")
                        .append("in", new Document("$multiply", Arrays.asList("$$score", 20))))))
        );
       return doc;
    }


    public void Update(String dbName, String collectionName, Document filter, List<Document> update) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateMany(filter, update);
        System.out.println("Document updated in collection: " + collectionName);
    }


    public void deleteCollection(String dbName, String collectionName){
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.getCollection(collectionName).drop();
    }


    public Document Find(String dbName, String collectionName, Document filter) {
        try {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document result = collection.find(filter).first();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Document> FindMany(String dbName, String collectionName, Document query){
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        List<Document> results = new ArrayList<>();
        for(Document doc : collection.find(query)){
            results.add(doc);
        }
        return results;
    }


    }




