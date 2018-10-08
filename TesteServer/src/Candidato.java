

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 22/03/2015
 */
public class Candidato  extends UnicastRemoteObject implements CandidatoInt{

    private String nome;
    private Integer numero;

    /**
     * 
     * @param nome
     * @param numero 
     */
    public Candidato(String n, Integer num) throws RemoteException {
        nome = n;
        numero = num;
    }
    
    /**
     * @return the nome
     */
    public String getNome(){
        return nome;
    }

    /**
     * @return the numero
     */
    public Integer getNumero(){
        return numero;
    }
}
