package com.zsm.cbl;

import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class CDCEventManager
{
    public static final ConcurrentLinkedDeque<CDCEvent> queue = new ConcurrentLinkedDeque<>();
}
