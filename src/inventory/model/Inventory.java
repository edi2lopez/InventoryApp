
package inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author el
 */
public class Inventory {
    
    
    Inventory() {
    }

    /**
     * The products as an observable list of Inventory.
     */
    private static final List<Product> productList = new ArrayList<Product>();
    private static final ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
    // Used to filter product searches
    private static final FilteredList<Product> productFilteredList = new FilteredList<>(productObservableList, p -> true);
    private static final SortedList<Product> productSortedList = new SortedList<>(productFilteredList);
    
    /**
     * Returns the products as an observable list of Inventory. 
     * @return
     */
    public static ObservableList<Product> getProduct() {
        return productObservableList;
    }

    /**
     * Add Products
     * @param product 
     */
    public static void addProduct(Product product) {
        productObservableList.add(product);
    }
    
    /**
     * Remove Products
     * @param productId 
     * @return  
     */
    public static boolean removeProduct(int productId) {
        return productObservableList.removeIf(product -> product.getId() == productId);
    }

    /**
     * Search product with live update
     * @return 
     */
    public static FilteredList<Product> filteredProduct() {
        return productFilteredList;
    }
    public static SortedList<Product> sortedProduct() {
        return productSortedList;
    }
    
    /**
     * Search product by id
     * 
     * @param productId
     * @return 
     */
    public static Product findProductById(int productId) {
        return productObservableList.stream()
                .filter(product -> product.getId() == productId)
                .findFirst()
                .orElse(null);
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
     * Returns the parts as an observable list of Inventory. 
     * @return
     */
    public static ObservableList<Part> getPart() {
        return partObservableList;
    }
    
    /**
     * Add Parts
     * @param part 
     */
    public static void addPart(Part part) {
        partObservableList.add(part);
    }
    
    /**
     * Remove Parts 
     * @param partId
     * @return  
     */
    public static boolean removePart(int partId) {
        return partObservableList.removeIf(part -> part.getId() == partId);
    }

    /**
     * Search part with live update
     * @return 
     */
    public static FilteredList<Part> filteredPart() {
        return partFilteredList;
    }
    public static SortedList<Part> sortedPart() {
        return partSortedList;
    } 

    /**
     * Search by part Id
     * 
     * @param partId
     * @return 
     */
    public static Part findPartById(int partId) {
        return partObservableList.stream()
                .filter(part -> part.getId() == partId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Populates top parts table associated with each product in the edit product dialog
     * 
     * @param ProductId
     * @return 
     */  
    public static ObservableList<Part> getPartsInProduct(int ProductId) {

        Product selectedProduct = Inventory.findProductById(ProductId);
        List<Integer> partIds = new ArrayList<>(selectedProduct.getPartIds());

        List<Part> partsInProducts;
        partsInProducts = new ArrayList<>(Inventory.getPart().stream()
                .filter(e -> partIds.contains(e.getId()))
                .collect(Collectors.toList()));

        return FXCollections.observableList(partsInProducts);
              
    }

    /**
     * Populates bottom parts table associated with each product in the edit product dialog
     * 
     * @param ProductId
     * @return 
     */
    public static ObservableList<Part> getPartsNotInProduct(int ProductId) {
        
        Product selectedProduct = Inventory.findProductById(ProductId);
        List<Integer> partIds = new ArrayList<>(selectedProduct.getPartIds());

        List<Part> partsInProducts;
        partsInProducts = new ArrayList<>(Inventory.getPart().stream()
                .filter(e -> !partIds.contains(e.getId()))
                .collect(Collectors.toList()));

        return FXCollections.observableList(partsInProducts);
              
    }
    
}
