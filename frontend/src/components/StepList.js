import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

function StepItem({handleDragStart, addStep, step,...props}) {
  return (
    <div
      key={step.id}
      className="d-flex flex-row justify-content-between align-items-center node-definition"
      onClick={() => addStep(step)}
    >
      {step.label}
    </div>
  );
}

function StepList({steps,addStep, ...props}) {
  return (
    <>
        {steps.map((step, index) => (
          <StepItem key={index} step = {step} addStep={addStep}/>
        ))}
    </>
  );
}

export default StepList;