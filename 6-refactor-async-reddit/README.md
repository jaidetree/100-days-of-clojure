# What did I learn?
- `async/take!` reads the first message from a channel
- Use `async/go-loop` to continuously read from a channel
- clojure.core.async is more like a building block to build a stream library with than a stream library on its own, but then again channels combined with transducers can solve similar problems
- Got answers on https://www.reddit.com/r/Clojure/comments/7r807e/trouble_fetching_displaying_reddit_posts_with/

# Questions
- I implemented `channel-map` and `channel-for-each` using async/pipe, transducer, and a go-loop. Are these useful & practical or am I missing something?
