Finance Manager_android mobile app:

Description : *This app helps to manage Accounting, Asset Lifcycle management and Budgeting for the MSME (Micro Small Medium Enterprises).

Features of the app : (MVP ready) *User can add a transaction entry; *User can view automatic Ledger accounts, Trial balance, Income statement, and Balance sheet.

App Structure : *Home tab; *Accounting tab; *Assets tab; *Budgeting tab - all four inside Bottom navigation bar. *Accounting tab contains -> Main screen with analytics dashboard, top app bar with three different screens' text buttons (Journal, Ledger, Report). *Each screen is designed with Top app bar, Lazycolumn or LazyGrid. *Room database feature integrated for real storage of user's app data.

Tech stack : *Language - Kotlin.  *UI framework - Jetpack compose.  *Architecture - Clean architecture (MVVM). *Database - Room DB.

Architecture : *MVVM used, domain layer(model, usecase, repo interfaces); data layer(hilt setup, database model, dao, room repository); UI layer(viewmodel, screen classes, typesafe navigation).

Project status : Accounting module' MVP completed.

What learnt from this project journey : Hilt dependecy injection setup and usage, typesafe navigation, database migration in Sqlite, compose components such as LazyGrid and ExposedDropdownMenu.

Future improvements : *Adding Export menu inside Report screen to export the income statements as pdf and Date wise selection of entry and pending modules of Assets and Budgeting. *Improved UI with better colors and themes for UI components. *Rest API integration for remote data transfering.

### Screenshots
## [!Accounting Main] screenshots/AccountingMainScreen.jpeg
## [] screenshots/AccountingMainScreen.RecentTransaction.jpeg
## [] screenshots/AddTransactionScreen.jpeg
## [] screenshots/AddTransactionScreen2.jpeg
## [] screenshots/AssetsScreen.jpeg
## [] screenshots/BalanceSheetScreen.jpeg
## [] screenshots/BalanceSheetScreen2.jpeg
## [] screenshots/BudgetingScreen.jpeg
## [] screenshots/CharOfAccountsScreen.jpeg
## [] screenshots/ChartOfAccountsScreen2.jpeg
## [] screenshots/HomeScreen.jpeg
## [] screenshots/IncomeStatementScreen.jpeg
## [] screenshots/IncomeStatementScreen2.jpeg
## [] screenshots/JournalEntriesScreen.jpeg
## [] screenshots/LedgerScreen.jpeg
## [] screenshots/PostTrialBalancingButton.jpeg
## [] screenshots/PostTrialBalancingConfirmDialog.jpeg
## [] screenshots/ReportScreen.jpeg
## [] screenshots/TAccountScreen.jpeg
## [] screenshots/TransactionHistoryScreen.jpeg
## [] screenshots/TrialBalanceScreen.jpeg