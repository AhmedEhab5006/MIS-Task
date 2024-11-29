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
               default:
                   System.out.println("Invalid input!");
                   break;
           }

       }
    }
}