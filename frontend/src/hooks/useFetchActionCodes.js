import { useEffect, useState } from 'react'
import urlConfig from '../consts/url-config'




let backendActionCodes = [
  {
    code: "Demand Code",
    type: "TableMapper",
    actionCodeGroupName: "Setup",
    columns: [
      "columnY",
      "columnZ"
    ],
    defaultData: [],
    component : "Collection"
  },
  {
    code: "Collectable Event",
    type: "TableMapper",
    actionCodeGroupName : "Event",
    columns: [
      "columnY",
      "columnZ"
    ],
    defaultData: [],
    component : "Collection"
  },
  {
    code: "Billing Code",
    type: "TableMapper",
    actionCodeGroupName: "Setup",
    columns: [
      "columnY",
      "columnZ"
    ],
    defaultData: [],
    component : "Billing"
  },
  {
    code: "Billable Event",
    type: "TableMapper",
    actionCodeGroupName : "Setup",
    columns: [
      "columnY",
      "columnZ"
    ],
    defaultData: [],
    component : "Billing"
  }
]

const fetchData =  async (setActionCodes) => {
  const url = urlConfig['action-code'];
   try {
     const response = await fetch(url, {method: "get"});
     if (!response.ok) {
       throw new Error(`Response status: ${response.status}`);
     }
     const actionCodes = await response.json();
     setActionCodes(processActionCodes(actionCodes));
   } catch (error) {
     alert(error.message);
   }
}


function useFetchActionCodes() {

  const [actionCodes, setActionCodes] = useState([])
    useEffect(() => {  
     const data = fetchData(setActionCodes);
    },[])
    return actionCodes;
}


const mapActionCodes = (actionCodes, componentIndex) => {
  let actionCodesGroupedByGroupName = Object.groupBy(actionCodes, ({ actionCodeGroupName }) => actionCodeGroupName);
  return Object.keys(actionCodesGroupedByGroupName)
  .map((groupName,groupIndex) => {
    let actionCodes = actionCodesGroupedByGroupName[groupName];    
    return {
      id : componentIndex + '' + groupIndex,
      label : groupName,
      key : componentIndex + '' + groupIndex,
      children: actionCodes.map((actionCode, actionCodeIndex) => {
        return {...actionCode,  id: componentIndex + '' + groupIndex + '' + actionCodeIndex, label: actionCode.code, key:componentIndex + '' + groupIndex + '' + actionCodeIndex};
      })
    }
  });
}

const processActionCodes = (actionCodes) => {
  let actionCodesGroupedByComponent = Object.groupBy(actionCodes, ({ componentName }) => componentName);
  return Object.keys(actionCodesGroupedByComponent)
  .map((componentName,componentIndex) => {
    let actionCodes = actionCodesGroupedByComponent[componentName];    
    return {
      id : componentIndex,
      label : componentName,
      key : componentIndex,
      children: mapActionCodes(actionCodes, componentIndex)
    }
  });
}


export default useFetchActionCodes;