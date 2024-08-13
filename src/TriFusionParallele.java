import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TriFusionParallele {

    // Méthode principale pour trier une liste en utilisant le tri fusion parallèle
    public static void triFusion(List<Integer> liste) {
        // Cas de base : si la liste a 1 élément ou moins, elle est déjà triée
        if (liste.size() <= 1) {
            return;
        }

        // Trouver le milieu de la liste
        int milieu = liste.size() / 2;

        // Diviser la liste en deux parties
        List<Integer> gauche = new ArrayList<>(liste.subList(0, milieu));
        List<Integer> droite = new ArrayList<>(liste.subList(milieu, liste.size()));

        // Trier les deux parties en parallèle en utilisant des threads
        Thread threadGauche = new Thread(() -> triFusion(gauche));
        Thread threadDroite = new Thread(() -> triFusion(droite));

        threadGauche.start();
        threadDroite.start();

        try {
            // Attendre la fin des threads
            threadGauche.join();
            threadDroite.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fusionner les résultats triés des deux parties
        fusionner(liste, gauche, droite);
    }

    // Méthode pour fusionner deux listes triées en une seule liste triée
    private static void fusionner(List<Integer> liste, List<Integer> gauche, List<Integer> droite) {
        int indexGauche = 0, indexDroite = 0, indexListe = 0;

        // Comparer les éléments de gauche et de droite et fusionner dans la liste principale
        while (indexGauche < gauche.size() && indexDroite < droite.size()) {
            if (gauche.get(indexGauche) < droite.get(indexDroite)) {
                liste.set(indexListe++, gauche.get(indexGauche++));
            } else {
                liste.set(indexListe++, droite.get(indexDroite++));
            }
        }

        // Copier les éléments restants de gauche, s'il y en a
        while (indexGauche < gauche.size()) {
            liste.set(indexListe++, gauche.get(indexGauche++));
        }

        // Copier les éléments restants de droite, s'il y en a
        while (indexDroite < droite.size()) {
            liste.set(indexListe++, droite.get(indexDroite++));
        }
    }

    // Méthode principale pour tester le tri fusion parallèle avec une liste générée aléatoirement
    public static void main(String[] args) {
        // Générer une liste de 100 éléments aléatoires entre 0 et 1000
        List<Integer> liste = genererListeAleatoire(100, 0, 1000);

        // Afficher la liste non triée
        System.out.println("Liste non triée: " + liste);

        // Trier la liste en utilisant le tri fusion parallèle
        triFusion(liste);

        // Afficher la liste triée
        System.out.println("Liste triée: " + liste);
    }

    // Méthode pour générer une liste d'entiers aléatoires dans une plage donnée
    private static List<Integer> genererListeAleatoire(int taille, int min, int max) {
        List<Integer> liste = new ArrayList<>();
        Random random = new Random();

        // Remplir la liste avec des entiers aléatoires
        for (int i = 0; i < taille; i++) {
            liste.add(random.nextInt(max - min + 1) + min);
        }

        return liste;
    }
}