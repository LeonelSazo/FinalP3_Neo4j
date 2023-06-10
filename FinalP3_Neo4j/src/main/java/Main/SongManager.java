/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author Leonel
 */
import org.neo4j.driver.*; // Importamos las clases necesarias de la biblioteca Neo4j

import java.util.Scanner;

public class SongManager {
    private static final String URI = "bolt://localhost:7687"; // URI de conexión a la base de datos Neo4j
    private static final String USER = "neo4j"; // Usuario de la base de datos Neo4j
    private static final String PASSWORD = "leonel"; // Falta contraseña Contraseña de la base de datos Neo4j

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido al Registro de Canciones!");

        try (Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD)); // Establecemos la conexión con la base de datos Neo4j
             Session session = driver.session()) {

            while (true) {
                System.out.println("\nSelecciona una opcion:");
                System.out.println("1. Agregar cancion");
                System.out.println("2. Eliminar cancion");
                System.out.println("3. Actualizar calificacion de una cancion");
                System.out.println("4. Salir");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después de leer el número

                switch (opcion) {
                    case 1:
                        agregarCancion(session, scanner); // Llamamos al método para agregar una canción
                        break;
                    case 2:
                        eliminarCancion(session, scanner); // Llamamos al método para eliminar una canción
                        break;
                    case 3:
                        actualizarCalificacion(session, scanner); // Llamamos al método para actualizar la calificación de una canción
                        break;
                    case 4:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opcion invalida. Intenta de nuevo.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void agregarCancion(Session session, Scanner scanner) {
        System.out.println("Ingrese el nombre de la cancion:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el nombre del artista:");
        String artista = scanner.nextLine();

        System.out.println("Ingrese la calificacion de la cancion:");
        int calificacion = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea después de leer el número

        String query = "CREATE (s:Song {name: $name, artist: $artist, rating: $rating})";
        session.run(query, Values.parameters("name", nombre, "artist", artista, "rating", calificacion));
        
        
 // Crear una relación SAME_ARTIST entre las canciones del mismo artista
        String sameArtistQuery = "MATCH (s1:Song), (s2:Song) WHERE s1.artist = $artist AND s2.artist = $artist AND s1 <> s2 " +
                "CREATE (s1)-[:SAME_ARTIST]->(s2)";
        session.run(sameArtistQuery, Values.parameters("artist", artista));
        
        
        System.out.println("Cancion agregada exitosamente.");
    }
    
    private static void eliminarCancion(Session session, Scanner scanner) {
    System.out.println("Ingrese el nombre de la cancion a eliminar:");
    String nombre = scanner.nextLine();

    // Eliminar la canción y las relaciones asociadas
    String query = "MATCH (s:Song {name: $name})-[r]-() DELETE s, r";
    session.run(query, Values.parameters("name", nombre));

    System.out.println("Cancion eliminada exitosamente.");
}

//    private static void eliminarCancion(Session session, Scanner scanner) {
//        System.out.println("Ingrese el nombre de la cancion a eliminar:");
//        String nombre = scanner.nextLine();
//
//        String query = "MATCH (s:Song {name: $name}) DELETE s";
//        session.run(query, Values.parameters("name", nombre));
//
//        System.out.println("Cancion eliminada exitosamente.");
//    }

    private static void actualizarCalificacion(Session session, Scanner scanner) {
        System.out.println("Ingrese el nombre de la cancion:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese la nueva calificacion de la cancion:");
        int calificacion = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea después de leer el número

        String query = "MATCH (s:Song {name: $name}) SET s.rating = $rating";
        session.run(query, Values.parameters("name", nombre, "rating", calificacion));

        System.out.println("Calificacion de la cancion actualizada exitosamente.");
    }
}

