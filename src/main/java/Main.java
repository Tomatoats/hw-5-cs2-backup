import java.util.*;
import java.nio.file.Paths;
import java.io.IOException;

/* COP 3503C Assignment 5
This program is written by: Alexys Veloz */


public class Main {

    //first line: 1st # = cities, 2nd # = amount of paths, 3rd is which city is capital
    // last line is total distance.
    public static void main(String[] args) {

        try (Scanner in = new Scanner(Paths.get("in.txt")))
        {
            //take in the inputs
            while (in.hasNext())
            {
                //this block of text takes in the graph
                int cities = in.nextInt();
                int paths = in.nextInt();
                int capital = in.nextInt();
                int j = 0;
                int k = 0;
                List<Edge> edges = new ArrayList<Edge>();
                for (int i = 0; i < paths; i++){
                    int x = in.nextInt() ;
                    int y = in.nextInt()  ;
                    int value = in.nextInt();
                    Edge toAdd = new Edge(x,y,value);
                    edges.add(toAdd);

                }
                int length = in.nextInt();

                 //if you need to make sure it still inputs properly use this
                //*/
                System.out.printf("%d\n",length);
                // construct graph
                Graph graph = new Graph(edges, cities+1);


                for (int source = 0; source < cities + 1; source++) {
                    findShortestPaths(graph, source, cities+1);
                }
            }
        } catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public static void findShortestPaths(Graph graph, int source, int n)
    {
// create a min-heap and push source node having distance 0
        PriorityQueue<Node> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Node(source, 0));
// set initial distance from the source to `v` as infinity
        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
// distance from the source to itself is zero
        dist.set(source, 0);
// boolean array to track vertices for which minimum
// cost is already found
        boolean[] done = new boolean[n];
        done[source] = true;
// stores predecessor of a vertex (to a print path)
        int[] prev = new int[n];
        prev[source] = -1;
// run till min-heap is empty
        while (!minHeap.isEmpty())
        {
// Remove and return the best vertex
            Node node = minHeap.poll();
// get the vertex number
            int u = node.vertex;
// do for each neighbor `v` of `u`
            for (Edge edge: graph.adjList.get(u))
            {
                int v = edge.dest;
                int weight = edge.weight;
// Relaxation step
                if (!done[v] && (dist.get(u) + weight) < dist.get(v))
                {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new Node(v, dist.get(v)));
                }
            }
// mark vertex `u` as done so it will not get picked up again
            done[u] = true;
        }
        List<Integer> route = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            if (i != source && dist.get(i) != Integer.MAX_VALUE)
            {
                getRoute(prev, i, route);
                System.out.printf("Path (%d —> %d): Minimum cost = %d, Route = %s\n",
                        source, i, dist.get(i), route);
                route.clear();
            }
        }
    }
    private static void getRoute(int[] prev, int i, List<Integer> route)
    {
        if (i >= 0)
        {
            getRoute(prev, prev[i], route);
            route.add(i);
        }
    }
}
//hear ye this shit bout to be crazy
class Edge
{
    int source, dest, weight;
    public Edge(int source, int dest, int weight)
    {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }
}

class Node
{
    int vertex, weight;
    public Node(int vertex, int weight)
    {
        this.vertex = vertex;
        this.weight = weight;
    }
}
// A class to represent a graph object
class Graph
{
    // A list of lists to represent an adjacency list
    List<List<Edge>> adjList = null;
    // Constructor
    Graph(List<Edge> edges, int n)
    {
        adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
// add edges to the directed graph
        for (Edge edge: edges) {
            adjList.get(edge.source).add(edge);
        }
    }
}
