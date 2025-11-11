package org.example;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "de.flapdoodle.mongodb.embedded.version=7.0.17",
})
public class MongoDbSpringIntegrationTest {

    private static String COLLECTIONNAME = "mixed_types";

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoCollection<Document> collection;

    @BeforeEach
    public void setUp() {
        // Clear any previous data
        mongoTemplate.dropCollection(COLLECTIONNAME);
        collection = mongoTemplate.getDb().getCollection(COLLECTIONNAME);
        assertNotNull(collection, "MongoCollection should not be null");

        List<Document> docs = new ArrayList<>();

        // ObjectId
        docs.add(new Document("_id", new ObjectId("507f1f77bcf86cd799439011"))
                .append("value", new ObjectId("64b8b3e9fa9c2b5d6f4e1234")));

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
        docs.add(new Document("_id", new Date(123456))
                .append("value", new Date(654321)));

        // UUID
        docs.add(new Document("_id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .append("value", UUID.fromString("987e6543-e21b-12d3-a456-426614174999")));

        // Insert all
        mongoTemplate.insert(docs, COLLECTIONNAME);
    }

    // #################################################################################################################
    // Test cases using com.mongodb.client.MongoCollection
    // #################################################################################################################

    // -------------------------------
    // ObjectId
    // -------------------------------
    @Test
    void testFindByObjectId() {
        Document doc = collection.find(new Document().append("_id", new ObjectId("507f1f77bcf86cd799439011"))).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByObjectIdValue() {
        Document doc = collection.find(new Document().append("value", new ObjectId("64b8b3e9fa9c2b5d6f4e1234"))).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // String
    // -------------------------------
    @Test
    void testFindByStringId() {
        Document doc = collection.find(new Document().append("_id", "string_id")).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByStringValue() {
        Document doc = collection.find(new Document().append("value", "string_value")).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // Integer
    // -------------------------------
    @Test
    void testFindByIntegerId() {
        Document doc = collection.find(new Document().append("_id", 123)).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByIntegerValue() {
        Document doc = collection.find(new Document().append("value", 456)).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // Long
    // -------------------------------
    @Test
    void testFindByLongId() {
        Document doc = collection.find(new Document().append("_id", 1234567890123L)).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByLongValue() {
        Document doc = collection.find(new Document().append("value", 9876543210987L)).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // Double
    // -------------------------------
    @Test
    void testFindByDoubleId() {
        Document doc = collection.find(new Document().append("_id", 3.14159)).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByDoubleValue() {
        Document doc = collection.find(new Document().append("value", 2.71828)).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // Boolean
    // -------------------------------
    @Test
    void testFindByBooleanId() {
        Document doc = collection.find(new Document().append("_id", true)).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByBooleanValue() {
        Document doc = collection.find(new Document().append("value", false)).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // Date
    // -------------------------------
    @Test
    void testFindByDateId() {
        Document doc = collection.find(new Document().append("_id", new Date(123456))).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByDateValue() {
        Document doc = collection.find(new Document().append("value", new Date(654321))).first();
        assertNotNull(doc);
    }

    // -------------------------------
    // UUID
    // -------------------------------
    @Test
    void testFindByUUIDId() {
        Document doc = collection.find(new Document().append("_id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))).first();
        assertNotNull(doc);
    }

    @Test
    void testFindByUUIDValue() {
        Document doc = collection.find(new Document().append("value", UUID.fromString("987e6543-e21b-12d3-a456-426614174999"))).first();
        assertNotNull(doc);
    }

    // #################################################################################################################
    // Test cases using org.springframework.data.mongodb.core.MongoTemplate
    // #################################################################################################################

    // -------------------------------
    // ObjectId
    // -------------------------------
    @Test
    void testFindByObjectIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(new ObjectId("507f1f77bcf86cd799439011"));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByObjectIdValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(new ObjectId("64b8b3e9fa9c2b5d6f4e1234"));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // String
    // -------------------------------
    @Test
    void testFindByStringIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is("string_id");
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByStringValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is("string_value");
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // Integer
    // -------------------------------
    @Test
    void testFindByIntegerIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(123);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByIntegerValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(456);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // Long
    // -------------------------------
    @Test
    void testFindByLongIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(1234567890123L);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByLongValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(9876543210987L);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // Double
    // -------------------------------
    @Test
    void testFindByDoubleIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(3.14159);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByDoubleValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(2.71828);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // Boolean
    // -------------------------------
    @Test
    void testFindByBooleanIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(true);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByBooleanValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(false);
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // Date
    // -------------------------------
    @Test
    void testFindByDateIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(new Date(123456));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByDateValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(new Date(654321));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    // -------------------------------
    // UUID
    // -------------------------------
    @Test
    void testFindByUUIDIdWithMongoTemplate() {
        var criteria = Criteria.where("_id").is(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }

    @Test
    void testFindByUUIDValueWithMongoTemplate() {
        var criteria = Criteria.where("value").is(UUID.fromString("987e6543-e21b-12d3-a456-426614174999"));
        var query = new Query(criteria);

        List<Document> doc = mongoTemplate.find(query, Document.class, COLLECTIONNAME);
        assertEquals(1, doc.size());
        assertNotNull(doc.get(0));
    }
}
