<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="assets/banner-dark.svg">
    <img alt="Core Java by CodeChanakya" src="assets/banner-light.svg" width="100%">
  </picture>
</p>

<p align="center">
  <img alt="Java 21 or newer" src="https://img.shields.io/badge/Java-21%2B-B83A0E">
  <img alt="Programs ready" src="https://img.shields.io/badge/programs%20ready-1%20of%2043-0F766E">
  <img alt="License MIT" src="https://img.shields.io/badge/license-MIT-0E1726">
  <a href="https://github.com/OWNER/Core-Java/actions/workflows/ci.yml"><img alt="CI" src="https://github.com/OWNER/Core-Java/actions/workflows/ci.yml/badge.svg"></a>
</p>

<p align="center">
  <a href="https://OWNER.github.io/Core-Java/"><b>Browse the site</b></a> &nbsp;·&nbsp;
  <a href="https://codespaces.new/OWNER/Core-Java"><b>Open in Codespaces</b></a>
</p>

---

**Core Java** is a set of 43 Java programs for learners, from a first `System.out.println` to a concurrent order-processing system. Each program has a reading order, a sample run, exercises you attempt before you open the solution, and files with one planted bug so you meet compiler errors on your own terms.

It follows a fixed path: smallest building block first, then the next concept that depends on it, until you can design and test a small system yourself.

> **Status:** Phase 1 is ready. The other programs are planned and appear below as they are built. Each ready program lists the Java version and platform it was run on.

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
| 02 | Employee Salary Calculator | variables, operators, casting | Planned |
| 03 | Money Handling Mini-Lab | `BigDecimal`, rounding | Planned |
| 04 | Electricity Bill Generator | `if` / `else`, constants, methods | Planned |
| 05 | ATM Menu Simulation | `switch`, loops, `Scanner` | Planned |
| 06 | Student Marks Analyzer | arrays, method overloading | Planned |
| 07 | Shopping Cart Calculator | strings, `StringBuilder` | Planned |

</details>

<details>
<summary><b>Phase 03 · Object-Oriented Programming</b> (6 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 08 | Banking Application | classes, encapsulation | Planned |
| 09 | JUnit Starter Lab | JUnit 5, assertions | Planned |
| 10 | Employee Management System | inheritance, polymorphism | Planned |
| 11 | Vehicle Rental System | abstract classes, interfaces | Planned |
| 12 | Payment Processing System | interfaces, composition | Planned |
| 13 | E-commerce Order Management | object relationships | Planned |

</details>

<details>
<summary><b>Phase 04 · Java Essentials</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 14 | Order Status Management | enums, `static`, `final` | Planned |
| 15 | Banking Exception Management | custom exceptions, `try` / `catch` | Planned |
| 16 | Generic Repository | generics, bounded types | Planned |
| 17 | Plugin Discovery Demonstration | annotations, reflection | Planned |

</details>

<details>
<summary><b>Phase 05 · Collections</b> (5 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 18 | Online Shopping Cart | `List`, `Iterator`, `Comparator` | Planned |
| 19 | Customer Search | `Set`, `Comparable` | Planned |
| 20 | Order Processing Queue | `Queue`, `Deque` | Planned |
| 21 | Employee Directory | `HashMap`, `equals` / `hashCode` | Planned |
| 22 | Product Inventory | `Map` and `List` together, generics | Planned |

</details>

<details>
<summary><b>Phase 06 · Files, I/O and Date</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 23 | Employee File Management | streams, buffered I/O | Planned |
| 24 | Log File Analyzer | NIO, `Path` and `Files` | Planned |
| 25 | Daily Transaction Report | `LocalDate`, formatting | Planned |
| 26 | Appointment Scheduler | `ZonedDateTime`, file persistence | Planned |

</details>

<details>
<summary><b>Phase 07 · Modern Java</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 27 | Customer Data Processing | lambdas, `filter` / `map` | Planned |
| 28 | Transaction Reporting | records, `Optional` | Planned |
| 29 | Sales Analytics | `groupingBy`, collectors | Planned |
| 30 | Product Recommendation Filtering | sealed classes, pattern matching | Planned |

</details>

<details>
<summary><b>Phase 08 · Concurrency</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 31 | Background Report Generator | threads, `ExecutorService` | Planned |
| 32 | Concurrent Order Processor | `synchronized`, race conditions | Planned |
| 33 | Parallel Data Processing | `Future`, thread safety | Planned |
| 34 | Asynchronous Payment Status Simulator | `CompletableFuture` | Planned |

</details>

<details>
<summary><b>Phase 09 · Professional Java</b> (4 programs)</summary>

| No. | Program | Concepts | Status |
|---|---|---|---|
| 35 | Maven-based Java Application | Maven, project structure | Planned |
| 36 | Automated Business Logic Tests | JUnit 5, parameterized tests | Planned |
| 37 | JDBC Banking Application | `PreparedStatement`, transactions | Planned |
| 38 | Employee Database Management | CRUD, SOLID, Git | Planned |

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

Built by **CodeChanakya** ·

Released under the [MIT License](LICENSE).
