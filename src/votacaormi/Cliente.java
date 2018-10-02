package votacaormi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 22/03/2015
 */
public class Cliente implements Runnable {

    private IUrna urna;

    /**
     *
     */
    @Override
    public void run() {
        try {
            this.principal();
            this.urna = this.getUrnaService();
            System.out.println("cliente@localhost$> Urna localizada, pronto para votar!");
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }
        
        this.capturarVotos();
        
    }

    public void principal() {
 
        //janela do programa    
        JFrame frame = new JFrame("Carregar Imagem");
        //container onde serão adicionados todos componentes
        Container container = frame.getContentPane();
 
        //carrega a imagem passando o nome da mesma
        ImageIcon img = new ImageIcon("Sim.jpg");
         
        //pega a altura e largura
        //adiciona a imagem em um label
        JLabel label = new JLabel(img);
        //adiciona a altura e largura em outro label
        
        //cria o JPanel para adicionar os labels
        JPanel panel = new JPanel();
        panel.add(label, BorderLayout.NORTH);
        
        //adiciona o panel no container
        container.add(panel, BorderLayout.CENTER);
         
        frame.pack();
        frame.setVisible(true);
 
        //pronto e simples
 
    }
    /**
     *
     */
    public void capturarVotos() {

        boolean exit = false;

        try {
            final Scanner teclado = new Scanner(System.in);
            
            
           
            System.out.println("##########################################");
            System.out.println("## Escolha um candidato e em seguida    ##");
            System.out.println("## digite o numero correspondente a ele ##");
            System.out.println("## para contabilizar 1 voto             ##");
            System.out.println("## Exemplo:                             ##");
            System.out.println("##    Candidato arthur, numero 29       ##");
            System.out.println("## Digite 29 no console:                ##");
            System.out.println("## cliente@localhost$> 29 [enter]       ##");
            System.out.println("##########################################");

            while (!exit) {
                
                System.out.println("\n## Candidatos ##");
                this.mostrarCandidatos();
                System.out.println("################");

                System.out.print("cliente@localhost$> ");
                final String voto = Console.readCommand(teclado);
                
                if(voto.equals("/parcial")){
                    System.out.println("\n## Parcial dos votos ##");
                    this.urna.calcularParcial();
                    
                }else{
                
                    try {
                        Integer numero = Integer.parseInt(voto);
                        this.urna.votar(numero);
                    } catch (NumberFormatException ex) {
                        System.err.println("cliente@localhost$> voto invalido!");
                    }
                }
                
                
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * 
     * @throws Exception 
     */
    private void mostrarCandidatos() throws Exception {
        
        final Set<Candidato> candidatos = this.urna.getCandidatos();
       
        candidatos.stream().forEach((candidato) -> {
            System.out.println(String.format("-> Nome %s, número %s", 
                    candidato.getNome(), candidato.getNumero()));
        });
        
    }
    
    /**
     *
     * @return @throws MalformedURLException
     */
    private IUrna getUrnaService() throws Exception {
        return (IUrna) Naming.lookup("rmi://localhost/Urna");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Cliente());
    }
}
