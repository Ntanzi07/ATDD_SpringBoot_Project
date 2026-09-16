import { useState } from 'react'
import { api } from '../api'
import { Alert, Badge, DeleteButton, Field, Modal, PageHeader } from '../components'
import { useResource } from '../hooks'
import { FindById } from './FindById'

const PREMIUM_GOAL = 12

export default function Signatures() {
  const { data: signatures, loading, error, reload } = useResource(api.signatures.list)
  const [editing, setEditing] = useState(null)
  const [feedback, setFeedback] = useState({ type: 'success', message: '' })

  const handleDelete = async sig => {
    try {
      await api.signatures.remove(sig.id)
      setFeedback({ type: 'success', message: `Assinatura #${sig.id} excluída.` })
      reload()
    } catch (err) {
      setFeedback({ type: 'error', message: err.message })
    }
  }

  return (
    <section>
      <PageHeader
        title="Assinaturas"
        subtitle="GET · GET /{id} · PUT · DELETE em /signatures — criadas automaticamente junto com o usuário"
      >
        <button type="button" className="ghost" onClick={reload}>Recarregar</button>
      </PageHeader>

      <FindById label="Buscar assinatura por ID" fetcher={api.signatures.get} />

      <Alert {...feedback} onClose={() => setFeedback({ ...feedback, message: '' })} />
      <Alert message={error} />

      {loading ? (
        <p className="muted">Carregando...</p>
      ) : signatures.length === 0 ? (
        <p className="empty">Nenhuma assinatura. Crie um usuário primeiro.</p>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th><th>Usuário</th><th>Plano</th><th>Cursos concluídos</th>
                <th>Créditos</th><th>Moedas</th><th className="right">Ações</th>
              </tr>
            </thead>
            <tbody>
              {signatures.map(s => (
                <tr key={s.id}>
                  <td className="mono">{s.id}</td>
                  <td className="mono">#{s.userId}</td>
                  <td><Badge value={s.plan} /></td>
                  <td><Progress value={s.successFinishedCourses} plan={s.plan} /></td>
                  <td>{s.courseCredits}</td>
                  <td>{s.coins}</td>
                  <td className="right row-actions">
                    <button type="button" className="ghost" onClick={() => setEditing(s)}>Editar</button>
                    <DeleteButton onConfirm={() => handleDelete(s)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editing && (
        <SignatureForm
          signature={editing}
          onClose={() => setEditing(null)}
          onSaved={saved => {
            setEditing(null)
            setFeedback({ type: 'success', message: `Assinatura #${saved.id} atualizada.` })
            reload()
          }}
        />
      )}
    </section>
  )
}

function Progress({ value, plan }) {
  const pct = Math.min(100, Math.round((value / PREMIUM_GOAL) * 100))
  return (
    <div className="progress" title={plan === 'PREMIUM' ? 'Premium' : `${value} de ${PREMIUM_GOAL} para Premium`}>
      <div className="progress-bar"><div style={{ width: `${pct}%` }} /></div>
      <span className="mono">{value}/{PREMIUM_GOAL}</span>
    </div>
  )
}

function SignatureForm({ signature, onClose, onSaved }) {
  const [form, setForm] = useState({
    plan: signature.plan,
    courseCredits: signature.courseCredits,
    successFinishedCourses: signature.successFinishedCourses,
    coins: signature.coins,
  })
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const setNumber = key => e => setForm({ ...form, [key]: e.target.value === '' ? '' : Number(e.target.value) })

  const handleSubmit = async e => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const dto = Object.fromEntries(Object.entries(form).map(([k, v]) => [k, v === '' ? null : v]))
      onSaved(await api.signatures.update(signature.id, dto))
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal title={`Editar assinatura #${signature.id}`} onClose={onClose}>
      <form onSubmit={handleSubmit} className="form">
        <Field label="Plano">
          <select value={form.plan} onChange={e => setForm({ ...form, plan: e.target.value })}>
            <option value="BASIC">BASIC</option>
            <option value="PREMIUM">PREMIUM</option>
          </select>
        </Field>
        <div className="grid-3">
          <Field label="Cursos concluídos">
            <input type="number" min="0" value={form.successFinishedCourses} onChange={setNumber('successFinishedCourses')} />
          </Field>
          <Field label="Créditos">
            <input type="number" min="0" value={form.courseCredits} onChange={setNumber('courseCredits')} />
          </Field>
          <Field label="Moedas">
            <input type="number" min="0" value={form.coins} onChange={setNumber('coins')} />
          </Field>
        </div>
        <p className="muted small">Campo vazio é enviado como null e o valor atual é mantido.</p>
        <Alert message={error} />
        <div className="form-actions">
          <button type="button" className="ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>
        </div>
      </form>
    </Modal>
  )
}
