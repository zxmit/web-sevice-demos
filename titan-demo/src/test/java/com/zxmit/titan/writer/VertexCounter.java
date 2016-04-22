package com.zxmit.titan.writer;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.function.Function;

/**
 * Created by zxm on 2016/3/31.
 */
public class VertexCounter implements Function<Traverser<Vertex>, Object>{

    public int num;

    @Override
    public Object apply(Traverser<Vertex> vertexTraverser) {
        num++;
        return null;
    }
}
