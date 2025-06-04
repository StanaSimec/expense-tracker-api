INSERT INTO category (id, name) VALUES (1, 'Groceries') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (2, 'Leisure') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (3, 'Electronics') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (4, 'Utilities') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (5, 'Clothing') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (6, 'Health') ON CONFLICT DO NOTHING;
INSERT INTO category (id, name) VALUES (7, 'Others') ON CONFLICT DO NOTHING;

CREATE INDEX IF NOT EXISTS index_category_id ON category(id);

INSERT INTO person (id, username, email, password)
VALUES (1, 'Tester', 'test@test.com', 'test') ON CONFLICT DO NOTHING;