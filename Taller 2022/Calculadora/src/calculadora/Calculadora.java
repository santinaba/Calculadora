package calculadora;

import java.util.Stack;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.EmptyStackException;

public class Calculadora extends JFrame implements ActionListener
{
    JButton Calcular;
    JTextField Entrada;
    JTextField EntradaX;
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
        Entrada.setBounds(150,50,150,30);
        add(Entrada);
        
        EntradaX=new JTextField();
        EntradaX.setBounds(150,90,150,30);
        EntradaX.setText("Valor de X");
        add(EntradaX);
        
        
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
    //  2x+2x+1-2*5 = 4x-9         
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==Calcular)
        { 
            String expr=Entrada.getText();
            
            String Exp=expr;
            
            if(Verx(expr))
            {
                String X=EntradaX.getText();
                Exp=Nuevacad(expr,X);
            }
           
            System.out.println("CADENA->>> "+Exp);
            
            InfijaAPostfija obj=new InfijaAPostfija();
            String cad=obj.Depurar(Exp); 
          
            String vcad[]=cad.split(" "); // vector de cadenas
            obj.LlenarPila(vcad);
            obj.ConvertirDeinaPost();
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
                if(X.peek().equals("s")||X.peek().equals("c")||X.peek().equals("t")||X.peek().equals("i")||X.peek().equals("o")||X.peek().equals("p"))
                {
                    try{
                    Y.push(evalfunc(X.pop(),Y.pop())+"");
                                           // System.out.println(Y)
;
                    }catch (EmptyStackException x)
                        {
                            Entrada.setText(" ");
                            Salida.setText(" ");
                            Post.setText(" ");
                            JOptionPane.showMessageDialog(null, "Error ded sintaxis!");
                        }
                }
                else
                {
                    if (operadores.contains("" + X.peek())) 
                    {
                        try{
                            Y.push(evaluar(X.pop(), Y.pop(), Y.pop()) + "");
                        }catch (EmptyStackException x)
                        {
                            Entrada.setText(" ");
                            Salida.setText(" ");
                            Post.setText(" ");
                            JOptionPane.showMessageDialog(null, "Error ded sintaxis!");
                            
                        }
                    }
                    else 
                    {
                        Y.push(X.pop());
                    }
                }
            }
            try{
                if(Y.peek().equals("Infinity"))
                    JOptionPane.showMessageDialog(null, "Imposible dividir entre 0");
                Salida.setText(Y.peek());
            }
            catch(EmptyStackException x)
            {
                Entrada.setText(" ");
                Salida.setText(" ");
                Post.setText(" ");
            }
        }
    }
    
    static String Nuevacad(String rar, String VR)
    {
        char cad[]= rar.toCharArray();
        String cade="";
        System.out.println("longitud de cadena ---> "+ cad.length );
        if(cad.length==2&&(cad[0]+0 <= 97 && cad[0]+0 >= 122))
        {
             JOptionPane.showMessageDialog(null, "Error ded sintaxis!");
             return "0";
        }
        else    
        {
            if(cad[0]+0==120)
            {
                JOptionPane.showMessageDialog(null, "Error ded sintaxis!");
                return "0";
            }
        }

        for(int i=0;i<cad.length;i++)
        {
            if(cad[i]=='x')
            {
                //try{
                    if(i!=0)
                    {

                        if(cad[i-1]+0>=48&&cad[i-1]+0<=57)
                        {
                            cade=cade+"*"+VR;
                        }
                        if(cad[i-1]+0>=42&&cad[i-1]+0<=47)
                        {
                            cade=cade+VR;
                        }
                        if(cad[i-1]+0>=97&&cad[i-1]+0<=122)
                        {
                            cade=cade+VR;
                        }

                        if(cad[i-1]+0==124)
                        {
                            cade=cade+VR;
                        }
                        if(cad[i-1]+0==94)
                        {
                            cade=cade+VR;
                        }
                    }
            }   
            else
                cade=cade+cad[i];
        }
        return cade;
    }
    static boolean Verx(String cad)
    {   
        for(int i=0;i<cad.length();i++)
        {
            if(cad.charAt(i)=='x')
            {
                return true;
            }
        }
        
        return false;
    }
    static Double evalfunc(String op, String n)
    {
        Double Num=Double.parseDouble(n)
;
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
        if(op.equals("i"))
        {
            return (Math.sinh(Num));
        }
        if(op.equals("o"))
        {
            return (Math.cosh(Num));
        }
        if(op.equals("p"))
        {
            return (Math.tanh(Num));
        }
            
        return 0.0;    
    }
    
    static Double evaluar(String op, String n2, String n1)
    {
        //analizador semantico
       try{
        Double num1 = Double.parseDouble(n1);
        Double num2 = Double.parseDouble(n2);
        
        if (op.equals("+"))
            return (num1 + num2);
        if (op.equals("-"))
            return (num1 - num2);
        if (op.equals("*"))
            return (num1 * num2);
        if (op.equals("/"))
        {
            try{
                return (num1 / num2);
            }catch(ArithmeticException g)
            {
                JOptionPane.showMessageDialog(null, "Imposible dividir entre 0");
            }
        }
            
        if (op.equals("^"))
            return (Math.pow(num1, num2));
        if (op.equals("|"))
            return (Math.pow(num1,(1/num2)));       
        }
        catch(NumberFormatException x)
                {
                    JOptionPane.showMessageDialog(null, "Introduzca el valor de X");
                }
        return 0.0;
    }
    public static void main(String[] args) 
    {
        Calculadora cal=new Calculadora();     
    }
}