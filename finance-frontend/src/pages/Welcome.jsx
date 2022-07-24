import React from 'react';
import PageContainer from "@/components/PageContainer";
import {Alert, Card} from 'antd';

const Welcome = () => {
  return (
    <PageContainer>
      <Card>
        <Alert
          message="欢迎使用慧记账平台."
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
        {/*<Typography.Text strong>
          Advanced Form{' '}
          <a
            href="https://procomponents.ant.design/components/table"
            rel="noopener noreferrer"
            target="__blank"
          >
            Welcome
          </a>
        </Typography.Text>
        <CodePreview>yarn add @ant-design/pro-table</CodePreview>
        <Typography.Text
          strong
          style={{
            marginBottom: 12,
          }}
        >
          Advanced layout{' '}
          <a
            href="https://procomponents.ant.design/components/layout"
            rel="noopener noreferrer"
            target="__blank"
          >
            Welcome
          </a>
        </Typography.Text>
        <CodePreview>yarn add @ant-design/pro-layout</CodePreview>*/}
      </Card>
    </PageContainer>
  );
};

export default Welcome;
