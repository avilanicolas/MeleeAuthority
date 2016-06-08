package main.java.meleeauthority;

public enum CharacterPredicate {
    
    // YES I know that lambdas are a thing, and that, in fact,
    // predicates are used there. However, for problems that I don't
    // currently have time to solve, Java8 is not usable on all machines
    // which this server runs on.
    // TODO use java 8 so I don't have this unholy abortion below
    GT(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one > two;
        }
    }, ">"),

    
    GROEQ(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one >= two;
        }
    }, ">="),

    EQ(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one > two;
        }
    }, "="),

    NEQ(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one > two;
        }
    }, "!="),

    LTOEQ(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one > two;
        }
    }, "<="),

    LT(new UsablePredicate() {
        @Override
        public boolean evaluate(float one, float two) {
            return one > two;
        }
    }, "<");

    private UsablePredicate predicate;
    private String toString;

    private CharacterPredicate(UsablePredicate predicate, String toString) {
        this.predicate = predicate;
        this.toString = toString;
    }

    public boolean evaluate(float one, float two) {
        return predicate.evaluate(one, two);
    }

    @Override
    public String toString() {
        return toString;
    }

    private static class UsablePredicate {
        public boolean evaluate(float one, float two) {
            return false;
        }
    }
}
