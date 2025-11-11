import { spawnSync } from 'node:child_process';
import { existsSync, readFileSync } from 'node:fs';
import { resolve } from 'node:path';

const env = { ...process.env };

function loadEnvValue() {
  const current = env.VITE_API_URL?.trim();
  if (current) {
    return current;
  }

  const envPath = resolve(process.cwd(), '.env');
  if (!existsSync(envPath)) {
    return undefined;
  }

  const content = readFileSync(envPath, 'utf8');
  for (const rawLine of content.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith('#')) {
      continue;
    }

    const delimiterIndex = line.indexOf('=');
    if (delimiterIndex === -1) {
      continue;
    }

    const key = line.slice(0, delimiterIndex).trim();
    if (key !== 'VITE_API_URL') {
      continue;
    }

    const value = line.slice(delimiterIndex + 1).trim();
    if (!value) {
      continue;
    }

    return value.replace(/^['"]|['"]$/g, '').trim();
  }

  return undefined;
}

const viteApiUrl = loadEnvValue() || 'http://tucasdesk-backend:8080';
env.VITE_API_URL = viteApiUrl;

const result = spawnSync('npm', ['run', '--silent', 'build:compile'], {
  stdio: 'inherit',
  env
});

if (result.error) {
  throw result.error;
}

if (typeof result.status === 'number' && result.status !== 0) {
  process.exit(result.status);
}
