package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class GanadorDAO {
    private MongoCollection<Document> collection;

    public GanadorDAO(MongoDatabase database) {
        this.collection = database.getCollection("ganadores");
    }

    public void registrarGanador(String cedulaCliente, double cantidadGanada) {
        Document ganador = new Document("cedula_cliente", cedulaCliente)
                .append("ganador", true)
                .append("cantidad_ganada", cantidadGanada);
        collection.insertOne(ganador);
    }

    public List<Document> obtenerGanadores() {
        return collection.find().into(new java.util.ArrayList<>());
    }
}