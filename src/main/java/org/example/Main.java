package org.example;

public class Main {
    public static void main(String[] args) {
        int choice;
        while ((choice = Menu.MainMenu()) != -1){
           switch (choice) {
               case 1 :
                   Menu.DeleteMenu();
                   break;
               case 2 :
                   Menu.UpdateMenu();
                   break;
               case 3 :
                   Menu.CreateRelationshipMenu();
                   break;
               default:
                   System.out.println("Invalid input!");
                   break;
           }

       }
    }
}