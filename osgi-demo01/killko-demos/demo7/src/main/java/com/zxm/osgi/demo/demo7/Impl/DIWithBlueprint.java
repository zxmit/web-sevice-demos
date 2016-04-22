/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo7.Impl;

import com.zxm.osgi.demo.demo2.contract.ICalculation;

/**
 *
 * @author han
 */
public class DIWithBlueprint {

    private ICalculation calculator;

    public void setCalculator(ICalculation service) {
        this.calculator = service;
        System.out.println();
        System.out.println("7+1=" + calculator.add(7, 1));
        System.out.println("7-1=" + calculator.sub(7, 1));
        System.out.println("7*3=" + calculator.mul(7, 3));
    }
}
