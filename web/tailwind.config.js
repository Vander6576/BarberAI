/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: ["class"],
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: "#0d0e12",
        foreground: "#f3f4f6",
        card: "#16181f",
        gold: {
          DEFAULT: "#d9a05b",
          light: "#ecd0a5"
        },
        emerald: "#10b981",
        ruby: "#ef4444"
      }
    },
  },
  plugins: [],
}
