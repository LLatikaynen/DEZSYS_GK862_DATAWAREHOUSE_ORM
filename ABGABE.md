# 🚀 ABGABE-ANLEITUNG

## **Was du deinem Lehrer zeigen sollst**

### **1. Schritt: Projekt starten**
```bash
cd /home/wiggly/IdeaProjects/DEZSYS_GK862_DATAWAREHOUSE_ORM
pkill -9 -f java 2>/dev/null || true
sleep 2
./gradlew clean build
java -jar build/libs/DEZSYS_GK862_WAREHOUSE_ORM-0.0.1-SNAPSHOT.jar
```

**Der Lehrer sieht in der Konsole:**
```
LADE TESTDATEN IN MYSQL
2 WAREHOUSES, 10 PRODUKTE UND 30 PURCHASES GELADEN
=== STARTE 300 PURCHASE GENERIERUNG ===
... 100 Purchases generiert
... 200 Purchases generiert
... 300 Purchases generiert
✓ FERTIG: 300 Datensätze generiert!
Application started on port 8080
```

### **2. Schritt: Datenbank zeigen**
```bash
mariadb -u springuser -p'springpass' -h localhost << EOF
USE example;
SELECT 'Warehouses:' as '';
SELECT COUNT(*) FROM data_warehouse;
SELECT 'Produkte:' as '';
SELECT COUNT(*) FROM product;
SELECT 'Purchases (330 Total):' as '';
SELECT COUNT(*) FROM purchase;
EOF
```

### **3. Schritt: API testen**

**Top 5 Produkte:**
```bash
curl -s http://localhost:8080/api/statistics/top-products | jq .
```

**Statistiken:**
```bash
curl -s http://localhost:8080/api/statistics/sales | jq .
```

**KI-Prognose (echte Ollama Integration!):**
```bash
curl http://localhost:8080/api/forecast
```

### **4. Schritt: Code erklären**
- Öffne `CODEERKLAERUNG.md` → erkläre den wichtigsten Code
- Zeige `ForecastController.java` → erkläre die Ollama Integration
- Zeige `DatabaseSeeder.java` → erkläre die automatische Datenladeung

---

## 📁 **Dateien deiner Abgabe**

| Datei | Beschreibung |
|-------|-------------|
| `src/main/java/org/example/DataWarehouse.java` | Entity mit 1:N Beziehung |
| `src/main/java/org/example/Product.java` | Entity mit M:1 Beziehung |
| `src/main/java/org/example/Purchase.java` | Entity mit 2x M:1 Beziehung |
| `src/main/java/org/example/DataWarehouseRepository.java` | Custom Queries |
| `src/main/java/org/example/DatabaseSeeder.java` | Automatisches Daten-Laden |
| `src/main/java/org/example/ForecastController.java` | REST-API + KI-Integration |
| `CODEERKLAERUNG.md` | Erklärung der wichtigsten Code-Abschnitte |
| `TESTPROTOKOLL.md` | Protokoll aller Tests |
| `application.properties` | MySQL-Konfiguration |

---

## ✅ **Anforderungen erfüllt**

### GK - Grundlagen
- ✓ ORM mit @Entity Annotation
- ✓ Beziehungen: @OneToMany, @ManyToOne
- ✓ 2 Warehouses, 10 Produkte, 30 Purchases

### EK - Erweiterte Grundlagen
- ✓ CrudRepository mit Custom Queries
- ✓ findByWarehouseID(), updateWarehouseName()
- ✓ 30+ Purchase-Datensätze

### EK Vertiefung
- ✓ 330 Datensätze (30 Initial + 300 Auto-Generiert)
- ✓ Echte LLM-Integration mit Ollama/llama3.2
- ✓ KI-Prognosen für Verkäufe

---

## 🎯 **Timing für die Präsentation**

| Zeit | Was | Dauer |
|------|-----|-------|
| 0:00 | App starten | 10s |
| 0:10 | Datenbank zeigen | 15s |
| 0:25 | API-Endpoints testen | 30s |
| 0:55 | KI-Prognose abrufen | 10s |
| 1:05 | Code erklären (CODEERKLAERUNG.md) | 3-5 min |
| 6:05 | Fragen beantworten | offenzeit |

**Total: ~6-8 Minuten**

---

## 🔥 **Was wird dein Lehrer beeindrucken?**

1. **Automatische Datenladeung** - CommandLineRunner beim App-Start
2. **Echte KI-Integration** - Nicht nur Dummy-Antworten!
3. **Clean Code** - Gut strukturierte Repositories & Services
4. **SQL + ORM** - Verständnis von Beziehungen
5. **REST-API** - Mehrere Endpoints mit Statistiken
6. **Fehlerbehandlung** - Try-Catch mit Fallback

---

## 📝 **Deine To-Do Liste**

- [ ] App starten: `java -jar build/libs/DEZSYS_GK862_WAREHOUSE_ORM-0.0.1-SNAPSHOT.jar`
- [ ] In MariaDB shell 330 Purchases überprüfen
- [ ] `/api/forecast` Endpoint testen
- [ ] CODEERKLAERUNG.md durchlesen
- [ ] TESTPROTOKOLL.md deinem Lehrer zeigen
- [ ] Demo üben (Timeline oben)
- [ ] Bei Fragen: README.md nochmal lesen

**DU SCHAFFST DAS! 🚀**


