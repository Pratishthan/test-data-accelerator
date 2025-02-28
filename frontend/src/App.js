// App.jsx
import React, { useState } from 'react';
import './App.css';

const predefinedSteps = [
  { id: 'step1', name: 'Input Data' },
  { id: 'step2', name: 'Validate Data' },
  { id: 'step3', name: 'Process Payment' },
  { id: 'step4', name: 'Send Confirmation' },
  { id: 'step5', name: 'Generate Report' },
];

function App() {
  const [availableSteps, setAvailableSteps] = useState(predefinedSteps);
  const [flowSteps, setFlowSteps] = useState([]);

  // Handle drag start
  const handleDragStart = (e, step) => {
    e.dataTransfer.setData('stepId', step.id);
  };

  // Handle drop on flow area
  const handleDrop = (e) => {
    e.preventDefault();
    const stepId = e.dataTransfer.getData('stepId');
    const step = availableSteps.find(s => s.id === stepId);
    
    if (step && !flowSteps.some(s => s.id === stepId)) {
      setFlowSteps([...flowSteps, step]);
      setAvailableSteps(availableSteps.filter(s => s.id !== stepId));
    }
  };

  // Allow drop
  const handleDragOver = (e) => {
    e.preventDefault();
  };

  // Handle reordering within flow
  const handleFlowDragOver = (e) => {
    e.preventDefault();
  };

  const handleFlowDrop = (e, targetIndex) => {
    e.preventDefault();
    const stepId = e.dataTransfer.getData('stepId');
    const draggedStep = flowSteps.find(s => s.id === stepId);
    
    if (draggedStep) {
      const newFlow = flowSteps.filter(s => s.id !== stepId);
      newFlow.splice(targetIndex, 0, draggedStep);
      setFlowSteps(newFlow);
    }
  };

  // Remove step from flow
  const removeStep = (stepId) => {
    const step = flowSteps.find(s => s.id === stepId);
    setFlowSteps(flowSteps.filter(s => s.id !== stepId));
    setAvailableSteps([...availableSteps, step]);
  };

  // Download template
  const downloadTemplate = async () => {
    try {
      const stepsOrder = flowSteps.map(step => ({
        id: step.id,
        name: step.name
      }));

      const response = await fetch('http://localhost:8080/', {
        method: 'POST',
        mode: 'cors', // Explicitly set CORS mode
        credentials: 'same-origin', // or 'include' if sending cookies
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ steps: stepsOrder }),
      });

      if (!response.ok) {
        throw new Error('API request failed');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'template.xlsx'; // or whatever file type your API returns
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error downloading template:', error);
      alert('Failed to download template');
    }
  };

  return (
    <div className="app">
      <h1>Build Your Flow</h1>
      
      <div className="container">
        <div className="steps-panel">
          <h2>Available Steps</h2>
          <div className="steps-list">
            {availableSteps.map(step => (
              <div
                key={step.id}
                className="step-item"
                draggable
                onDragStart={(e) => handleDragStart(e, step)}
              >
                {step.name}
              </div>
            ))}
          </div>
        </div>

        <div
          className="flow-panel"
          onDrop={handleDrop}
          onDragOver={handleDragOver}
        >
          <h2>Your Flow</h2>
          <div className="flow-list">
            {flowSteps.map((step, index) => (
              <div
                key={step.id}
                className="flow-step"
                draggable
                onDragStart={(e) => handleDragStart(e, step)}
                onDrop={(e) => handleFlowDrop(e, index)}
                onDragOver={handleFlowDragOver}
              >
                <span>{index + 1}. {step.name}</span>
                <button onClick={() => removeStep(step.id)}>×</button>
              </div>
            ))}
            {flowSteps.length === 0 && (
              <p className="empty-message">Drag steps here to build your flow</p>
            )}
          </div>
        </div>
      </div>

      <button
        className="download-btn"
        onClick={downloadTemplate}
        disabled={flowSteps.length === 0}
      >
        Download Template
      </button>
    </div>
  );
}

export default App;