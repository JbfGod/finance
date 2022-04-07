
export const isTrue = (expression, msg) => {
  if (expression) {
    return
  }
  throw new Error(msg)
}
