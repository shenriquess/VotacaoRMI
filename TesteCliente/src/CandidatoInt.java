/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;

/**
 *
 * @author Professor-
 */
public interface CandidatoInt extends Remote {
    public String getNome() throws RemoteException;
    public Integer getNumero()throws RemoteException;
    
}
