import {useCallback, useEffect, useMemo, useState} from "react"
import {useCurrentUser} from "@/utils/hooks";
import {listUserFromSuperCustomerUsingGET} from "@/services/swagger/userWeb";
import {getCurrentUser} from "@/utils/common";

export default function useSuperCustomerUser() {
  const currentUser = getCurrentUser()
  const isSuperCustomer = currentUser?.customerNumber === "HX_TOP"
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
