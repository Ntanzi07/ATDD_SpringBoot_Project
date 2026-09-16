import { useEffect, useState } from 'react'

// Hook para carregar uma lista e recarregar quando precisar
export function useResource(loader) {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [version, setVersion] = useState(0)

  useEffect(() => {
    let active = true
    setLoading(true)
    loader()
      .then(result => active && (setData(result ?? []), setError('')))
      .catch(err => active && setError(err.message))
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [version])

  return { data, loading, error, reload: () => setVersion(v => v + 1) }
}
