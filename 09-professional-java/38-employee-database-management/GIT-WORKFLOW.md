# A Git workflow for this project

This is one full cycle, the way teams work on a shared repository. Replace `core-java` with the name of your own repository if it is different.

## 1. Start from a clean, current main

```bash
git switch main
git pull
git status            # should say "nothing to commit, working tree clean"
```

## 2. Make a branch for one change

One branch is one idea. A good name says what it does.

```bash
git switch -c feature/find-by-name-prefix
```

## 3. Work in small steps, and test before every commit

```bash
./run.sh --test       # all green? then commit
git add src/main/java/com/codechanakya/empdb/EmployeeRepository.java \
        src/main/java/com/codechanakya/empdb/InMemoryEmployeeRepository.java \
        src/main/java/com/codechanakya/empdb/JdbcEmployeeRepository.java \
        src/test/java/com/codechanakya/empdb/EmployeeRepositoryContract.java
git diff --staged     # read what you are about to commit
git commit -m "Add findByNamePrefix to the repository and its contract test"
```

Adding files by name, instead of `git add .`, keeps `lib/`, `out/` and `target/` out of the commit by accident. Read the diff before you commit. It is the cheapest code review you will ever get.

### Writing a commit message

| Do | Do not |
|---|---|
| Start with a verb: "Add", "Fix", "Rename". | "stuff", "changes", "wip" |
| Say what and why in the first line, under about 70 characters. | One line that says nothing about the reason. |
| Make one logical change per commit. | Mix a bug fix, a rename and a new feature. |

## 4. Push and open a pull request

```bash
git push -u origin feature/find-by-name-prefix
```

Open a pull request on GitHub. Say what changed, why, and how you tested it. A reviewer reads the diff, asks questions, and approves or asks for changes. The CI job runs every program's `run.sh --check`, so a red mark means something broke.

## 5. Keep the branch up to date

```bash
git fetch origin
git rebase origin/main        # or: git merge origin/main
```

## 6. If Git reports a conflict

A conflict means two branches changed the same lines. Git stops and marks the file:

```text
<<<<<<< HEAD
your version
=======
the other version
>>>>>>> origin/main
```

Open the file, decide what the final text should be, delete the three marker lines, then:

```bash
git add path/to/the/file
git rebase --continue         # or: git commit, if you were merging
```

To give up and return to where you were, run `git rebase --abort` (or `git merge --abort`). Nothing is lost.

## 7. Merge, then clean up

After approval, merge the pull request on GitHub, then tidy locally:

```bash
git switch main
git pull
git branch -d feature/find-by-name-prefix
```

## What to keep out of the repository

This project's `.gitignore` already covers these. Know why each is there.

| Pattern | Why it is ignored |
|---|---|
| `target/` | Maven builds it from the source. It is large and changes on every build. |
| `out/` | The same, for the `javac` route in `run.sh`. |
| `lib/` | Downloaded jars. Anyone can fetch them again, and the `pom.xml` names exact versions. |
| `*.class` | Compiled code. |
| `.idea/`, `*.iml`, `.vscode/` | Settings of one person's editor. |

Never commit passwords, tokens or `.env` files. If one is committed by mistake, change the secret first (rewriting history does not take back something already pushed), and then remove the file.

## Useful commands when something looks wrong

| Question | Command |
|---|---|
| What changed since the last commit? | `git status` and `git diff` |
| What did I commit recently? | `git log --oneline -10` |
| Undo a change in one file that is not staged? | `git restore path/to/file` |
| Take a file out of the next commit? | `git restore --staged path/to/file` |
| Fix the last commit message (not pushed yet)? | `git commit --amend` |
| Undo a pushed commit safely? | `git revert <commit>` (a new commit that reverses it) |
