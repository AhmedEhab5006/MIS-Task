class Operations:
    def __init__(self, db):
        self.db = db

    def create_graph(self):
        team_members = [
            {"name": "Ahmed", "role": "FullStack", "level": 3},
            {"name": "Ihab", "role": "Backend", "level": 3},
            {"name": "Abdelrahman", "role": "Leader", "level": 3},
            {"name": "Nabil", "role": "FullStack", "level": 3},
            {"name": "Youssef", "role": "Backend", "level": 3},
        ]
        relationships = [
            ("Ahmed", "Ihab", "WORKS_WITH", {"since": "2020"}),
            ("Ahmed", "Abdelrahman", "WORKS_WITH", {"since": "2022"}),
            ("Nabil", "Abdelrahman", "WORKS_WITH", {"since": "2022"}),
            ("Youssef", "Ahmed", "WORKS_WITH", {"since": "2024"}),
        ]

        def create(tx, members, relations):
            for member in members:
                tx.run(
                    "CREATE (:TeamMember {name: $name, role: $role, level: $level})",
                    **member
                )
            for rel in relations:
                tx.run(
                    """
                    MATCH (a:TeamMember {name: $from}), (b:TeamMember {name: $to})
                    CREATE (a)-[r:WORKS_WITH]->(b)
                    SET r += $props
                    """, {"from": rel[0], "to": rel[1], "props": rel[3]}
                )
        with self.db.driver.session() as session:
            session.write_transaction(create, team_members, relationships)
            print("Graph created with relationships and properties")

    def delete_node(self, name):
        def delete(tx, name):
            tx.run("MATCH (n:TeamMember {name: $name}) DETACH DELETE n", name=name)
        with self.db.driver.session() as session:
            session.write_transaction(delete, name)
            print(f"Deleted node with name: {name}")

    def delete_relationship(self, from_name, to_name, rel_type):
        def delete(tx, from_name, to_name, rel_type):
            query = f"""
            MATCH (a:TeamMember {{name: $from_name}})-[r:{rel_type}]->(b:TeamMember {{name: $to_name}})
            DELETE r
            """
            tx.run(query, {"from_name": from_name, "to_name": to_name})
        with self.db.driver.session() as session:
            session.write_transaction(delete, from_name, to_name, rel_type)
            print(f"Deleted relationship: {rel_type} from {from_name} to {to_name}")

    def delete_node_property(self, name, property_key):
        def delete(tx, name, property_key):
            query = f"""
            MATCH (n:TeamMember {{name: $name}})
            REMOVE n.{property_key}
            """
            tx.run(query, {"name": name})
        with self.db.driver.session() as session:
            session.write_transaction(delete, name, property_key)
            print(f"Deleted property '{property_key}' from node '{name}'")

    def delete_relationship_property(self, from_name, to_name, rel_type, property_key):
        def delete(tx, from_name, to_name, rel_type, property_key):
            query = f"""
            MATCH (a:TeamMember {{name: $from_name}})-[r:{rel_type}]->(b:TeamMember {{name: $to_name}})
            REMOVE r[$property_key]
            """
            tx.run(query, {
                "from_name": from_name,
                "to_name": to_name,
                "property_key": property_key
            })

        with self.db.driver.session() as session:
            session.write_transaction(delete, from_name, to_name, rel_type, property_key)
            print(
                f"Deleted property '{property_key}' from relationship '{rel_type}' between '{from_name}' and '{to_name}'")

    def update_node_property(self, name, property_key, property_value):
        def update(tx, name, property_key, property_value):
            query = f"""
            MATCH (n: TeamMember {{name: $name}})
            SET n.{property_key} = $property_value
            """
            tx.run(query, {"name": name, "property_value": property_value})
        with self.db.driver.session() as session:
            session.write_transaction(update, name, property_key, property_value)
            print(f"Updated property '{property_key}' to '{property_value}' for node '{name}'")

    def update_relationship_property(self, from_name, to_name, rel_type, property_key, property_value):
        def update(tx, from_name, to_name, rel_type, property_key, property_value):
            query = f"""
            MATCH (a: TeamMember {{name: $from_name}}) -[r:{rel_type}]-> (b:TeamMember {{name: $to_name}})
            SET r.{property_key} = $property_value
            """
            tx.run(query, {
                "from_name": from_name,
                "to_name": to_name,
                "property_value": property_value
            })
        with self.db.driver.session() as session:
            session.write_transaction(update, from_name, to_name, rel_type, property_key, property_value)
            print(f"Updated property '{property_key}' to '{property_value}' for relationship '{rel_type}' from '{from_name}' to '{to_name}'")

    def find_node_by_name(self,name):
        def find(tx, name):
            result = tx.run(
                """
                MATCH (n: TeamMember {name: $name})
                RETURN n
                """,
                name = name
            )
            return [record["n"] for record in result]
        with self.db.driver.session() as session:
            nodes = session.read_transaction(find, name)
            if nodes:
                for node in nodes:
                    print(f"Found node: {dict(node)}")
            else:
                print(f"No node found with name: {name}")

    def find_relationships_by_property(self, property_key, property_value):
        def find(tx, property_key, property_value):
            result = tx.run(
                """
                MATCH (a: TeamMember) -[r: WORKS_WITH]-> (b:TeamMember)
                WHERE r[$property_key] = $property_value
                RETURN a, r, b
                """,
                property_key = property_key,
                property_value = property_value
            )
            return [(record["a"], record["r"], record["b"]) for record in result]
        with self.db.driver.session() as session:
            relationships = session.read_transaction(find, property_key, property_value)
            if relationships:
                for a, r, b in relationships:
                    print(f"Relationships found: {dict(a)} {dict(r)} {dict(b)}")
            else:
                print(f"No relationships found with property '{property_key}' = '{property_value}'")