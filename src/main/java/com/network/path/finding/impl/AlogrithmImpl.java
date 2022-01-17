/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.impl;

import com.network.path.finding.Alogrithm;
import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.Node;
import com.network.path.finding.PathFinder;
import com.network.path.finding.data.PathAttr;
import com.network.path.finding.data.Topology;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;

public class AlogrithmImpl implements Alogrithm {

    private Topology topology;

    public AlogrithmImpl() {
        topology = new Topology();
    }

    @Override
    public void onNodeAdd(Node node) {
        topology.addNode(node);
    }

    @Override
    public void onNodeRemove(Node node) {
        topology.removeNode(node);
    }

    @Override
    public void onLinkAdd(Link link) {
        topology.addLink(link);
    }

    @Override
    public void onLinkRemove(Link link) {
        topology.removeLink(link);
    }

    @Override
    public PathFinder createPathFinder(Node from, Node to, Map<Attr, Integer> attrMap, Attr[] cmpSeq) {
        PathAttr constraint = new PathAttr().setWorstValue();
        attrMap.forEach(constraint::set);

        BiFunction<int[], int[], int[]> sum = (t, u) -> {
            int[] values = new int[cmpSeq.length];
            for (int i = 0; i < cmpSeq.length; i++) {
                values[i] = cmpSeq[i].sum(t[i], u[i]);
            }
            return values;
        };

        Function<Link, int[]> eval = l -> {
            int[] values = new int[cmpSeq.length];
            for (int i = 0; i < cmpSeq.length; i++) {
                values[i] = l.get(cmpSeq[i]);
            }
            return values;
        };

        ToIntBiFunction<int[], int[]> cmp = (l, r) -> {
            for (int i = 0; i < cmpSeq.length; i++) {
                int c;
                if (cmpSeq[i] == Attr.DELAY || cmpSeq[i] == Attr.JITTER) {
                    c = Integer.compare(l[i] / 10, r[i] / 10);
                } else if (cmpSeq[i] == Attr.UNRESERVED) {
                    c = Integer.compare(Integer.MAX_VALUE - l[i], Integer.MAX_VALUE - r[i]);
                } else {
                    c = Integer.compare(l[i], r[i]);
                }
                if (c != 0) {
                    return c;
                }
            }
            return 0;
        };
        PathFinder pathFinder = new GreedyCSPF(topology, from, to, constraint);
        pathFinder.setObjectiveFunction(eval, sum, cmp, cmpSeq);
        return pathFinder;
    }

    public String toString(Class c) {
        return topology.toString(c);
    }

}
