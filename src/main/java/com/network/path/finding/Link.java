/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding;

/**
 *描述： 链路的结构及方法定义
 *
 * @author kingqh
 */
public interface Link {

    /**
     * 整型id
     * @return 唯一标识
     */
    int id();

    /**
     * 源节点
     * @return 节点对象
     */
    Node from();

    /**
     * 目的节点
     * @return 节点对象
     */
    Node to();

    /**
     * 获取对应属性值
     * @param attr 属性类型
     * @return 属性值
     */
    int get(Attr attr);

    /**
     * 根据优先级获取剩余带宽
     * @param pri 优先级
     * @return 剩余带宽
     */
    int getUnreservedPri(int pri);

    Link unwrap();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

}
