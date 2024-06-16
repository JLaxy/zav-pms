/*
 * Contains all of the pre-defined Unit Measure in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class UnitMeasureCBox {
    public enum UnitMeasure {
        // UNIT_MEASURE_ID(unit_measure_id_db)
        BOTTLE(1),
        PACK(2),
        SACHET(3),
        BOX(4);

        private final int unit_measure_id;

        // Constructor for ENUM
        UnitMeasure(int unit_measure_id) {
            this.unit_measure_id = unit_measure_id;
        }

        // Returns the value of the unit_measure_id equivalent to database
        public int getValue() {
            return this.unit_measure_id;
        }
    }
}
