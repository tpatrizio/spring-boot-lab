INSERT INTO greeting(id, lang, msg) VALUES
(1, 'it', 'Ciao'),
(2, 'en', 'Hello')
ON CONFLICT DO NOTHING
;
