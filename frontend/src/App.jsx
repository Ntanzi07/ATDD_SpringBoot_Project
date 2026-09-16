import { useState, useEffect } from 'react'
import './App.css'

const API = window.location.port === '5173' ? 'http://localhost:8080' : ''

function App() {
  const [page, setPage] = useState('users')

  return (
    <div className="app">
      <nav className="sidebar">
        <h2>ATDD</h2>
        <button className={page === 'users' ? 'active' : ''} onClick={() => setPage('users')}>Usuários</button>
        <button className={page === 'courses' ? 'active' : ''} onClick={() => setPage('courses')}>Cursos</button>
        <button className={page === 'create-user' ? 'active' : ''} onClick={() => setPage('create-user')}>Novo Usuário</button>
        <button className={page === 'create-course' ? 'active' : ''} onClick={() => setPage('create-course')}>Novo Curso</button>
        <button className={page === 'registration' ? 'active' : ''} onClick={() => setPage('registration')}>Matrícula</button>
        <button className={page === 'status' ? 'active' : ''} onClick={() => setPage('status')}>Status Matrícula</button>
      </nav>
      <main className="content">
        {page === 'users' && <UserList />}
        {page === 'courses' && <CourseList />}
        {page === 'create-user' && <CreateUser onCreated={() => setPage('users')} />}
        {page === 'create-course' && <CreateCourse />}
        {page === 'registration' && <CreateRegistration />}
        {page === 'status' && <RegistrationStatus />}
      </main>
    </div>
  )
}

function UserList() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch(`${API}/users`)
      .then(r => r.json())
      .then(data => setUsers(data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <p>Carregando...</p>

  return (
    <div>
      <h1>Usuários</h1>
      {users.length === 0 ? (
        <p>Nenhum usuário cadastrado.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.name}</td>
                <td>{u.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

function CourseList() {
  const [courses, setCourses] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch(`${API}/courses`)
      .then(r => r.json())
      .then(data => setCourses(data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <p>Carregando...</p>

  return (
    <div>
      <h1>Cursos</h1>
      {courses.length === 0 ? (
        <p>Nenhum curso cadastrado.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Título</th>
              <th>Descrição</th>
            </tr>
          </thead>
          <tbody>
            {courses.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.title}</td>
                <td>{c.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

function CreateUser({ onCreated }) {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    fetch(`${API}/users`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    })
      .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t) })
        return r.json()
      })
      .then(() => {
        alert('Usuário criado!')
        onCreated()
      })
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }

  return (
    <div>
      <h1>Novo Usuário</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Nome
          <input type="text" value={name} onChange={e => setName(e.target.value)} required />
        </label>
        <label>
          Email
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />
        </label>
        <label>
          Senha
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Criando...' : 'Criar'}
        </button>
      </form>
    </div>
  )
}

function CreateCourse() {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    fetch(`${API}/courses`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, description })
    })
      .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t) })
        return r.json()
      })
      .then(() => {
        alert('Curso criado!')
        setTitle('')
        setDescription('')
      })
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }

  return (
    <div>
      <h1>Novo Curso</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Título
          <input type="text" value={title} onChange={e => setTitle(e.target.value)} required />
        </label>
        <label>
          Descrição
          <input type="text" value={description} onChange={e => setDescription(e.target.value)} required />
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Criando...' : 'Criar'}
        </button>
      </form>
    </div>
  )
}

function CreateRegistration() {
  const [userId, setUserId] = useState('')
  const [courseId, setCourseId] = useState('')
  const [users, setUsers] = useState([])
  const [courses, setCourses] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    fetch(`${API}/users`).then(r => r.json()).then(setUsers).catch(() => {})
    fetch(`${API}/courses`).then(r => r.json()).then(setCourses).catch(() => {})
  }, [])

  const handleSubmit = (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    fetch(`${API}/registration-numbers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: Number(userId), courseId: Number(courseId), bonus: false })
    })
      .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t) })
        return r.json()
      })
      .then(() => {
        alert('Matrícula criada!')
        setUserId('')
        setCourseId('')
      })
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }

  return (
    <div>
      <h1>Nova Matrícula</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Usuário
          <select value={userId} onChange={e => setUserId(e.target.value)} required>
            <option value="">Selecione...</option>
            {users.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
          </select>
        </label>
        <label>
          Curso
          <select value={courseId} onChange={e => setCourseId(e.target.value)} required>
            <option value="">Selecione...</option>
            {courses.map(c => <option key={c.id} value={c.id}>{c.title}</option>)}
          </select>
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Matriculando...' : 'Matricular'}
        </button>
      </form>
    </div>
  )
}

function RegistrationStatus() {
  const [regId, setRegId] = useState('')
  const [data, setData] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSearch = (e) => {
    e.preventDefault()
    setError('')
    setData(null)
    setLoading(true)

    fetch(`${API}/registration-numbers/${regId}`)
      .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t) })
        return r.json()
      })
      .then(setData)
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }

  return (
    <div>
      <h1>Status da Matrícula</h1>
      <form onSubmit={handleSearch}>
        <label>
          ID da Matrícula
          <input type="number" value={regId} onChange={e => setRegId(e.target.value)} required />
        </label>
        <button type="submit" disabled={loading}>
          {loading ? 'Buscando...' : 'Buscar'}
        </button>
      </form>

      {error && <p className="error">{error}</p>}

      {data && (
        <div className="status-card">
          <h2>Matrícula #{data.id}</h2>
          <p><strong>Usuário ID:</strong> {data.userId}</p>
          <p><strong>Curso ID:</strong> {data.courseId}</p>
          <p><strong>Status:</strong> <span className={`badge ${data.status}`}>{data.status}</span></p>
          {data.finalGrade != null && <p><strong>Nota Final:</strong> {data.finalGrade}</p>}
          <p><strong>Bônus:</strong> {data.bonus ? 'Sim' : 'Não'}</p>
        </div>
      )}
    </div>
  )
}

export default App