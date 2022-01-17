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
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class TestKsp extends TestTopoBase {
    @Before
    public void init() {
        initTopo();
    }

    /**
     *   C --3- D --4- F \
     *    \ 2  1|   2/ |2  \ 1
     *      \   | /    |    \
     *         E --3- G --2- H
     */
    @Test
    public void testSimpleTopo() {
        addLink(getNode("C"), getNode("D"),Attr.COST, 3);
        addLink(getNode("D"), getNode("C"),Attr.COST, 3);
        addLink(getNode("D"), getNode("F"), Attr.COST, 4);
        addLink(getNode("F"), getNode("D"), Attr.COST, 4);
        addLink(getNode("C"), getNode("E"), Attr.COST, 2);
        addLink(getNode("E"), getNode("C"), Attr.COST, 2);
        addLink(getNode("E"), getNode("D"), Attr.COST, 1);
        addLink(getNode("D"), getNode("E"), Attr.COST, 1);
        addLink(getNode("E"), getNode("F"), Attr.COST, 2);
        addLink(getNode("F"), getNode("E"), Attr.COST, 2);
        addLink(getNode("E"), getNode("G"), Attr.COST, 3);
        addLink(getNode("G"), getNode("E"), Attr.COST, 3);
        addLink(getNode("F"), getNode("G"), Attr.COST, 2);
        addLink(getNode("G"), getNode("F"), Attr.COST, 2);
        addLink(getNode("G"), getNode("H"), Attr.COST, 2);
        addLink(getNode("H"), getNode("G"), Attr.COST, 2);
        addLink(getNode("F"), getNode("H"), Attr.COST, 1);
        addLink(getNode("H"), getNode("F"), Attr.COST, 1);
        Attr[] cmp = new Attr[]{Attr.COST};
        List<List<Link>> paths = alogrithm.createPathFinder(getNode("C"), getNode("H"), new HashMap<>(), cmp).find(10);
        paths.forEach(System.out::println);
    }
}
