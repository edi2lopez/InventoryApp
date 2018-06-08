
package inventory.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public class Outsourced extends Part {

    public Outsourced() {
    }
      
    private StringProperty companyName;

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName = new SimpleStringProperty();
        this.companyName.setValue(companyName);
    }
    
    public StringProperty companyNameProperty() {
        return companyName;
    }
    
}
