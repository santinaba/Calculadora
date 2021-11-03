package com.mycompany.calculadora;

import java.util.*;

public class Parseador {

    private String ultimaParseada;

    public Parseador() {
        ultimaParseada = "0";
    }

    public String parsear(String expresion) throws SintaxException {
        Stack PilaNumeros = new Stack();
        Stack PilaOperadores = new Stack();
        String expr = quitaEspacios(expresion.toLowerCase());
        String fragmento;
        int pos = 0, tamano = 0;
        byte cont = 1;

        //ANALIZADOR SINTACTICO    
        final String funciones[] = {"1 2 3 4 5 6 7 8 9 0 ( ) + - * / ^ %",
            "pi",
            "ln(",
            "log( abs( sen( sin( cos( tan( sec( csc( cot( sgn("};
        final String parentesis = "( ln log abs sen sin cos tan sec csc cot sgn";

        byte anterior = 0;

        try {
            //ANALIZADOR SEMANTICO
            while (pos < expr.length()) {
                tamano = 0;
                cont = 1;
                while (tamano == 0 && cont <= 6) {
                    if (pos + cont <= expr.length() && funciones[cont - 1].indexOf(expr.substring(pos, pos + cont)) != -1) {
                        tamano = cont;
                    }
                    cont++;
                }

                if (tamano == 0) {
                    ultimaParseada = "0";
                    throw new SintaxException("Error en la expresi�n");
                } else if (tamano == 1) {
                    if (isNum(expr.substring(pos, pos + tamano))) {
                        if (anterior == 1 || anterior == 4) {
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        fragmento = "";
                        do {
                            fragmento = fragmento + expr.charAt(pos);
                            pos++;
                        } while (pos < expr.length() && (isNum(expr.substring(pos, pos + tamano)) || expr.charAt(pos) == '.' || expr.charAt(pos) == ','));
                        try {
                            Double.valueOf(fragmento);
                        } catch (NumberFormatException e) {
                            ultimaParseada = "0";
                            throw new SintaxException("N�mero mal digitado");
                        }
                        PilaNumeros.push(new String(fragmento));
                        anterior = 1;
                        pos--;
                    } else if (expr.charAt(pos) == '+' || expr.charAt(pos) == '*' || expr.charAt(pos) == '/' || expr.charAt(pos) == '%') {
                        if (anterior == 0 || anterior == 2 || anterior == 3) {
                            throw new SintaxException("Error en la expresi�n");
                        }

                        sacaOperadores(PilaNumeros, PilaOperadores, expr.substring(pos, pos + tamano));
                        anterior = 2;
                    } else if (expr.charAt(pos) == '^') {
                        if (anterior == 0 || anterior == 2 || anterior == 3) {
                            throw new SintaxException("Error en la expresi�n");
                        }

                        PilaOperadores.push(new String("^"));
                        anterior = 2;
                    } else if (expr.charAt(pos) == '-') {
                        if (anterior == 0 || anterior == 2 || anterior == 3) {
                            PilaNumeros.push(new String("-1"));
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        } else {//si el menos es binario
                            sacaOperadores(PilaNumeros, PilaOperadores, "-");
                        }
                        anterior = 2;
                    } else if (expr.charAt(pos) == '(') {
                        if (anterior == 1 || anterior == 4) {
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaOperadores.push(new String("("));
                        anterior = 3;
                    } else if (expr.charAt(pos) == ')') {
                        if (anterior != 1 && anterior != 4) {
                            throw new SintaxException("Error en la expresi�n");
                        }

                        while (!PilaOperadores.empty() && parentesis.indexOf(((String) PilaOperadores.peek())) == -1) {
                            sacaOperador(PilaNumeros, PilaOperadores);
                        }
                        if (!((String) PilaOperadores.peek()).equals("(")) {
                            PilaNumeros.push(new String(((String) PilaNumeros.pop()) + " " + ((String) PilaOperadores.pop())));
                        } else {
                            PilaOperadores.pop();
                        }
                        anterior = 4;
                    }
                } else if (tamano >= 2) {
                    fragmento = expr.substring(pos, pos + tamano);
                    if (fragmento.equals("pi")) {
                        if (anterior == 1 || anterior == 4) {
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaNumeros.push(fragmento);
                        anterior = 1;
                    } else {
                        if (anterior == 1 || anterior == 4) {
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaOperadores.push(fragmento.substring(0, fragmento.length() - 1));
                        anterior = 3;
                    }
                }
                pos += tamano;
            }
            while (!PilaOperadores.empty()) {
                if (parentesis.indexOf((String) PilaOperadores.peek()) != -1) {
                    throw new SintaxException("Hay un par�ntesis de m�s");
                }
                sacaOperador(PilaNumeros, PilaOperadores);
            }

        } catch (EmptyStackException e) {
            ultimaParseada = "0";
            throw new SintaxException("Expresi�n mal digitada");
        }

        ultimaParseada = ((String) PilaNumeros.pop());

        if (!PilaNumeros.empty()) {
            ultimaParseada = "0";
            throw new SintaxException("Error en la expresi�n");
        }

        return ultimaParseada; 
    }

    public double f(String expresionParseada, double x) throws ArithmeticException {
        Stack pilaEvaluar = new Stack();
        double a, b;
        StringTokenizer tokens = new StringTokenizer(expresionParseada);
        String tokenActual;

        try {
            while (tokens.hasMoreTokens()) {
                tokenActual = tokens.nextToken();
                if (tokenActual.equals("pi")) {
                    pilaEvaluar.push(new Double(Math.PI));
                } else if (tokenActual.equals("+")) {
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a + b));
                } else if (tokenActual.equals("-")) {
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a - b));
                } else if (tokenActual.equals("*")) {//Multiplicaci�n
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a * b));
                } else if (tokenActual.equals("/")) {//Divisi�n

                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    if (b == 0) {
                        System.out.println("No se puede dividir entre 0");
                    }
                    if (b != 0) {
                        pilaEvaluar.push(new Double(a / b));
                    }
                } else if (tokenActual.equals("^")) {
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.pow(a, b)));
                } else if (tokenActual.equals("%")) {
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a % b));
                } else if (tokenActual.equals("ln")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.log(a)));
                } else if (tokenActual.equals("log")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.log(a) / Math.log(10)));
                } else if (tokenActual.equals("abs")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.abs(a)));
                } else if (tokenActual.equals("rnd")) {
                    pilaEvaluar.push(new Double(Math.random()));
                } else if (tokenActual.equals("sen") || tokenActual.equals("sin")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.sin(a)));
                } else if (tokenActual.equals("cos")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.cos(a)));
                } else if (tokenActual.equals("tan")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.tan(a)));
                } else if (tokenActual.equals("sec")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(1 / Math.cos(a)));
                } else if (tokenActual.equals("csc")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(1 / Math.sin(a)));
                } else if (tokenActual.equals("cot")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(1 / Math.tan(a)));
                } else if (tokenActual.equals("sgn")) {
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(sgn(a)));
                } else {
                    pilaEvaluar.push(Double.valueOf(tokenActual));
                }
            }//while
        } catch (EmptyStackException e) {
            throw new ArithmeticException("Expresi�n mal parseada");
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Expresi�n mal digitada");
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Valor no real en la expresi�n");
        }

        a = ((Double) pilaEvaluar.pop()).doubleValue();

        if (!pilaEvaluar.empty()) {
            throw new ArithmeticException("Expresi�n mal digitada");
        }

        return a;
    }

    public double f(double x) throws ArithmeticException {
        try {
            return f(ultimaParseada, x);
        } catch (ArithmeticException e) {
            throw e;
        }
    }

    private void sacaOperador(Stack Numeros, Stack operadores) throws EmptyStackException {
        String operador, a, b;
        final String operadoresBinarios = "+ - * / ^ %";

        try {
            operador = (String) operadores.pop(); //

            if (operadoresBinarios.indexOf(operador) != -1) {
                b = (String) Numeros.pop();
                a = (String) Numeros.pop();
                Numeros.push(new String(a + " " + b + " " + operador));
            } else { //Sino s�lo saca un elemento
                a = (String) Numeros.pop();
                Numeros.push(new String(a + " " + operador));
            }
        } catch (EmptyStackException e) {
            throw e;
        }
    }

    private void sacaOperadores(Stack PilaNumeros, Stack PilaOperadores, String operador) {
        final String parentesis = "( ln log abs sen sin cos tan sec csc cot sgn";
        while (!PilaOperadores.empty() && parentesis.indexOf((String) PilaOperadores.peek()) == -1 && ((String) PilaOperadores.peek()).length() == 1 && prioridad(((String) PilaOperadores.peek()).charAt(0)) >= prioridad(operador.charAt(0))) {
            sacaOperador(PilaNumeros, PilaOperadores);
        }
        PilaOperadores.push(operador);
    }

    private int prioridad(char s) {
        if (s == '+' || s == '-') {
            return 0;
        } else if (s == '*' || s == '/' || s == '%') {
            return 1;
        } else if (s == '^') {
            return 2;
        }

        return -1;
    }

    private boolean isNum(String s) {
        if (s.compareTo("0") >= 0 && s.compareTo("9") <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private String quitaEspacios(String polinomio) {
        String unspacedString = "";

        for (int i = 0; i < polinomio.length(); i++) {
            if (polinomio.charAt(i) != ' ') {
                unspacedString += polinomio.charAt(i);
            }
        }

        return unspacedString;
    }

    private double sgn(double a) {
        if (a < 0) {
            return -1;
        } else if (a > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private class SintaxException extends ArithmeticException {

        public SintaxException() {
            super("Error de sintaxis en el polinomio");
        }

        public SintaxException(String e) {
            super(e);
        }
    }
}
