<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="assets/banner-dark.svg">
    <img alt="Core Java by CodeChanakya" src="assets/banner-light.svg" width="100%">
  </picture>
</p>

<p align="center">
  <img alt="Java 21 or newer" src="https://img.shields.io/badge/Java-21%2B-B83A0E">
  <img alt="Programs ready" src="https://img.shields.io/badge/programs%20ready-38%20of%2043-0F766E">
  <img alt="License MIT" src="https://img.shields.io/badge/license-MIT-0E1726">
  <a href="https://github.com/codechanakya/core-java/actions/workflows/ci.yml"><img alt="CI" src="https://github.com/codechanakya/core-java/actions/workflows/ci.yml/badge.svg"></a>
</p>

<p align="center">
  <a href="https://codechanakya.github.io/core-java/"><b>Browse the site</b></a> &nbsp;·&nbsp;
  <a href="https://codechanakya.github.io/core-java/flows.html"><b>Flow gallery</b></a> &nbsp;·&nbsp;
  <a href="https://codespaces.new/codechanakya/core-java"><b>Open in Codespaces</b></a>
</p>

---

**Core Java** is a set of 43 Java programs for learners, from a first `System.out.println` to a concurrent order-processing system. Each program has a reading order, a sample run, exercises you attempt before you open the solution, and files with one planted bug so you meet compiler errors on your own terms.

It follows a fixed path: smallest building block first, then the next concept that depends on it, until you can design and test a small system yourself.

> **Status:** Phases 1 to 9 are ready (38 of 43 programs). Programs 9, 36 and 38 download the JUnit launcher, and Programs 37 and 38 download a small database driver, on their first run, so those need an internet connection once. The other programs are planned and appear below as they are built. Each ready program lists the Java version and platform it was run on.

## Start here

1. Install a JDK, version 21 or newer, and check it with `java -version` and `javac -version`.
2. Clone this repository.
3. Run the first program:

```bash
cd 01-getting-started/01-personal-profile-generator
./run.sh --check
```

You should see the profile card and then `OK: output matches expected-output.txt`.

On Windows, use Git Bash or WSL for the `run.sh` scripts, or run the two `javac` and `java` commands written in each program's README.

## The roadmap

```mermaid
flowchart LR
    P1["01 Getting Started"] --> P2["02 Core Fundamentals"]
    P2 --> P3["03 Object-Oriented Programming"]
    P3 --> P4["04 Java Essentials"]
    P4 --> P5["05 Collections"]
    P5 --> P6["06 Files, I/O and Date"]
    P6 --> P7["07 Modern Java"]
    P7 --> P8["08 Concurrency"]
    P8 --> P9["09 Professional Java"]
    P9 --> C["Capstones"]
```

Each phase ends with a project that uses everything taught before it. Frameworks such as Spring Boot come only after the Core Java foundation is solid.

<!--gallery-->
## See every program as a picture

Each ready program has a flow diagram in three layers: a story in everyday words, the numbered steps with the file where each one happens, and a note for experienced developers. The same picture sits at the top of every program README and on the [flow gallery](https://codechanakya.github.io/core-java/flows.html).

<table>
  <tr>
    <td align="center" valign="top"><a href="01-getting-started/01-personal-profile-generator"><img alt="Flow of Program 01, Personal Profile Generator, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-01.svg" width="290"></a><br><b>01 · Personal Profile Generator</b></td>
    <td align="center" valign="top"><a href="02-core-fundamentals/02-employee-salary-calculator"><img alt="Flow of Program 02, Employee Salary Calculator, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-02.svg" width="290"></a><br><b>02 · Employee Salary Calculator</b></td>
    <td align="center" valign="top"><a href="02-core-fundamentals/03-money-handling-mini-lab"><img alt="Flow of Program 03, Money Handling Mini-Lab, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-03.svg" width="290"></a><br><b>03 · Money Handling Mini-Lab</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="02-core-fundamentals/04-electricity-bill-generator"><img alt="Flow of Program 04, Electricity Bill Generator, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-04.svg" width="290"></a><br><b>04 · Electricity Bill Generator</b></td>
    <td align="center" valign="top"><a href="02-core-fundamentals/05-atm-menu-simulation"><img alt="Flow of Program 05, ATM Menu Simulation, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-05.svg" width="290"></a><br><b>05 · ATM Menu Simulation</b></td>
    <td align="center" valign="top"><a href="02-core-fundamentals/06-student-marks-analyzer"><img alt="Flow of Program 06, Student Marks Analyzer, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-06.svg" width="290"></a><br><b>06 · Student Marks Analyzer</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="02-core-fundamentals/07-shopping-cart-calculator"><img alt="Flow of Program 07, Shopping Cart Calculator (Phase 2 project), in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-07.svg" width="290"></a><br><b>07 · Shopping Cart Calculator (Phase 2 project)</b></td>
    <td align="center" valign="top"><a href="03-object-oriented-programming/08-banking-application"><img alt="Flow of Program 08, Banking Application, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-08.svg" width="290"></a><br><b>08 · Banking Application</b></td>
    <td align="center" valign="top"><a href="03-object-oriented-programming/09-junit-starter-lab"><img alt="Flow of Program 09, JUnit Starter Lab, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-09.svg" width="290"></a><br><b>09 · JUnit Starter Lab</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="03-object-oriented-programming/10-employee-management-system"><img alt="Flow of Program 10, Employee Management System, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-10.svg" width="290"></a><br><b>10 · Employee Management System</b></td>
    <td align="center" valign="top"><a href="03-object-oriented-programming/11-vehicle-rental-system"><img alt="Flow of Program 11, Vehicle Rental System, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-11.svg" width="290"></a><br><b>11 · Vehicle Rental System</b></td>
    <td align="center" valign="top"><a href="03-object-oriented-programming/12-payment-processing-system"><img alt="Flow of Program 12, Payment Processing System, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-12.svg" width="290"></a><br><b>12 · Payment Processing System</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="03-object-oriented-programming/13-ecommerce-order-management"><img alt="Flow of Program 13, E-commerce Order Management (Phase 3 project), in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-13.svg" width="290"></a><br><b>13 · E-commerce Order Management (Phase 3 project)</b></td>
    <td align="center" valign="top"><a href="04-java-essentials/14-order-status-management"><img alt="Flow of Program 14, Order Status Management, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-14.svg" width="290"></a><br><b>14 · Order Status Management</b></td>
    <td align="center" valign="top"><a href="04-java-essentials/15-banking-exception-management"><img alt="Flow of Program 15, Banking Exception Management, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-15.svg" width="290"></a><br><b>15 · Banking Exception Management</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="04-java-essentials/16-generic-repository"><img alt="Flow of Program 16, Generic Repository, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-16.svg" width="290"></a><br><b>16 · Generic Repository</b></td>
    <td align="center" valign="top"><a href="04-java-essentials/17-plugin-discovery-demonstration"><img alt="Flow of Program 17, Plugin Discovery Demonstration (Phase 4 project), in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-17.svg" width="290"></a><br><b>17 · Plugin Discovery Demonstration (Phase 4 project)</b></td>
    <td align="center" valign="top"><a href="05-collections/18-online-shopping-cart"><img alt="Flow of Program 18, Online Shopping Cart, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-18.svg" width="290"></a><br><b>18 · Online Shopping Cart</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="05-collections/19-customer-search"><img alt="Flow of Program 19, Customer Search, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-19.svg" width="290"></a><br><b>19 · Customer Search</b></td>
    <td align="center" valign="top"><a href="05-collections/20-order-processing-queue"><img alt="Flow of Program 20, Order Processing Queue, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-20.svg" width="290"></a><br><b>20 · Order Processing Queue</b></td>
    <td align="center" valign="top"><a href="05-collections/21-employee-directory"><img alt="Flow of Program 21, Employee Directory, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-21.svg" width="290"></a><br><b>21 · Employee Directory</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="05-collections/22-product-inventory"><img alt="Flow of Program 22, Product Inventory (Phase 5 project), in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-22.svg" width="290"></a><br><b>22 · Product Inventory (Phase 5 project)</b></td>
    <td align="center" valign="top"><a href="06-files-io-date/23-employee-file-management"><img alt="Flow of Program 23, Employee File Management, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-23.svg" width="290"></a><br><b>23 · Employee File Management</b></td>
    <td align="center" valign="top"><a href="06-files-io-date/24-log-file-analyzer"><img alt="Flow of Program 24, Log File Analyzer, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-24.svg" width="290"></a><br><b>24 · Log File Analyzer</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="06-files-io-date/25-daily-transaction-report"><img alt="Flow of Program 25, Daily Transaction Report, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-25.svg" width="290"></a><br><b>25 · Daily Transaction Report</b></td>
    <td align="center" valign="top"><a href="06-files-io-date/26-appointment-scheduler"><img alt="Flow of Program 26, Appointment Scheduler (Phase 6 project), in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-26.svg" width="290"></a><br><b>26 · Appointment Scheduler (Phase 6 project)</b></td>
    <td align="center" valign="top"><a href="07-modern-java/27-customer-data-processing"><img alt="Flow of Program 27, Customer Data Processing, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-27.svg" width="290"></a><br><b>27 · Customer Data Processing</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="07-modern-java/28-transaction-reporting"><img alt="Flow of Program 28, Transaction Reporting, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-28.svg" width="290"></a><br><b>28 · Transaction Reporting</b></td>
    <td align="center" valign="top"><a href="07-modern-java/29-sales-analytics"><img alt="Flow of Program 29, Sales Analytics, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-29.svg" width="290"></a><br><b>29 · Sales Analytics</b></td>
    <td align="center" valign="top"><a href="07-modern-java/30-product-recommendation-filtering"><img alt="Flow of Program 30, Product Recommendation Filtering, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-30.svg" width="290"></a><br><b>30 · Product Recommendation Filtering</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="08-concurrency/31-background-report-generator"><img alt="Flow of Program 31, Background Report Generator, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-31.svg" width="290"></a><br><b>31 · Background Report Generator</b></td>
    <td align="center" valign="top"><a href="08-concurrency/32-concurrent-order-processor"><img alt="Flow of Program 32, Concurrent Order Processor, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-32.svg" width="290"></a><br><b>32 · Concurrent Order Processor</b></td>
    <td align="center" valign="top"><a href="08-concurrency/33-parallel-data-processing"><img alt="Flow of Program 33, Parallel Data Processing, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-33.svg" width="290"></a><br><b>33 · Parallel Data Processing</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="08-concurrency/34-asynchronous-payment-status-simulator"><img alt="Flow of Program 34, Asynchronous Payment Status Simulator, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-34.svg" width="290"></a><br><b>34 · Asynchronous Payment Status Simulator</b></td>
    <td align="center" valign="top"><a href="09-professional-java/35-maven-based-java-application"><img alt="Flow of Program 35, Maven-based Java Application, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-35.svg" width="290"></a><br><b>35 · Maven-based Java Application</b></td>
    <td align="center" valign="top"><a href="09-professional-java/36-automated-business-logic-tests"><img alt="Flow of Program 36, Automated Business Logic Tests, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-36.svg" width="290"></a><br><b>36 · Automated Business Logic Tests</b></td>
  </tr>
  <tr>
    <td align="center" valign="top"><a href="09-professional-java/37-jdbc-banking-application"><img alt="Flow of Program 37, JDBC Banking Application, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-37.svg" width="290"></a><br><b>37 · JDBC Banking Application</b></td>
    <td align="center" valign="top"><a href="09-professional-java/38-employee-database-management"><img alt="Flow of Program 38, Employee Database Management, in 5 steps. The steps are listed in text below the picture." src="docs/flows/program-38.svg" width="290"></a><br><b>38 · Employee Database Management</b></td>
  </tr>
</table>

Colour code: slate is input, teal is work, amber is a check, violet is stored data, orange is the result you see.
<!--/gallery-->

## All programs

<details open>
<summary><b>Phase 01 · Getting Started</b> (1 program)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 01 | [Personal Profile Generator](01-getting-started/01-personal-profile-generator) | `javac` and `java`, `System.out` | Ready |

</details>

<details>
<summary><b>Phase 02 · Core Fundamentals</b> (6 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 02 | [Employee Salary Calculator](02-core-fundamentals/02-employee-salary-calculator) | variables, operators, casting | Ready |
| 03 | [Money Handling Mini-Lab](02-core-fundamentals/03-money-handling-mini-lab) | `BigDecimal`, rounding | Ready |
| 04 | [Electricity Bill Generator](02-core-fundamentals/04-electricity-bill-generator) | `if` / `else`, constants, methods | Ready |
| 05 | [ATM Menu Simulation](02-core-fundamentals/05-atm-menu-simulation) | `switch`, loops, `Scanner` | Ready |
| 06 | [Student Marks Analyzer](02-core-fundamentals/06-student-marks-analyzer) | arrays, method overloading | Ready |
| 07 | [Shopping Cart Calculator](02-core-fundamentals/07-shopping-cart-calculator) | strings, `StringBuilder` | Ready |

</details>

<details>
<summary><b>Phase 03 · Object-Oriented Programming</b> (6 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 08 | [Banking Application](03-object-oriented-programming/08-banking-application) | classes, encapsulation | Ready |
| 09 | [JUnit Starter Lab](03-object-oriented-programming/09-junit-starter-lab) | JUnit 5, assertions | Ready |
| 10 | [Employee Management System](03-object-oriented-programming/10-employee-management-system) | inheritance, polymorphism | Ready |
| 11 | [Vehicle Rental System](03-object-oriented-programming/11-vehicle-rental-system) | abstract classes, interfaces | Ready |
| 12 | [Payment Processing System](03-object-oriented-programming/12-payment-processing-system) | interfaces, composition | Ready |
| 13 | [E-commerce Order Management](03-object-oriented-programming/13-ecommerce-order-management) | object relationships | Ready |

</details>

<details>
<summary><b>Phase 04 · Java Essentials</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 14 | [Order Status Management](04-java-essentials/14-order-status-management) | enums, `static`, `final` | Ready |
| 15 | [Banking Exception Management](04-java-essentials/15-banking-exception-management) | custom exceptions, `try` / `catch` | Ready |
| 16 | [Generic Repository](04-java-essentials/16-generic-repository) | generics, bounded types | Ready |
| 17 | [Plugin Discovery Demonstration](04-java-essentials/17-plugin-discovery-demonstration) | annotations, reflection | Ready |

</details>

<details>
<summary><b>Phase 05 · Collections</b> (5 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 18 | [Online Shopping Cart](05-collections/18-online-shopping-cart) | `List`, `Iterator`, `Comparator` | Ready |
| 19 | [Customer Search](05-collections/19-customer-search) | `Set`, `Comparable` | Ready |
| 20 | [Order Processing Queue](05-collections/20-order-processing-queue) | `Queue`, `Deque` | Ready |
| 21 | [Employee Directory](05-collections/21-employee-directory) | `HashMap`, `equals` / `hashCode` | Ready |
| 22 | [Product Inventory](05-collections/22-product-inventory) | `Map` and `List` together, generics | Ready |

</details>

<details>
<summary><b>Phase 06 · Files, I/O and Date</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 23 | [Employee File Management](06-files-io-date/23-employee-file-management) | streams, buffered I/O | Ready |
| 24 | [Log File Analyzer](06-files-io-date/24-log-file-analyzer) | NIO, `Path` and `Files` | Ready |
| 25 | [Daily Transaction Report](06-files-io-date/25-daily-transaction-report) | `LocalDate`, formatting | Ready |
| 26 | [Appointment Scheduler](06-files-io-date/26-appointment-scheduler) | `ZonedDateTime`, file persistence | Ready |

</details>

<details>
<summary><b>Phase 07 · Modern Java</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 27 | [Customer Data Processing](07-modern-java/27-customer-data-processing) | lambdas, `filter` / `map` | Ready |
| 28 | [Transaction Reporting](07-modern-java/28-transaction-reporting) | records, `Optional` | Ready |
| 29 | [Sales Analytics](07-modern-java/29-sales-analytics) | `groupingBy`, collectors | Ready |
| 30 | [Product Recommendation Filtering](07-modern-java/30-product-recommendation-filtering) | sealed classes, pattern matching | Ready |

</details>

<details>
<summary><b>Phase 08 · Concurrency</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 31 | [Background Report Generator](08-concurrency/31-background-report-generator) | threads, `ExecutorService` | Ready |
| 32 | [Concurrent Order Processor](08-concurrency/32-concurrent-order-processor) | `synchronized`, race conditions | Ready |
| 33 | [Parallel Data Processing](08-concurrency/33-parallel-data-processing) | `Future`, thread safety | Ready |
| 34 | [Asynchronous Payment Status Simulator](08-concurrency/34-asynchronous-payment-status-simulator) | `CompletableFuture` | Ready |

</details>

<details>
<summary><b>Phase 09 · Professional Java</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 35 | [Maven-based Java Application](09-professional-java/35-maven-based-java-application) | Maven, project structure | Ready |
| 36 | [Automated Business Logic Tests](09-professional-java/36-automated-business-logic-tests) | JUnit 5, parameterized tests | Ready |
| 37 | [JDBC Banking Application](09-professional-java/37-jdbc-banking-application) | `PreparedStatement`, transactions | Ready |
| 38 | [Employee Database Management](09-professional-java/38-employee-database-management) | CRUD, SOLID, Git | Ready |

</details>

<details>
<summary><b>Capstones</b> (5 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 39 | Enterprise Banking System | JDBC, collections, tests | Planned |
| 40 | E-commerce Order Management | streams, interfaces, database | Planned |
| 41 | Employee Management System | CRUD, date and time, JUnit | Planned |
| 42 | Concurrent Order Processing System | `CompletableFuture`, thread safety | Planned |
| 43 | Library Management System | built on your own, graded by a rubric | Planned |

</details>

## Inside every program

Open one program and you know where everything is in all of them:

```text
01-personal-profile-generator/
├── README.md             problem, concepts, how to run
├── run.sh                one command, no setup  (--check compares with expected output)
├── src/main/java/        the program, in reading order
├── expected-output.txt   what a correct run prints
├── exercises/            tasks with progressive hints, no solutions
├── broken/               files with one deliberate bug each
└── solutions/            open after you have tried
```

From Program 9 on, each program also has `src/test/java/` with JUnit tests.

## How to know you have learned something

Reading a program does not count. Each topic moves up a level only when your own code shows it.

| Level | Name | You can... |
|---|---|---|
| 1 | Understood | explain the concept in your own words |
| 2 | Applied | write a program that uses it |
| 3 | Connected | combine it with what you learned earlier |
| 4 | Mastered | solve a new problem alone, explain your design choices and test the result |

## Check everything

```bash
./scripts/check-all.sh
```

This compiles and runs every ready program and compares each with its `expected-output.txt`. The same script runs in GitHub Actions on Java 21 and 25.

## About

Built by **CodeChanakya** · [codechanakya.github.io](https://codechanakya.github.io/)

Released under the [MIT License](LICENSE).
