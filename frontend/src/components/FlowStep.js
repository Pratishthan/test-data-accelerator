import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import CloseIcon from '@mui/icons-material/Close';
import {  IconButton } from '@mui/material';

function FlowStep({handleDragStart, handleFlowDrop, handleFlowDragOver, removeStep, index, step,...props}) {
  return (
    <div
        key={step.id}
        className="child"
        draggable
        onDragStart={(e) => handleDragStart(e, step)}
        onDrop={(e) => handleFlowDrop(e, index)}
        onDragOver={handleFlowDragOver}
        {...props}
        >
        <span>{index + 1}. {step.name}</span>

        <IconButton onClick={(e) => removeStep(step.id)}>
            <CloseIcon fontSize='large' className='p-1 m-1'/>
        </IconButton>
    </div>
  );
}

export default FlowStep;