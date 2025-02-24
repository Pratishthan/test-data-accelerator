import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { IconButton } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';


function Step({handleDragStart, handleAddStep, step,...props}) {

  return (
    <div
      key={step.id}
      className="step-item d-flex flex-row justify-content-between"
      draggable
      onDragStart={(e) => handleDragStart(e, step)}
    >
      {step.name}
      <IconButton color='#000' onClick={() => handleAddStep(step.id)}><AddIcon/></IconButton>
    </div>
  );
}

export default Step;