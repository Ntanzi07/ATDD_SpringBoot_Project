import { useCallback, useEffect, useState } from 'react'
import { api, readStorage, writeStorage } from '../api'
import { Alert, Badge, Field, Modal, PageHeader } from '../components'
import { useResource } from '../hooks'
import { FindById } from './FindById'

// A API nao tem GET /registration-numbers (listar todas),
// entao guardamos no navegador os IDs das matriculas criadas/buscadas aqui.
const STORAGE_KEY = 'atdd.registrationIds'

function loadIds() {
  try {
    return JSON.parse(readStorage(STORAGE_KEY, '[]'))
  } catch {
    return []
  }
}

export default function Registrations() {
  const users = useResource(api.users.list)
  const courses = useResource(api.courses.list)
  const [ids, setIds] = useState(loadIds)
  const [rows, setRows] = useState({})
  const [concluding, setConcluding] = useState(null)
  const [feedback, setFeedback] = useState({ type: 'success', message: '' })

  const track = useCallback(id => {
    setIds(prev => {
      const next = prev.includes(id) ? prev : [id, ...prev]
      writeStorage(STORAGE_KEY, JSON.stringify(next))
      return next
    })
  }, [])

  const untrack = id => {
    const next = ids.filter(i => i !== id)
    writeStorage(STORAGE_KEY, JSON.stringify(next))
    setIds(next)
  }

  const refresh = useCallback(async () => {
    const entries = await Promise.all(
      ids.map(id => api.registrations.get(id).then(r => [id, r]).catch(err => [id, { error: err.message }])),
    )
    setRows(Object.fromEntries(entries))
  }, [ids])

  useEffect(() => {
    refresh()
  }, [refresh])

  const userName = id => users.data.find(u => u.id === id)?.name ?? `#${id}`
  const courseTitle = id => courses.data.find(c => c.id === id)?.title ?? `#${id}`

  return (
    <section>
      <PageHeader title="Matrículas" subtitle="POST · GET /{id} · PATCH /{id}/conclude em /registration-numbers">
        <button type="button" className="ghost" onClick={refresh}>Recarregar</button>
      </PageHeader>

      <EnrollForm
        users={users.data}
        courses={courses.data}
        onEnrolled={reg => {
          track(reg.id)
          setFeedback({ type: 'success', message: `Matrícula #${reg.id} criada (IN_PROGRESS).` })
        }}
      />

      <FindById
        label="Buscar matrícula por ID"
        fetcher={api.registrations.get}
        render={reg => (
          <div className="find-result">
            <pre className="json">{JSON.stringify(reg, null, 2)}</pre>
            <button type="button" className="ghost" onClick={() => track(reg.id)}>Adicionar na lista</button>
          </div>
        )}
      />

      <Alert {...feedback} onClose={() => setFeedback({ ...feedback, message: '' })} />

      {ids.length === 0 ? (
        <p className="empty">Nenhuma matrícula nesta lista ainda. Crie uma acima.</p>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th><th>Aluno</th><th>Curso</th><th>Status</th><th>Nota</th><th>Bônus</th>
                <th className="right">Ações</th>
              </tr>
            </thead>
            <tbody>
              {ids.map(id => {
                const r = rows[id]
                if (!r) return <tr key={id}><td className="mono">{id}</td><td colSpan="6" className="muted">Carregando...</td></tr>
                if (r.error) {
                  return (
                    <tr key={id}>
                      <td className="mono">{id}</td>
                      <td colSpan="5" className="error-text">{r.error}</td>
                      <td className="right"><button type="button" className="ghost" onClick={() => untrack(id)}>Remover</button></td>
                    </tr>
                  )
                }
                return (
                  <tr key={id}>
                    <td className="mono">{r.id}</td>
                    <td>{userName(r.userId)}</td>
                    <td>{courseTitle(r.courseId)}</td>
                    <td><Badge value={r.status} /></td>
                    <td className="mono">{r.finalGrade ?? '—'}</td>
                    <td>{r.bonus ? 'Sim' : 'Não'}</td>
                    <td className="right row-actions">
                      <button type="button" disabled={r.status !== 'IN_PROGRESS'} onClick={() => setConcluding(r)}>Concluir</button>
                      <button type="button" className="ghost" onClick={() => untrack(id)}>Tirar da lista</button>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}

      {concluding && (
        <ConcludeForm
          registration={concluding}
          onClose={() => setConcluding(null)}
          onConcluded={message => {
            setConcluding(null)
            setFeedback({ type: 'success', message })
            refresh()
          }}
        />
      )}
    </section>
  )
}

function EnrollForm({ users, courses, onEnrolled }) {
  const [form, setForm] = useState({ userId: '', courseId: '', bonus: false })
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  const handleSubmit = async e => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const reg = await api.registrations.enroll({
        userId: Number(form.userId),
        courseId: Number(form.courseId),
        bonus: form.bonus,
      })
      onEnrolled(reg)
      setForm({ ...form, courseId: '' })
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <form className="card inline-form wrap" onSubmit={handleSubmit}>
      <Field label="Aluno">
        <select value={form.userId} onChange={e => setForm({ ...form, userId: e.target.value })} required>
          <option value="">Selecione...</option>
          {users.map(u => <option key={u.id} value={u.id}>#{u.id} — {u.name}</option>)}
        </select>
      </Field>
      <Field label="Curso">
        <select value={form.courseId} onChange={e => setForm({ ...form, courseId: e.target.value })} required>
          <option value="">Selecione...</option>
          {courses.map(c => <option key={c.id} value={c.id}>#{c.id} — {c.title}</option>)}
        </select>
      </Field>
      <label className="check">
        <input type="checkbox" checked={form.bonus} onChange={e => setForm({ ...form, bonus: e.target.checked })} />
        Bônus
      </label>
      <button type="submit" disabled={saving}>{saving ? 'Matriculando...' : 'Matricular'}</button>
      {error && <div className="full"><Alert message={error} /></div>}
    </form>
  )
}

function ConcludeForm({ registration, onClose, onConcluded }) {
  const [grade, setGrade] = useState('')
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  const findSignature = async userId => (await api.signatures.list()).find(s => s.userId === userId)

  const handleSubmit = async e => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const before = await findSignature(registration.userId)
      const result = await api.registrations.conclude(registration.id, { finalGrade: Number(grade) })
      const after = await findSignature(registration.userId)

      let message = `Matrícula #${result.id} concluída com nota ${result.finalGrade}.`
      if (after) {
        message += ` Cursos concluídos: ${after.successFinishedCourses}. Plano: ${after.plan}.`
        if (before?.plan === 'BASIC' && after.plan === 'PREMIUM') message += ' Promovido para PREMIUM!'
      }
      onConcluded(message)
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal title={`Concluir matrícula #${registration.id}`} onClose={onClose}>
      <form onSubmit={handleSubmit} className="form">
        <Field label="Nota final" hint="Entre 0 e 10. Nota >= 7 conta como curso concluido (12 cursos = PREMIUM).">
          <input type="number" step="0.1" value={grade} onChange={e => setGrade(e.target.value)} required autoFocus />
        </Field>
        <Alert message={error} />
        <div className="form-actions">
          <button type="button" className="ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" disabled={saving}>{saving ? 'Enviando...' : 'Concluir'}</button>
        </div>
      </form>
    </Modal>
  )
}
