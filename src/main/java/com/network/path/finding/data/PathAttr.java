/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding.data;

import com.network.path.finding.Attr;
import com.network.path.finding.Link;

/**
 * 路径质量属性
 * @author kingqh
 */
public final class PathAttr {
    private int[] attrs = new int[Attr.values().length];

    public PathAttr() {

    }

    public PathAttr(Link link) {
        for (Attr attr : Attr.constraints()) {
            attrs[attr.ordinal()] = link.get(attr);
        }
    }

    public void set(Attr attr, int value) {
        if (attr.isConstraint()) {
            attrs[attr.ordinal()] = value;
        }
    }

    public int get(Attr attr) {
        return attrs[attr.ordinal()];
    }

    public boolean compareTo(PathAttr pathAttr) {
        for (Attr attr : Attr.constraints()) {
            if (attr.compare(attrs[attr.ordinal()], pathAttr.attrs[attr.ordinal()]) < 0) {
                return true;
            }
        }
        return false;
    }

    public static PathAttr sum(PathAttr a, PathAttr b) {
        PathAttr ret = new PathAttr();
        for (Attr attr : Attr.constraints()) {
            ret.attrs[attr.ordinal()] = attr.sum(a.attrs[attr.ordinal()], b.attrs[attr.ordinal()]);
        }
        return ret;
    }

    public static PathAttr sub(PathAttr a, PathAttr b) {
        PathAttr ret = new PathAttr();
        for (Attr attr : Attr.constraints()) {
            ret.attrs[attr.ordinal()] = attr.sub(a.attrs[attr.ordinal()], b.attrs[attr.ordinal()]);
        }
        return ret;
    }

    public PathAttr setWorstValue() {
        for (int i = 0; i < attrs.length; i++) {
            attrs[i] = Integer.MAX_VALUE;
        }
        attrs[Attr.LOSS.ordinal()] = 100;
        return this;
    }

}
