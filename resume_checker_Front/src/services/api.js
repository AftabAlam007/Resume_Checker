import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  // Do NOT set a global Content-Type header here — it breaks FormData uploads
});

// Helper to get JSON headers when needed
export const jsonHeaders = () => ({ 'Content-Type': 'application/json' });

// Add request interceptor to add token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Auth services
export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);
  return response.data;
};

export const signup = async (userData) => {
  const response = await api.post('/auth/register', userData);
  return response.data;
};

// Resume services
export const uploadResume = async (file, keywords) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('keywords', keywords);
  // Let the browser/axios set the Content-Type (with boundary) for multipart/form-data
  const response = await api.post('/resume/upload', formData);
  return response.data;
};

export const getResults = async () => {
  const response = await api.get('/resume/results');
  return response.data;
};

export default api;