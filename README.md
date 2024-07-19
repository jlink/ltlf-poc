# Linear Temporal Logic (finite) POC

This is a proof of concept for a simple linear temporal logic (LTL) implementation in Java. 
The implementation is based on the finite semantics of LTL, 
see [LTL](./src/main/java/ltlf/LTL.java) for details.

The goal is to eventually provide something like [Quickstrom](https://quickstrom.io/) in [jqwik](https://jqwik.net/)

## TODOs

- [x] atomic facts
- [x] not
- [x] and
- [x] or
- [x] implies
- [x] always
- [x] eventually
- [x] next
- [ ] until
- [ ] Property-based tests for LTL
- [ ] Make LTLFormula generic (by any state type)
- [ ] Provide mismatching details in case of failed match
- [ ] Allow to switch between finite and infinite semantics