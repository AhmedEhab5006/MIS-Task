package org.example;

import com.mongodb.client.MongoDatabase;

import static org.example.Menu.connection;

public class Main {
    public static void main(String[] args) {
        int choice;
        while ((choice = Menu.MainMenu()) != -1){
           switch (choice) {
               case 1 :
                   Menu.createCollectionsAndDocuments();
                   break;
               case 2 :
                   Menu.deleteDocuementsFromCollections();
                   break;
               case 3 :
                   Menu.UpdateMenu();
                   break;
               case 4 :
                   Menu.createOneToManyRelationship();
                   break;
               default:
                   System.out.println("Invalid input!");
                   break;
           }

       }
    }
}


//for the final point in the task which is to use aggregation to display the one to many
 //db.orders.aggregate([{$lookup:{from:"customers",localField:"customerID",foreignField:"_id",as:"customer_details"}},{$unwind:"$customer_details"}])