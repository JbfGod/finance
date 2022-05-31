import {DownOutlined} from "@ant-design/icons";
import {Dropdown, Menu, Space} from "antd";
import React from "react";

const children = (
  <a onClick={e => e.preventDefault()}>
    更多
    <DownOutlined/>
  </a>
)

export default function AutoDropdown({overlay, max = 3, ...props}) {
  const isOver = overlay.length - max > 1
  const unOverComp = overlay.slice(0, max).filter(v => v)
  const overComp = overlay.slice(max).filter(v => v)
  return (
    <>
      <Space>
        {unOverComp}
        {isOver && (
          <Dropdown key="more" overlay={(
            <Menu items={overComp.map(Comp => ({
              key: Comp.key,
              label: Comp
            }))}/>
          )}>
            {props.children || children}
          </Dropdown>
        ) || overComp}
      </Space>
    </>
  )

}
