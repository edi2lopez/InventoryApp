
package inventory;

import inventory.model.Inhouse;
import inventory.model.Inventory;
import inventory.model.Outsourced;
import inventory.model.Part;
import inventory.model.Product;
import inventory.view.InventoryOverviewController;
import inventory.view.PartEditDialogController;
import inventory.view.ProductEditDialogController;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author el
 */
public class MainApp extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("InventoryApp");
        
        initRootLayout();
        showInventoryOverview();
        
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the part overview inside the root layout.
     */
    public void showInventoryOverview() {
        try {
            // Load inventory overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/InventoryOverview.fxml"));
            AnchorPane inventoryOverview = (AnchorPane) loader.load();

            // Set inventory overview into the left of root layout.
            rootLayout.setLeft(inventoryOverview);
            
            // Give the controller access to the main app.
            InventoryOverviewController controller = loader.getController();
            controller.setMainApp(this);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Opens a dialog to edit details for the specified product. If the user
    * clicks OK, the changes are saved into the provided product object and true
    * is returned.
    * 
    * @param product the product object to be edited
    * @return true if the user clicked OK, false otherwise.
    */
    public boolean showProductEditDialog(Product product) {
           try {
           // Load the fxml file and create a new stage for the popup dialog.
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(MainApp.class.getResource("view/ProductEditDialog.fxml"));
           AnchorPane page = (AnchorPane) loader.load();

           // Create the dialog Stage.
           Stage dialogStage = new Stage();
           dialogStage.setTitle("Edit Product");
           dialogStage.initModality(Modality.WINDOW_MODAL);
           dialogStage.initOwner(primaryStage);
           Scene scene = new Scene(page);
           dialogStage.setScene(scene);

           // Set the product into the controller.
           ProductEditDialogController controller = loader.getController();
           controller.setProduct(product);
           controller.setDialogStage(dialogStage, product);

           // Show the dialog and wait until the user closes it
           dialogStage.showAndWait();

           return controller.isOkClicked();
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
    }
    
    /**
    * Opens a dialog to edit details for the specified part. If the user
    * clicks OK, the changes are saved into the provided part object and true
    * is returned.
    * 
    * @param part the part object to be edited
    * @return true if the user clicked OK, false otherwise.
    */
    public boolean showPartEditDialog(Part part) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PartEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Part");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the part into the controller.
            PartEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPart(part);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    /**
    * @return true if the user clicked OK, false otherwise.
    */
    public boolean showNewPartEditDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PartEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Part");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the part into the controller.
            PartEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setNewPart();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

   /**
     * Returns the main stage.
     * @return
    */
   public Stage getPrimaryStage() {
        return primaryStage;
   }    

   /**
     * @param args the command line arguments
    */
   public static void main(String[] args) {
       setupData();
       launch(args);       
   }
   
   /**
    * Add Sample data for testing
    */
   private static void setupData() {

        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();
        
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);
        Inventory.addProduct(product5);
                
        product1.setId(1);
        product2.setId(2);
        product3.setId(3);
        product4.setId(4);
        product5.setId(5);
        
        product1.setName("Product 1");
        product2.setName("Product 2");
        product3.setName("Product 3");
        product4.setName("Product 4");
        product5.setName("Product 5");
        
        product1.setStock(15);
        product2.setStock(20);
        product3.setStock(25);
        product4.setStock(35);
        product5.setStock(40);
        
        product1.setPrice(10.50);
        product2.setPrice(20.50);
        product3.setPrice(30.50);
        product4.setPrice(40.50);
        product5.setPrice(50.50);
        
        product1.setMin(10);
        product2.setMin(9);
        product3.setMin(8);
        product4.setMin(7);
        product5.setMin(6);
        
        product1.setMax(50);
        product2.setMax(60);
        product3.setMax(70);
        product4.setMax(80);
        product5.setMax(90);
        
        Outsourced part1 = new Outsourced();
        Outsourced part2 = new Outsourced();
        Outsourced part3 = new Outsourced();
        Inhouse part4 = new Inhouse();
        Inhouse part5 = new Inhouse();

        part1.setId(1);
        part2.setId(2);
        part3.setId(3);
        part4.setId(4);
        part5.setId(5);
        
        part1.setName("Part 1");
        part2.setName("Part 2");
        part3.setName("Part 3");
        part4.setName("Part 4");
        part5.setName("Part 5");
        
        part1.setStock(100);
        part2.setStock(150);
        part3.setStock(200);
        part4.setStock(250);
        part5.setStock(300);
        
        part1.setPrice(1.50);
        part2.setPrice(2.50);
        part3.setPrice(3.50);    
        part4.setPrice(4.50);  
        part5.setPrice(5.50);  
        
        part1.setMin(50);
        part2.setMin(45);
        part3.setMin(30);
        part4.setMin(25);
        part5.setMin(20);
        
        part1.setMax(500);
        part2.setMax(550);
        part3.setMax(600);
        part4.setMax(650);
        part5.setMax(400);
        
        part1.setCompanyName("company A");
        part2.setCompanyName("Company B");
        part3.setCompanyName("Company B");
        part4.setMachineId(567);
        part5.setMachineId(678);
        
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Inventory.addPart(part5);
        
        product1.addPartId(1);
        product1.addPartId(2);
        product1.addPartId(3);

        product2.addPartId(3);
        product2.addPartId(4);
        product2.addPartId(5);
        
        product3.addPartId(1);
        product3.addPartId(5);
        
        product4.addPartId(3);
        
        product5.addPartId(5);

   }
    
}