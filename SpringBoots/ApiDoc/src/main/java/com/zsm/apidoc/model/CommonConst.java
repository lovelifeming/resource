package com.zsm.apidoc.model;

/**
 * @Author :zengsm.
 * @Description :
 * @Date:Created in 2019/5/24 11:06.
 * @Modified By :
 */
public class CommonConst
{
    public static final String JSON_DATA_LIST = "dataList";

    private static String RESULT_TYPE_NORMAL = "normal";

    private static String RESULT_TYPE_PAGE = "page";

    private static String RESULT_TYPE_LIST = "list";

    private static String RESULT_TYPE_OTHER = "other";

    private static String JSON_ERROR_CODE = "errorCode";

    private static String JSON_ERROR_MSG = "errorMsg";

    private static String JSON_START_PAGE_NUM = "startPageNum";

    private static String JSON_PAGE_SIZE = "pageSize";

    private static String JSON_PAGE_COUNT = "pageCount";

    private static String JSON_TOTAL_COUNT = "totalCount";

    public static final String RESULT_TYPE_NORMAL_FINAL = "normal";

    public static String getJsonDataList()
    {
        return JSON_DATA_LIST;
    }

    public static String getResultTypeNormal()
    {
        return RESULT_TYPE_NORMAL;
    }

    public static String getResultTypePage()
    {
        return RESULT_TYPE_PAGE;
    }

    public static String getResultTypeList()
    {
        return RESULT_TYPE_LIST;
    }

    public static String getResultTypeOther()
    {
        return RESULT_TYPE_OTHER;
    }

    public static String getJsonErrorCode()
    {
        return JSON_ERROR_CODE;
    }

    public static String getJsonErrorMsg()
    {
        return JSON_ERROR_MSG;
    }

    public static String getJsonStartPageNum()
    {
        return JSON_START_PAGE_NUM;
    }

    public static String getJsonPageSize()
    {
        return JSON_PAGE_SIZE;
    }

    public static String getJsonPageCount()
    {
        return JSON_PAGE_COUNT;
    }

    public static String getJsonTotalCount()
    {
        return JSON_TOTAL_COUNT;
    }

    public static String getResultTypeNormalFinal()
    {
        return RESULT_TYPE_NORMAL_FINAL;
    }
}
