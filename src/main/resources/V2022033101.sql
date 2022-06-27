create schema if not exists finance collate utf8mb4_bin;
use finance;
CREATE TABLE if not exists `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '用户姓名',
    `account` varchar(50) NOT NULL COMMENT '登录账号',
    `password` varchar(255) DEFAULT NULL COMMENT '登陆密码',
    `role` enum('ADMIN', 'APPROVER', 'NORMAL', 'OFFICER') not null default 'NORMAL' comment '用户角色',
    `customer_number` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户编号',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='用户表';
replace into `user` (id, name, account, role, password)
values(1, '超级管理员', 'super_admin', 'ADMIN','$2a$10$YdOoLfvwipCxpCcs.yGv/ujEDs7OvWTjhXG16QSpH5k28U6o1BK0q');

CREATE TABLE if not exists `customer` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '客户编号',
    `business_user_id` bigint(20) not null default 1 comment '业务负责人',
    `name` varchar(255) NOT NULL COMMENT '客户名称',
    `industry_id` bigint(20) not null comment '所属行业',
    `category_id` bigint(20) not null comment '客户类别',
    `type` enum('RENT', 'PROXY', 'RENT_AND_PROXY') not null comment '客户类型：租用、代理、租用+代理',
    `enabled` bit not null default true comment '客户是否启用',
    `effect_time` datetime null comment '租赁生效时间',
    `expire_time` datetime null comment '租赁过期时间',
    `contact_name` varchar(50) not null default '' comment '联系人',
    `telephone` varchar(50) not null default '' comment '联系电话',
    `bank_account` varchar(50) not null default '' comment '银行账户',
    `bank_account_name` varchar(50) not null default '' comment '银行开户名称',
    `use_foreign_exchange` bit not null default false comment '是否使用外汇',
    `remark` varchar(500) comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
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
    `root_number` varchar(50) not null comment '根级别Number',
    `remark` varchar(500) comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='客户分类表';

CREATE TABLE if not exists `resource` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '功能编号',
    `name` varchar(255) NOT NULL COMMENT '功能名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `type` enum('MENU', 'PERMIT', 'DATA_SCOPE') not null default 'MENU' comment '类型：MENU,PERMIT,DATA_SCOPE',
    `url` varchar(255) not null default '' comment '访问链接',
    `icon` varchar(255) not null default '' comment '图标',
    `business_code` varchar(50) not null default '' comment '业务代码',
    `permit_code` varchar(500) not null default '' comment '权限代码',
    `sort_num` int(11) not null default 10 comment '排序编号升序',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='功能表';

CREATE TABLE if not exists `user_resource` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) not null comment '用户ID',
    `resource_id` bigint(20) NOT NULL COMMENT '功能ID',
    `permit_code` varchar(500) NOT NULL default '' COMMENT '功能操作权限，多个,分隔',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`user_id`, `resource_id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='用户的功能列表';

CREATE TABLE if not exists `customer_resource` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `resource_id` bigint(20) NOT NULL COMMENT '功能ID',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `resource_id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='客户的功能列表';

CREATE TABLE if not exists `industry` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `number` varchar(50) not null comment '行业编号',
    `name` varchar(255) NOT NULL COMMENT '行业名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `left_value` int(11) not null default 1 comment '节点左值',
    `right_value` int(11) not null default 2 comment '节点右值',
    `root_number` varchar(50) not null comment '根级别Number',
    `remark` varchar(500) comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='行业分类表';

CREATE TABLE if not exists `subject` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `industry_id` bigint(20) not null comment '所属行业',
    `number` varchar(50) not null comment '科目编号',
    `name` varchar(255) NOT NULL COMMENT '科目名称',
    `lending_direction` enum('BORROW', 'LOAN', 'DEFAULT') not null default 'DEFAULT' comment '科目方向,BORROW：借、LOAN：贷、NOTHING：借+贷',
    `type` enum('SUBJECT', 'COST', 'SUBJECT_AND_COST') not null default 'SUBJECT' comment '科目类型,SUBJECT:科目、COST:费用、SUBJECT_AND_COST:科目和结算',
    `assist_settlement` enum('NOTHING', 'SUPPLIER', 'CUSTOMER', 'EMPLOYEE', 'BANK') not null default 'NOTHING' comment '辅助结算，自己翻译',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `left_value` int(11) not null default 1 comment '节点左值',
    `right_value` int(11) not null default 2 comment '节点右值',
    `root_number` varchar(50) not null comment '根级别Number',
    `remark` varchar(500) comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `industry_id`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='科目表';

CREATE TABLE if not exists `expense_bill` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `customer_number` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户编号',
    `number` varchar(50) not null comment '报销单号',
    `expense_person` varchar(255) not null comment '报销人',
    `expense_time` datetime not null comment '报销日期',
    `position` varchar(50) not null comment '职位',
    `total_subsidy_amount` decimal(10, 5) not null comment '合计补助金额',
    `reason` varchar(500) not null comment '报销事由',
    `audit_status` enum('TO_BE_AUDITED', 'AUDITED') default 'TO_BE_AUDITED' not null comment '审核状态',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='费用报销单';

CREATE TABLE if not exists `expense_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `bill_id` bigint(20) not null comment '对应的报销单ID',
    `serial_number` int not null comment '序号',
    `subject_id` bigint(20) not null comment '费用名称对应的科目',
    `name` varchar(255) not null comment '费用名称',
    `begin_time` datetime not null comment '开始日期',
    `end_time` datetime not null comment '结束日期',
    `travel_place` varchar(255) not null comment '出差起讫地点',
    `summary` varchar(500) not null default '' comment '摘要',
    `num_of_bill` int not null null comment '票据张数',
    `bill_amount` decimal(10, 5) not null comment '票据金额',
    `actual_amount` decimal(10, 5) not null comment '实际金额',
    `subsidy_amount` decimal(10, 5) not null comment '补助费用金额',
    `subtotal_amount` decimal(10, 5) not null comment '小计费用金额',
    `remark` varchar(500) not null default '' comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='费用报销条目';

CREATE TABLE if not exists `expense_item_subsidy` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `bill_id` bigint(20) not null comment '对应的报销单ID',
    `item_id` bigint(20) not null comment '对应的费用报销条目ID',
    `subject_id` bigint(20) not null comment '补助费用名称对应的科目',
    `name` varchar(255) not null comment '补助费用名称',
    `days` int not null comment '天数',
    `amount_for_day` decimal (10, 5) not null comment '元/天',
    `amount` decimal(10, 5) not null comment '补助金额',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='费用报销条目详情(出差补助明细)';

CREATE TABLE if not exists `expense_item_attachment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `bill_id` bigint(20) not null comment '对应的报销单ID',
    `item_id` bigint(20) not null comment '对应的费用报销条目ID',
    `name` varchar(255) not null comment '票据名称',
    `url` varchar(500) not null comment '附件url',
    `remark` varchar(500) default '' comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='费用报销条目附件';


CREATE TABLE if not exists `currency` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `customer_number` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户编号',
    `number` varchar(50) not null comment '货币编号',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `name` varchar(255) not null comment '货币名称',
    `rate` decimal(10, 5) not null comment '汇率',
    `remark` varchar(500) default '' comment '备注',
    `create_by` bigint(20) not null default 1,
    `audit_status` enum('TO_BE_AUDITED', 'AUDITED') default 'TO_BE_AUDITED' not null comment '审核状态',
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (customer_id, `year_month_num`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='货币汇率列表';

CREATE TABLE if not exists `voucher` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `customer_number` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户编号',
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `currency_id` bigint(20) not null default 0 comment '原币ID',
    `currency_name` varchar(255) not null comment '原币名称',
    `rate` decimal(10, 5) not null comment '原币汇率',
    `unit` varchar(255) not null comment '单位',
    `serial_number` int(11) not null comment '凭证序号,每月凭证从1开始',
    `voucher_time` datetime not null comment '凭证日期',
    `attachment_num` int(11) not null default 0 comment '附件张数',
    `total_currency_amount` decimal(10, 5) not null comment '原币合计金额',
    `total_local_currency_amount` decimal(10, 5) not null comment '本币合计金额',
    `audit_status` enum('TO_BE_AUDITED', 'AUDITED') default 'TO_BE_AUDITED' not null comment '审核状态',
    `bookkeeping` bit not null default false comment '记账状态',
    `bookkeeping_by` bigint(20) not null default 0 comment '记账人',
    `bookkeeper_name` varchar(50) not null default '' comment '记账人',
    `audit_by` bigint(20) not null default 0 comment '审核人',
    `auditor_name` varchar(50) not null default '' comment '审核人',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='凭证';

CREATE TABLE if not exists `voucher_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `voucher_id` bigint(20) not null comment '所属凭证ID',
    `summary` varchar(500) not null default '' comment '摘要',
    `subject_id` bigint(20) not null comment '科目ID',
    `subject_number` varchar(50) not null comment '科目编号',
    `subject_name` varchar(255) not null comment '科目名称',
    `lending_direction` enum('BORROW', 'LOAN') not null comment '科目方向,BORROW：借、LOAN：贷、NOTHING：借+贷',
    `currency_id` bigint(20) not null default 0 comment '原币ID',
    `currency_name` varchar(255) not null comment '原币名称',
    `rate` decimal(10, 5) not null comment '原币汇率',
    `amount` decimal(10, 5) not null comment '借方金额',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='凭证项';

CREATE TABLE if not exists `sequence` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `use_category` varchar(50) not null default '' comment '使用类别',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='序列表';

truncate table resource;
replace into `resource` (id, number, name, parent_id, parent_number, has_leaf, level, type, url, icon, business_code,permit_code, sort_num, super_customer)values
(1, '1', '系统管理', 0, '', true, 1, 'MENU', '/system', 'icon-xitongguanli1', '', '', 40, false),
(2, '2', '用户管理', 1, '1', false, 2, 'MENU', '/system/user', '', '', '', 1000, false),
(6, '6', '客户授权管理', 1, '1', false, 2, 'MENU', '/system/customerGrantPermissionPage', '', '', '', 1000, true),
(50, '50', '基础数据管理', 0, '', true, 1, 'MENU', '/base', 'icon-jichushuju_icox', '', '', 30, false),
(51, '51', '行业管理', 50, '50', false, 2, 'MENU', '/base/industry', '', '', '', 300, true),
(60, '60', '科目管理', 50, '50', false, 2, 'MENU', '/base/subject', '', '', '', 400, false),
(70, '70', '客户分类管理', 50, '50', false, 2, 'MENU', '/base/customerCategory', '', '', '', 200, true),
(80, '80', '客户档案', 50, '50', false, 2, 'MENU', '/base/customer', '', 'customer', 'view:all', 100, true),
(100, '100', '费用报销管理', 0, '', false, 1, 'MENU', '/expense/bill', 'icon-baoxiaoshenqing-feiyongbaoxiaoshenqing-06', 'expenseBill', 'view:all,base,auditing,unAuditing,print', 10, false),
(120, '120', '记账管理', 0, '', true, 1, 'MENU', '/voucher', 'icon-ico_hushigongzuozhan_jizhangguanli', '', '', 20, false),
(121, '121', '凭证管理', 120, '120', false, 2, 'MENU', '/voucher/list', '', 'voucher', 'view:all,base,auditing,unAuditing,bookkeeping,unBookkeeping,print', 100, false),
(130, '130', '批量审核', 120, '120', false, 2, 'MENU', '/voucher/batchAuditing', '', 'voucher:batch', 'auditing,unAuditing', 300, false),
(140, '140', '批量记账', 120, '120', false, 2, 'MENU', '/voucher/batchBookkeeping', '', 'voucher:batch', 'bookkeeping,unBookkeeping', 400, false),
(150, '150', '科目账簿', 120, '120', false, 2, 'MENU', '/voucher/book', '', '', '', 200, false),
(160, '160', '汇率管理', 120, '120', false, 2, 'MENU', '/voucher/currency', '', 'currency', 'base,auditing,unAuditing', 500, false)
;

delete from `user_resource` where user_id = 1;
insert into `user_resource` (user_id, resource_id,permit_code)
select 1, f.id,f.permit_code from `resource` f;

delete from `customer_resource` where customer_id = 0;
insert into `customer_resource` (customer_id, resource_id)
select 0, f.id from `resource` f;

replace into customer (id,`number`,`name`,industry_id,category_id,type,use_foreign_exchange,business_user_id)
values(0, 'HX_TOP', '记账平台',0,0,'RENT_AND_PROXY', true, 1);