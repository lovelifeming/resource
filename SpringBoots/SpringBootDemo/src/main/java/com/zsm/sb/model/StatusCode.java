package com.zsm.sb.model;

/**
 * @Author :zengsm.
 * @Description :
 * @Date:Created in 2019/5/22 15:35.
 * @Modified By :
 */
public enum StatusCode
{
    Success("200", "请求成功"),
    Waiting("200", "排队等待中"),
    CommitSuccess("204", "任务提交成功"),
    MultipleChoices("300", "被请求的资源存在多个可供选择的响应"),
    BadRequest("400", "请求未能处理"),
    Unauthorized("401", "请求资源无权限"),
    Forbidden("403", "请求的页面被禁止访问"),
    NotFound("404", "请求成功"),
    MethodNotAllowed("405", "请求中指定的方法不被允许"),
    NotAcceptable("406", "请求格式错误"),
    ProxyAuthenticationRequired("407", "必须代理服务器进行验证"),
    RequestTimeout("408", "服务器等待超时"),
    Conflict("409", "请求冲突"),
    Gone("410", "资源被永久删除"),
    UnprocessableEntity("422", "验证错误"),
    InternalServerError("500", "服务异常"),
    NotImplemented("501", "上游服务器无效响应"),
    BadGateway("502", "资源被永久删除"),
    ServiceUnavailable("503", "服务器暂时不可以"),
    GatewayTimeout("504", "网关超时"),

    //******************************java内置异常（非计划型）******************************
    //IllegalArgumentException	抛出的异常表明向方法传递了一个不合法或不正确的参数。
    IllegalArgumentException("0101001", "参数不合法"),

    //NullPointerException	当应用程序试图在需要对象的地方使用 null 时，抛出该异常
    NullPointerException("0201003", "空指针"),

    //ArithmeticException	当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。
    ArithmeticException("0102005", "运算条件异常"),

    //OutOfMemoryException 内存不足错误
    OutOfMemoryError("0299018", "内存不足"),

    //ArrayIndexOutOfBoundsException 数组下标越界异常
    ArrayIndexOutOfBoundsException("0202019", "数组下标越界"),

    //ClassCastException 当试图将对象强制转换为不是实例的子类时，抛出该异常。
    ClassCastException("0199020", "类型转换错误"),

    //FileNotFoundException 文件未找到异常
    FileNotFoundException("0299021", "文件未找到"),

    //ArrayStoreException 数组存储异常
    ArrayStoreException("0199022", "数组存储异常"),

    //ClassNotFoundException 应用程序试图加载类时，找不到相应的类，抛出该异常。
    ClassNotFoundException("0299024", "类加载异常，找不到相应的类"),

    //NumberFormatException 字符串转换为数字异常
    NumberFormatException("0299025", "字符串转数字错误"),

    //IllegalAccessException 没有访问权限
    IllegalAccessException("0299026", "没有访问权限"),

    //SQLException 数据库操作异常
    DataAccessException("0299027", "数据操作异常"),

    //PSQLException 数据库异常
    SQLException("0299036", "数据库异常"),

    //EOFException 文件已结束异常
    EOFException("0299028", "文件已结束"),

    //SecturityException 违背安全原则异常
    SecurityException("0299029", "违背安全原则"),

    //NoSuchMethodException 方法未找到异常
    NoSuchMethodException("0299030", "方法未找到"),

    //InterruptedException	线程被中断异常
    InterruptedException("0299031", "线程被中断"),

    //IOException IO异常
    IOException("0299036", "IO异常"),

    //SQLException相关
    DB_Refuse_Connection("0103006", "数据库拒绝连接"),
    DB_Deadlock("0299015", "数据库死锁"),
    DB_Connect_Timeout("0299016", "数据库连接超时"),
    DB_Unique_Index_Repeat("0104009", "数据库唯一索引重复"),
    DB_Table_Field_NOT_EXIST("0104010", "数据库表字段不存在"),
    DB_Table_Field_Type_NOT_MATCH("0104011", "数据库表字段类型不匹配"),
    DB_Table_Field_LENGTH("0104012", "超出数据库表字段长度"),
    Invalid_Column_Name("0299032", "无效列名"),
    Table_Or_View_NOT_Exist("0299033", "表或视图不存在"),
    Can_NOT_Insert_NULL("0299034", "不能插入空值"),
    Invalid_Value("0299035", "无效数字"),

    //IOException相关
    Enter_Path_NOT_EXIST("0103007", "输入路径不存在"),
    Output_Path_NOT_EXIST("0103008", "输出路径不存在"),
    Disk_Space_NOT_ENOUGH("0299017", "磁盘空间不足"),

    Parameter_Enum_NOT_EXIST("0101002", "对应枚举不存在"),

    Data_Outside_Special_Range("0102004", "超出特定范围"),

    DB_Result_IS_NULL("0204013", "数据库查询结果为空"),

    Auth_TOKEN_ERROR("0105000", "令牌不正确或已过期"),
    Role_NOT_Power("0105014", "角色不具备该权限"),
    Auth_NOT_Validate("0105015", "认证用户名不存在"),
    Auth_Unknow_Error("0105016", "认证时发生未知错误，请联系管理员"),
    Auth_Password_Error("0105017", "账户密码错误"),
    Auth_Status_Error("0105018", "账户状态异常"),
    Auth_Role_Grade_Error("0105019", "当前角色无访问权限"),
    Auth_Match_Error("0105020", "认证信息匹配失败"),
    Auth_IP_VALID_HYPER_ERROR("0105021", "不允许使用超级管理员登录"),

    Connection_Pool_NOT_Resource("0203023", "连接池没有资源"),
    Parameter_Enum_NOT_Match("0299037", "枚举不匹配"),
    Account_NOT_Exist("0299038", "账号不存在"),
    Member_Exist_NOT_Delete("0299039", "该部门或其下级部门成员不为空，无法删除"),
    NOT_NULL("0299040", "不能为空"),
    Employee_Already_Exist("0299041", "员工已存在"),
    STATUS_ERROR("0299042", "状态异常"),
    UNKNOW_ERROR("0299999", "出现未知错误，请联系管理员"),
    NOT_Allow_Edit("0102043", "不允许编辑"),
    NOT_Obsolete("0102044", "该状态不能作废"),
    NO_NEED_TO_Recover("0102045", "该状态无需恢复"),
    Incorrect_Format("0102046", "格式不正确");

    private String code;

    private String message;

    StatusCode(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
