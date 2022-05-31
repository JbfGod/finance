import menus from "../config/menus";
import {flatTree} from "@/utils/common";
/**
 * TODO 通过菜单加载权限
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export const CAN = "can"
export const USER_PAGE = "user:page"
export const CUSTOMER_CATEGORY_PAGE = "customerCategory:page"
export const CUSTOMER_PAGE = "customer:page"
export const CUSTOMER_AUTHORIZE_PAGE = "customerAuthorize:page"
export const INDUSTRY_PAGE = "base:industryPage"
export const SUBJECT_PAGE = "base:subjectPage"
export const EXPENSE_BILL_PAGE = "expense:billPage"
export const VOUCHER_LIST_PAGE = "voucher:listPage"
export const VOUCHER_BOOK_PAGE = "voucher:bookPage"
export const CURRENCY_PAGE = "currencyPage"

const permissions = [
  USER_PAGE, CUSTOMER_CATEGORY_PAGE, CUSTOMER_PAGE, CUSTOMER_AUTHORIZE_PAGE, INDUSTRY_PAGE, SUBJECT_PAGE,
  EXPENSE_BILL_PAGE, VOUCHER_LIST_PAGE, VOUCHER_BOOK_PAGE, CURRENCY_PAGE
]

export default function access(initialState) {
  const {selfPermissions} = initialState ?? {};
  let accessMap = permissions.reduce((curr, next) => {
    curr[next] = selfPermissions ? selfPermissions.includes(next) : false
    return curr
  }, {})
  const allMenuAccess = flatTree(menus, "routes")
    .filter(menu => menu.access)
    .reduce((curr, next) => {
      curr[next.access] = (selfPermissions || []).includes(next.access)
      return curr
    }, {})
  return {
    ...accessMap,
    ...allMenuAccess,
    [CAN]: true
  }
}
