import {Popconfirm} from "antd";
import React, {useState} from "react";

export default function LoadingConfirm({onConfirm, ...props}) {
  const [loading, setLoading] = useState(true)
  const triggerConfirm = () => {
    setLoading(true)
    onConfirm && onConfirm().finally(_ => setLoading(false))
  }
  return (
    <Popconfirm {...props} onConfirm={triggerConfirm} okButtonProps={{loading}}>
      {props.children}
    </Popconfirm>
  )
}
