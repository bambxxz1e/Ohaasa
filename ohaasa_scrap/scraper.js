import puppeteer from "puppeteer";
import mysql from "mysql2/promise";
import fetch from "node-fetch"; // 번역 API용
import dotenv from "dotenv";

dotenv.config();

// DB 연결
const pool = mysql.createPool({
  host: "localhost",
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
});

// 일본어 → 한국어 번역 함수
const translateToKorean = async (text) => {
  const res = await fetch(
    `https://translate.googleapis.com/translate_a/single?client=gtx&sl=ja&tl=ko&dt=t&q=${encodeURIComponent(
      text
    )}`
  );
  const data = await res.json();
  return data[0].map((d) => d[0]).join("");
};

// 일본 별자리 → 한국 별자리 매핑 함수
const mapZodiac = (jpZodiac) => {
  switch (jpZodiac) {
    case "おひつじ座":
      return "양자리";
    case "おうし座":
      return "황소자리";
    case "ふたご座":
      return "쌍둥이자리";
    case "かに座":
      return "게자리";
    case "しし座":
      return "사자자리";
    case "おとめ座":
      return "처녀자리";
    case "てんびん座":
      return "천칭자리";
    case "さそり座":
      return "전갈자리";
    case "いて座":
      return "사수자리";
    case "やぎ座":
      return "염소자리";
    case "みずがめ座":
      return "물병자리";
    case "うお座":
      return "물고기자리";
    default:
      return jpZodiac; // 모르는 경우 그대로
  }
};

// 스크래핑 함수
const scrapeHoroscope = async () => {
  const url = "https://www.asahi.co.jp/ohaasa/week/horoscope/index.html";
  const browser = await puppeteer.launch({ headless: "new" });
  const page = await browser.newPage();
  await page.goto(url, { waitUntil: "networkidle2" });

  // 별자리 블록 선택
  const horoscopes = await page.$$eval("ul.oa_horoscope_list > li", (items) =>
    items.map((item) => {
      const rank =
        parseInt(item.querySelector("span.horo_rank")?.innerText) || null;
      const zodiacSign =
        item.querySelector("sapn[class*='horo_name']")?.innerText || null;
      const fullText = item.querySelector("dd.horo_txt")?.innerText || null;

      return { rank, zodiacSign, advice: fullText };
    })
  );

  await browser.close();
  return horoscopes;
};

// DB 저장 함수
const saveToDB = async (horoscopes) => {
  const today = new Date().toISOString().split("T")[0];

  // 서버 실행 시 테이블 초기화
  await pool.execute("TRUNCATE TABLE horoscope");

  for (let h of horoscopes) {
    // 일본어 → 한국어 번역
    const adviceKR = h.advice ? await translateToKorean(h.advice) : null;
    const zodiacKR = h.zodiacSign ? mapZodiac(h.zodiacSign) : null;

    await pool.execute(
      "INSERT INTO horoscope (`date`, `rank`, zodiacSign, advice) VALUES (?, ?, ?, ?)",
      [today, h.rank, zodiacKR, adviceKR]
    );
  }

  console.log(`✅ 오늘 운세 ${horoscopes.length}개 DB 저장 완료`);
};

(async () => {
  const horoscopes = await scrapeHoroscope();
  await saveToDB(horoscopes);
})();
