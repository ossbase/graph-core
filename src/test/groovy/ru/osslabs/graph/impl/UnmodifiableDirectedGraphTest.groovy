package ru.osslabs.graph.impl

import spock.lang.Specification

/**
 * Created by ikuchmin on 15.03.16.
 */
class UnmodifiableDirectedGraphTest extends Specification {
    def "unmodifiable graph should not have methods modifying state"() {
        given:
        def graph = new UnmodifiableDirectedGraph<>(
                new DirectedGraphImpl<>({ s, t -> new ExEdge<String>(s, t) })
                        .addVertex('v1').addVertex('v2'))

        when:
        graph.addEdge(new ExEdge('v1', 'v2'))

        then:
        thrown UnsupportedOperationException

        when:
        graph.addVertex('v3')

        then:
        thrown UnsupportedOperationException

        when:
        graph.addGraph(new DirectedGraphImpl<>({ s, t -> new ExEdge<String>(s, t) })
                .addVertex('v1').addVertex('v2'))

        then:
        thrown UnsupportedOperationException
    }
}
