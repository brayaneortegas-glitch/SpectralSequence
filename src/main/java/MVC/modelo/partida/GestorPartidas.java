package MVC.modelo.partida;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GestorPartidas {
    private final Path ruta;
    private List<RegistroPartida> historial = new ArrayList<>();

    public GestorPartidas() {
        this(Path.of("historial.csv"));
    }

    public GestorPartidas(Path ruta) {
        this.ruta = ruta.toAbsolutePath();
    }

    public void cargar() throws IOException {
        List<RegistroPartida> leidos = new ArrayList<>();
        if (!Files.notExists(ruta)) {
            for (String linea : Files.readAllLines(ruta)) {
                try {
                    leidos.add(RegistroPartida.desdeCSV(linea));
                } catch (IllegalArgumentException | DateTimeParseException error) {
                    throw new IOException("Resultado inválido en la línea " + (leidos.size() + 1), error);
                }
            }
        }
        historial = leidos;
    }

    public void guardar(RegistroPartida registro) throws IOException {
        cargar();
        List<String> filas = new ArrayList<>();
        String nuevaFila = registro.aCSV();
        for (RegistroPartida anterior : historial) {
            String fila = anterior.aCSV();
            if (fila.equals(nuevaFila)) {
                return;
            }
            filas.add(fila);
        }
        filas.add(nuevaFila);
        Files.createDirectories(ruta.getParent());
        Path temporal = Files.createTempFile(ruta.getParent(), "partidas-", ".tmp");
        try {
            Files.write(temporal, filas);
            Files.move(temporal, ruta, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
        historial.add(registro);
    }

    public List<RegistroPartida> getHistorial() {
        return List.copyOf(historial);
    }

    public List<RegistroPartida> getTop3() {
        List<RegistroPartida> mejores = new ArrayList<>();
        for (RegistroPartida candidato : historial) {
            if (candidato.victoria()) {
                int posicion = 0;
                while (posicion < mejores.size()) {
                    RegistroPartida actual = mejores.get(posicion);
                    if (candidato.puntaje() > actual.puntaje()
                            || (candidato.puntaje() == actual.puntaje() && candidato.segundos() < actual.segundos())) {
                        break;
                    }
                    posicion++;
                }
                mejores.add(posicion, candidato);
                if (mejores.size() > 3) {
                    mejores.remove(3);
                }
            }
        }
        return List.copyOf(mejores);
    }
}
