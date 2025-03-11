package org.educa.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.InfoPasajero;
import org.educa.wrappers.VueloWithPrecio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {

    private static final String COLECCION_RESERVAS = "reservas";
    private final Gson gson = new GsonBuilder().create();

    @Override
    public List<ReservaEntity> findAll() {
        List<ReservaEntity> reservas = new ArrayList<>();
        ReservaEntity reserva;
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            FindIterable<Document> documents = collection.find();
            for (Document document : documents) {
                reserva = gson.fromJson(document.toJson(), ReservaEntity.class);
                reservas.add(reserva);
            }
        }
        return reservas;
    }

    @Override
    public ReservaEntity findById(int id) {
        ReservaEntity reserva = new ReservaEntity();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            Document document = collection.find(Filters.eq("_id", id)).first();
            if (document != null) {
                reserva = gson.fromJson(document.toJson(), ReservaEntity.class);
            }
        }
        return reserva;
    }

    @Override
    public Integer save(ReservaEntity reserva) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            Document document = Document.parse(gson.toJson(reserva));
            InsertOneResult ior = collection.insertOne(document);
            if (ior.getInsertedId() != null) {
                return ior.getInsertedId().asInt32().intValue();
            } else {
                return -1;
            }
        }
    }

    @Override
    public Long update(ReservaEntity reservaToUpdate) {
        long filasAfectadas;
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            Document document = Document.parse(gson.toJson(reservaToUpdate));
            UpdateResult updateResult = collection.replaceOne(Filters.eq("_id", reservaToUpdate.getId()), document);
            filasAfectadas = updateResult.getModifiedCount();
        }
        return filasAfectadas;
    }

    @Override
    public Long delete(int id) {
        long filasAfectadas;
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            DeleteResult deleteResult = collection.deleteOne(Filters.eq("_id", id));
            filasAfectadas = deleteResult.getDeletedCount();
        }
        return filasAfectadas;
    }

    @Override
    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        List<ReservaWithRelations> reservasWithRelations = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(Filters.gte("precio", cantidad)),
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.unwind("$vuelo"),
                    Aggregates.lookup("pasajeros", "pasajero_id", "_id", "pasajero"),
                    Aggregates.unwind("$pasajero")
            );
            AggregateIterable<Document> documents = collection.aggregate(pipeline);
            for (Document document : documents) {
                reservasWithRelations.add(gson.fromJson(document.toJson(), ReservaWithRelations.class));
            }
        }
        return reservasWithRelations;
    }

    @Override
    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        InfoPasajero infoPasajero = new InfoPasajero();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            List<ReservaWithRelations> reservas = new ArrayList<>();
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.lookup("pasajeros", "pasajero_id", "_id", "pasajero"),
                    Aggregates.unwind("$pasajero"),
                    Aggregates.match(Filters.eq("pasajero.pasaporte", pasaporte)),
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.unwind("$vuelo")
            );
            AggregateIterable<Document> documentos = collection.aggregate(pipeline);
            for (Document document : documentos) {
                reservas.add(gson.fromJson(document.toJson(), ReservaWithRelations.class));
            }
            List<VueloWithPrecio> vuelos = new ArrayList<>();
            if (reservas.isEmpty()) {
                infoPasajero.setVuelos(vuelos);
                return infoPasajero;
            }
            infoPasajero.setPasajero(reservas.getFirst().getPasajero());
            for (ReservaWithRelations reservaWithRelations : reservas) {
                VueloWithPrecio vueloWithPrecio = new VueloWithPrecio();
                vueloWithPrecio.setVuelo(reservaWithRelations.getVuelo());
                vueloWithPrecio.setPrecio(reservaWithRelations.getPrecio());
                vueloWithPrecio.setEstado(reservaWithRelations.getEstado());
                vuelos.add(vueloWithPrecio);
            }
            infoPasajero.setVuelos(vuelos);
        }
        return infoPasajero;
    }

    @Override
    public List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        List<ReservaEntity> reservas = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_RESERVAS);
            FindIterable<Document> documents = collection.find(Filters.eq("vuelo_id", vueloId));
            for (Document document : documents) {
                ReservaEntity reserva = gson.fromJson(document.toJson(), ReservaEntity.class);
                reservas.add(reserva);
            }
        }
        return reservas;
    }
}
