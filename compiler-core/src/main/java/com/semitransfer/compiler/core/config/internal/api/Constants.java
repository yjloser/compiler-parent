package com.semitransfer.compiler.core.config.internal.api;


/**
 * <p>
 * 封装常量
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public interface Constants {
    /**
     * 0
     */
    int NUM_ZERO = 0;
    /**
     * 1
     */
    int NUM_ONE = 1;

    /**
     * 2
     */
    int NUM_TWO = 2;

    /**
     * 3
     */
    int NUM_THREE = 3;

    /**
     * 4
     */
    int NUM_FOUR = 4;

    /**
     * 5
     */
    int NUM_FIVE = 5;
    /**
     * 6
     */
    int NUM_SIX = 6;

    /**
     * 7
     */
    int NUM_SEVEN = 7;

    /**
     * 8
     */
    int NUM_EIGHT = 8;

    /**
     * 9
     */
    int NUM_NINE = 9;

    /**
     * 10
     */
    int NUM_TEN = 10;


    /**
     * 20
     */
    int NUM_TWENTY = 20;


    /**
     * 30
     */
    int NUM_THIRTH = 30;


    /**
     * 40
     */
    int NUM_FORTH = 40;


    /**
     * 50
     */
    int NUM_FIFTY = 50;


    /**
     * 60
     */
    int NUM_SIXTY = 60;

    /**
     * 70
     */
    int NUM_SEVENTY = 70;

    /**
     * 80
     */
    int NUM_EIGHTY = 80;

    /**
     * 90
     */
    int NUM_NINETY = 90;

    /**
     * 100
     */
    int NUM_HUNDRED = 100;

    /**
     * 字符串0
     */
    String STR_ZERO = "0";

    /**
     * 统一入口
     */
    String UNIFY_ENTRANCE = "/unifyEntrance.do";


    /**
     * 统一入口 新增
     */
    String UNIFY_ENTRANCE_ADD = "/unifyEntrance/save.do";

    /**
     * 统一入口 删除
     */
    String UNIFY_ENTRANCE_REMOVE = "/unifyEntrance/remove.do";

    /**
     * 统一入口 更新
     */
    String UNIFY_ENTRANCE_UPDATE = "/unifyEntrance/update.do";

    /**
     * 统一入口 获取单个
     */
    String UNIFY_ENTRANCE_GET = "/unifyEntrance/get.do";


    /**
     * 统一入口 获取列表
     */
    String UNIFY_ENTRANCE_LIST = "/unifyEntrance/list.do";


    /**
     * 未命名
     */
    String UNKNOWN = "unknown";

    /**
     * 密钥模
     */
    String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    /**
     * 本地回环
     */
    String LOCALHOST = "0:0:0:0:0:0:0:1";

    /**
     * redis返回码
     **/
    String FIELD_FIREDIS_RETURN_CODE = "codes";

    /**
     * 手机端参数头
     **/
    String FIELD_PARA = "para";
    /**
     * 请求参数
     **/
    String FIELD_PARAMS = "params";

    /**
     * 密钥
     */
    String FIELD_KEY = "key";

    /**
     * 校验状态
     */
    String FIELD_CHECK_STATUS = "check_status";
    /**
     * 数据库类型
     */
    String FIELD_ORACLE = "oracle";

    /**
     * 当前页面
     */
    String FIELD_CURRENTPAGE = "current";

    /**
     * 返回码
     */
    String FIELD_CODE = "code";
    /**
     * 返回信息
     */
    String FIELD_MSG = "msg";

    /**
     * 数据库类型
     */
    String FIELD_DATABASE_TYPE = "databaseType";


    /**
     * 类型
     */
    String FIELD_TYPE = "type";

    /**
     * 备注
     */
    String FIELD_REMARK = "remark";

    /**
     * 实体
     */
    String FIELD_ITEM = "item";

    /**
     * 集合
     */
    String FIELD_ITEMS = "items";

    /**
     * 开始页面
     **/
    String FIELD_BIGENPAGE = "bigen";

    /**
     * 停止页面
     **/
    String FIELD_ENDPAGE = "end";
    /**
     * 每页条数
     */
    String FIELD_SIZE = "size";

    /**
     * 最大值
     */
    String FIELD_MAX = "max";

    /**
     * 最小值
     */
    String FIELD_MIN = "min";

    /**
     * 步长
     */
    String FIELD_STEP = "step";

    /**
     * 精度
     */
    String FIELD_PRECISION = "precision";

    /**
     * 当前页
     */
    String FIELD_CURRENT = "current";

    /**
     * 返回结果
     */
    String FIELD_RESULT = "result";

    /**
     * 解析
     */
    String FIELD_ANALYZES = "analyze";

    /**
     * 每页显示
     */
    String FIELD_PAGE_SIZE = "pageSize";

    /**
     * 主键
     */
    String FIELD_ID = "id";

    /**
     * 列表分页
     */
    String FIELD_LIST = "list";

    /**
     * 大写GET
     */
    String FIELD_GET_UPCASE = "GET";

    /**
     * 大写LIST
     */
    String FIELD_LIST_UPCASE = "LIST";


    /**
     * 大写ADD
     */
    String FIELD_ADD_UPCASE = "ADD";

    /**
     * 大写SAVE
     */
    String FIELD_SAVE_UPCASE = "SAVE";

    /**
     * 授权
     */
    String FIELD_AUTHORIZE = "authorize";

    /**
     * 导入
     */
    String FIELD_IMPORT = "import";

    /**
     * 导出
     */
    String FIELD_EXPORT = "export";

    /**
     * 下载
     */
    String FIELD_DOWNLOAD = "download";

    /**
     * get
     */
    String FIELD_GET = "get";

    /**
     * 用户名
     */
    String FIELD_USER_NAME = "username";


    /**
     * 新增
     */
    String FIELD_ADD = "add";


    /**
     * 首页
     */
    String FIELD_INDEX = "index";

    /**
     * 头部信息
     */
    String FIELD_X_TOKEN = "X-Token";

    /**
     * 过期时间
     */
    String FIELD_EXPIRES = "expires";


    /**
     * 更新
     */
    String FIELD_UPDATE = "update";

    /**
     * 删除
     */
    String FIELD_REMOVE = "remove";

    /**
     * 跳转
     */
    String FIELD_JUMP = "jump";

    /**
     * 登陸
     */
    String FIELD_SIGNIN = "signIn";

    /**
     * 退出
     */
    String FIELD_SIGNOUT = "signOut";

    /**
     * 删除
     */
    String FIELD_DELETE = "delete";

    /**
     * 個人信息
     */
    String FIELD_USER_INFO = "userInfo";

    /**
     * 授权
     */
    String FIELD_AUTHORIZATION = "Authorization";
    /**
     * 临时token
     */
    String FIELD_TOKEN = "token";

    String FIELD_USER = "user";
    String FIELD_ROLE = "role";
    String FIELD_DEPT = "dept";
    String FIELD_MENU = "menu";
    String FIELD_ACL = "acl";


    String FIELD_USERS = "users";
    String FIELD_ROLES = "roles";
    String FIELD_DEPTS = "depts";
    String FIELD_MENUS = "menus";
    String FIELD_ACLS = "acls";


    /**
     * 临时token
     */
    String FIELD_LOGIN_KEY = "loginKey";

    /**
     * 登录
     */
    String FIELD_LOGIN = "login";

    /**
     * 刷新
     */
    String FIELD_REFRESH = "refresh";

    /**
     * 邮箱
     */
    String FIELD_EMAIL = "email";

    /**
     * 修改密码
     */
    String FIELD_CHANGE_PASSWORD = "changePassword";

    /**
     * 修改个人信息
     */
    String FIELD_CHANGE_USER_INFO = "changeUserInfo";

    /**
     * 用户表示
     */
    String ISS_UER = "semitransfer";
    /**
     * 生成密钥
     */
    String DES_PASWD = "ACabN";

    /**
     * 验证邮箱密码
     */
    String CHECK_MODIFY = "checkModify";

    /**
     * 账户
     */
    String FIELD_ACCOUNT = "account";

    /**
     * 密码
     */
    String FIELD_PASSWORD = "password";

    /**
     * 项目名属性
     */
    String FIELD_APPLICATION_NAME = "spring.application.name";

    /**
     * 用户id
     */
    String FIELD_USERID = "userId";

    /**
     * ip最大值
     */
    int NUM_MAX_SIZE = 255;
    /**
     * 资源路径
     */
    String FIELD_LOCAL_PATH = "local.path";

    /**
     * 文件名
     */
    String FIELD_FILE_NAME = "fileName";
    /**
     * 本地路径
     */
    String FIELD_PATH = "path";
    /**
     * 标识
     */
    String FIELD_PATH_SIGN = "psign";
    /**
     * 资源域名
     */
    String FIELD_IMG_URL = "img.url";

    /**
     * 宋体
     */
    String FONT = "宋体";


    /**
     * 邮箱协议
     */
    String FIELD_MAIL_PROTOCOL = "mail.protocol";

    /**
     * 邮箱服务器
     */
    String FIELD_MAIL_HOST = "mail.host";

    /**
     * 发送人
     */
    String FIELD_MAIL_SENDER = "mail.sender";

    /**
     * 资源域名
     */
    String FIELD_MAIL_SENDER_NAME = "mail.senderName";

    /**
     * 授权码
     */
    String FIELD_MAIL_AUTH = "mail.auth";

    /**
     * 邮箱标题
     */
    String FIELD_MAIL_TITLE = "mail.title";


    /**
     * 邮箱内容
     */
    String FIELD_MAIL_CONTENT = "mail.content";

    /**
     * 名称
     */
    String FIELD_NAME = "name";

    /**
     * 是否显示在前端页面
     */
    String FIELD_SHOW = "show";

    /**
     * 是否自定义组件代码
     */
    String FIELD_COSTOM = "costom";

    /**
     * 预新增字段
     */
    String FIELD_PREADDED = "preAdded";

    /**
     * 检索标识
     */
    String FIELD_SEARCH = "search";

    /**
     * 列表
     */
    String FIELD_COLUMN = "column";

    /**
     * 自定义格式或js文本内容
     */
    String FIELD_FORMAT = "format";

    /**
     * 顺序
     */
    String FIELD_ORDER = "order";

    /**
     * 必填
     */
    String FIELD_REQUIRED = "required";

    /**
     * 正则
     */
    String FIELD_RULES = "rules";

    /**
     * 字段名称
     */
    String FIELD_LABLE = "label";

    /**
     * 外部组件
     */
    String FIELD_EXTERNAL = "external";

    /**
     * 列表
     */
    String ACL_LIST = "列表";

    /**
     * 新增
     */
    String ACL_SAVE = "新增";

    /**
     * 更新
     */
    String ACL_UPDATE = "更新";

    /**
     * 查询
     */
    String ACL_GET = "查询";

    /**
     * 删除
     */
    String ACL_REMOVE = "删除";

    /**
     * 变更状态
     */
    String ACL_CHANGE = "变更状态";

    /**
     * 下载
     */
    String ACL_DOWNLOAD = "下载";

    /**
     * 导入
     */
    String ACL_IMPORT = "导入";

    /**
     * 到处
     */
    String ACL_EXPORT = "导出";

    /**
     * 检索
     */
    String ACL_SEARCH = "检索";

    /**
     * 跳转
     */
    String ACL_JUMP = "跳转";

    /**
     * 通用
     */
    String FIELD_COM = "COM";

    /**
     * 检索
     */
    String FIELD_SEARCH_UPCASE = "SEARCH";

    /**
     * 全部
     */
    String FIELD_ALL = "ALL";

    /**
     * 组件
     */
    String FIELD_NODE = "node";

    /**
     * 数据库列
     */
    String TABLE_FIELD = "TableField";

    /**
     * 前端组件标识
     */
    String PAGE_FIELD = "PageField";


    /**
     * 占位文本
     */
    String FIELD_PLACEHOLDER = "placeholder";

    /**
     * 是否显示清除按钮
     */
    String FIELD_CLEARABLE = "clearable";

    /**
     * 是否禁用选择器
     */
    String FIELD_DISABLED = "disabled";

    /**
     * 是否显示多选框
     */
    String FIELD_SHOW_CHECKBOX = "show-checkbox";

    /**
     * 没有数据时的提示
     */
    String FIELD_EMPTY_TEXT = "empty-text";

    /**
     * 完全只读，开启后不会弹出选择器，只在没有设置 open 属性下生效
     */
    String FIELD_READONLY = "readonly";

    /**
     * 文本框是否可以输入，只在没有使用 slot 时有效
     */
    String FIELD_EDITABLE = "editable";

    /**
     * 是否检索
     */
    String FIELD_FILTERABLE = "filterable";

    /**
     * 组件内容
     */
    String FIELD_PROPS = "props";

    /**
     * 组件
     */
    String FIELD_NODE_CONFIG = "nodeConfig";

    /**
     * 组件数据信息
     */
    String FIELD_NODE_CHILD = "nodeChild";

    /**
     * 日期选择器出现的位置
     */
    String FIELD_PLACEMENT = "placement";

    /**
     * 开启后，可以选择多个日期
     */
    String FIELD_MULTIPLE = "multiple";

    /**
     * 文本类型
     */
    String FIELD_TEXTAREA = "textarea";

    /**
     * 值
     */
    String FIELD_VALUE = "value";


    /**
     * 检索信息
     */
    String SEARCH_DATA = "SEARCH-DATA";

    /**
     * 行
     */
    String FIELD_ROWS = "rows";


    /**
     * 自适应
     */
    String FIELD_AUTOSIZE = "autosize";

    /**
     * 原生的 wrap 属性，可选值为 hard 和 soft，仅在 textarea 下生效
     */
    String FIELD_WRAP = "wrap";
    /**
     * 星级
     */
    String FIELD_COUNT = "count";

    /**
     * 格式化
     */
    String FIELD_CHARACTER = "character";

    /**
     * 使用图标
     */
    String FIELD_ICON = "icon";
    /**
     * 使用自定义图标
     */
    String FIELD_CUSTOM_ICON = "custom-icon";

    /**
     * 是否显示提示文字
     */
    String FIELD_SHOW_TEXT = "show-text";

    /**
     * data数据
     */
    String FIELD_DATA = "data";

    /**
     * 接口地址
     */
    String FIELD_API = "api";

    /**
     * 模块
     */
    String FIELD_MODULE = "module";

    /**
     * 操作姓名
     */
    String FIELD_OPERATOR_NAME = "operatorName";

    /**
     * 操作
     */
    String FIELD_OPERATOR = "operator";

    /**
     * 操作ip
     */
    String FIELD_OPERATOR_IP = "operatorIp";

    /**
     * 操作时间
     */
    String FIELD_OPERATOR_TIME = "operateTime";

    /**
     * 联系人
     */
    String FIELD_CONTACTS_NAME = "contactsName";

    /**
     * 日志列表
     */
    String FIELD_LOGGER_LIST = "LOGGER_LIST";

    /**
     * 在显示复选框的情况下，是否严格的遵循父子不互相关联的做法
     */
    String FIELD_CHECK_STRICTLY = "check-strictly";

    /**
     * 下拉
     */
    String COMPONENT_SELECT = "Select";

    /**
     * 属性
     */
    String COMPONENT_TREE = "Tree";

    /**
     * 是否允许半选
     */
    String FIELD_ALLOW_HALF = "allow-half";

    /**
     * 级联
     */
    String COMPONENT_CASCADER = "Cascader";

    /**
     * 权限列表
     */
    String POWER = "POWER_LIST";

    /**
     * 验证码
     */
    String CODE = "CODE";

    /**
     * 菜单列表
     */
    String MENU = "MENU_LIST";


    /**
     * 权限code
     */
    String POWER_CODE = "aclCode";


    /**
     * 角色列表
     */
    String ROLE_ROLEARRAY_SELECT = "Role_RoleArray_Select";

    /**
     * 部门列表
     */
    String DEPT_DEPTARRAY_CASCADER = "Dept_DeptArray_Cascader";

    /**
     * 菜单树形
     */
    String MENU_TREE = "Menu_Tree";


    /**
     * 全局数据
     */
    String GLOBAL_DATA = "globalData";
}