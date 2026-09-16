import { useState } from 'react'
import { api } from '../api'
import { Alert, DeleteButton, Field, Modal, PageHeader } from '../components'
import { useResource } from '../hooks'
import { FindById } from './FindById'

const EMPTY = { name: '', email: '', password: '' }

export default function Users() {
  const { data: users, loading, error, reload } = useResource(api.users.list)
  const [editing, setEditing] = useState(null) // null | 'new' | user
  const [feedback, setFeedback] = useState({ type: 'success', message: '' })

  const handleDelete = async user => {
    try {
      await api.users.remove(user.id)
      setFeedback({ type: 'success', message: `Usuário #${user.id} excluído.` })
      reload()
    } catch (err) {
      setFeedback({ type: 'error', message: err.message })
    }
  }

  return (
    <section>
      <PageHeader title="Usuários" subtitle="POST · GET · GET /{id} · PUT · DELETE em /users">
        <button type="button" className="ghost" onClick={reload}>Recarregar</button>
        <button type="button" onClick={() => setEditing('new')}>Novo usuário</button>
      </PageHeader>

      <FindById label="Buscar usuário por ID" fetcher={api.users.get} />

      <Alert {...feedback} onClose={() => setFeedback({ ...feedback, message: '' })} />
      <Alert message={error} />

      {loading ? (
        <p className="muted">Carregando...</p>
      ) : users.length === 0 ? (
        <p className="empty">Nenhum usuário cadastrado.</p>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Nome</th><th>E-mail</th><th className="right">Ações</th></tr>
            </thead>
            <tbody>
              {users.map(u => (
                <tr key={u.id}>
                  <td className="mono">{u.id}</td>
                  <td>{u.name}</td>
                  <td>{u.email}</td>
                  <td className="right row-actions">
                    <button type="button" className="ghost" onClick={() => setEditing(u)}>Editar</button>
                    <DeleteButton onConfirm={() => handleDelete(u)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editing && (
        <UserForm
          user={editing === 'new' ? null : editing}
          onClose={() => setEditing(null)}
          onSaved={saved => {
            setEditing(null)
            setFeedback({ type: 'success', message: `Usuário #${saved.id} salvo.` })
            reload()
          }}
        />
      )}
    </section>
  )
}

function UserForm({ user, onClose, onSaved }) {
  const [form, setForm] = useState(user ? { name: user.name, email: user.email, password: '' } : EMPTY)
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const set = key => e => setForm({ ...form, [key]: e.target.value })

  const handleSubmit = async e => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const saved = user ? await api.users.update(user.id, form) : await api.users.create(form)
      onSaved(saved)
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal title={user ? `Editar usuário #${user.id}` : 'Novo usuário'} onClose={onClose}>
      <form onSubmit={handleSubmit} className="form">
        <Field label="Nome">
          <input value={form.name} onChange={set('name')} required autoFocus />
        </Field>
        <Field label="E-mail">
          <input type="email" value={form.email} onChange={set('email')} required />
        </Field>
        <Field label="Senha" hint="Mínimo 8 caracteres, 1 letra maiúscula e 1 caractere especial. No PUT a senha é obrigatória.">
          <input type="password" value={form.password} onChange={set('password')} required />
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
