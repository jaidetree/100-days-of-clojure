# What did I learn?
- `async/onto-chan` Doesn't return the input `ch` channel but instead returns another channel that signifies when all items finish copying.
- Slack Clojurians channel is helpful & decently friendly. Didn't feel like anyone was trying to play "smartest person in the room" or make someone feel dumb for not knowing something.

# Questions
- I implemented `channel-map` and `channel-for-each` using async/pipe, transducer, and a go-loop. Are these useful & practical or am I missing something?
  - Turns out it's okay.
  - Instead of `channel-for-each` could use `sink` or `drain`
  - `channel-for-each` could be a macro because it doesn't return anything.
  - Loses some of the flexibility of transducers.
  - `channel-map` would not be good for side-effects because of the `channel-mutex` which I'm not sure what it means.
