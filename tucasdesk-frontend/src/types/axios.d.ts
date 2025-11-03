import "axios";

declare module "axios" {
  interface InternalAxiosRequestConfig<D = any> {
    skipAuth?: boolean;
    _retry?: boolean;
  }

  interface AxiosRequestConfig<D = any> {
    skipAuth?: boolean;
  }
}
