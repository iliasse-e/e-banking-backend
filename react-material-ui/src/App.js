import './App.css';
import { QueryClient, QueryClientProvider, useQuery } from 'react-query'
import axios from 'axios';

const queryClient = new QueryClient()

const getDepartements = async () => {
  const res = await axios.get('https://geo.api.gouv.fr/departements')
  return res.data
}

function Example() {
  const { isLoading, error, data, isFetching } = useQuery('repoData', getDepartements)
  if (data) console.log(data, data[0])

  if (isLoading) return 'Loading...'  
  if (error) return 'An error has occurred: ' + error.message

  return (
    <div>
      <div>{isFetching ? "Updating..." : ""}</div>
      {data.map((dep) => 
        <p key={dep.nom}>{dep.nom}</p>
      )}
    </div>
  )
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <p>WEEZA</p>
      <Example />
    </QueryClientProvider>
  );
}

export default App;
