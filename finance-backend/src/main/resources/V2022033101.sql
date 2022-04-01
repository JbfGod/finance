CREATE TABLE if not exists `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(255) NOT NULL COMMENT '登录用户名',
    `password` varchar(50) DEFAULT NULL COMMENT '登陆密码',
    `customer_id` bigint(20) not null default 0 comment '所属客户',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    `deleted` bit not null default false,
    PRIMARY KEY (`id`),
    unique key (`customer_id`, `username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
replace into `user` (id, username, password)
values(1, 'super_admin', '$2a$10$YdOoLfvwipCxpCcs.yGv/ujEDs7OvWTjhXG16QSpH5k28U6o1BK0q');

CREATE TABLE if not exists `customer` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) not null comment '登录账户',
    `name` varchar(255) NOT NULL COMMENT '客户名称',
    `industry_id` bigint(20) not null comment '所属行业',
    `category_id` bigint(20) not null comment '客户类别',
    `type` enum('RENT', 'PROXY', 'RENT_AND_PROXY') not null comment '客户类型：租用、代理、租用+代理',
    `status` enum('ENABLE', 'DISABLE') not null comment '客户状态：停用、启用',
    `effect_time` datetime not null comment '租赁生效时间',
    `expire_time` datetime not null comment '租赁过期时间',
    `telephone` varchar(50) not null comment '客户电话',
    `bank_account` varchar(50) not null comment '银行账户',
    `bank_account_name` varchar(50) not null comment '银行开户名称',
    `use_foreign_exchange` bit not null default false comment '是否使用外汇',
    `remark` varchar(500) comment '备注',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    `deleted` bit not null default false,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户表';

CREATE TABLE if not exists `customer_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '客户编号',
    `name` varchar(255) NOT NULL COMMENT '客户名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null comment '节点深度',
    `left_value` int(11) not null comment '节点左值',
    `right_value` int(11) not null comment '节点右值',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    `deleted` bit not null default false,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户分类表';

CREATE TABLE if not exists `industry` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) not null comment '行业编号',
    `name` varchar(255) NOT NULL COMMENT '行业名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null comment '节点深度',
    `left_value` int(11) not null comment '节点左值',
    `right_value` int(11) not null comment '节点右值',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    `deleted` bit not null default false,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行业分类表';

CREATE TABLE if not exists `subject` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `industry_id` bigint(20) not null comment '所属行业',
    `number` varchar(50) not null comment '科目编号',
    `name` varchar(255) NOT NULL COMMENT '科目名称',
    `parent_id` bigint(20) not null default 0 comment '父级ID',
    `parent_number` varchar(50) not null comment '父级编号',
    `has_leaf` bit not null default false comment '是否有叶子节点',
    `level` int(11) not null comment '节点深度',
    `left_value` int(11) not null comment '节点左值',
    `right_value` int(11) not null comment '节点右值',
    `create_by` varchar(50) not null default 'admin',
    `create_time` datetime not null default current_timestamp,
    `modify_by` varchar(50) not null default 'admin',
    `modify_time` datetime not null default current_timestamp,
    `deleted` bit not null default false,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='科目表';