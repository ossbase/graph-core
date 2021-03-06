package ru.osslabs.graph;

import java.util.Collection;
import java.util.List;

/**
 * Created by ikuchmin on 03.03.16.
 */
public interface DirectedGraph<V, E extends Edge<V>, G extends DirectedGraph<V, E, G>>
        extends Graph<V, E, G> {

    /**
     * Returns a set of all edges incoming into the specified vertex.
     *
     * @param vertex the vertex for which the list of incoming edges to be
     * returned.
     *
     * @return a set of all edges incoming into the specified vertex.
     */
    Collection<E> incomingEdgesOf(V vertex);

    Collection<V> incomingVerticesOf(V vertex);

    /**
     * Returns a set of all edges outgoing from the specified vertex.
     *
     * @param vertex the vertex for which the list of outgoing edges to be
     * returned.
     *
     * @return a set of all edges outgoing from the specified vertex.
     */
    Collection<E> outgoingEdgesOf(V vertex);

    Collection<V> outgoingVerticesOf(V vertex);

    List<Boolean> containsOutgoingVertices(V vertex, V... vertices);

    List<Boolean> containsOutgoingVertices(V vertex, List<? extends V> vertices);

    List<Boolean> containsIncomingVertices(V vertex, V... vertices);

    List<Boolean> containsIncomingVertices(V vertex, List<? extends V> vertices);

//    boolean containsAllOutgoingVertices(V vertex, V... vertices);
//
//    boolean containsAllOutgoingVertices(V vertex, List<? extends V> vertices);
//
//    boolean containsAllIncomingVertices(V vertex, V... vertices);
//
//    boolean containsAllIncomingVertices(V vertex, List<? extends V> vertices);

}
