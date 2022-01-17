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


public class LinkDecorator implements Link {

    private Link link;

    public LinkDecorator(Link link) {
        this.link = link;
    }

    @Override
    public int id() {
        return link.id();
    }

    @Override
    public Node from() {
        return link.from();
    }

    @Override
    public Node to() {
        return link.to();
    }

    @Override
    public int get(Attr attr) {
        return link.get(attr);
    }

    @Override
    public int getUnreservedPri(int pri) {
        return link.getUnreservedPri(pri);
    }

    @Override
    public Link unwrap() {
        return link;
    }

    @Override
    public String toString() {
        return link.toString();
    }
}
