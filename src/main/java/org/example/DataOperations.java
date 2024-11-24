package org.example;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.SQLOutput;
import java.util.Arrays;

public class DataOperations {

    private final MongoClient mongoClient;
    public DataOperations(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoClient getMongoClient(){
        return this.mongoClient;
    }




    public void Insert(String dbName, String collectionName, Document document) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
        System.out.println("Document inserted into collection: " + collectionName);
    }

    public void CreateCollection(String dbName, String collectionName) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.createCollection(collectionName);
        System.out.println("Collection created: " + collectionName);
    }


    public void Update(String dbName, String collectionName, Document filter, Document update) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateOne(filter, update);
        System.out.println("Document updated in collection: " + collectionName);
    }


    public void Delete(String dbName, String collectionName, Document filter) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteOne(filter);
        System.out.println("Document deleted from collection: " + collectionName);
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

    public void InsertWithRefrence(String dbName, String collectionName, Document document, String refKey, Object refValue){

        MongoDatabase database = mongoClient.getDatabase(dbName);

        MongoCollection<Document> collection = database.getCollection(collectionName);

        document.append(refKey, refValue);
        collection.insertOne(document);

        //System.out.println("Document that has refrence has been inserted in "" + collectionName);

    }



    }
