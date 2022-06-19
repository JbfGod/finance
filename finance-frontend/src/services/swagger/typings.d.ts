declare namespace API {
  type AddCurrencyRequest = {
    name: string;
    number: string;
    rate: number;
    remark?: string;
    yearMonthNum: number;
  };

  type AddCustomerCategoryRequest = {
    name: string;
    number: string;
    parentId: number;
    remark?: string;
  };

  type AddIndustryRequest = {
    name: string;
    number: string;
    parentId: number;
    remark?: string;
  };

  type AddSubjectRequest = {
    assistSettlement?: 'BANK' | 'CUSTOMER' | 'EMPLOYEE' | 'NOTHING' | 'SUPPLIER';
    industryId: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    name: string;
    number: string;
    parentId: number;
    remark?: string;
    type: 'COST' | 'SUBJECT' | 'SUBJECT_AND_COST';
  };

  type AddUserRequest = {
    account: string;
    customerNumber?: string;
    name: string;
    password: string;
    role: 'ADMIN' | 'APPROVER' | 'NORMAL' | 'OFFICER';
  };

  type AddVoucherRequest = {
    attachmentNum?: number;
    currencyId: number;
    currencyName: string;
    items?: Item[];
    rate: number;
    serialNumber?: number;
    unit: string;
    voucherDate: string;
  };

  type CopyCurrencyRequest = {
    current?: number;
    isOverride?: boolean;
    pageSize?: number;
    sourceYearMonth?: number;
    targetYearMonth?: number;
  };

  type CurrencyVO = {
    auditStatus?: string;
    creatorName?: string;
    customerId?: number;
    id?: number;
    name?: string;
    number?: string;
    rate?: number;
    remark?: string;
    yearMonthNum?: string;
  };

  type CustomerCategoryVO = {
    hasLeaf?: boolean;
    id?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
  };

  type CustomerCueVO = {
    id?: number;
    name?: string;
    number?: string;
  };

  type CustomerListVO = {
    bankAccount?: string;
    bankAccountName?: string;
    businessUserId?: number;
    category?: string;
    contactName?: string;
    createTime?: string;
    effectTime?: string;
    enabled?: boolean;
    expireTime?: string;
    id?: number;
    industryId?: number;
    name?: string;
    number?: string;
    remark?: string;
    telephone?: string;
    type?: 'PROXY' | 'RENT' | 'RENT_AND_PROXY';
    useForeignExchange?: boolean;
  };

  type ExpenseBillDetailVO = {
    expensePerson?: string;
    expenseTime?: string;
    id?: number;
    items?: Item[];
    number?: string;
    position?: string;
    reason?: string;
    totalSubsidyAmount?: number;
  };

  type ExpenseBillPrintContentVO = {
    createTime?: string;
    creatorName?: string;
    expensePerson?: string;
    expenseTime?: string;
    id?: number;
    items?: Item[];
    number?: string;
    position?: string;
    reason?: string;
    totalSubsidyAmount?: number;
  };

  type ExpenseBillVO = {
    auditStatus?: 'AUDITED' | 'TO_BE_AUDITED';
    createTime?: string;
    customerId?: number;
    customerNumber?: string;
    expensePerson?: string;
    expenseTime?: string;
    id: number;
    number?: string;
    position?: string;
    reason?: string;
  };

  type GrantResourcesToCustomerRequest = {
    customerId: number;
    resourceIds?: number[];
  };

  type GrantResourcesToUserRequest = {
    resourceWithOperateIds?: string[];
    userId: number;
  };

  type IndustryVO = {
    hasLeaf?: boolean;
    id?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
  };

  type Item = {
    amount: number;
    lendingDirection: 'BORROW' | 'DEFAULT' | 'LOAN';
    subjectId: number;
    subjectName: string;
    subjectNumber: string;
    summary: string;
  };

  type Item0 = {
    amount?: number;
    creditAmount?: number;
    debitAmount?: number;
    id?: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    subjectId?: number;
    subjectName?: string;
    summary?: string;
  };

  type ItemAttachment = {
    id?: number;
    name?: string;
    remark?: string;
    url?: string;
  };

  type ItemSubsidy = {
    amount?: number;
    amountForDay?: number;
    days?: number;
    id?: number;
    subject?: Record<string, any>;
  };

  type Json = true;

  type ProxyCustomer = {
    id?: number;
    name?: string;
    number?: string;
  };

  type R = {
    data?: Record<string, any>;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type ResourceIdentifiedVO = {
    hasLeaf?: boolean;
    id?: number;
    number?: string;
  };

  type SubjectVO = {
    assistSettlement?: 'BANK' | 'CUSTOMER' | 'EMPLOYEE' | 'NOTHING' | 'SUPPLIER';
    hasLeaf?: boolean;
    id?: number;
    industry?: string;
    industryId?: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    level?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
    type?: 'COST' | 'SUBJECT' | 'SUBJECT_AND_COST';
  };

  type TreeCustomerCategoryVO = {
    children?: TreeCustomerCategoryVO[];
    hasLeaf?: boolean;
    id?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
  };

  type TreeIndustryVO = {
    children?: TreeIndustryVO[];
    hasLeaf?: boolean;
    id?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
  };

  type TreeResourceVO = {
    children?: TreeResourceVO[];
    hasLeaf?: boolean;
    id?: number;
    level?: number;
    name?: string;
    number?: string;
    parentNumber?: string;
    permitCode?: string;
    sortNum?: number;
    type?: 'DATA_SCOPE' | 'MENU' | 'PERMIT';
    url?: string;
  };

  type TreeResourceWithOperateVO = {
    children?: TreeResourceWithOperateVO[];
    id?: string;
    name?: string;
    parentId?: string;
    sortNum?: number;
  };

  type TreeSubjectVO = {
    assistSettlement?: 'BANK' | 'CUSTOMER' | 'EMPLOYEE' | 'NOTHING' | 'SUPPLIER';
    children?: TreeSubjectVO[];
    hasLeaf?: boolean;
    id?: number;
    industry?: string;
    industryId?: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    level?: number;
    name?: string;
    number?: string;
    parentId?: number;
    parentNumber?: string;
    remark?: string;
    type?: 'COST' | 'SUBJECT' | 'SUBJECT_AND_COST';
  };

  type UpdateCurrencyRequest = {
    id: number;
    name: string;
    number: string;
    rate: number;
    remark?: string;
  };

  type UpdateCustomerCategoryRequest = {
    id: number;
    name: string;
    remark?: string;
  };

  type UpdateIndustryRequest = {
    id: number;
    name: string;
    remark?: string;
  };

  type UpdateSelfPasswordRequest = {
    newPassword: string;
    oldPassword: string;
  };

  type UpdateSubjectRequest = {
    assistSettlement?: 'BANK' | 'CUSTOMER' | 'EMPLOYEE' | 'NOTHING' | 'SUPPLIER';
    id: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    name: string;
    remark?: string;
    type: 'COST' | 'SUBJECT' | 'SUBJECT_AND_COST';
  };

  type UpdateUserPasswordRequest = {
    id: number;
    password: string;
  };

  type UpdateUserRequest = {
    id: number;
    name: string;
  };

  type UpdateVoucherRequest = {
    attachmentNum?: number;
    currencyId: number;
    currencyName: string;
    deletedItemIds?: number[];
    id: number;
    items?: Item[];
    rate: number;
    serialNumber?: number;
    unit: string;
    voucherDate: string;
  };

  type UserCueVO = {
    account?: string;
    id?: number;
    name?: string;
  };

  type UserListVO = {
    account?: string;
    createBy?: number;
    createTime?: string;
    customerId?: number;
    customerName?: string;
    customerNumber?: string;
    id?: number;
    modifyBy?: number;
    modifyTime?: string;
    name?: string;
  };

  type UserOwnedMenuVO = {
    children?: UserOwnedMenuVO[];
    icon?: string;
    key?: string;
    locale?: boolean;
    name?: string;
    parentKey?: string;
    path?: string;
  };

  type UserSelfVO = {
    account?: string;
    createBy?: number;
    createTime?: string;
    customerId?: number;
    customerNumber?: string;
    id?: number;
    modifyBy?: number;
    modifyTime?: string;
    name?: string;
    proxyCustomer?: ProxyCustomer;
    role?: 'ADMIN' | 'APPROVER' | 'NORMAL' | 'OFFICER';
  };

  type VoucherBookVO = {
    creditAmount?: number;
    debitAmount?: number;
    id?: number;
    lendingDirection?: 'BORROW' | 'DEFAULT' | 'LOAN';
    serialNumber?: number;
    subjectName?: string;
    subjectNumber?: string;
    summary?: string;
    voucherDate?: string;
    yearMonthNum?: number;
  };

  type VoucherDetailVO = {
    attachmentNum?: number;
    currencyId?: number;
    currencyName?: string;
    id?: number;
    items?: Item0[];
    rate?: number;
    unit?: string;
    voucherDate?: string;
  };

  type VoucherPrintContentVO = {
    attachmentNum?: number;
    currencyId?: number;
    currencyName?: string;
    customerName?: string;
    id?: number;
    items?: Item0[];
    serialNumber?: number;
    totalCurrencyAmount?: number;
    totalLocalCurrencyAmount?: number;
    unit?: string;
    voucherTime?: string;
  };

  type VoucherVO = {
    attachmentNum?: number;
    auditStatus?: 'AUDITED' | 'TO_BE_AUDITED';
    bookkeeping?: boolean;
    creatorName?: string;
    currencyName?: string;
    currencyType?: 'FOREIGN' | 'LOCAL';
    current?: number;
    customerNumber?: string;
    id?: number;
    pageSize?: number;
    serialNumber?: number;
    unit?: string;
    voucherTime?: string;
    yearMonthNum?: string;
  };

  type RPageCurrencyVO_ = {
    current?: number;
    data?: CurrencyVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageCustomerListVO_ = {
    current?: number;
    data?: CustomerListVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageExpenseBillVO_ = {
    current?: number;
    data?: ExpenseBillVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageUserListVO_ = {
    current?: number;
    data?: UserListVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageVoucherBookVO_ = {
    current?: number;
    data?: VoucherBookVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageVoucherVO_ = {
    current?: number;
    data?: VoucherVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RCurrencyVO_ = {
    data?: CurrencyVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RExpenseBillDetailVO_ = {
    data?: ExpenseBillDetailVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RExpenseBillPrintContentVO_ = {
    data?: ExpenseBillPrintContentVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListCurrencyVO_ = {
    data?: CurrencyVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListCustomerCategoryVO_ = {
    data?: CustomerCategoryVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListCustomerCueVO_ = {
    data?: CustomerCueVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListIndustryVO_ = {
    data?: IndustryVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListResourceIdentifiedVO_ = {
    data?: ResourceIdentifiedVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListSubjectVO_ = {
    data?: SubjectVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListTreeCustomerCategoryVO_ = {
    data?: TreeCustomerCategoryVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListTreeIndustryVO_ = {
    data?: TreeIndustryVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListTreeResourceVO_ = {
    data?: TreeResourceVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListTreeResourceWithOperateVO_ = {
    data?: TreeResourceWithOperateVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListTreeSubjectVO_ = {
    data?: TreeSubjectVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListUserCueVO_ = {
    data?: UserCueVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListUserListVO_ = {
    data?: UserListVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListUserOwnedMenuVO_ = {
    data?: UserOwnedMenuVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListString_ = {
    data?: string[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RUserSelfVO_ = {
    data?: UserSelfVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RVoucherDetailVO_ = {
    data?: VoucherDetailVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RVoucherPrintContentVO_ = {
    data?: VoucherPrintContentVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RInt_ = {
    data?: number;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RString_ = {
    data?: string;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type auditingCurrencyUsingPUTParams = {
    /** id */
    id: number;
  };

  type deleteCurrencyUsingDELETEParams = {
    /** id */
    id: number;
  };

  type currencyByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type currencyOfYearMonthUsingGETParams = {
    /** yearMonthNum */
    yearMonthNum?: number;
  };

  type pageCurrencyUsingGETParams = {
    current?: number;
    pageSize?: number;
    yearMonthNum?: number;
  };

  type unAuditingCurrencyUsingPUTParams = {
    /** id */
    id: number;
  };

  type addCustomerUsingPOSTParams = {
    bankAccount?: string;
    bankAccountName?: string;
    businessUserId?: number;
    categoryId: number;
    contactName?: string;
    effectTime?: string;
    enabled?: boolean;
    expireTime?: string;
    industryId: number;
    name: string;
    number: string;
    remark?: string;
    telephone?: string;
    type: 'PROXY' | 'RENT' | 'RENT_AND_PROXY';
    useForeignExchange: boolean;
  };

  type deleteCustomerUsingDELETEParams = {
    /** id */
    id: number;
  };

  type pageCustomerUsingGETParams = {
    bankAccount?: string;
    bankAccountName?: string;
    categoryId?: number;
    current?: number;
    industryId?: number;
    name?: string;
    number?: string;
    pageSize?: number;
    status?: 'INITIALIZING' | 'SUCCESS';
    telephone?: string;
    type?: 'PROXY' | 'RENT' | 'RENT_AND_PROXY';
    useForeignExchange?: boolean;
    userAccount?: string;
  };

  type searchCustomerCueUsingGETParams = {
    keyword?: string;
    num?: number;
  };

  type updateCustomerUsingPUTParams = {
    bankAccount?: string;
    bankAccountName?: string;
    businessUserId?: number;
    effectTime?: string;
    enabled?: boolean;
    expireTime?: string;
    id: number;
    industryId: number;
    name: string;
    remark?: string;
    telephone?: string;
    type: 'PROXY' | 'RENT' | 'RENT_AND_PROXY';
    useForeignExchange: boolean;
  };

  type resourceIdsOfCustomerUsingGETParams = {
    /** customerId */
    customerId: number;
  };

  type treeResourceOfCustomerUsingGETParams = {
    /** customerId */
    customerId: number;
  };

  type treeResourceWithOperateUsingGETParams = {
    /** customerId */
    customerId: number;
  };

  type deleteCustomerCategoryUsingDELETEParams = {
    /** id */
    id: number;
  };

  type addExpenseBillUsingPOSTParams = {
    expensePerson: string;
    expenseTime: string;
    'items[0].actualAmount': number;
    'items[0].attachments[0].name': string;
    'items[0].attachments[0].remark'?: string;
    'items[0].beginTime': string;
    'items[0].billAmount': number;
    'items[0].endTime': string;
    'items[0].name': string;
    'items[0].numOfBill': number;
    'items[0].remark'?: string;
    'items[0].subjectId': number;
    'items[0].subsidies[0].amount': number;
    'items[0].subsidies[0].amountForDay': number;
    'items[0].subsidies[0].days': number;
    'items[0].subsidies[0].name': string;
    'items[0].subsidies[0].subjectId': number;
    'items[0].subsidyAmount': number;
    'items[0].subtotalAmount': number;
    'items[0].summary': string;
    'items[0].travelPlace': string;
    number?: string;
    position: string;
    reason: string;
    totalSubsidyAmount: number;
  };

  type auditingExpenseBillUsingPUTParams = {
    /** id */
    id: number;
  };

  type deleteExpenseBillUsingDELETEParams = {
    /** id */
    id: number;
  };

  type expenseBillByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type pageExpenseBillUsingGETParams = {
    current?: number;
    number?: string;
    pageSize?: number;
  };

  type searchExpenseBillCueUsingGETParams = {
    column?: 'REASON';
    keyword?: string;
    num?: number;
  };

  type searchExpenseItemCueUsingGETParams = {
    column?: 'REMARK' | 'SUMMARY' | 'TRAVEL_PLACE';
    keyword?: string;
    num?: number;
  };

  type unAuditingExpenseBillUsingPUTParams = {
    /** id */
    id: number;
  };

  type updateExpenseBillUsingPUTParams = {
    deletedAttachmentIds?: number[];
    deletedItemIds?: number[];
    deletedSubsidyIds?: number[];
    expensePerson: string;
    expenseTime: string;
    id: number;
    'items[0].actualAmount': number;
    'items[0].attachments[0].id'?: number;
    'items[0].attachments[0].name': string;
    'items[0].attachments[0].remark'?: string;
    'items[0].beginTime': string;
    'items[0].billAmount': number;
    'items[0].endTime': string;
    'items[0].id'?: number;
    'items[0].name': string;
    'items[0].numOfBill': number;
    'items[0].remark'?: string;
    'items[0].subjectId': number;
    'items[0].subsidies[0].amount': number;
    'items[0].subsidies[0].amountForDay': number;
    'items[0].subsidies[0].days': number;
    'items[0].subsidies[0].id'?: number;
    'items[0].subsidies[0].name': string;
    'items[0].subsidies[0].subjectId': number;
    'items[0].subsidyAmount': number;
    'items[0].subtotalAmount': number;
    'items[0].summary': string;
    'items[0].travelPlace': string;
    position: string;
    reason: string;
    totalSubsidyAmount: number;
  };

  type printContentOfExpenseBillUsingGETParams = {
    /** id */
    id: number;
  };

  type deleteIndustryUsingDELETEParams = {
    /** id */
    id: number;
  };

  type persignedObjectUrlUsingGETParams = {
    bucket: 'EXPENSE_BILL';
    fileExtend?: string;
    fileName: string;
    objectName?: string;
  };

  type deleteSubjectUsingDELETEParams = {
    /** id */
    id: number;
  };

  type listSubjectUsingGETParams = {
    industryId?: number;
    name?: string;
    number?: string;
  };

  type treeSubjectUsingGETParams = {
    industryId?: number;
    name?: string;
    number?: string;
  };

  type deleteUserUsingDELETEParams = {
    /** id */
    id: number;
  };

  type listUserUsingGETParams = {
    account?: string;
    current?: number;
    customerName?: string;
    customerNumber?: string;
    name?: string;
    pageSize?: number;
    role?: 'ADMIN' | 'APPROVER' | 'NORMAL' | 'OFFICER';
  };

  type pageUserUsingGETParams = {
    account?: string;
    current?: number;
    customerName?: string;
    customerNumber?: string;
    name?: string;
    pageSize?: number;
    role?: 'ADMIN' | 'APPROVER' | 'NORMAL' | 'OFFICER';
  };

  type searchUserCueUsingGETParams = {
    keyword?: string;
  };

  type switchProxyCustomerUsingPUTParams = {
    /** Authorization */
    Authorization: string;
    /** customerId */
    customerId: number;
  };

  type resourceIdsOfUserUsingGETParams = {
    /** userId */
    userId: number;
  };

  type batchAuditingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type auditingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type batchBookkeepingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type bookkeepingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type deleteVoucherUsingDELETEParams = {
    /** id */
    id: number;
  };

  type voucherDetailUsingGETParams = {
    /** id */
    id: number;
  };

  type pageVoucherUsingGETParams = {
    currencyType?: 'FOREIGN' | 'LOCAL';
    current?: number;
    pageSize?: number;
    yearMonthNum?: number;
  };

  type pageVoucherBookUsingGETParams = {
    current?: number;
    pageSize?: number;
    yearMonthNum?: number;
  };

  type searchVoucherCueUsingGETParams = {
    column?: 'SUMMARY';
    keyword?: string;
    num?: number;
  };

  type batchUnAuditingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type unAuditingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type batchUnBookkeepingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type unBookkeepingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type usableSerialNumberUsingGETParams = {
    /** yearMonthNum */
    yearMonthNum?: number;
  };

  type printContentOfVoucherUsingGETParams = {
    /** id */
    id: number;
  };
}
