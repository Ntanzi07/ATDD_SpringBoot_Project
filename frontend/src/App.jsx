import { useEffect, useState } from 'react'
import './App.css'
import { getBaseUrl, onRequest, readStorage, setBaseUrl, writeStorage } from './api'
import Courses from './pages/Courses'
import Registrations from './pages/Registrations'
import Signatures from './pages/Signatures'
import Users from './pages/Users'

const PAGES = [
  { key: 'users', label: 'Usuários', path: '/users', Component: Users },
  { key: 'courses', label: 'Cursos', path: '/courses', Component: Courses },
  { key: 'registrations', label: 'Matrículas', path: '/registration-numbers', Component: Registrations },
  { key: 'signatures', label: 'Assinaturas', path: '/signatures', Component: Signatures },
]

export default function App() {
  const [page, setPage] = useState(() => readStorage('atdd.page', 'users'))
  const [log, setLog] = useState([])
  const [logOpen, setLogOpen] = useState(true)

  useEffect(() => onRequest(entry => setLog(prev => [entry, ...prev].slice(0, 50))), [])

  const current = PAGES.find(p => p.key === page) ?? PAGES[0]
  const { Component } = current

  const go = key => {
    setPage(key)
    writeStorage('atdd.page', key)
  }

  return (
    <div className={`app ${logOpen ? 'with-log' : ''}`}>
      <nav className="sidebar">
        <div className="brand">
          <strong>TopClass</strong>
          <span className="muted small">Painel de testes da API</span>
        </div>
        {PAGES.map(p => (
          <button key={p.key} type="button" className={p.key === current.key ? 'nav active' : 'nav'} onClick={() => go(p.key)}>
            <span>{p.label}</span>
            <code>{p.path}</code>
          </button>
        ))}
        <ApiBase />
        <button type="button" className="ghost small-btn" onClick={() => setLogOpen(o => !o)}>
          {logOpen ? 'Esconder log' : 'Mostrar log'}
        </button>
      </nav>

      <main className="content">
        <Component key={current.key} />
      </main>

      {logOpen && <RequestLog entries={log} onClear={() => setLog([])} />}
    </div>
  )
}

function ApiBase() {
  const [value, setValue] = useState(getBaseUrl())
  const [saved, setSaved] = useState(false)

  return (
    <form
      className="api-base"
      onSubmit={e => {
        e.preventDefault()
        setBaseUrl(value)
        setSaved(true)
        setTimeout(() => window.location.reload(), 300)
      }}
    >
      <label className="field">
        <span className="small">URL da API</span>
        <input value={value} onChange={e => setValue(e.target.value)} placeholder="(mesma origem)" />
      </label>
      <button type="submit" className="ghost small-btn">{saved ? 'Salvo' : 'Aplicar'}</button>
    </form>
  )
}

function RequestLog({ entries, onClear }) {
  const [openId, setOpenId] = useState(null)

  return (
    <aside className="log">
      <div className="log-header">
        <h2>Requisições</h2>
        <button type="button" className="link" onClick={onClear}>Limpar</button>
      </div>
      {entries.length === 0 && <p className="muted small">As chamadas feitas para a API aparecem aqui.</p>}
      <ul>
        {entries.map(e => (
          <li key={e.id}>
            <button type="button" className="log-line" onClick={() => setOpenId(openId === e.id ? null : e.id)}>
              <span className={`method m-${e.method.toLowerCase()}`}>{e.method}</span>
              <span className="path">{e.url.replace(/^https?:\/\/[^/]+/, '')}</span>
              <span className={`status s-${String(e.status)[0]}`}>{e.status || 'ERR'}</span>
            </button>
            {openId === e.id && (
              <div className="log-detail">
                <small className="muted">{e.ms} ms · {e.at.toLocaleTimeString()}</small>
                {e.requestBody !== undefined && (
                  <>
                    <small>Request body</small>
                    <pre className="json">{JSON.stringify(e.requestBody, null, 2)}</pre>
                  </>
                )}
                <small>Response</small>
                <pre className="json">{typeof e.responseBody === 'string' ? e.responseBody : JSON.stringify(e.responseBody, null, 2)}</pre>
              </div>
            )}
          </li>
        ))}
      </ul>
    </aside>
  )
}
