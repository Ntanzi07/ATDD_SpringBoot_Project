import { useState } from 'react'
import { Alert } from '../components'

// Formulario reutilizavel para testar os endpoints GET /{id}
export function FindById({ label, fetcher, render }) {
  const [id, setId] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async e => {
    e.preventDefault()
    setLoading(true)
    setError('')
    setResult(null)
    try {
      setResult(await fetcher(id))
    } catch (err) {
      setError(err.status ? `${err.status} — ${err.message}` : err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card find">
      <form onSubmit={handleSubmit} className="inline-form">
        <label className="field grow">
          <span>{label}</span>
          <input type="number" min="1" value={id} onChange={e => setId(e.target.value)} placeholder="ID" required />
        </label>
        <button type="submit" className="ghost" disabled={loading}>{loading ? 'Buscando...' : 'Buscar'}</button>
      </form>
      <Alert message={error} />
      {result && (render ? render(result) : <pre className="json">{JSON.stringify(result, null, 2)}</pre>)}
    </div>
  )
}
