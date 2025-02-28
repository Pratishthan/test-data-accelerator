import { useState } from 'react'

function useMinimise(initalState) {
  const [minimised, setMinimised] = useState(initalState);
  const minimise = () => setMinimised(true);
  const maximise = () => setMinimised(false);
  return [minimised, minimise, maximise];
}
export default useMinimise;