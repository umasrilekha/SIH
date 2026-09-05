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

let isRefreshingToken = false;
let failedQueue: Array<{ resolve: (token: string) => void; reject: (err: any) => void }> = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token!);
    }
  });
  failedQueue = [];
};

api.interceptors.request.use(
  async (config) => {
    let token = localStorage.getItem('govmesh_food_token');
    if (!token && !config.url?.includes('/auth/login') && !config.url?.includes('/ws') && !config.url?.includes('/govmesh')) {
      // Auto-initialize demo officer session if missing
      try {
        const authRes = await axios.post(`${API_BASE_URL}/auth/login`, {
          username: 'food.officer',
          password: 'Food@123'
        });
        if (authRes.data?.token) {
          token = authRes.data.token;
          localStorage.setItem('govmesh_food_token', token as string);
          localStorage.setItem('govmesh_food_user', JSON.stringify(authRes.data.user));
        }
      } catch (e) {
        // Continue with unauthenticated request if login fails
      }
    }

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403) &&
      !originalRequest._retry &&
      !originalRequest.url?.includes('/auth/login')
    ) {
      if (isRefreshingToken) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return api(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshingToken = true;

      try {
        const refreshRes = await axios.post(`${API_BASE_URL}/auth/login`, {
          username: 'food.officer',
          password: 'Food@123'
        });
        const newToken = refreshRes.data?.token;
        if (newToken) {
          localStorage.setItem('govmesh_food_token', newToken);
          localStorage.setItem('govmesh_food_user', JSON.stringify(refreshRes.data.user));
          api.defaults.headers.common.Authorization = `Bearer ${newToken}`;
          originalRequest.headers.Authorization = `Bearer ${newToken}`;
          processQueue(null, newToken);
          isRefreshingToken = false;
          return api(originalRequest);
        }
      } catch (refreshErr) {
        processQueue(refreshErr, null);
        isRefreshingToken = false;
        localStorage.removeItem('govmesh_food_token');
        localStorage.removeItem('govmesh_food_user');
        if (window.location.pathname !== '/login') {
          window.location.href = '/login?expired=true';
        }
      }
    }
    return Promise.reject(error);
  }
);
