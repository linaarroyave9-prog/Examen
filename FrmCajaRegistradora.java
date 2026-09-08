import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmCajaRegistradora extends JFrame {

    private int[] denominaciones = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};

    private String[] presentaciones = {
        "billete", "billete", "billete", "billete", "billete",
        "billete", "billete", "moneda", "moneda", "moneda", "moneda"
    };

    private int[] existencias = new int[denominaciones.length];

    private JComboBox cmbDenominacion;
    private JTextField txtExistencia;
    private JTextField txtValorDevolver;
    private JTable tblDevuelta;

    private String[] encabezados = {"Cantidad", "Presentacion", "Denominacion"};

    public FrmCajaRegistradora() {

        setSize(520, 520);
        setTitle("Caja Registradora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblDenominacion = new JLabel("Denominación");
        lblDenominacion.setBounds(10, 10, 120, 25);
        add(lblDenominacion);

        cmbDenominacion = new JComboBox();
        cmbDenominacion.setBounds(140, 10, 150, 25);
        add(cmbDenominacion);

        String[] itemsDenominacion = new String[denominaciones.length];

        for (int i = 0; i < denominaciones.length; i++) {
            itemsDenominacion[i] = String.valueOf(denominaciones[i]);
        }

        DefaultComboBoxModel dcm = new DefaultComboBoxModel(itemsDenominacion);
        cmbDenominacion.setModel(dcm);

        JButton btnActualizarExistencia = new JButton("Actualizar Existencia");
        btnActualizarExistencia.setBounds(10, 45, 170, 25);
        add(btnActualizarExistencia);

        txtExistencia = new JTextField();
        txtExistencia.setBounds(190, 45, 100, 25);
        add(txtExistencia);

        JLabel lblValorDevolver = new JLabel("Valor a Devolver");
        lblValorDevolver.setBounds(10, 90, 120, 25);
        add(lblValorDevolver);

        txtValorDevolver = new JTextField();
        txtValorDevolver.setBounds(140, 90, 150, 25);
        add(txtValorDevolver);

        JButton btnDevolver = new JButton("Devolver");
        btnDevolver.setBounds(300, 90, 100, 25);
        add(btnDevolver);

        tblDevuelta = new JTable();

        JScrollPane spDevuelta = new JScrollPane(tblDevuelta);
        spDevuelta.setBounds(10, 130, 480, 300);
        add(spDevuelta);

        DefaultTableModel dtm = new DefaultTableModel(
                new String[0][encabezados.length],
                encabezados
        );

        tblDevuelta.setModel(dtm);

        btnActualizarExistencia.addActionListener(evento -> {
            actualizarExistencia();
        });

        btnDevolver.addActionListener(evento -> {
            devolver();
        });
    }

    private boolean esNumeroValido(String texto) {

        if (texto == null || texto.length() == 0) {
            return false;
        }

        for (int i = 0; i < texto.length(); i++) {

            char caracter = texto.charAt(i);

            if (!Character.isDigit(caracter)) {
                return false;
            }
        }

        return true;
    }

    private void actualizarExistencia() {

        String texto = txtExistencia.getText();

        if (!esNumeroValido(texto)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe ingresar un valor numérico válido para la existencia"
            );
            return;
        }

        int posicion = cmbDenominacion.getSelectedIndex();
        int cantidad = Integer.parseInt(texto);

        existencias[posicion] = cantidad;

        JOptionPane.showMessageDialog(
                null,
                "Existencia actualizada para la denominación $"
                        + denominaciones[posicion]
                        + ": "
                        + cantidad
        );

        txtExistencia.setText("");
    }

    private void devolver() {

        String texto = txtValorDevolver.getText();

        if (!esNumeroValido(texto)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe ingresar un valor numérico válido para devolver"
            );
            return;
        }

        int valor = Integer.parseInt(texto);

        int[] cantidadUsada = new int[denominaciones.length];

        for (int i = 0; i < denominaciones.length; i++) {

            int cantidadNecesaria = valor / denominaciones[i];

            int cantidadDisponible = Math.min(
                    cantidadNecesaria,
                    existencias[i]
            );

            cantidadUsada[i] = cantidadDisponible;

            valor = valor - (cantidadDisponible * denominaciones[i]);

            existencias[i] = existencias[i] - cantidadDisponible;
        }

        if (valor > 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay suficiente existencia para completar la devuelta exacta. Falta $"
                            + valor
            );
        }

        int totalUsadas = 0;

        for (int i = 0; i < cantidadUsada.length; i++) {

            if (cantidadUsada[i] > 0) {
                totalUsadas++;
            }
        }

        String[][] datosDevuelta =
                new String[totalUsadas][encabezados.length];

        int fila = 0;

        for (int i = 0; i < cantidadUsada.length; i++) {

            if (cantidadUsada[i] > 0) {

                datosDevuelta[fila][0] =
                        String.valueOf(cantidadUsada[i]);

                datosDevuelta[fila][1] =
                        presentaciones[i];

                datosDevuelta[fila][2] =
                        String.valueOf(denominaciones[i]);

                fila++;
            }
        }

        DefaultTableModel dtm =
                new DefaultTableModel(datosDevuelta, encabezados);

        tblDevuelta.setModel(dtm);

        txtValorDevolver.setText("");
    }

    public static void main(String[] args) {

        FrmCajaRegistradora ventana = new FrmCajaRegistradora();

        ventana.setLocationRelativeTo(null);

        ventana.setVisible(true);
    }
}
