/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.test;

import com.network.path.finding.test.data.DummyLink;
import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.PathFinder;
import com.network.path.finding.test.data.Flow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFlowAlgorithm extends TestTopoBase {

    @Before
    public void init() {
        initTopo();
    }

    /**     B \
     *    /    \
     *  A  -C - E -- F
     *   \     /
     *     D /
     */
    @Test
    public void testAddFlowBandwidth() {
        addLink(getNode("A"), getNode("B"),Attr.BANDWIDTH, 100);
        addLink(getNode("A"), getNode("C"), Attr.BANDWIDTH, 100);
        addLink(getNode("A"), getNode("D"), Attr.BANDWIDTH, 100);
        addLink(getNode("B"), getNode("E"), Attr.BANDWIDTH, 100);
        addLink(getNode("C"), getNode("E"), Attr.BANDWIDTH, 100);
        addLink(getNode("D"), getNode("E"), Attr.BANDWIDTH, 100);
        addLink(getNode("E"), getNode("F"), Attr.BANDWIDTH, 100);
        Attr[] cmp = new Attr[]{Attr.UNRESERVED};
        PathFinder pf = alogrithm.createPathFinder(getNode("A"), getNode("F"), new HashMap<>(), cmp);
        Map<Attr, Integer> attr1 = new HashMap<>();
        attr1.put(Attr.BANDWIDTH, 10);
        List<Link> path1 = pf.find();
        Flow flow1 = new Flow(getNode("A"), getNode("F"), path1,0, attr1);
        path1.forEach(l -> ((DummyLink) l.unwrap()).addFlow(flow1));
        path1.forEach(l -> Assert.assertEquals(90, l.get(Attr.UNRESERVED)));
        path1.forEach(l -> Assert.assertEquals(90, l.getUnreservedPri(0)));
        path1.forEach(l -> Assert.assertEquals(90, l.getUnreservedPri(1)));

        Map<Attr, Integer> attr2 = new HashMap<>();
        attr2.put(Attr.BANDWIDTH, 20);
        List<Link> path2 = pf.find();
        Flow flow2 = new Flow(getNode("A"), getNode("F"), path2,1, attr2);
        path2.forEach(l -> ((DummyLink) l.unwrap()).addFlow(flow2));
        Assert.assertEquals(80, path2.get(0).get(Attr.UNRESERVED));
        Assert.assertEquals(80, path2.get(1).get(Attr.UNRESERVED));
        Assert.assertEquals(70, path2.get(2).get(Attr.UNRESERVED));
        Assert.assertEquals(100, path2.get(0).getUnreservedPri(0));
        Assert.assertEquals(100, path2.get(1).getUnreservedPri(0));
        Assert.assertEquals(90, path2.get(2).getUnreservedPri(0));
        Assert.assertEquals(80, path2.get(0).getUnreservedPri(1));
        Assert.assertEquals(80, path2.get(1).getUnreservedPri(1));
        Assert.assertEquals(70, path2.get(2).getUnreservedPri(1));

        path2.forEach(l -> ((DummyLink) l.unwrap()).removeFlow(flow2));
        Assert.assertEquals(100, path2.get(0).get(Attr.UNRESERVED));
        Assert.assertEquals(100, path2.get(1).get(Attr.UNRESERVED));
        Assert.assertEquals(90, path2.get(2).get(Attr.UNRESERVED));
        Assert.assertEquals(100, path2.get(0).getUnreservedPri(0));
        Assert.assertEquals(100, path2.get(1).getUnreservedPri(0));
        Assert.assertEquals(90, path2.get(2).getUnreservedPri(0));
        Assert.assertEquals(100, path2.get(0).getUnreservedPri(1));
        Assert.assertEquals(100, path2.get(1).getUnreservedPri(1));
        Assert.assertEquals(90, path2.get(2).getUnreservedPri(1));
    }
}
