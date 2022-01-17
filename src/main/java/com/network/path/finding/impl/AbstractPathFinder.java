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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

/**
 * 公共抽象类
 * @author kingqh
 */
public abstract class AbstractPathFinder implements PathFinder {

    protected final Topology topology;

    protected final Node from;

    protected final Node to;

    /** 选路约束条件 */
    protected final PathAttr constraint;

    /** 链路过滤 */
    protected List<Predicate<Link>> linkFilters;

    /** 优选属性长度 */
    protected int cmpAttrLen;

    /** 属性求和 */
    protected BiFunction<int[], int[], int[]> sum;

    /** 属性计算 */
    protected Function<Link, int[]> eval;

    /** 属性比较 */
    protected ToIntBiFunction<int[], int[]> cmp;

    /** 优选属性数组 */
    protected Attr[] cmpSeq;

    /** 排除 */
    protected List<Object> exclusion;

    public AbstractPathFinder(Topology topology, Node from, Node to, PathAttr constraint) {
        this.topology = topology;
        this.from = from;
        this.to = to;
        this.constraint = constraint;
        linkFilters = new ArrayList<>();
        exclusion = new ArrayList<>();
    }

    @Override
    public void setObjectiveFunction(Function<Link, int[]> eval, BiFunction<int[], int[], int[]> sum,
                                     ToIntBiFunction<int[], int[]> cmp, Attr[] cmpSeq) {
        this.cmpAttrLen = cmpSeq.length;
        this.eval = eval;
        this.sum = sum;
        this.cmp = cmp;
        this.cmpSeq = cmpSeq;
    }

    @Override
    public boolean addLinkFilter(Predicate<Link> linkFilter) {
        return linkFilters.add(linkFilter);
    }

    @Override
    public void setExclusion(List<Object> objects) {
        exclusion = objects;
    }
}
