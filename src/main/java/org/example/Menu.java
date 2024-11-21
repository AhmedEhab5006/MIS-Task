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
        System.out.println("(-1) to exit");
        int choice = input.nextInt();
        return choice ;
    }

    public static void CreateCollectionMenu(){
        System.out.println("Enter your collection name :");
        String collectionName = input.nextLine();
        System.out.println("Enter your desired database name");
        String dbName = input.nextLine();
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

            System.out.println("Do you want to add another field ?? (Any number) for YES (-1) for NO");
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

    }

    public static void MultiplyScoreArray(){

    }


}


