/*
 * Copyright © 2018 Network.  All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0 which accompanies this distribution,
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.network.path.finding;

/**
 * 描述：链路质量属性的定义以及方法实现
 *
 * @author kingqh
 */
public enum Attr {
    /**
     * 计算参数。
     * 说明：跳数，带宽，可分配带宽，费用，时延，抖动，丢白率，着色
     */
    HOP, BANDWIDTH, UNRESERVED, COST, DELAY, JITTER, LOSS, COLOR;

    private static final Attr[] CONSTRAINTS = new Attr[] { HOP, DELAY, JITTER, LOSS };

    private static final char[] UNIT = new char[] { 'K', 'M', 'G', 'T' };

    public static Attr[] constraints() {
        return CONSTRAINTS;
    }

    public boolean isConstraint() {
        switch (this) {
            case HOP:
            case DELAY:
            case JITTER:
            case LOSS:
                return true;
            default:
                return false;
        }
    }

    public final int sum(int a, int b) {
        if (this == BANDWIDTH || this == UNRESERVED) {
            return Integer.min(a, b);
        } else if (this == LOSS) {
            return 100 - (100 - a) * (100 - b) / 100;
        } else {
            return a + b;
        }
    }

    public final int sub(int a, int b) {
        if (this == BANDWIDTH || this == UNRESERVED) {
            return a;
        } else if (this == LOSS) {
            return 100 - ((100 - a) * 100) / (100 - b);
        } else {
            return a - b;
        }
    }

    public final int compare(int a, int b) {
        return (this == BANDWIDTH || this == UNRESERVED) ? Integer.compare(b, a)
                : Integer.compare(a, b);
    }

    public static String bandwidthToString(int bandwidth) {
        if (bandwidth < 1000) {
            return String.format("%d", bandwidth);
        }
        int unitIndex = (int) Math.log10(bandwidth) / 3 - 1;
        int divider = (int) Math.pow(1000, (double) unitIndex + 1);
        return String.format("%d%c", bandwidth / divider, UNIT[unitIndex]);
    }

}
