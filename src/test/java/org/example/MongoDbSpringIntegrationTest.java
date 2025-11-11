package org.example;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "de.flapdoodle.mongodb.embedded.version=7.0.17",
})
public class MongoDbSpringIntegrationTest {

    private static String COLLECTIONNAME = "mixed_types";

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        // Clear any previous data
        mongoTemplate.dropCollection(COLLECTIONNAME);

        List<Document> docs = new ArrayList<>();

        // ObjectId
        docs.add(new Document("_id", new ObjectId())
                .append("value", new ObjectId()));

        // String
        docs.add(new Document("_id", "string_id")
                .append("value", "string_value"));

        // Integer
        docs.add(new Document("_id", 123)
                .append("value", 456));

        // Long
        docs.add(new Document("_id", 1234567890123L)
                .append("value", 9876543210987L));

        // Double
        docs.add(new Document("_id", 3.14159)
                .append("value", 2.71828));

        // Boolean
        docs.add(new Document("_id", true)
                .append("value", false));

        // Date
        docs.add(new Document("_id", new Date())
                .append("value", new Date(System.currentTimeMillis() + 60000)));

        // UUID
        docs.add(new Document("_id", UUID.randomUUID())
                .append("value", UUID.randomUUID()));

        // Insert all
        mongoTemplate.insert(docs, COLLECTIONNAME);

        System.out.println("✅ Inserted " + docs.size() + " documents into collection '" + COLLECTIONNAME + "'");
    }

    @Test
    public void test() {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        DBObject collection = mongoTemplate.save(objectToSave, "collection");

        // then
        System.out.println(collection);
    }
}
