# Finance Manager 📱

A native Android mobile application designed to help **Micro, Small and Medium Enterprises (MSMEs)** manage accounting, asset lifecycle management, and budgeting.

> 🚧 **Project Status:** Accounting Module MVP completed. Assets and Budgeting modules are planned for future development.

---

## 📌 Overview

Finance Manager is an Android application focused on simplifying core financial management activities for MSMEs.

The current MVP allows users to record accounting transactions and automatically generate fundamental financial reports from the stored transaction data.

---

## ✨ Features

### Accounting Module

- Add accounting transaction entries
- Store transaction data locally
- Automatic Ledger generation
- Automatic Trial Balance generation
- Automatic Income Statement generation
- Automatic Balance Sheet generation
- Journal Entries
- Transaction History
- Chart of Accounts
- T-Account representation
- Post-Trial Balance workflow

### Planned Modules

- Asset Lifecycle Management
- Budgeting

---

## 🧭 App Structure

The application is organized into four primary sections through a bottom navigation bar:

- 🏠 Home
- 📊 Accounting
- 🏢 Assets
- 💰 Budgeting

### Accounting Section

The Accounting module contains:

- Accounting dashboard
- Journal
- Ledger
- Reports
- Transaction History
- Chart of Accounts
- T-Accounts
- Trial Balance
- Income Statement
- Balance Sheet

The screens use Jetpack Compose components such as `LazyColumn`, `LazyGrid`, and `ExposedDropdownMenu`.

---

## 🏗️ Architecture

The project follows **MVVM with Clean Architecture principles**, separating application responsibilities into presentation, domain, and data layers.

### Presentation Layer

- ViewModels
- Jetpack Compose screens
- UI state handling
- Type-safe Navigation

### Domain Layer

- Domain models
- Use Cases
- Repository interfaces
- Business logic

### Data Layer

- Room Database
- Room Entities
- DAOs
- Repository implementations
- Dependency Injection using Hilt
- Database migrations

This separation keeps business logic independent from UI and data-storage implementations.

---

## 🛠️ Tech Stack

| Technology               | Usage                        |
|--------------------------|------------------------------|
| **Kotlin**               | Primary programming language |
| **Jetpack Compose**      | Declarative UI framework     |
| **Android SDK**          | Native Android development   |
| **Room**                 | Local database / persistence |
| **Hilt**                 | Dependency Injection         |
| **Coroutines**           | Asynchronous programming     |
| **Type-safe Navigation** | Application navigation       |
| **Git & GitHub**         | Version control              |

---

## 🧠 Key Concepts Learned

This project provided practical experience with:

- MVVM architecture
- Clean Architecture principles
- Hilt Dependency Injection
- Room Database
- DAO implementation
- Repository pattern
- Database migrations
- SQLite schema changes
- Type-safe Navigation
- Jetpack Compose UI development
- `LazyColumn`
- `LazyGrid`
- `ExposedDropdownMenu`
- ViewModel and UI state management
- Separation of domain, data, and presentation responsibilities

---

# 📸 Screenshots

## 🏠 Home

![Home Screen](screenshots/HomeScreen.jpeg)

---

## 📊 Accounting

### Accounting Dashboard

![Accounting Main Screen](screenshots/AccountingMainScreen.jpeg)

### Recent Transactions

![Recent Transactions](screenshots/AccountingMainScreen.RecentTransaction.jpeg)

---

## 🧾 Transaction Management

### Add Transaction

![Add Transaction](screenshots/AddTransactionScreen.jpeg)

### Add Transaction — Additional View

![Add Transaction](screenshots/AddTransactionScreen2.jpeg)

### Transaction History

![Transaction History](screenshots/TransactionHistoryScreen.jpeg)

---

## 📚 Accounting Records

### Journal Entries

![Journal Entries](screenshots/JournalEntriesScreen.jpeg)

### Ledger

![Ledger](screenshots/LedgerScreen.jpeg)

### T-Account

![T-Account](screenshots/TAccountScreen.jpeg)

### Chart of Accounts

![Chart of Accounts](screenshots/CharOfAccountsScreen.jpeg)

### Chart of Accounts — Additional View

![Chart of Accounts](screenshots/ChartOfAccountsScreen2.jpeg)

---

## 📈 Financial Reports

### Reports

![Reports](screenshots/ReportScreen.jpeg)

### Trial Balance

![Trial Balance](screenshots/TrialBalanceScreen.jpeg)

### Income Statement

![Income Statement](screenshots/IncomeStatementScreen.jpeg)

### Income Statement — Additional View

![Income Statement](screenshots/IncomeStatementScreen2.jpeg)

### Balance Sheet

![Balance Sheet](screenshots/BalanceSheetScreen.jpeg)

### Balance Sheet — Additional View

![Balance Sheet](screenshots/BalanceSheetScreen2.jpeg)

---

## ⚙️ Accounting Workflow

### Post Trial Balance

![Post Trial Balance](screenshots/PostTrialBalancingButton.jpeg)

### Confirmation Dialog

![Post Trial Balance Confirmation](screenshots/PostTrialBalancingConfirmDialog.jpeg)

---

## 🏢 Other Modules

### Assets

![Assets](screenshots/AssetsScreen.jpeg)

### Budgeting

![Budgeting](screenshots/BudgetingScreen.jpeg)

---

# 🚀 Project Status

### Completed

- [x] Android project architecture
- [x] Bottom navigation
- [x] Type-safe navigation
- [x] Hilt Dependency Injection
- [x] Room Database integration
- [x] Database migration
- [x] Accounting data layer
- [x] Transaction entry
- [x] Journal Entries
- [x] Ledger
- [x] T-Accounts
- [x] Trial Balance
- [x] Income Statement
- [x] Balance Sheet
- [x] Transaction History
- [x] Chart of Accounts
- [x] Accounting Module MVP

### In Progress / Planned

- [ ] Asset Lifecycle Management
- [ ] Budgeting Module
- [ ] PDF export for financial reports
- [ ] Date-based transaction filtering
- [ ] UI/UX refinement
- [ ] Improved color system and themes
- [ ] REST API integration
- [ ] Remote data synchronization

---

# 🔮 Future Improvements

### Reporting

- Export financial statements as PDF
- Add date-wise transaction filtering
- Improve report presentation

### UI/UX

- Refine visual hierarchy
- Improve color and theme system
- Improve usability and consistency across screens

### Data & Backend

- REST API integration
- Remote data synchronization
- Cloud-based data storage

---

## 📚 Project Purpose

This project was developed as a practical Android development project to strengthen my understanding of:

**Kotlin → Jetpack Compose → Architecture → Local Persistence → Dependency Injection → Navigation → Financial application logic**

The project focuses on applying software engineering concepts to a realistic business-oriented application rather than building isolated practice exercises.

---

## 👨‍💻 Development

Built as a personal Android development project using Kotlin and modern Android development practices.