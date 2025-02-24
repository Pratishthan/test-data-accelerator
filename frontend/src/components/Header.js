import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

function Header({flowSteps,downloadTemplate,...props}) {
  return (
    <div className='w-100 d-flex justify-content-between margin-1'>
        <h1>Build Your Flow</h1>
    </div>
  );
}

export default Header;