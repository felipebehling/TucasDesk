import axios from "axios";

/**
 * An Axios instance configured for making requests to the TucasDesk backend API.
 * The base URL is set to `http://localhost:8080`.
 */
const api = axios.create({
  baseURL: "http://localhost:8080", // Base URL for the backend
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * Axios request interceptor to automatically add the JWT to the Authorization header.
 * It retrieves the token from local storage and adds it as a Bearer token.
 */
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
