import {useCallback, useEffect, useMemo, useState} from "react"
import {useCurrentUser} from "@/utils/hooks";
import {listUserFromSuperCustomerUsingGET} from "@/services/swagger/userWeb";

export default function useSuperCustomerUser() {
  const {isSuperCustomer} = useCurrentUser()
  const [users, setUsers] = useState([])
  const approvers = useMemo(() => users.filter(user => ["ADMIN", "ADVANCED_APPROVER", "APPROVER"].includes(user.role)), [users])

  const fetchUsers = useCallback(() => {
    listUserFromSuperCustomerUsingGET().then(({data}) => setUsers(data))
  }, [])
  useEffect(() => {
    isSuperCustomer && fetchUsers()
  }, [isSuperCustomer])
  return {
    users,
    approvers,
    fetchUsers
  }
}
