

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
import java.util.*;
 
public interface ServerInt extends Remote{	
	public boolean login (ClientInt a)throws RemoteException ;
	public void publish (String s)throws RemoteException ;
	public Vector getConnected() throws RemoteException ;
        public HashSet<CandidatoInt> getCandidatos() throws RemoteException;
        public boolean votar(Integer numero) throws RemoteException;
        public void calcularParcial() throws RemoteException;
         public HashMap<CandidatoInt, Integer> getVotos() throws RemoteException;
}
