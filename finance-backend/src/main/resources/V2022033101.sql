create schema if not exists finance collate utf8mb4_bin;

CREATE TABLE if not exists `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '用户姓名',
    `account` varchar(50) NOT NULL COMMENT '登录账号',
    `password` varchar(255) DEFAULT NULL COMMENT '登陆密码',
    `role` enum('ADMIN', 'APPROVER', 'NORMAL', 'OFFICER') not null default 'NORMAL' comment '用户角色',
    `customer_account` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户账户',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='用户表';
replace into `user` (id, name, account, password)
values(1, '超级管理员', 'super_admin', '$2a$10$YdOoLfvwipCxpCcs.yGv/ujEDs7OvWTjhXG16QSpH5k28U6o1BK0q');

CREATE TABLE if not exists `customer` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `account` varchar(50) not null comment '客户账户',
    `user_account` varchar(50) not null comment '客户账户',
    `name` varchar(255) NOT NULL COMMENT '客户名称',
    `industry_id` bigint(20) not null comment '所属行业',
    `category_id` bigint(20) not null comment '客户类别',
    `type` enum('RENT', 'PROXY', 'RENT_AND_PROXY') not null comment '客户类型：租用、代理、租用+代理',
    `enabled` bit not null default true comment '客户是否启用',
    `status` enum('INITIALIZING', 'SUCCESS') not null default 'INITIALIZING' comment '客户状态',
    `effect_time` datetime not null comment '租赁生效时间',
    `expire_time` datetime not null comment '租赁过期时间',
    `contact_name` varchar(50) not null comment '联系人',
    `telephone` varchar(50) not null comment '联系电话',
    `bank_account` varchar(50) not null comment '银行账户',
    `bank_account_name` varchar(50) not null comment '银行开户名称',
    `use_foreign_exchange` bit not null default false comment '是否使用外汇',
    `remark` varchar(500) comment '备注',
    `table_identified` varchar(50) not null comment '客户的表标识',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='客户表';

CREATE TABLE if not exists `customer_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '客户编号',
    `name` varchar(255) NOT NULL COMMENT '客户名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `left_value` int(11) not null default 1 comment '节点左值',
    `right_value` int(11) not null default 2 comment '节点右值',
    `root_id` int(11) not null comment '根级别ID',
    `remark` varchar(500) comment '备注',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='客户分类表';

CREATE TABLE if not exists `function` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '功能编号',
    `name` varchar(255) NOT NULL COMMENT '功能名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `type` enum('MENU', 'BUTTON') not null default 'MENU' comment '功能类型：MENU,BUTTON',
    `url` varchar(255) not null default '' comment '访问链接',
    `icon` varchar(255) not null default '' comment '图标',
    `permit_code` varchar(255) not null default '' comment '权限代码',
    `sort_num` int(11) not null default 10 comment '排序编号升序',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='功能表';

CREATE TABLE if not exists `user_function` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) not null comment '用户ID',
    `function_id` varchar(255) NOT NULL COMMENT '功能ID',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`user_id`, `function_id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='用户的功能列表';

CREATE TABLE if not exists `customer_function` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `function_id` bigint(20) NOT NULL COMMENT '功能ID',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `function_id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='客户的功能列表';

CREATE TABLE if not exists `industry` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '行业编号',
    `name` varchar(255) NOT NULL COMMENT '行业名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `left_value` int(11) not null default 1 comment '节点左值',
    `right_value` int(11) not null default 2 comment '节点右值',
    `root_id` int(11) not null comment '根级别ID',
    `remark` varchar(500) comment '备注',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='行业分类表';

CREATE TABLE if not exists `subject` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `industry_id` bigint(20) not null comment '所属行业',
    `number` varchar(50) not null comment '科目编号',
    `name` varchar(255) NOT NULL COMMENT '科目名称',
    `direction` enum('BORROW', 'LOAN', 'NOTHING') not null default 'NOTHING' comment '科目方向,BORROW：借、LOAN：贷、NOTHING：无',
    `type` enum('SUBJECT', 'COST', 'SUBJECT_AND_COST') not null default 'SUBJECT' comment '科目类型,SUBJECT:科目、COST:费用、SUBJECT_AND_COST:科目和结算',
    `assist_settlement` enum('NOTHING', 'SUPPLIER', 'CUSTOMER', 'EMPLOYEE', 'BANK') not null default 'NOTHING' comment '辅助结算，自己翻译',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `left_value` int(11) not null default 1 comment '节点左值',
    `right_value` int(11) not null default 2 comment '节点右值',
    `root_id` int(11) not null comment '根级别ID',
    `remark` varchar(500) comment '备注',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`industry_id`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='科目表';

CREATE TABLE if not exists `sequence` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `use_category` varchar(50) not null default '' comment '使用类别',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='序列表';

replace into `function` (id, number, name, parent_id, parent_number, has_leaf, level, type, url, icon, permit_code)values
(1, '01', '系统管理', 0, '', true, 1, 'MENU', '/system', '', ''),
(2, '0101', '用户管理', 0, '01', false, 1, 'MENU', '/system/user', '', 'system:user'),
-- (3, '0102', '用户权限管理', 0, '01', true, 1, 'MENU', '/system/grantUserPermission', '', 'system:user'),
(4, '0103', '客户分类管理', 0, '01', false, 1, 'MENU', '/system/customerCategory', '', 'system:customerCategory'),
(5, '0104', '客户档案', 0, '01', false, 1, 'MENU', '/system/customer', '', 'system:customer'),
(6, '0105', '客户授权管理', 0, '01', false, 1, 'MENU', '/system/customerGrantPermissionPage', '', 'system:customer:grantPermissionPage'),
(20, '02', '基础数据管理', 0, '', true, 1, 'MENU', '/base', '', ''),
(21, '0201', '行业管理', 0, '02', false, 1, 'MENU', '/base/industry', '', 'base:industry'),
(22, '0202', '科目管理', 0, '02', false, 1, 'MENU', '/base/subject', '', 'base:subject');

delete from `user_function` where user_id = 1;
insert into `user_function` (user_id, function_id)
select 1, f.id from `function` f;

delete from `customer_function` where customer_id = 0;
insert into `customer_function` (customer_id, function_id)
select 0, f.id from `function` f;