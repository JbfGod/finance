declare namespace API {
  type accountBalanceUsingGETParams = {
    yearMonth: string;
  };

  type AccountBalanceVO = {
    creditAnnualAmount?: number;
    creditClosingAmount?: number;
    creditCurrentAmount?: number;
    creditOpeningAmount?: number;
    debitAnnualAmount?: number;
    debitClosingAmount?: number;
    debitCurrentAmount?: number;
    debitOpeningAmount?: number;
    subjectId?: number;
    subjectName?: string;
    subjectNumber?: string;
  };

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

  type addExpenseBillUsingPOSTParams = {
    expensePerson: string;
    expenseTime: string;
    'items[0].actualAmount': number;
    'items[0].attachments[0].name': string;
    'items[0].attachments[0].remark'?: string;
    'items[0].beginTime': string;
    'items[0].billAmount': number;
    'items[0].endTime': string;
    'items[0].numOfBill': number;
    'items[0].remark'?: string;
    'items[0].subjectId': number;
    'items[0].subjectNumber': string;
    'items[0].subsidies[0].amount': number;
    'items[0].subsidies[0].amountForDay': number;
    'items[0].subsidies[0].days': number;
    'items[0].subsidies[0].subjectId': number;
    'items[0].subsidies[0].subjectNumber': string;
    'items[0].subsidyAmount'?: number;
    'items[0].subtotalAmount'?: number;
    'items[0].summary': string;
    'items[0].travelPlace': string;
    number?: string;
    position: string;
    reason: string;
  };

  type AddIndustryRequest = {
    name: string;
    number: string;
    parentId: number;
    remark?: string;
  };

  type AddInitialBalanceRequest = {
    amount: number;
    currencyName: string;
    lendingDirection: 'BORROW' | 'DEFAULT' | 'LOAN';
    localAmount: number;
    subjectId: number;
    subjectNumber: string;
    yearMonthDate: string;
  };

  type AddSubjectRequest = {
    assistSettlement?: 'BANK' | 'CUSTOMER' | 'EMPLOYEE' | 'NOTHING' | 'SUPPLIER';
    industryId?: number;
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
    role: 'ADMIN' | 'ADVANCED_APPROVER' | 'NORMAL' | 'NORMAL_APPROVER';
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

  type ApprovalFlowItemVO = {
    approvalFlowId?: number;
    approverIds?: number[];
    department?: string;
    id?: number;
    lasted?: boolean;
    level?: number;
  };

  type ApprovalInstanceItemVO = {
    approvalTime?: string;
    approver?: string;
    approverId?: number;
    canApproved?: boolean;
    id?: number;
    lasted?: boolean;
    level?: number;
    passed?: boolean;
    remark?: string;
  };

  type approvalInstanceUsingGETParams = {
    /** id */
    id: number;
  };

  type ApprovalInstanceVO = {
    currentLevel?: number;
    ended?: boolean;
    items?: ApprovalInstanceItemVO[];
  };

  type ApprovedRequest = {
    approvalInstanceItemId: number;
    moduleRecordId: number;
    remark?: string;
  };

  type auditingCurrencyUsingPUTParams = {
    /** id */
    id: number;
  };

  type auditingExpenseBillUsingPUTParams = {
    /** id */
    id: number;
  };

  type auditingInitialBalanceUsingPUTParams = {
    yearMonthDate: string;
  };

  type auditingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type batchAuditingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type batchBookkeepingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type batchUnAuditingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type batchUnBookkeepingVoucherUsingPUTParams = {
    beginSerialNum?: number;
    endSerialNum?: number;
    yearMonth: number;
  };

  type bookkeepingInitialBalanceUsingPUTParams = {
    /** id */
    id: number;
  };

  type bookkeepingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type cancelClosedAccountUsingPOSTParams = {
    yearMonthDate: string;
    yearMonthNum?: number;
  };

  type closeAccountUsingPOSTParams = {
    beginDate?: string;
    endDate?: string;
    year?: number;
    yearMonthDate: string;
    yearMonthNum?: number;
  };

  type CopyCurrencyRequest = {
    isOverride?: boolean;
    sourceYearMonth?: number;
    targetYearMonth?: number;
  };

  type currencyByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type currencyOfYearMonthUsingGETParams = {
    /** yearMonthNum */
    yearMonthNum?: number;
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

  type Customer = {
    id?: number;
    name?: string;
    number?: string;
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

  type dailyBankUsingGETParams = {
    currencyName?: string;
    subjectId?: number;
    voucherDate: string;
  };

  type DailyBankVO = {
    creditTotal?: number;
    currency?: string;
    debitTotal?: number;
    id?: string;
    key?: string;
    localBalanceOfToday?: number;
    localBalanceOfYesterday?: number;
    localCreditAmountOfToday?: number;
    localDebitAmountOfToday?: number;
    subjectId?: number;
    subjectName?: string;
    subjectNumber?: string;
    summary?: string;
  };

  type dailyCashUsingGETParams = {
    currencyName?: string;
    subjectId?: number;
    voucherDate: string;
  };

  type DailyCashVO = {
    balanceOfToday?: number;
    balanceOfYesterday?: number;
    creditAmountOfToday?: number;
    creditTotal?: number;
    currency?: string;
    debitAmountOfToday?: number;
    debitTotal?: number;
    id?: string;
    key?: string;
    localBalanceOfToday?: number;
    localBalanceOfYesterday?: number;
    localCreditAmountOfToday?: number;
    localDebitAmountOfToday?: number;
    subjectId?: number;
    subjectName?: string;
    subjectNumber?: string;
  };

  type deleteCurrencyUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteCustomerCategoryUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteCustomerUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteExpenseBillUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteFlowItemUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteIndustryUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteInitialBalanceUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteSubjectUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteUserUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteVoucherUsingDELETEParams = {
    /** id */
    id: number;
  };

  type expenseBillByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type ExpenseBillDetailVO = {
    approvalFlowInstanceId?: number;
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
    auditStatus?: 'APPROVED' | 'AUDITED' | 'TO_BE_AUDITED';
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

  type FlowItem = {
    approverIds: number[];
    department?: string;
  };

  type flowItemsUsingGETParams = {
    businessModule: 'EXPENSE_BILL';
    customerId: number;
  };

  type generalLedgerUsingGETParams = {
    currencyName: string;
    endMonth: string;
    startMonth: string;
  };

  type GeneralLedgerVO = {
    balance?: number;
    creditAmount?: number;
    debitAmount?: number;
    id?: string;
    lendingDirection?: '借' | '平' | '贷';
    maxVoucherNumber?: number;
    month?: number;
    rowSpan?: number;
    subjectId?: number;
    subjectName?: string;
    subjectNumber?: string;
    summary?: string;
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

  type InitialBalanceItemVO = {
    amount?: number;
    currencyName?: string;
    id?: number;
    initialBalanceId?: number;
    lendingDirection?: string;
    subjectId?: number;
    subjectName?: string;
    subjectNumber?: string;
    year?: number;
    yearMonthNum?: number;
  };

  type InitialBalanceVO = {
    auditStatus?: 'APPROVED' | 'AUDITED' | 'TO_BE_AUDITED';
    bookkeeping?: boolean;
    creatorName?: string;
    id?: number;
    yearMonthDate?: string;
    yearMonthNum?: number;
  };

  type Item = {
    creditAmount?: number;
    debitAmount?: number;
    subjectId: number;
    subjectNumber: string;
    summary: string;
  };

  type Item0 = {
    creditAmount?: number;
    debitAmount?: number;
    id?: number;
    serialNumber?: number;
    subjectId?: number;
    subjectName?: string;
    summary?: string;
  };

  type ItemAttachment = {
    id?: number;
    name?: string;
    remark?: string;
    serialNumber?: number;
    url?: string;
  };

  type ItemSubsidy = {
    amount?: number;
    amountForDay?: number;
    days?: number;
    id?: number;
    name?: string;
    serialNumber?: number;
    subjectId?: number;
  };

  type Json = true;

  type listSubjectUsingGETParams = {
    current?: number;
    industryId?: number;
    name?: string;
    number?: string;
    pageSize?: number;
  };

  type OwnedApprovalCustomerVO = {
    id?: number;
    name?: string;
    number?: string;
  };

  type ownedCustomerUsingGETParams = {
    customerName?: string;
    customerNumber?: string;
  };

  type pageCanApprovedExpenseBillUsingGETParams = {
    auditStatus?: 'APPROVED' | 'AUDITED';
    current?: number;
    customerId?: number;
    endDate?: string;
    number?: string;
    pageSize?: number;
    startDate?: string;
  };

  type pageCurrencyUsingGETParams = {
    current?: number;
    pageSize?: number;
    yearMonthNum?: number;
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
    telephone?: string;
    type?: 'PROXY' | 'RENT' | 'RENT_AND_PROXY';
    useForeignExchange?: boolean;
    userAccount?: string;
  };

  type pageExpenseBillUsingGETParams = {
    auditStatus?: 'APPROVED' | 'AUDITED';
    current?: number;
    customerId?: number;
    endDate?: string;
    number?: string;
    pageSize?: number;
    startDate?: string;
  };

  type pageInitialBalanceItemUsingGETParams = {
    current?: number;
    pageSize?: number;
  };

  type pageSubjectUsingGETParams = {
    current?: number;
    industryId?: number;
    name?: string;
    number?: string;
    pageSize?: number;
  };

  type pageUserUsingGETParams = {
    account?: string;
    current?: number;
    customerName?: string;
    customerNumber?: string;
    name?: string;
    pageSize?: number;
    role?: 'ADMIN' | 'ADVANCED_APPROVER' | 'NORMAL' | 'NORMAL_APPROVER';
  };

  type pageVoucherBookUsingGETParams = {
    current?: number;
    pageSize?: number;
    yearMonthNum?: number;
  };

  type pageVoucherUsingGETParams = {
    currencyType?: 'FOREIGN' | 'LOCAL';
    current?: number;
    endDate?: string;
    pageSize?: number;
    serialNumber?: number;
    startDate?: string;
  };

  type persignedObjectUrlUsingGETParams = {
    bucket: 'EXPENSE_BILL';
    fileExtend?: string;
    fileName: string;
    objectName?: string;
  };

  type printContentOfExpenseBillUsingGETParams = {
    /** id */
    id: number;
  };

  type printContentOfVoucherUsingGETParams = {
    /** id */
    id: number;
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

  type RApprovalInstanceVO_ = {
    data?: ApprovalInstanceVO;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
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

  type ResourceIdentifiedVO = {
    hasLeaf?: boolean;
    id?: number;
    number?: string;
  };

  type resourceIdsOfCustomerUsingGETParams = {
    /** customerId */
    customerId: number;
  };

  type resourceIdsOfUserUsingGETParams = {
    /** userId */
    userId: number;
  };

  type ReviewRejectedRequest = {
    approvalInstanceItemId: number;
    moduleRecordId: number;
    previousApprovalInstanceItemId?: number;
    remark?: string;
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

  type RInitialBalanceVO_ = {
    data?: InitialBalanceVO;
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

  type RListAccountBalanceVO_ = {
    data?: AccountBalanceVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListApprovalFlowItemVO_ = {
    data?: ApprovalFlowItemVO[];
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

  type RListDailyBankVO_ = {
    data?: DailyBankVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListDailyCashVO_ = {
    data?: DailyCashVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RListGeneralLedgerVO_ = {
    data?: GeneralLedgerVO[];
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

  type RListOwnedApprovalCustomerVO_ = {
    data?: OwnedApprovalCustomerVO[];
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

  type RListVoucherItemVO_ = {
    data?: VoucherItemVO[];
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
  };

  type RLocalDate_ = {
    data?: string;
    errorCode?: string;
    host?: string;
    message?: string;
    redirectUrl?: string;
    showType?: number;
    success?: boolean;
    traceId?: string;
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

  type RPageInitialBalanceItemVO_ = {
    current?: number;
    data?: InitialBalanceItemVO[];
    pageSize?: number;
    success?: boolean;
    total?: number;
  };

  type RPageSubjectVO_ = {
    current?: number;
    data?: SubjectVO[];
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

  type SaveApprovalFlowRequest = {
    businessModule: 'EXPENSE_BILL';
    customerId: number;
    flowItems: FlowItem[];
  };

  type searchCustomerCueUsingGETParams = {
    keyword?: string;
    num?: number;
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

  type searchVoucherCueUsingGETParams = {
    column?: 'SUMMARY';
    keyword?: string;
    num?: number;
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

  type subLedgerUsingGETParams = {
    currencyName: string;
    endMonth: string;
    startMonth: string;
    subjectId?: number;
  };

  type switchProxyCustomerUsingPUTParams = {
    /** Authorization */
    Authorization: string;
    /** customerId */
    customerId?: number;
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
    disabled?: boolean;
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

  type treeResourceWithOperateUsingGETParams = {
    /** customerId */
    customerId: number;
  };

  type TreeResourceWithOperateVO = {
    children?: TreeResourceWithOperateVO[];
    disabled?: boolean;
    id?: string;
    name?: string;
    parentId?: string;
    sortNum?: number;
  };

  type unAuditingCurrencyUsingPUTParams = {
    /** id */
    id: number;
  };

  type unAuditingExpenseBillUsingPUTParams = {
    /** id */
    id: number;
  };

  type unAuditingInitialBalanceUsingPUTParams = {
    /** id */
    id: number;
  };

  type unAuditingVoucherUsingPUTParams = {
    /** id */
    id: number;
  };

  type unBookkeepingInitialBalanceUsingPUTParams = {
    /** id */
    id: number;
  };

  type unBookkeepingVoucherUsingPUTParams = {
    /** id */
    id: number;
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
    'items[0].numOfBill': number;
    'items[0].remark'?: string;
    'items[0].subjectId': number;
    'items[0].subjectNumber': string;
    'items[0].subsidies[0].amount': number;
    'items[0].subsidies[0].amountForDay': number;
    'items[0].subsidies[0].days': number;
    'items[0].subsidies[0].id'?: number;
    'items[0].subsidies[0].subjectId': number;
    'items[0].subsidies[0].subjectNumber': string;
    'items[0].subsidyAmount'?: number;
    'items[0].subtotalAmount'?: number;
    'items[0].summary': string;
    'items[0].travelPlace': string;
    position: string;
    reason: string;
  };

  type UpdateIndustryRequest = {
    id: number;
    name: string;
    remark?: string;
  };

  type UpdateInitialBalanceRequest = {
    amount: number;
    currencyId: number;
    currencyName: string;
    id: number;
    lendingDirection: 'BORROW' | 'DEFAULT' | 'LOAN';
    rate: number;
    subjectId: number;
    subjectNumber: string;
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

  type usableSerialNumberUsingGETParams = {
    /** yearMonthNum */
    yearMonthNum?: number;
  };

  type UserListVO = {
    account?: string;
    customerId?: number;
    customerName?: string;
    customerNumber?: string;
    id?: number;
    name?: string;
    role?: 'ADMIN' | 'ADVANCED_APPROVER' | 'NORMAL' | 'NORMAL_APPROVER';
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
    customer?: Customer;
    customerId?: number;
    customerNumber?: string;
    id?: number;
    modifyBy?: number;
    modifyTime?: string;
    name?: string;
    proxyCustomer?: Customer;
    role?: 'ADMIN' | 'ADVANCED_APPROVER' | 'NORMAL' | 'NORMAL_APPROVER';
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

  type voucherDetailUsingGETParams = {
    /** id */
    id: number;
  };

  type VoucherDetailVO = {
    attachmentNum?: number;
    currencyId?: number;
    currencyName?: string;
    expenseBillId?: number;
    id?: number;
    items?: Item0[];
    rate?: number;
    serialNumber?: number;
    source?: 'EXPENSE_BILL' | 'MANUAL';
    unit?: string;
  };

  type voucherItemBySubjectUsingGETParams = {
    subjectId: number;
    yearMonth: string;
    yearMonthNum?: number;
  };

  type VoucherItemVO = {
    id?: number;
    localCreditAmount?: number;
    localDebitAmount?: number;
    summary?: string;
    voucherDate?: string;
    voucherId?: number;
    voucherNumber?: number;
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
    auditStatus?: 'APPROVED' | 'AUDITED' | 'TO_BE_AUDITED';
    bookkeeping?: boolean;
    creatorName?: string;
    currencyName?: string;
    currencyType?: 'FOREIGN' | 'LOCAL';
    current?: number;
    customerNumber?: string;
    expenseBillId?: number;
    id?: number;
    pageSize?: number;
    serialNumber?: number;
    source?: 'EXPENSE_BILL' | 'MANUAL';
    unit?: string;
    voucherDate?: string;
    yearMonthNum?: string;
  };
}
