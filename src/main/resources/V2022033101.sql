create schema if not exists finance collate utf8mb4_bin;
use finance;
CREATE TABLE if not exists `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '用户姓名',
    `account` varchar(50) NOT NULL COMMENT '登录账号',
    `password` varchar(255) DEFAULT NULL COMMENT '登陆密码',
    `role` enum('ADMIN', 'NORMAL', 'ADVANCED_APPROVER', 'NORMAL_APPROVER') not null default 'NORMAL' comment '用户角色',
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
    `accounting_system` int(2) not null default 1 comment '会计制度，值的含义参考枚举类',
    `category_id` bigint(20) not null comment '客户类别',
    `type` enum('RENT', 'PROXY', 'RENT_AND_PROXY') not null comment '客户类型：租用、代理、租用+代理',
    `enabled` bit not null default true comment '客户是否启用',
    `enable_period` int(11) not null default 0 comment '启用期间yyyyMM',
    `current_period` int(11) not null default 0 comment '当前期间yyyyMM',
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
    `module` int(2) not null default 1,
    `permit_code` varchar(500) not null default '' comment '权限代码',
    `sort_num` int(11) not null default 10 comment '排序编号升序',
    `super_customer` bit(1) not null default false,
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

CREATE TABLE if not exists `subject` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `number` varchar(50) not null comment '科目编号',
    `name` varchar(255) NOT NULL COMMENT '科目名称',
    `lending_direction` enum('BORROW', 'LOAN') not null default 'BORROW' comment '科目方向,BORROW：借、LOAN：贷',
    `category` varchar(20) not null default 'ASSETS' comment '科目类别',
    `assist_settlement` enum('NOTHING', 'SUPPLIER', 'CUSTOMER', 'EMPLOYEE', 'BANK') not null default 'NOTHING' comment '辅助结算，自己翻译',
    `be_cash_flow` bit(1) not null default false comment '现金流量科目',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `root_id` bigint(20) not null default 0,
    `parent_number` varchar(50) not null default '0' comment '父级编号',
    `path` varchar(255) not null default '',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null default 1 comment '节点深度',
    `root_number` varchar(50) not null comment '根级别Number',
    `beginning_balance` decimal(12, 5) not null default 0 comment '年初余额',
    `opening_balance` decimal(12, 5) not null default 0 comment '期初余额(本年借方-本年贷方+年初余额)',
    `debit_annual_amount` decimal(12, 5) not null default 0 comment '本年累计借方金额',
    `credit_annual_amount` decimal(12, 5) not null default 0 comment '本年累计贷方金额',
    `remark` varchar(500) comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `number`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='科目表';

CREATE TABLE if not exists `expense_bill` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `customer_number` varchar(50) NOT NULL default 'HX_TOP' COMMENT '客户编号',
    `number` varchar(50) not null comment '报销单号',
    `expense_person` varchar(255) not null comment '报销人',
    `expense_time` datetime not null comment '报销日期',
    `position` varchar(50) not null comment '职位',
    `total_num_of_bill` decimal(12, 5) not null default 0 comment '总票据张数',
    `total_bill_amount` decimal(12, 5) not null default 0 comment '总票据金额',
    `total_actual_amount` decimal(12, 5) not null default 0 comment '总实际金额',
    `total_subtotal_amount` decimal(12, 5) not null default 0 comment '总小计金额',
    `total_subsidy_amount` decimal(12, 5) not null default 0 comment '合计补助金额',
    `reason` varchar(500) not null comment '报销事由',
    `audit_status` enum('TO_BE_AUDITED', 'AUDITED', 'APPROVED') default 'TO_BE_AUDITED' not null comment '审核状态',
    `approval_flow_instance_id` bigint(20) not null default 0 comment '流程审批ID，0表示还未开启审批流程',
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
    `serial_number` int not null comment '序号',
    `customer_id` bigint(20) not null comment '客户ID',
    `bill_id` bigint(20) not null comment '对应的报销单ID',
    `subject_id` bigint(20) not null comment '费用名称对应的科目',
    `subject_number` varchar(50) not null comment '科目编号',
    `begin_time` datetime not null comment '开始日期',
    `end_time` datetime not null comment '结束日期',
    `travel_place` varchar(255) not null comment '出差起讫地点',
    `summary` varchar(500) not null default '' comment '摘要',
    `num_of_bill` int not null default 0 comment '票据张数',
    `bill_amount` decimal(12, 5) not null default 0 comment '票据金额',
    `actual_amount` decimal(12, 5) not null default 0 comment '实际金额',
    `subsidy_amount` decimal(12, 5) not null default 0 comment '补助费用金额',
    `subtotal_amount` decimal(12, 5) not null default 0 comment '小计费用金额',
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
    `serial_number` int not null comment '序号',
    `customer_id` bigint(20) not null comment '客户ID',
    `bill_id` bigint(20) not null comment '对应的报销单ID',
    `item_id` bigint(20) not null comment '对应的费用报销条目ID',
    `subject_id` bigint(20) not null comment '补助费用名称对应的科目',
    `subject_number` varchar(50) not null comment '科目编号',
    `days` int not null comment '天数',
    `amount_for_day` decimal (10, 5) not null comment '元/天',
    `amount` decimal(12, 5) not null default 0 comment '补助金额',
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
    `serial_number` int not null comment '序号',
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
    `rate` decimal(12, 5) not null comment '汇率',
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
    `source` enum('MANUAL', 'EXPENSE_BILL') not null default 'MANUAL' comment '凭证来源，MANUAL：手工，EXPENSE_BILL：费用报销单转',
    `expense_bill_id` bigint(20) not null default 0 comment 'source=EXPENSE_BILL时>0',
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `serial_number` int(11) not null comment '凭证序号,每月凭证从1开始',
    `voucher_date` date not null comment '凭证日期',
    `attachment_num` int(11) not null default 0 comment '附件张数',
    `total_debit_amount` decimal(12, 5) not null default 0 comment '借方原币合计金额',
    `total_credit_amount` decimal(12, 5) not null default 0 comment '贷方原币合计金额',
    `total_local_debit_amount` decimal(12, 5) not null default 0 comment '借方本币合计金额',
    `total_local_credit_amount` decimal(12, 5) not null default 0 comment '贷方本币合计金额',
    `audit_status` enum('TO_BE_AUDITED', 'AUDITED') default 'TO_BE_AUDITED' not null comment '审核状态',
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
    `serial_number` int not null comment '序号',
    `customer_id` bigint(20) not null comment '客户ID',
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `voucher_date` date not null comment '凭证日期:yyyyMMdd',
    `voucher_id` bigint(20) not null comment '所属凭证ID',
    `voucher_number` int(11) not null comment '凭证号',
    `currency_name` varchar(255) not null comment '原币名称',
    `summary` varchar(500) not null default '' comment '摘要',
    `subject_id` bigint(20) not null comment '科目ID',
    `subject_number` varchar(50) not null comment '科目编号',
    `debit_amount` decimal(12, 5) default 0 not null comment '借方金额(原币)',
    `credit_amount` decimal(12, 5) default 0 not null comment '贷方金额(原币)',
    `local_debit_amount` decimal(12, 5) default 0 not null comment '借方金额(本币)',
    `local_credit_amount` decimal(12, 5) default 0 not null comment '贷方金额(本币)',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='凭证项';
create index voucher_item_year_month_num_index
    on voucher_item (year_month_num);
create index voucher_item_subject_id_index
    on voucher_item (subject_id);

CREATE TABLE if not exists `sequence` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `use_category` varchar(50) not null default '' comment '使用类别',
    `create_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='序列表';

create table if not exists `initial_balance` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `subject_id` bigint(20) not null comment '科目ID',
    `subject_number` varchar(50) not null comment '科目编号',
    `beginning_amount` decimal(12, 5) not null default 0 comment '年初余额',
    `opening_amount` decimal(12, 5) not null default 0 comment '期初余额(本年借方-本年贷方+年初余额)',
    `debit_annual_amount` decimal(12, 5) not null default 0 comment '本年累计借方金额',
    `credit_annual_amount` decimal(12, 5) not null default 0 comment '本年累计贷方金额',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='初始余额';

create table if not exists `initial_balance_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `initial_balance_id` bigint(20) not null,
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `subject_id` bigint(20) not null comment '科目ID',
    `subject_number` varchar(50) not null comment '科目编号',
    `currency_name` varchar(255) not null comment '原币名称',
    `lending_direction` enum('BORROW', 'LOAN') not null comment '科目方向,BORROW：借、LOAN：贷',
    `debit_amount` decimal(12, 5) not null default 0 comment '借方金额',
    `credit_amount` decimal(12, 5) not null default 0 comment '贷方金额',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='初始余额条目';

create table if not exists `account_close_list` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='关账列表';

CREATE TABLE if not exists `approval_flow` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null comment '客户ID',
    `business_module` enum('EXPENSE_BILL') not null comment '所属业务模块',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批流';

CREATE TABLE if not exists `approval_flow_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `approval_flow_id` bigint(20) not null comment '审批流程ID',
    `department` varchar(255) not null default '' comment '部门',
    `level` int not null comment '审批级别，从1开始',
    `lasted` bit not null default false comment '是否是末级审批',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批流的审批项';

CREATE TABLE if not exists `approval_flow_approver` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `flow_id` bigint(20) not null comment '审批流程ID',
    `item_id` bigint(20) not null comment '审批流的审批项ID',
    `approver_id` bigint(20) not null comment '审批人ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批流相关的审核人员';

CREATE TABLE if not exists `approval_instance` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null,
    `business_module` enum('EXPENSE_BILL') not null comment '所属业务模块',
    `module_id` bigint(20) not null comment '被审批的记录的ID',
    `approval_flow_id` bigint(20) not null comment '审批流程ID',
    `current_item_id` bigint(20) not null default 0 comment '当前所处审批项',
    `current_level` int not null default 1 comment '当前所处审批级别',
    `ended` bit not null default false comment '审批实例是否结束',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批实例';

CREATE TABLE if not exists `approval_instance_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `instance_id` bigint(20) not null comment '审批流程实例',
    `module_id` bigint(20) not null comment '被审批的记录的ID',
    `level` int not null comment '审批级别，从1开始',
    `approver` varchar(50) not null default '' comment '审批人',
    `approver_id` bigint(20) not null default 0 comment '审批人ID',
    `approval_time` datetime comment '审批时间',
    `passed` bit not null default false comment '是否通过',
    `lasted` bit not null default false comment '是否是末级审批',
    `remark` varchar(500) default '' comment '备注',
    `create_by` bigint(20) not null default 1,
    `creator_name` varchar(50) not null default '管理员',
    `create_time` datetime not null default current_timestamp,
    `modify_by` bigint(20) not null default 1,
    `modify_name` varchar(50) not null default '管理员',
    `modify_time` datetime not null default current_timestamp,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批实例的审批项';

create index approval_instance_item_approver_id_index
    on approval_instance_item (approver_id);

CREATE TABLE if not exists `approval_instance_approver` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null,
    `instance_id` bigint(20) not null comment '审批流实例ID',
    `module_id` bigint(20) not null comment '被审批的记录的ID',
    `item_id` bigint(20) not null comment '审批流的审批项ID',
    `approver_id` bigint(20) not null comment '审批人ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='审批实例相关的审核人员';
create index approval_instance_approver_approver_id_index
    on approval_instance_approver (approver_id);
create index approval_instance_approver_customer_id_index
    on approval_instance_approver (customer_id);

CREATE TABLE if not exists `account_balance` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null,
    `year` int(11) not null comment '年份:yyyy',
    `year_month_num` int(11) not null comment '月份:yyyyMM',
    `subject_id` bigint(20) not null,
    `subject_number` varchar(50) not null,
    `max_voucher_number` int(11) not null default 0 comment '最大凭证号',
    `debit_opening_amount` decimal(12, 5) default 0 not null comment '期初金额(借)',
    `credit_opening_amount` decimal(12, 5) default 0 not null comment '期初金额(贷)',
    `debit_closing_amount` decimal(12, 5) default 0 not null comment '期末金额(借)',
    `credit_closing_amount` decimal(12, 5) default 0 not null comment '期末金额(贷)',
    `debit_current_amount` decimal(12, 5) default 0 not null comment '本期金额(借)',
    `credit_current_amount` decimal(12, 5) default 0 not null comment '本期金额(贷)',
    `debit_annual_amount` decimal(12, 5) default 0 not null comment '年累计金额(借)',
    `credit_annual_amount` decimal(12, 5) default 0 not null comment '年累计金额(贷)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='科目余额(月关帐的时候生成)';

CREATE TABLE if not exists `report` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `name` varchar(255) not null default '',
    `row_num` int default 0 comment '行次',
    `level` int(2) not null default 0,
    `category` int(2) not null default 1,
    `formula_type` int(2) not null default 0 comment '0:none, 1:report_formula, 2:aggregate_formula',
    PRIMARY KEY (`id`, `customer_id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='报表条目';

CREATE TABLE if not exists `report_formula` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `report_id` bigint(20) default 0 comment '序号',
    `symbol` varchar(2) not null default '+' comment '计算符号',
    `number` varchar(50) not null default '' comment '科目编号',
    `number_rule` int(2) not null default '1' comment '取数规则',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='报表条目计算公式(科目)';

CREATE TABLE if not exists `aggregate_formula` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `aggregate_report_id` bigint(20) not null comment '条目',
    `report_id` bigint(20) not null comment '参与计算的条目id',
    `symbol` varchar(2) not null default '+' comment '计算符号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB collate = utf8mb4_bin COMMENT='报表条目计算公式(条目)';

truncate table resource;
replace into `resource` (id, number, name, parent_id, parent_number, has_leaf, level,
                         type, url, icon, module,permit_code, sort_num, super_customer)values
(1, '1', '客户管理', 0, '', true, 1, 'MENU', '/Manage/customer', 'icon-kehuguanli', 1, '', 10, false),
(2, '2', '客户分类', 1, '1', false, 2, 'MENU', '/Manage/customer/category', '', 1, '', 1000, false),
(3, '3', '客户档案', 1, '1', false, 2, 'MENU', '/Manage/customer/archive', '', 1, '', 1000, false),
(5, '5', '审批流程配置', 1, '1', false, 2, 'MENU', '/Manage/customer/approvalFlow', '', 1, '', 1000, true),

(20, '20', '设置', 0, '', true, 1, 'MENU', '/Manage/setting', 'icon-xitongguanli1', 1, '', 20, false),
(21, '21', '用户管理', 20, '20', false, 2, 'MENU', '/Manage/setting/user', '', 1, '', 1000, false),

(50, '50', '设置', 0, '', true, 1, 'MENU', '/Finance/setting', 'icon-xitongguanli1', 2, '', 90, false),
(60, '60', '科目', 50, '50', false, 2, 'MENU', '/Finance/setting/subject', '', 2, '', 400, false),
(90, '90', '初始余额', 50, '50', false, 2, 'MENU', '/Finance/setting/initialBalance', '', 2, 'base,auditing,unAuditing', 60, false),

(100, '100', '费用报销管理', 0, '', false, 1, 'MENU', '/Finance/expense/bill', 'icon-baoxiaoshenqing-feiyongbaoxiaoshenqing-06', 2, 'view:all,base,auditing,unAuditing,print', 10, false),
(120, '120', '记账管理', 0, '', true, 1, 'MENU', '/Finance/voucher', 'icon-ico_hushigongzuozhan_jizhangguanli', 2, '', 20, false),
(121, '121', '录凭证', 120, '120', false, 2, 'MENU', '/Finance/voucher/record', '', 2, 'view:all,base,auditing,unAuditing,print', 100, false),
(122, '122', '查凭证', 120, '120', false, 2, 'MENU', '/Finance/voucher/list', '', 2, '', 100, false),
-- (160, '160', '汇率管理', 120, '120', false, 2, 'MENU', '/Finance/voucher/currency', '', 2, 'base,auditing,unAuditing', 500, false),
(170, '170', '结账', 120, '120', false, 2, 'MENU', '/Finance/voucher/accountClose', '', 2, '', 50, false),

(180, '180', '账簿', 0, '', true, 1, 'MENU', '/Finance/book', 'icon-zhangbu-normal', 2, '', 50, false),
(181, '181', '科目余额表', 180, '180', false, 2, 'MENU', '/Finance/book/accountBalance', '', 2, '', 50, false),
(182, '182', '总账', 180, '180', false, 2, 'MENU', '/Finance/book/generalLedger', '', 2, '', 50, false),
(183, '183', '明细账', 180, '180', false, 2, 'MENU', '/Finance/book/subLedger', '', 2, '', 50, false),
(184, '184', '现金日记账', 180, '180', false, 2, 'MENU', '/Finance/book/dailyCash', '', 2, '', 50, false),
(185, '185', '银行日记账', 180, '180', false, 2, 'MENU', '/Finance/book/dailyBank', '', 2, '', 50, false),

(200, '200', '报表', 0, '', true, 1, 'MENU', '/Finance/report', 'icon-baobiaoguanli', 2, '', 50, false),
(260, '260', '利润表', 200, '200', false, 2, 'MENU', '/Finance/report/profit', '', 2, '', 50, false),
(270, '270', '现金流量表', 200, '200', false, 2, 'MENU', '/Finance/report/cashFlow', '', 2, '', 50, false),
(280, '280', '资产负债表', 200, '200', false, 2, 'MENU', '/Finance/report/balanceSheet', '', 2, '', 50, false)
;

delete from `user_resource` where user_id = 1;
insert into `user_resource` (user_id, resource_id,permit_code)
select 1, f.id,f.permit_code from `resource` f;

delete from `customer_resource` where customer_id = 0;
insert into `customer_resource` (customer_id, resource_id)
select 0, f.id from `resource` f;

replace into customer (id,`number`,`name`,industry_id,category_id,type,use_foreign_exchange,business_user_id)
values(0, 'HX_TOP', '记账平台',0,0,'RENT_AND_PROXY', true, 1);