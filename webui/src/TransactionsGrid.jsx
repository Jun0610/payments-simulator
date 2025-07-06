import {AgGridReact} from 'ag-grid-react';
import {colorSchemeDarkBlue, themeQuartz} from 'ag-grid-community'
import {useState} from 'react'
import { useMemo } from 'react';
import {Flex} from 'antd'


const TransactionGrid = ({mostRecentTransactions}) => {
  const [colDefs, setColDefs] = useState([
    {field : 'sender', headerName : "Sender", flex : 1},
    {field : 'receiver', headerName : "Receiver", flex : 1},
    {field : 'amount', headerName : "Amount ($)", flex : 1}
  ])

  const rowData = useMemo(() => {
    const rows = [];
      for (const t of mostRecentTransactions) {
        rows.push({
          sender : t.senderName,
          receiver : t.receiverName,
          amount : t.amount
        })
      }
      return rows;
  }, [mostRecentTransactions])

  return <div style={{height: 475, width : "45%", textAlign : "left", marginTop: 40}}>
    <Flex>
      <h3 style={{margin : "auto", marginBottom : 10}}>10 most recent transactions</h3>
    </Flex>
    
    <AgGridReact
      theme={themeQuartz.withPart(colorSchemeDarkBlue)}
      rowData={rowData}
      columnDefs={colDefs}/>
  </div>
}


export default TransactionGrid;