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
  const getSubjects = useCallback(({name, number, industryId}) => {
    return subjects.filter(sub => (
      (!industryId || sub.industryId === industryId)
      &&
      (!name || sub.name.startsWith(name))
      &&
      (!number || sub.number.startsWith(number))
    ))
  }, [subjects])

  useEffect(() => {
    isAuth && fetchSubjects()
  }, [isAuth])
  return {
    subjects,
    getSubjects,
    treeSubjects,
    subjectById,
    fetchSubjects
  }
}
