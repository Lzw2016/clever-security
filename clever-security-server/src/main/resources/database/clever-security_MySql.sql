
-- create database `clever-security`;

/* ====================================================================================================================
    user -- 用户表
==================================================================================================================== */
create table user
(
    id              bigint          not null        auto_increment                          comment '主键id',
    username        varchar(63)     not null        unique                                  comment '登录名',
    password        varchar(127)                                                            comment '密码',
    user_type       int(1)          not null        default 0                               comment '用户类型，0：系统内建，1：外部系统用户',
    telephone       varchar(31)                                                             comment '手机号',
    email           varchar(63)                                                             comment '邮箱',
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
    controller_class            varchar(255)        not null                                    comment 'controller类名称',
    controller_method           varchar(255)        not null                                    comment 'controller类的方法名称',
    controller_method_params    varchar(255)        not null                                    comment 'controller类的方法参数签名',
    resources_url               varchar(255)        not null                                    comment '资源url地址(只用作显示使用)',
    need_authorization          int(1)              not null    default 1                       comment '需要授权才允许访问，1：需要；2：不需要',
    controller_exist            int(1)              not null    default 1                       comment 'controller路由资源是否存在，0：不存在；1：存在',
    create_at                   datetime(3)         not null    default current_timestamp(3)    comment '创建时间',
    update_at                   datetime(3)                     on update current_timestamp(3)  comment '更新时间',
    primary key (id)
) comment = 'web权限表(permission子表)';
create index web_permission_permission_str on web_permission (permission_str);
create index web_permission_controller_class on web_permission (controller_class);
create index web_permission_controller_method on web_permission (controller_method);
create index web_permission_controller_method_params on web_permission (controller_method_params);
create index web_permission_controller_resources_url on web_permission (resources_url);
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
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    user_module -- 用户-系统
==================================================================================================================== */
create table user_sys
(
    username        varchar(64)     not null                                        comment '登录名',
    sys_name        varchar(128)    not null                                        comment '系统(或服务)名称',
    create_at       datetime(3)     not null    default current_timestamp(3)        comment '创建时间',
    update_at       datetime(3)                 on update current_timestamp(3)      comment '更新时间',
    primary key (username, sys_name)
) comment = '用户-系统';
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/*------------------------------------------------------------------------------------------------------------------------
todo
1. 菜单-权限 关联表
2. ui-权限 关联表
--------------------------------------------------------------------------------------------------------------------------*/

















