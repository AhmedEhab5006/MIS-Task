package org.example;
import java.sql.SQLOutput;
import java.util.*;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.text.DocumentFilter;


public class Menu {

    public static Connection connection = new Connection("localhost", 27017);
    public static DataOperations dbOps = new DataOperations(connection.getMongoClient());

    public static Scanner input = new Scanner(System.in);

    public static int MainMenu() {
        System.out.println("---------------Welcome to our DB management system--------------------------------");
        System.out.println("Choose an operation to do :");
        System.out.println("(1) to create customers and orders collections");
        System.out.println("(2) to delete a document from the two collections");
        System.out.println("(3) to update a document");
        System.out.println("(4) for the One to Many relationship");
        System.out.println("(-1) to exit");


        try {
            int choice = input.nextInt();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Please enter an integer number");
            return -1;
        }

    }


    public static void createCollectionsAndDocuments(){
        String dbName = "techStoreDB";
        String customersCollection = "customers";
        String ordersCollection = "orders";

        //dropping the collections so that the data dont get duplicated when we run again
        dbOps.deleteCollection(dbName,customersCollection);
        dbOps.deleteCollection(dbName,ordersCollection);

        dbOps.CreateCustomersCollection(dbName,customersCollection);
        dbOps.CreateOrdersCollection(dbName,ordersCollection);


        dbOps.Insert(dbName, customersCollection,new Document("_id",2).append("name","ali").append("age",20).append("city","cairo"));
        dbOps.Insert(dbName, customersCollection,new Document("_id",3).append("name","ahmed").append("age",19).append("city","giza"));
        dbOps.Insert(dbName, customersCollection,new Document("_id",1).append("name","omar").append("age",21).append("city","alexandria"));

        dbOps.Insert(dbName,ordersCollection,new Document("_id",101).append("item","laptop").append("price",65000).append("customerID",2));
        dbOps.Insert(dbName,ordersCollection,new Document("_id",102).append("item","mobile").append("price",40000).append("customerID",1));
        dbOps.Insert(dbName,ordersCollection,new Document("_id",103).append("item","headphone").append("price",1000).append("customerID",1));
        dbOps.Insert(dbName,ordersCollection,new Document("_id",104).append("item","charger").append("price",100).append("customerID",3));


        System.out.println("Customers and Orders collections created successfully");

    }

    public static void deleteDocuementsFromCollections(){
       String dbName = "techStoreDB";
       String customersCollection = "customers";
       String ordersCollection = "orders";

       dbOps.deleteOneDocument(dbName, customersCollection,new Document());
       dbOps.deleteOneDocument(dbName, ordersCollection,new Document());

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

    public static void createOneToManyRelationship(){
        String dbName = "techStoreDB";
        String customersCollection = "customers";
        String ordersCollection = "orders";

        System.out.println("--viewing the one to many relationships--");
        viewCustomersOrders(dbName,customersCollection,ordersCollection,1);
        viewCustomersOrders(dbName,customersCollection,ordersCollection,3);
    }

    private static void viewCustomersOrders(String dbName, String customersCollection, String ordersCollection,int customerID){
        //to find the customer by the ID
        Document customer = dbOps.Find(dbName, customersCollection,new Document("_id",customerID));

        if(customer!= null){
            System.out.println("\n customer: "+customer.toJson());

            List<Document> customerOrders = dbOps.FindMany(dbName, ordersCollection,new Document("customerID",customerID));
            //retriving orders from this customer

            if(!customerOrders.isEmpty()){
                System.out.println("orders for this customer are ");
                for(Document orders: customerOrders){
                    System.out.println(orders.toJson());
                }
            }else{
                System.out.println("this customer has no orders");
            }
        }else{
            System.out.println("Customer with _id = "+ customerID + " is ont found" );
        }
    }

}




