CREATE TABLE if not exists `industry_${tableId}` (
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

CREATE TABLE if not exists `subject_${tableId}` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`industry_id` bigint(20) not null comment '所属行业',
`number` varchar(50) not null comment '科目编号',
`name` varchar(255) NOT NULL COMMENT '科目名称',
`direction` enum('BORROW', 'LOAN', 'NOTHING') not null default 'NOTHING' comment '科目方向,BORROW：借、LOAN：贷、NOTHING：空',
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

CREATE TABLE if not exists `sequence_${tableId}` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`use_category` varchar(50) not null default '' comment '使用类别',
`create_time` datetime not null default current_timestamp,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列表';