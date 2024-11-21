package org.example;

public class Main {
    public static void main(String[] args) {
        int choice;
        while ((choice = Menu.MainMenu()) != -1){
           switch (choice) {
               case 1 :
                   Menu.CreateCollectionMenu();
                   break;
               case 2 :
                   Menu.InsertDocumentMenu();
                   break;
               case 3 :
                   Menu.DeleteMenu();
                   break;
               case 4 :
                   Menu.UpdateMenu();
                   break;
               default:
                   System.out.println("Invalid input!");
           }

       }
    }
}