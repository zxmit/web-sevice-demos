/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo9;

/**
 *
 * @author han
 */
public class SomeBean {
    private String demo9package;
    private String demo9version;
    private String author;

    public void setDemo9package(String demo9package) {
        this.demo9package = demo9package;
        System.out.println("demo9package被注入，值为："+demo9package);
    }

    public void setDemo9version(String demo9version) {
        this.demo9version = demo9version;
        System.out.println("demo9version被注入，值为："+demo9version);
    }

    public void setAuthor(String author) {
        this.author = author;
        System.out.println("author被注入，值为："+author);
    }

    public String getDemo9package() {
        return demo9package;
    }

    public String getDemo9version() {
        return demo9version;
    }

    public String getAuthor() {
        return author;
    }

    

}
