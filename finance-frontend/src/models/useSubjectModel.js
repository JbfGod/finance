import {useCallback, useEffect, useMemo, useState} from "react"
import {treeSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {flatTree} from "@/utils/common";
import {useCurrentUser} from "@/utils/hooks";

export default function useSubjectModel() {
  const {isAuth} = useCurrentUser()

  const [treeSubjects, setTreeSubjects] = useState([])
  const subjects = useMemo(() => flatTree(treeSubjects).map(({children, ...subject}) => subject)
    , [treeSubjects]
  )
  const subjectById = useMemo(() => subjects.reduce((curr, next) => {
    curr[next.id] = next
    return curr
  }, {}), [subjects])

  const fetchSubjects = useCallback(() => {
    treeSubjectUsingGET().then(({data}) => {
      setTreeSubjects(data)
    })
  }, [])
  useEffect(() => {
    isAuth && fetchSubjects()
  }, [isAuth])
  return {
    subjects,
    treeSubjects,
    subjectById,
    fetchSubjects
  }
}
