import menus from "../config/menus";
import {flatTree} from "@/utils/common";
/**
 * TODO 通过菜单加载权限
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */

export default function access(initialState) {
  const {selfPermissions} = initialState ?? {}
  const allMenuAccess = flatTree(menus, "routes")
    .filter(menu => menu.access)
    .reduce((curr, next) => {
      curr[next.access] = (selfPermissions || []).includes(next.access)
      return curr
    }, {})
  return {
    ...(selfPermissions || []).reduce((curr, next) => {
      curr[next] = true
      return curr;
    }, {}),
    ...allMenuAccess,
    "can": true
  }
}
