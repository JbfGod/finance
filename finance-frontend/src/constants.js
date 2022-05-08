const constants = {
  /**
   * SessionStorage Item Key
   */
  ACCESS_TOKEN: "AccessToken",
  /**
   * LocalStorage Item Key
   */
  LAST_LOGIN_CUSTOMER_ACCOUNT: "LS_Key_LAST_LOGIN_CUSTOMER_ACCOUNT",

  USER_ROLES: [
    {label: "操作人员", value: "NORMAL"},
    {label: "审批人员", value: "APPROVER"},
    {label: "机关人员", value: "OFFICER"},
  ],
  SUBJECT_TYPES: [
    {label: "科目", value: "SUBJECT"},
    {label: "花费", value: "COST"},
    {label: "科目+花费", value: "SUBJECT_AND_COST"},
  ],
  SUBJECT_DIRECTIONS: [
    {label: "无", value: "NOTHING"},
    {label: "借", value: "BORROW"},
    {label: "贷", value: "LOAN"},
  ],
  SUBJECT_ASSIST_SETTLEMENT: [
    {label: "无", value: "NOTHING"},
    {label: "供应商", value: "SUPPLIER"},
    {label: "客户", value: "CUSTOMER"},
    {label: "员工", value: "EMPLOYEE"},
    {label: "银行", value: "BANK"}
  ],
  CUSTOMER_STATUS: [
    {label: "初始化中", value: "INITIALIZING"},
    {label: "已完成", value: "SUCCESS"}
  ],
  CUSTOMER_TYPES: [
    {label: "租用", value: "RENT"},
    {label: "代理", value: "PROXY"},
    {label: "租用+代理", value: "RENT_AND_PROXY"}
  ],
}

export default constants
