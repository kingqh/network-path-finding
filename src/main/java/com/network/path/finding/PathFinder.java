/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

public interface PathFinder {

    List<Link> find();

    List<List<Link>> find(int k);

    /**
     * 设置选路目标函数
     * @param eval 估值函数
     * @param sum 求和函数
     * @param cmp 目标值比较函数
     * @param cmpSeq 优选属性列表
     */
    void setObjectiveFunction(Function<Link, int[]> eval, BiFunction<int[], int[], int[]> sum,
                              ToIntBiFunction<int[], int[]> cmp, Attr[] cmpSeq);

    /**
     * 链路过滤功能，可以基于带宽、亲和属性等条件进行链路过滤。
     * @param linkFilter 链路过滤函数
     * @return
     */
    boolean addLinkFilter(Predicate<Link> linkFilter);

    /**
     * 设置排除对象列表
     * @param objects 选路排除对象，可以是Node或者Link
     */
    void setExclusion(List<Object> objects);

}
