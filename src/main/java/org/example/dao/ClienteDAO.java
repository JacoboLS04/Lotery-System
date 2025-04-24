package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.example.modelo.Cliente;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class ClienteDAO {
    private MongoCollection<Document> collection;

    public ClienteDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        collection = database.getCollection("clientes");
    }

    public void agregarCliente(Cliente cliente) {
        Document doc = new Document("cedula", cliente.getCedula())
                .append("nombre", cliente.getNombre())
                .append("telefono", cliente.getTelefono())
                .append("email", cliente.getEmail())
                .append("billete", cliente.getBillete())
                .append("verificado", cliente.isVerificado())
                .append("encargado", cliente.getEncargado()); // Guardar encargado único
        collection.insertOne(doc);
    }

    public void actualizarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            System.out.println("El cliente no tiene un ID asignado.");
            return;
        }

        Document filtro = new Document("_id", cliente.getId()); // Ensure ObjectId is used
        Document actualizacion = new Document("$set", new Document()
                .append("nombre", cliente.getNombre())
                .append("telefono", cliente.getTelefono())
                .append("email", cliente.getEmail())
                .append("billete", cliente.getBillete())
                .append("verificado", cliente.isVerificado())
                .append("encargado", cliente.getEncargado())); // Update encargado

        UpdateResult resultado = collection.updateOne(filtro, actualizacion);
        System.out.println("Documentos modificados: " + resultado.getModifiedCount());
        if (resultado.getModifiedCount() == 0) {
            System.out.println("No se encontró el documento o no hubo cambios.");
        }
    }

    public List<Cliente> obtenerClientesVerificados() {
        List<Cliente> clientes = new ArrayList<>();
        for (Document doc : collection.find(eq("verificado", true))) {
            clientes.add(new Cliente(
                    doc.getObjectId("_id"),
                    doc.getString("cedula"),
                    doc.getString("nombre"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    doc.getString("billete"),
                    doc.getBoolean("verificado", false),
                    doc.getString("encargado") // Retrieve encargado
            ));
        }
        return clientes;
    }

    public void eliminarCliente(String cedula) {
        collection.deleteOne(eq("cedula", cedula));
    }

    public Cliente buscarPorCedula(String cedula) {
        Document doc = collection.find(eq("cedula", cedula)).first();
        if (doc != null) {
            return new Cliente(
                    doc.getObjectId("_id"), // Retrieve _id
                    doc.getString("cedula"),
                    doc.getString("nombre"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    doc.getString("billete"),
                    doc.getBoolean("verificado", false),
                    doc.getString("encargado") // Retrieve encargado
            );
        }
        return null;
    }

    public String obtenerCorreoPorCedula(String cedulaCliente) {
        Document cliente = collection.find(new Document("cedula", cedulaCliente)).first();
        return cliente != null ? cliente.getString("email") : null;
    }

    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        for (Document doc : collection.find()) {
            System.out.println("Retrieved document: " + doc.toJson()); // Debug log
            clientes.add(new Cliente(
                    doc.getObjectId("_id"),
                    doc.getString("cedula"),
                    doc.getString("nombre"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    doc.getString("billete"),
                    doc.getBoolean("verificado", false),
                    doc.getString("encargado")
            ));
        }
        return clientes;
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        for (Document doc : collection.find(regex("nombre", nombre, "i"))) {
            clientes.add(new Cliente(
                    doc.getObjectId("_id"),
                    doc.getString("cedula"),
                    doc.getString("nombre"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    doc.getString("billete"),
                    doc.getBoolean("verificado"),
                    doc.getString("encargado") // Retrieve encargado
            ));
        }
        return clientes;
    }

    public boolean existeCliente(String cedula) {
        Document cliente = collection.find(eq("cedula", cedula)).first();
        return cliente != null;
    }
}