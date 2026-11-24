# AI Khobor24

AI Khobor24 is a personal Android application and automated backend pipeline designed to run a 24/7 Bengali/English AI news service for **aikhobor24.blogspot.com**.

## Architecture

- **Android App (Kotlin + Jetpack Compose):** Thin client controlling dashboard, news sources, article editor, automation scheduler, and logs.
- **Backend (Google Apps Script + Google Sheets):** Runs 24/7 off-phone automation for RSS collection, duplicate checking, Gemini AI summarization and article generation, and Blogger API v3 draft creation/publishing.

## Setup Instructions

1. **Backend Deployment:**
   - Create a Google Sheet with the required tabs (`Sources`, `News`, `Research`, `Articles`, `Images`, `Categories`, `Labels`, `Settings`, `Jobs`, `Logs`, `Usage`).
   - Create a Google Apps Script project and paste the contents of `/backend/Code.gs`.
   - Set Script Properties: `SPREADSHEET_ID`, `OWNER_EMAIL`, `GEMINI_API_KEY`, `BLOGGER_BLOG_ID`.
   - Deploy as Web App (Execute as: Me, Who has access: Anyone with Google Account or Anyone).

2. **Android App:**
   - Open the app, enter your Apps Script Web App URL and Owner Email in the setup screen.
   - Tap **Test Connections** and proceed to the dashboard.
   - Manage news, generate Bengali articles, review drafts in Blogger, and configure automation.
