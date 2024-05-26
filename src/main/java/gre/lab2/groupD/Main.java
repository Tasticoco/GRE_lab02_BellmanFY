package gre.lab2.groupD;

import gre.lab2.graph.WeightedDigraphReader;

import java.io.IOException;

/**
 * LABORATOIRE 2 : ALGORITHME DE BELLMAN-FORD-YENS ET RECONSTRUCTION D'UN CIRCUIT ABSORBANT
 * Groupe D
 * Ce programme permet d'effectuer l'algorithme de Bellman-Ford avec l'amélioration de Yens sur des réseaux
 * donnés en format texte.
 * Ce programme à deux sorties possibles. Soit, on arrive à trouver une arborescence de plus courts chemins depuis l'origine
 * (ici l'origine sera toujours le sommet 0), soit on trouve un circuit absorbant.
 * Pour le premier cas, on affiche l'arborescence trouvée en imprimant dans le terminal la distance entre le sommet 0 et
 * chaque autre sommet ainsi qu'une liste de prédécesseur pour chaque sommet. Et pour le second cas,
 * on affiche le circuit absorbant en indiquant quels sommets sont impliqués et sa longueur.
 *
 * @author Junod Arthur
 * @author Häffner Edwin
 * @see BellmanFordYensAlgorithm
 */
public final class Main {
  static final String RESEAU_1 = "data/reseau1.txt";
  static final String RESEAU_2 = "data/reseau2.txt";
  static final String RESEAU_3 = "data/reseau3.txt";
  static final String RESEAU_4 = "data/reseau4.txt";
  public static void main(String[] args) throws IOException {
    var reseau = WeightedDigraphReader.fromFile(RESEAU_3); //Changer le fichier pour tester les autres réseaux
    var algo = new BellmanFordYensAlgorithm();

    //On applique l'algorithme de Bellman-Ford-Yens sur le réseau depuis le sommet 0
    var result = algo.compute(reseau, 0);

    //Affichage du résultat
    if(result.isNegativeCycle()){
      System.out.println("Circuit absorbant trouvé: " + result.getNegativeCycle());
    } else {
      System.out.println("Aucun circuit absorbant trouvé dans ce réseau.");
      if(reseau.getNVertices() < 25) {
        System.out.println("Arborescence de plus court chemin trouvé : " + result.getShortestPathTree());
      }
    }
  }
}
