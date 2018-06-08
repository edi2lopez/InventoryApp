
package inventory.view;

import inventory.model.Inhouse;
import inventory.model.Inventory;
import inventory.model.Outsourced;
import inventory.model.Part;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a part.
 *
 * @author EL
 */
public class PartEditDialogController {

    @FXML private TextField partIdField;
    @FXML private TextField partNameField;
    @FXML private TextField partStockField;
    @FXML private TextField partPriceField;
    @FXML private TextField partMinField;
    @FXML private TextField partMaxField;
    
    @FXML private ToggleGroup outsourcedToggleGroup;
    @FXML private RadioButton inhouseToggle;
    @FXML private Label machineIdLabel;
    @FXML private RadioButton outsourcedToggle;
    @FXML private TextField machineIdField;
    @FXML private Label companyNameLabel;
    @FXML private TextField companyNameField;

    private Stage dialogStage;
    private Part part;
    private Inhouse inhouse;
    private Outsourced outsourced;
    private static int nextPartId = 5;
            
    private boolean okClicked;
    //private boolean isInhouse;
    //private boolean isOutsourced;
 
    public PartEditDialogController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML private void initialize() {

        // Set the part ID as read only 
        partIdField.setDisable(true);

        // Start with inhouse selected
        inhouseToggle.setUserData("In-House");
        inhouseToggle.setSelected(true);
        
        outsourcedToggle.setSelected(false);
        outsourcedToggle.setUserData("Outsourced");

        // Hide outsourced label & textfield
        companyNameLabel.setVisible(false);
        companyNameField.setVisible(false);

        // Handle toggle behaviour
        handleToggle();

    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;             
    }
    
    /**
     * Set the toggle button action
     * 
     */
    private void handleToggle() {
        inhouseToggle.setOnAction((ActionEvent e) -> {
            machineIdLabel.setVisible(true);
            machineIdField.setVisible(true); 
            companyNameLabel.setVisible(false);
            companyNameField.setVisible(false);
        });
        outsourcedToggle.setOnAction((ActionEvent e) -> {
            machineIdLabel.setVisible(false);
            machineIdField.setVisible(false);
            companyNameLabel.setVisible(true);
            companyNameField.setVisible(true);
        });
    }

    /**
     * Sets the part to be edited in the dialog.
     * 
     * @param part
     */
    public void setPart(Part part) {
        this.part = part;

        partIdField.setText(Integer.toString(part.getId()));
        partNameField.setText(part.getName());
        partStockField.setText(Integer.toString(part.getStock()));
        partPriceField.setText(Double.toString(part.getPrice()));
        
        partMinField.setText(Integer.toString(part.getMin()));
        partMaxField.setText(Integer.toString(part.getMax()));
        
        if (part instanceof Inhouse) {
            machineIdField.setText(Integer.toString(((Inhouse) part).getMachineId()));
        } else if (part instanceof Outsourced) {
            companyNameField.setText(((Outsourced) part).getCompanyName());       
        }
            
    }
    
    /**
     * Set new Part 
     */
    public void setNewPart() {

        partIdField.setText(Integer.toString(++nextPartId));
        partNameField.setText("");
        partStockField.setText(Integer.toString(0));
        partPriceField.setText(Double.toString(0.00));
        partMinField.setText(Integer.toString(0));
        partMaxField.setText(Integer.toString(0));
        
        // start with 0 machine id by default
        machineIdField.setText(Integer.toString(0));

        outsourcedToggleGroup.selectedToggleProperty()
            .addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {

            if (outsourcedToggleGroup.getSelectedToggle().getUserData() == "In-House") {
                machineIdField.setText("");
                companyNameField.clear();
                //System.out.println("selected toggle userdata: " + outsourcedToggleGroup.getSelectedToggle().getUserData());
            }    
            if (outsourcedToggleGroup.getSelectedToggle().getUserData() == "Outsourced" ) {
                companyNameField.setText("");
                machineIdField.clear();
                //System.out.println("selected toggle userdata: " + outsourcedToggleGroup.getSelectedToggle().getUserData());
            }

        });

    }
    
    /**
     * Check if In-house toggle is selected
     * @return 
     */
    private boolean isInhouse() {
        return outsourcedToggleGroup.getSelectedToggle().getUserData() == "In-House";
    }
    
    /**
     * Check if Outsourced toggle is selected
     * @return 
     */
    private boolean isOutsourced() {
        return outsourcedToggleGroup.getSelectedToggle().getUserData() == "Outsourced";
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
        
        if(isInputValid()) {
            
            if ( (part instanceof Inhouse || part instanceof Outsourced) ) {
                part.setId(Integer.parseInt(partIdField.getText()));
                part.setName(partNameField.getText());
                part.setStock(Integer.parseInt(partStockField.getText()));
                part.setPrice(Double.parseDouble(partPriceField.getText()));
                part.setMin(Integer.parseInt(partMinField.getText()));
                part.setMax(Integer.parseInt(partMaxField.getText()));

                if (part instanceof Inhouse) {
                    part = (Inhouse) part;
                    ((Inhouse) part).setMachineId(Integer.parseInt(machineIdField.getText()));
                } else if (part instanceof Outsourced) {
                    part = (Outsourced) part;
                    ((Outsourced) part).setCompanyName(companyNameField.getText());
                }
            } else if (isInhouse()) {
                try {
                    Inhouse part = new Inhouse();
                    part.setId(Integer.parseInt(partIdField.getText()));
                    part.setName(partNameField.getText());
                    part.setStock(Integer.parseInt(partStockField.getText()));
                    part.setPrice(Double.parseDouble(partPriceField.getText()));
                    part.setMin(Integer.parseInt(partMinField.getText()));
                    part.setMax(Integer.parseInt(partMaxField.getText()));
                    ((Inhouse) part).setMachineId(Integer.parseInt(machineIdField.getText()));                   
                    Inventory.getPart().add(part);
                } catch(Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Valid Part ID");
                    alert.setHeaderText(e.toString());
                    alert.show();
                }
            } else if (isOutsourced()) {
                Outsourced part = new Outsourced();
                part.setId(Integer.parseInt(partIdField.getText()));
                part.setName(partNameField.getText());
                part.setStock(Integer.parseInt(partStockField.getText()));
                part.setPrice(Double.parseDouble(partPriceField.getText()));
                part.setMin(Integer.parseInt(partMinField.getText()));
                part.setMax(Integer.parseInt(partMaxField.getText()));
                ((Outsourced) part).setCompanyName(companyNameField.getText());
                
                Inventory.getPart().add(part);                
            }
            
            okClicked = true;
            dialogStage.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle( "Part Information" );
            alert.setHeaderText("Part Updated");
            if ( part instanceof Inhouse || part instanceof Outsourced) {
                alert.setContentText("Part " + part.getName() + " Successfully Updated"); 
            } else {
               alert.setContentText("Part " + partNameField.getText() + " Successfully Added");  
            }
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
        
        if (partNameField.getText() == null || partNameField.getText().length() == 0) {
            errorMessage += "No valid part name!\n"; 
        }
 
        if (partStockField.getText() == null || partStockField.getText().length() == 0) {
            errorMessage += "No valid inventory Level!\n"; 
        } else {
            // try to parse the stock into an int.
            try {
                Integer.parseInt(partStockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }
        }
        
        if (partPriceField.getText() == null || partPriceField.getText().length() == 0) {
            errorMessage += "No valid price!\n"; 
        } else {
            // try to parse the price into an double.
            try {
                Double.parseDouble(partPriceField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid price (must be an dollar amount)!\n"; 
            }
        }
        
        if (partMaxField.getText() == null || partMaxField.getText().length() == 0) {
            errorMessage += "No valid inventory Level!\n"; 
        } else {
            // try to parse the maximum quantity into an int.
            try {
                Integer.parseInt(partMaxField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid maximum Inventory Level (must be an number)!\n"; 
            }
        }  
        
        if (partMinField.getText() == null || partMinField.getText().length() == 0) {
            errorMessage += "No valid minimum inventory Level!\n"; 
        } else {
            // try to parse the minimum quantity into an int.
            try {
                Integer.parseInt(partMinField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }
        }

        //Stock error
        try {
            int stock = Integer.parseInt(partStockField.getText());
            int min = Integer.parseInt(partMinField.getText());
            int max = Integer.parseInt(partMaxField.getText());
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
        
        // Edit exsisting Inhouse & Outsourced parts
        if (part instanceof Inhouse || isInhouse() ) {
            if ( machineIdField.getText() == null || machineIdField.getText().length() == 0 ) {
                errorMessage += "In-House part. No valid machine ID!\n"; 
            } else {
                // try to parse the machine ID into an int.
                try {
                    Integer.parseInt(machineIdField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid machine ID (must be an number)!\n"; 
                }
            }
        } else if (part instanceof Outsourced || isOutsourced()) {
            if ( companyNameField.getText() == null && companyNameField.getText().length() == 0 ) {
                errorMessage += "Outsourced part. No valid company name!\n"; 
            }            
        }
        
        if(part instanceof Inhouse && isOutsourced() ) {
            errorMessage += "Error! You can change and Inhouse part to an Outsourced part. Please select In-House Part!\n"; 
        }
        if(part instanceof Outsourced && isInhouse() ) {
            errorMessage += "Error! You can change and Outsourced part to an Inhouse part. Please select Outsourced Part!\n"; 
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