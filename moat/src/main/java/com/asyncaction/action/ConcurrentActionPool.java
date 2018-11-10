package com.asyncaction.action;

import java.util.Queue;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-31
 * Time: 13:11
 */
public class ConcurrentActionPool<R> extends ActionFlow<R> {

    private final static int CONCURRENT_COUNT = 3;

    ConcurrentActionPool(ActionManager actionManager, Action action, int priority, long delayMillis) {
        super(actionManager, action, priority, delayMillis);
        Queue q;
    }

    /*package*/

}
