import mysql from "mysql2/promise";
import dotenv from "dotenv";

dotenv.config();

export const db = await mysql.createConnection({
  host: "localhost",
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: "ohaasa",
});
