
-- create database `clever-security`;
-- use `clever-security`;


/* ====================================================================================================================
    service_sys -- 服务系统
==================================================================================================================== */
create table service_sys
(
    id                  bigint          not null        auto_increment                          comment '主键id',
    sys_name            varchar(127)    not null                                                comment '系统(或服务)名称',
    redis_name_space    varchar(127)    not null                                                comment '全局的Session Redis前缀',
    description         varchar(511)                                                            comment '说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '服务系统';
create index service_sys_sys_name on service_sys (sys_name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user -- 用户表
==================================================================================================================== */
create table user
(
    id              bigint          not null        auto_increment                          comment '主键id',
    username        varchar(63)     not null        unique                                  comment '登录名',
    password        varchar(127)                                                            comment '密码',
    user_type       int(1)          not null        default 0                               comment '用户类型，0：系统内建，1：外部系统用户',
    telephone       varchar(31)                     unique                                  comment '手机号',
    email           varchar(63)                                                             comment '邮箱',
    expired_time    datetime(3)                                                             comment '帐号过期时间',
    locked          int(1)          not null        default 0                               comment '帐号是否锁定，0：未锁定；1：锁定',
    enabled         int(1)          not null        default 1                               comment '是否启用，0：禁用；1：启用',
    description     varchar(511)                                                            comment '说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '用户表';
create index user_username on user (username);
create index user_telephone on user (telephone);
create index user_email on user (email);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    role -- 角色表
==================================================================================================================== */
create table role
(
    id              bigint          not null        auto_increment                          comment '主键id',
    name            varchar(63)     not null        unique                                  comment '角色名称',
    description     varchar(1023)                                                           comment '角色说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '角色表';
create index role_name on role (name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    permission -- 权限表
==================================================================================================================== */
create table permission
(
    id              bigint          not null        auto_increment                          comment '主键id',
    sys_name        varchar(127)    not null                                                comment '系统(或服务)名称',
    title           varchar(255)    not null                                                comment '权限标题',
    permission_str  varchar(255)    not null        unique                                  comment '唯一权限标识字符串',
    resources_type  int(1)          not null        default 1                               comment '权限类型，1:web资源权限, 2:菜单权限，3:ui权限，......',
    description     varchar(1203)                                                           comment '权限说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '权限表';
create index permission_sys_name on permission (sys_name);
create index permission_permission_str on permission (permission_str);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    web_permission -- web权限表
==================================================================================================================== */
create table web_permission
(
    id                          bigint              not null    auto_increment                  comment '主键id',
    permission_str              varchar(255)        not null    unique                          comment '权限标识字符串',
    target_class                varchar(255)        not null                                    comment 'controller类名称',
    target_method               varchar(255)        not null                                    comment 'controller类的方法名称',
    target_method_params        varchar(255)        not null                                    comment 'controller类的方法参数签名',
    resources_url               varchar(255)        not null                                    comment '资源url地址(只用作显示使用)',
    need_authorization          int(1)              not null    default 1                       comment '需要授权才允许访问，1：需要；2：不需要',
    target_exist                int(1)              not null    default 1                       comment 'controller路由资源是否存在，0：不存在；1：存在',
    create_at                   datetime(3)         not null    default current_timestamp(3)    comment '创建时间',
    update_at                   datetime(3)                     on update current_timestamp(3)  comment '更新时间',
    primary key (id)
) comment = 'web权限表(permission子表)';
create index web_permission_permission_str on web_permission (permission_str);
create index web_permission_target_class on web_permission (target_class);
create index web_permission_target_method on web_permission (target_method);
create index web_permission_target_method_params on web_permission (target_method_params);
create index web_permission_resources_url on web_permission (resources_url);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_role -- 用户-角色
==================================================================================================================== */
create table user_role
(
    username    varchar(63)     not null                                        comment '登录名',
    role_name   varchar(63)     not null                                        comment '角色名称',
    create_at   datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at   datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (username, role_name)
) comment = '用户-角色';
create index user_role_username on user_role (username);
create index user_role_role_name on user_role (role_name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    role_permission -- 角色-权限
==================================================================================================================== */
create table role_permission
(
    role_name       varchar(63)     not null                                        comment '角色名称',
    permission_str  varchar(255)    not null                                        comment '资源访问所需要的权限标识字符串',
    create_at       datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at       datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (role_name, permission_str)
) comment = '角色-权限';
create index role_permission_role_name on role_permission (role_name);
create index role_permission_permission_str on role_permission (permission_str);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_module -- 用户-系统
==================================================================================================================== */
create table user_sys
(
    username        varchar(63)     not null                                        comment '登录名',
    sys_name        varchar(127)    not null                                        comment '系统(或服务)名称',
    create_at       datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at       datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (username, sys_name)
) comment = '用户-系统';
create index user_sys_username on user_sys (username);
create index user_sys_sys_name on user_sys (sys_name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    remember_me_token -- “记住我”功能的token
==================================================================================================================== */
create table remember_me_token (
    id              bigint          not null    auto_increment                      comment '主键id',
    sys_name        varchar(127)    not null                                        comment '系统(或服务)名称',
    series          varchar(63)     not null    unique                              comment 'token序列号',
    username        varchar(63)     not null                                        comment '用户登录名',
    token           varchar(63)     not null                                        comment 'token数据',
    last_used       timestamp       not null                                        comment '最后使用时间',
    create_at       datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at       datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (id)
) comment = '“记住我”功能的token';
create index remember_me_token_username on remember_me_token (username);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_login_log -- 用户登录日志
==================================================================================================================== */
create table user_login_log (
    id                  bigint          not null    auto_increment                      comment '主键id',
    sys_name            varchar(127)    not null                                        comment '系统(或服务)名称',
    username            varchar(63)     not null                                        comment '用户登录名',
    login_time          datetime(3)     not null                                        comment '登录时间',
    login_ip            varchar(63)     not null                                        comment '登录IP',
    authentication_info text            not null                                        comment '登录的用户信息',
    session_id          varchar(63)     not null    unique                              comment '登录SessionID',
    login_state         int(1)          not null    default 0                           comment '登录状态，0：未知；1：已登录；2：登录已过期',
    create_at           datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at           datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (id)
) comment = '用户登录日志';
create index user_login_log_username on user_login_log (username);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/





/*------------------------------------------------------------------------------------------------------------------------
TODO
1. 菜单表(权限子表)
2. 菜单-权限 关联表
--------------------------------------------------------------------------------------------------------------------------*/

















