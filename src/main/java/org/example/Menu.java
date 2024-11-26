package org.example;
import java.util.*;

import org.bson.Document;

import javax.swing.text.DocumentFilter;


public class Menu {

    public static Connection connection = new Connection("localhost", 27017);
    public static DataOperations dbOps = new DataOperations(connection.getMongoClient());

    public static Scanner input = new Scanner(System.in);

    public static int MainMenu() {
        System.out.println("---------------Welcome to our DB management system--------------------------------");
        System.out.println("Choose an operation to do :");
        System.out.println("(1) to delete a document from a collection");
        System.out.println("(2) to update a document");
        System.out.println("(3) to create a one to many relationship");
        System.out.println("(-1) to exit");


        try {
            int choice = input.nextInt();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Please enter an integer number");
            return -1;
        }

    }


    public static void DeleteMenu() {

        int cont = 0;
        while (cont != -1) {

            System.out.println("Enter desired database :");
            String dbName = input.next();
            System.out.println("Enter desired collection name :");
            String collectionName = input.next();
            System.out.println("Enter DOC filter key :");
            String filterKey = input.next();
            System.out.println("Enter DOC filter value :");
            String filterValue = input.next();

            Document filter = new Document(filterKey, filterValue);


            Document found = dbOps.Find(dbName, collectionName, filter);


            try {
                found.get("_id");
                dbOps.Delete(dbName, collectionName, (filter));
            } catch (NullPointerException e) {
                System.out.println("There are no matched documents");
            }

            System.out.println("Do you want to do another delete operation ?? (1) for YES (-1) for no");

            try {
                cont = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer number");
                cont = -1;
            }


        }
    }

    public static void UpdateMenu() {

        System.out.println("Enter desired database :");
        String dbName = input.next();
        System.out.println("Enter desired collection name :");
        String collectionName = input.next();
        System.out.println("Enter DOC filter key");
        String filterKey = input.next();
        System.out.println("Enter DOC filter value");
        String filterValue = input.next();


        List<Double> scoreList = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Document filter = new Document(filterKey, filterValue);
        Document found = dbOps.Find(dbName, collectionName, filter);
        List<Document> doc = new ArrayList<>() ;


        try {
            //making sure that document exists
            found.get("_id");


            //Initializing our score array to 0
            Document update = new Document("$set", new Document("Score", scoreList));
            doc.add(update);
            dbOps.Update(dbName, collectionName, filter, doc);
            System.out.println("Score Array added!");
            doc.remove(update);


            //Adding numbers
            doc.add(dbOps.AddNumbersToIndex().get(1));
            dbOps.Update(dbName , collectionName , filter , doc);
            System.out.println("Number added!");


            //resetting doc list
            doc.clear();


            //multiplying numbers
            doc.add(dbOps.MultiplyBy20().get(1));
            dbOps.Update(dbName , collectionName , filter , doc);


            System.out.println("Numbers multiplied!");



        } catch (NullPointerException e) {
            System.out.println("There are no matched documents");
        }
    }


    public static void CreateRelationshipMenu() {

        System.out.println("Enter database name");
        String dbName = input.next();

        System.out.println("Enter the first collection's name");
        String firstCollection = input.next();

        System.out.println("Enter the second collection's name");
        String secondCollection = input.next();

        System.out.println("---Inserting document in the first collection menu---");
        Document firstCollectionDocument = new Document();
        System.out.println("Enter the key and the value, Enter '-1' in the key to finish inserting key and value");
        while (true) {
            System.out.print("key: ");
            String key = input.next();
            if (key.equals("-1")) {
                break;
            }

            System.out.print("value: ");
            String value = input.next();
            firstCollectionDocument.append(key, value);
        }
        dbOps.Insert(dbName, firstCollection, firstCollectionDocument);


        System.out.println("---Inserting document in the second collection that has refrence to the first menu--- ");
        System.out.println("Enter the key and the value, Enter '-1' in the key to finish inserting key and value ");
        Document secondCollectionDocument = new Document();
        while (true) {
            System.out.print("key: ");
            String key = input.next();
            if (key.equals("-1")) {
                break;
            }

            System.out.print("value: ");
            String value = input.next();
            secondCollectionDocument.append(key, value);
        }

        System.out.println("Enter the refrence key to use in the second collection ");
        String refkey = input.next();
        System.out.println("Enter the key to identify the document in first collection ");
        String firstFilterKey = input.next();

        System.out.println("Enter the value for that key that identifies the document in the first collection");
        String firstFilterValue = input.next();

        Document firstFilter = new Document(firstFilterKey, firstFilterValue);
        Document firstDocument = dbOps.Find(dbName, firstCollection, firstFilter);

        if (firstDocument != null) {
            dbOps.InsertWithRefrence(dbName, secondCollection, secondCollectionDocument, refkey, firstDocument.get("_id"));
            System.out.println("Document that has refrence to the first collection is inserted in the second collection  ");
        } else {
            System.out.println("An error occurred ");
        }
    }
}




