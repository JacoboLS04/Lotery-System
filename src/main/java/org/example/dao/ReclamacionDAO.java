package org.example.dao;

    import com.mongodb.client.MongoCollection;
    import com.mongodb.client.MongoDatabase;
    import org.bson.Document;
    import org.example.modelo.Reclamacion;

    import java.util.ArrayList;
    import java.util.List;

    import static com.mongodb.client.model.Filters.eq;

    public class ReclamacionDAO {
        private final MongoCollection<Document> collection;

        public ReclamacionDAO() {
            MongoDatabase database = DatabaseConnection.getDatabase();
            this.collection = database.getCollection("reclamaciones");
        }

        public void agregarReclamacion(Reclamacion reclamacion) {
            Document doc = new Document("id", reclamacion.getId())
                    .append("cedulaCliente", reclamacion.getCedulaCliente())
                    .append("mensaje", reclamacion.getMensaje())
                    .append("fecha", reclamacion.getFecha())
                    .append("estado", reclamacion.getEstado())
                    .append("respuesta", reclamacion.getRespuesta());
            collection.insertOne(doc);
        }

        public List<Reclamacion> obtenerReclamaciones() {
            List<Reclamacion> reclamaciones = new ArrayList<>();
            for (Document doc : collection.find()) {
                reclamaciones.add(new Reclamacion(
                        doc.getString("id"),
                        doc.getString("cedulaCliente"),
                        doc.getString("mensaje"),
                        doc.getString("fecha"),
                        doc.getString("estado"),
                        doc.getString("respuesta")
                ));
            }
            return reclamaciones;
        }

        public void actualizarEstadoYRespuesta(String id, String nuevoEstado, String respuesta) {
            collection.updateOne(eq("id", id), new Document("$set", new Document("estado", nuevoEstado).append("respuesta", respuesta)));
        }
    }