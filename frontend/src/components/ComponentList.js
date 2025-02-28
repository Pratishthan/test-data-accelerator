import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import {  IconButton } from '@mui/material';
import GroupList from './GroupList';
import {FormLabel} from '@mui/material';
import useMinimise from '../hooks/useMinimised';

function Component({component, addStep, ...props}) {
  const [minimised, minimise, maximise] = useMinimise(true);
  return (
    <div className={ `border-radius rounded-2`}>
      <div className='d-flex flex-row align-items-center' onClick={() => maximise()}>
        <div>
          <IconButton onClick={(e) =>  {e.stopPropagation();  minimised ? maximise() : minimise() }} >
              {minimised &&  <KeyboardArrowRightIcon fontSize='large' className='p-1 m-1' /> }
              {!minimised &&  <KeyboardArrowDownIcon fontSize='large' className='p-1 m-1' /> }
          </IconButton>
        </div> 
        <FormLabel>{component.label}</FormLabel>
      </div>
      {!minimised && <GroupList groups={component.children} addStep={addStep}/>}
  </div>
  );
}

function ComponentList({components, addStep,...props}) {
  return (
    <>
      {
          components.map((component,index) => (
            <Component component={component} addStep={addStep} key={index}/>
          )
        )
      }
    </>
  );
}

export default ComponentList;