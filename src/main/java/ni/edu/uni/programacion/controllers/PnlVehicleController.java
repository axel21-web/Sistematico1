/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.edu.uni.programacion.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import ni.edu.uni.programacion.backend.dao.implementation.JsonVehicleDaoImpl;
import ni.edu.uni.programacion.backend.pojo.Vehicle;
import ni.edu.uni.programacion.backend.pojo.VehicleSubModel;
import ni.edu.uni.programacion.views.panels.PnlVehicle;

/**
 *
 * @author Sistemas-05
 */
public class PnlVehicleController implements ActionListener{
    private PnlVehicle pnlVehicle;
    private JsonVehicleDaoImpl jvdao;
    private List<VehicleSubModel> vSubModels;
    private Gson gson;
    private DefaultComboBoxModel cmbModelMake;
    private DefaultComboBoxModel cmbModelModel;
    private DefaultComboBoxModel cmbModelColor;
    private JFileChooser FC;
    private Vehicle V;
    private String[] STATUS = new String[]{"Active", "Inactive"};
    private DefaultComboBoxModel cmbModelStatus;
    
    public PnlVehicleController(PnlVehicle pnlVehicle) throws FileNotFoundException {
        this.pnlVehicle = pnlVehicle;
        initComponent();        
    }
    
    private void initComponent()  throws FileNotFoundException{
        jvdao = new JsonVehicleDaoImpl();
        gson = new Gson();
        
        pnlVehicle.getBtnBrowse().addActionListener(this);
        pnlVehicle.getBtnSave().addActionListener(this);
        
        JsonReader jreader = new JsonReader(
               new BufferedReader(new InputStreamReader(
                       getClass().getResourceAsStream("/jsons/vehicleData.json")))
        );
        
        Type listType = new TypeToken<ArrayList<VehicleSubModel>>(){}.getType();
        vSubModels = gson.fromJson(jreader, listType);
        
        List<String> makes = vSubModels.stream().map(x -> x.getMake()).collect(Collectors.toList());
        List<String> models = vSubModels.stream().map(x -> x.getModel()).collect(Collectors.toList());
        List<String> colors = vSubModels.stream().map(x -> x.getColor()).collect(Collectors.toList());
        cmbModelMake = new DefaultComboBoxModel(makes.toArray());
        cmbModelModel = new DefaultComboBoxModel(models.toArray());
        cmbModelColor = new DefaultComboBoxModel(colors.toArray());
        cmbModelStatus = new DefaultComboBoxModel(STATUS);
        
        pnlVehicle.getCmbMake().setModel(cmbModelMake);
        pnlVehicle.getCmbModel().setModel(cmbModelModel);
        pnlVehicle.getCmbEColor().setModel(cmbModelColor);
        pnlVehicle.getCmbIColor().setModel(cmbModelColor);
        pnlVehicle.getCmbStatus().setModel(cmbModelStatus);
    }
    
    private void btnSaveActionListener(ActionEvent e){
        int stock, year;
        String make, model, style, vin, eColor, iColor, miles, engine, image, status;
        float price;
        Vehicle.Transmission transmission;
            
        stock = Integer.parseInt(pnlVehicle.getTxtStock().getText());
        year = Integer.parseInt(pnlVehicle.getSpnYear().getValue().toString());
        make = pnlVehicle.getCmbMake().getSelectedItem().toString();
        model = pnlVehicle.getCmbModel().getSelectedItem().toString();
        style = pnlVehicle.getTxtStyle().getText();
        vin = pnlVehicle.getFmtVin().getText();
        eColor = pnlVehicle.getCmbEColor().getSelectedItem().toString();
        iColor = pnlVehicle.getCmbIColor().getSelectedItem().toString();
        miles = pnlVehicle.getSpnMiles().getValue().toString();
        engine = pnlVehicle.getTxtEngine().getText();
        image = pnlVehicle.getTxtImage().getText();
        status = pnlVehicle.getCmbStatus().getSelectedItem().toString();
        price = Float.parseFloat(pnlVehicle.getSpnPrice().getValue().toString());
        transmission = pnlVehicle.getRbtnAut().isSelected()?
                Vehicle.Transmission.AUTOMATIC : Vehicle.Transmission.MANUAL;
        
        V = new Vehicle(stock, year, make, model, style, vin, iColor, iColor, miles, price, transmission, engine, image, status);
        
        try {
            jvdao.create(V);
            JOptionPane.showMessageDialog(null, "Archivo guardado correctamente");
        } catch (Exception ex) {
        }
    }
    
    private void btnBrowserActionListener(ActionEvent e){
        FC = new JFileChooser();
        
        int option = FC.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION){
            return;
        }
        
        File ImageFile = FC.getSelectedFile();
        pnlVehicle.getTxtImage().setText(ImageFile.getPath());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equalsIgnoreCase("Save")){
            btnSaveActionListener(arg0);
        }
        if (arg0.getActionCommand().equalsIgnoreCase("Browse")){
            btnBrowserActionListener(arg0);
        }
    }
}
