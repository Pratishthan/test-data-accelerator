// App.jsx
import React, { use, useEffect, useState } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import GroupCreator from "./components/GroupCreator"
import StepList from "./components/AvailableSteps"
import Header from './components/Header';
import Group from './components/Group'

const entity = [
  { id: 'ts', name: 'Target System Code'},
  { id: 'cm', name: 'Collection Method Code'},
  { id: 'tscm', name: 'TSCM Mapping' },
  { id: 'rec', name: 'Response Error Code' },
  { id: 'ce', name: 'Collection Entity' },
]

const predefinedSteps = [
  { id: 'tsSetEntityData', srNo : 1,name: 'Input Data', entityId : "ts" },
  { id: 'tsEndCreateRequest', srNo : 2,name: 'Send Create Request', entityId : "ts" },
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

  // Handle drag start
  const handleDragStart = (e, step) => {
    e.dataTransfer.setData('stepId', step.id);
  };

  // Handle drop on flow area
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

  // Allow drop
  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleEntitySelect = (entity) => {
    console.log("Entity",entity)
    setSelectedEntity(entity);
  };

  const handleFlowDragOver = (e) => {
    e.preventDefault();
  };

  // Remove step from flow
  const removeStep = (stepId) => {
    const step = flowSteps.find(s => s.id === stepId);
    setFlowSteps(flowSteps.filter(s => s.id !== stepId));
  };

  // Remove group from flow
  const handleDeleteGroup = (e,groupId) => {
    e.stopPropagation()
    setFlowSteps(flowSteps.filter(step => step.groupId !== groupId));
    setGroups(groups.filter(group => group.id !== groupId))
    setSelectedGroup(null)
  };

  const handleGroupNameChange = (groupId, groupName) => {
    setGroups( [...groups.filter(group => group.id !== groupId), { ...groups.filter(group => group.id === groupId)[0], groupName}])
  };

  // Add group to flow
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

  const handleAddStep = (stepId) => {
    console.log(selectedGroup)
    if (!selectedGroup) {
      window.alert("select group first")
      return
    }

    const step = availableSteps.find(s => s.id === stepId);
    step &&  setFlowSteps([...flowSteps, {...step, id : step.id + Math.random(), groupId : selectedGroup}]);
    const group = groups.find(s => s.id === selectedGroup);
    group && setGroups([...groups.filter(gp => gp.id !== group.id), { ...group, minimised: false}]);
  };

  const handleMinimised = (e,groupId) => {
    e.stopPropagation();
    const group = groups.find(s => s.id === groupId);
    group && setGroups([...groups.filter(gp => gp.id !== groupId), { ...group, minimised: true}]);
  }


  useEffect(()=> {
    setSelectedEntity(entity[0]);
  },[])

  useEffect(()=> {
    console.log("FlowStepss : ", flowSteps)
    console.log( "Groups : ",groups);
  },[flowSteps, groups])

  useEffect(() => {
    setAvailableSteps(
      predefinedSteps.filter(step => step.entityId && selectedEntity && selectedEntity.id && selectedEntity.id === step.entityId )
    )
  }, [selectedEntity])

  useEffect(() => {
    console.log(selectedGroup)
  }, [selectedGroup])

  return (
    <div className="app">
      <Header flowSteps={flowSteps}/>
      <div className="container">
        <div className="steps-panel">
          <GroupCreator entity={entity} handleEntitySelect={handleEntitySelect} selectedEntity={selectedEntity} handleAddGroup={handleAddGroup}/>
          <StepList availableSteps = {availableSteps} handleDragStart = {handleDragStart} handleAddStep={handleAddStep}/>
        </div>

        <div className="flow-panel">
          <h2>Your Flow</h2>
          {
              groups
                .sort((g1,g2) => g1.srNo - g2.srNo)
                .map(group =>
                  <Group
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
                  />
              )
          }
        </div>
      </div>
    </div>
  );
}

export default App;