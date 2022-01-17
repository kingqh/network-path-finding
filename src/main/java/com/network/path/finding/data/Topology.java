/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.data;

import com.network.path.finding.impl.LinkDecorator;
import com.network.path.finding.Link;
import com.network.path.finding.Node;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

public class Topology {

    private int maxNodeId = 0;
    private List<BitSet> rAdjMatrix;
    private List<int[]> nodeLinkRange;

    private List<Node> nodes;
    private List<LinkDecorator> links;

    public Topology() {
        nodes = new LinkedList<>();
        links = new ArrayList<>();
        nodeLinkRange = new ArrayList<>();
        rAdjMatrix = new ArrayList<>();
    }

    public void addNode(Node node) {
        checkState(node.id() >= nodes.size() || node != nodes.get(node.id()), "Node already exists.", node);
        maxNodeId = Integer.max(node.id(), maxNodeId);
        int last = nodeLinkRange.isEmpty() ? 0 : nodeLinkRange.get(nodeLinkRange.size() - 1)[1];
        int delta = node.id() + 1 - nodeLinkRange.size();
        while (delta-- > 0) {
            nodes.add(null);
            nodeLinkRange.add(new int[] { last, last });
            rAdjMatrix.add(new BitSet());
        }
        nodes.set(node.id(), node);
        rAdjMatrix.set(node.id(), new BitSet());
    }

    public void removeNode(Node node) {
        checkState(node == nodes.get(node.id()), "Node not exists or already removed.", node);
        int[] range = nodeLinkRange.get(node.id());
        checkState(range[0] == range[1] && rAdjMatrix.get(node.id()).isEmpty(), "Removing all link of the node", node.id());
        nodes.set(node.id(), null);
        rAdjMatrix.set(node.id(), null);
    }

    public void addLink(Link link) {
        checkState(nodes.contains(link.from()) && nodes.contains(link.to()), "Link from or to does not exists.", link);

        Node from = link.from(), to = link.to();
        int[] range = nodeLinkRange.get(from.id());
        int index = Collections.binarySearch(links.subList(range[0], range[1]), link,
                Comparator.comparingInt(Link::id));
        checkState(index < 0, "Duplicate link", link);

        index = range[0] - index - 1;
        links.add(index, new LinkDecorator(link));

        range[1]++;
        nodeLinkRange.subList(from.id() + 1, nodeLinkRange.size()).forEach(r -> {
            r[0]++;
            r[1]++;
        });

        rAdjMatrix.get(to.id()).set(from.id());
    }

    public void removeLink(Link link) {
        checkState(nodes.contains(link.from()) && nodes.contains(link.to()), link);

        Node from = link.from(), to = link.to();
        int[] range = nodeLinkRange.get(from.id());
        int index = Collections.binarySearch(links.subList(range[0], range[1]), link,
                Comparator.comparingInt(Link::id));
        checkState(index >= 0, "Link not found", link);

        links.remove(range[0] + index);
        range[1]--;
        nodeLinkRange.subList(from.id() + 1, nodeLinkRange.size()).forEach(r -> {
            r[0]--;
            r[1]--;
        });

        if (links.subList(range[0], range[1]).stream().noneMatch(l -> l.to().equals(link.to()))) {
            rAdjMatrix.get(to.id()).clear(from.id());
        }
    }

    public synchronized int getMaxNodeId() {
        return maxNodeId;
    }

    public List<LinkDecorator> getOutLinks(Node node) {
        int[] range = nodeLinkRange.get(node.id());
        return links.subList(range[0], range[1]);
    }

    public List<LinkDecorator> getInputLinks(Node node) {
        BitSet bitSet = rAdjMatrix.get(node.id());
        List<LinkDecorator> inputLinks = new LinkedList<>();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            int[] range = nodeLinkRange.get(i);
            List<LinkDecorator> linkList = links.subList(range[0], range[1]);
            inputLinks.addAll(linkList.stream().filter(l -> l.to() == node).collect(Collectors.toList()));
        }

        return inputLinks;
    }

    public String toString(Class c) {
        StringBuilder stringBuilder = new StringBuilder();
        if (c == Node.class) {
            nodes.stream().filter(Objects::nonNull).forEach(n -> stringBuilder.append(n).append(' '));
        } else if (c == Link.class) {
            new ArrayList<>(links).stream()
                    .sorted(Comparator.comparingInt(LinkDecorator::id))
                    .forEach(l -> stringBuilder.append(l).append('\n'));
        } else {
            stringBuilder.append("N/A");
        }
        return stringBuilder.toString();
    }
}
