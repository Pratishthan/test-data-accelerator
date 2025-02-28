import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import ComponentList from './ComponentList';
import useMinimise from '../hooks/useMinimised';

function SideBar({actionCodes, addStep,...props}) {
  const [minimised, minimise, maximise] = useMinimise(false);
  return (
    <> 
        {
           !minimised && 
           <div className="steps-panel">
            <ComponentList components={actionCodes} addStep={addStep}/>
          </div>
        }
        {
          minimised &&
          <div className="steps-panel-minimised">
            <span>open</span>
          </div>  
        }
    </>
  );
}

export default SideBar;