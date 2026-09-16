// Cliente HTTP da API REST.
// - Em dev (vite na porta 5173) aponta para o Spring em localhost:8080.
// - No Docker o front e servido pelo proprio Spring, entao usa a mesma origem.
// - A URL pode ser trocada na tela (fica salva no navegador).

const DEFAULT_BASE = window.location.port === '5173' ? 'http://localhost:8080' : ''
const STORAGE_KEY = 'atdd.apiBase'

function readStorage(key, fallback) {
  try {
    const value = localStorage.getItem(key)
    return value ?? fallback
  } catch {
    return fallback
  }
}

function writeStorage(key, value) {
  try {
    localStorage.setItem(key, value)
  } catch {
    // navegador sem storage: segue so em memoria
  }
}

let baseUrl = readStorage(STORAGE_KEY, DEFAULT_BASE)
const listeners = new Set()

export function getBaseUrl() {
  return baseUrl
}

export function setBaseUrl(url) {
  baseUrl = url.replace(/\/+$/, '')
  writeStorage(STORAGE_KEY, baseUrl)
}

// Registra quem quer receber o historico de requisicoes (painel de log)
export function onRequest(listener) {
  listeners.add(listener)
  return () => listeners.delete(listener)
}

export class ApiError extends Error {
  constructor(status, message) {
    super(message || `Erro HTTP ${status}`)
    this.status = status
  }
}

async function request(method, path, body) {
  const url = `${baseUrl}${path}`
  const started = performance.now()
  const entry = { id: crypto.randomUUID(), method, url, requestBody: body, at: new Date() }

  try {
    const response = await fetch(url, {
      method,
      headers: body !== undefined ? { 'Content-Type': 'application/json' } : undefined,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
    const text = await response.text()
    let data = text
    try {
      data = text ? JSON.parse(text) : null
    } catch {
      // resposta nao e JSON (ex.: mensagem de erro em texto)
    }

    Object.assign(entry, { status: response.status, responseBody: data, ms: Math.round(performance.now() - started) })
    listeners.forEach(l => l(entry))

    if (!response.ok) {
      const message = typeof data === 'string' ? data : data?.message || data?.error
      throw new ApiError(response.status, message)
    }
    return data
  } catch (err) {
    if (!(err instanceof ApiError)) {
      Object.assign(entry, { status: 0, responseBody: String(err), ms: Math.round(performance.now() - started) })
      listeners.forEach(l => l(entry))
      throw new ApiError(0, `Nao foi possivel conectar em ${url}. A API esta rodando?`)
    }
    throw err
  }
}

export const api = {
  users: {
    list: () => request('GET', '/users'),
    get: id => request('GET', `/users/${id}`),
    create: dto => request('POST', '/users', dto),
    update: (id, dto) => request('PUT', `/users/${id}`, dto),
    remove: id => request('DELETE', `/users/${id}`),
  },
  courses: {
    list: () => request('GET', '/courses'),
    get: id => request('GET', `/courses/${id}`),
    create: dto => request('POST', '/courses', dto),
    update: (id, dto) => request('PUT', `/courses/${id}`, dto),
    remove: id => request('DELETE', `/courses/${id}`),
  },
  signatures: {
    list: () => request('GET', '/signatures'),
    get: id => request('GET', `/signatures/${id}`),
    update: (id, dto) => request('PUT', `/signatures/${id}`, dto),
    remove: id => request('DELETE', `/signatures/${id}`),
  },
  registrations: {
    get: id => request('GET', `/registration-numbers/${id}`),
    enroll: dto => request('POST', '/registration-numbers', dto),
    conclude: (id, dto) => request('PATCH', `/registration-numbers/${id}/conclude`, dto),
  },
}

export { readStorage, writeStorage }
