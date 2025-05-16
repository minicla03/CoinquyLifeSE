<p align="center">
  <img src="https://github.com/user-attachments/assets/4d4eafbf-9908-4a72-a9aa-9e93a7e5eb09" alt="Logo CoinquyLife" width="200" />
</p>

# 🏠 CoinquyLife – WebApp Microservizi

**CoinquyLife** è una piattaforma web pensata per semplificare la vita in una casa condivisa. Ogni funzionalità è implementata come **microservizio indipendente**, seguendo l'architettura **Spring Boot + MVC**, e comunicano tra loro tramite API REST.

---

## ⚙️ Funzionalità principali

- 🧹 **Gestione dei turni** – Crea e assegna turni di pulizia o attività.
- 💸 **Gestione delle spese** – Registra spese condivise e calcola bilanci.
- 📬 **Bacheca dei messaggi** – Invia e ricevi comunicazioni tra coinquilini.
- 📊 **Sondaggi** – Partecipa a decisioni comuni tramite votazioni.
- 🏆 **Classifica gamificata** – Guadagna punti per attività completate.
- 🏠 **Selezione della casa** – Crea o entra in una casa condivisa.

---

## 🧱 Architettura Tecnica

- **Framework**: Spring Boot
- **Stile Architetturale**: Microservizi REST
- **Pattern per ogni microservizio**: MVC (`Model`, `Service`, `Controller`)
- **Persistenza**: MongoDB
- **Comunicazione**: REST (`RestTemplate`)
- **Sicurezza**: JWT con Spring Security (nel microservizio Auth)
- **Containerizzazione**: Docker e Docker Compose

![image](https://github.com/user-attachments/assets/29f255e6-fe59-4b7a-a041-dbc80a478edc)

---

## 📁 Struttura del Progetto

``` 
CoinquyLife-Web/
│
├── docker-compose.yml
│
├── AuthMicroservice/
│   ├── Dockerfile
│   └── src/main/java/com/coinquylife/auth/...
│
├── HouseSelectionMicroservice/
│   ├── Dockerfile
│   └── src/main/java/com/coinquylife/house/...
│
├── TurnManagementMicroservice/
│   └── ...
│
├── ExpenseMicroservice/
│   └── ...
│
└── ...
``` 
---

#### 📂 Livelli principali
``` 
/src/main/java/com/coinquylife/<servizio>/
├── controller/     --> RestController con endpoint REST
├── model/          --> Entity o DTO
├── repository/     --> Interfacce JPA/Mongo
├── service/        --> Logica di business
└── config/         --> Configurazioni (Bean, Security, ecc.)
``` 
---

## 🖥️ Frontend: Web UI

### 📁 Struttura tipica
``` 
resource/static             
│   ├── index.html
│   ├── css/
│   └── js/
``` 
## 🎯 Requisiti

- **Java 21**
- **Spring Boot 3.4.5**
- **Maven**
- **Docker + Docker Compose**

---

👋 Grazie per aver scelto **CoinquyLife**!

Semplifica la convivenza, organizza la tua casa e rendi ogni giornata più leggera.  
Con affetto,  
**💙 Il CoinquyTeam**

## 👥 Il Team CoinquyLife

| Nome Completo               | 
|-----------------------------|
| Aragonese Alessandra        | 
| Maturo Christian            | 
| Mininno Claudio             |
| Ricci Giuseppe              | 
| Simeone Lucia               | 

---

**Grazie a tutto il team per il duro lavoro e la passione!** 💙

---
