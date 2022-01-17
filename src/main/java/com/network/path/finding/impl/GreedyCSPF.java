/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.impl;

import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.Node;
import com.network.path.finding.PathFinder;
import com.network.path.finding.data.PathAttr;
import com.network.path.finding.data.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;

public class GreedyCSPF extends AbstractPathFinder {

    private static final Logger LOG = LoggerFactory.getLogger(GreedyCSPF.class);

    private int maxNodeId;
    private Link[] cameFrom;
    private int[][] best;
    private PathAttr[] minConstraintFromSrc;
    private PathAttr[] minConstraintToDst;
    private List<List<Link>> paths = new LinkedList<>();
    private PriorityQueue<List<Link>> potentialPaths = new PriorityQueue<>(this::comparePath);

    public GreedyCSPF(Topology topology, Node from, Node to, PathAttr constraint) {
        super(topology, from, to, constraint);
        init();
    }

    private void init() {
        maxNodeId = topology.getMaxNodeId();
        cameFrom = new Link[maxNodeId + 1];
        minConstraintToDst = new PathAttr[maxNodeId + 1];
        minConstraintFromSrc = new PathAttr[maxNodeId + 1];
        best = new int[maxNodeId + 1][cmpAttrLen];

        for (int i = 0; i <= maxNodeId; i++) {
            minConstraintToDst[i] = new PathAttr();
        }
    }

    @Override
    public List<Link> find() {
        for (Attr attr : Attr.constraints()) {
            if (constraint.get(attr) != Integer.MAX_VALUE) {
                reverseDijkstra(attr);
            }
        }
        return dijkstra(from, to);
    }

    @Override
    public List<List<Link>> find(int numOfPaths) {
        if (paths.isEmpty()) {
            paths.add(createCSPF(from, to, new LinkedList<>()).find());
        }

        int exclusionSize = exclusion.size();

        for (int k = paths.size() - 1; k < numOfPaths - 1; k++) {
            for (int i = 0; i < paths.get(k).size(); i++) {
                Node spurNode = paths.get(k).get(i).from();
                List<Link> rootPath = paths.get(k).subList(0, i);

                for (List<Link> p : paths) {
                    if (rootPath.equals(p.subList(0, Integer.min(i, p.size())))) {
                        exclusion.add(p.get(i));
                    }
                }

                rootPath.forEach(l -> exclusion.addAll(topology.getOutLinks(l.from())));

                List<Link> spurPath = createCSPF(spurNode, to, rootPath).find();
                if (!spurPath.isEmpty()) {
                    List<Link> totalPath = new ArrayList<>(rootPath);
                    totalPath.addAll(spurPath);
                    if (!potentialPaths.contains(totalPath)) {
                        potentialPaths.add(totalPath);
                    }
                }
            }

            if (potentialPaths.isEmpty()) {
                break;
            }

            paths.add(potentialPaths.remove());
            exclusion.subList(exclusionSize, exclusion.size()).clear();
        }

        return paths;
    }

    @Override
    public void setObjectiveFunction(Function<Link, int[]> eval, BiFunction<int[], int[], int[]> sum,
                                     ToIntBiFunction<int[], int[]> cmp, Attr[] cmpSeq) {
        super.setObjectiveFunction(eval, sum, cmp, cmpSeq);
        best = new int[maxNodeId + 1][cmpAttrLen];
        for (int i = 0; i < cmpSeq.length; i++) {
            if (cmpSeq[i].equals(Attr.UNRESERVED)) {
                best[from.id()][i] = Integer.MAX_VALUE / 10;
                break;
            }
        }
    }

    private List<Link> dijkstra(Node from, Node to) {
        BitSet openTableInBitSet = new BitSet(topology.getMaxNodeId());
        BitSet closeTable = new BitSet(topology.getMaxNodeId());

        Set<Link> excludeLinks = new HashSet<>();
        for (Object obj : exclusion) {
            if (obj instanceof Link) {
                excludeLinks.add((Link) obj);
            } else if (obj instanceof Node) {
                closeTable.set(((Node) obj).id());
            }  else {
                LOG.error("unrecognized exclude object.");
            }
        }

        // 从小到大排序
        PriorityQueue<Node> openTable = new PriorityQueue<>((n1, n2) -> cmp.applyAsInt(best[n1.id()], best[n2.id()]));

        for (int i = 0; i < cameFrom.length; i++) {
            cameFrom[i] = null;
            minConstraintFromSrc[i] = new PathAttr();
        }

        openTable.add(from);
        openTableInBitSet.set(from.id());
        while (!openTable.isEmpty()) {
            Node curNode = openTable.remove();
            openTableInBitSet.clear(curNode.id());

            if (curNode.equals(to)) {
                return new ArrayList<>(buildPath(from, to));
            }
            closeTable.set(curNode.id());
            for (LinkDecorator link : topology.getOutLinks(curNode)) {
                Node neighbor = link.to();
                int neighborId = neighbor.id();
                if (closeTable.get(neighborId) || excludeLinks.contains(link)) {
                    continue;
                }

                PathAttr pathAttr = PathAttr.sum(minConstraintFromSrc[curNode.id()], new PathAttr(link));
                // 链路过滤：源到当前节点属性 + 链路属性 + 邻居节点到目的的最小属性 > 约束属性，则舍弃该链路
                if (!linkFilters.stream().allMatch(f -> f.test(link)) ||
                        constraint.compareTo(PathAttr.sum(pathAttr, minConstraintToDst[neighborId]))) {
                    continue;
                }
                int[] tentativeScore;
                try {
                    tentativeScore = sum.apply(best[curNode.id()], eval.apply(link));
                } catch (RuntimeException e) {
                    LOG.error("exception ", e);
                    return new ArrayList<>();
                }

                if (!openTableInBitSet.get(neighborId) || cmp.applyAsInt(tentativeScore, best[neighborId]) < 0) {
                    cameFrom[neighborId] = link;
                    minConstraintFromSrc[neighborId] = pathAttr;
                    best[neighborId] = tentativeScore;
                    if (!openTableInBitSet.get(neighborId)) {
                        openTable.add(neighbor);
                        openTableInBitSet.set(neighborId);
                    } else {
                        openTable.remove(neighbor);
                        openTable.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private void reverseDijkstra(Attr attr) {
        BitSet openTableInBitSet = new BitSet(topology.getMaxNodeId() + 1);
        BitSet closeTable = new BitSet(topology.getMaxNodeId() + 1);
        PriorityQueue<Node> openTable = new PriorityQueue<>(
                (n1, n2) -> attr.compare(minConstraintToDst[n1.id()].get(attr), minConstraintToDst[n2.id()].get(attr)));

        for (PathAttr a : minConstraintToDst) {
            a.set(attr, Integer.MAX_VALUE);
        }
        minConstraintToDst[to.id()].set(attr, 0);

        openTable.add(to);
        openTableInBitSet.set(to.id());
        while (!openTable.isEmpty()) {
            Node t = openTable.remove();
            openTableInBitSet.clear(t.id());
            closeTable.set(t.id());

            if (t == from) {
                continue;
            }

            for (LinkDecorator link : topology.getInputLinks(t)) {
                Node f = link.from();
                if (closeTable.get(f.id())) {
                    continue;
                }
                int value = minConstraintToDst[t.id()].get(attr) + link.get(attr);
                if (!openTableInBitSet.get(f.id()) || value < minConstraintToDst[f.id()].get(attr)) {
                    minConstraintToDst[f.id()].set(attr, value);
                    if (!openTableInBitSet.get(f.id())) {
                        openTable.add(f);
                        openTableInBitSet.set(f.id());
                    } else {
                        openTable.remove(f);
                        openTable.add(f);
                    }
                }
            }
        }
    }

    private List<Link> buildPath(Node from, Node to) {
        Node current = to;
        List<Link> path = new LinkedList<>();
        while (!from.equals(current)) {
            path.add(0, cameFrom[current.id()]);
            current = cameFrom[current.id()].from();
        }

        return path;
    }

    private PathFinder createCSPF(Node from, Node to, List<Link> rootPath) {
        PathAttr c = constraint;
        for (Link link : rootPath) {
            c = PathAttr.sub(c, new PathAttr(link));
        }
        GreedyCSPF greedyCSPF = new GreedyCSPF(topology, from, to, c);
        greedyCSPF.setObjectiveFunction(eval, sum, cmp, cmpSeq);
        greedyCSPF.setExclusion(exclusion);
        linkFilters.forEach(greedyCSPF::addLinkFilter);
        return greedyCSPF;
    }

    private int comparePath(List<Link> p1, List<Link> p2) {
        int[] v1 = new int[cmpAttrLen], v2 = new int[cmpAttrLen];
        for (Link l : p1) {
            v1 = sum.apply(v1, eval.apply(l));
        }
        for (Link l : p2) {
            v2 = sum.apply(v2, eval.apply(l));
        }
        return cmp.applyAsInt(v1, v2);
    }
}
