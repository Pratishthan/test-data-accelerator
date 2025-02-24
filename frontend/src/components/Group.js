import React , { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import FlowStep from './FlowStep';
import { TextField, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import CloseFullscreenIcon from '@mui/icons-material/CloseFullscreen';


function Group({
    handleDrop, handleDragOver, handleDragStart, handleFlowDrop, 
    handleFlowDragOver, removeStep, flowSteps,group,
    handleDeleteGroup,handleGroupNameChange, handleMinimised,
    handleSelectGroup,
    ...props
  }) {

    const handleDropItem = (e) => {
      handleDrop(e);
    }

    return (
      <div key={group.id} className={ `flow-list border border-2 border-dark-subtle p-1 m-1 border-radius rounded-2 `}      >
        <div className='group-header d-flex flex-row justify-content-between p-1'
            id={group.id}
            onDrop={handleDropItem} 
            onDragOver={handleDragOver}
            onClick={(e) => handleSelectGroup(group.id)}
        >
          <TextField id={group.id} defaultValue={group.groupName} muted label="Group Name" onBlur={(e) => {handleGroupNameChange(group.id, e.target.value)}}/>  
          <div >
            <IconButton id={group.id} onClick={(e) => handleMinimised(e,group.id)}>
                <CloseFullscreenIcon fontSize='large' className='p-1 m-1' />
            </IconButton>
            <IconButton id={group.id}  onClick={(e) => {handleDeleteGroup(e,group.id)} }>
              <DeleteIcon fontSize='large' className='p-1 m-1' />
            </IconButton>
          </div>   
        </div>
            {
                !group.minimised &&
                <div id={group.id} className='overflow-scroll'>
                {
                  flowSteps
                  .map((step, index) => 
                    <FlowStep
                      id={group.id}
                      key={step.id}
                      step={step}
                      index={index}
                      className="flow-step"
                      draggable
                      handleDragStart={handleDragStart}
                      handleFlowDrop={handleFlowDrop}
                      handleFlowDragOver={handleFlowDragOver}
                      removeStep={removeStep}
                      {...props}
                    />
                )}
                </div>  
            }
    </div>
  );
}

export default Group;