
package inventory.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author el
 */
public class Inhouse extends Part {

    public Inhouse () {
    }

    private IntegerProperty machineId;

    public int getMachineId() {
        return machineId.get();
    }

    public void setMachineId(int machineId) {
        this.machineId = new SimpleIntegerProperty();
        this.machineId.setValue(machineId);
    }
    
    public IntegerProperty machineIdProperty() {
        return machineId;
    }

    
}
