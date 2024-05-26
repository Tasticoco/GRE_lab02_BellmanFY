package gre.lab2.groupD;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.*;

/**
 * Cette classe implémente l'algorithme de Bellman-Ford-Yens pour trouver les plus courts chemins
 * dans un graphe pondéré et détecter les cycles négatifs.
 *
 * @author Junod Arthur
 * @author Häffner Edwin
 * @see WeightedDigraph
 * @see BFYResult
 */
public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {

  // Représentation en int de la sentinelle (on part donc du principe que les sommets sont numérotés strictement positivement >= 0)
  private static final int SENTINEL = -1;
  private static final int SANS_PREDECESSEUR = -1;

  /**
   * Applique l'algorithme de Bellman-Ford-Yens
   *
   * @param graph Le graphe sur lequel on veut appliquer BFY.
   * @param from Le sommet depuis lequel on applique BFY.
   * @return Un résultat de l'algorithme prenant soit la forme d'un cycle négatif, soit une arborescence de plus court chemin.
   */
  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {
    // Récupère le nombre de sommets dans le graphe sur lequel on veut appliquer BFY
    int nbSommets = graph.getNVertices();
    // Tableau des longueurs des plus courts chemins menant à chaque sommet depuis le sommet "from" (l'index du tableau représente le sommet).
    int[] distance = new int[nbSommets];
    // Tableau des prédécesseurs directs de chaque sommet dans le chemin trouvé le plus court.
    int[] predecesseurs = new int[nbSommets];
    // Tableau de boolean qui représente quel sommet est dans la file (true s'il est actuellement dans la file).
    boolean[] sommetsEnFile = new boolean[nbSommets];
    // La fonction ne retourne pas la meme chose selon le résultat de notre Bellman-Ford-Yens
    BFYResult resultat = null;

    //Initialisation des tableaux
    for (int j = 0; j < nbSommets; j++) {
      // Les distances sont initialisées à la valeur maximale, car nous cherchons des améliorations sur le chemin le plus court.
      distance[j] = Integer.MAX_VALUE;
      predecesseurs[j] = SANS_PREDECESSEUR; // Au début, aucun sommet n'a de prédécesseur.
    }

    distance[from] = 0;
    // Compteur pour vérifier si on a un circuit absorbant.
    int k = 0;

    // File des sommets à traiter.
    Queue<Integer> fileSommet = new ArrayDeque<>();
    fileSommet.add(from);
    sommetsEnFile[from] = true;
    // On rajoute la sentinelle à la file.
    fileSommet.add(SENTINEL);

    //Algorithme de Bellman-Ford-Yens
    while (!fileSommet.isEmpty()) {
      int actuel = fileSommet.remove();
      if (actuel == SENTINEL) {
        if (!fileSommet.isEmpty()) {
          k += 1;
          if (k == graph.getNVertices()) {
            // Un cycle négatif a été trouvé, donc on le calcule.
            resultat = trouverCycleNegatif(trouverSommetCycleNegatif(nbSommets, fileSommet, predecesseurs), predecesseurs, graph);
            break;
          } else fileSommet.add(SENTINEL);
        }
      } else {
        sommetsEnFile[actuel] = false;

        // Itère sur les arcs sortants du sommet actuel.
        for (WeightedDigraph.Edge arc : graph.getOutgoingEdges(actuel)) {
          int succ = arc.to();
          // Si amélioration, on met à jour.
          if (distance[succ] > distance[actuel] + arc.weight()) {
            distance[succ] = distance[actuel] + arc.weight();
            predecesseurs[succ] = actuel;
            // Vérifie si le successeur est dans la file.
            if (!sommetsEnFile[succ]) {
              fileSommet.add(succ);
              sommetsEnFile[succ] = true;
            }
          }
        }
      }
    }
    // Si le résultat n'a pas été changé, il n'y a pas de circuit absorbant et on peut donc créer un résultat
    if (resultat == null) {
      resultat = new BFYResult.ShortestPathTree(distance, predecesseurs);
    }

    return resultat;
  }

  /**
   * Méthode pour trouver le cycle négatif du graphe. À seulement utiliser si un cycle négatif a été détecté grâce à
   * l'algorithme de Bellman-Ford-Yens.
   *
   * @throws IllegalStateException Si le sommet de départ n'est pas dans un cycle négatif.
   * @param debut Sommet sur lequel on commence la détection, il est nécessaire que ce sommet fasse partie du cycle négatif.
   * @param p Tableau des prédécesseurs de chaque sommet.
   * @param graph Le graphe sur lequel on travaille.
   * @return Le cycle négatif ainsi que sa longueur.
   */
  private BFYResult.NegativeCycle trouverCycleNegatif(int debut, int[] p, WeightedDigraph graph) {
    List<Integer> cycle = new LinkedList<>();
    int longueur = 0;
    int actuel = debut;

    //Itère sur tous les prédécesseurs du sommet de départ pour trouver tous les sommets composant ce cycle.
    do {
      // On ajoute le sommet depuis la tête de la liste, car on itère dans le sens inverse des arcs.
      cycle.addFirst(actuel);
      // Récupère les arcs qui sortent du prédécesseur du sommet actuel.
      for (WeightedDigraph.Edge arc : graph.getOutgoingEdges(p[actuel])) {
        // Si l'arc du prédécesseur mène au sommet actuel, on rajoute la longueur de l'arc à celle du cycle.
        if (arc.to() == actuel) {
          longueur += arc.weight();
          break; // Sortir de la boucle une fois l'arc trouvé
        }
      }
      if(p[actuel] == SANS_PREDECESSEUR)
        throw new IllegalStateException("Vous essayez de trouver un cycle négatif à partir d'un sommet qui n'est pas dans un cycle négatif...");

      actuel = p[actuel];

    } while (actuel != debut); // On s'arrête une fois qu'on a fait le tour du cycle.

    return new BFYResult.NegativeCycle(cycle, longueur);
  }


  /**
   * Méthode pour trouver un sommet appartenant au cycle négatif. À seulement utiliser si un cycle négatif a été détecté
   * grâce à l'algorithme de Bellman-Ford-Yens.
   *
   * @param nbSommets Nombre total de sommets dans le graphe.
   * @param fileSommets File contenant les sommets à traiter.
   * @param predecesseurs Tableau des prédécesseurs de chaque sommet.
   * @return Un sommet appartenant au cycle négatif.
   */
  private int trouverSommetCycleNegatif(int nbSommets, Queue<Integer> fileSommets, int[] predecesseurs) {
    boolean[] sommetsVisites = new boolean[nbSommets];

    int sommetDansCycle = fileSommets.remove();

    // Remonte les prédécesseurs à partir du sommet courant jusqu'à trouver un sommet déjà visité (appartenant au cycle négatif).
    while (!sommetsVisites[sommetDansCycle]) {
      sommetsVisites[sommetDansCycle] = true;
      sommetDansCycle = predecesseurs[sommetDansCycle];
    }

    return sommetDansCycle;
  }
}

