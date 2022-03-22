package com.zsm.encrypt.util;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:37.
 * @Description: Stream进度条
 */
public interface ProgressNotifier
{
    /**
     * 开始
     */
    void start();

    /**
     * 进行中
     *
     * @param progressedSize 当前处理的大小
     */
    void progressed(long progressedSize);

    /**
     * 结束
     *
     * @param totalSize 处理的总大小
     */
    void finish(long totalSize);

    /**
     * 发生了异常
     *
     * @param t 异常
     */
    void error(Exception t);
}
