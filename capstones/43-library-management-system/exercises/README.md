# Stretch goals

These are optional. Finish the acceptance tests and your own tests first. A few points of the rubric (see
[RUBRIC.md](../RUBRIC.md), "Stretch") come from here, and they are worth more when the basics are solid.

1. **Save to a file.** Write a `CsvLibraryStore` that implements `LibraryStore` and keeps books, members and loans
   in CSV files. Run your own tests against both stores. If the service tests pass with either, your design is
   sound.
2. **Save to a database.** Write a `JdbcLibraryStore` using Derby, as in Programs 39 to 41.
3. **A report of the most borrowed books.** Count loans per book and print the top three. Decide where the counting
   belongs and write tests first.
4. **Per-book fines.** Reference books carry a higher fine. Add the rule without changing the existing tests,
   which tells you whether your design allows change.
5. **A simple menu.** A `Scanner` loop in `Main` that lets someone search, borrow and return. Keep all rules in
   `LibraryService`, and keep the menu thin.

No model answers are provided for these. That is the point of a solo build.
