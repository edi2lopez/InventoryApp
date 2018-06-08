
package inventory.model;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author el
 */
public class Product extends Item {
    
    public Product() {
    }
    
    /**
     * The parts as an observable list of Inventory.
     */
    private static final List<Part> partList = new ArrayList<Part>();
    private static final ObservableList<Part> partObservableList = FXCollections.observableArrayList(partList);
    // Used to filter part searches
    private static final FilteredList<Part> partFilteredList = new FilteredList<>(partObservableList, p -> true);
    private static final SortedList<Part> partSortedList = new SortedList<>(partFilteredList);

    /**
     * Set array to hold partIds in product
     */
    private List<Integer> partIdList = new ArrayList<>();

    /**
     * Get Part IDs from product
     * @return partIds
     */
    public List<Integer> getPartIds() {
        return partIdList;
    }

    /**
     * Set Part IDs in Product
     * 
     * @param partId
     */
    public void setPartIds(List<Integer> partId) {
        this.partIdList = partId;
    }
    
    /**
     * Add parts to product
     * @param partId 
     */
    public void addPartId(int partId) {
        partIdList.add(partId);
    }

    /**
     * Remove parts from product
     * 
     * @param partId
     * @return 
     */
    public boolean removePartId(int partId) {
        return partIdList.removeIf(x -> x == partId);
    }
    
}
