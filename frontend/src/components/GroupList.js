import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';

import { FormLabel, IconButton } from '@mui/material';
import useMinimise from '../hooks/useMinimised';
import StepList from "./StepList"

function Group({group, addStep, ...props}) {
  const [minimised, minimise, maximise] = useMinimise(true);
  return (
    <div className={`border-radius rounded-2`}>
      <div className='d-flex flex-row align-items-center ps-3' onClick={() => maximise()}>
        <div>
          <IconButton onClick={(e) =>  {e.stopPropagation();  minimised ? maximise() : minimise() }} >
              {minimised &&  <KeyboardArrowRightIcon fontSize='large' className='p-1 m-1' /> }
              {!minimised &&  <KeyboardArrowDownIcon fontSize='large' className='p-1 m-1' /> }
          </IconButton>
        </div> 
        <FormLabel>{group.label}</FormLabel>
      </div>
      {!minimised && <StepList steps={group.children} addStep={addStep}/>}
  </div>
  );
}

function GroupList({groups, addStep,...props}) {
  return (
    <>
      {
          groups.map((group,index) => (
            <Group group={group} addStep={addStep} key={index}/>
          )
        )
      }
    </>
  );
}

export default GroupList;