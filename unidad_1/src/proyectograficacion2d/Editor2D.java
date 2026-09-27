package proyectograficacion2d;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;


public class Editor2D extends JFrame {

    private Canvas2D canvas;

    public Editor2D() {
        // Configuración de la ventana principal
        setTitle("Proyecto Graficación 2D - NetBeans");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setLayout(new BorderLayout());

        // Instancia del lienzo de dibujo
        canvas = new Canvas2D();
        add(canvas, BorderLayout.CENTER);

        // Panel inferior con los botones
        JPanel panelControl = new JPanel();

        JButton btnTrasladar = new JButton("Trasladar");
        JButton btnEscalar = new JButton("Escalar");
        JButton btnRotar = new JButton("Rotar");
        JButton btnSesgar = new JButton("Sesgar");
        JButton btnReset = new JButton("Reiniciar");
        
        //Boton cerrar ventana
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(231, 76, 60)); // Color rojo visual
        btnCerrar.setForeground(Color.WHITE);

        panelControl.add(btnTrasladar);
        panelControl.add(btnEscalar);
        panelControl.add(btnRotar);
        panelControl.add(btnSesgar);
        panelControl.add(btnReset);
        panelControl.add(btnCerrar);

        add(panelControl, BorderLayout.SOUTH);

        // Eventos de los botones
        btnTrasladar.addActionListener((ActionEvent e) -> canvas.aplicarTraslacion(30, 20));
        btnEscalar.addActionListener((ActionEvent e) -> canvas.aplicarEscalamiento(1.1, 1.1));
        btnRotar.addActionListener((ActionEvent e) -> canvas.aplicarRotacion(15));
        btnSesgar.addActionListener((ActionEvent e) -> canvas.aplicarSesgado(0.2, 0.0));
        btnReset.addActionListener((ActionEvent e) -> canvas.reiniciar());
        
        // Cierre de ventana
        btnCerrar.addActionListener((ActionEvent e) -> dispose());
    }

    public static void main(String[] args) {
        // Ejecución en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new Editor2D().setVisible(true);
        });
    }
}

 // Clase personalizada del lienzo

class Canvas2D extends JPanel {

    // Vértices del rectángulo original en coordenadas relativas al centro (x, y, 1)
    private final double[][] verticesOriginales = {
        {-60, -40, 1},
        { 60, -40, 1},
        { 60,  40, 1},
        {-60,  40, 1}
    };

    // Matriz de transformación acumulada
    private double[][] matrizAcumulada;

    public Canvas2D() {
        setBackground(Color.WHITE);
        reiniciar();
    }

    public void reiniciar() {
        // Matriz Identidad 3x3 inicial
        matrizAcumulada = new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        repaint();
    }

    
    //Traslación y Escalamiento
    
    public void aplicarTraslacion(double dx, double dy) {
        double[][] T = {
            {1, 0, dx},
            {0, 1, dy},
            {0, 0,  1}
        };
        matrizAcumulada = multiplicarMatrices(T, matrizAcumulada);
        repaint();
    }

    public void aplicarEscalamiento(double sx, double sy) {
        double[][] S = {
            {sx,  0, 0},
            { 0, sy, 0},
            { 0,  0, 1}
        };
        matrizAcumulada = multiplicarMatrices(S, matrizAcumulada);
        repaint();
    }

    
    //Rotación y Sesgado
    
    public void aplicarRotacion(double grados) {
        double rad = Math.toRadians(grados);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double[][] R = {
            {cos, -sin, 0},
            {sin,  cos, 0},
            {  0,    0, 1}
        };
        matrizAcumulada = multiplicarMatrices(R, matrizAcumulada);
        repaint();
    }

    public void aplicarSesgado(double shx, double shy) {
        double[][] SH = {
            {  1, shx, 0},
            {shy,   1, 0},
            {  0,   0, 1}
        };
        matrizAcumulada = multiplicarMatrices(SH, matrizAcumulada);
        repaint();
    }

    
    //Representación Matricial (Multiplicación 3x3)
    
    private double[][] multiplicarMatrices(double[][] A, double[][] B) {
        double[][] C = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                C[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
