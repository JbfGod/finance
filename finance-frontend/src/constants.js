const constants = {
  /**
   * SessionStorage Item Key
   */
  ACCESS_TOKEN: "AccessToken",
  /**
   * LocalStorage Item Key
   */
  LAST_LOGIN_CUSTOMER_ACCOUNT: "LS_Key_LAST_LOGIN_CUSTOMER_ACCOUNT",

}
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
  APPROVER: {label: "审批人员", value: "APPROVER"},
  OFFICER: {label: "机关人员", value: "OFFICER"}
}
export const SUBJECT_TYPE = {
  SUBJECT: {label: "科目", value: "SUBJECT"},
  COST: {label: "费用", value: "COST"},
  SUBJECT_AND_COST: {label: "科目+费用", value: "SUBJECT_AND_COST"},
}
export const SUBJECT_DIRECTION = {
  NOTHING: {label: "无", value: "NOTHING"},
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

export default constants
