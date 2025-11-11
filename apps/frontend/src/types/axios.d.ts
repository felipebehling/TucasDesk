import "axios";

declare module "axios" {
  interface InternalAxiosRequestConfig {
    skipAuth?: boolean;
    _retry?: boolean;
  }

  interface AxiosRequestConfig {
    skipAuth?: boolean;
  }
}
