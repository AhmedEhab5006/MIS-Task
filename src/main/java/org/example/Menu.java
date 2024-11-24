package org.example;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.bson.Document;

import javax.swing.text.DocumentFilter;


public class Menu {

    public static Connection connection = new Connection("localhost", 27017);
    public static DataOperations dbOps = new DataOperations(connection.getMongoClient());

    public static Scanner input = new Scanner(System.in);
    public static int MainMenu(){
        System.out.println("---------------Welcome to our DB management system--------------------------------");
        System.out.println("Choose an operation to do :");
        System.out.println("(1) to create a new collection");
        System.out.println("(2) to insert a document to a collection");
        System.out.println("(3) to delete a document from a collection");
        System.out.println("(4) to update a document");
        System.out.println("(5) to create a one to many relationship");
        System.out.println("(-1) to exit");
        int choice = input.nextInt();
        return choice ;
    }

    public static void CreateCollectionMenu(){
        System.out.println("Enter your collection name :");
        String collectionName = input.next();
        System.out.println("Enter your desired database name");
        String dbName = input.next();
        dbOps.CreateCollection(dbName , collectionName);
    }

    public static void InsertDocumentMenu(){
        System.out.println("Enter your desired database name :");
        String dbName = input.next();
        System.out.println("Enter your desired collection name : ");
        String collectionName = input.next();
        Document doc = new Document();
        int choice = 0 ;
        while(choice != -1){

            System.out.println("Enter your key name :");
            String key = input.next();
            System.out.println("Enter your value name :");
            String value = input.next();

            doc.append(key , value);

            System.out.println("Do you want to add another field ? (Any number) for YES (-1) for NO");
            choice = input.nextInt();
        }

        dbOps.Insert(dbName , collectionName , doc);
    }

    public static void DeleteMenu(){

        System.out.println("Enter desired database :");
        String dbName = input.next();
        System.out.println("Enter desired collection name :");
        String collectionName = input.next();
        System.out.println("Enter DOC filter key :");
        String filterKey = input.next();
        System.out.println("Enter DOC filter value :");
        String filterValue = input.next();

        Document filter = new Document(filterKey , filterValue);
        dbOps.Delete(dbName , collectionName , (filter));

    }

    public static void UpdateMenu(){

        System.out.println("Enter desired database :");
        String dbName = input.next();
        System.out.println("Enter desired collection name :");
        String collectionName = input.next();
        System.out.println("Enter DOC filter key");
        String filterKey = input.next();
        System.out.println("Enter DOC filter value");
        String filterValue = input.next();


        List<Double> scoreList = Arrays.asList(0.0 , 0.0 , 0.0 , 0.0 , 0.0 , 0.0);

        Document filter = new Document(filterKey , filterValue);
        Document update = new Document("$set", new Document("Score" , scoreList));

        System.out.println("Score Array added!");

        dbOps.Update(dbName , collectionName , filter , update);

        Document found = dbOps.Find(dbName , collectionName , filter);



        if (found.getString("_id").equals("1")){

            update = new Document("$set", new Document("Score.2" , 5.0));

        }
        else {
            update = new Document("$set", new Document("Score.4" , 6.0));
        }


        dbOps.Update(dbName , collectionName , filter , update);
        System.out.println("Number added!");

        found = dbOps.Find(dbName , collectionName , filter);

        scoreList = (List<Double>) found.get("Score");


        for (int i = 0 ; i < scoreList.size() ; i++){
            scoreList.set(i , scoreList.get(i)*20.0);
        }

        System.out.println(scoreList);

        update = new Document("$set", new Document("Score" , scoreList));

        dbOps.Update(dbName , collectionName , filter , update);


        System.out.println("Numbers multiplied!");


    }


    public static void CreateRelationshipMenu(){

        System.out.println("Enter database name");
        String dbName = input.next();

        System.out.println("Enter the first collection's name");
        String firstCollection = input.next();

        System.out.println("Enter the second collection's name");
        String secondCollection = input.next();

        System.out.println("---Inserting document in the first collection menu---");
        Document firstCollectionDocument = new Document();
        System.out.println("Enter the key and the value, Enter '-1' in the key to finish inserting key and value");
        while(true) {
            System.out.print("key: ");
            String key = input.next();
            if (key.equals("-1")) {
                break;
            }

            System.out.print("value: ");
            String value = input.next();
            firstCollectionDocument.append(key,value);
        }
        dbOps.Insert(dbName,firstCollection,firstCollectionDocument);


        System.out.println("---Inserting document in the second collection that has refrence to the first menu--- ");
        System.out.println("Enter the key and the value, Enter '-1' in the key to finish inserting key and value ");
        Document secondCollectionDocument = new Document();
        while(true) {
            System.out.print("key: ");
            String key = input.next();
            if (key.equals("-1")) {
                break;
            }

            System.out.print("value: ");
            String value = input.next();
            secondCollectionDocument.append(key,value);
        }

        System.out.println("Enter the refrence key to use in the second collection ");
        String refkey = input.next();
        System.out.println("Enter the key to identify the document in first collection ");
        String firstFilterKey = input.next();

        System.out.println("Enter the value for that key that identifies the document in the first collection");
        String firstFilterValue = input.next();

        Document firstFilter = new Document(firstFilterKey,firstFilterValue);
        Document firstDocument = dbOps.Find(dbName,firstCollection,firstFilter);

        if(firstDocument != null){
            dbOps.InsertWithRefrence(dbName,secondCollection,secondCollectionDocument,refkey,firstDocument.get("_id"));
            System.out.println("Document that has refrence to the first collection is inserted in the second collection  ");
        }
        else{
            System.out.println("An error occurred ");
        }
    }

    public static void MultiplyScoreArray(){

    }



}


