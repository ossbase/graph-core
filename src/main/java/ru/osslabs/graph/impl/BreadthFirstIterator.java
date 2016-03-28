package ru.osslabs.graph.impl;

import ru.osslabs.graph.Edge;
import ru.osslabs.graph.Graph;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static ru.osslabs.graph.impl.BreadthFirstIterator.VisitColor.GRAY;
import static ru.osslabs.graph.impl.BreadthFirstIterator.VisitColor.WHITE;

/**
 * Created by ikuchmin on 16.03.16.
 */
public class BreadthFirstIterator<V, E extends Edge<V>> implements Graph<V, E>, Iterable<E> {
//    @Delegate(types = {GraphReadOperations.class, GraphModifyOperations.class})
    private final Graph<V, E> graph;

    private final V startVertex;


    public BreadthFirstIterator(Graph<V, E> graph) {
        Objects.requireNonNull(graph);
        if (graph.getVertices().isEmpty())
            throw new IllegalArgumentException("Graph should have one or more getVertices. Current graph haven't getVertices");

        this.graph = graph;
        this.startVertex = graph.getVertices().stream().findFirst().get();
    }

    public BreadthFirstIterator(Graph<V, E> graph, V startVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
    }

    // TODO: support ConcurrentModificationException if graph modified
    @Override
    public Iterator<E> iterator() {
        return new Itr(startVertex);
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @implSpec The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     * @implNote The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     * <p>
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @return a sequential {@code Stream} over the elements in this collection
     * @implSpec The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     * @since 1.8
     */
    Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public <R, EE extends Edge<R>, G extends Graph<R, EE>> G collectVertices(V startVertex,
                                                                             G collector,
                                                                             BiFunction<Optional<R>, V, R> mapper) {

        Map<V, Optional<R>> mappedVertices = new HashMap<>();

        BiFunction<Optional<R>, V, Optional<R>> innerFn = (parent, target) -> {
            Optional<R> mappedVertex = Optional.ofNullable(mapper.apply(parent, target));
            mappedVertices.put(target, mappedVertex);

            if (mappedVertex.isPresent()) {
                collector.addVertex(mappedVertex.get());
                if (parent.isPresent()) collector.addEdge(parent.get(), mappedVertex.get());
            }
            return mappedVertex;
        };

        innerFn.apply(Optional.empty(), startVertex);
        forEach(e -> innerFn.apply(mappedVertices.get(e.getSource()), e.getTarget()));

        return collector;
    }

    private class Itr implements Iterator<E> {

        Queue<E> queue = new LinkedList<>();
        Map<V, VisitColor> seen = new HashMap<>();

        Itr(V startVertex) {
            seen.put(startVertex, WHITE);
            encounterVertex(startVertex);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public E next() {
            E res = queue.poll();
            encounterVertex(res.getTarget());
            return res;
        }

        private V encounterVertex(V target) {
            if (seen.put(target, GRAY) == WHITE)
                graph.edgesOf(target).stream()
                        .filter(e -> !seen.containsKey(e.getTarget()))
                        .forEach(e -> {
                            seen.put(e.getTarget(), WHITE);
                            queue.offer(e);
                        });
            else
                throw new RuntimeException("Algorithm has collision. Please check implementation");

            return target;
        }
    }

    enum VisitColor {
        WHITE, GRAY
    }

    //boilerplate


    @Override
    public BiFunction<V, V, E> getEdgeFactory() {
        return graph.getEdgeFactory();
    }

    @Override
    public boolean containsEdge(E e) {
        return graph.containsEdge(e);
    }

    @Override
    public boolean containsVertex(V v) {
        return graph.containsVertex(v);
    }

    @Override
    public List<Boolean> containsVertices(V... v) {
        return graph.containsVertices(v);
    }

    @Override
    public List<Boolean> containsVertices(List<? extends V> v) {
        return graph.containsVertices(v);
    }

    @Override
    public Collection<E> edgesOf(V vertex) {
        return graph.edgesOf(vertex);
    }

    @Override
    public Collection<V> getVertices() {
        return graph.getVertices();
    }

    @Override
    public Graph<V, E> addEdge(E edge) {
        return graph.addEdge(edge);
    }

    @Override
    public Graph<V, E> addEdges(Collection<? extends E> edges) {
        return graph.addEdges(edges);
    }

    @Override
    public Graph<V, E> addEdge(V sourceVertex, V targetVertex) {
        return graph.addEdge(sourceVertex, targetVertex);
    }

    @Override
    public Graph<V, E> addVertex(V v) {
        return graph.addVertex(v);
    }

    @Override
    public Graph<V, E> addVertices(V... vertices) {
        return graph.addVertices(vertices);
    }

    @Override
    public Graph<V, E> addVertices(Collection<? extends V> vertices) {
        return graph.addVertices(vertices);
    }

    @Override
    public Graph<V, E> addGraph(Graph<V, E> sourceGraph) {
        return graph.addGraph(sourceGraph);
    }
}