import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Dropdown from 'react-bootstrap/Dropdown';
import AddIcon from '@mui/icons-material/Add';
import { Button } from '@mui/material';



function GroupCreator({handleEntitySelect, selectedEntity, entity, handleAddGroup,...props}) {

  return (
    <div key="GroupCreatoe" className='d-flex justify-content-between w-100'>
            <Dropdown>
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                {selectedEntity && selectedEntity.name}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {
                  entity
                  .map( (item, index) => 
                    <Dropdown.Item key={index} href="#/action-1" onClick={() => handleEntitySelect(item)}>{item.name}</Dropdown.Item>
                  )
                }
              </Dropdown.Menu>
            </Dropdown>
            <div>
            <Button color='#000' endIcon={ <AddIcon/> } onClick={handleAddGroup}>
              Group
            </Button>
            </div>
            
          </div>
  );
}

export default GroupCreator;