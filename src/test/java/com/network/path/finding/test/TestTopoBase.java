/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.test;

import com.network.path.finding.test.data.DummyLink;
import com.network.path.finding.test.data.DummyNode;
import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.Node;
import com.network.path.finding.impl.AlogrithmImpl;

import java.util.HashMap;
import java.util.Map;

public class TestTopoBase {

    Map<String, Node> nodeMap;
    AlogrithmImpl alogrithm;

    public void initTopo() {
        alogrithm = new AlogrithmImpl();
        nodeMap = new HashMap<>();
        addNode("A");
        addNode("B");
        addNode("C");
        addNode("D");
        addNode("E");
        addNode("F");
        addNode("G");
        addNode("H");
    }

    public void addNode(String name) {
        DummyNode node = new DummyNode(name);
        alogrithm.onNodeAdd(node);
        nodeMap.put(name, node);
    }

    public Node getNode(String name) {
        return nodeMap.get(name);
    }

    public void addLink(Node from, Node to,  Object... attrs) {
        DummyLink link = new DummyLink(from, to);
        for (int i = 0; i < attrs.length; i+=2) {
            link.setAttr((Attr)attrs[i], (Integer) attrs[i + 1]);
        }
        alogrithm.onLinkAdd(link);
    }

    public void removeLink(Link link) {
        alogrithm.onLinkRemove(link);
    }

}
