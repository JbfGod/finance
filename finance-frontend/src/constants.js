/**
 * LocalStorage Item Key
 */
export const LAST_LOGIN_CUSTOMER_ACCOUNT = "LS_Key_LAST_LOGIN_CUSTOMER_ACCOUNT"
export const CURRENT_USER_KEY = "CURRENT_USER"
/**
 * SessionStorage Item Key
 */
export const ACCESS_TOKEN = "AccessToken"
/**
 * BucketName
 */
export const BUCKET_EXPENSE_BILL = "EXPENSE_BILL"

export const IMG_ACCEPT = ".jpeg,.jpg,.png,.gif"
/**
 * enum
 */
export const USER_ROLE = {
  NORMAL: {label: "操作人员", value: "NORMAL"},
  NORMAL_APPROVER: {label: "审批人员", value: "NORMAL_APPROVER"},
  ADVANCED_APPROVER: {label: "操作+审批人员", value: "ADVANCED_APPROVER"},
}
export const SUBJECT_TYPE = {
  SUBJECT: {label: "科目", value: "SUBJECT"},
  COST: {label: "费用", value: "COST"},
  SUBJECT_AND_COST: {label: "科目+费用", value: "SUBJECT_AND_COST"},
}
export const LENDING_DIRECTION = {
  DEFAULT: {label: "借+贷", value: "DEFAULT"},
  BORROW: {label: "借", value: "BORROW"},
  LOAN: {label: "贷", value: "LOAN"},
}
export const SUBJECT_ASSIST_SETTLEMENT = {
  NOTHING: {label: "无", value: "NOTHING"},
  SUPPLIER: {label: "供应商", value: "SUPPLIER"},
  CUSTOMER: {label: "客户", value: "CUSTOMER"},
  EMPLOYEE: {label: "员工", value: "EMPLOYEE"},
  BANK: {label: "银行", value: "BANK"}
}
export const CUSTOMER_STATUS = {
  INITIALIZING: {label: "初始化中", value: "INITIALIZING"},
  SUCCESS: {label: "已完成", value: "SUCCESS"}
}
export const CUSTOMER_TYPE = {
  RENT: {label: "租用", value: "RENT"},
  PROXY: {label: "代理", value: "PROXY"},
  RENT_AND_PROXY: {label: "租用+代理", value: "RENT_AND_PROXY"}
}

export const ApprovalBusinessModules = {
  EXPENSE_BILL: {label: "费用报销", value: "EXPENSE_BILL"}
}

/**
 * 货币类型
 */
export const CURRENCY_TYPE = {
  /**
   * 本币
   */
  LOCAL: "LOCAL",
  /**
   * 外币
   */
  FOREIGN: "FOREIGN"
}
/**
 * 审核状态
 */
export const AuditStatus = {
  /**
   * 待审核
   */
  TO_BE_AUDITED : "TO_BE_AUDITED",
  /**
   * 已审核
   */
  AUDITED: "AUDITED",
  APPROVED: "APPROVED"
}
