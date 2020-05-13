package com.open.web.constant;

/**
 * @Description
 * @auther 程佳伟
 * @create 2019-10-13 02:36
 */
public class ResponseCode {

    /**
     * 操作成功
     */
    public static final String OK = "0000";
    /**
     * 请求参数有错误
     */
    public static final String BAD_REQUEST = "1000";
    /**
     * 未授权/未登录
     */
    public static final String UNAUTHORIZED = "2000";
    /**
     * 未登录
     */
    public static final String NOT_LOGIN = "3000";
    /**
     * 未找到用户信息
     */
    public static final String USER_NOT_EXIST = "3001";
    /**
     * 该用户已经被注册
     */
    public static final String USERNAME_OCCUPIED = "3002";
    /**
     * 资源未找到/不存在
     */
    public static final String NOT_FOUND = "4000";
    /**
     * 上传资源已存在
     */
    public static final String DUPLICATE_FILE = "4001";
    /**
     * 服务器出错
     */
    public static final String SERVER_ERROR = "5000";
    /**
     * 数据库错误-查询失败
     */
    public static final String QUERY_FAILED = "6000";
    /**
     * 数据库错误-更新失败
     */
    public static final String UPDATE_FAILED = "6001";
    /**
     * 数据库错误-删除失败
     */
    public static final String DELETE_FAILED = "6002";
    /**
     * 数据库错误-添加失败
     */
    public static final String ADD_FAILED = "6003";
    /**
     * 网络问题-请求超时
     */
    public static final String REQUEST_TIMEOUT = "7000";
}
