import {Select} from "antd";
import {useEffect} from "react";
import {useModel} from "umi";

export default function SubjectSelect({value, onChange, ...props}) {
  const {subjects} = useModel("useSubjectModel")

  const triggerChange = (newValue) => {
    onChange?.(newValue)
  }

  useEffect(() => {

  }, [])
  return (
    <Select value={value} onChange={triggerChange}
      options={subjects.map(sub => ({label: `${sub.number} ${sub.name}`, value: sub.id}))}
      {...props}
    />
  )
}
