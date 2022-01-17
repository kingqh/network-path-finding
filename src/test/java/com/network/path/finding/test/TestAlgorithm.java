/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.test;

import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.PathFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAlgorithm extends TestTopoBase {



    @Before
    public void init() {
        initTopo();
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testSimpleTopo() {
        addLink(getNode("A"), getNode("B"),Attr.COST, 10);
        addLink(getNode("B"), getNode("C"), Attr.COST, 10);
        addLink(getNode("C"), getNode("D"), Attr.COST, 10);
        addLink(getNode("D"), getNode("H"), Attr.COST, 10);
        addLink(getNode("A"), getNode("E"), Attr.COST, 20);
        addLink(getNode("E"), getNode("F"), Attr.COST, 20);
        addLink(getNode("F"), getNode("G"), Attr.COST, 20);
        addLink(getNode("G"), getNode("H"), Attr.COST, 20);
        String topoStr1 = alogrithm.toString(Link.class);
        System.out.println(topoStr1);
        Assert.assertTrue(topoStr1.contains(getNode("A").toString()));
        Assert.assertTrue(topoStr1.contains(getNode("D").toString()));
        Assert.assertTrue(topoStr1.contains(getNode("H").toString()));
        Assert.assertTrue(topoStr1.contains("10"));
        Assert.assertTrue(topoStr1.contains("20"));
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testCmpHop() {
        addLink(getNode("A"), getNode("B"),Attr.HOP, 1);
        addLink(getNode("B"), getNode("C"), Attr.HOP, 1);
        addLink(getNode("C"), getNode("D"), Attr.HOP, 1);
        addLink(getNode("D"), getNode("H"), Attr.HOP, 1);
        addLink(getNode("A"), getNode("E"), Attr.HOP, 2);
        addLink(getNode("E"), getNode("F"), Attr.HOP, 2);
        addLink(getNode("F"), getNode("G"), Attr.HOP, 2);
        addLink(getNode("G"), getNode("H"), Attr.HOP, 2);
        Attr[] cmp = new Attr[]{Attr.HOP};
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.HOP, 3);
        addLink(getNode("B"), getNode("C"), Attr.HOP, 3);
        addLink(getNode("C"), getNode("D"), Attr.HOP, 3);
        addLink(getNode("D"), getNode("H"), Attr.HOP, 3);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testCmpCost() {
        addLink(getNode("A"), getNode("B"),Attr.COST, 10);
        addLink(getNode("B"), getNode("C"), Attr.COST, 10);
        addLink(getNode("C"), getNode("D"), Attr.COST, 10);
        addLink(getNode("D"), getNode("H"), Attr.COST, 10);
        addLink(getNode("A"), getNode("E"), Attr.COST, 20);
        addLink(getNode("E"), getNode("F"), Attr.COST, 20);
        addLink(getNode("F"), getNode("G"), Attr.COST, 20);
        addLink(getNode("G"), getNode("H"), Attr.COST, 20);
        Attr[] cmp = new Attr[]{Attr.COST};
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.COST, 30);
        addLink(getNode("B"), getNode("C"), Attr.COST, 30);
        addLink(getNode("C"), getNode("D"), Attr.COST, 30);
        addLink(getNode("D"), getNode("H"), Attr.COST, 30);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testCmpUreserved() {
        addLink(getNode("A"), getNode("B"),Attr.BANDWIDTH, 1000);
        addLink(getNode("B"), getNode("C"), Attr.BANDWIDTH, 1000);
        addLink(getNode("C"), getNode("D"), Attr.BANDWIDTH, 1000);
        addLink(getNode("D"), getNode("H"), Attr.BANDWIDTH, 1000);
        addLink(getNode("A"), getNode("E"), Attr.BANDWIDTH, 800);
        addLink(getNode("E"), getNode("F"), Attr.BANDWIDTH, 800);
        addLink(getNode("F"), getNode("G"), Attr.BANDWIDTH, 800);
        addLink(getNode("G"), getNode("H"), Attr.BANDWIDTH, 800);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.BANDWIDTH, 600);
        addLink(getNode("B"), getNode("C"), Attr.BANDWIDTH, 600);
        addLink(getNode("C"), getNode("D"), Attr.BANDWIDTH, 600);
        addLink(getNode("D"), getNode("H"), Attr.BANDWIDTH, 600);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testConstraintsHop() {
        addLink(getNode("A"), getNode("B"),Attr.HOP, 1);
        addLink(getNode("B"), getNode("C"), Attr.HOP, 1);
        addLink(getNode("C"), getNode("D"), Attr.HOP, 1);
        addLink(getNode("D"), getNode("H"), Attr.HOP, 1);
        addLink(getNode("A"), getNode("E"), Attr.HOP, 1);
        addLink(getNode("E"), getNode("F"), Attr.HOP, 1);
        addLink(getNode("F"), getNode("G"), Attr.HOP, 1);
        addLink(getNode("G"), getNode("H"), Attr.HOP, 2);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        Map<Attr, Integer> contraints = new HashMap<>();
        contraints.put(Attr.HOP, 4);
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.HOP, 1);
        addLink(getNode("B"), getNode("C"), Attr.HOP, 1);
        addLink(getNode("C"), getNode("D"), Attr.HOP, 1);
        addLink(getNode("D"), getNode("H"), Attr.HOP, 3);
        List<Link> emptyLinks = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        Assert.assertEquals(0, emptyLinks.size());
        contraints.put(Attr.HOP, 5);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testConstraintsDeplay() {
        addLink(getNode("A"), getNode("B"),Attr.DELAY, 1);
        addLink(getNode("B"), getNode("C"), Attr.DELAY, 1);
        addLink(getNode("C"), getNode("D"), Attr.DELAY, 1);
        addLink(getNode("D"), getNode("H"), Attr.DELAY, 1);
        addLink(getNode("A"), getNode("E"), Attr.DELAY, 1);
        addLink(getNode("E"), getNode("F"), Attr.DELAY, 1);
        addLink(getNode("F"), getNode("G"), Attr.DELAY, 1);
        addLink(getNode("G"), getNode("H"), Attr.DELAY, 2);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        Map<Attr, Integer> contraints = new HashMap<>();
        contraints.put(Attr.DELAY, 4);
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.DELAY, 1);
        addLink(getNode("B"), getNode("C"), Attr.DELAY, 1);
        addLink(getNode("C"), getNode("D"), Attr.DELAY, 1);
        addLink(getNode("D"), getNode("H"), Attr.DELAY, 3);
        List<Link> emptyLinks = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        Assert.assertEquals(0, emptyLinks.size());
        contraints.put(Attr.DELAY, 5);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testConstraintsJitter() {
        addLink(getNode("A"), getNode("B"),Attr.JITTER, 1);
        addLink(getNode("B"), getNode("C"), Attr.JITTER, 1);
        addLink(getNode("C"), getNode("D"), Attr.JITTER, 1);
        addLink(getNode("D"), getNode("H"), Attr.JITTER, 1);
        addLink(getNode("A"), getNode("E"), Attr.JITTER, 1);
        addLink(getNode("E"), getNode("F"), Attr.JITTER, 1);
        addLink(getNode("F"), getNode("G"), Attr.JITTER, 1);
        addLink(getNode("G"), getNode("H"), Attr.JITTER, 2);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        Map<Attr, Integer> contraints = new HashMap<>();
        contraints.put(Attr.JITTER, 4);
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.JITTER, 1);
        addLink(getNode("B"), getNode("C"), Attr.JITTER, 1);
        addLink(getNode("C"), getNode("D"), Attr.JITTER, 1);
        addLink(getNode("D"), getNode("H"), Attr.JITTER, 3);
        List<Link> emptyLinks = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        Assert.assertEquals(0, emptyLinks.size());
        contraints.put(Attr.JITTER, 5);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testConstraintsLoss() {
        addLink(getNode("A"), getNode("B"),Attr.LOSS, 0);
        addLink(getNode("B"), getNode("C"), Attr.LOSS, 0);
        addLink(getNode("C"), getNode("D"), Attr.LOSS, 0);
        addLink(getNode("D"), getNode("H"), Attr.LOSS, 10);
        addLink(getNode("A"), getNode("E"), Attr.LOSS, 0);
        addLink(getNode("E"), getNode("F"), Attr.LOSS, 0);
        addLink(getNode("F"), getNode("G"), Attr.LOSS, 0);
        addLink(getNode("G"), getNode("H"), Attr.LOSS, 20);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        Map<Attr, Integer> contraints = new HashMap<>();
        contraints.put(Attr.LOSS, 10);
        List<Link> links = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.LOSS, 0);
        addLink(getNode("B"), getNode("C"), Attr.LOSS, 0);
        addLink(getNode("C"), getNode("D"), Attr.LOSS, 0);
        addLink(getNode("D"), getNode("H"), Attr.LOSS, 30);
        List<Link> emptyLinks = alogrithm.createPathFinder(getNode("A"), getNode("H"), contraints, cmp).find();
        Assert.assertEquals(0, emptyLinks.size());
        contraints.put(Attr.LOSS, 20);
        List<Link> links1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp).find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }

    /**     B -- C -- D
     *    /              \
     *  A                H
     *   \  E -- F -- G /
     */
    @Test
    public void testLinkFilter() {
        addLink(getNode("A"), getNode("B"),Attr.BANDWIDTH, 1000);
        addLink(getNode("B"), getNode("C"), Attr.BANDWIDTH, 1000);
        addLink(getNode("C"), getNode("D"), Attr.BANDWIDTH, 1000);
        addLink(getNode("D"), getNode("H"), Attr.BANDWIDTH, 1000);
        addLink(getNode("A"), getNode("E"), Attr.BANDWIDTH, 800);
        addLink(getNode("E"), getNode("F"), Attr.BANDWIDTH, 800);
        addLink(getNode("F"), getNode("G"), Attr.BANDWIDTH, 800);
        addLink(getNode("G"), getNode("H"), Attr.BANDWIDTH, 800);
        Attr[] cmp = new Attr[]{Attr.HOP};
        PathFinder pf = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp);
        pf.addLinkFilter(l -> l.get(Attr.UNRESERVED) >= 1000);
        List<Link> links = pf.find();
        List<String> pathStrList = new ArrayList<>();
        pathStrList.add(getNode("A") + " -> " + getNode("B"));
        pathStrList.add(getNode("B") + " -> " + getNode("C"));
        pathStrList.add(getNode("C") + " -> " + getNode("D"));
        pathStrList.add(getNode("D") + " -> " + getNode("H"));
        Assert.assertEquals(links.size(), pathStrList.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links.get(i).toString().contains(pathStrList.get(i)));
        }
        links.forEach(this::removeLink);
        addLink(getNode("A"), getNode("B"),Attr.BANDWIDTH, 600);
        addLink(getNode("B"), getNode("C"), Attr.BANDWIDTH, 600);
        addLink(getNode("C"), getNode("D"), Attr.BANDWIDTH, 600);
        addLink(getNode("D"), getNode("H"), Attr.BANDWIDTH, 600);
        List<Link> emptyLinks = pf.find();
        Assert.assertEquals(0, emptyLinks.size());
        PathFinder pf1 = alogrithm.createPathFinder(getNode("A"), getNode("H"), new HashMap<>(), cmp);
        pf1.addLinkFilter(l -> l.get(Attr.UNRESERVED) >= 800);
        List<Link> links1 = pf1.find();
        List<String> pathStrList1 = new ArrayList<>();
        pathStrList1.add(getNode("A") + " -> " + getNode("E"));
        pathStrList1.add(getNode("E") + " -> " + getNode("F"));
        pathStrList1.add(getNode("F") + " -> " + getNode("G"));
        pathStrList1.add(getNode("G") + " -> " + getNode("H"));
        Assert.assertEquals(links1.size(), pathStrList1.size());
        for(int i = 0; i < links.size(); i++) {
            Assert.assertTrue(links1.get(i).toString().contains(pathStrList1.get(i)));
        }
    }
}
