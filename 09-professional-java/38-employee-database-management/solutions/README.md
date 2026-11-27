# Solutions

One worked answer to every exercise that changes code, kept in the same folder layout as the program so you can compare file by file.

| Exercise | Where |
|---|---|
| A2 | `findByNamePrefix` in the interface and both repositories, `searchByName` in the service, and `findByNamePrefixIgnoresCaseAndKeepsIdOrder` in the contract |
| B1 | `EmployeeService.averageSalary` and its test |
| C1 | `CountingEmployeeRepository.java` and `CountingEmployeeRepositoryTest.java` |

A1 needs only the new enum constant; the table stores names as text. For A3, only `hireDateBoundary` fails: it expects 1 July to be in the future, and with the new clock it is today. B2 follows the same pattern as the raise rule already in the service.

C1 shows the open-closed idea. The counting repository passes the whole contract (it is a third subclass of `EmployeeRepositoryContract`) and no existing class was edited. Only `Main` was extended to use it.

Run the solution version (run `../run.sh --test` once first so the jars are downloaded):

```bash
./run.sh            # the demonstration, with an "Exercise answers" section
./run.sh --test     # the tests, including the new ones
```

Each change is marked with the exercise number in a comment, for example `// A2`. Your own answer can look different and still be right.
