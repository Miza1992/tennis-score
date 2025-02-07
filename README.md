# 🏉 Tennis Score Application

## 📌 Description
Cette application permet de **calculer l'évolution du score d'un match de tennis** en fonction des balles gagnées par les joueurs A et B.  
Elle utilise **Spring Boot**, **Spring Cloud Stream**, et **Kafka** pour traiter et transmettre les scores en temps réel.

---

## 🚀 Fonctionnalités
✅ **Calcul automatique du score** via l’API REST `/compute-score`.  
✅ **Envoi des scores à Kafka** via `/send-message`.  
✅ **Consommation et transformation du score via Kafka** (`receiveScore()`).  
✅ **Transmission des résultats vers un autre topic Kafka** (`sendScore-out-0`).  
✅ **API REST et Kafka intégrés pour interagir avec le système**.

---

## 👖 Prérequis
Avant de lancer l'application, assurez-vous d'avoir installé :

- **Java 17** ✅
- **Maven** (`mvn -version`) ✅
- **Docker & Docker Compose** (`docker -v`) ✅
- **Kafka** (via Docker Compose) ✅

---

## ⚙️ Installation et Configuration

### 👅 1️⃣ **Cloner le Projet**
```bash
git clone https://github.com/votre-repository/tennis-score.git
cd tennis-score
```

### 🛠️ 2️⃣ **Configurer Kafka avec Docker**
Démarrer Kafka en utilisant **Docker Compose** :
```bash
docker-compose up -d
```

📌 Vérifiez que **Kafka fonctionne** :
```bash
docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092
```

Si les topics **n’existent pas**, créez-les :
```bash
docker exec -it kafka kafka-topics.sh --create --topic receiveScore-in-0 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
docker exec -it kafka kafka-topics.sh --create --topic sendScore-out-0 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

---

## ▶️ Exécution de l’Application

### 🚀 3️⃣ **Démarrer l’Application**
Lancer l’application Spring Boot avec **Maven** :
```bash
mvn clean spring-boot:run
```

📌 **L'application démarre sur** `http://localhost:8080`

---

## 🛠️ API Endpoints

### 🏉 **1️⃣ Calculer un Score avec l’API REST**
```bash
GET /compute-score?input=ABABAA
```
📌 **Exemple de réponse JSON** :
```json
[
    "Player A: 15 / Player B: 0",
    "Player A: 15 / Player B: 15",
    "Player A: 30 / Player B: 15",
    "Player A: 30 / Player B: 30",
    "Player A: 40 / Player B: 30",
    "Player A wins the game"
]
```

---

### 📡 **2️⃣ Envoyer une Séquence de Score à Kafka**
```bash
GET /send-message?message=ABABAA
```
📌 **Explication** :
- Cette requête envoie `"ABABAA"` à Kafka sur **le topic `receiveScore-in-0`**.
- Un consommateur Kafka (`receiveScore()`) **écoute ce topic, calcule le score et envoie chaque étape sur `sendScore-out-0`**.

---

## 📡 Fonctionnement de Kafka

### 🛠️ **3️⃣ Que se passe-t-il après `/send-message` ?**
- **1️⃣ `receiveScore()` écoute `receiveScore-in-0` et reçoit `"ABABAA"`**.
- **2️⃣ `computeScore()` transforme `"ABABAA"` en séquence de score**.
- **3️⃣ Chaque étape du score est envoyée à `sendScore-out-0`**.

---

## 📡 Vérifier Kafka

### 👅 **Lire les Messages du Topic `receiveScore-in-0`**
```bash
docker exec -it kafka kafka-console-consumer.sh --topic receiveScore-in-0 --from-beginning --bootstrap-server localhost:9092
```
📌 **Ce topic doit contenir** :
```
ABABAA
```

### 👅 **Lire les Scores Calculés dans `sendScore-out-0`**
```bash
docker exec -it kafka kafka-console-consumer.sh --topic sendScore-out-0 --from-beginning --bootstrap-server localhost:9092
```
📌 **Sortie attendue** :
```
Player A: 15 / Player B: 0
Player A: 15 / Player B: 15
Player A: 30 / Player B: 15
Player A: 30 / Player B: 30
Player A: 40 / Player B: 30
Player A wins the game
```

---

## ✅ Tests Unitaires

### 🔬 **Exécuter les Tests**
```bash
mvn test
```

📌 **Résultat attendu** :
```
✅ Test: KafkaConsumerService reçoit bien les messages
✅ Test: KafkaProducerService envoie bien les messages
✅ Test: ComputeScore fonctionne correctement
```

---

## 👥 Auteur
- **Développeur** : [HAMZA ELHAMDI](https://github.com/Miza1992)

---

## 🎯 Conclusion
Cette application est un **exemple de microservice basé sur Kafka**, permettant de **calculer et transmettre des scores de tennis** en temps réel.
- 📡 **Vous pouvez interagir directement avec l’API REST (`/compute-score`)**
- 🔄 **Vous pouvez également utiliser Kafka pour gérer les scores (`/send-message`)**

🚀 **Kafka et l’API REST fonctionnent ensemble pour un traitement en temps réel !** 🏉🔥"# tennis-score" 
