// App.jsx
import React, { useEffect, useState } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/Header';
import useFetchActionCodes from './hooks/useFetchActionCodes';
import Group from "./components/Group"
import SideBar from './components/SideBar';


const entity = [
  { id: 'ts', name: 'Target System Code'},
  { id: 'cm', name: 'Collection Method Code'},
  { id: 'tscm', name: 'TSCM Mapping' },
  { id: 'rec', name: 'Response Error Code' },
  { id: 'ce', name: 'Collection Entity' },
]

const predefinedSteps = [
  { id: 'tsSetEntityData', srNo : 1, name: 'Input Data', entityId : "ts" },
  { id: 'tsEndCreateRequest', srNo : 2, name: 'Send Create Request', entityId : "ts" },
  { id: 'cmProcessPayment', srNo : 3,name: 'Process Payment', entityId : "cm"  },
  { id: 'cmSendConfirmation',srNo : 4,name: 'Send Confirmation', entityId : "cm"  },
  { id: 'cmGenerateReport', srNo : 5,name: 'Generate Report', entityId : "cm"  },
];
const initGroupId = Math.random();


function App() {
  const [availableSteps, setAvailableSteps] = useState([]);
  const [flowSteps, setFlowSteps] = useState([]);
  const [groupSerialNumber, setGroupSerialNumber] = useState(1);
  const [groups,setGroups] = useState([{id: initGroupId, groupName: "", minimised: true, srNo: 1}])
  const [selectedGroup,setSelectedGroup] = useState(initGroupId)
  const [selectedEntity, setSelectedEntity] = useState({});
  const [totalNodesUsed, setTotalNodesUsed] = useState(1)

  const actionCodes = useFetchActionCodes();

  const handleStepDataChange = (id, key, value) => {
      const step = flowSteps.find(step => step.id === id);
      setFlowSteps([...flowSteps.filter(step=> step.id !==id), {...step, [key] : value} ])
  }

  const handleFlowDrop = (e, targetIndex) => {
    e.preventDefault();
    const stepId = e.dataTransfer.getData('stepId');
    const draggedStep = flowSteps.find(s => s.id === stepId);
    if (draggedStep) {
      const filteredSteps = flowSteps.filter(step => step.id !== draggedStep.id);
      const stepsBeforeTargetStep = filteredSteps.slice(0, targetIndex);
      const stepsAfterTargetStep = filteredSteps.slice(targetIndex);
      const updatedIndexBeforeTargetIndex = stepsBeforeTargetStep.map((step,index) => {return {...step, order : index}})
      const updatedIndexAfterTargetIndex = stepsAfterTargetStep.map((step,index) => {return {...step, order : targetIndex + 1 + index}})
      setFlowSteps([...updatedIndexBeforeTargetIndex, {...draggedStep, order : targetIndex}, ...updatedIndexAfterTargetIndex])
    }
  };

  const handleDragStart = (e, step) => {
    e.dataTransfer.setData('stepId', step.id);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    const stepId = e.dataTransfer.getData('stepId');
    const groupId =  parseFloat(e.target.id);
    const step = availableSteps.find(s => s.id === stepId);
    const group = groups.find(s => s.id === groupId);
    setSelectedGroup(group.id)
    step && setFlowSteps([...flowSteps, {...step, id : step.id + Math.random(), groupId}]);
    group && setGroups([...groups.filter(gp => gp.id !== group.id), { ...group, minimised: false}]);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleEntitySelect = (entity) => {
    setSelectedEntity(entity);
  };

  const handleFlowDragOver = (e) => {
    e.preventDefault();
  };

  const removeStep = (stepId) => {
    const removedStep = flowSteps.find(s => s.id === stepId);
    if (removedStep) {
      const filteredSteps = flowSteps.filter(step => step.id !== removedStep.id);
      const stepsBeforeTargetStep = filteredSteps.slice(0, removedStep.order);
      const stepsAfterTargetStep = filteredSteps.slice(removedStep.order);
      const updatedIndexBeforeTargetIndex = stepsBeforeTargetStep.map((step,index) => {return {...step, order : index}})
      const updatedIndexAfterTargetIndex = stepsAfterTargetStep.map((step,index) => {return {...step, order : removedStep.order + index}})
      setFlowSteps([...updatedIndexBeforeTargetIndex, ...updatedIndexAfterTargetIndex])
    }
  };

  const handleDeleteGroup = (e,groupId) => {
    e.stopPropagation()
    setFlowSteps(flowSteps.filter(step => step.groupId !== groupId));
    setGroups(groups.filter(group => group.id !== groupId))
    setSelectedGroup(null)
  };

  const handleGroupNameChange = (groupId, groupName) => {
    setGroups( [...groups.filter(group => group.id !== groupId), { ...groups.filter(group => group.id === groupId)[0], groupName}])
  };

  const handleAddGroup = () => {
    const groupId = Math.random();
    setSelectedGroup(groupId);
    setGroupSerialNumber(groupSerialNumber +1)
    setGroups([...groups, {id: groupId, groupName: "", minimised: true, srNo: groupSerialNumber +1}])
  };

  const handleSelectGroup = (groupId) => {
    setSelectedGroup(groupId)
    const group = groups.find(g => g.id === selectedGroup);
    group && setGroups([...groups.filter(gp => gp.id !== group.id), { ...group, minimised: false}]);
  };

  const handleAddStep = (step) => {
    if (!selectedGroup) {
      window.alert("select group first")
      return
    }
    setFlowSteps([...flowSteps, {...step, label: step.label + " " + totalNodesUsed , id : step.id + Math.random(), groupId : selectedGroup, order: Object.keys(flowSteps).length + 1}]);
    const group = groups.find(s => s.id === selectedGroup);
    group && setGroups([...groups.filter(gp => gp.id !== group.id), { ...group, minimised: false}]);
    setTotalNodesUsed(totalNodesUsed+1)
  };

  const handleMinimised = (e,groupId) => {
    e.stopPropagation();
    const group = groups.find(s => s.id === groupId);
    group && setGroups([...groups.filter(gp => gp.id !== groupId), { ...group, minimised: true}]);
  }

  const handleExport = () => {
    const exportData = {} 
    flowSteps
    .sort(step => step.a - step.b)
    .forEach( step => {
      exportData[step.label] = step
    })
    console.log(exportData);
  }

  useEffect(()=> {
    setSelectedEntity(entity[0]);
  },[])

  useEffect(()=> {
    const div = document.getElementById(selectedGroup);
    div.scrollTo({ top: div.scrollHeight, behavior: "smooth" });
  },[groups])

  useEffect(() => {
    setAvailableSteps(
      predefinedSteps.filter(step => step.entityId && selectedEntity && selectedEntity.id && selectedEntity.id === step.entityId ) 
    )
  }, [selectedEntity])


  return (
    <div className="app">
      <Header flowSteps={flowSteps} handleExport={handleExport}/> 
      <div className='d-flex flex-row h-webkit-height-available'>
        <SideBar actionCodes={actionCodes} addStep={handleAddStep}/>
        <div className="container">
          <div className="flow-panel">
            <h2>Your Flow</h2>
            {
                groups
                  .sort((g1,g2) => g1.srNo - g2.srNo)
                  .map((group, index) =>
                    <Group
                        key={index}
                        group={group}
                        flowSteps= {flowSteps.filter(step =>  { return step.groupId === group.id} )}
                        handleDrop={handleDrop}
                        handleDragOver={handleDragOver}
                        handleDragStart={handleDragStart}
                        handleFlowDrop={handleFlowDrop}
                        handleFlowDragOver={handleFlowDragOver}
                        removeStep={removeStep}
                        handleDeleteGroup={handleDeleteGroup}
                        handleGroupNameChange={handleGroupNameChange}
                        handleSelectGroup={handleSelectGroup}
                        handleMinimised={handleMinimised}
                        handleStepDataChange={handleStepDataChange}
                    />
                )
            }
          </div>
        </div>
      </div>

    </div>
  );
}

export default App;