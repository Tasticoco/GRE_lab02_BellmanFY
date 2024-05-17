package gre.lab2.groupD;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.*;

public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {

  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {
    int nbVertices = graph.getNVertices();
    int[] lambda = new int[nbVertices]; //Longueurs des plus courts chemins depuis "from" jusqu'a s, s étant l'un des éléments de ce tableau
    int[] p = new int[nbVertices]; //Prédécesseurs immédiats dans ces chemins.
    boolean aCircuitAbsorbant = false;
    boolean[] sommetsEnFile = new boolean[nbVertices]; //Utilisé pour garder quel sommet est déjà dans la queue
    List<Integer> negativeCycle = null;

    for(int j = 0; j < nbVertices; j++){
      lambda[j] = Integer.MAX_VALUE;
      p[j] = -1; //-1 étant NULL (L'assistant voulait pas que je garde Integer pour ça :P)
    }

    lambda[from] = 0;
    int k = 0;

    Queue<Integer> fileSommet = new LinkedList<>();
    fileSommet.add(from);
    sommetsEnFile[from] = true;
    fileSommet.add(null); //Sentinelle

    while(!fileSommet.isEmpty()){
      Integer curr = fileSommet.remove();
      if(curr == null) {
        if(!fileSommet.isEmpty()) {
          k += 1;
          if (k == graph.getNVertices()) {
            aCircuitAbsorbant = true;
            for(int i = 0; i < k + 1; i++){
                curr = fileSommet.remove();
            }
            negativeCycle = findNegativeCycle(curr, p, graph);
            break; //R contient un circuit absorbant accessible depuis "from"
          } else fileSommet.add(null);
        }
      } else {
          sommetsEnFile[curr] = false;

          for(WeightedDigraph.Edge edge : graph.getOutgoingEdges(curr)){
            //public record Edge(int from, int to, int weight)
            int succ = edge.to();
            if(lambda[succ] > lambda[curr] + edge.weight()){ //Amélioration -> mise à jour (pas sur de mettre j.weight() lol)
              lambda[succ] = lambda[curr] + edge.weight();
              p[succ] = curr;
              if(!sommetsEnFile[succ]){ // Vérifier contenance pour j dans Queue
                fileSommet.add(succ);
              }
            }
          }
      }
      }

    BFYResult res; // La fonction ne retourne pas la meme chose selon le résultat de notre Bellman-Ford-Yens
    if(aCircuitAbsorbant){
      res = new BFYResult.NegativeCycle(negativeCycle, negativeCycle.size());
    } else {
      res = new BFYResult.ShortestPathTree(lambda,p);
    }

    //TROUVER CYCLE NÉGATIF -> 5.4 dans le cours
    return res;
  }

  private List<Integer> findNegativeCycle(int start, int[] p, WeightedDigraph graph) {
    List<Integer> cycle = new LinkedList<>();
    int curr = start;
    Set<Integer> visited = new HashSet<>();

    while (!visited.contains(curr)) {
      visited.add(curr);
      cycle.add(curr);
      curr = p[curr];
    }

    int startIndex = cycle.indexOf(curr);
    return cycle.subList(startIndex, cycle.size());
  }
}


