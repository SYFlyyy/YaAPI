-- 创建库
create database if not exists yaapi;

-- 切换库
use yaapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  text                                   null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    accessKey    varchar(512)                           null comment 'accessKey',
    secretKey    varchar(512)                           null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- 接口信息表
create table if not exists yaapi.`interface_info`
(
    `id`                   bigint                             not null auto_increment comment '主键' primary key,
    `name`                 varchar(256)                       not null comment '接口名称',
    `description`          varchar(256)                       null comment '描述',
    `host`                 varchar(512)                       null comment '主机名',
    `url`                  varchar(512)                       not null comment '接口地址',
    `method`               varchar(256)                       not null comment '请求类型',
    `requestParams`        text                               null comment '请求参数',
    `requestParamsRemark`  text                               null comment '请求参数说明',
    `responseParamsRemark` text                               null comment '响应参数说明',
    `requestHeader`        text                               null comment '请求头',
    `responseHeader`       text                               null comment '响应头',
    `status`               int      default 0                 not null comment '接口状态（0-关闭，1-开启）',
    `userId`               bigint                             not null comment '创建人',
    `createTime`           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`           datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`             tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';

-- 用户调用接口关系表
create table if not exists yaapi.`user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '调用用户 id',
    `interfaceInfoId` bigint                             not null comment '接口 id',
    `totalNum`        int      default 0                 not null comment '总调用次数',
    `leftNum`         int      default 0                 not null comment '剩余调用次数',
    `status`          int      default 0                 not null comment '0-正常，1-禁用',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';