/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.test.data;

import com.network.path.finding.Attr;
import com.network.path.finding.Link;
import com.network.path.finding.Node;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Flow {

    private final Node from;
    private final Node to;
    private int id;
    private static int MAX_ID = 0;
    private final List<Link> path;
    final int pri;
    public static final int MAX_PRIORITY = 16;
    private final Map<Attr, Integer> attrMap;
    private static final Queue<Integer> AVAILABLE_ID = new PriorityQueue<>(Integer::compare);

    public Flow(Node from, Node to, List<Link> path, int pri, Map<Attr, Integer> attrMap) {
        this.from = from;
        this.to = to;
        this.path = path;
        this.pri = pri;
        this.attrMap = attrMap;
        this.id = AVAILABLE_ID.isEmpty() ? MAX_ID++ : AVAILABLE_ID.remove();
    }

    Node from() {
        return from;
    }

    Node to() {
        return to;
    }

    int getPri() {
        return pri;
    }

    int get(Attr attr) {
        return attrMap.get(attr);
    }

    List<Link> getPath() {
        return path;
    }

    public void freeId() {
        AVAILABLE_ID.add(id);
        id = -1;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("id: %d node: %s -> %s path: %s", id, from, to, path);
    }
}
