package gre.lab2.groupD;

import gre.lab2.graph.WeightedDigraphReader;

import java.io.IOException;

public final class Main {
  static final String RESEAU_1 = "data/reseau1.txt";
  static final String RESEAU_2 = "data/reseau2.txt";
  static final String RESEAU_3 = "data/reseau3.txt";
  static final String RESEAU_4 = "data/reseau4.txt";
  public static void main(String[] args) throws IOException {
    // TODO
    //  - Renommage du package ;
    //  - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.
    var reseau = WeightedDigraphReader.fromFile(RESEAU_2); //Changer le fichier pour tester les autres réseaux
    var algo = new BellmanFordYensAlgorithm();

    var result = algo.compute(reseau, 0);

    if(result.isNegativeCycle()){
      System.out.println("Circuit absorbant trouvé: " + result.getNegativeCycle());
    } else {
      System.out.println("Arborescence de plus court chemin trouvé : " + result.getShortestPathTree());
    }

    //Reseau 1 :
    // Arborescence de plus court chemin trouvé :
    // ShortestPathTree{distances=[0, 2, 12, 4, 7, 2, 2, 4, 0, 1, 3, 0, -1, 4, 3], predecessors=[-1, 9, 1, 11, 13, 12, 3, 3, 0, 12, 1, 5, 0, 5, 3]}

    //Reseau 2 :
    // Circuit absorbant trouvé:
    // NegativeCycle{vertices=[14, 10], length=2}

    //Reseau 3 :
    // Arborescence de plus court chemin trouvé : long...

    //Reseau 4 :
    // Circuit absorbant trouvé:
    // NegativeCycle{vertices=[809, 973, 582, 662, 842, 642, 594, 982, 603, 996, 682, 904, 722, 683, 593, 954, 673, 962, 641, 859], length=20}

  }
}
