import constants, {ACCESS_TOKEN, CUSTOMER_HEAD} from "@/constants";
import {message, Modal} from "antd";
import React from "react";

export function setAccessToken(token) {
  sessionStorage.setItem(ACCESS_TOKEN, token)
}

export function clearAccessToken() {
  sessionStorage.removeItem(ACCESS_TOKEN)
}

export function getAccessToken() {
  return sessionStorage.getItem(ACCESS_TOKEN)
}
const CURR_CUSTOMER_KEY = "CURR_CUSTOMER"
export function getCurrCustomer() {
  const customer = sessionStorage.getItem(CURR_CUSTOMER_KEY)
  return customer && JSON.parse(customer) || undefined
}

export function setCurrCustomer(customer) {
  return sessionStorage.setItem(CURR_CUSTOMER_KEY, JSON.stringify(customer))
}

export function removeCurrCustomer() {
  return sessionStorage.removeItem(CURR_CUSTOMER_KEY)
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
