

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
import java.rmi.server.*;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
 
public class StartServer {
    
        private Server urna;
        private ScheduledExecutorService executorService;
        
        public void Startserver(){
            
        }
        
	public static void main(String[] args) {
		try {
				//System.setSecurityManager(new RMISecurityManager());
			 	java.rmi.registry.LocateRegistry.createRegistry(1099);
			 	
                                ServerInt b= new Server();
                                
                                StartServer ini = new StartServer();
                                b= ini.prepararUrna();
                                Naming.rebind("rmi://192.168.125.1/myabc", b);
                                System.out.println("[Sistema] O servidor de chat foi iniciado.");
                                System.out.println("Trabalho de Sistemas Distribuídos – Professora Carla Lara");
                                ini.iniciarApurador();
                                
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ini.escutarComandos();
                                    }
                                }).start();
 
			}catch (Exception e) {
					System.out.println("O servidor de chat falhou : " + e);
			}
	}
               
        private Server prepararUrna() throws RemoteException {

        final Server urnaVazia = new Server();

        System.out.println("## Iniciando Urna ##\n");
        
        System.out.println("urna@localhost$> Informe os candidados desta eleição seguinte o seguinte esquema:");
        System.out.println("urna@localhost$> nome:numero,nome:numero ...");
        System.out.println("urna@localhost$> Quando inserir todos, tecle [enter] para gravar");
        System.out.print("urna@localhost$> ");
        
        final String comando = Console.readCommand(new Scanner(System.in));
        
        if (comando != null && !comando.isEmpty()) {
            
            final String[] arrayCandidatos = comando.split(",");
            
            for (String dadosCandidato : arrayCandidatos) {
                
                final String nome = dadosCandidato.split(":")[0];
                final String numero = dadosCandidato.split(":")[1];
                
                urnaVazia.adicionaCandidato(new Candidato(nome, 
                        Integer.parseInt(numero)));
            }
        }
        
        this.mostrarCandidatos(urnaVazia);
        
        this.urna=urnaVazia;
        
        return urnaVazia;
    }

    /**
     * 
     */
    private void escutarComandos() {

        boolean exit = false;
        
        try {
            final Scanner teclado = new Scanner(System.in);

            System.out.println("\n## Comandos ##");
            System.out.println("/candidatos = ver candidatos");
            System.out.println("/finalizar = fecha votacao e contabiliza\n");

            while (!exit) {
                System.out.print("urna@localhost$> ");

                final String comando = Console.readCommand(teclado);

                switch (comando) {
                    case "/finalizar":
                        this.urna.calcularParcial();
                        this.executorService.shutdown();
                        System.exit(0);
                        break;
                    case "/candidatos":
                        this.mostrarCandidatos(this.urna);
                        break;
                      
                    default:
                        System.err.println("urna@localhost$> Comando desconhecido!");
                        break;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * 
     */
    private void iniciarApurador() {
       
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        
        this.executorService.scheduleAtFixedRate(() -> 
            { try {
                urna.calcularParcial();
            } catch (RemoteException ex) {
                Logger.getLogger(StartServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            }, 5, 5, TimeUnit.SECONDS);
    }
    
    /**
     * 
     * @throws Exception 
     */
    private void mostrarCandidatos(Server urna) throws RemoteException {
        
        
        final Set<CandidatoInt> candidatos = urna.getCandidatos();
        
        candidatos.stream().forEach((candidato) -> {
            try {
                System.out.println(String.format("-> Nome %s, número %s",
                        candidato.getNome(), candidato.getNumero()));
            } catch (RemoteException ex) {
                Logger.getLogger(StartServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
