
package inventory.view;

import inventory.MainApp;
import inventory.model.Inventory;
import inventory.model.Part;
import inventory.model.Product;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author EL
 */
public class InventoryOverviewController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private TextField lookupProductField;
    @FXML private Button lookupProductButton;
    
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TextField lookupPartField;
    @FXML private Button lookupPartButton;
    
    @FXML private Button addPartButton;
 
    // Reference to the main application.
    private MainApp mainApp;
    
    /**
     * Used to increment the items IDs in the dialog
     * Start at 5 because pre-loaded data
     */
    private static int nextProductId = 5;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public InventoryOverviewController() {
    }  

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
                   
        // Initialize product & part tables.
        setProductTable();
        setPartTable();
        
        // search functions
        lookupProductButton.setOnAction(this::handleLookupProduct);
        productLookupResult();
        
        lookupPartButton.setOnAction(this::handleLookupPart);
        partLookupResult();
  
    }  
   
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
        //setting up the product & part table data sources
        productTable.setItems(Inventory.getProduct());
        partTable.setItems(Inventory.getPart());

    }
    
    /**
     * Set products table
     */
    private void setProductTable() {
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        productStockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        productPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }
    
    /**
     * Set parts table
     */
    private void setPartTable () {
        partIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());  
    }
    
    /**
     * Called when the user clicks on the delete product button.
     */
    @FXML private void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        
        if ( selectedProduct != null && !selectedProduct.getPartIds().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle( "Product Information" );
            alert.setHeaderText("Product can not be deleted!");
            alert.setContentText("Error! " + selectedProduct.getName() + " contains parts. Please first remove the following parts from product: " + selectedProduct.getPartIds());            
            alert.show();  
        } else if ( selectedProduct != null && selectedProduct.getId() >= 0 ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle( "Delete Product" );
            alert.setHeaderText("Delete Product " + selectedProduct.getName());
            alert.setContentText("Are you sure that you want to delete " + selectedProduct.getName() + "?");            
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                               .ifPresent(response -> Inventory.removeProduct(selectedProduct.getId()));
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Please select a product in the table.");

            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the new product button. Opens a dialog to edit
     * details for a new product.
     */
    @FXML private void handleNewProduct() {
        Product tempProduct = new Product();
        
        // Initialize product with empty/default values
        tempProduct.setId(++nextProductId);
        tempProduct.setName("");
        tempProduct.setStock(0);
        tempProduct.setPrice(0.00);
        tempProduct.setMin(0);
        tempProduct.setMax(0);

        boolean okClicked = mainApp.showProductEditDialog(tempProduct);
        if (okClicked) {
            Inventory.getProduct().add(tempProduct);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected product.
     */
    @FXML public void handleEditProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            boolean okClicked = mainApp.showProductEditDialog(selectedProduct);

            if (okClicked) {
                productTable.refresh();
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Please select a product in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks on the search product button.
     */
    @FXML private void handleLookupProduct(ActionEvent event) {
        productTable.setItems(Inventory.sortedProduct());
    }
    
    /**
     * Product search results table
     */
    public void productLookupResult() {
        lookupProductField.textProperty().addListener((observable, oldValue, newValue) -> {
            Inventory.filteredProduct().setPredicate(product -> {
                // If filter text is empty, display all products.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare product name of every product with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                // False, it does not match.
                if ( product.getName().toLowerCase().contains(lowerCaseFilter) ) {
                    return true; // Filter matches first name.
                }
                // TO DO another condition (maybe search by price or inventory level)
                // Leave the redundant statement for now
                return false;
            });
            productTable.refresh();
        });
    }
    

    /**
     * Called when the user clicks on the delete part button.
     */
    @FXML private void handleDeletePart() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        if ( selectedPart != null && selectedPart.getId() >= 0 ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle( "Delete Part" );
            alert.setHeaderText("Delete Part " + selectedPart.getName());
            alert.setContentText("Are you sure that you want to delete " + selectedPart.getName() + "?");            
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                               .ifPresent(response -> Inventory.removePart(selectedPart.getId()));
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new part button. Opens a dialog to edit
     * details for a new part.
     */
    @FXML private void handleNewPart() {

        boolean okClicked = mainApp.showNewPartEditDialog();
        if (okClicked) {
            System.out.println("part updated ... ");
        }

    }
 
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected part.
     */
    @FXML private void handleEditPart() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        
        if (selectedPart != null) {
            boolean okClicked = mainApp.showPartEditDialog(selectedPart);
            if (okClicked) {
                partTable.refresh();
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part in the table.");

            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks on the search part button.
     */
    @FXML private void handleLookupPart(ActionEvent event) {
        partTable.setItems(Inventory.sortedPart());
    }
    
    /**
     * Product search results table
     */
    public void partLookupResult() {
        lookupPartField.textProperty().addListener((observable, oldValue, newValue) -> {
            Inventory.filteredPart().setPredicate(part -> {
                // If filter text is empty, display all products.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare product name of every product with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                // False, it does not match.
                if ( part.getName().toLowerCase().contains(lowerCaseFilter) ) {
                    return true; // Filter matches first name.
                }
                // TO DO another condition (maybe search by price or inventory level)
                // Leave the redundant statement for now                
                return false;
            });
        });
    }
 
    /**
     * Called when the user clicks exit.
     */
    
    @FXML private void handleExit() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Exit System" );
        alert.setHeaderText("Exit Inventory Management System");
        alert.setContentText("Are you sure that you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } 
        
        
    }
         
}
