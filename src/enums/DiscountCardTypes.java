package enums;

public class DiscountCardTypes {
    public enum CardType {
        SENIOR_CITIZEN(1, "Senior Citizen"),
        PWD(2, "PWD");

        private final int type_id;
        private final String type_name;

        CardType(int type_id, String type_name) {
            this.type_id = type_id;
            this.type_name = type_name;
        }

        public int getValue() {
            return this.type_id;
        }

        public String getName() {
            return this.type_name;
        }

        @Override
        public String toString() {
            return this.type_name;
        }

        public static CardType fromString(String text) {
            for (CardType b : CardType.values()) {
                if (b.type_name.equalsIgnoreCase(text.replace(" ", "_"))) {
                    return b;
                }
            }
            throw new IllegalArgumentException("No enum constant " + text);
        }
    }
}
