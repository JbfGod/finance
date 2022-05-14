/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export const CAN = "can"
export const USER_PAGE = "system:user"
export const CUSTOMER_CATEGORY_PAGE = "system:customerCategory"
export const CUSTOMER_PAGE = "system:customer"
export const CUSTOMER_AUTHORIZE_PAGE = "system:customer:grantPermissionPage"
export const INDUSTRY_PAGE = "base:industry"
export const SUBJECT_PAGE = "base:subject"

const permissions = [
  USER_PAGE,  CUSTOMER_CATEGORY_PAGE,  CUSTOMER_PAGE,  CUSTOMER_AUTHORIZE_PAGE,  INDUSTRY_PAGE,  SUBJECT_PAGE
]

export default function access(initialState) {
  const { selfPermissions } = initialState ?? {};
  const accessMap = permissions.reduce((curr, next) => {
    curr[next] = selfPermissions?selfPermissions.includes(next):false
    return curr
  }, {})
  return {
    [CAN] : true,
    ...accessMap,
  }
}
