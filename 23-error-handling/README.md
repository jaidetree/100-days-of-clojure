# What did I learn?

- https://adambard.com/blog/acceptable-error-handling-in-clojure/
- Actual Exception handling is non-idiomatic in Clojure, it's typically done when dealing with Clojure interop.
  - Exceptions can work against referential transparency and functional purity if a side-effecty function throws an exception.
- Instead try returning a list like `[ :ok, result ]` or `[ :error, "message" ]` which we can deconstruct in args and make utility functions for continuing a chain until an error occurs.
  - Though that pattern is seemingly better suited with pattern matching like in Elixir \ Erlang
