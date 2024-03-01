import {useCallback, useEffect, useMemo, useState} from "react"
import {listSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {arrayToTree, getAccessToken} from "@/utils/common";

export default function useSubjectModel() {
  const isAuth = !!getAccessToken()
  const [subjects, setSubjects] = useState([])
  const treeSubjects = useMemo(() => arrayToTree(subjects), [subjects])
  const subjectById = useMemo(() => subjects.reduce((curr, next) => {
    curr[next.id] = next
    return curr
  }, {}), [subjects])
  // 现金科目
  const cashSubjects = useMemo(() => subjects.filter(sub => sub.level === 1 && sub?.name.includes("现金")), [subjects])
  // 银行科目
  const bankSubjects = useMemo(() => subjects.filter(sub => sub.level === 1 && sub?.name.includes("银行")), [subjects])

  const fetchSubjects = useCallback((params) => {
    return listSubjectUsingGET(params).then(result => {
      setSubjects(result.data.map(sub => ({
        ...sub, isLeaf: !sub.hasLeaf, key: sub.id, title: `${sub.number} ${sub.name}`
      })))
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
      return listSubjectUsingGET(params).then(result => {
        setSubjects(result.data.filter(filter))
        return result
      })
    }
    return subjects.filter(filter)
  }, [subjects])

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
