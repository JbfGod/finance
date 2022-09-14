import menus from "../config/menus";
import {flatTree} from "@/utils/common";
/**
 * TODO 通过菜单加载权限
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */

export default function access(initialState) {
  const {selfPermissions, treeMenus = []} = initialState ?? {}
  const isAuth = !! initialState.currentUser
  const selfMenuPaths = flatTree(treeMenus, "children").map(m => m.path)
  const allMenuAccess = flatTree(menus, "routes")
    .filter(menu => menu.path)
    .reduce((curr, next) => {
      curr[next.path] = (selfMenuPaths || []).includes(next.path)
      return curr
    }, {})
  return {
    ...(selfPermissions || []).reduce((curr, next) => {
      curr[next] = true
      return curr;
    }, {}),
    ...allMenuAccess,
    isAuth,
    "can": true
  }
}
