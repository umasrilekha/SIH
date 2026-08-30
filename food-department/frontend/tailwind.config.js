/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        gov: {
          bg: '#f5f6f8',
          surface: '#ffffff',
          navy: '#1e293b',
          blue: '#1e40af',
          sky: '#f0f9ff',
          border: '#e2e8f0',
          text: '#0f172a',
          muted: '#64748b',
          saffron: '#d97706',
        }
      }
    },
  },
  plugins: [],
}
