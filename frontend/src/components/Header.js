import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from "@mui/material/Button"

function Header({flowSteps, handleExport, ...props}) {
  return (
    <div className='w-100 d-flex justify-content-between ps-2 pt-2'>
        <h1>Build Your Flow</h1>
        <Button variant="contained" className='m-2' onClick={(e) => {handleExport()}}>EXPORT</Button>
    </div>
  );
}

export default Header;