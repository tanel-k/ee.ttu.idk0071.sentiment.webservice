/*
 * In-memory database initialization.
 */
INSERT INTO sentiment_type(code, name) VALUES ('NEG', 'Negative');
INSERT INTO sentiment_type(code, name) VALUES ('NEU', 'Neutral');
INSERT INTO sentiment_type(code, name) VALUES ('POS', 'Positive');
 
INSERT INTO business_type(id, name) VALUES (1, 'E-commerce');
INSERT INTO business_type(id, name) VALUES (2, 'Retailer');
INSERT INTO business_type(id, name) VALUES (3, 'Private banking');

INSERT INTO country(code, name) VALUES ('EST', 'Estonia');
INSERT INTO country(code, name) VALUES ('USA', 'United States of America');
INSERT INTO country(code, name) VALUES ('AUS', 'Australia');

INSERT INTO sentiment_lookup_domain(id, name) VALUES (1, 'Google');