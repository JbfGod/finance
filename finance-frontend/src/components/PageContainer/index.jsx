import React from 'react';
import {PageContainer} from "@ant-design/pro-layout";

export default function GlobalPageContainer(props) {
  return (
    <PageContainer {...props} header={{title: null}}/>
  )
}
