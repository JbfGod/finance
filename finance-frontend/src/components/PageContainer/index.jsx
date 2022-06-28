import React from 'react';
import {PageContainer} from "@ant-design/pro-layout";

export default function GlobalPageContainer({header = {title: null}, ...props}) {
  return (
    <PageContainer header={header} {...props}/>
  )
}
