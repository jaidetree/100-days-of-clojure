# What did I learn?
- Clojure will not wait for all channels to close before exiting. Instead call `async/<!!` to block the app while waiting for channel output.
