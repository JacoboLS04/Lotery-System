package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoletoDAO {
    private final MongoCollection<Document> coleccionBoletos;

    public BoletoDAO(MongoDatabase database) {
        this.coleccionBoletos = database.getCollection("boletos");
    }

    public String generarCodigoBoleto(String sorteo) {
        String abreviacion;
        switch (sorteo) {
            case "Premio Mayor":
                abreviacion = "PM";
                break;
            case "Sueldazo Cafetero":
                abreviacion = "SC";
                break;
            case "Seco de 25 Millones":
                abreviacion = "S25";
                break;
            case "Seco de 10 Millones":
                abreviacion = "S10";
                break;
            default:
                throw new IllegalArgumentException("Sorteo no válido");
        }

        String numeroUnico = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return abreviacion + "-" + numeroUnico;
    }

    public void guardarBoleto(String codigoBoleto, String cedulaCliente, String sorteo) {
        Document boleto = new Document("codigo_boleto", codigoBoleto)
                .append("cedula_cliente", cedulaCliente)
                .append("sorteo", sorteo);

        coleccionBoletos.insertOne(boleto);
    }

    public List<Document> obtenerBoletos() {
        return coleccionBoletos.find().into(new ArrayList<>());
    }

}