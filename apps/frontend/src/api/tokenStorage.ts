import type { AuthenticatedUser } from "../interfaces/Auth";

const TOKEN_KEY = "tucasdesk.token";
const REFRESH_TOKEN_KEY = "tucasdesk.refreshToken";
const USER_KEY = "tucasdesk.usuario";

type StorageType = "local" | "session";

export interface StoredAuth {
  token: string | null;
  refreshToken: string | null;
  usuario: AuthenticatedUser | null;
  storageType: StorageType | null;
}

type AuthChangeEvent = {
  type: "update" | "clear";
  data: StoredAuth;
};

type AuthChangeListener = (event: AuthChangeEvent) => void;

const listeners = new Set<AuthChangeListener>();

const isBrowser = typeof window !== "undefined";

function notify(type: AuthChangeEvent["type"]) {
  const data = getStoredAuth();
  listeners.forEach((listener) => listener({ type, data }));
}

function getStorage(type: StorageType): Storage {
  return type === "local" ? window.localStorage : window.sessionStorage;
}

function clearStorage(storage: Storage) {
  storage.removeItem(TOKEN_KEY);
  storage.removeItem(REFRESH_TOKEN_KEY);
  storage.removeItem(USER_KEY);
}

function setTokenValues(
  storage: Storage,
  token: string,
  refreshToken: string | null,
  usuario: AuthenticatedUser | null,
) {
  storage.setItem(TOKEN_KEY, token);
  if (refreshToken) {
    storage.setItem(REFRESH_TOKEN_KEY, refreshToken);
  } else {
    storage.removeItem(REFRESH_TOKEN_KEY);
  }
  if (usuario) {
    storage.setItem(USER_KEY, JSON.stringify(usuario));
  } else {
    storage.removeItem(USER_KEY);
  }
}

function parseUsuario(raw: string | null): AuthenticatedUser | null {
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw) as AuthenticatedUser;
  } catch (error) {
    console.error("Failed to parse stored user payload", error);
    return null;
  }
}

/**
 * Retrieves authentication information (tokens and user data) from storage.
 *
 * @returns {StoredAuth} The stored authentication data.
 */
export function getStoredAuth(): StoredAuth {
  if (!isBrowser) {
    return { token: null, refreshToken: null, usuario: null, storageType: null };
  }

  const localToken = window.localStorage.getItem(TOKEN_KEY);
  const sessionToken = window.sessionStorage.getItem(TOKEN_KEY);

  const storage = localToken
    ? window.localStorage
    : sessionToken
      ? window.sessionStorage
      : null;

  const storageType: StorageType | null = storage
    ? storage === window.localStorage
      ? "local"
      : "session"
    : null;

  const token = localToken ?? sessionToken;
  const refreshToken = storage ? storage.getItem(REFRESH_TOKEN_KEY) : null;
  const usuario = storage ? parseUsuario(storage.getItem(USER_KEY)) : null;

  return {
    token,
    refreshToken,
    usuario,
    storageType,
  };
}

/**
 * Persists the authentication state (tokens and user data) to the appropriate storage.
 *
 * @param {object} params - The authentication data to persist.
 * @param {string} params.token - The access token.
 * @param {string | null} [params.refreshToken] - The refresh token.
 * @param {AuthenticatedUser | null} [params.usuario] - The authenticated user's data.
 * @param {boolean} params.remember - Whether to use local storage (persistent) or session storage.
 * @returns {StorageType | null} The type of storage used, or null if not in a browser environment.
 */
export function persistAuthState(params: {
  token: string;
  refreshToken?: string | null;
  usuario?: AuthenticatedUser | null;
  remember: boolean;
}): StorageType | null {
  if (!isBrowser) {
    return null;
  }

  const targetStorage = params.remember ? window.localStorage : window.sessionStorage;

  clearStorage(window.localStorage);
  clearStorage(window.sessionStorage);

  setTokenValues(targetStorage, params.token, params.refreshToken ?? null, params.usuario ?? null);

  const storageType = targetStorage === window.localStorage ? "local" : "session";
  notify("update");
  return storageType;
}

/**
 * Updates the stored user information.
 *
 * @param {AuthenticatedUser | null} usuario - The user data to store.
 * @param {StorageType | null} [storageType] - The type of storage to use.
 */
export function persistUsuario(
  usuario: AuthenticatedUser | null,
  storageType?: StorageType | null,
) {
  if (!isBrowser) {
    return;
  }

  const effectiveType = storageType ?? getStoredAuth().storageType;
  if (!effectiveType) {
    return;
  }

  const storage = getStorage(effectiveType);
  if (usuario) {
    storage.setItem(USER_KEY, JSON.stringify(usuario));
  } else {
    storage.removeItem(USER_KEY);
  }
  notify("update");
}

/**
 * Updates the stored authentication tokens.
 *
 * @param {string} token - The new access token.
 * @param {string | null} [refreshToken] - The new refresh token.
 * @param {StorageType | null} [storageType] - The type of storage to use.
 */
export function updateStoredTokens(
  token: string,
  refreshToken?: string | null,
  storageType?: StorageType | null,
) {
  if (!isBrowser) {
    return;
  }

  const effectiveType = storageType ?? getStoredAuth().storageType;
  if (!effectiveType) {
    return;
  }

  const storage = getStorage(effectiveType);
  storage.setItem(TOKEN_KEY, token);
  if (refreshToken !== undefined) {
    if (refreshToken) {
      storage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    } else {
      storage.removeItem(REFRESH_TOKEN_KEY);
    }
  }
  notify("update");
}

/**
 * Clears all stored authentication data from both local and session storage.
 */
export function clearStoredAuth() {
  if (!isBrowser) {
    return;
  }

  clearStorage(window.localStorage);
  clearStorage(window.sessionStorage);
  notify("clear");
}

/**
 * Subscribes a listener to authentication state changes.
 *
 * @param {AuthChangeListener} listener - The callback function to execute on change.
 * @returns {() => void} A function that unsubscribes the listener when called.
 */
export function subscribeToAuthChanges(listener: AuthChangeListener) {
  listeners.add(listener);
  return () => {
    listeners.delete(listener);
  };
}
