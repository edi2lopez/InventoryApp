
package inventory.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public abstract class Item {
    
    /**
     * abstract class with items from UML Diagram
     * all the properties that will be automatically
     * shared with product & part separate subclasses
     */
    
    private IntegerProperty id;
    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty stock;
    private IntegerProperty min;
    private IntegerProperty max;

    /**
     * Getters & setters 
     * @return 
     */ 
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty();
        this.id.setValue(id);
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty();
        this.name.setValue(name);
    }
    
    public StringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price = new SimpleDoubleProperty();
        this.price.setValue(price);
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock = new SimpleIntegerProperty();
        this.stock.setValue(stock);
    }
    
    public IntegerProperty stockProperty() {
        return stock;
    }

    public int getMin() {
        return min.get();
    }

    public void setMin(int min) {
        this.min = new SimpleIntegerProperty();
        this.min.setValue(min);
    }
    
    public IntegerProperty minProperty() {
        return min;
    }

    public int getMax() {
        return max.get();
    }

    public void setMax(int max) {
        this.max = new SimpleIntegerProperty();
        this.max.setValue(max);
    }
    
    public IntegerProperty maxProperty() {
        return max;
    }

}
