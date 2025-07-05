<h1 align="center">Nothing</h1>

<p align="center">
  <img src="https://media.giphy.com/media/2IudUHdI075HL02Pkk/giphy.gif" width="200" alt="canteen gif">
</p>

<p align="center">
  <b>A Java Swing GUI + MySQL project to manage canteen operations with user roles</b><br>
  <i>Admin controls + Student meal bookings made easy 🥗</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Swing-UI-blue?style=for-the-badge" />
</p>

---

## 📸 Live Preview

> ⚠️ **This is a desktop-based application.** To preview, download and run it locally.
>  
> Want to convert it to web-based? Ask me! 😉

---

## 🚀 Features

### 👨‍💼 Admin
- 🔐 Login with credentials
- 🧮 Set/update today’s total meals
- 🍛 Add menu items with prices & timings

### 🎓 Student
- 🔐 Secure student login
- 👀 View meals and today's menu
- 📝 Book a meal with optional special request

---

## 📦 Tech Stack

| Tech     | Purpose             |
|----------|---------------------|
| Java     | Core programming    |
| Swing    | GUI development     |
| MySQL    | Database            |
| JDBC     | Database connector  |

---

## 🛠️ How to Setup

### ⚙️ Requirements

- 🟨 Java JDK 8+
- 🐬 MySQL Server
- 🧩 MySQL Connector/J (`.jar` file)

### 🧪 Clone & Run

```bash
git clone https://github.com/your-username/bookkarooo-java.git
cd canteen-management-java
javac -cp .;mysql-connector-java-8.0.xx.jar sql.java
java -cp .;mysql-connector-java-8.0.xx.jar sql
