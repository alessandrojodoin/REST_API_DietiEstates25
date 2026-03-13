curl -X POST http://localhost:8080/api/1.0/auth/amministratore ^
 -H "Content-Type: application/json" ^
 -d "{\"username\":\"Amministratore\",\"agenziaImmobiliare\":\"MyAgenziaTest\", \"email\":\"default@email.com\"}"