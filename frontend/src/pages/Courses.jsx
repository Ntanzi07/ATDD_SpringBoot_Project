import { useState } from 'react'
import { api } from '../api'
import { Alert, DeleteButton, Field, Modal, PageHeader } from '../components'
import { useResource } from '../hooks'
import { FindById } from './FindById'

export default function Courses() {
  const { data: courses, loading, error, reload } = useResource(api.courses.list)
  const [editing, setEditing] = useState(null)
  const [feedback, setFeedback] = useState({ type: 'success', message: '' })

  const handleDelete = async course => {
    try {
      await api.courses.remove(course.id)
      setFeedback({ type: 'success', message: `Curso #${course.id} excluído.` })
      reload()
    } catch (err) {
      setFeedback({ type: 'error', message: err.message })
    }
  }

  return (
    <section>
      <PageHeader title="Cursos" subtitle="POST · GET · GET /{id} · PUT · DELETE em /courses">
        <button type="button" className="ghost" onClick={reload}>Recarregar</button>
        <button type="button" onClick={() => setEditing('new')}>Novo curso</button>
      </PageHeader>

      <FindById label="Buscar curso por ID" fetcher={api.courses.get} />

      <Alert {...feedback} onClose={() => setFeedback({ ...feedback, message: '' })} />
      <Alert message={error} />

      {loading ? (
        <p className="muted">Carregando...</p>
      ) : courses.length === 0 ? (
        <p className="empty">Nenhum curso cadastrado.</p>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Título</th><th>Descrição</th><th className="right">Ações</th></tr>
            </thead>
            <tbody>
              {courses.map(c => (
                <tr key={c.id}>
                  <td className="mono">{c.id}</td>
                  <td>{c.title}</td>
                  <td className="muted">{c.description || '—'}</td>
                  <td className="right row-actions">
                    <button type="button" className="ghost" onClick={() => setEditing(c)}>Editar</button>
                    <DeleteButton onConfirm={() => handleDelete(c)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editing && (
        <CourseForm
          course={editing === 'new' ? null : editing}
          onClose={() => setEditing(null)}
          onSaved={saved => {
            setEditing(null)
            setFeedback({ type: 'success', message: `Curso #${saved.id} salvo.` })
            reload()
          }}
        />
      )}
    </section>
  )
}

function CourseForm({ course, onClose, onSaved }) {
  const [form, setForm] = useState({ title: course?.title ?? '', description: course?.description ?? '' })
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const set = key => e => setForm({ ...form, [key]: e.target.value })

  const handleSubmit = async e => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const saved = course ? await api.courses.update(course.id, form) : await api.courses.create(form)
      onSaved(saved)
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal title={course ? `Editar curso #${course.id}` : 'Novo curso'} onClose={onClose}>
      <form onSubmit={handleSubmit} className="form">
        <Field label="Título">
          <input value={form.title} onChange={set('title')} required autoFocus />
        </Field>
        <Field label="Descrição">
          <textarea rows="3" value={form.description} onChange={set('description')} />
        </Field>
        <Alert message={error} />
        <div className="form-actions">
          <button type="button" className="ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>
        </div>
      </form>
    </Modal>
  )
}
