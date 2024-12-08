from neoConnection import DbConnection
from Operations import Operations

def main():
    uri = "bolt://localhost:7687"
    user = "neo4j"
    password = "12345678"

    db = DbConnection(uri, user, password)
    operations = Operations(db)

    while True:
        print("\nMain Menu")
        print("1. Create Graph")
        print("2. Delete Node, Relationship, or Property")
        print("3. Update Node or Relationship")
        print("4. Find Node or Relationship")
        print("5. Exit")

        choice = input("Select an option (1-5): ")

        if choice == "1":
            print("Creating graph...")
            operations.create_graph()

        elif choice == "2":
            while True:
                print("\nDelete Options")
                print("1. Delete Node")
                print("2. Delete Relationship")
                print("3. Delete Node Property")
                print("4. Delete Relationship Property")
                print("5. Back to Main Menu")

                delete_choice = input("Select an option (1-4): ")

                if delete_choice == "1":
                    name = input("Enter the name of the node to delete: ")
                    operations.delete_node(name)

                elif delete_choice == "2":
                    from_name = input("Enter the name of the source node: ")
                    to_name = input("Enter the name of the target node: ")
                    rel_type = "WORKS_WITH"
                    operations.delete_relationship(from_name, to_name, rel_type)

                elif delete_choice == "3":
                    name = input("Enter the name of the node: ")
                    property_key = input("Enter the property key to delete: ")
                    operations.delete_node_property(name, property_key)

                elif delete_choice == "4":
                    from_name = input("Enter the name of the source node: ")
                    to_name = input("Enter the name of the target node: ")
                    rel_type = "WORKS_WITH"
                    property_key = input("Enter the property key to delete: ")
                    operations.delete_relationship_property(from_name, to_name, rel_type, property_key)

                elif delete_choice == "5":
                    break

                else:
                    print("Invalid option. Please try again")

        elif choice == "3":
            while True:
                print("\nUpdate Options")
                print("1. Update Node Property")
                print("2. Update Relationship Property")
                print("3. Back to Main Menu")

                update_choice = input("Select an option (1-3): ")

                if update_choice == "1":
                    name = input("Enter the name of the node: ")
                    property_key = input("Enter the property key to update: ")
                    property_value = input("Enter the new property value: ")
                    operations.update_node_property(name, property_key, property_value)

                elif update_choice == "2":
                    from_name = input("Enter the name of the source node: ")
                    to_name = input("Enter the name of the target node: ")
                    rel_type = "WORKS_WITH"
                    property_key = input("Enter the property key to update: ")
                    property_value = input("Enter the new property value: ")
                    operations.update_relationship_property(from_name, to_name, rel_type, property_key, property_value)

                elif update_choice == "3":
                    break

                else:
                    print("Invalid option. Please try again.")

        elif choice == "4":
            while True:
                print("\nFind Options")
                print("1. Find Node by Name")
                print("2. Find Relationships by Property")
                print("3. Back to Main Menu")

                find_choice = input("Select an option (1-3): ")

                if find_choice == "1":
                    name = input("Enter the name of the node to find: ")
                    operations.find_node_by_name(name)

                elif find_choice == "2":
                    property_key = input("Enter the relationship property key to find: ")
                    property_value = input("Enter the property value to find: ")
                    operations.find_relationships_by_property(property_key, property_value)

                elif find_choice == "3":
                    break

                else:
                    print("Invalid option. Please try again.")

        elif choice == "5":
            print("Exiting...")
            break

        else:
            print("Invalid option. Please try again.")

    db.close()

if __name__ == "__main__":
    main()