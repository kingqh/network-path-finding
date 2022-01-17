/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;

/**
 * 算法模块对外接口。
 *
 * @author kingqh
 */
@ThreadSafe
public interface Alogrithm {

    /**
     * 拓扑中添加节点
     * @param node 节点对象
     */
    void onNodeAdd(Node node);

    /**
     * 从拓扑中删除节点
     * @param node 节点对象
     */
    void onNodeRemove(Node node);

    /**
     * 拓扑中添加链路
     * @param link 链路对象
     */
    void onLinkAdd(Link link);

    /**
     * 从拓扑中删除链路
     * @param link 链路对象
     */
    void onLinkRemove(Link link);

    /**
     * 创建一个寻路对象
     * @param from      源
     * @param to       目的
     * @param attrMap  约束的属性map
     * @param cmpSeq   优选属性列表
     * @return         寻路器
     */
    PathFinder createPathFinder(Node from, Node to, Map<Attr, Integer> attrMap, Attr[] cmpSeq);
}
