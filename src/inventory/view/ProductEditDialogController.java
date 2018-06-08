
package inventory.view;

import inventory.model.Inventory;
import inventory.model.Product;
import inventory.model.Part;
import java.util.Optional;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a product.
 *
 * @author EL
 */
public class ProductEditDialogController {
 
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField productStockField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMinField;
    @FXML private TextField productMaxField;
    
    /**
     * Top Part Table
     */
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private Button addPartButton;
    
    // Search funtions
    @FXML private TextField lookupPartField;
    @FXML private Button lookupPartButton;
    
    /**
     * Bottom Part Table
     */
    @FXML private TableView<Part> partTableAdded;
    @FXML private TableColumn<Part, Integer> partIdColumnAdded;
    @FXML private TableColumn<Part, String> partNameColumnAdded;
    @FXML private TableColumn<Part, Integer> partStockColumnAdded;
    @FXML private TableColumn<Part, Double> partPriceColumnAdded;    
    @FXML private Button removePartButton;

    private Product product;
    private Stage dialogStage;
    private boolean okClicked = false;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ProductEditDialogController() {
    }  

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML private void initialize() {
        // Set the product ID as read only 
        productIdField.setDisable(true);

        // Set initial part tables
        setPartTable();
        setPartTableAdded();

        // search functions
        lookupPartButton.setOnAction(this::handleLookupPart);

    } 
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     * @param product
     */
    public void setDialogStage(Stage dialogStage, Product product) {
        this.dialogStage = dialogStage;
        this.product = product;

        if( ! product.getName().equals("")) {
            // Set the parts top table
            partTable.setItems(Inventory.getPartsNotInProduct(product.getId()));  

            // Set the parts bottom table
            partTableAdded.setItems(Inventory.getPartsInProduct(product.getId()));
        }
            
        partsTableActions();
   
    }
    
    /**
     * Set top part table
     */
    private void setPartTable() {
        partIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());  
    }

    /**
     * Set bottom part table
     */
    private void setPartTableAdded() {
        partIdColumnAdded.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        partNameColumnAdded.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStockColumnAdded.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partPriceColumnAdded.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());  
    }
    
    /**
     * Sets the product to be edited in the dialog.
     * 
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;

        productIdField.setText(Integer.toString(product.getId()));
        productNameField.setText(product.getName());
        productStockField.setText(Integer.toString(product.getStock()));
        productPriceField.setText(Double.toString(product.getPrice()));        
        productMinField.setText(Integer.toString(product.getMin()));
        productMaxField.setText(Integer.toString(product.getMax()));
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }  
    
    /**
     * Called when the user clicks ok.
     */
    @FXML private void handleOk() {
        if (isInputValid()) {

            // Products
            product.setId(Integer.parseInt(productIdField.getText()));
            product.setName(productNameField.getText());
            product.setStock(Integer.parseInt(productStockField.getText()));
            product.setPrice(Double.parseDouble(productPriceField.getText()));
            product.setMin(Integer.parseInt(productMinField.getText()));
            product.setMax(Integer.parseInt(productMaxField.getText()));

            okClicked = true;
            dialogStage.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle( "Product Information" );
            alert.setHeaderText("Product Updated");
            alert.setContentText("Product " + product.getName() + " Successfully Updated. You can now add parts to product!");            
            alert.show();
            
        }
    }
    
    /**
     * Called when the user clicks on the search part button.
     */
    @FXML private void handleLookupPart(ActionEvent event) {

        FilteredList<Part> partFilteredList = new FilteredList<>(Inventory.getPartsNotInProduct(product.getId()), p -> true);
        SortedList<Part> partSortedList = new SortedList<>(partFilteredList);

        partFilteredList.predicateProperty().bind(javafx.beans.binding.Bindings.createObjectBinding(() -> {
            String text = lookupPartField.getText();
            if (text == null || text.isEmpty()) {
                return null;
            } else {
                final String uppercase = text.toUpperCase();
                return (part) -> part.getName().toUpperCase().contains(uppercase);
            }
        }, lookupPartField.textProperty()));
        
        partTable.setItems(partSortedList);
        
    }

    /**
     * Called when the user clicks on add part to Product.
     */
    @FXML private void handleAddPartIds() {
    }
    
    private void partsTableActions() {

        addPartButton.setOnAction((ActionEvent event) -> { 
            Part selectedPart = partTable.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                product.addPartId(selectedPart.getId()); 
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle( "Add part to product" );
                alert.setHeaderText("Add part " + selectedPart.getName() + " to product " + product.getName());
                alert.setContentText("Are you sure that you want to add part " + selectedPart.getName() + " to product " + product.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    partTable.setItems(Inventory.getPartsNotInProduct(product.getId()));
                    partTableAdded.setItems(Inventory.getPartsInProduct(product.getId()));   
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle( "No Selection" );
                alert.setHeaderText("No Part Selected");
                alert.setContentText("Please select a part in the table.");            
                alert.show();
            }
        });
    
    }
    
    /**
     * Called when the user clicks on delete part from Product.
     */
    @FXML private void handleRemovePartIds() {
        Part selectedPart = partTableAdded.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            product.addPartId(selectedPart.getId()); 

           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
           alert.setTitle( "Remove part from product" );
           alert.setHeaderText("Remove part " + selectedPart.getName() + " from product " + product.getName());
           alert.setContentText("Are you sure that you want to remove part " + selectedPart.getName() + " from product " + product.getName() + "?");
           Optional<ButtonType> result = alert.showAndWait();
           if (result.get() == ButtonType.OK) {
                product.removePartId(selectedPart.getId());      
                partTable.setItems(Inventory.getPartsNotInProduct(product.getId()));
                partTableAdded.setItems(Inventory.getPartsInProduct(product.getId())); 
           }
        }  else {
            System.out.println("nothing selected ...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle( "No Selection" );
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part in the table.");            
            alert.show();
        }
    }
 
    /**
     * Called when the user clicks cancel.
     */
    @FXML private void handleCancel() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Cancel Changes" );
        alert.setHeaderText("Cancel Changes");
        alert.setContentText("Are you sure that you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dialogStage.close();
        } 
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {

        String errorMessage = "";
        
        if (productNameField.getText() == null || productNameField.getText().length() == 0) {
            errorMessage += "No valid product name!\n"; 
        }

        if (productStockField.getText() == null || productStockField.getText().length() == 0) {
            errorMessage += "No valid inventory Level!\n"; 
        } else {
            // try to parse the stock into an int.
            try {
                Integer.parseInt(productStockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }
        }
        
        if (productPriceField.getText() == null || productPriceField.getText().length() == 0) {
            errorMessage += "No valid price!\n"; 
        } else {
            // try to parse the price into an double.
            try {
                Double.parseDouble(productPriceField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid price (must be an dollar amount)!\n"; 
            }
        }
        
        if (productMaxField.getText() == null || productMaxField.getText().length() == 0) {
            errorMessage += "No valid inventory Level!\n"; 
        } else {
            // try to parse the minimum quantity into an int.
            try {
                Integer.parseInt(productMaxField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }
        }  
        
        if (productMinField.getText() == null || productMinField.getText().length() == 0) {
            errorMessage += "No valid inventory Level!\n"; 
        } else {
            // try to parse the minimum quantity into an int.
            try {
                Integer.parseInt(productMinField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }
        }
        
        //Stock error
        try {
            int stock = Integer.parseInt(productStockField.getText());
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());
            if ( min > max ) {
                errorMessage += "Maximum Stock amount cannot be less than the Minimum Stock amount!\n"; 
            }
            if(stock < min) {
                errorMessage += "Inventory amount cannot be less than the Minimum Stock amount!\n"; 
            }
            if(stock > max) {
                errorMessage += "Inventory amount cannot be more than the Maximum Stock amount!\n"; 
            } 
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers");
        }       

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
        
        
    }
    
 }
