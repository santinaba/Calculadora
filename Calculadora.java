package calculadora;

import java.util.Stack;
import javax.swing.*;
import java.awt.event.*;

public class Calculadora extends JFrame implements ActionListener
{
    
    JButton Calcular;
    JTextField Entrada;
    JTextField Salida;
    JTextField Post;
    Calculadora()
    {
        setLayout(null);
        setBounds(500,300,450,350);
        setTitle("Calculadora de expresiones");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Entrada =new JTextField();
        Entrada.setBounds(150,80,150,30);
        add(Entrada);
        
        Salida =new JTextField();
        Salida.setBounds(150,180,150,30);
        add(Salida);
        
        Post= new JTextField();
        Post.setBounds(150,230,150,30);
        Post.setText("***PostFija***");
        add(Post);
        
        Calcular= new JButton("Calcular");
        Calcular.setBounds(180,130,100,30);
        add(Calcular);
        Calcular.addActionListener(this);
        
        setVisible(true);
    }    
               
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==Calcular)
        { 
            String expr=Entrada.getText();
            InfijaAPostfija obj=new InfijaAPostfija();
            String cad=obj.Depurar(expr); 
            String vcad[]=cad.split(" "); // vector de cadenas
            obj.LlenarPila(vcad);
            obj.ConvertirDeinaPost();
            //System.out.println(obj.ImprimirPostfija());
            Post.setText(obj.ImprimirPostfija()); 
            String exp=obj.ImprimirPostfija();
            exp=exp.substring(1);
            String[] post = exp.split(" ");
            Stack < String > X = new Stack < String > ();
            Stack < String > Y = new Stack < String > ();
            for (int i = post.length - 1; i >= 0; i--) 
            {
                X.push(post[i]);
            }
            String operadores = "+-*/^|";
            while (!X.isEmpty()) 
            {
                if(X.peek().equals("s")||X.peek().equals("c")||X.peek().equals("t"))
                {
                    Y.push(evalfunc(X.pop(),Y.pop())+"");
                }
                else
                {
                    if (operadores.contains("" + X.peek())) 
                    {
                        Y.push(evaluar(X.pop(), Y.pop(), Y.pop()) + "");
                    }
                    else 
                    {
                        Y.push(X.pop());
                    }
                }
            }
            Salida.setText(Y.peek());
        }
    }
    static Double evalfunc(String op, String n)
    {
        Double Num=Double.parseDouble(n);
        if(op.equals("s"))
        {
            return (Math.sin(Num));
        }
         if(op.equals("c"))
        {
            return (Math.cos(Num));
        }
          if(op.equals("t"))
        {
            return (Math.tan(Num));
        }
            
        return 0.0;    
    }
    static Double evaluar(String op, String n2, String n1)
    {
        //analizador semantico
        Double num1 = Double.parseDouble(n1);
        Double num2 = Double.parseDouble(n2);
        if (op.equals("+"))
            return (num1 + num2);
        if (op.equals("-"))
            return (num1 - num2);
        if (op.equals("*"))
            return (num1 * num2);
        if (op.equals("/"))
            return (num1 / num2);
        if (op.equals("^"))
            return (Math.pow(num1, num2));
        if (op.equals("|"))
            return (Math.pow(num1,(1/num2)));       
            
        return 0.0;
    }
    public static void main(String[] args) 
    {
        Calculadora cal=new Calculadora();     
    }
}