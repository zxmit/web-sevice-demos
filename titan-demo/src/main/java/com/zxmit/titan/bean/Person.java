package com.zxmit.titan.bean;

/**
 * Created by zxm on 2016/3/7.
 */
public class Person {

    private final String prefix = "4110231989";
    private String id;
    private String name;
    private String dadId;
    private String dadName;
    private String momId;
    private String momName;
    private String guardianId;
    private String guardianName;

    public Person(int id) {
        this.id = prefix + id;
        this.name = "name" + id+"".substring(1);
        this.dadId = prefix + "00000000";
        this.momId = prefix + "00000000";
        this.guardianId = prefix + "00000000";
        this.dadName = "-";
        this.momName = "-";
        this.guardianName = "-";
    }

    public Person(int id, int dadId, int momId, int guardianId) {
        this.id = prefix + id;
        this.name = "name" + (id+"").substring(1);
        this.dadId = prefix + dadId;
        this.dadName = "name" + (dadId+"").substring(1);
        this.momId = prefix + momId;
        this.momName = "name" + (momId+"").substring(1);
        this.guardianId = prefix + guardianId;
        this.guardianName = "name" + (guardianId+"").substring(1);
    }

    public Person(String id, String name, String dadId, String dadName, String momId, String momName, String guardianId, String guardianName) {
        this.id = id;
        this.name = name;
        this.dadId = dadId;
        this.dadName = dadName;
        this.momId = momId;
        this.momName = momName;
        this.guardianId = guardianId;
        this.guardianName = guardianName;
    }

    @Override
    public String toString() {
        return  id +
                "," + name  +
                "," + dadId +
                "," + dadName +
                "," + momId +
                "," + momName +
                "," + guardianId +
                "," + guardianName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDadId() {
        return dadId;
    }

    public void setDadId(String dadId) {
        this.dadId = dadId;
    }

    public String getDadName() {
        return dadName;
    }

    public void setDadName(String dadName) {
        this.dadName = dadName;
    }

    public String getMomId() {
        return momId;
    }

    public void setMomId(String momId) {
        this.momId = momId;
    }

    public String getMomName() {
        return momName;
    }

    public void setMomName(String momName) {
        this.momName = momName;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
}
