package com.zxmit.titan.writer;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.zxmit.titan.bean.Person;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by zxm on 2016/3/31.
 */
public class TitanGraphWriterTest {

    private TitanGraphWriter writer;
    private TitanGraph graph;
    private GraphTraversalSource g;

    @Before
    public void init() {
        writer = new TitanGraphWriter();
        graph = TitanFactory.open("src/main/resources/titan-hbase-es.properties");
        g = graph.traversal();
    }

    @Test
    public void testSearchPerformanceWithSocket() throws Exception {
        Cluster cluster = Cluster.open("src/main/resources/remote.yaml");
        Client client = cluster.connect();
        long start = System.currentTimeMillis();
        System.out.println("------------------------------------------------------------");
        ResultSet results = client.submit("g.V().has(\"cardId\", \"411023199210006984\").values()");
        System.out.println(results);
//        for(Result result : results) {
//            System.out.println(result.toString());
//        }
        long end1 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end1-start) + "]");
        results = client.submit("g.V().has(\"cardId\", \"411023199210014507\").values()");
        System.out.println(results);
//        for(Result result : results) {
//            System.out.println(result.toString());
//        }
        long end2 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end2-end1) + "]");
        results = client.submit("g.V().has(\"cardId\", \"411023199210000002\").values()");
        System.out.println(results);
//        for(Result result : results) {
//            System.out.println(result.toString());
//        }
        long end3 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end3-end2) + "]");
        System.out.println("------------------------------------------------------------");
        client.close();
        cluster.close();
    }

    @Test
    public void testSearchPerformanceWithEmbedded() {
        System.out.println("------------------------------------------------------------");
        long start = System.currentTimeMillis();
        System.out.println(writer.vertexExist(g, "411023199210006984"));
        long end1 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end1-start) + "]");
        System.out.println(writer.vertexExist(g, "411023199210014507"));
        long end2 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end2-end1) + "]");
        System.out.println(writer.vertexExist(g, "411023199210000002"));
        long end3 = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [" + (end3-end2) + "]");
        System.out.println("------------------------------------------------------------");
    }

    @Test
    public void testLoadFromNothing() {
        int vertexCount = 50000;
        int base = 9000000;
        String prefix = "56102311111";
        for(int i=0; i<vertexCount; i++) {
            Person person = new Person(prefix+(base+i),"son"+(base+i),prefix+(base+i),"father"+(base+i),
                    prefix+(base+i),"mother"+(base+i),prefix+(base+i),"father"+(base+i));
            writer.loadDataWithoutSearch(graph, person, g);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%% [" + vertexCount + "]");
        }
        writer.printInfo();
    }

    @Test
    public void testLoadFromNothingWithBatch() {
        int vertexCount = 50000;
        int base = 9000000;
        int batchSize = 50;
        String prefix = "67";
        for(int i=0; i<vertexCount;) {
            List<Person> persons = new ArrayList<Person>(batchSize);
            for(int j=0; j<batchSize&&i<vertexCount; j++,i++) {
                Person person = new Person(prefix+"102311111"+(base+i),"son"+(base+i),prefix+"102411111"+(base+i),"father"+(base+i),
                        prefix+"102511111"+(base+i),"mother"+(base+i),prefix+"102411111"+(base+i),"father"+(base+i));
                persons.add(person);
            }
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%% [" + i + "]");
            System.out.println(persons);
            writer.loadDataWithoutSearchWithBatch(graph, persons, g);
        }
        writer.printInfo();
    }

    @Test
    public void testLoadFromCSV() throws IOException {
        File sourceFile = new File("src/main/resources/person_storage.csv");
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String line = "";
        int count = 1;
        while((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            Person person = new Person(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
            writer.loadDataBeforeSearch(graph, person, g);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%% [" + count++ + "]");
        }
        writer.printInfo();
    }

    @Test
    public void testLoadFromCSVWithBatch() throws IOException {
        int batchSize = 50;
        File sourceFile = new File("src/main/resources/person_storage.csv");
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String line = "";
        int count = 1;
        while((line = reader.readLine()) != null) {
            List<Person> persons = new ArrayList<Person>(batchSize);
            for(int i=0; i<batchSize; i++) {
                String[] values = line.split(",");
                Person person = new Person(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                persons.add(person);
                if((i != (batchSize-1)) && (line = reader.readLine()) == null)
                    break;
            }
            writer.loadDataBeforeSearchWithBatch(graph, persons, g);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%% [" + count++ + "]");
        }
        writer.printInfo();
    }

    @Test
    public void testLoadForContrast() {
        int vertexCount = 20000;
        int base = 9000000;
        String prefix = "58";
        for(int i=0; i<vertexCount-1; i++) {
            Person person = new Person(prefix+"102311111"+(base+i),"son"+(base+i),prefix+"102411111"+(base+i),"father"+(base+i),
                    prefix+"102511111"+(base+i),"mother"+(base+i),prefix+"102611111"+(base+i),"guardian"+(base+i));
            writer.loadDataWithoutSearchForContrast(graph, person, g, false);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%% [" + i + "]");
        }
        int i = vertexCount-1;
        Person person = new Person(prefix+"102311111"+(base+i),"son"+(base+i),prefix+"102411111"+(base+i),"father"+(base+i),
                prefix+"102511111"+(base+i),"mother"+(base+i),prefix+"102611111"+(base+i),"guardian"+(base+i));
        System.out.println("["+person+"]");
        writer.loadDataWithoutSearchForContrast(graph, person, g, true);
//        loadDataWithoutSearchForContrast()
        writer.printInfo();
    }

    @Test
    public void queryVertexNum() {
//        VertexCounter counter = new VertexCounter();
        g.V().has("cardId", "411023199510020000").values("name").next();
        g.V().has("cardId", "411023199510019999").values("name").next();
        for(int i=100; i<200; i++) {
            long start = System.currentTimeMillis();
            System.out.println(g.V().has("cardId", "411023199510009" + i).values("name").next());
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        }

        long t1 = System.currentTimeMillis();
        System.out.println(g.V().has("cardId", "411023199510019998").values("name").next());
        long t2 = System.currentTimeMillis();
        System.out.println("deep [ 1 ] : " + (t2-t1));
        System.out.println(g.V().has("cardId", "411023199510000087").as("a").out("father").values("name").next());
        long t3 = System.currentTimeMillis();
        System.out.println("deep [ 2 ] : " + (t3-t2));
        System.out.println(g.V().has("cardId", "411023199510019996").out("father").out("father").values("name").next());
        long t4 = System.currentTimeMillis();
        System.out.println("deep [ 3 ] : " + (t4-t3));
        System.out.println(g.V().has("cardId", "411023199510019995").out("father").out("father").out("father").values("name").next());
        long t5 = System.currentTimeMillis();
        System.out.println("deep [ 4 ] : " + (t5-t4));
        System.out.println(g.V().has("cardId", "411023199510019994").out("father").out("father").out("father").out("father").values("name").next());
        long t6 = System.currentTimeMillis();
        System.out.println("deep [ 5 ] : " + (t6-t5));
    }

    @Test
    public void testManageGraphSchema() {
        writer.manageGraphSchema(graph);
    }

    @After
    public void close() {
        graph.close();
    }

}
