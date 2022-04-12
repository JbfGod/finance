
export function onClick(callback) {
  return (e) => {
    e.stopPropagation()
    callback?.()
  }
}
