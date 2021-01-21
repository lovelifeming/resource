package com.zsm.flowable.mapper;

import com.zsm.flowable.model.ManageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Author: zeng.
 * @Date:Created in 2021-01-07 10:27.
 * @Description:
 */
@Mapper
public interface MyselfQuery
{
    @Select("<script>SELECT NAME_ processName,bb.ID_ processId,START_TIME_ startTime," +
            "ASSIGNEE_ assignee,TEXT_ businessKey,REV_ rev FROM\n" +
            "(SELECT ID_,NAME_,DESCRIPTION_ FROM `ACT_RE_PROCDEF`) aa INNER JOIN\n" +
            "(SELECT ID_,PROC_DEF_ID_,START_TIME_,REV_ FROM `ACT_HI_PROCINST` WHERE DELETE_REASON_ IS NULL " +
            ")bb ON aa.ID_=bb.PROC_DEF_ID_ LEFT JOIN\n" +
            "(SELECT PROC_INST_ID_,ASSIGNEE_ FROM `ACT_RU_TASK`)cc ON cc.PROC_INST_ID_=bb.ID_ LEFT JOIN\n" +
            "(SELECT PROC_INST_ID_,TEXT_ FROM `ACT_HI_VARINST` " +
            "WHERE NAME_='businessKey')dd ON dd.PROC_INST_ID_=bb.ID_ WHERE " +
            "<if test='processName !=null and processName !=\"\"'>NAME_=#{processName} AND </if>" +
            "<if test='processId !=null and processId !=\"\"'>bb.ID_=#{processId} AND </if>" +
            "<if test='startTime !=null and startTime !=\"\"'>START_TIME_&gt;=#{startTime} AND </if>" +
            "<if test='endTime !=null and endTime !=\"\"'>START_TIME_&lt;=#{endTime} AND </if>" +
            "<if test='assignee !=null and assignee !=\"\"'>ASSIGNEE_=#{assignee} AND </if>" +
            "<if test='rev !=null and rev !=\"\"'>REV_=#{rev} AND </if>" +
            "1=1 ORDER BY START_TIME_ DESC </script>")
    List<ManageInfo> queryManageInfo(String processName, String processId, String startTime, String endTime,
                                     String assignee, String rev);
}
