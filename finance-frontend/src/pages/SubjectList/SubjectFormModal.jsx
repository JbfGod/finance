import React from "react";
import {useModel} from "umi";
import {ModalForm, ProFormGroup, ProFormItem, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import * as subjectWeb from "@/services/swagger/subjectWeb";
import {LENDING_DIRECTION, SUBJECT_ASSIST_SETTLEMENT, SUBJECT_CATEGORY} from "@/constants";
import SubjectSelect from "@/pages/SubjectList/SubjectSelect";
import {Form} from "antd";

export default function SubjectFormModal({modal, tblActionRef}) {
  const {fetchSubjects, subjectById} = useModel("useSubjectModel")
  const {handleVisible, state} = modal
  const {industryId, id: parentId, category = "ASSETS"} = state.parent || {}
  const [form] = Form.useForm();
  const formParentId = Form.useWatch('parentId', form);
  return (
    <ModalForm title="新增科目" width={680} open={true}
               form={form}
               initialValues={{parentId, category, assistSettlement: "NOTHING", lendingDirection: "BORROW"}}
               modalProps={{destroyOnClose: true}}
               onOpenChange={handleVisible}
               layout="horizontal" grid={true}
               onFinish={async (value) => {
                 await subjectWeb.addSubjectUsingPOST({
                   ...value,
                   number: `${subjectById[formParentId]?.number || ""}${value.number}`,
                   industryId: industryId || industryId,
                   parentId
                 }).then(() => {
                   modal.close()
                   fetchSubjects()
                   tblActionRef.current?.reload()
                 })
               }}
    >
      <ProFormGroup colProps={{md: 12}}>
        <ProFormItem name="parentId" label="上级科目">
          <SubjectSelect allowClear={true} style={{width: 245}} disabled={!!parentId}/>
        </ProFormItem>
      </ProFormGroup>
      <ProFormText name="number" label="科目编号" colProps={{md: 12}}
                   addonBefore={subjectById[formParentId]?.number}
                   rules={[
                     {required: true, message: "科目编号不能为空！"},
                   ]}
      />
      <ProFormText name="name" label="科目名称" colProps={{md: 12}}
                   rules={[
                     {required: true, message: "科目名称不能为空！"},
                   ]}
      />
      <ProFormSelect name="category" colProps={{md: 12}} disabled={!!parentId}
                     allowClear={false} label="类别" options={Object.values(SUBJECT_CATEGORY)}/>
      <ProFormSelect name="lendingDirection" allowClear={false} label="余额方向" options={Object.values(LENDING_DIRECTION)}
                     rules={[
                       {required: true, message: "余额方向不能为空！"},
                     ]}
      />
      <ProFormSelect name="assistSettlement" allowClear={false} label="辅助结算"
                     options={Object.values(SUBJECT_ASSIST_SETTLEMENT)}/>
    </ModalForm>
  )
}
