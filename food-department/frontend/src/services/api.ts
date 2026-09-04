import axios from 'axios';

function cleanApiBaseUrl(url: string | undefined): string {
  if (!url) return 'https://sih-awaq.onrender.com/api';
  let cleaned = url.trim().replace(/\/+$/, '');
  if (!cleaned.endsWith('/api')) {
    cleaned = `${cleaned}/api`;
  }
  return cleaned;
}

const PRODUCTION_BACKEND_URL = 'https://sih-awaq.onrender.com/api';
const RAW_URL = import.meta.env.VITE_API_BASE_URL || import.meta.env.VITE_API_URL || (import.meta.env.PROD ? PRODUCTION_BACKEND_URL : '/api');
const API_BASE_URL = RAW_URL === '/api' ? '/api' : cleanApiBaseUrl(RAW_URL);

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('govmesh_food_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('govmesh_food_token');
      localStorage.removeItem('govmesh_food_user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login?expired=true';
      }
    }
    return Promise.reject(error);
  }
);
