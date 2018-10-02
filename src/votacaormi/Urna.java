package votacaormi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 22/03/2015
 */
public class Urna extends UnicastRemoteObject implements IUrna {

    private HashMap<Candidato, Integer> votos;

    /**
     * 
     * @throws RemoteException 
     */
    public Urna() throws RemoteException { }
    
    /**
     * 
     * @return
     * @throws RemoteException 
     */
    @Override
    public HashSet<Candidato> getCandidatos() throws RemoteException {
        return new HashSet<>(this.votos.keySet());
    }
    
    /**
     * 
     * @param numero
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean votar(Integer numero) throws RemoteException {
        
        this.votos.keySet().stream().filter((candidato) -> 
                (candidato.getNumero().equals(numero))).forEach((candidato) -> {
            this.votos.put(candidato, this.votos.get(candidato) + 1);
        });
        
        
        
        return true;
    }

    /**
     * 
     * @param candidato 
     */
    public void adicionaCandidato(Candidato candidato) {
        
        if (this.votos == null) {
            this.votos = new HashMap<>();
        }
        
        this.votos.put(candidato, 0);
    }

    /**
     * 
     */
    
    /**
     *
     * @throws java.rmi.RemoteException
     */
    @Override
    public void calcularParcial() throws RemoteException{
        
        
        System.out.println("\nurna@localhost$> Parcial: ");
        
        
        DefaultPieDataset dpd = new DefaultPieDataset();
        
        

        

            
        
                
        this.votos.keySet().stream().forEach((candidato) -> {
            System.out.println(String.format("Candidato %s "
                    + "recebeu %s votos", candidato.getNome(), this.votos.get(candidato)));
            
            
            dpd.setValue(candidato.getNome(), this.votos.get(candidato));
            
            
        });
        
         JFreeChart grafico = ChartFactory.createPieChart("Nome do Grafico", dpd, true, true, true);
         
        //ChartPanel chartPanel = new ChartPanel(grafico);
        try {
	  ChartUtilities.saveChartAsJPEG(
          new java.io.File("Sim.jpg"), grafico, 500, 300);
	} catch (java.io.IOException exc) {
	    System.err.println("Error writing image to file");
	}       
        
       
    }
}
