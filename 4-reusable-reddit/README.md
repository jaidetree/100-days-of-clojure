# What did I learn?
- How to require local libs
- You can require libs that your libs require
- If you stop requiring the parent lib you will need to add child lib back to deps.edn
- `doseq` Will run side effects within its body
- `clj-http` Can coerce JSON into EDN if the optional library JSON `ceshire` is also available in deps.edn.
