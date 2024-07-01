package enums;

public class ReductionTypes {
    public enum Type {
        // TYPE(id_in_database)
        USED(1),
        EXPIRED(2),
        MISHANDLED(3);

        private final int id;

        // Constructor for ENUM
        Type(int id) {
            this.id = id;
        }

        // Returns the reduction type id equivalent to database
        public int getValue() {
            return this.id;
        }
    }
}
