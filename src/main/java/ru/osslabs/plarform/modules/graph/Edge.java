package ru.osslabs.plarform.modules.graph;

/**
 * Created by ikuchmin on 03.03.16.
 */
public interface Edge<V> {

    V getSource();

    V getTarget();
}
