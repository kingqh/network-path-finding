/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.test.data;

import com.network.path.finding.Node;

import java.util.PriorityQueue;
import java.util.Queue;

public class DummyNode implements Node {

    private String name;
    private int id;
    private static int MAX_ID = 0;
    private static final Queue<Integer> AVAILABLE_ID = new PriorityQueue<>(Integer::compare);

    public DummyNode(String name) {
        this.name = name;
        this.id = AVAILABLE_ID.isEmpty() ? MAX_ID++ : AVAILABLE_ID.remove();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void freeId() {
        AVAILABLE_ID.add(id);
        id = -1;
    }

    @Override
    public String toString() {
        return String.format(" name %s id %d ", name, id);
    }

}
