package satyamconsignment.ui.Report.SupplierLedger;

import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;

public class SupplierLedgerController implements Initializable {
    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> supplierList;
    String jrxmlFileName,pdfFileName;
    Map map;
    
    
    @FXML
    private ComboBox<String> supplier_name_combo;
    @FXML
    private ToggleGroup choice;
    @FXML
    private JFXRadioButton supplier_ledger_radio;
    @FXML
    private JFXRadioButton agewise_outstanding_radio;
    @FXML
    private Button generate_pdf_btn;
    @FXML
    private Button launch_pdf_btn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            pdfFileName="Report.pdf";
            rrc=new Rrc();
            databaseHandler=DatabaseHandler.getInstance();
            conn=databaseHandler.getConnection();
            ps=conn.prepareStatement("select * from Supplier_Master_Table order by name collate nocase");
            rs=ps.executeQuery();
            supplierList = FXCollections.observableArrayList();
            while(rs.next())
            {
                supplierList.add(rs.getString("name"));
            }
            supplier_name_combo.setItems(supplierList);
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void generatePDF(ActionEvent event) {
        try {
            if(agewise_outstanding_radio.isSelected())
            {
                jrxmlFileName = "SupplierLedgerAge.jrxml";
            } else {
                jrxmlFileName = "SupplierLedger.jrxml";
            }
           JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
           map=new HashMap();
           map.put("supplierName", supplier_name_combo.getSelectionModel().getSelectedItem());
           JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
           JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
           rrc.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void launchPdf(ActionEvent event) {
        /*try {
            //Runtime.getRuntime().exec("cmd start Report.pdf");
            Desktop.getDesktop().open("Report.pdf");
        } catch (IOException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
}
