package com.zxmit.titan.reader;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.Result;

/**
 * Created by zxm on 2016/3/30.
 */
public class TitanGraphReader {
    public static void main(String[] args) throws Exception {
        Cluster cluster = Cluster.open("src/main/resources/remote.yaml");
        Client client = cluster.connect();

//        ResultSet results = client.submit("graph.addVertex(\"name1111\", \"stephen1\")");
        long start = System.currentTimeMillis();
        ResultSet results = client.submit("g.V().has(\"cardId\", \"521023111119000005\").values()");
        System.out.println("++++++++++++++++++++++++++++++");
        for(Result result : results) {
            System.out.println(result.toString());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("++++++++++++++++++++++++++++++");
        results = client.submit("g.V().has(\"cardId\", \"411023111119196790\").values()");
        System.out.println("++++++++++++++++++++++++++++++");
        for(Result result : results) {
            System.out.println(result.toString());
        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - end);
        System.out.println("++++++++++++++++++++++++++++++");
        results = client.submit("g.V().has(\"cardId\", \"411023111119116789\").values()");
        System.out.println("++++++++++++++++++++++++++++++");
        for(Result result : results) {
            System.out.println(result.toString());
        }
        long end3 = System.currentTimeMillis();
        System.out.println(end3 - end2);
        System.out.println("++++++++++++++++++++++++++++++");
        client.close();
        cluster.close();

    }
}
