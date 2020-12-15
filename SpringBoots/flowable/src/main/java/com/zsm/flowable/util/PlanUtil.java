package com.zsm.flowable.util;

import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-07 15:04.
 * @Description:
 */
public class PlanUtil
{
    private static final String PLAN_PROCESS_KEY = "plan";

    /**
     * 提交
     *
     * @param inputUserId      采购申请人ID
     * @param departmentUserId 部门审核人ID
     * @param planId           采购计划ID
     */
    public static void submit(String inputUserId, String departmentUserId, String planId)
    {

        if (inputUserId.isEmpty() || departmentUserId.isEmpty() || planId.isEmpty())
        {
            throw new RuntimeException("【saveAndSubmit】参数缺失！");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("inputUserId", inputUserId);
        map.put("departmentUserId", departmentUserId);
        FlowableUtil util = new FlowableUtil();
        util.startAndComplete(PLAN_PROCESS_KEY, planId, map);
    }

    /**
     * 通过-部门审核
     *
     * @param userId        审核操作人ID
     * @param companyUserId 公司审批人ID
     * @param planId        采购计划ID
     */
    public static void successApplyDepartment(String userId, String companyUserId, String planId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("companyUserId", companyUserId);
        map.put("outcome", "YES");
        apply(userId, planId, map);
    }

    /**
     * 拒绝-部门审核
     *
     * @param userId 审核操作人ID
     * @param planId 采购计划ID
     */
    public static void failApplyDepartment(String userId, String planId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "NO");
        apply(userId, planId, map);
    }

    /**
     * 通过-公司审批
     *
     * @param userId 审批操作人ID
     * @param planId 采购计划ID
     */
    public static void successApplyCompany(String userId, String planId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "YES");
        apply(userId, planId, map);
    }

    /**
     * 拒绝-公司审批
     *
     * @param userId 审批操作人ID
     * @param planId 采购计划ID
     */
    public static void failApplyCompany(String userId, String planId)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "NO");
        apply(userId, planId, map);
    }

    /**
     * 申请处理方法
     *
     * @param userId 申请操作人ID
     * @param planId 采购计划ID
     * @param map    变量组
     */
    private static void apply(String userId, String planId, HashMap<String, Object> map)
    {

        FlowableUtil util = new FlowableUtil();
        List<Task> list = util.getListByUserId(userId);
        Task task = util.getOneTask(list, planId);
        if (null != task)
        {
            util.complete(task.getId(), map);
        }
    }
}
