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

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.network.path.finding.Attr.BANDWIDTH;
import static com.network.path.finding.Attr.bandwidthToString;

public class DummyLink implements Link {

    private int id;
    private static int MAX_ID = 0;
    private Node from;
    private Node to;
    private List<Flow> flows = new ArrayList<>();
    private int[] attrs = new int[Attr.values().length];
    private int[] bandwidthReservedBelowPri;
    private static final Queue<Integer> AVAILABLE_ID = new PriorityQueue<>(Integer::compare);

    public DummyLink(Node from, Node to) {
        this.from = from;
        this.to = to;
        bandwidthReservedBelowPri = new int[Flow.MAX_PRIORITY];
        this.id = AVAILABLE_ID.isEmpty() ? MAX_ID++ : AVAILABLE_ID.remove();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public Node from() {
        return from;
    }

    @Override
    public Node to() {
        return to;
    }

    /**
     * 链路上的bandwidth指的是链路的TE可用带宽。UNRESERVED指得是TE剩余可用的带宽。
     * @param attr 属性类型
     * @return 链路不同属性值
     */
    @Override
    public int get(Attr attr) {
        if (Attr.UNRESERVED.equals(attr)) {
            return (bandwidthReservedBelowPri[Flow.MAX_PRIORITY - 1] < attrs[Attr.BANDWIDTH.ordinal()]) ?
                    attrs[Attr.BANDWIDTH.ordinal()] - bandwidthReservedBelowPri[Flow.MAX_PRIORITY - 1] : 0;
        }
        return attrs[attr.ordinal()];
    }

    /**
     * 优先级抢占时用的带宽
     * @param pri 优先级
     * @return 不同优先级的剩余可用带宽。优先级越高，剩余可用越多。
     */
    @Override
    public int getUnreservedPri(int pri) {
        return attrs[Attr.BANDWIDTH.ordinal()] - bandwidthReservedBelowPri[pri];
    }

    public List<Flow> getFlows() {
        return flows;
    }

    /**
     * 将流的带宽占用到链路上，类似设备auto-bandwidth改变占用带宽
     * @param flow 链路上的业务流
     */
    public void addFlow(Flow flow) {
        flows.add(flow);
        for (int pri = flow.getPri(); pri < Flow.MAX_PRIORITY; pri++) {
            bandwidthReservedBelowPri[pri] += flow.get(BANDWIDTH);
        }
    }

    public void removeFlow(Flow flow) {
        flows.remove(flow);
        for (int pri = flow.getPri(); pri < Flow.MAX_PRIORITY; pri++) {
            bandwidthReservedBelowPri[pri] -= flow.get(BANDWIDTH);
        }
    }

    @Override
    public Link unwrap() {
        return this;
    }

    public void freeId() {
        AVAILABLE_ID.add(id);
        id = -1;
    }

    public void setAttr(Attr attr, int value) {
        attrs[attr.ordinal()] = value;
    }

    @Override
    public String toString() {
        return String.format("link: id : %d %s -> %s , attr: b %s u %s c %d d %d j %d l %d brp: %s", id, from, to,
                bandwidthToString(attrs[Attr.BANDWIDTH.ordinal()]), bandwidthToString(attrs[Attr.UNRESERVED.ordinal()]),
                attrs[Attr.COST.ordinal()], attrs[Attr.DELAY.ordinal()], attrs[Attr.JITTER.ordinal()],
                attrs[Attr.LOSS.ordinal()], toString(bandwidthReservedBelowPri));
    }

    private String toString(int[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0; i< array.length; i++) {
            builder.append(" ").append(array[i]).append(" ");
        }
        return builder.append("]").toString();
    }
}
