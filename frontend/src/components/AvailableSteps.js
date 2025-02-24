import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import StepItem from './StepItem';

function AvailableSteps({availableSteps, handleDragStart, handleAddStep,...props}) {
  return (
    <>
        {availableSteps.map(step => (
          <StepItem step = {step} handleDragStart={handleDragStart} handleAddStep={handleAddStep}/>
        ))}
    </>
  );
}

export default AvailableSteps;