package MVC.modelo.partida;

import java.time.LocalDateTime;

public final class RegistroPartida {
    private final String nombre;
    private final int puntaje;
    private final int segundos;
    private final boolean victoria;
    private final LocalDateTime fecha;

    public RegistroPartida(String nombre, int puntaje, int segundos, boolean victoria, LocalDateTime fecha) {
        this.nombre = validarNombre(nombre);
        if (puntaje < 0 || segundos < 0 || fecha == null) {
            throw new IllegalArgumentException("Resultado de partida inválido");
        }
        this.puntaje = puntaje;
        this.segundos = segundos;
        this.victoria = victoria;
        this.fecha = fecha;
    }

    static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 12
                || nombre.contains(";") || nombre.contains("\n") || nombre.contains("\r")) {
            throw new IllegalArgumentException("El nombre debe tener de 1 a 12 caracteres, sin punto y coma ni saltos de línea");
        }
        return nombre.trim();
    }

    public String nombre() {
        return nombre;
    }

    public int puntaje() {
        return puntaje;
    }

    public int segundos() {
        return segundos;
    }

    public boolean victoria() {
        return victoria;
    }

    public LocalDateTime fecha() {
        return fecha;
    }

    public String aCSV() {
        return nombre + ";" + puntaje + ";" + segundos + ";" + victoria + ";" + fecha;
    }

    public static RegistroPartida desdeCSV(String linea) {
        String[] datos = linea.split(";", -1);
        if (datos.length != 5 || !(datos[3].equals("true") || datos[3].equals("false"))) {
            throw new IllegalArgumentException("Fila de historial inválida");
        }
        return new RegistroPartida(datos[0], Integer.parseInt(datos[1]), Integer.parseInt(datos[2]),
                Boolean.parseBoolean(datos[3]), LocalDateTime.parse(datos[4]));
    }
}
