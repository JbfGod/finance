/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export const CAN = "can"
export const USER_PAGE = "sys:userPage"
export const CUSTOMER_CATEGORY_PAGE = "sys:customer:categoryPage"
export const CUSTOMER_PAGE = "sys:customerPage"
export const CUSTOMER_AUTHORIZE_PAGE = "sys:customer:grantResourcePage"
export const INDUSTRY_PAGE = "base:industryPage"
export const SUBJECT_PAGE = "base:subjectPage"
export const EXPENSE_BILL_PAGE = "expense:billPage"
export const VOUCHER_LIST_PAGE = "voucher:listPage"
export const VOUCHER_BOOK_PAGE = "voucher:bookPage"
export const CURRENCY_PAGE = "currencyPage"

const permissions = [
  USER_PAGE,  CUSTOMER_CATEGORY_PAGE,  CUSTOMER_PAGE,  CUSTOMER_AUTHORIZE_PAGE,  INDUSTRY_PAGE,  SUBJECT_PAGE,
  EXPENSE_BILL_PAGE, VOUCHER_LIST_PAGE, VOUCHER_BOOK_PAGE, CURRENCY_PAGE
]

export default function access(initialState) {
  const { selfPermissions, currentUser } = initialState ?? {};
  const accessMap = permissions.reduce((curr, next) => {
    curr[next] = selfPermissions?selfPermissions.includes(next):false
    return curr
  }, {})
  return {
    [CAN] : true,
    ...accessMap,
  }
}
