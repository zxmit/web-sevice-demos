package com.zxmit.titan.writer;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.thinkaurelius.titan.core.util.TitanId;
import com.thinkaurelius.titan.graphdb.database.management.ManagementSystem;
import com.zxmit.titan.bean.Person;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by zxm on 2016/3/28.
 */
public class TitanGraphWriter {

    private long costTime;
    private int expectNum;
    private int vertexNum;
    Map<String, Long> vertexId = new HashMap<String, Long>();
    private void test(TitanGraph graph, Person person) {
        TitanTransaction tx = graph.newTransaction();
        tx.addVertex(T.label, "person",T.id, TitanId.toVertexId(Long.parseLong(person.getId())), "name", person.getName());
        tx.commit();
    }





    protected void printInfo() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("预期图中节点数: " + expectNum);
        System.out.println("实际图中节点数: " + vertexNum);
        System.out.println("构建图谱耗时: " + costTime);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void loadDataWithoutSearchWithBatch(final TitanGraph graph, List<Person> persons, GraphTraversalSource g) {
        long start = System.currentTimeMillis();
        graph.tx().rollback();
        try {
            for(Person person : persons) {
                expectNum += 3;
                Vertex son = graph.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
                Vertex father  = graph.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
                Vertex mother = graph.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
                String guardianId = person.getGuardianId();
                Vertex guardian = null;
                if(guardianId.equals(person.getDadId())) {
                    guardian = father;
                } else if(guardianId.equals(person.getMomId())) {
                    guardian = mother;
                } else {
                    guardian = graph.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
                }
                son.addEdge("father", father);
                son.addEdge("mother", mother);
                son.addEdge("guardian", guardian);
                vertexNum += 3;
            }

            graph.tx().commit();
            long end = System.currentTimeMillis();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@cost time ["+ (end-start) +"]");
            costTime += end-start;
        } catch (Exception e) {
            e.printStackTrace();
            graph.tx().rollback();
        } finally {

        }
    }

    public void loadDataWithoutSearch(final TitanGraph graph, Person person, GraphTraversalSource g) {
        expectNum += 3;
        long start = System.currentTimeMillis();
        graph.tx().rollback();
        try {
            Vertex son = graph.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
            Vertex father  = graph.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
            Vertex mother = graph.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
            String guardianId = person.getGuardianId();
            Vertex guardian = null;
            if(guardianId.equals(person.getDadId())) {
                guardian = father;
            } else if(guardianId.equals(person.getMomId())) {
                guardian = mother;
            } else {
                guardian = graph.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
            }
            son.addEdge("father", father);
            son.addEdge("mother", mother);
            son.addEdge("guardian", guardian);
            graph.tx().commit();
            long end = System.currentTimeMillis();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@cost time ["+ (end-start) +"]");
            costTime += end-start;
            vertexNum += 3;
        } catch (Exception e) {
            e.printStackTrace();
            graph.tx().rollback();
        } finally {

        }
    }

    Map<String, Vertex> vertexes = new HashMap<String, Vertex>();
    int count = 0;
    public void loadDataWithoutSearchForContrast(final TitanGraph graph, Person person, GraphTraversalSource g, boolean commitImmediately) {
        expectNum += 3;
        long start = System.currentTimeMillis();
//        graph.tx().rollback();
        try {
            Vertex son = graph.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
            Vertex father  = graph.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
            Vertex mother = graph.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
            Vertex guardian = graph.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
            son.addEdge("father", father);
            son.addEdge("mother", mother);
            son.addEdge("guardian", guardian);
            count += 4;
            if(count >= 700 || commitImmediately) {
                graph.tx().commit();
                count = 0;
            }
            long end = System.currentTimeMillis();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@cost time ["+ (end-start) +"]");
            costTime += end-start;
            vertexNum += 3;
        } catch (Exception e) {
            e.printStackTrace();
            graph.tx().rollback();
        }
    }

    /**
     * 不创建新的事务，使用graph添加节点
     * @param graph
     * @param person
     * @param g
     */
    public void loadDataBeforeSearch(final TitanGraph graph, Person person, GraphTraversalSource g) {
        expectNum++;
        long start = System.currentTimeMillis();
        graph.tx().rollback();
        try {
            Vertex son = vertexExist(g, person.getId());
            if(son == null) {
                son = graph.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
            }
            Vertex father = vertexExist(g, person.getDadId());
            if(father == null) {
                father = graph.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
            }
            Vertex mother = vertexExist(g, person.getMomId());
            if(mother == null) {
                mother = graph.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
            }
            String guardianId = person.getGuardianId();
            Vertex guardian = null;
            if(guardianId.equals(person.getDadId())) {
                guardian = father;
            } else if(guardianId.equals(person.getMomId())) {
                guardian = mother;
            } else {
                guardian = vertexExist(g, person.getGuardianId());
                if(guardian == null) {
                    guardian = graph.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
                }
            }
            son.addEdge("father", father);
            son.addEdge("mother", mother);
            son.addEdge("guardian", guardian);
            graph.tx().commit();
            long end = System.currentTimeMillis();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@cost time ["+ (end-start) +"]");
            costTime += end-start;
            vertexNum++;
        } catch (Exception e) {
            e.printStackTrace();
            graph.tx().rollback();
        } finally {

        }
    }

    private Vertex getVertex(Map<String, Vertex> vertexes, String name, String cardId, TitanGraph graph, GraphTraversalSource g) {
        Vertex vertex = null;
        if(vertexes.containsKey(cardId)) {
            vertex = vertexes.get(cardId);
        } else {
            vertex = vertexExist(g, cardId);
            if(vertex == null ) {
                vertex = graph.addVertex(T.label, "person", "name", name, "cardId", cardId);
                vertexes.put(cardId, vertex);
            }
        }
        return vertex;
    }

    public void loadDataBeforeSearchWithBatch(final TitanGraph graph, List<Person> persons, GraphTraversalSource g) {
        expectNum++;
        long start = System.currentTimeMillis();
        Map<String, Vertex> vertexes = new HashMap<String, Vertex>();
        graph.tx().rollback();
        try {
            for(Person person : persons) {
                Vertex son = getVertex(vertexes, person.getName(), person.getId(), graph, g);
                Vertex father = getVertex(vertexes, person.getDadName(), person.getDadId(), graph, g);
                Vertex mother = getVertex(vertexes, person.getMomName(), person.getMomId(), graph, g);
                String guardianId = person.getGuardianId();
                Vertex guardian = null;
                if(guardianId.equals(person.getDadId())) {
                    guardian = father;
                } else if(guardianId.equals(person.getMomId())) {
                    guardian = mother;
                } else {
                    guardian = getVertex(vertexes, person.getGuardianName(), person.getGuardianId(), graph, g);
                }
                son.addEdge("father", father);
                son.addEdge("mother", mother);
                son.addEdge("guardian", guardian);
            }
            graph.tx().commit();
            long end = System.currentTimeMillis();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@cost time ["+ (end-start) +"]");
            costTime += end-start;
            vertexNum++;
        } catch (Exception e) {
            e.printStackTrace();
            graph.tx().rollback();
        }
    }




    public void loadDataBeforeSearchNoMap(final TitanGraph graph, Person person, GraphTraversalSource g) {
        expectNum++;
        long start = System.currentTimeMillis();
        TitanTransaction tx = graph.newTransaction();
        try {
            Vertex son = vertexExist(g, person.getId());
            System.out.println("######################## Son ID : " + person.getId() + " son exist: " + (son != null));
            son = son != null ? tx.getVertex((long)son.id()) : tx.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
            Vertex father = vertexExist(g, person.getDadId());
            System.out.println("######################## Father ID : " + person.getDadId() + " father exist: " + (father != null));
            father = father != null ? tx.getVertex((long)father.id()) : tx.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
            Vertex mother = vertexExist(g, person.getMomId());
            mother = mother != null ? tx.getVertex((long)mother.id()) : tx.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
            String guardianId = person.getGuardianId();
            Vertex guardian = null;
            if(guardianId.equals(person.getDadId())) {
                guardian = father;
            } else if(guardianId.equals(person.getMomId())) {
                guardian = mother;
            } else {
                guardian = vertexExist(g, guardianId);
                System.out.println("######################## Guardian ID : " + person.getGuardianId() + " guardian exist: " + (guardian != null));
                guardian = guardian != null ? guardian : tx.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
            }
            son.addEdge("father", father);
            son.addEdge("mother", mother);
            son.addEdge("guardian", guardian);
            tx.commit();
            long end = System.currentTimeMillis();
            costTime += end-start;
            vertexNum++;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if(tx != null)
                tx.close();
        }
    }

    /**
     * 数据加载，数据加载前进行查询节点是否存在
     * @param graph
     * @param person
     * @param g
     */
    public void loadDataBeforeSearchWithMap(final TitanGraph graph, Person person, GraphTraversalSource g) {
        expectNum++;
        long start = System.currentTimeMillis();
        TitanTransaction tx = graph.newTransaction();
        try {
            Vertex son = vertexExist(g, person.getId());
            System.out.println("######################## Son ID : " + person.getId() + " son exist: " + (son != null));
            if(son != null || vertexId.containsKey(person.getId())) {
                son = son != null? tx.getVertex((long)son.id()) : tx.getVertex(vertexId.get(person.getId()));
            }else {
                son = tx.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
                vertexId.put(person.getId(), (long)son.id());
            }
//            son = son != null ? tx.getVertex((long)son.id()) : tx.addVertex(T.label, "person", "name", person.getName(), "cardId", person.getId());
            Vertex father = vertexExist(g, person.getDadId());
            System.out.println("######################## Father ID : " + person.getDadId() + " father exist: " + (father != null));
            if(father != null || vertexId.containsKey(person.getDadId())) {
                father = father != null ? tx.getVertex((long)father.id()) : tx.getVertex(vertexId.get(person.getDadId()));
            }else {
                father = tx.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
                vertexId.put(person.getDadId(), (long)father.id());
            }
//            father = father != null ? tx.getVertex((long)father.id()) : tx.addVertex(T.label, "person", "name", person.getDadName(), "cardId", person.getDadId());
            Vertex mother = vertexExist(g, person.getMomId());
            System.out.println("######################## Mother ID : " + person.getDadId() + " mother exist: " + (mother != null));
            if(mother != null || vertexId.containsKey(person.getMomId())) {
                mother =  mother != null ? tx.getVertex((long)mother.id()) : tx.getVertex(vertexId.get(person.getMomId()));
            } else {
                mother = tx.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());
                vertexId.put(person.getMomId(), (long)mother.id());
            }
//            mother = mother != null ? tx.getVertex((long)mother.id()) : tx.addVertex(T.label, "person", "name", person.getMomName(), "cardId", person.getMomId());

            String guardianId = person.getGuardianId();
            Vertex guardian = null;
            if(guardianId.equals(person.getDadId())) {
                guardian = father;
            } else if(guardianId.equals(person.getMomId())) {
                guardian = mother;
            } else {
                guardian = vertexExist(g, guardianId);
                System.out.println("######################## Guardian ID : " + person.getGuardianId() + " guardian exist: " + (guardian != null));
                if(guardian != null || vertexId.containsKey(person.getGuardianId())) {
                    guardian = guardian != null ? tx.getVertex((long)guardian.id()) : tx.getVertex(vertexId.get(person.getGuardianId()));
                } else {
                    guardian = tx.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
                    vertexId.put(person.getGuardianId(), (long)guardian.id());
                }
//                guardian = guardian != null ? guardian : tx.addVertex(T.label, "person", "name", person.getGuardianName(), "cardId", person.getGuardianId());
            }

            son.addEdge("father", father);
            son.addEdge("mother", mother);
            son.addEdge("guardian", guardian);
            tx.commit();


            long end = System.currentTimeMillis();
            costTime += end-start;
            vertexNum++;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if(tx != null)
                tx.close();
        }
    }

    /**
     * 判断节点是否存在
     * @param g
     * @param cardID
     * @return
     */
    public Vertex vertexExist(GraphTraversalSource g, String cardID) {
        GraphTraversal<Vertex, Vertex> traversal = g.V().has("cardId",cardID);
        if(traversal.hasNext()) {
            return traversal.next();
        }
        return null;
    }

    /**
     * 管理数据图元数据
     * @param graph
     */
    public void manageGraphSchema(final TitanGraph graph) {

        ManagementSystem mgmt = (ManagementSystem)graph.openManagement();
        try {
            // 创建节点的属性值，并对name属性创建索引
            if(mgmt.containsPropertyKey("name")) {
                mgmt.getPropertyKey("name").remove();
            }

            PropertyKey name = mgmt.makePropertyKey("name").dataType(String.class).make();

            if(mgmt.getGraphIndex("name") == null) {
                TitanManagement.IndexBuilder nameIndexBuilder = mgmt.buildIndex("name", Vertex.class).addKey(name);
                nameIndexBuilder.buildCompositeIndex();
            }
            if(mgmt.containsPropertyKey("cardId")) {
                mgmt.getPropertyKey("cardId").remove();
            }
            PropertyKey cardId = mgmt.makePropertyKey("cardId").dataType(String.class).make();
            TitanManagement.IndexBuilder cardIdIndexBuilder = mgmt.buildIndex("cardId", Vertex.class).addKey(cardId).unique();

            cardIdIndexBuilder.buildCompositeIndex();
            //        mgmt.setConsistency(cardIdIndex, ConsistencyModifier.LOCK);

            // 创建边标签
            if(mgmt.getEdgeLabel("father") == null) {
                mgmt.makeEdgeLabel("father").multiplicity(Multiplicity.MANY2ONE).make();
            }

            if (mgmt.getEdgeLabel("mother") == null) {
                mgmt.makeEdgeLabel("mother").multiplicity(Multiplicity.MANY2ONE).make();
            }
            if (mgmt.getEdgeLabel("guardian") == null) {
                mgmt.makeEdgeLabel("guardian").multiplicity(Multiplicity.MANY2ONE).make();
            }
            // 创建节点标签
            if (mgmt.getVertexLabel("person") == null) {
                mgmt.makeVertexLabel("person").make();
            }
            mgmt.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mgmt.rollback();
        }

    }
}
