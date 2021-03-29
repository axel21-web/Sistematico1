package ni.edu.uni.programacion.controllers;

import com.google.gson.Gson;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import ni.edu.uni.programacion.backend.dao.implementation.JsonVehicleDaoImpl;
import ni.edu.uni.programacion.views.panels.PnlViewVehicle;

/**
 *
 * @author Axel Moreno
 */
public class PnlViewVehiclesController
{	
	private PnlViewVehicle pnlViewVehicle;
	private JsonVehicleDaoImpl jvdao;
	private final String PROPIERTIES[] = new String[]{"STOCK","YEAR", "MAKE", "MODEL", "STYLE", "VIN", "EXTERIOR COLOR", "INTERIOR COLOR", "MILES", "PRICE", "TRANSMISSION", "ENGINE", "IMAGE", "STATUS"};
	private DefaultComboBoxModel<String> cmbFmodel;
	private DefaultTableModel model;
	private Gson gson;

	public PnlViewVehiclesController(PnlViewVehicle pnlViewVehicle)
        {
		this.pnlViewVehicle = pnlViewVehicle;
		try 
                {
			initComponent();
		} catch (IOException ex) 
                {                  
			Logger.getLogger(PnlViewVehiclesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void initComponent() throws FileNotFoundException, IOException
        {
       
		jvdao = new JsonVehicleDaoImpl();
		if(jvdao.getAll().isEmpty())
                {
			return;
	        }
        
		String M[][]= new String[jvdao.getAll().size()][14];
        
		for(int i = 0; i < jvdao.getAll().size(); i++){
			M[i][0] = Integer.toString(jvdao.getAll().get(i).getStockNumber());
			M[i][1] = Integer.toString(jvdao.getAll().get(i).getYear());
			M[i][2] = jvdao.getAll().get(i).getMake();
			M[i][3] = jvdao.getAll().get(i).getModel();
			M[i][4] = jvdao.getAll().get(i).getStyle();
			M[i][5] = jvdao.getAll().get(i).getVin();
			M[i][6] = jvdao.getAll().get(i).getExteriorColor();
			M[i][7] = jvdao.getAll().get(i).getInteriorColor();
			M[i][8] = jvdao.getAll().get(i).getMiles();
			M[i][9] = Float.toString(jvdao.getAll().get(i).getPrice());
			M[i][10] = jvdao.getAll().get(i).getTransmission().toString();
			M[i][11] = jvdao.getAll().get(i).getEngine();
			M[i][12] = jvdao.getAll().get(i).getImage();
			M[i][13] = jvdao.getAll().get(i).getStatus();
		}
        
		model= new DefaultTableModel(M, PROPIERTIES);
		pnlViewVehicle.getTbVehicles().setModel(model);
		gson = new Gson();
		cmbFmodel = new DefaultComboBoxModel<>(PROPIERTIES);
        
		pnlViewVehicle.getCmbSearch().setModel(cmbFmodel);
        
		pnlViewVehicle.getTxtSearch().addKeyListener(new KeyAdapter()
                {
			public void K(final KeyEvent e)
                        {
				TableRowSorter TFilter = new TableRowSorter(pnlViewVehicle.getTbVehicles().getModel());
				String s = pnlViewVehicle.getTxtSearch().getText();
				FilterTabe(pnlViewVehicle.getCmbSearch().getSelectedIndex(), TFilter);
			}
		});
	}
	
	private void FilterTabe(int a, TableRowSorter Filter)
        {
		Filter.setRowFilter(RowFilter.regexFilter(pnlViewVehicle.getTxtSearch().getText(), a));
		pnlViewVehicle.getTbVehicles().setRowSorter(Filter);
	} 
}