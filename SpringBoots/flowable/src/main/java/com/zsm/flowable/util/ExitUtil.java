package com.zsm.flowable.util;

import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-07 15:02.
 * @Description:
 */
public class ExitUtil
{
    private static final String EXIT_PROCESS_KEY = "exit";

    /**
     * 提交
     *
     * @param inputUserId 申请人ID
     * @param audiUserId  审核人ID
     * @param exitId      出库单ID
     */
    public static void submit(String inputUserId, String audiUserId, String exitId)
    {

        if (inputUserId.isEmpty() || audiUserId.isEmpty() || exitId.isEmpty())
        {
            throw new RuntimeException("【saveAndSubmit】参数缺失！");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("inputUserId", inputUserId);
        map.put("audiUserId", audiUserId);
        FlowableUtil util = new FlowableUtil();
        util.startAndComplete(EXIT_PROCESS_KEY, exitId, map);
    }

    /**
     * 通过-审核
     *
     * @param userId 审核操作人ID
     * @param exitId 出库单ID
     */
    public static void successApplyDepartment(String userId, String exitId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "YES");
        apply(userId, exitId, map);
    }

    /**
     * 拒绝-审核
     *
     * @param userId 审核操作人ID
     * @param exitId 采购计划ID
     */
    public static void failApplyDepartment(String userId, String exitId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "NO");
        apply(userId, exitId, map);
    }

    /**
     * 申请处理方法
     *
     * @param userId 申请操作人ID
     * @param exitId 出库单ID
     * @param map    变量组
     */
    private static void apply(String userId, String exitId, HashMap<String, Object> map)
    {

        FlowableUtil util = new FlowableUtil();
        List<Task> list = util.getListByUserId(userId);
        Task task = util.getOneTask(list, exitId);
        if (null != task)
        {
            util.complete(task.getId(), map);
        }
    }
}
