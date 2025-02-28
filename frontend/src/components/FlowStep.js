import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import CloseIcon from '@mui/icons-material/Close';
import {  IconButton, TextField } from '@mui/material';
import DragHandleIcon from '@mui/icons-material/DragHandle';

function FlowStep({handleDragStart, handleFlowDrop, handleFlowDragOver, removeStep, index, step, handleStepDataChange,...props}) {
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
        <div className='d-flex align-items-center'>
          <IconButton>
            <DragHandleIcon fontSize='large' className='p-1 m-1'/>
          </IconButton>
          <TextField key={step.id} defaultValue={step.label} onBlur={(e) => handleStepDataChange(step.id,"label",e.target.value)} size='small'/>
        </div>
        <IconButton onClick={(e) => removeStep(step.id)}>
            <CloseIcon fontSize='large' className='p-1 m-1'/>
        </IconButton>
    </div>
  );
}

export default FlowStep;