export interface Customer {
  id?: number;
  name: string;
  email: string;
  createdBy?: string;
}

export interface BankAccount {
  type: string;            // 'CurrentAccount' | 'SavingAccount'
  id: string;
  balance: number;
  status: string;
  currency: string;
  createdDate: string;
  customerDTO: Customer;
  overDraft?: number;      // current account
  interestRate?: number;   // saving account
}

export interface AccountOperation {
  id: number;
  operationDate: string;
  amount: number;
  type: 'DEBIT' | 'CREDIT';
  description: string;
  performedBy: string;
}

export interface AccountHistory {
  accountId: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  accountOperationDTOS: AccountOperation[];
}
