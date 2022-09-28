import {useCallback, useEffect, useMemo, useState} from "react"
import {listSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {arrayToTree} from "@/utils/common";
import {useCurrentUser} from "@/utils/hooks";

export default function useSubjectModel() {
  const {isAuth} = useCurrentUser()
  const [subjects, setSubjects] = useState([])
  const treeSubjects = useMemo(() => arrayToTree(subjects), [subjects])
  const subjectById = useMemo(() => subjects.reduce((curr, next) => {
    curr[next.id] = next
    return curr
  }, {}), [subjects])
  const fetchSubjects = useCallback((params) => {
    return listSubjectUsingGET(params).then(result => {
      setSubjects(result.data)
      return result
    })
  }, [])
  const getSubjects = useCallback(async (params) => {
    const {name, number, industryId} = params
    const filter = sub => (
      (!industryId || sub.industryId === industryId)
      &&
      (!name || sub.name.startsWith(name))
      &&
      (!number || sub.number.startsWith(number))
    )
    if (subjects.length === 0) {
      return fetchSubjects().then(result => result.data.filter(filter))
    }
    return subjects.filter(filter)
  }, [subjects])

  // 现金科目
  const cashSubjects = useMemo(() => treeSubjects.filter(sub => sub.name.contains("现金")), [])
  // 银行科目
  const bankSubjects = useMemo(() => treeSubjects.filter(sub => sub.name.contains("银行")), [])

  useEffect(() => {
    isAuth && fetchSubjects()
  }, [isAuth])
  return {
    subjects,
    getSubjects,
    treeSubjects,
    cashSubjects,
    bankSubjects,
    subjectById,
    fetchSubjects
  }
}
