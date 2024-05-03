package gre.lab2.groupD;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.Arrays;

public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {

  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {
    int nbVertices = graph.getNVertices();
    int[] lambda = new int[nbVertices];
    Integer[] p = new Integer[nbVertices];
    for(int j = 0; j < nbVertices; j++){
      lambda[j] = Integer.MAX_VALUE;
      p[j] = null;
    }

    return null;
  }
}
