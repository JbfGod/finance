/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState) {
  const { selfPermissions } = initialState ?? {};
  const accessMap = (selfPermissions || []).reduce((curr, next) => {
    curr[next] = true
    return curr
  }, {})
  return {
    strictMode: true,
    ...accessMap,
  }
}
