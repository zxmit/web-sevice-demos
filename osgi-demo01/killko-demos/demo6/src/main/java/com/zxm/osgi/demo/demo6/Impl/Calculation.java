/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo6.Impl;

import com.zxm.osgi.demo.demo2.contract.ICalculation;

/**
 *
 * @author han
 */
public class Calculation implements ICalculation {

    @Override
    public long add(long p1, long p2) {
        return p1+p2;
    }

    @Override
    public long sub(long p1, long p2) {
        return p1-p2;
    }

    @Override
    public long mul(long p1, long p2) {
        return p1*p2;
    }

}
