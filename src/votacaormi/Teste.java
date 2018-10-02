package votacaormi;
import java.awt.*;
import java.io.Serializable;
import javax.swing.*;
 
public class Teste implements Serializable{
    
    public Teste(){
        
    }
 
    public void Principal() {
 
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
}