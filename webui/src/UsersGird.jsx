import {AgGridReact} from 'ag-grid-react';
import {colorSchemeDarkBlue, themeQuartz} from 'ag-grid-community'
import {useState} from 'react'
import { useMemo } from 'react';
import {Flex} from 'antd'


const UsersGrid = ({users}) => {
  const [colDefs, setColDefs] = useState([
    {field : 'user', headerName : "User", flex : 1},
    {field : 'balance', headerName : "Balance ($)", flex : 1}
  ])

  const rowData = useMemo(() => {
    const rows = [];
      for (const name in users) {
        rows.push({
          user : name,
          balance : users[name]
        })
      }
      return rows;
  }, [users])

  return <div style={{height: 475, width : "45%", textAlign : "left", marginTop: 40}}>
    <Flex>
      <h3 style={{margin : "auto", marginBottom : 10}}>User Balances</h3>
    </Flex>
    
    <AgGridReact
      theme={themeQuartz.withPart(colorSchemeDarkBlue)}
      rowData={rowData}
      columnDefs={colDefs}/>
  </div>
}


export default UsersGrid;