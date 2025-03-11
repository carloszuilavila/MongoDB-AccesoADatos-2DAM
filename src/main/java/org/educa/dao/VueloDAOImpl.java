package org.educa.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.VueloWithRelations;
import org.educa.settings.DatabaseSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VueloDAOImpl implements VueloDAO {

    private static final String COLECCION_VUELOS = "vuelos";
    private final Gson gson = new GsonBuilder().create();

    @Override
    public List<VueloWithRelations> getBeneficioVuelo() {
        List<VueloWithRelations> vuelos = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLECCION_VUELOS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.lookup("aeropuertos", "origen_id", "_id", "origen"),
                    Aggregates.unwind("$origen"),
                    Aggregates.lookup("aeropuertos", "destino_id", "_id", "destino"),
                    Aggregates.unwind("$destino")
            );
            AggregateIterable<Document> documents = collection.aggregate(pipeline);
            for (Document document : documents) {
                VueloWithRelations vueloWithRelations = gson.fromJson(document.toJson(), VueloWithRelations.class);
                vuelos.add(vueloWithRelations);
            }
        }
        return vuelos;
    }
}
