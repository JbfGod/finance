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
      </Card>
    </PageContainer>
  );
};

export default Welcome;
