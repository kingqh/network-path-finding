/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding;

/**
 * 描述：节点的结构及方法定义
 *
 * @author kingqh
 */
public interface Node {

    int id();

    String getName();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

}
