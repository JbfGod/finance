import {ACCESS_TOKEN} from "@/constants";
import React from "react";

export function preMinioUrl(url) {
  if (url.startsWith("blob")) {
    return url
  }
  return `/minio${url}`
}

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

const USER_IDENTITY = "USER_IDENTITY"

/**
 * @returns [APPROVER|NORMAL]{string}
 */
export function getUserIdentity() {
  return sessionStorage.getItem(USER_IDENTITY)
}

/**
 * 切换用户身份
 * @param identity [NORMAL|APPROVER](操作员|审批人员)
 */
export function switchUserIdentity(identity) {
  return sessionStorage.setItem(USER_IDENTITY, identity)
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

export function flatTreeToMap(treeData = {}, keyField = "id", childrenField = "children") {
  const map = {}
  eachTree(treeData, node => map[node[keyField]] = node, childrenField)
  return map
}

export function flatArrayToMap(array = [], keyField = "id") {
  const map = {}
  array.forEach(ele => map[ele[keyField]] = ele)
  return map;
}

export function jsonToFormData(json) {
  const formData = new FormData()
  const recursion = (obj, keyPrefix = "") => {
    const keys = Object.keys(obj)
    keys.forEach(key => {
      const value = obj[key]
      if (value == null || value === "") {
        return
      }
      let fKey = `${keyPrefix}${keyPrefix ? "." : ""}${key}`
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

/**
 * 数字人民币转大写
 */
export function convertCurrency(currencyDigits) {
  // Predefine the radix characters and currency symbols for output:
  const CN_ZERO = "零", CN_ONE = "壹", CN_TWO = "贰", CN_THREE = "叁", CN_FOUR = "肆", CN_FIVE = "伍",
    CN_SIX = "陆", CN_SEVEN = "柒", CN_EIGHT = "捌", CN_NINE = "玖", CN_TEN = "拾", CN_HUNDRED = "佰",
    CN_THOUSAND = "仟", CN_TEN_THOUSAND = "万", CN_HUNDRED_MILLION = "亿", CN_SYMBOL = "人民币",
    CN_DOLLAR = "元", CN_TEN_CENT = "角", CN_CENT = "分", CN_INTEGER = "整";

  // Variables:
  let integral;    // Represent integral part of digit number.
  let decimal;    // Represent decimal part of digit number.
  let outputCharacters;    // The output result.
  let parts;
  let digits, radices, bigRadices, decimals;
  let zeroCount;
  let i, p, d;
  let quotient, modulus;

  // Validate input string:
  let currencyDigitsStr = currencyDigits.toString();

  currencyDigitsStr = currencyDigitsStr.replace(/,/g, "");    // Remove comma delimiters.
  currencyDigitsStr = currencyDigitsStr.replace(/^0+/, "");    // Trim zeros at the beginning.

  parts = currencyDigitsStr.split(".");
  if (parts.length > 1) {
    integral = parts[0];
    decimal = parts[1];
    // Cut down redundant decimal digits that are after the second.
    decimal = decimal.substring(0, 2);
  } else {
    integral = parts[0]
    decimal = ""
  }
  // Prepare the characters corresponding to the digits:
  digits = [CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE]
  radices = ["", CN_TEN, CN_HUNDRED, CN_THOUSAND]
  bigRadices = ["", CN_TEN_THOUSAND, CN_HUNDRED_MILLION]
  decimals = [CN_TEN_CENT, CN_CENT]
  // Start processing:
  outputCharacters = "";
  // Process integral part if it is larger than 0:
  if (Number(integral) > 0) {
    zeroCount = 0;
    for (i = 0; i < integral.length; i++) {
      p = integral.length - i - 1;
      d = integral.substr(i, 1);
      quotient = p / 4;
      modulus = p % 4;
      if (d === "0") {
        zeroCount++;
      } else {
        if (zeroCount > 0) {
          outputCharacters += digits[0];
        }
        zeroCount = 0;
        outputCharacters += digits[Number(d)] + radices[modulus];
      }
      if (modulus === 0 && zeroCount < 4) {
        outputCharacters += bigRadices[quotient];
        zeroCount = 0;
      }
    }
    outputCharacters += CN_DOLLAR;
  }
  // Process decimal part if there is:
  if (decimal !== "") {
    for (i = 0; i < decimal.length; i++) {
      d = decimal.substr(i, 1);
      if (d !== "0") {
        outputCharacters += digits[Number(d)] + decimals[i];
      }
    }
  }
  // Confirm and return the final output string:
  if (outputCharacters === "") {
    outputCharacters = CN_ZERO + CN_DOLLAR;
  }
  if (decimal === "") {
    outputCharacters += CN_INTEGER;
  }
  return outputCharacters;
}

export function getHasChildNode(treeData, childrenField = "children") {
  const hasChildNodes = []
  const recursion = (tree) => {
    if (!tree) {
      return
    }
    for (let i = 0; i < tree.length; i++) {
      const children = tree[i][childrenField]
      if (!children || children.length === 0) {
        continue
      }
      hasChildNodes.push(tree[i])
      recursion(children)
    }
  }
  recursion(treeData)
  return hasChildNodes
}
