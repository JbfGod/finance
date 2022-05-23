import constants from "@/constants";

export function setAccessToken(token) {
  sessionStorage.setItem(constants.ACCESS_TOKEN, token)
}

export function clearAccessToken() {
  sessionStorage.removeItem(constants.ACCESS_TOKEN)
}

export function getAccessToken() {
  return sessionStorage.getItem(constants.ACCESS_TOKEN)
}

export function eachTree(treeData = [], callback, childrenField = "children") {
  const recursion = (node) => {
    if (node == null) {
      return
    }
    callback && callback(node)
    const children = node[childrenField]
    if (children == null || children.length === 0) {
      return
    }
    for (let i = 0; i < children.length; i++) {
      recursion(children[i])
    }
  }
  for (let i = 0; i < treeData.length; i++) {
    recursion(treeData[i])
  }
}

export function flatTree(treeData = [], childrenField = "children") {
  const flatArray = []
  eachTree(treeData, node => flatArray.push(node), childrenField)
  return flatArray
}

export function jsonToFormData(json, matchTransform) {
  const formData = new FormData()
  const recursion = (obj, keyPrefix = "") => {
    const keys = Object.keys(obj)
    keys.forEach(key => {
      const value = obj[key]
      if (value == null || value === "") {
        return
      }
      let fKey = `${keyPrefix}${keyPrefix ? "." : ""}${key}`
      if (matchTransform) {
        for (let regexp in matchTransform) {
          if (new RegExp(regexp).test(fKey)) {
            const transformValue = matchTransform[regexp](key, value) || {}
            recursion(transformValue, keyPrefix)
            return
          }
        }
      }
      if (Array.isArray(value)) {
        value.forEach((tmp, index) => {
          recursion(tmp, `${fKey}[${index}]`)
        })
      } else if (value instanceof File) {
        formData.append(fKey, value)
      } else if (typeof value === "object") {
        recursion(value, `${fKey}`)
      } else {
        formData.append(fKey, value)
      }
    })
  }
  recursion(json)
  return formData
}
