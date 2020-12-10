create database if not exists `clever-security` default character set = utf8;
use `clever-security`;


/* ====================================================================================================================
    domain -- 数据域
==================================================================================================================== */
create table domain
(
    id                  bigint          not null                                                comment '域id(系统自动生成且不会变化)',
    name                varchar(127)    not null        unique                                  comment '域名称',
    redis_name_space    varchar(63)     not null        unique                                  comment 'Redis前缀',
    description         varchar(511)                                                            comment '说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '数据域';
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user -- 用户表
==================================================================================================================== */
create table user
(
    uid                 varchar(63)     not null                                                comment '用户id(系统自动生成且不会变化)',
    login_name          varchar(63)     not null        unique collate utf8_bin                 comment '用户登录名(允许修改)',
    password            varchar(127)                                                            comment '密码',
    telephone           varchar(31)                     unique                                  comment '手机号',
    email               varchar(63)                     unique collate utf8_bin                 comment '邮箱',
    expired_time        datetime(3)                                                             comment '帐号过期时间(空表示永不过期)',
    enabled             int(1)          not null        default 1                               comment '是否启用，0:禁用，1:启用',
    nickname            varchar(63)     not null                                                comment '用户昵称',
    avatar              varchar(511)                                                            comment '用户头像',
    register_channel    int(1)          not null        default 0                               comment '用户注册渠道，0:管理员，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序',
    from_source         int(1)          not null        default 0                               comment '用户来源，0:系统注册，1:外部导入(同步)',
    description         varchar(511)                                                            comment '说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (uid)
) comment = '用户表';
create index user_nickname on user (nickname);
/*------------------------------------------------------------------------------------------------------------------------
TODO user_ext 用户扩展表
--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    role -- 角色表
==================================================================================================================== */
create table role
(
    id                  bigint          not null                                                comment '角色id(系统自动生成且不会变化)',
    domain_id           bigint          not null                                                comment '域id',
    name                varchar(63)     not null                                                comment '角色名称',
    description         varchar(1023)                                                           comment '角色说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '角色表';
create index role_name on role (name);
create unique index role_domain_id_name on role (domain_id, name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    permission -- 权限表
==================================================================================================================== */
create table permission
(
    id                  bigint          not null                                                comment '权限id(系统自动生成且不会变化)',
    parent_id           bigint          not null        default -1                              comment '上级权限id',
    domain_id           bigint          not null                                                comment '域id',
    str_flag            varchar(255)    not null                                                comment '权限唯一字符串标识',
    title               varchar(255)    not null                                                comment '权限标题',
    resources_type      int(1)          not null        default 1                               comment '权限类型，1:API权限，2:菜单权限，3:UI组件权限',
    enable_auth         int(1)          not null        default 1                               comment '是否启用授权，0:不启用，1:启用',
    description         varchar(1203)                                                           comment '权限说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '权限表';
create index permission_str_flag on permission (str_flag);
create unique index permission_domain_id_str_flag on permission (domain_id, str_flag);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_domain -- 用户-域
==================================================================================================================== */
create table user_domain
(
    domain_id           bigint          not null                                                comment '域id',
    uid                 varchar(63)     not null                                                comment '用户id',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (domain_id, uid)
) comment = '用户-域';
create index user_domain_uid on user_domain (uid);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_role -- 用户-角色
==================================================================================================================== */
create table user_role
(
    uid                 varchar(63)     not null                                                comment '用户id',
    role_id             bigint          not null                                                comment '角色id',
    domain_id           bigint          not null                                                comment '域id',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (uid, role_id)
) comment = '用户-角色';
create index user_role_uid on user_role (uid);
create index user_role_role_id on user_role (role_id);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    role_permission -- 角色-权限
==================================================================================================================== */
create table role_permission
(
    role_id             bigint          not null                                                comment '角色id',
    permission_id       bigint          not null                                                comment '权限id',
    domain_id           bigint          not null                                                comment '域id',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (role_id, permission_id)
) comment = '角色-权限';
create index role_permission_role_id on role_permission (role_id);
create index role_permission_permission_id on role_permission (permission_id);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    api_permission -- API权限表
==================================================================================================================== */
create table api_permission
(
    id                  bigint          not null                                                comment 'API权限id(系统自动生成且不会变化)',
    permission_id       bigint          not null                                                comment '权限id',
    class_name          varchar(255)    not null        collate utf8_bin                        comment 'controller类名称',
    method_name         varchar(255)    not null        collate utf8_bin                        comment 'controller类的方法名称',
    method_params       varchar(255)    not null        collate utf8_bin                        comment 'controller类的方法参数签名',
    api_path            varchar(255)    not null                                                comment 'API接口地址(只用作显示使用)',
    api_exist           int(1)          not null        default 1                               comment 'API接口是否存在，0：不存在；1：存在',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = 'API权限表(permission子表)';
create index api_permission_permission_id on api_permission (permission_id);
create index api_permission_class_name on api_permission (class_name);
create index api_permission_method_name on api_permission (method_name);
create index api_permission_method_params on api_permission (method_params);
create index api_permission_api_path on api_permission (api_path);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    menu_permission -- 菜单权限表
==================================================================================================================== */
create table menu_permission
(
    id                  bigint          not null                                                comment '菜单权限id(系统自动生成且不会变化)',
    permission_id       bigint          not null                                                comment '权限id',
    path                varchar(255)    not null        collate utf8_bin                        comment '菜单路径',
    name                varchar(63)     not null                                                comment '菜单名称',
    icon                varchar(63)                                                             comment '菜单图标',
    hide_mode           int(1)          not null        default 0                               comment '菜单隐藏模式，0：不隐藏；1：隐藏当前菜单和子菜单，2:隐藏子菜单',
    ext_config          text                                                                    comment '菜单扩展配置',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '菜单权限表(permission子表)';
create index menu_permission_permission_id on menu_permission (permission_id);
create index menu_permission_path on menu_permission (path);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    ui_permission -- UI组件权限表
==================================================================================================================== */
create table ui_permission
(
    id                  bigint          not null                                                comment 'ui组件权限id(系统自动生成且不会变化)',
    permission_id       bigint          not null                                                comment '权限id',
    ui_name             varchar(255)    not null                                                comment '页面UI组件名称',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = 'UI组件权限表(permission子表)';
create index ui_permission_permission_id on ui_permission (permission_id);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    jwt_token -- JWT-Token表
==================================================================================================================== */
create table jwt_token
(
    id                          bigint          not null                                        comment 'JWT-Token id(系统自动生成且不会变化)',
    domain_id                   bigint          not null                                        comment '域id',
    uid                         varchar(63)     not null                                        comment '用户id',
    token                       varchar(1023)   not null                                        comment 'token数据',
    expired_time                datetime(3)                                                     comment 'JWT-Token过期时间(空表示永不过期)',
    disable                     int(1)          not null        default 0                       comment 'JWT-Token是否禁用，0:未禁用；1:已禁用',
    disable_reason              varchar(127)                                                    comment 'JWT-Token禁用原因',
    refresh_token               varchar(127)                                                    comment '刷新Token',
    refresh_token_expired_time  datetime(3)                                                     comment '刷新Token过期时间',
    refresh_token_state         int(1)                                                          comment '刷新Token状态，0:无效(已使用)；1:有效(未使用)',
    refresh_token_use_time      datetime(3)                                                     comment '刷新Token使用时间',
    refresh_create_token_id     bigint                                                          comment '刷新token创建的JWT-Token id',
    create_at                   datetime(3)     not null        default current_timestamp(3)    comment '创建时间',
    update_at                   datetime(3)                     on update current_timestamp(3)  comment '更新时间',
    primary key (id)
) comment = 'JWT-Token表';
create index jwt_token_uid on jwt_token (uid);
create index jwt_token_expired_time on jwt_token (expired_time);
create index jwt_token_refresh_token on jwt_token (refresh_token);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    validate_code -- 验证码
==================================================================================================================== */
create table validate_code
(
    id                  bigint          not null                                                comment '验证码 id(系统自动生成且不会变化)',
    domain_id           bigint          not null                                                comment '域id',
    uid                 varchar(63)     not null                                                comment '用户id',
    code                varchar(15)     not null                                                comment '验证码',
    type                int(1)          not null        default 1                               comment '验证码类型，1:登录验证码，2:找回密码验证码，3:重置密码(修改密码)验证码',
    expired_time        datetime(3)     not null                                                comment '验证码过期时间',
    validate_time       datetime(3)                                                             comment '验证码验证时间(使用时间)',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '验证码';
create index validate_code_uid on validate_code (uid);
create index validate_code_code on validate_code (code);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    scan_code_login -- 扫码登录表
==================================================================================================================== */
create table scan_code_login
(
    id                      bigint          not null                                            comment 'scan code id(系统自动生成且不会变化)',
    domain_id               bigint          not null                                            comment '域id',
    scan_code               varchar(63)     not null        unique                              comment '扫描二维码',
    scan_code_state         int(1)          not null        default 0                           comment '扫描二维码状态，0:已创建(待扫描)，1:已扫描(待确认)，2:已确认(待登录)，3:登录成功，4:已失效',
    expired_time            datetime(3)     not null                                            comment '扫描二维码过期时间',
    bind_token              varchar(1023)                                                       comment '绑定的JWT-Token数据',
    bind_token_time         datetime(3)                                                         comment '(扫描时间)绑定JWT-Token时间',
    confirm_expired_time    datetime(3)                                                         comment '确认登录过期时间',
    confirm_time            datetime(3)                                                         comment '确认登录时间',
    get_token_expired_time  datetime(3)                                                         comment '获取登录JWT-Token过期时间',
    login_time              datetime(3)                                                         comment '登录时间',
    token_id                bigint                                                              comment '登录生成的JWT-Token id',
    invalid_reason          varchar(63)                                                         comment '扫描二维码失效原因',
    create_at               datetime(3)     not null        default current_timestamp(3)        comment '创建时间',
    update_at               datetime(3)                     on update current_timestamp(3)      comment '更新时间',
    primary key (id)
) comment = '扫码登录表';
create index scan_code_login_expired_time on scan_code_login (expired_time);
create index scan_code_login_token_id on scan_code_login (token_id);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_login_log -- 用户登录日志
==================================================================================================================== */
create table user_login_log
(
    id                  bigint          not null        auto_increment                          comment '登录日志id(系统自动生成且不会变化)',
    domain_id           bigint          not null                                                comment '域id',
    uid                 varchar(63)     not null                                                comment '用户id',
    login_time          datetime(3)     not null                                                comment '登录时间',
    login_ip            varchar(63)     not null                                                comment '登录IP',
    login_channel       int(1)                                                                  comment '登录渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序',
    login_type          int(1)          not null                                                comment '登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录',
    login_state         int(1)          not null        default 0                               comment '登录状态，0:登录失败，1:登录成功',
    request_data        varchar(1023)   not null                                                comment '登录请求数据',
    jwt_token_id        bigint                                                                  comment 'JWT-Token id',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '用户登录日志';
create index user_login_log_uid on user_login_log (uid);
create index user_login_login_time on user_login_log (login_time);
create index user_login_jwt_token_id on user_login_log (jwt_token_id);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/



