package calculadora;

import java.util.*;
public class InfijaAPostfija
{

    public Stack E = new Stack();
    public Stack P = new Stack();
    public Stack S = new Stack ();
    
    public void LlenarPila(String J[])
    {
        for (int i = J.length - 1; i >= 0; i--)
        {
            this.E.push(J[i]);
            System.out.println(E);
        }
        
    }
    public String Depurar(String s)
    {
        s = "(" + s + ")";
        String simbolos = "+-*/^|()sctiop";
        String str = "";

        for (int i = 0; i < s.length(); i++)
        {
            if (simbolos.contains("" + s.charAt(i)))
                str += " " + s.charAt(i) + " ";
            else
                str += s.charAt(i);
        }
        return str.replaceAll("\\s+", " ");
    }
    //analizador sintactico
    public int Jerarquia(String op)
    {
        int prf = 99;
        if (op.equals("|")) //raiz
            prf = 4;
        if (op.equals("^")||op.equals("s")||op.equals("c")||op.equals("t")||op.equals("i")||op.equals("o")||op.equals("p"))
            prf = 4;
        if (op.equals("*") || op.equals("/"))
            prf = 4;
        if (op.equals("+") || op.equals("-"))
            prf = 3;
        if (op.equals(")"))
            prf = 2;
        if (op.equals("("))
            prf = 1;
        return prf;
    }
    void ConvertirDeinaPost()
    {
        while (!E.isEmpty())
        {
            switch (Jerarquia((String) E.peek()))
            {
                case 1:
                    P.push(E.pop());
                    break;
                case 2:
                    while(!P.peek().equals("("))
                    {
                        S.push(P.pop());
                    }
                    P.pop();
                    E.pop();
                    break;
                case 3:
                case 4:
                    while(Jerarquia((String) P.peek()) >= Jerarquia((String) E.peek()))
                    {
                        S.push(P.pop());
                    }
                    P.push(E.pop());
                    break;
                
                default:
                    S.push(E.pop());
                    System.out.println(S);
            }
        }
    }
    String ImprimirPostfija()
    {
        String postfix = S.toString().replaceAll("[\\]\\[,]", "");
        return postfix;
    }
}