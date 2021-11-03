package com.mycompany.calculadora;

import java.awt.*;

public class PruebaParseador extends java.applet.Applet {

    Parseador miparser = new Parseador(); //
    String expresion = new String();
    double valor = 0;
    TextField inputexpresion = new TextField("Expresión Matemática");
    Button boton = new Button("Evaluar la expresión");
    TextField outputparseo = new TextField("          ");
    TextField outputevaluar = new TextField("         ");
    Label info = new Label("Informacion de errores           ", Label.CENTER);

    public void init() {
        add(inputexpresion);
        add(boton);
        add(outputparseo);
        add(outputevaluar);
        add(info);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            try {
                info.setText("");
                expresion = inputexpresion.getText();
                outputparseo.setText(miparser.parsear(expresion));
                outputevaluar.setText("" + redondeo(miparser.f(valor), 5));
            } catch (Exception e) {
                info.setText(e.toString());
            }
        }
        return true;
    }

    private double redondeo(double numero, int decimales) {
        return ((double) Math.round(numero * Math.pow(10, decimales))) / Math.pow(10, decimales);
    }
}
