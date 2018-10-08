

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Professor-
 */
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
 
public class Server  extends UnicastRemoteObject implements ServerInt{
	
	private Vector v=new Vector();	
	public Server() throws RemoteException{}
        
         private HashMap<Candidato, Integer> votos;
    
    /**
     * 
     * @throws RemoteException 
     */
    
    /**
     * 
     * @return
     * @throws RemoteException 
     */
    public HashSet<CandidatoInt> getCandidatos() throws RemoteException {
        return new HashSet<>(this.votos.keySet());
    }
    
    public HashMap<CandidatoInt, Integer> getVotos() throws RemoteException {
        return new HashMap<>(this.votos);
    }
    
    /**
     * 
     * @param numero
     * @return
     * @throws RemoteException 
     */
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
		
	public boolean login(ClientInt a) throws RemoteException{	
		System.out.println(a.getName() + "  está conectado....");	
		a.tell("Lista de Candidatos: ");
		return true;		
	}
	
	public void publish(String s) throws RemoteException{
            
            votar(Integer.parseInt(s));
	    /*System.out.println(s);
		for(int i=0;i<v.size();i++){
		    try{
		    	ClientInt tmp=(ClientInt)v.get(i);
			tmp.tell(s);
		    }catch(Exception e){
		    	//problem with the client not connected.
		    	//Better to remove it
		    }
		}*/
	}
 
	public Vector getConnected() throws RemoteException{
		return v;
	}
}
