

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Professor-
 */
import javax.swing.*;
import javax.swing.border.*;
 
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
 
public class VotacaoUI extends JFrame{
  private Client client;
  private ServerInt server;
   
  public void Grafico () {
 
        //janela do programa
      
      
        JFrame frame2 = new JFrame("Sistema de Votação");
        frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //container onde serão adicionados todos componentes
        Container container = frame2.getContentPane();
        //carrega a imagem passando o nome da mesma
        ImageIcon img1 = new ImageIcon("Votacao.jpg");
        Image img = img1.getImage();
        img.flush();
        //pega a altura e largura
        //adiciona a imagem em um label
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(img));
        
        //cria o JPanel para adicionar os labels
        JPanel panel = new JPanel();
        panel.add(label, BorderLayout.NORTH);
        
        //adiciona o panel no container
        container.add(panel, BorderLayout.CENTER);
         
        frame2.pack();
        frame2.repaint();
        frame2.setVisible(true);
        
 
        //pronto e simples
 
    }
  
 
  public void capturarVotos() {

        boolean exit = false;

        try {
            final Scanner teclado = new Scanner(System.in);
            
            System.out.println("##########################################");
            System.out.println("## Escolha um candidato e em seguida    ##");
            System.out.println("## digite o numero correspondente a ele ##");
            System.out.println("## para contabilizar 1 voto             ##");
            System.out.println("## Exemplo:                             ##");
            System.out.println("##    Candidato Jose, numero 9876       ##");
            System.out.println("## Digite 9876 no console:                ##");
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
                    
                    this.calcularParcial();
                    
                }else{
                
                    try {
                        Integer numero = Integer.parseInt(voto);
                        this.server.votar(numero);
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
  
     
    public void calcularParcial() throws RemoteException{
        
        
        System.out.println("\nurna@localhost$> Parcial: ");
        
        
        HashMap<CandidatoInt, Integer> votos = this.server.getVotos();
        DefaultPieDataset dpd = new DefaultPieDataset();
                 
        votos.keySet().stream().forEach((candidato) -> {
            try {
                System.out.println(String.format("Candidato %s "
                        + "recebeu %s votos", candidato.getNome(), votos.get(candidato)));
            } catch (RemoteException ex) {
                Logger.getLogger(VotacaoUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
                dpd.setValue(candidato.getNome(), votos.get(candidato));
            } catch (RemoteException ex) {
                Logger.getLogger(VotacaoUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
         JFreeChart grafico = ChartFactory.createPieChart("Sistema de Votacao", dpd, true, true, true);
         
        //ChartPanel chartPanel = new ChartPanel(grafico);
        try {
	  ChartUtilities.saveChartAsJPEG(
          new java.io.File("Votacao.jpg"), grafico, 500, 300);
	} catch (java.io.IOException exc) {
	    System.err.println("Error writing image to file");
	}       
        
       
    }
  
    private void mostrarCandidatos() throws Exception {
        
        final Set<CandidatoInt> candidatos = this.server.getCandidatos();
       
        candidatos.stream().forEach((candidato) -> {
            try {
                System.out.println(String.format("-> Nome %s, número %s",
                        candidato.getNome(), candidato.getNumero()));
                this.client.tell("Candidato: " +candidato.getNome() +" - Número: " + candidato.getNumero());
            } catch (RemoteException ex) {
                Logger.getLogger(VotacaoUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
  
  
  public void doConnect(){
	    if (connect.getText().equals("Conectar")){
	    	if (name.getText().length()<2){JOptionPane.showMessageDialog(frame, "Você precisa inserir um nome."); return;}
	    	try{
                    client=new Client(name.getText());
                    client.setGUI(this);
                    server=(ServerInt)Naming.lookup("rmi://192.168.125.1/myabc");
                    server.login(client);
                    
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            capturarVotos();
                        }
                    }).start();
                    connect.setText("Desconectar");	
                    
	    	}catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERRO, Não foi possível conectar....");}		  
	      }else{
	    	  	connect.setText("Conectar");
	    	  	//Better to implement Logout ....
		}
	  }  
  
  public void sendText(){
    if (connect.getText().equals("Conectar")){
    	JOptionPane.showMessageDialog(frame, "Você precisa se conectar primeiro."); return;	
    }
      String st=tf.getText();
      tf.setText("");
      //Remove if you are going to implement for remote invocation
      try{
    	  	server.publish(st);
  	  	}catch(Exception e){e.printStackTrace();}
  }
 
  public void writeMsg(String st){  tx.setText(tx.getText()+"\n"+st);  }
 
  public void updateUsers(Vector v){
      DefaultListModel listModel = new DefaultListModel();
      if(v!=null) for (int i=0;i<v.size();i++){
    	  try{  String tmp=((ClientInt)v.get(i)).getName();
    	  		listModel.addElement(tmp);
    	  }catch(Exception e){e.printStackTrace();}
      }
      lst.setModel(listModel);
  }
  
  public static void main(String [] args){
	System.out.println("Trabalho de Sistemas Distribuídos – Professora Carla Lara");
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                new VotacaoUI();
            }
        }).start();
	

        
        
        
  }  
  
  
  //User Interface code.
  public VotacaoUI(){        
            frame=new JFrame("Trabalho de Sistemas Distribuídos – Professora Carla Lara");
	    JPanel main =new JPanel();
	    JPanel top =new JPanel();
	    JPanel cn =new JPanel();
	    JPanel bottom =new JPanel();
	    tf=new JTextField();
	    name=new JTextField();
	    tx=new JTextArea();
	    connect=new JButton("Conectar");
	    JButton bt=new JButton("Enviar Voto");
            JButton gf=new JButton("Parcial dos Votos");
	    lst=new JList();        
	    main.setLayout(new BorderLayout(5,5));         
	    top.setLayout(new GridLayout(1,1));   
	    cn.setLayout(new BorderLayout(5,5));
	    bottom.setLayout(new GridLayout(1,1,1,1));
	    top.add(new JLabel("Entre com o seu nome para votar: "),BorderLayout.CENTER);
            top.add(name,BorderLayout.CENTER);    
	    top.add(connect,BorderLayout.CENTER);
	    cn.add(new JScrollPane(tx), BorderLayout.CENTER);        
	    cn.add(lst, BorderLayout.EAST); 
            bottom.add(new JLabel("Digite aqui o seu voto: "), BorderLayout.WEST);
	    bottom.add(tf, BorderLayout.CENTER);    
	    bottom.add(bt, BorderLayout.EAST);
            bottom.add(gf, BorderLayout.SOUTH);
	    main.add(top, BorderLayout.NORTH);
	    main.add(cn, BorderLayout.CENTER);
	    main.add(bottom, BorderLayout.SOUTH);
	    main.setBorder(new EmptyBorder(10, 10, 10, 10) );
	    //Events
	    connect.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent e){ doConnect();   }  });
	    bt.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent e){ sendText();   }  });
	    tf.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent e){ sendText();   }  });
            gf.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                     try {
                         calcularParcial();
                     } catch (RemoteException ex) {
                         Logger.getLogger(VotacaoUI.class.getName()).log(Level.SEVERE, null, ex);
                     }
                       Grafico();              
                                                
                                     
                                    
                    }  });
	    frame.setContentPane(main);
	    frame.setSize(630,600);
	    frame.setVisible(true);  
	  }
	  JTextArea tx;
	  JTextField tf,ip, name;
	  JButton connect;
	  JList lst;
	  JFrame frame;
          
}
